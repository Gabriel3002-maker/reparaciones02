package ec.gob.loja.reparaciones.repository;

import ec.gob.loja.reparaciones.domain.Material;
import ec.gob.loja.reparaciones.repository.rowmapper.CatalogoItemRowMapper;
import ec.gob.loja.reparaciones.repository.rowmapper.MaterialDanioRowMapper;
import ec.gob.loja.reparaciones.repository.rowmapper.MaterialReporteControlRowMapper;
import ec.gob.loja.reparaciones.repository.rowmapper.MaterialRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the Material entity.
 */
@SuppressWarnings("unused")
class MaterialRepositoryInternalImpl extends SimpleR2dbcRepository<Material, Long> implements MaterialRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final CatalogoItemRowMapper catalogoitemMapper;
    private final MaterialDanioRowMapper materialdanioMapper;
    private final MaterialReporteControlRowMapper materialreportecontrolMapper;
    private final MaterialRowMapper materialMapper;

    private static final Table entityTable = Table.aliased("material", EntityManager.ENTITY_ALIAS);
    private static final Table categoriaTable = Table.aliased("catalogo_item", "categoria");
    private static final Table materialDanioTable = Table.aliased("material_danio", "materialDanio");
    private static final Table materialReporteControlTable = Table.aliased("material_reporte_control", "materialReporteControl");

    public MaterialRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        CatalogoItemRowMapper catalogoitemMapper,
        MaterialDanioRowMapper materialdanioMapper,
        MaterialReporteControlRowMapper materialreportecontrolMapper,
        MaterialRowMapper materialMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Material.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.catalogoitemMapper = catalogoitemMapper;
        this.materialdanioMapper = materialdanioMapper;
        this.materialreportecontrolMapper = materialreportecontrolMapper;
        this.materialMapper = materialMapper;
    }

    @Override
    public Flux<Material> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Material> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = MaterialSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(CatalogoItemSqlHelper.getColumns(categoriaTable, "categoria"));
        columns.addAll(MaterialDanioSqlHelper.getColumns(materialDanioTable, "materialDanio"));
        columns.addAll(MaterialReporteControlSqlHelper.getColumns(materialReporteControlTable, "materialReporteControl"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(categoriaTable)
            .on(Column.create("categoria_id", entityTable))
            .equals(Column.create("id", categoriaTable))
            .leftOuterJoin(materialDanioTable)
            .on(Column.create("material_danio_id", entityTable))
            .equals(Column.create("id", materialDanioTable))
            .leftOuterJoin(materialReporteControlTable)
            .on(Column.create("material_reporte_control_id", entityTable))
            .equals(Column.create("id", materialReporteControlTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Material.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Material> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Material> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<Material> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<Material> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<Material> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private Material process(Row row, RowMetadata metadata) {
        Material entity = materialMapper.apply(row, "e");
        entity.setCategoria(catalogoitemMapper.apply(row, "categoria"));
        entity.setMaterialDanio(materialdanioMapper.apply(row, "materialDanio"));
        entity.setMaterialReporteControl(materialreportecontrolMapper.apply(row, "materialReporteControl"));
        return entity;
    }

    @Override
    public <S extends Material> Mono<S> save(S entity) {
        return super.save(entity);
    }
}

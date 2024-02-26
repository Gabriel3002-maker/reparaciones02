package ec.gob.loja.reparaciones.repository;

import ec.gob.loja.reparaciones.domain.DetalleDanio;
import ec.gob.loja.reparaciones.repository.rowmapper.CatalogoItemRowMapper;
import ec.gob.loja.reparaciones.repository.rowmapper.DetalleDanioRowMapper;
import ec.gob.loja.reparaciones.repository.rowmapper.DetalleReporteDanioRowMapper;
import ec.gob.loja.reparaciones.repository.rowmapper.MachineryRowMapper;
import ec.gob.loja.reparaciones.repository.rowmapper.MaterialDanioRowMapper;
import ec.gob.loja.reparaciones.repository.rowmapper.OrdenBodegaRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the DetalleDanio entity.
 */
@SuppressWarnings("unused")
class DetalleDanioRepositoryInternalImpl extends SimpleR2dbcRepository<DetalleDanio, Long> implements DetalleDanioRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final CatalogoItemRowMapper catalogoitemMapper;
    private final MaterialDanioRowMapper materialdanioMapper;
    private final MachineryRowMapper machineryMapper;
    private final OrdenBodegaRowMapper ordenbodegaMapper;
    private final DetalleReporteDanioRowMapper detallereportedanioMapper;
    private final DetalleDanioRowMapper detalledanioMapper;

    private static final Table entityTable = Table.aliased("detalle_danio", EntityManager.ENTITY_ALIAS);
    private static final Table tipoDanioTable = Table.aliased("catalogo_item", "tipoDanio");
    private static final Table materialDanioTable = Table.aliased("material_danio", "materialDanio");
    private static final Table maquinariaTable = Table.aliased("machinery", "maquinaria");
    private static final Table ordenBodegaTable = Table.aliased("orden_bodega", "ordenBodega");
    private static final Table detalleReporteDanioTable = Table.aliased("detalle_reporte_danio", "detalleReporteDanio");

    public DetalleDanioRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        CatalogoItemRowMapper catalogoitemMapper,
        MaterialDanioRowMapper materialdanioMapper,
        MachineryRowMapper machineryMapper,
        OrdenBodegaRowMapper ordenbodegaMapper,
        DetalleReporteDanioRowMapper detallereportedanioMapper,
        DetalleDanioRowMapper detalledanioMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(DetalleDanio.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.catalogoitemMapper = catalogoitemMapper;
        this.materialdanioMapper = materialdanioMapper;
        this.machineryMapper = machineryMapper;
        this.ordenbodegaMapper = ordenbodegaMapper;
        this.detallereportedanioMapper = detallereportedanioMapper;
        this.detalledanioMapper = detalledanioMapper;
    }

    @Override
    public Flux<DetalleDanio> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<DetalleDanio> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = DetalleDanioSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(CatalogoItemSqlHelper.getColumns(tipoDanioTable, "tipoDanio"));
        columns.addAll(MaterialDanioSqlHelper.getColumns(materialDanioTable, "materialDanio"));
        columns.addAll(MachinerySqlHelper.getColumns(maquinariaTable, "maquinaria"));
        columns.addAll(OrdenBodegaSqlHelper.getColumns(ordenBodegaTable, "ordenBodega"));
        columns.addAll(DetalleReporteDanioSqlHelper.getColumns(detalleReporteDanioTable, "detalleReporteDanio"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(tipoDanioTable)
            .on(Column.create("tipo_danio_id", entityTable))
            .equals(Column.create("id", tipoDanioTable))
            .leftOuterJoin(materialDanioTable)
            .on(Column.create("material_danio_id", entityTable))
            .equals(Column.create("id", materialDanioTable))
            .leftOuterJoin(maquinariaTable)
            .on(Column.create("maquinaria_id", entityTable))
            .equals(Column.create("id", maquinariaTable))
            .leftOuterJoin(ordenBodegaTable)
            .on(Column.create("orden_bodega_id", entityTable))
            .equals(Column.create("id", ordenBodegaTable))
            .leftOuterJoin(detalleReporteDanioTable)
            .on(Column.create("detalle_reporte_danio_id", entityTable))
            .equals(Column.create("id", detalleReporteDanioTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, DetalleDanio.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<DetalleDanio> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<DetalleDanio> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<DetalleDanio> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<DetalleDanio> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<DetalleDanio> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private DetalleDanio process(Row row, RowMetadata metadata) {
        DetalleDanio entity = detalledanioMapper.apply(row, "e");
        entity.setTipoDanio(catalogoitemMapper.apply(row, "tipoDanio"));
        entity.setMaterialDanio(materialdanioMapper.apply(row, "materialDanio"));
        entity.setMaquinaria(machineryMapper.apply(row, "maquinaria"));
        entity.setOrdenBodega(ordenbodegaMapper.apply(row, "ordenBodega"));
        entity.setDetalleReporteDanio(detallereportedanioMapper.apply(row, "detalleReporteDanio"));
        return entity;
    }

    @Override
    public <S extends DetalleDanio> Mono<S> save(S entity) {
        return super.save(entity);
    }
}

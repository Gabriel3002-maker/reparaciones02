package ec.gob.loja.reparaciones.repository;

import ec.gob.loja.reparaciones.domain.DetalleReporteDanio;
import ec.gob.loja.reparaciones.repository.rowmapper.DetalleReporteDanioRowMapper;
import ec.gob.loja.reparaciones.repository.rowmapper.MaterialReporteControlRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the DetalleReporteDanio entity.
 */
@SuppressWarnings("unused")
class DetalleReporteDanioRepositoryInternalImpl
    extends SimpleR2dbcRepository<DetalleReporteDanio, Long>
    implements DetalleReporteDanioRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final MaterialReporteControlRowMapper materialreportecontrolMapper;
    private final DetalleReporteDanioRowMapper detallereportedanioMapper;

    private static final Table entityTable = Table.aliased("detalle_reporte_danio", EntityManager.ENTITY_ALIAS);
    private static final Table materialReporteTable = Table.aliased("material_reporte_control", "materialReporte");

    public DetalleReporteDanioRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        MaterialReporteControlRowMapper materialreportecontrolMapper,
        DetalleReporteDanioRowMapper detallereportedanioMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(DetalleReporteDanio.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.materialreportecontrolMapper = materialreportecontrolMapper;
        this.detallereportedanioMapper = detallereportedanioMapper;
    }

    @Override
    public Flux<DetalleReporteDanio> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<DetalleReporteDanio> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = DetalleReporteDanioSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(MaterialReporteControlSqlHelper.getColumns(materialReporteTable, "materialReporte"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(materialReporteTable)
            .on(Column.create("material_reporte_id", entityTable))
            .equals(Column.create("id", materialReporteTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, DetalleReporteDanio.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<DetalleReporteDanio> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<DetalleReporteDanio> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<DetalleReporteDanio> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<DetalleReporteDanio> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<DetalleReporteDanio> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private DetalleReporteDanio process(Row row, RowMetadata metadata) {
        DetalleReporteDanio entity = detallereportedanioMapper.apply(row, "e");
        entity.setMaterialReporte(materialreportecontrolMapper.apply(row, "materialReporte"));
        return entity;
    }

    @Override
    public <S extends DetalleReporteDanio> Mono<S> save(S entity) {
        return super.save(entity);
    }
}

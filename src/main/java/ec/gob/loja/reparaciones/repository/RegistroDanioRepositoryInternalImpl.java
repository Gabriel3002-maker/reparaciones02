package ec.gob.loja.reparaciones.repository;

import ec.gob.loja.reparaciones.domain.RegistroDanio;
import ec.gob.loja.reparaciones.repository.rowmapper.DetalleDanioRowMapper;
import ec.gob.loja.reparaciones.repository.rowmapper.RegistroDanioRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the RegistroDanio entity.
 */
@SuppressWarnings("unused")
class RegistroDanioRepositoryInternalImpl extends SimpleR2dbcRepository<RegistroDanio, Long> implements RegistroDanioRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final DetalleDanioRowMapper detalledanioMapper;
    private final RegistroDanioRowMapper registrodanioMapper;

    private static final Table entityTable = Table.aliased("registro_danio", EntityManager.ENTITY_ALIAS);
    private static final Table detalleDanioTable = Table.aliased("detalle_danio", "detalleDanio");

    public RegistroDanioRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        DetalleDanioRowMapper detalledanioMapper,
        RegistroDanioRowMapper registrodanioMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(RegistroDanio.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.detalledanioMapper = detalledanioMapper;
        this.registrodanioMapper = registrodanioMapper;
    }

    @Override
    public Flux<RegistroDanio> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<RegistroDanio> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = RegistroDanioSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(DetalleDanioSqlHelper.getColumns(detalleDanioTable, "detalleDanio"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(detalleDanioTable)
            .on(Column.create("detalle_danio_id", entityTable))
            .equals(Column.create("id", detalleDanioTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, RegistroDanio.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<RegistroDanio> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<RegistroDanio> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<RegistroDanio> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<RegistroDanio> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<RegistroDanio> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private RegistroDanio process(Row row, RowMetadata metadata) {
        RegistroDanio entity = registrodanioMapper.apply(row, "e");
        entity.setDetalleDanio(detalledanioMapper.apply(row, "detalleDanio"));
        return entity;
    }

    @Override
    public <S extends RegistroDanio> Mono<S> save(S entity) {
        return super.save(entity);
    }
}

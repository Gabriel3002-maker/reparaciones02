package ec.gob.loja.reparaciones.repository;

import ec.gob.loja.reparaciones.domain.Machinery;
import ec.gob.loja.reparaciones.repository.rowmapper.CatalogoItemRowMapper;
import ec.gob.loja.reparaciones.repository.rowmapper.MachineryRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the Machinery entity.
 */
@SuppressWarnings("unused")
class MachineryRepositoryInternalImpl extends SimpleR2dbcRepository<Machinery, Long> implements MachineryRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final CatalogoItemRowMapper catalogoitemMapper;
    private final MachineryRowMapper machineryMapper;

    private static final Table entityTable = Table.aliased("machinery", EntityManager.ENTITY_ALIAS);
    private static final Table nombreTable = Table.aliased("catalogo_item", "nombre");

    public MachineryRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        CatalogoItemRowMapper catalogoitemMapper,
        MachineryRowMapper machineryMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Machinery.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.catalogoitemMapper = catalogoitemMapper;
        this.machineryMapper = machineryMapper;
    }

    @Override
    public Flux<Machinery> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Machinery> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = MachinerySqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(CatalogoItemSqlHelper.getColumns(nombreTable, "nombre"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(nombreTable)
            .on(Column.create("nombre_id", entityTable))
            .equals(Column.create("id", nombreTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Machinery.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Machinery> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Machinery> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<Machinery> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<Machinery> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<Machinery> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private Machinery process(Row row, RowMetadata metadata) {
        Machinery entity = machineryMapper.apply(row, "e");
        entity.setNombre(catalogoitemMapper.apply(row, "nombre"));
        return entity;
    }

    @Override
    public <S extends Machinery> Mono<S> save(S entity) {
        return super.save(entity);
    }
}

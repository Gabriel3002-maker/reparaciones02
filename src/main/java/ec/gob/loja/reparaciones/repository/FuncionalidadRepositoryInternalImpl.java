package ec.gob.loja.reparaciones.repository;

import ec.gob.loja.reparaciones.domain.Funcionalidad;
import ec.gob.loja.reparaciones.repository.rowmapper.FuncionalidadRowMapper;
import ec.gob.loja.reparaciones.repository.rowmapper.FuncionalidadRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the Funcionalidad entity.
 */
@SuppressWarnings("unused")
class FuncionalidadRepositoryInternalImpl extends SimpleR2dbcRepository<Funcionalidad, Long> implements FuncionalidadRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final FuncionalidadRowMapper funcionalidadMapper;

    private static final Table entityTable = Table.aliased("funcionalidad", EntityManager.ENTITY_ALIAS);
    private static final Table padreTable = Table.aliased("funcionalidad", "padre");

    public FuncionalidadRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        FuncionalidadRowMapper funcionalidadMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Funcionalidad.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.funcionalidadMapper = funcionalidadMapper;
    }

    @Override
    public Flux<Funcionalidad> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Funcionalidad> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = FuncionalidadSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(FuncionalidadSqlHelper.getColumns(padreTable, "padre"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(padreTable)
            .on(Column.create("padre_id", entityTable))
            .equals(Column.create("id", padreTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Funcionalidad.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Funcionalidad> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Funcionalidad> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private Funcionalidad process(Row row, RowMetadata metadata) {
        Funcionalidad entity = funcionalidadMapper.apply(row, "e");
        entity.setPadre(funcionalidadMapper.apply(row, "padre"));
        return entity;
    }

    @Override
    public <S extends Funcionalidad> Mono<S> save(S entity) {
        return super.save(entity);
    }
}

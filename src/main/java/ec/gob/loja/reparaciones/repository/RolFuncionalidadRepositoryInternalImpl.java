package ec.gob.loja.reparaciones.repository;

import ec.gob.loja.reparaciones.domain.RolFuncionalidad;
import ec.gob.loja.reparaciones.repository.rowmapper.FuncionalidadRowMapper;
import ec.gob.loja.reparaciones.repository.rowmapper.RolFuncionalidadRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the RolFuncionalidad entity.
 */
@SuppressWarnings("unused")
class RolFuncionalidadRepositoryInternalImpl
    extends SimpleR2dbcRepository<RolFuncionalidad, Long>
    implements RolFuncionalidadRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final FuncionalidadRowMapper funcionalidadMapper;
    private final RolFuncionalidadRowMapper rolfuncionalidadMapper;

    private static final Table entityTable = Table.aliased("rol_funcionalidad", EntityManager.ENTITY_ALIAS);
    private static final Table funcionalidadTable = Table.aliased("funcionalidad", "funcionalidad");

    public RolFuncionalidadRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        FuncionalidadRowMapper funcionalidadMapper,
        RolFuncionalidadRowMapper rolfuncionalidadMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(RolFuncionalidad.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.funcionalidadMapper = funcionalidadMapper;
        this.rolfuncionalidadMapper = rolfuncionalidadMapper;
    }

    @Override
    public Flux<RolFuncionalidad> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<RolFuncionalidad> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = RolFuncionalidadSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(FuncionalidadSqlHelper.getColumns(funcionalidadTable, "funcionalidad"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(funcionalidadTable)
            .on(Column.create("funcionalidad_id", entityTable))
            .equals(Column.create("id", funcionalidadTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, RolFuncionalidad.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<RolFuncionalidad> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<RolFuncionalidad> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<RolFuncionalidad> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<RolFuncionalidad> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<RolFuncionalidad> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private RolFuncionalidad process(Row row, RowMetadata metadata) {
        RolFuncionalidad entity = rolfuncionalidadMapper.apply(row, "e");
        entity.setFuncionalidad(funcionalidadMapper.apply(row, "funcionalidad"));
        return entity;
    }

    @Override
    public <S extends RolFuncionalidad> Mono<S> save(S entity) {
        return super.save(entity);
    }
}

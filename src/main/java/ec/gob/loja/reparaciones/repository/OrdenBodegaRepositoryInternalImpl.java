package ec.gob.loja.reparaciones.repository;

import ec.gob.loja.reparaciones.domain.OrdenBodega;
import ec.gob.loja.reparaciones.repository.rowmapper.OrdenBodegaRowMapper;
import ec.gob.loja.reparaciones.repository.rowmapper.PersonaRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the OrdenBodega entity.
 */
@SuppressWarnings("unused")
class OrdenBodegaRepositoryInternalImpl extends SimpleR2dbcRepository<OrdenBodega, Long> implements OrdenBodegaRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final PersonaRowMapper personaMapper;
    private final OrdenBodegaRowMapper ordenbodegaMapper;

    private static final Table entityTable = Table.aliased("orden_bodega", EntityManager.ENTITY_ALIAS);
    private static final Table receptorTable = Table.aliased("persona", "receptor");

    public OrdenBodegaRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        PersonaRowMapper personaMapper,
        OrdenBodegaRowMapper ordenbodegaMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(OrdenBodega.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.personaMapper = personaMapper;
        this.ordenbodegaMapper = ordenbodegaMapper;
    }

    @Override
    public Flux<OrdenBodega> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<OrdenBodega> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = OrdenBodegaSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(PersonaSqlHelper.getColumns(receptorTable, "receptor"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(receptorTable)
            .on(Column.create("receptor_id", entityTable))
            .equals(Column.create("id", receptorTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, OrdenBodega.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<OrdenBodega> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<OrdenBodega> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<OrdenBodega> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<OrdenBodega> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<OrdenBodega> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private OrdenBodega process(Row row, RowMetadata metadata) {
        OrdenBodega entity = ordenbodegaMapper.apply(row, "e");
        entity.setReceptor(personaMapper.apply(row, "receptor"));
        return entity;
    }

    @Override
    public <S extends OrdenBodega> Mono<S> save(S entity) {
        return super.save(entity);
    }
}

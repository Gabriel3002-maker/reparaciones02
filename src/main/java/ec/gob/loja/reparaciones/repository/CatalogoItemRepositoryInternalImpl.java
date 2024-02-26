package ec.gob.loja.reparaciones.repository;

import ec.gob.loja.reparaciones.domain.CatalogoItem;
import ec.gob.loja.reparaciones.repository.rowmapper.CatalogoItemRowMapper;
import ec.gob.loja.reparaciones.repository.rowmapper.CatalogoRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the CatalogoItem entity.
 */
@SuppressWarnings("unused")
class CatalogoItemRepositoryInternalImpl extends SimpleR2dbcRepository<CatalogoItem, Long> implements CatalogoItemRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final CatalogoRowMapper catalogoMapper;
    private final CatalogoItemRowMapper catalogoitemMapper;

    private static final Table entityTable = Table.aliased("catalogo_item", EntityManager.ENTITY_ALIAS);
    private static final Table catalogoTable = Table.aliased("catalogo", "catalogo");

    public CatalogoItemRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        CatalogoRowMapper catalogoMapper,
        CatalogoItemRowMapper catalogoitemMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(CatalogoItem.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.catalogoMapper = catalogoMapper;
        this.catalogoitemMapper = catalogoitemMapper;
    }

    @Override
    public Flux<CatalogoItem> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<CatalogoItem> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = CatalogoItemSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(CatalogoSqlHelper.getColumns(catalogoTable, "catalogo"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(catalogoTable)
            .on(Column.create("catalogo_id", entityTable))
            .equals(Column.create("id", catalogoTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, CatalogoItem.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<CatalogoItem> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<CatalogoItem> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private CatalogoItem process(Row row, RowMetadata metadata) {
        CatalogoItem entity = catalogoitemMapper.apply(row, "e");
        entity.setCatalogo(catalogoMapper.apply(row, "catalogo"));
        return entity;
    }

    @Override
    public <S extends CatalogoItem> Mono<S> save(S entity) {
        return super.save(entity);
    }
}

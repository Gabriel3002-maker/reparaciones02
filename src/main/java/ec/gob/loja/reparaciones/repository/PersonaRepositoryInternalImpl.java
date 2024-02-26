package ec.gob.loja.reparaciones.repository;

import ec.gob.loja.reparaciones.domain.Persona;
import ec.gob.loja.reparaciones.repository.rowmapper.CatalogoItemRowMapper;
import ec.gob.loja.reparaciones.repository.rowmapper.PersonaRowMapper;
import ec.gob.loja.reparaciones.repository.rowmapper.UserRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the Persona entity.
 */
@SuppressWarnings("unused")
class PersonaRepositoryInternalImpl extends SimpleR2dbcRepository<Persona, Long> implements PersonaRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final CatalogoItemRowMapper catalogoitemMapper;
    private final UserRowMapper userMapper;
    private final PersonaRowMapper personaMapper;

    private static final Table entityTable = Table.aliased("persona", EntityManager.ENTITY_ALIAS);
    private static final Table tipoIdentificacionTable = Table.aliased("catalogo_item", "tipoIdentificacion");
    private static final Table usuarioTable = Table.aliased("jhi_user", "usuario");

    public PersonaRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        CatalogoItemRowMapper catalogoitemMapper,
        UserRowMapper userMapper,
        PersonaRowMapper personaMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Persona.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.catalogoitemMapper = catalogoitemMapper;
        this.userMapper = userMapper;
        this.personaMapper = personaMapper;
    }

    @Override
    public Flux<Persona> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Persona> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = PersonaSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(CatalogoItemSqlHelper.getColumns(tipoIdentificacionTable, "tipoIdentificacion"));
        columns.addAll(UserSqlHelper.getColumns(usuarioTable, "usuario"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(tipoIdentificacionTable)
            .on(Column.create("tipo_identificacion_id", entityTable))
            .equals(Column.create("id", tipoIdentificacionTable))
            .leftOuterJoin(usuarioTable)
            .on(Column.create("usuario_id", entityTable))
            .equals(Column.create("id", usuarioTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Persona.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Persona> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Persona> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<Persona> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<Persona> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<Persona> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private Persona process(Row row, RowMetadata metadata) {
        Persona entity = personaMapper.apply(row, "e");
        entity.setTipoIdentificacion(catalogoitemMapper.apply(row, "tipoIdentificacion"));
        entity.setUsuario(userMapper.apply(row, "usuario"));
        return entity;
    }

    @Override
    public <S extends Persona> Mono<S> save(S entity) {
        return super.save(entity);
    }
}

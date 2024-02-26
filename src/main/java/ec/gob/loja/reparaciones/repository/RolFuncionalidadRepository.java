package ec.gob.loja.reparaciones.repository;

import ec.gob.loja.reparaciones.domain.RolFuncionalidad;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the RolFuncionalidad entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RolFuncionalidadRepository extends ReactiveCrudRepository<RolFuncionalidad, Long>, RolFuncionalidadRepositoryInternal {
    @Override
    Mono<RolFuncionalidad> findOneWithEagerRelationships(Long id);

    @Override
    Flux<RolFuncionalidad> findAllWithEagerRelationships();

    @Override
    Flux<RolFuncionalidad> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM rol_funcionalidad entity WHERE entity.funcionalidad_id = :id")
    Flux<RolFuncionalidad> findByFuncionalidad(Long id);

    @Query("SELECT * FROM rol_funcionalidad entity WHERE entity.funcionalidad_id IS NULL")
    Flux<RolFuncionalidad> findAllWhereFuncionalidadIsNull();

    @Override
    <S extends RolFuncionalidad> Mono<S> save(S entity);

    @Override
    Flux<RolFuncionalidad> findAll();

    @Override
    Mono<RolFuncionalidad> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface RolFuncionalidadRepositoryInternal {
    <S extends RolFuncionalidad> Mono<S> save(S entity);

    Flux<RolFuncionalidad> findAllBy(Pageable pageable);

    Flux<RolFuncionalidad> findAll();

    Mono<RolFuncionalidad> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<RolFuncionalidad> findAllBy(Pageable pageable, Criteria criteria);

    Mono<RolFuncionalidad> findOneWithEagerRelationships(Long id);

    Flux<RolFuncionalidad> findAllWithEagerRelationships();

    Flux<RolFuncionalidad> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}

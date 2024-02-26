package ec.gob.loja.reparaciones.repository;

import ec.gob.loja.reparaciones.domain.Funcionalidad;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Funcionalidad entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FuncionalidadRepository extends ReactiveCrudRepository<Funcionalidad, Long>, FuncionalidadRepositoryInternal {
    @Query("SELECT * FROM funcionalidad entity WHERE entity.padre_id = :id")
    Flux<Funcionalidad> findByPadre(Long id);

    @Query("SELECT * FROM funcionalidad entity WHERE entity.padre_id IS NULL")
    Flux<Funcionalidad> findAllWherePadreIsNull();

    @Override
    <S extends Funcionalidad> Mono<S> save(S entity);

    @Override
    Flux<Funcionalidad> findAll();

    @Override
    Mono<Funcionalidad> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface FuncionalidadRepositoryInternal {
    <S extends Funcionalidad> Mono<S> save(S entity);

    Flux<Funcionalidad> findAllBy(Pageable pageable);

    Flux<Funcionalidad> findAll();

    Mono<Funcionalidad> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Funcionalidad> findAllBy(Pageable pageable, Criteria criteria);
}

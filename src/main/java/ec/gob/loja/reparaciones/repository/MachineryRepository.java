package ec.gob.loja.reparaciones.repository;

import ec.gob.loja.reparaciones.domain.Machinery;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Machinery entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MachineryRepository extends ReactiveCrudRepository<Machinery, Long>, MachineryRepositoryInternal {
    @Override
    Mono<Machinery> findOneWithEagerRelationships(Long id);

    @Override
    Flux<Machinery> findAllWithEagerRelationships();

    @Override
    Flux<Machinery> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM machinery entity WHERE entity.nombre_id = :id")
    Flux<Machinery> findByNombre(Long id);

    @Query("SELECT * FROM machinery entity WHERE entity.nombre_id IS NULL")
    Flux<Machinery> findAllWhereNombreIsNull();

    @Override
    <S extends Machinery> Mono<S> save(S entity);

    @Override
    Flux<Machinery> findAll();

    @Override
    Mono<Machinery> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface MachineryRepositoryInternal {
    <S extends Machinery> Mono<S> save(S entity);

    Flux<Machinery> findAllBy(Pageable pageable);

    Flux<Machinery> findAll();

    Mono<Machinery> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Machinery> findAllBy(Pageable pageable, Criteria criteria);

    Mono<Machinery> findOneWithEagerRelationships(Long id);

    Flux<Machinery> findAllWithEagerRelationships();

    Flux<Machinery> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}

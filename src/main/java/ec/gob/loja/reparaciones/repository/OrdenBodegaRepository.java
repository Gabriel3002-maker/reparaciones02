package ec.gob.loja.reparaciones.repository;

import ec.gob.loja.reparaciones.domain.OrdenBodega;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the OrdenBodega entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrdenBodegaRepository extends ReactiveCrudRepository<OrdenBodega, Long>, OrdenBodegaRepositoryInternal {
    @Override
    Mono<OrdenBodega> findOneWithEagerRelationships(Long id);

    @Override
    Flux<OrdenBodega> findAllWithEagerRelationships();

    @Override
    Flux<OrdenBodega> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM orden_bodega entity WHERE entity.receptor_id = :id")
    Flux<OrdenBodega> findByReceptor(Long id);

    @Query("SELECT * FROM orden_bodega entity WHERE entity.receptor_id IS NULL")
    Flux<OrdenBodega> findAllWhereReceptorIsNull();

    @Override
    <S extends OrdenBodega> Mono<S> save(S entity);

    @Override
    Flux<OrdenBodega> findAll();

    @Override
    Mono<OrdenBodega> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface OrdenBodegaRepositoryInternal {
    <S extends OrdenBodega> Mono<S> save(S entity);

    Flux<OrdenBodega> findAllBy(Pageable pageable);

    Flux<OrdenBodega> findAll();

    Mono<OrdenBodega> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<OrdenBodega> findAllBy(Pageable pageable, Criteria criteria);

    Mono<OrdenBodega> findOneWithEagerRelationships(Long id);

    Flux<OrdenBodega> findAllWithEagerRelationships();

    Flux<OrdenBodega> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}

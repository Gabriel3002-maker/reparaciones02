package ec.gob.loja.reparaciones.repository;

import ec.gob.loja.reparaciones.domain.RegistroDanio;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the RegistroDanio entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RegistroDanioRepository extends ReactiveCrudRepository<RegistroDanio, Long>, RegistroDanioRepositoryInternal {
    @Override
    Mono<RegistroDanio> findOneWithEagerRelationships(Long id);

    @Override
    Flux<RegistroDanio> findAllWithEagerRelationships();

    @Override
    Flux<RegistroDanio> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM registro_danio entity WHERE entity.detalle_danio_id = :id")
    Flux<RegistroDanio> findByDetalleDanio(Long id);

    @Query("SELECT * FROM registro_danio entity WHERE entity.detalle_danio_id IS NULL")
    Flux<RegistroDanio> findAllWhereDetalleDanioIsNull();

    @Override
    <S extends RegistroDanio> Mono<S> save(S entity);

    @Override
    Flux<RegistroDanio> findAll();

    @Override
    Mono<RegistroDanio> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface RegistroDanioRepositoryInternal {
    <S extends RegistroDanio> Mono<S> save(S entity);

    Flux<RegistroDanio> findAllBy(Pageable pageable);

    Flux<RegistroDanio> findAll();

    Mono<RegistroDanio> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<RegistroDanio> findAllBy(Pageable pageable, Criteria criteria);

    Mono<RegistroDanio> findOneWithEagerRelationships(Long id);

    Flux<RegistroDanio> findAllWithEagerRelationships();

    Flux<RegistroDanio> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}

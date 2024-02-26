package ec.gob.loja.reparaciones.repository;

import ec.gob.loja.reparaciones.domain.ParametroSistema;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the ParametroSistema entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParametroSistemaRepository extends ReactiveCrudRepository<ParametroSistema, Long>, ParametroSistemaRepositoryInternal {
    @Override
    <S extends ParametroSistema> Mono<S> save(S entity);

    @Override
    Flux<ParametroSistema> findAll();

    @Override
    Mono<ParametroSistema> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface ParametroSistemaRepositoryInternal {
    <S extends ParametroSistema> Mono<S> save(S entity);

    Flux<ParametroSistema> findAllBy(Pageable pageable);

    Flux<ParametroSistema> findAll();

    Mono<ParametroSistema> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ParametroSistema> findAllBy(Pageable pageable, Criteria criteria);
}

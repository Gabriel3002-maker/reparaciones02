package ec.gob.loja.reparaciones.repository;

import ec.gob.loja.reparaciones.domain.MaterialDanio;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the MaterialDanio entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MaterialDanioRepository extends ReactiveCrudRepository<MaterialDanio, Long>, MaterialDanioRepositoryInternal {
    @Override
    <S extends MaterialDanio> Mono<S> save(S entity);

    @Override
    Flux<MaterialDanio> findAll();

    @Override
    Mono<MaterialDanio> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface MaterialDanioRepositoryInternal {
    <S extends MaterialDanio> Mono<S> save(S entity);

    Flux<MaterialDanio> findAllBy(Pageable pageable);

    Flux<MaterialDanio> findAll();

    Mono<MaterialDanio> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<MaterialDanio> findAllBy(Pageable pageable, Criteria criteria);
}

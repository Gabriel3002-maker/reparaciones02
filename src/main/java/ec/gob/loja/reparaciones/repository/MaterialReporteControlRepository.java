package ec.gob.loja.reparaciones.repository;

import ec.gob.loja.reparaciones.domain.MaterialReporteControl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the MaterialReporteControl entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MaterialReporteControlRepository
    extends ReactiveCrudRepository<MaterialReporteControl, Long>, MaterialReporteControlRepositoryInternal {
    @Override
    <S extends MaterialReporteControl> Mono<S> save(S entity);

    @Override
    Flux<MaterialReporteControl> findAll();

    @Override
    Mono<MaterialReporteControl> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface MaterialReporteControlRepositoryInternal {
    <S extends MaterialReporteControl> Mono<S> save(S entity);

    Flux<MaterialReporteControl> findAllBy(Pageable pageable);

    Flux<MaterialReporteControl> findAll();

    Mono<MaterialReporteControl> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<MaterialReporteControl> findAllBy(Pageable pageable, Criteria criteria);
}

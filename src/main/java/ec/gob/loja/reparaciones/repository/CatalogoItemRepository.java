package ec.gob.loja.reparaciones.repository;

import ec.gob.loja.reparaciones.domain.CatalogoItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the CatalogoItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CatalogoItemRepository extends ReactiveCrudRepository<CatalogoItem, Long>, CatalogoItemRepositoryInternal {
    @Query("SELECT * FROM catalogo_item entity WHERE entity.catalogo_id = :id")
    Flux<CatalogoItem> findByCatalogo(Long id);

    @Query("SELECT * FROM catalogo_item entity WHERE entity.catalogo_id IS NULL")
    Flux<CatalogoItem> findAllWhereCatalogoIsNull();

    @Override
    <S extends CatalogoItem> Mono<S> save(S entity);

    @Override
    Flux<CatalogoItem> findAll();

    @Override
    Mono<CatalogoItem> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface CatalogoItemRepositoryInternal {
    <S extends CatalogoItem> Mono<S> save(S entity);

    Flux<CatalogoItem> findAllBy(Pageable pageable);

    Flux<CatalogoItem> findAll();

    Mono<CatalogoItem> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<CatalogoItem> findAllBy(Pageable pageable, Criteria criteria);
}

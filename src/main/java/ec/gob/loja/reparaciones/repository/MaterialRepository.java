package ec.gob.loja.reparaciones.repository;

import ec.gob.loja.reparaciones.domain.Material;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Material entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MaterialRepository extends ReactiveCrudRepository<Material, Long>, MaterialRepositoryInternal {
    @Override
    Mono<Material> findOneWithEagerRelationships(Long id);

    @Override
    Flux<Material> findAllWithEagerRelationships();

    @Override
    Flux<Material> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM material entity WHERE entity.categoria_id = :id")
    Flux<Material> findByCategoria(Long id);

    @Query("SELECT * FROM material entity WHERE entity.categoria_id IS NULL")
    Flux<Material> findAllWhereCategoriaIsNull();

    @Query("SELECT * FROM material entity WHERE entity.material_danio_id = :id")
    Flux<Material> findByMaterialDanio(Long id);

    @Query("SELECT * FROM material entity WHERE entity.material_danio_id IS NULL")
    Flux<Material> findAllWhereMaterialDanioIsNull();

    @Query("SELECT * FROM material entity WHERE entity.material_reporte_control_id = :id")
    Flux<Material> findByMaterialReporteControl(Long id);

    @Query("SELECT * FROM material entity WHERE entity.material_reporte_control_id IS NULL")
    Flux<Material> findAllWhereMaterialReporteControlIsNull();

    @Override
    <S extends Material> Mono<S> save(S entity);

    @Override
    Flux<Material> findAll();

    @Override
    Mono<Material> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface MaterialRepositoryInternal {
    <S extends Material> Mono<S> save(S entity);

    Flux<Material> findAllBy(Pageable pageable);

    Flux<Material> findAll();

    Mono<Material> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Material> findAllBy(Pageable pageable, Criteria criteria);

    Mono<Material> findOneWithEagerRelationships(Long id);

    Flux<Material> findAllWithEagerRelationships();

    Flux<Material> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}

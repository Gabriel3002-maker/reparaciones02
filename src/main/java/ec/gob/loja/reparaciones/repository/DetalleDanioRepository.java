package ec.gob.loja.reparaciones.repository;

import ec.gob.loja.reparaciones.domain.DetalleDanio;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the DetalleDanio entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DetalleDanioRepository extends ReactiveCrudRepository<DetalleDanio, Long>, DetalleDanioRepositoryInternal {
    @Override
    Mono<DetalleDanio> findOneWithEagerRelationships(Long id);

    @Override
    Flux<DetalleDanio> findAllWithEagerRelationships();

    @Override
    Flux<DetalleDanio> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM detalle_danio entity WHERE entity.tipo_danio_id = :id")
    Flux<DetalleDanio> findByTipoDanio(Long id);

    @Query("SELECT * FROM detalle_danio entity WHERE entity.tipo_danio_id IS NULL")
    Flux<DetalleDanio> findAllWhereTipoDanioIsNull();

    @Query("SELECT * FROM detalle_danio entity WHERE entity.material_danio_id = :id")
    Flux<DetalleDanio> findByMaterialDanio(Long id);

    @Query("SELECT * FROM detalle_danio entity WHERE entity.material_danio_id IS NULL")
    Flux<DetalleDanio> findAllWhereMaterialDanioIsNull();

    @Query("SELECT * FROM detalle_danio entity WHERE entity.maquinaria_id = :id")
    Flux<DetalleDanio> findByMaquinaria(Long id);

    @Query("SELECT * FROM detalle_danio entity WHERE entity.maquinaria_id IS NULL")
    Flux<DetalleDanio> findAllWhereMaquinariaIsNull();

    @Query("SELECT * FROM detalle_danio entity WHERE entity.orden_bodega_id = :id")
    Flux<DetalleDanio> findByOrdenBodega(Long id);

    @Query("SELECT * FROM detalle_danio entity WHERE entity.orden_bodega_id IS NULL")
    Flux<DetalleDanio> findAllWhereOrdenBodegaIsNull();

    @Query("SELECT * FROM detalle_danio entity WHERE entity.detalle_reporte_danio_id = :id")
    Flux<DetalleDanio> findByDetalleReporteDanio(Long id);

    @Query("SELECT * FROM detalle_danio entity WHERE entity.detalle_reporte_danio_id IS NULL")
    Flux<DetalleDanio> findAllWhereDetalleReporteDanioIsNull();

    @Override
    <S extends DetalleDanio> Mono<S> save(S entity);

    @Override
    Flux<DetalleDanio> findAll();

    @Override
    Mono<DetalleDanio> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface DetalleDanioRepositoryInternal {
    <S extends DetalleDanio> Mono<S> save(S entity);

    Flux<DetalleDanio> findAllBy(Pageable pageable);

    Flux<DetalleDanio> findAll();

    Mono<DetalleDanio> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<DetalleDanio> findAllBy(Pageable pageable, Criteria criteria);

    Mono<DetalleDanio> findOneWithEagerRelationships(Long id);

    Flux<DetalleDanio> findAllWithEagerRelationships();

    Flux<DetalleDanio> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}

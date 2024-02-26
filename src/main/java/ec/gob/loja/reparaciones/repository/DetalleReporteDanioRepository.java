package ec.gob.loja.reparaciones.repository;

import ec.gob.loja.reparaciones.domain.DetalleReporteDanio;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the DetalleReporteDanio entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DetalleReporteDanioRepository
    extends ReactiveCrudRepository<DetalleReporteDanio, Long>, DetalleReporteDanioRepositoryInternal {
    @Override
    Mono<DetalleReporteDanio> findOneWithEagerRelationships(Long id);

    @Override
    Flux<DetalleReporteDanio> findAllWithEagerRelationships();

    @Override
    Flux<DetalleReporteDanio> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM detalle_reporte_danio entity WHERE entity.material_reporte_id = :id")
    Flux<DetalleReporteDanio> findByMaterialReporte(Long id);

    @Query("SELECT * FROM detalle_reporte_danio entity WHERE entity.material_reporte_id IS NULL")
    Flux<DetalleReporteDanio> findAllWhereMaterialReporteIsNull();

    @Override
    <S extends DetalleReporteDanio> Mono<S> save(S entity);

    @Override
    Flux<DetalleReporteDanio> findAll();

    @Override
    Mono<DetalleReporteDanio> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface DetalleReporteDanioRepositoryInternal {
    <S extends DetalleReporteDanio> Mono<S> save(S entity);

    Flux<DetalleReporteDanio> findAllBy(Pageable pageable);

    Flux<DetalleReporteDanio> findAll();

    Mono<DetalleReporteDanio> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<DetalleReporteDanio> findAllBy(Pageable pageable, Criteria criteria);

    Mono<DetalleReporteDanio> findOneWithEagerRelationships(Long id);

    Flux<DetalleReporteDanio> findAllWithEagerRelationships();

    Flux<DetalleReporteDanio> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}

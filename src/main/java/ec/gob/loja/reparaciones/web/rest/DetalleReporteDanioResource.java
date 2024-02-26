package ec.gob.loja.reparaciones.web.rest;

import ec.gob.loja.reparaciones.domain.DetalleReporteDanio;
import ec.gob.loja.reparaciones.repository.DetalleReporteDanioRepository;
import ec.gob.loja.reparaciones.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link ec.gob.loja.reparaciones.domain.DetalleReporteDanio}.
 */
@RestController
@RequestMapping("/api/detalle-reporte-danios")
@Transactional
public class DetalleReporteDanioResource {

    private final Logger log = LoggerFactory.getLogger(DetalleReporteDanioResource.class);

    private static final String ENTITY_NAME = "detalleReporteDanio";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DetalleReporteDanioRepository detalleReporteDanioRepository;

    public DetalleReporteDanioResource(DetalleReporteDanioRepository detalleReporteDanioRepository) {
        this.detalleReporteDanioRepository = detalleReporteDanioRepository;
    }

    /**
     * {@code POST  /detalle-reporte-danios} : Create a new detalleReporteDanio.
     *
     * @param detalleReporteDanio the detalleReporteDanio to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new detalleReporteDanio, or with status {@code 400 (Bad Request)} if the detalleReporteDanio has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<DetalleReporteDanio>> createDetalleReporteDanio(@RequestBody DetalleReporteDanio detalleReporteDanio)
        throws URISyntaxException {
        log.debug("REST request to save DetalleReporteDanio : {}", detalleReporteDanio);
        if (detalleReporteDanio.getId() != null) {
            throw new BadRequestAlertException("A new detalleReporteDanio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return detalleReporteDanioRepository
            .save(detalleReporteDanio)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/detalle-reporte-danios/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /detalle-reporte-danios/:id} : Updates an existing detalleReporteDanio.
     *
     * @param id the id of the detalleReporteDanio to save.
     * @param detalleReporteDanio the detalleReporteDanio to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated detalleReporteDanio,
     * or with status {@code 400 (Bad Request)} if the detalleReporteDanio is not valid,
     * or with status {@code 500 (Internal Server Error)} if the detalleReporteDanio couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<DetalleReporteDanio>> updateDetalleReporteDanio(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DetalleReporteDanio detalleReporteDanio
    ) throws URISyntaxException {
        log.debug("REST request to update DetalleReporteDanio : {}, {}", id, detalleReporteDanio);
        if (detalleReporteDanio.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, detalleReporteDanio.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return detalleReporteDanioRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return detalleReporteDanioRepository
                    .save(detalleReporteDanio)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /detalle-reporte-danios/:id} : Partial updates given fields of an existing detalleReporteDanio, field will ignore if it is null
     *
     * @param id the id of the detalleReporteDanio to save.
     * @param detalleReporteDanio the detalleReporteDanio to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated detalleReporteDanio,
     * or with status {@code 400 (Bad Request)} if the detalleReporteDanio is not valid,
     * or with status {@code 404 (Not Found)} if the detalleReporteDanio is not found,
     * or with status {@code 500 (Internal Server Error)} if the detalleReporteDanio couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<DetalleReporteDanio>> partialUpdateDetalleReporteDanio(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DetalleReporteDanio detalleReporteDanio
    ) throws URISyntaxException {
        log.debug("REST request to partial update DetalleReporteDanio partially : {}, {}", id, detalleReporteDanio);
        if (detalleReporteDanio.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, detalleReporteDanio.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return detalleReporteDanioRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<DetalleReporteDanio> result = detalleReporteDanioRepository
                    .findById(detalleReporteDanio.getId())
                    .map(existingDetalleReporteDanio -> {
                        if (detalleReporteDanio.getCodigo() != null) {
                            existingDetalleReporteDanio.setCodigo(detalleReporteDanio.getCodigo());
                        }
                        if (detalleReporteDanio.getFecha() != null) {
                            existingDetalleReporteDanio.setFecha(detalleReporteDanio.getFecha());
                        }
                        if (detalleReporteDanio.getContribuyente() != null) {
                            existingDetalleReporteDanio.setContribuyente(detalleReporteDanio.getContribuyente());
                        }
                        if (detalleReporteDanio.getDireccion() != null) {
                            existingDetalleReporteDanio.setDireccion(detalleReporteDanio.getDireccion());
                        }
                        if (detalleReporteDanio.getReferencia() != null) {
                            existingDetalleReporteDanio.setReferencia(detalleReporteDanio.getReferencia());
                        }
                        if (detalleReporteDanio.getHorasTrabajadas() != null) {
                            existingDetalleReporteDanio.setHorasTrabajadas(detalleReporteDanio.getHorasTrabajadas());
                        }
                        if (detalleReporteDanio.getPersonalResponsable() != null) {
                            existingDetalleReporteDanio.setPersonalResponsable(detalleReporteDanio.getPersonalResponsable());
                        }

                        return existingDetalleReporteDanio;
                    })
                    .flatMap(detalleReporteDanioRepository::save);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /detalle-reporte-danios} : get all the detalleReporteDanios.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of detalleReporteDanios in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<DetalleReporteDanio>> getAllDetalleReporteDanios(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get all DetalleReporteDanios");
        if (eagerload) {
            return detalleReporteDanioRepository.findAllWithEagerRelationships().collectList();
        } else {
            return detalleReporteDanioRepository.findAll().collectList();
        }
    }

    /**
     * {@code GET  /detalle-reporte-danios} : get all the detalleReporteDanios as a stream.
     * @return the {@link Flux} of detalleReporteDanios.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<DetalleReporteDanio> getAllDetalleReporteDaniosAsStream() {
        log.debug("REST request to get all DetalleReporteDanios as a stream");
        return detalleReporteDanioRepository.findAll();
    }

    /**
     * {@code GET  /detalle-reporte-danios/:id} : get the "id" detalleReporteDanio.
     *
     * @param id the id of the detalleReporteDanio to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the detalleReporteDanio, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<DetalleReporteDanio>> getDetalleReporteDanio(@PathVariable("id") Long id) {
        log.debug("REST request to get DetalleReporteDanio : {}", id);
        Mono<DetalleReporteDanio> detalleReporteDanio = detalleReporteDanioRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(detalleReporteDanio);
    }

    /**
     * {@code DELETE  /detalle-reporte-danios/:id} : delete the "id" detalleReporteDanio.
     *
     * @param id the id of the detalleReporteDanio to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteDetalleReporteDanio(@PathVariable("id") Long id) {
        log.debug("REST request to delete DetalleReporteDanio : {}", id);
        return detalleReporteDanioRepository
            .deleteById(id)
            .then(
                Mono.just(
                    ResponseEntity
                        .noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }
}

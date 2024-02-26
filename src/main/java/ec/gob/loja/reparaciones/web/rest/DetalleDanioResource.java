package ec.gob.loja.reparaciones.web.rest;

import ec.gob.loja.reparaciones.domain.DetalleDanio;
import ec.gob.loja.reparaciones.repository.DetalleDanioRepository;
import ec.gob.loja.reparaciones.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
 * REST controller for managing {@link ec.gob.loja.reparaciones.domain.DetalleDanio}.
 */
@RestController
@RequestMapping("/api/detalle-danios")
@Transactional
public class DetalleDanioResource {

    private final Logger log = LoggerFactory.getLogger(DetalleDanioResource.class);

    private static final String ENTITY_NAME = "detalleDanio";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DetalleDanioRepository detalleDanioRepository;

    public DetalleDanioResource(DetalleDanioRepository detalleDanioRepository) {
        this.detalleDanioRepository = detalleDanioRepository;
    }

    /**
     * {@code POST  /detalle-danios} : Create a new detalleDanio.
     *
     * @param detalleDanio the detalleDanio to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new detalleDanio, or with status {@code 400 (Bad Request)} if the detalleDanio has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<DetalleDanio>> createDetalleDanio(@Valid @RequestBody DetalleDanio detalleDanio) throws URISyntaxException {
        log.debug("REST request to save DetalleDanio : {}", detalleDanio);
        if (detalleDanio.getId() != null) {
            throw new BadRequestAlertException("A new detalleDanio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return detalleDanioRepository
            .save(detalleDanio)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/detalle-danios/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /detalle-danios/:id} : Updates an existing detalleDanio.
     *
     * @param id the id of the detalleDanio to save.
     * @param detalleDanio the detalleDanio to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated detalleDanio,
     * or with status {@code 400 (Bad Request)} if the detalleDanio is not valid,
     * or with status {@code 500 (Internal Server Error)} if the detalleDanio couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<DetalleDanio>> updateDetalleDanio(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DetalleDanio detalleDanio
    ) throws URISyntaxException {
        log.debug("REST request to update DetalleDanio : {}, {}", id, detalleDanio);
        if (detalleDanio.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, detalleDanio.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return detalleDanioRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return detalleDanioRepository
                    .save(detalleDanio)
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
     * {@code PATCH  /detalle-danios/:id} : Partial updates given fields of an existing detalleDanio, field will ignore if it is null
     *
     * @param id the id of the detalleDanio to save.
     * @param detalleDanio the detalleDanio to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated detalleDanio,
     * or with status {@code 400 (Bad Request)} if the detalleDanio is not valid,
     * or with status {@code 404 (Not Found)} if the detalleDanio is not found,
     * or with status {@code 500 (Internal Server Error)} if the detalleDanio couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<DetalleDanio>> partialUpdateDetalleDanio(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DetalleDanio detalleDanio
    ) throws URISyntaxException {
        log.debug("REST request to partial update DetalleDanio partially : {}, {}", id, detalleDanio);
        if (detalleDanio.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, detalleDanio.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return detalleDanioRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<DetalleDanio> result = detalleDanioRepository
                    .findById(detalleDanio.getId())
                    .map(existingDetalleDanio -> {
                        if (detalleDanio.getCodigo() != null) {
                            existingDetalleDanio.setCodigo(detalleDanio.getCodigo());
                        }
                        if (detalleDanio.getDescripcionDanio() != null) {
                            existingDetalleDanio.setDescripcionDanio(detalleDanio.getDescripcionDanio());
                        }
                        if (detalleDanio.getTecnico() != null) {
                            existingDetalleDanio.setTecnico(detalleDanio.getTecnico());
                        }
                        if (detalleDanio.getNamePerson() != null) {
                            existingDetalleDanio.setNamePerson(detalleDanio.getNamePerson());
                        }
                        if (detalleDanio.getDireccion() != null) {
                            existingDetalleDanio.setDireccion(detalleDanio.getDireccion());
                        }
                        if (detalleDanio.getEstadoReparacion() != null) {
                            existingDetalleDanio.setEstadoReparacion(detalleDanio.getEstadoReparacion());
                        }
                        if (detalleDanio.getObservacion() != null) {
                            existingDetalleDanio.setObservacion(detalleDanio.getObservacion());
                        }

                        return existingDetalleDanio;
                    })
                    .flatMap(detalleDanioRepository::save);

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
     * {@code GET  /detalle-danios} : get all the detalleDanios.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of detalleDanios in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<DetalleDanio>> getAllDetalleDanios(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get all DetalleDanios");
        if (eagerload) {
            return detalleDanioRepository.findAllWithEagerRelationships().collectList();
        } else {
            return detalleDanioRepository.findAll().collectList();
        }
    }

    /**
     * {@code GET  /detalle-danios} : get all the detalleDanios as a stream.
     * @return the {@link Flux} of detalleDanios.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<DetalleDanio> getAllDetalleDaniosAsStream() {
        log.debug("REST request to get all DetalleDanios as a stream");
        return detalleDanioRepository.findAll();
    }

    /**
     * {@code GET  /detalle-danios/:id} : get the "id" detalleDanio.
     *
     * @param id the id of the detalleDanio to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the detalleDanio, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<DetalleDanio>> getDetalleDanio(@PathVariable("id") Long id) {
        log.debug("REST request to get DetalleDanio : {}", id);
        Mono<DetalleDanio> detalleDanio = detalleDanioRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(detalleDanio);
    }

    /**
     * {@code DELETE  /detalle-danios/:id} : delete the "id" detalleDanio.
     *
     * @param id the id of the detalleDanio to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteDetalleDanio(@PathVariable("id") Long id) {
        log.debug("REST request to delete DetalleDanio : {}", id);
        return detalleDanioRepository
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

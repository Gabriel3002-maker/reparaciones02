package ec.gob.loja.reparaciones.web.rest;

import ec.gob.loja.reparaciones.domain.OrdenBodega;
import ec.gob.loja.reparaciones.repository.OrdenBodegaRepository;
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
 * REST controller for managing {@link ec.gob.loja.reparaciones.domain.OrdenBodega}.
 */
@RestController
@RequestMapping("/api/orden-bodegas")
@Transactional
public class OrdenBodegaResource {

    private final Logger log = LoggerFactory.getLogger(OrdenBodegaResource.class);

    private static final String ENTITY_NAME = "ordenBodega";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrdenBodegaRepository ordenBodegaRepository;

    public OrdenBodegaResource(OrdenBodegaRepository ordenBodegaRepository) {
        this.ordenBodegaRepository = ordenBodegaRepository;
    }

    /**
     * {@code POST  /orden-bodegas} : Create a new ordenBodega.
     *
     * @param ordenBodega the ordenBodega to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ordenBodega, or with status {@code 400 (Bad Request)} if the ordenBodega has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<OrdenBodega>> createOrdenBodega(@RequestBody OrdenBodega ordenBodega) throws URISyntaxException {
        log.debug("REST request to save OrdenBodega : {}", ordenBodega);
        if (ordenBodega.getId() != null) {
            throw new BadRequestAlertException("A new ordenBodega cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return ordenBodegaRepository
            .save(ordenBodega)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/orden-bodegas/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /orden-bodegas/:id} : Updates an existing ordenBodega.
     *
     * @param id the id of the ordenBodega to save.
     * @param ordenBodega the ordenBodega to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordenBodega,
     * or with status {@code 400 (Bad Request)} if the ordenBodega is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ordenBodega couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<OrdenBodega>> updateOrdenBodega(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdenBodega ordenBodega
    ) throws URISyntaxException {
        log.debug("REST request to update OrdenBodega : {}, {}", id, ordenBodega);
        if (ordenBodega.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordenBodega.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return ordenBodegaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return ordenBodegaRepository
                    .save(ordenBodega)
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
     * {@code PATCH  /orden-bodegas/:id} : Partial updates given fields of an existing ordenBodega, field will ignore if it is null
     *
     * @param id the id of the ordenBodega to save.
     * @param ordenBodega the ordenBodega to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordenBodega,
     * or with status {@code 400 (Bad Request)} if the ordenBodega is not valid,
     * or with status {@code 404 (Not Found)} if the ordenBodega is not found,
     * or with status {@code 500 (Internal Server Error)} if the ordenBodega couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<OrdenBodega>> partialUpdateOrdenBodega(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdenBodega ordenBodega
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrdenBodega partially : {}, {}", id, ordenBodega);
        if (ordenBodega.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordenBodega.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return ordenBodegaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<OrdenBodega> result = ordenBodegaRepository
                    .findById(ordenBodega.getId())
                    .map(existingOrdenBodega -> {
                        if (ordenBodega.getCodigo() != null) {
                            existingOrdenBodega.setCodigo(ordenBodega.getCodigo());
                        }
                        if (ordenBodega.getDetalleNecesidad() != null) {
                            existingOrdenBodega.setDetalleNecesidad(ordenBodega.getDetalleNecesidad());
                        }
                        if (ordenBodega.getFecha() != null) {
                            existingOrdenBodega.setFecha(ordenBodega.getFecha());
                        }

                        return existingOrdenBodega;
                    })
                    .flatMap(ordenBodegaRepository::save);

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
     * {@code GET  /orden-bodegas} : get all the ordenBodegas.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ordenBodegas in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<OrdenBodega>> getAllOrdenBodegas(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get all OrdenBodegas");
        if (eagerload) {
            return ordenBodegaRepository.findAllWithEagerRelationships().collectList();
        } else {
            return ordenBodegaRepository.findAll().collectList();
        }
    }

    /**
     * {@code GET  /orden-bodegas} : get all the ordenBodegas as a stream.
     * @return the {@link Flux} of ordenBodegas.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<OrdenBodega> getAllOrdenBodegasAsStream() {
        log.debug("REST request to get all OrdenBodegas as a stream");
        return ordenBodegaRepository.findAll();
    }

    /**
     * {@code GET  /orden-bodegas/:id} : get the "id" ordenBodega.
     *
     * @param id the id of the ordenBodega to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ordenBodega, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<OrdenBodega>> getOrdenBodega(@PathVariable("id") Long id) {
        log.debug("REST request to get OrdenBodega : {}", id);
        Mono<OrdenBodega> ordenBodega = ordenBodegaRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(ordenBodega);
    }

    /**
     * {@code DELETE  /orden-bodegas/:id} : delete the "id" ordenBodega.
     *
     * @param id the id of the ordenBodega to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteOrdenBodega(@PathVariable("id") Long id) {
        log.debug("REST request to delete OrdenBodega : {}", id);
        return ordenBodegaRepository
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

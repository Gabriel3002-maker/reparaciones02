package ec.gob.loja.reparaciones.web.rest;

import ec.gob.loja.reparaciones.domain.MaterialDanio;
import ec.gob.loja.reparaciones.repository.MaterialDanioRepository;
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
 * REST controller for managing {@link ec.gob.loja.reparaciones.domain.MaterialDanio}.
 */
@RestController
@RequestMapping("/api/material-danios")
@Transactional
public class MaterialDanioResource {

    private final Logger log = LoggerFactory.getLogger(MaterialDanioResource.class);

    private static final String ENTITY_NAME = "materialDanio";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MaterialDanioRepository materialDanioRepository;

    public MaterialDanioResource(MaterialDanioRepository materialDanioRepository) {
        this.materialDanioRepository = materialDanioRepository;
    }

    /**
     * {@code POST  /material-danios} : Create a new materialDanio.
     *
     * @param materialDanio the materialDanio to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new materialDanio, or with status {@code 400 (Bad Request)} if the materialDanio has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<MaterialDanio>> createMaterialDanio(@RequestBody MaterialDanio materialDanio) throws URISyntaxException {
        log.debug("REST request to save MaterialDanio : {}", materialDanio);
        if (materialDanio.getId() != null) {
            throw new BadRequestAlertException("A new materialDanio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return materialDanioRepository
            .save(materialDanio)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/material-danios/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /material-danios/:id} : Updates an existing materialDanio.
     *
     * @param id the id of the materialDanio to save.
     * @param materialDanio the materialDanio to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated materialDanio,
     * or with status {@code 400 (Bad Request)} if the materialDanio is not valid,
     * or with status {@code 500 (Internal Server Error)} if the materialDanio couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<MaterialDanio>> updateMaterialDanio(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MaterialDanio materialDanio
    ) throws URISyntaxException {
        log.debug("REST request to update MaterialDanio : {}, {}", id, materialDanio);
        if (materialDanio.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, materialDanio.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return materialDanioRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return materialDanioRepository
                    .save(materialDanio)
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
     * {@code PATCH  /material-danios/:id} : Partial updates given fields of an existing materialDanio, field will ignore if it is null
     *
     * @param id the id of the materialDanio to save.
     * @param materialDanio the materialDanio to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated materialDanio,
     * or with status {@code 400 (Bad Request)} if the materialDanio is not valid,
     * or with status {@code 404 (Not Found)} if the materialDanio is not found,
     * or with status {@code 500 (Internal Server Error)} if the materialDanio couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<MaterialDanio>> partialUpdateMaterialDanio(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MaterialDanio materialDanio
    ) throws URISyntaxException {
        log.debug("REST request to partial update MaterialDanio partially : {}, {}", id, materialDanio);
        if (materialDanio.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, materialDanio.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return materialDanioRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<MaterialDanio> result = materialDanioRepository
                    .findById(materialDanio.getId())
                    .map(existingMaterialDanio -> {
                        if (materialDanio.getCodigo() != null) {
                            existingMaterialDanio.setCodigo(materialDanio.getCodigo());
                        }
                        if (materialDanio.getCantidadPedida() != null) {
                            existingMaterialDanio.setCantidadPedida(materialDanio.getCantidadPedida());
                        }
                        if (materialDanio.getObservacion() != null) {
                            existingMaterialDanio.setObservacion(materialDanio.getObservacion());
                        }

                        return existingMaterialDanio;
                    })
                    .flatMap(materialDanioRepository::save);

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
     * {@code GET  /material-danios} : get all the materialDanios.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of materialDanios in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<MaterialDanio>> getAllMaterialDanios() {
        log.debug("REST request to get all MaterialDanios");
        return materialDanioRepository.findAll().collectList();
    }

    /**
     * {@code GET  /material-danios} : get all the materialDanios as a stream.
     * @return the {@link Flux} of materialDanios.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<MaterialDanio> getAllMaterialDaniosAsStream() {
        log.debug("REST request to get all MaterialDanios as a stream");
        return materialDanioRepository.findAll();
    }

    /**
     * {@code GET  /material-danios/:id} : get the "id" materialDanio.
     *
     * @param id the id of the materialDanio to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the materialDanio, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<MaterialDanio>> getMaterialDanio(@PathVariable("id") Long id) {
        log.debug("REST request to get MaterialDanio : {}", id);
        Mono<MaterialDanio> materialDanio = materialDanioRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(materialDanio);
    }

    /**
     * {@code DELETE  /material-danios/:id} : delete the "id" materialDanio.
     *
     * @param id the id of the materialDanio to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteMaterialDanio(@PathVariable("id") Long id) {
        log.debug("REST request to delete MaterialDanio : {}", id);
        return materialDanioRepository
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

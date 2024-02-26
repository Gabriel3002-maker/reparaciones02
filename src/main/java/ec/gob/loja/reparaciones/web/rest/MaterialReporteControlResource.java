package ec.gob.loja.reparaciones.web.rest;

import ec.gob.loja.reparaciones.domain.MaterialReporteControl;
import ec.gob.loja.reparaciones.repository.MaterialReporteControlRepository;
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
 * REST controller for managing {@link ec.gob.loja.reparaciones.domain.MaterialReporteControl}.
 */
@RestController
@RequestMapping("/api/material-reporte-controls")
@Transactional
public class MaterialReporteControlResource {

    private final Logger log = LoggerFactory.getLogger(MaterialReporteControlResource.class);

    private static final String ENTITY_NAME = "materialReporteControl";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MaterialReporteControlRepository materialReporteControlRepository;

    public MaterialReporteControlResource(MaterialReporteControlRepository materialReporteControlRepository) {
        this.materialReporteControlRepository = materialReporteControlRepository;
    }

    /**
     * {@code POST  /material-reporte-controls} : Create a new materialReporteControl.
     *
     * @param materialReporteControl the materialReporteControl to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new materialReporteControl, or with status {@code 400 (Bad Request)} if the materialReporteControl has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<MaterialReporteControl>> createMaterialReporteControl(
        @RequestBody MaterialReporteControl materialReporteControl
    ) throws URISyntaxException {
        log.debug("REST request to save MaterialReporteControl : {}", materialReporteControl);
        if (materialReporteControl.getId() != null) {
            throw new BadRequestAlertException("A new materialReporteControl cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return materialReporteControlRepository
            .save(materialReporteControl)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/material-reporte-controls/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /material-reporte-controls/:id} : Updates an existing materialReporteControl.
     *
     * @param id the id of the materialReporteControl to save.
     * @param materialReporteControl the materialReporteControl to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated materialReporteControl,
     * or with status {@code 400 (Bad Request)} if the materialReporteControl is not valid,
     * or with status {@code 500 (Internal Server Error)} if the materialReporteControl couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<MaterialReporteControl>> updateMaterialReporteControl(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MaterialReporteControl materialReporteControl
    ) throws URISyntaxException {
        log.debug("REST request to update MaterialReporteControl : {}, {}", id, materialReporteControl);
        if (materialReporteControl.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, materialReporteControl.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return materialReporteControlRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return materialReporteControlRepository
                    .save(materialReporteControl)
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
     * {@code PATCH  /material-reporte-controls/:id} : Partial updates given fields of an existing materialReporteControl, field will ignore if it is null
     *
     * @param id the id of the materialReporteControl to save.
     * @param materialReporteControl the materialReporteControl to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated materialReporteControl,
     * or with status {@code 400 (Bad Request)} if the materialReporteControl is not valid,
     * or with status {@code 404 (Not Found)} if the materialReporteControl is not found,
     * or with status {@code 500 (Internal Server Error)} if the materialReporteControl couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<MaterialReporteControl>> partialUpdateMaterialReporteControl(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MaterialReporteControl materialReporteControl
    ) throws URISyntaxException {
        log.debug("REST request to partial update MaterialReporteControl partially : {}, {}", id, materialReporteControl);
        if (materialReporteControl.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, materialReporteControl.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return materialReporteControlRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<MaterialReporteControl> result = materialReporteControlRepository
                    .findById(materialReporteControl.getId())
                    .map(existingMaterialReporteControl -> {
                        if (materialReporteControl.getCodigo() != null) {
                            existingMaterialReporteControl.setCodigo(materialReporteControl.getCodigo());
                        }
                        if (materialReporteControl.getCantidadUsada() != null) {
                            existingMaterialReporteControl.setCantidadUsada(materialReporteControl.getCantidadUsada());
                        }
                        if (materialReporteControl.getObservacion() != null) {
                            existingMaterialReporteControl.setObservacion(materialReporteControl.getObservacion());
                        }

                        return existingMaterialReporteControl;
                    })
                    .flatMap(materialReporteControlRepository::save);

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
     * {@code GET  /material-reporte-controls} : get all the materialReporteControls.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of materialReporteControls in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<MaterialReporteControl>> getAllMaterialReporteControls() {
        log.debug("REST request to get all MaterialReporteControls");
        return materialReporteControlRepository.findAll().collectList();
    }

    /**
     * {@code GET  /material-reporte-controls} : get all the materialReporteControls as a stream.
     * @return the {@link Flux} of materialReporteControls.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<MaterialReporteControl> getAllMaterialReporteControlsAsStream() {
        log.debug("REST request to get all MaterialReporteControls as a stream");
        return materialReporteControlRepository.findAll();
    }

    /**
     * {@code GET  /material-reporte-controls/:id} : get the "id" materialReporteControl.
     *
     * @param id the id of the materialReporteControl to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the materialReporteControl, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<MaterialReporteControl>> getMaterialReporteControl(@PathVariable("id") Long id) {
        log.debug("REST request to get MaterialReporteControl : {}", id);
        Mono<MaterialReporteControl> materialReporteControl = materialReporteControlRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(materialReporteControl);
    }

    /**
     * {@code DELETE  /material-reporte-controls/:id} : delete the "id" materialReporteControl.
     *
     * @param id the id of the materialReporteControl to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteMaterialReporteControl(@PathVariable("id") Long id) {
        log.debug("REST request to delete MaterialReporteControl : {}", id);
        return materialReporteControlRepository
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

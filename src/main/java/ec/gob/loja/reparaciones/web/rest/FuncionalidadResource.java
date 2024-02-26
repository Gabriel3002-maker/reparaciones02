package ec.gob.loja.reparaciones.web.rest;

import ec.gob.loja.reparaciones.domain.Funcionalidad;
import ec.gob.loja.reparaciones.repository.FuncionalidadRepository;
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
 * REST controller for managing {@link ec.gob.loja.reparaciones.domain.Funcionalidad}.
 */
@RestController
@RequestMapping("/api/funcionalidads")
@Transactional
public class FuncionalidadResource {

    private final Logger log = LoggerFactory.getLogger(FuncionalidadResource.class);

    private static final String ENTITY_NAME = "funcionalidad";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FuncionalidadRepository funcionalidadRepository;

    public FuncionalidadResource(FuncionalidadRepository funcionalidadRepository) {
        this.funcionalidadRepository = funcionalidadRepository;
    }

    /**
     * {@code POST  /funcionalidads} : Create a new funcionalidad.
     *
     * @param funcionalidad the funcionalidad to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new funcionalidad, or with status {@code 400 (Bad Request)} if the funcionalidad has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<Funcionalidad>> createFuncionalidad(@Valid @RequestBody Funcionalidad funcionalidad)
        throws URISyntaxException {
        log.debug("REST request to save Funcionalidad : {}", funcionalidad);
        if (funcionalidad.getId() != null) {
            throw new BadRequestAlertException("A new funcionalidad cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return funcionalidadRepository
            .save(funcionalidad)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/funcionalidads/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /funcionalidads/:id} : Updates an existing funcionalidad.
     *
     * @param id the id of the funcionalidad to save.
     * @param funcionalidad the funcionalidad to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated funcionalidad,
     * or with status {@code 400 (Bad Request)} if the funcionalidad is not valid,
     * or with status {@code 500 (Internal Server Error)} if the funcionalidad couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Funcionalidad>> updateFuncionalidad(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Funcionalidad funcionalidad
    ) throws URISyntaxException {
        log.debug("REST request to update Funcionalidad : {}, {}", id, funcionalidad);
        if (funcionalidad.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, funcionalidad.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return funcionalidadRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return funcionalidadRepository
                    .save(funcionalidad)
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
     * {@code PATCH  /funcionalidads/:id} : Partial updates given fields of an existing funcionalidad, field will ignore if it is null
     *
     * @param id the id of the funcionalidad to save.
     * @param funcionalidad the funcionalidad to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated funcionalidad,
     * or with status {@code 400 (Bad Request)} if the funcionalidad is not valid,
     * or with status {@code 404 (Not Found)} if the funcionalidad is not found,
     * or with status {@code 500 (Internal Server Error)} if the funcionalidad couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Funcionalidad>> partialUpdateFuncionalidad(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Funcionalidad funcionalidad
    ) throws URISyntaxException {
        log.debug("REST request to partial update Funcionalidad partially : {}, {}", id, funcionalidad);
        if (funcionalidad.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, funcionalidad.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return funcionalidadRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Funcionalidad> result = funcionalidadRepository
                    .findById(funcionalidad.getId())
                    .map(existingFuncionalidad -> {
                        if (funcionalidad.getNombre() != null) {
                            existingFuncionalidad.setNombre(funcionalidad.getNombre());
                        }
                        if (funcionalidad.getDescripcion() != null) {
                            existingFuncionalidad.setDescripcion(funcionalidad.getDescripcion());
                        }
                        if (funcionalidad.getUrl() != null) {
                            existingFuncionalidad.setUrl(funcionalidad.getUrl());
                        }
                        if (funcionalidad.getActivo() != null) {
                            existingFuncionalidad.setActivo(funcionalidad.getActivo());
                        }
                        if (funcionalidad.getIcono() != null) {
                            existingFuncionalidad.setIcono(funcionalidad.getIcono());
                        }
                        if (funcionalidad.getVisible() != null) {
                            existingFuncionalidad.setVisible(funcionalidad.getVisible());
                        }

                        return existingFuncionalidad;
                    })
                    .flatMap(funcionalidadRepository::save);

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
     * {@code GET  /funcionalidads} : get all the funcionalidads.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of funcionalidads in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<Funcionalidad>> getAllFuncionalidads() {
        log.debug("REST request to get all Funcionalidads");
        return funcionalidadRepository.findAll().collectList();
    }

    /**
     * {@code GET  /funcionalidads} : get all the funcionalidads as a stream.
     * @return the {@link Flux} of funcionalidads.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Funcionalidad> getAllFuncionalidadsAsStream() {
        log.debug("REST request to get all Funcionalidads as a stream");
        return funcionalidadRepository.findAll();
    }

    /**
     * {@code GET  /funcionalidads/:id} : get the "id" funcionalidad.
     *
     * @param id the id of the funcionalidad to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the funcionalidad, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Funcionalidad>> getFuncionalidad(@PathVariable("id") Long id) {
        log.debug("REST request to get Funcionalidad : {}", id);
        Mono<Funcionalidad> funcionalidad = funcionalidadRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(funcionalidad);
    }

    /**
     * {@code DELETE  /funcionalidads/:id} : delete the "id" funcionalidad.
     *
     * @param id the id of the funcionalidad to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteFuncionalidad(@PathVariable("id") Long id) {
        log.debug("REST request to delete Funcionalidad : {}", id);
        return funcionalidadRepository
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

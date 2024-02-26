package ec.gob.loja.reparaciones.web.rest;

import ec.gob.loja.reparaciones.domain.RolFuncionalidad;
import ec.gob.loja.reparaciones.repository.RolFuncionalidadRepository;
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
 * REST controller for managing {@link ec.gob.loja.reparaciones.domain.RolFuncionalidad}.
 */
@RestController
@RequestMapping("/api/rol-funcionalidads")
@Transactional
public class RolFuncionalidadResource {

    private final Logger log = LoggerFactory.getLogger(RolFuncionalidadResource.class);

    private static final String ENTITY_NAME = "rolFuncionalidad";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RolFuncionalidadRepository rolFuncionalidadRepository;

    public RolFuncionalidadResource(RolFuncionalidadRepository rolFuncionalidadRepository) {
        this.rolFuncionalidadRepository = rolFuncionalidadRepository;
    }

    /**
     * {@code POST  /rol-funcionalidads} : Create a new rolFuncionalidad.
     *
     * @param rolFuncionalidad the rolFuncionalidad to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rolFuncionalidad, or with status {@code 400 (Bad Request)} if the rolFuncionalidad has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<RolFuncionalidad>> createRolFuncionalidad(@Valid @RequestBody RolFuncionalidad rolFuncionalidad)
        throws URISyntaxException {
        log.debug("REST request to save RolFuncionalidad : {}", rolFuncionalidad);
        if (rolFuncionalidad.getId() != null) {
            throw new BadRequestAlertException("A new rolFuncionalidad cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return rolFuncionalidadRepository
            .save(rolFuncionalidad)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/rol-funcionalidads/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /rol-funcionalidads/:id} : Updates an existing rolFuncionalidad.
     *
     * @param id the id of the rolFuncionalidad to save.
     * @param rolFuncionalidad the rolFuncionalidad to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rolFuncionalidad,
     * or with status {@code 400 (Bad Request)} if the rolFuncionalidad is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rolFuncionalidad couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<RolFuncionalidad>> updateRolFuncionalidad(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RolFuncionalidad rolFuncionalidad
    ) throws URISyntaxException {
        log.debug("REST request to update RolFuncionalidad : {}, {}", id, rolFuncionalidad);
        if (rolFuncionalidad.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rolFuncionalidad.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return rolFuncionalidadRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return rolFuncionalidadRepository
                    .save(rolFuncionalidad)
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
     * {@code PATCH  /rol-funcionalidads/:id} : Partial updates given fields of an existing rolFuncionalidad, field will ignore if it is null
     *
     * @param id the id of the rolFuncionalidad to save.
     * @param rolFuncionalidad the rolFuncionalidad to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rolFuncionalidad,
     * or with status {@code 400 (Bad Request)} if the rolFuncionalidad is not valid,
     * or with status {@code 404 (Not Found)} if the rolFuncionalidad is not found,
     * or with status {@code 500 (Internal Server Error)} if the rolFuncionalidad couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<RolFuncionalidad>> partialUpdateRolFuncionalidad(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RolFuncionalidad rolFuncionalidad
    ) throws URISyntaxException {
        log.debug("REST request to partial update RolFuncionalidad partially : {}, {}", id, rolFuncionalidad);
        if (rolFuncionalidad.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rolFuncionalidad.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return rolFuncionalidadRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<RolFuncionalidad> result = rolFuncionalidadRepository
                    .findById(rolFuncionalidad.getId())
                    .map(existingRolFuncionalidad -> {
                        if (rolFuncionalidad.getRol() != null) {
                            existingRolFuncionalidad.setRol(rolFuncionalidad.getRol());
                        }
                        if (rolFuncionalidad.getActivo() != null) {
                            existingRolFuncionalidad.setActivo(rolFuncionalidad.getActivo());
                        }
                        if (rolFuncionalidad.getPrioridad() != null) {
                            existingRolFuncionalidad.setPrioridad(rolFuncionalidad.getPrioridad());
                        }

                        return existingRolFuncionalidad;
                    })
                    .flatMap(rolFuncionalidadRepository::save);

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
     * {@code GET  /rol-funcionalidads} : get all the rolFuncionalidads.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rolFuncionalidads in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<RolFuncionalidad>> getAllRolFuncionalidads(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get all RolFuncionalidads");
        if (eagerload) {
            return rolFuncionalidadRepository.findAllWithEagerRelationships().collectList();
        } else {
            return rolFuncionalidadRepository.findAll().collectList();
        }
    }

    /**
     * {@code GET  /rol-funcionalidads} : get all the rolFuncionalidads as a stream.
     * @return the {@link Flux} of rolFuncionalidads.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<RolFuncionalidad> getAllRolFuncionalidadsAsStream() {
        log.debug("REST request to get all RolFuncionalidads as a stream");
        return rolFuncionalidadRepository.findAll();
    }

    /**
     * {@code GET  /rol-funcionalidads/:id} : get the "id" rolFuncionalidad.
     *
     * @param id the id of the rolFuncionalidad to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rolFuncionalidad, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<RolFuncionalidad>> getRolFuncionalidad(@PathVariable("id") Long id) {
        log.debug("REST request to get RolFuncionalidad : {}", id);
        Mono<RolFuncionalidad> rolFuncionalidad = rolFuncionalidadRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(rolFuncionalidad);
    }

    /**
     * {@code DELETE  /rol-funcionalidads/:id} : delete the "id" rolFuncionalidad.
     *
     * @param id the id of the rolFuncionalidad to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteRolFuncionalidad(@PathVariable("id") Long id) {
        log.debug("REST request to delete RolFuncionalidad : {}", id);
        return rolFuncionalidadRepository
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

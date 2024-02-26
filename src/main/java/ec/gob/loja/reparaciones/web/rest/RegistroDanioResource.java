package ec.gob.loja.reparaciones.web.rest;

import ec.gob.loja.reparaciones.domain.RegistroDanio;
import ec.gob.loja.reparaciones.repository.RegistroDanioRepository;
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
 * REST controller for managing {@link ec.gob.loja.reparaciones.domain.RegistroDanio}.
 */
@RestController
@RequestMapping("/api/registro-danios")
@Transactional
public class RegistroDanioResource {

    private final Logger log = LoggerFactory.getLogger(RegistroDanioResource.class);

    private static final String ENTITY_NAME = "registroDanio";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RegistroDanioRepository registroDanioRepository;

    public RegistroDanioResource(RegistroDanioRepository registroDanioRepository) {
        this.registroDanioRepository = registroDanioRepository;
    }

    /**
     * {@code POST  /registro-danios} : Create a new registroDanio.
     *
     * @param registroDanio the registroDanio to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new registroDanio, or with status {@code 400 (Bad Request)} if the registroDanio has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<RegistroDanio>> createRegistroDanio(@Valid @RequestBody RegistroDanio registroDanio)
        throws URISyntaxException {
        log.debug("REST request to save RegistroDanio : {}", registroDanio);
        if (registroDanio.getId() != null) {
            throw new BadRequestAlertException("A new registroDanio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return registroDanioRepository
            .save(registroDanio)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/registro-danios/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /registro-danios/:id} : Updates an existing registroDanio.
     *
     * @param id the id of the registroDanio to save.
     * @param registroDanio the registroDanio to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated registroDanio,
     * or with status {@code 400 (Bad Request)} if the registroDanio is not valid,
     * or with status {@code 500 (Internal Server Error)} if the registroDanio couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<RegistroDanio>> updateRegistroDanio(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RegistroDanio registroDanio
    ) throws URISyntaxException {
        log.debug("REST request to update RegistroDanio : {}, {}", id, registroDanio);
        if (registroDanio.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, registroDanio.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return registroDanioRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return registroDanioRepository
                    .save(registroDanio)
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
     * {@code PATCH  /registro-danios/:id} : Partial updates given fields of an existing registroDanio, field will ignore if it is null
     *
     * @param id the id of the registroDanio to save.
     * @param registroDanio the registroDanio to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated registroDanio,
     * or with status {@code 400 (Bad Request)} if the registroDanio is not valid,
     * or with status {@code 404 (Not Found)} if the registroDanio is not found,
     * or with status {@code 500 (Internal Server Error)} if the registroDanio couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<RegistroDanio>> partialUpdateRegistroDanio(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RegistroDanio registroDanio
    ) throws URISyntaxException {
        log.debug("REST request to partial update RegistroDanio partially : {}, {}", id, registroDanio);
        if (registroDanio.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, registroDanio.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return registroDanioRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<RegistroDanio> result = registroDanioRepository
                    .findById(registroDanio.getId())
                    .map(existingRegistroDanio -> {
                        if (registroDanio.getCodigo() != null) {
                            existingRegistroDanio.setCodigo(registroDanio.getCodigo());
                        }
                        if (registroDanio.getFecha() != null) {
                            existingRegistroDanio.setFecha(registroDanio.getFecha());
                        }
                        if (registroDanio.getFechaInicio() != null) {
                            existingRegistroDanio.setFechaInicio(registroDanio.getFechaInicio());
                        }
                        if (registroDanio.getFechaFin() != null) {
                            existingRegistroDanio.setFechaFin(registroDanio.getFechaFin());
                        }
                        if (registroDanio.getDireccion() != null) {
                            existingRegistroDanio.setDireccion(registroDanio.getDireccion());
                        }
                        if (registroDanio.getParroquia() != null) {
                            existingRegistroDanio.setParroquia(registroDanio.getParroquia());
                        }
                        if (registroDanio.getBarrio() != null) {
                            existingRegistroDanio.setBarrio(registroDanio.getBarrio());
                        }

                        return existingRegistroDanio;
                    })
                    .flatMap(registroDanioRepository::save);

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
     * {@code GET  /registro-danios} : get all the registroDanios.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of registroDanios in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<RegistroDanio>> getAllRegistroDanios(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get all RegistroDanios");
        if (eagerload) {
            return registroDanioRepository.findAllWithEagerRelationships().collectList();
        } else {
            return registroDanioRepository.findAll().collectList();
        }
    }

    /**
     * {@code GET  /registro-danios} : get all the registroDanios as a stream.
     * @return the {@link Flux} of registroDanios.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<RegistroDanio> getAllRegistroDaniosAsStream() {
        log.debug("REST request to get all RegistroDanios as a stream");
        return registroDanioRepository.findAll();
    }

    /**
     * {@code GET  /registro-danios/:id} : get the "id" registroDanio.
     *
     * @param id the id of the registroDanio to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the registroDanio, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<RegistroDanio>> getRegistroDanio(@PathVariable("id") Long id) {
        log.debug("REST request to get RegistroDanio : {}", id);
        Mono<RegistroDanio> registroDanio = registroDanioRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(registroDanio);
    }

    /**
     * {@code DELETE  /registro-danios/:id} : delete the "id" registroDanio.
     *
     * @param id the id of the registroDanio to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteRegistroDanio(@PathVariable("id") Long id) {
        log.debug("REST request to delete RegistroDanio : {}", id);
        return registroDanioRepository
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

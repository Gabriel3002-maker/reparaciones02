package ec.gob.loja.reparaciones.web.rest;

import ec.gob.loja.reparaciones.domain.ParametroSistema;
import ec.gob.loja.reparaciones.repository.ParametroSistemaRepository;
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
 * REST controller for managing {@link ec.gob.loja.reparaciones.domain.ParametroSistema}.
 */
@RestController
@RequestMapping("/api/parametro-sistemas")
@Transactional
public class ParametroSistemaResource {

    private final Logger log = LoggerFactory.getLogger(ParametroSistemaResource.class);

    private static final String ENTITY_NAME = "parametroSistema";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ParametroSistemaRepository parametroSistemaRepository;

    public ParametroSistemaResource(ParametroSistemaRepository parametroSistemaRepository) {
        this.parametroSistemaRepository = parametroSistemaRepository;
    }

    /**
     * {@code POST  /parametro-sistemas} : Create a new parametroSistema.
     *
     * @param parametroSistema the parametroSistema to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new parametroSistema, or with status {@code 400 (Bad Request)} if the parametroSistema has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<ParametroSistema>> createParametroSistema(@Valid @RequestBody ParametroSistema parametroSistema)
        throws URISyntaxException {
        log.debug("REST request to save ParametroSistema : {}", parametroSistema);
        if (parametroSistema.getId() != null) {
            throw new BadRequestAlertException("A new parametroSistema cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return parametroSistemaRepository
            .save(parametroSistema)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/parametro-sistemas/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /parametro-sistemas/:id} : Updates an existing parametroSistema.
     *
     * @param id the id of the parametroSistema to save.
     * @param parametroSistema the parametroSistema to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parametroSistema,
     * or with status {@code 400 (Bad Request)} if the parametroSistema is not valid,
     * or with status {@code 500 (Internal Server Error)} if the parametroSistema couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<ParametroSistema>> updateParametroSistema(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ParametroSistema parametroSistema
    ) throws URISyntaxException {
        log.debug("REST request to update ParametroSistema : {}, {}", id, parametroSistema);
        if (parametroSistema.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, parametroSistema.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return parametroSistemaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return parametroSistemaRepository
                    .save(parametroSistema)
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
     * {@code PATCH  /parametro-sistemas/:id} : Partial updates given fields of an existing parametroSistema, field will ignore if it is null
     *
     * @param id the id of the parametroSistema to save.
     * @param parametroSistema the parametroSistema to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parametroSistema,
     * or with status {@code 400 (Bad Request)} if the parametroSistema is not valid,
     * or with status {@code 404 (Not Found)} if the parametroSistema is not found,
     * or with status {@code 500 (Internal Server Error)} if the parametroSistema couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ParametroSistema>> partialUpdateParametroSistema(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ParametroSistema parametroSistema
    ) throws URISyntaxException {
        log.debug("REST request to partial update ParametroSistema partially : {}, {}", id, parametroSistema);
        if (parametroSistema.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, parametroSistema.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return parametroSistemaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ParametroSistema> result = parametroSistemaRepository
                    .findById(parametroSistema.getId())
                    .map(existingParametroSistema -> {
                        if (parametroSistema.getNombre() != null) {
                            existingParametroSistema.setNombre(parametroSistema.getNombre());
                        }
                        if (parametroSistema.getCodigo() != null) {
                            existingParametroSistema.setCodigo(parametroSistema.getCodigo());
                        }
                        if (parametroSistema.getClase() != null) {
                            existingParametroSistema.setClase(parametroSistema.getClase());
                        }
                        if (parametroSistema.getValor() != null) {
                            existingParametroSistema.setValor(parametroSistema.getValor());
                        }

                        return existingParametroSistema;
                    })
                    .flatMap(parametroSistemaRepository::save);

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
     * {@code GET  /parametro-sistemas} : get all the parametroSistemas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of parametroSistemas in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<ParametroSistema>> getAllParametroSistemas() {
        log.debug("REST request to get all ParametroSistemas");
        return parametroSistemaRepository.findAll().collectList();
    }

    /**
     * {@code GET  /parametro-sistemas} : get all the parametroSistemas as a stream.
     * @return the {@link Flux} of parametroSistemas.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<ParametroSistema> getAllParametroSistemasAsStream() {
        log.debug("REST request to get all ParametroSistemas as a stream");
        return parametroSistemaRepository.findAll();
    }

    /**
     * {@code GET  /parametro-sistemas/:id} : get the "id" parametroSistema.
     *
     * @param id the id of the parametroSistema to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the parametroSistema, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ParametroSistema>> getParametroSistema(@PathVariable("id") Long id) {
        log.debug("REST request to get ParametroSistema : {}", id);
        Mono<ParametroSistema> parametroSistema = parametroSistemaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(parametroSistema);
    }

    /**
     * {@code DELETE  /parametro-sistemas/:id} : delete the "id" parametroSistema.
     *
     * @param id the id of the parametroSistema to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteParametroSistema(@PathVariable("id") Long id) {
        log.debug("REST request to delete ParametroSistema : {}", id);
        return parametroSistemaRepository
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

package ec.gob.loja.reparaciones.web.rest;

import ec.gob.loja.reparaciones.domain.Persona;
import ec.gob.loja.reparaciones.repository.PersonaRepository;
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
 * REST controller for managing {@link ec.gob.loja.reparaciones.domain.Persona}.
 */
@RestController
@RequestMapping("/api/personas")
@Transactional
public class PersonaResource {

    private final Logger log = LoggerFactory.getLogger(PersonaResource.class);

    private static final String ENTITY_NAME = "persona";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PersonaRepository personaRepository;

    public PersonaResource(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    /**
     * {@code POST  /personas} : Create a new persona.
     *
     * @param persona the persona to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new persona, or with status {@code 400 (Bad Request)} if the persona has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<Persona>> createPersona(@Valid @RequestBody Persona persona) throws URISyntaxException {
        log.debug("REST request to save Persona : {}", persona);
        if (persona.getId() != null) {
            throw new BadRequestAlertException("A new persona cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return personaRepository
            .save(persona)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/personas/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /personas/:id} : Updates an existing persona.
     *
     * @param id the id of the persona to save.
     * @param persona the persona to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated persona,
     * or with status {@code 400 (Bad Request)} if the persona is not valid,
     * or with status {@code 500 (Internal Server Error)} if the persona couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Persona>> updatePersona(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Persona persona
    ) throws URISyntaxException {
        log.debug("REST request to update Persona : {}, {}", id, persona);
        if (persona.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, persona.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return personaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return personaRepository
                    .save(persona)
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
     * {@code PATCH  /personas/:id} : Partial updates given fields of an existing persona, field will ignore if it is null
     *
     * @param id the id of the persona to save.
     * @param persona the persona to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated persona,
     * or with status {@code 400 (Bad Request)} if the persona is not valid,
     * or with status {@code 404 (Not Found)} if the persona is not found,
     * or with status {@code 500 (Internal Server Error)} if the persona couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Persona>> partialUpdatePersona(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Persona persona
    ) throws URISyntaxException {
        log.debug("REST request to partial update Persona partially : {}, {}", id, persona);
        if (persona.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, persona.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return personaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Persona> result = personaRepository
                    .findById(persona.getId())
                    .map(existingPersona -> {
                        if (persona.getIdentificacion() != null) {
                            existingPersona.setIdentificacion(persona.getIdentificacion());
                        }
                        if (persona.getPrimerApellido() != null) {
                            existingPersona.setPrimerApellido(persona.getPrimerApellido());
                        }
                        if (persona.getSegundoApellido() != null) {
                            existingPersona.setSegundoApellido(persona.getSegundoApellido());
                        }
                        if (persona.getPrimerNombre() != null) {
                            existingPersona.setPrimerNombre(persona.getPrimerNombre());
                        }
                        if (persona.getSegundoNombre() != null) {
                            existingPersona.setSegundoNombre(persona.getSegundoNombre());
                        }
                        if (persona.getCelular() != null) {
                            existingPersona.setCelular(persona.getCelular());
                        }
                        if (persona.getTelefonoConvencional() != null) {
                            existingPersona.setTelefonoConvencional(persona.getTelefonoConvencional());
                        }
                        if (persona.getCorreo() != null) {
                            existingPersona.setCorreo(persona.getCorreo());
                        }

                        return existingPersona;
                    })
                    .flatMap(personaRepository::save);

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
     * {@code GET  /personas} : get all the personas.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of personas in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<Persona>> getAllPersonas(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get all Personas");
        if (eagerload) {
            return personaRepository.findAllWithEagerRelationships().collectList();
        } else {
            return personaRepository.findAll().collectList();
        }
    }

    /**
     * {@code GET  /personas} : get all the personas as a stream.
     * @return the {@link Flux} of personas.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Persona> getAllPersonasAsStream() {
        log.debug("REST request to get all Personas as a stream");
        return personaRepository.findAll();
    }

    /**
     * {@code GET  /personas/:id} : get the "id" persona.
     *
     * @param id the id of the persona to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the persona, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Persona>> getPersona(@PathVariable("id") Long id) {
        log.debug("REST request to get Persona : {}", id);
        Mono<Persona> persona = personaRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(persona);
    }

    /**
     * {@code DELETE  /personas/:id} : delete the "id" persona.
     *
     * @param id the id of the persona to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deletePersona(@PathVariable("id") Long id) {
        log.debug("REST request to delete Persona : {}", id);
        return personaRepository
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

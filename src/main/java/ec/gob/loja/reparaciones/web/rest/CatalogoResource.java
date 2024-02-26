package ec.gob.loja.reparaciones.web.rest;

import ec.gob.loja.reparaciones.domain.Catalogo;
import ec.gob.loja.reparaciones.repository.CatalogoRepository;
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
 * REST controller for managing {@link ec.gob.loja.reparaciones.domain.Catalogo}.
 */
@RestController
@RequestMapping("/api/catalogos")
@Transactional
public class CatalogoResource {

    private final Logger log = LoggerFactory.getLogger(CatalogoResource.class);

    private static final String ENTITY_NAME = "catalogo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CatalogoRepository catalogoRepository;

    public CatalogoResource(CatalogoRepository catalogoRepository) {
        this.catalogoRepository = catalogoRepository;
    }

    /**
     * {@code POST  /catalogos} : Create a new catalogo.
     *
     * @param catalogo the catalogo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new catalogo, or with status {@code 400 (Bad Request)} if the catalogo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<Catalogo>> createCatalogo(@Valid @RequestBody Catalogo catalogo) throws URISyntaxException {
        log.debug("REST request to save Catalogo : {}", catalogo);
        if (catalogo.getId() != null) {
            throw new BadRequestAlertException("A new catalogo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return catalogoRepository
            .save(catalogo)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/catalogos/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /catalogos/:id} : Updates an existing catalogo.
     *
     * @param id the id of the catalogo to save.
     * @param catalogo the catalogo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated catalogo,
     * or with status {@code 400 (Bad Request)} if the catalogo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the catalogo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Catalogo>> updateCatalogo(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Catalogo catalogo
    ) throws URISyntaxException {
        log.debug("REST request to update Catalogo : {}, {}", id, catalogo);
        if (catalogo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, catalogo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return catalogoRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return catalogoRepository
                    .save(catalogo)
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
     * {@code PATCH  /catalogos/:id} : Partial updates given fields of an existing catalogo, field will ignore if it is null
     *
     * @param id the id of the catalogo to save.
     * @param catalogo the catalogo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated catalogo,
     * or with status {@code 400 (Bad Request)} if the catalogo is not valid,
     * or with status {@code 404 (Not Found)} if the catalogo is not found,
     * or with status {@code 500 (Internal Server Error)} if the catalogo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Catalogo>> partialUpdateCatalogo(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Catalogo catalogo
    ) throws URISyntaxException {
        log.debug("REST request to partial update Catalogo partially : {}, {}", id, catalogo);
        if (catalogo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, catalogo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return catalogoRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Catalogo> result = catalogoRepository
                    .findById(catalogo.getId())
                    .map(existingCatalogo -> {
                        if (catalogo.getNombre() != null) {
                            existingCatalogo.setNombre(catalogo.getNombre());
                        }
                        if (catalogo.getCodigo() != null) {
                            existingCatalogo.setCodigo(catalogo.getCodigo());
                        }
                        if (catalogo.getDescripcion() != null) {
                            existingCatalogo.setDescripcion(catalogo.getDescripcion());
                        }

                        return existingCatalogo;
                    })
                    .flatMap(catalogoRepository::save);

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
     * {@code GET  /catalogos} : get all the catalogos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of catalogos in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<Catalogo>> getAllCatalogos() {
        log.debug("REST request to get all Catalogos");
        return catalogoRepository.findAll().collectList();
    }

    /**
     * {@code GET  /catalogos} : get all the catalogos as a stream.
     * @return the {@link Flux} of catalogos.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Catalogo> getAllCatalogosAsStream() {
        log.debug("REST request to get all Catalogos as a stream");
        return catalogoRepository.findAll();
    }

    /**
     * {@code GET  /catalogos/:id} : get the "id" catalogo.
     *
     * @param id the id of the catalogo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the catalogo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Catalogo>> getCatalogo(@PathVariable("id") Long id) {
        log.debug("REST request to get Catalogo : {}", id);
        Mono<Catalogo> catalogo = catalogoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(catalogo);
    }

    /**
     * {@code DELETE  /catalogos/:id} : delete the "id" catalogo.
     *
     * @param id the id of the catalogo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteCatalogo(@PathVariable("id") Long id) {
        log.debug("REST request to delete Catalogo : {}", id);
        return catalogoRepository
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

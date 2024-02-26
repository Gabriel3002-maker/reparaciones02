package ec.gob.loja.reparaciones.web.rest;

import ec.gob.loja.reparaciones.domain.CatalogoItem;
import ec.gob.loja.reparaciones.repository.CatalogoItemRepository;
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
 * REST controller for managing {@link ec.gob.loja.reparaciones.domain.CatalogoItem}.
 */
@RestController
@RequestMapping("/api/catalogo-items")
@Transactional
public class CatalogoItemResource {

    private final Logger log = LoggerFactory.getLogger(CatalogoItemResource.class);

    private static final String ENTITY_NAME = "catalogoItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CatalogoItemRepository catalogoItemRepository;

    public CatalogoItemResource(CatalogoItemRepository catalogoItemRepository) {
        this.catalogoItemRepository = catalogoItemRepository;
    }

    /**
     * {@code POST  /catalogo-items} : Create a new catalogoItem.
     *
     * @param catalogoItem the catalogoItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new catalogoItem, or with status {@code 400 (Bad Request)} if the catalogoItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<CatalogoItem>> createCatalogoItem(@Valid @RequestBody CatalogoItem catalogoItem) throws URISyntaxException {
        log.debug("REST request to save CatalogoItem : {}", catalogoItem);
        if (catalogoItem.getId() != null) {
            throw new BadRequestAlertException("A new catalogoItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return catalogoItemRepository
            .save(catalogoItem)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/catalogo-items/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /catalogo-items/:id} : Updates an existing catalogoItem.
     *
     * @param id the id of the catalogoItem to save.
     * @param catalogoItem the catalogoItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated catalogoItem,
     * or with status {@code 400 (Bad Request)} if the catalogoItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the catalogoItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<CatalogoItem>> updateCatalogoItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CatalogoItem catalogoItem
    ) throws URISyntaxException {
        log.debug("REST request to update CatalogoItem : {}, {}", id, catalogoItem);
        if (catalogoItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, catalogoItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return catalogoItemRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return catalogoItemRepository
                    .save(catalogoItem)
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
     * {@code PATCH  /catalogo-items/:id} : Partial updates given fields of an existing catalogoItem, field will ignore if it is null
     *
     * @param id the id of the catalogoItem to save.
     * @param catalogoItem the catalogoItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated catalogoItem,
     * or with status {@code 400 (Bad Request)} if the catalogoItem is not valid,
     * or with status {@code 404 (Not Found)} if the catalogoItem is not found,
     * or with status {@code 500 (Internal Server Error)} if the catalogoItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<CatalogoItem>> partialUpdateCatalogoItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CatalogoItem catalogoItem
    ) throws URISyntaxException {
        log.debug("REST request to partial update CatalogoItem partially : {}, {}", id, catalogoItem);
        if (catalogoItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, catalogoItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return catalogoItemRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<CatalogoItem> result = catalogoItemRepository
                    .findById(catalogoItem.getId())
                    .map(existingCatalogoItem -> {
                        if (catalogoItem.getNombre() != null) {
                            existingCatalogoItem.setNombre(catalogoItem.getNombre());
                        }
                        if (catalogoItem.getCodigo() != null) {
                            existingCatalogoItem.setCodigo(catalogoItem.getCodigo());
                        }
                        if (catalogoItem.getDescripcion() != null) {
                            existingCatalogoItem.setDescripcion(catalogoItem.getDescripcion());
                        }
                        if (catalogoItem.getCatalogoCodigo() != null) {
                            existingCatalogoItem.setCatalogoCodigo(catalogoItem.getCatalogoCodigo());
                        }
                        if (catalogoItem.getActivo() != null) {
                            existingCatalogoItem.setActivo(catalogoItem.getActivo());
                        }

                        return existingCatalogoItem;
                    })
                    .flatMap(catalogoItemRepository::save);

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
     * {@code GET  /catalogo-items} : get all the catalogoItems.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of catalogoItems in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<CatalogoItem>> getAllCatalogoItems() {
        log.debug("REST request to get all CatalogoItems");
        return catalogoItemRepository.findAll().collectList();
    }

    /**
     * {@code GET  /catalogo-items} : get all the catalogoItems as a stream.
     * @return the {@link Flux} of catalogoItems.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<CatalogoItem> getAllCatalogoItemsAsStream() {
        log.debug("REST request to get all CatalogoItems as a stream");
        return catalogoItemRepository.findAll();
    }

    /**
     * {@code GET  /catalogo-items/:id} : get the "id" catalogoItem.
     *
     * @param id the id of the catalogoItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the catalogoItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<CatalogoItem>> getCatalogoItem(@PathVariable("id") Long id) {
        log.debug("REST request to get CatalogoItem : {}", id);
        Mono<CatalogoItem> catalogoItem = catalogoItemRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(catalogoItem);
    }

    /**
     * {@code DELETE  /catalogo-items/:id} : delete the "id" catalogoItem.
     *
     * @param id the id of the catalogoItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteCatalogoItem(@PathVariable("id") Long id) {
        log.debug("REST request to delete CatalogoItem : {}", id);
        return catalogoItemRepository
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

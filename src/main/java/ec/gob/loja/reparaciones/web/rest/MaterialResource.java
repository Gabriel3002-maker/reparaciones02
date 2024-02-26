package ec.gob.loja.reparaciones.web.rest;

import ec.gob.loja.reparaciones.domain.Material;
import ec.gob.loja.reparaciones.repository.MaterialRepository;
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
 * REST controller for managing {@link ec.gob.loja.reparaciones.domain.Material}.
 */
@RestController
@RequestMapping("/api/materials")
@Transactional
public class MaterialResource {

    private final Logger log = LoggerFactory.getLogger(MaterialResource.class);

    private static final String ENTITY_NAME = "material";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MaterialRepository materialRepository;

    public MaterialResource(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    /**
     * {@code POST  /materials} : Create a new material.
     *
     * @param material the material to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new material, or with status {@code 400 (Bad Request)} if the material has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<Material>> createMaterial(@Valid @RequestBody Material material) throws URISyntaxException {
        log.debug("REST request to save Material : {}", material);
        if (material.getId() != null) {
            throw new BadRequestAlertException("A new material cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return materialRepository
            .save(material)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/materials/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /materials/:id} : Updates an existing material.
     *
     * @param id the id of the material to save.
     * @param material the material to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated material,
     * or with status {@code 400 (Bad Request)} if the material is not valid,
     * or with status {@code 500 (Internal Server Error)} if the material couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Material>> updateMaterial(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Material material
    ) throws URISyntaxException {
        log.debug("REST request to update Material : {}, {}", id, material);
        if (material.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, material.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return materialRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return materialRepository
                    .save(material)
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
     * {@code PATCH  /materials/:id} : Partial updates given fields of an existing material, field will ignore if it is null
     *
     * @param id the id of the material to save.
     * @param material the material to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated material,
     * or with status {@code 400 (Bad Request)} if the material is not valid,
     * or with status {@code 404 (Not Found)} if the material is not found,
     * or with status {@code 500 (Internal Server Error)} if the material couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Material>> partialUpdateMaterial(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Material material
    ) throws URISyntaxException {
        log.debug("REST request to partial update Material partially : {}, {}", id, material);
        if (material.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, material.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return materialRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Material> result = materialRepository
                    .findById(material.getId())
                    .map(existingMaterial -> {
                        if (material.getCodigo() != null) {
                            existingMaterial.setCodigo(material.getCodigo());
                        }
                        if (material.getNombre() != null) {
                            existingMaterial.setNombre(material.getNombre());
                        }
                        if (material.getValorUnitario() != null) {
                            existingMaterial.setValorUnitario(material.getValorUnitario());
                        }
                        if (material.getStock() != null) {
                            existingMaterial.setStock(material.getStock());
                        }
                        if (material.getActivo() != null) {
                            existingMaterial.setActivo(material.getActivo());
                        }
                        if (material.getDescripcion() != null) {
                            existingMaterial.setDescripcion(material.getDescripcion());
                        }
                        if (material.getCreadoPor() != null) {
                            existingMaterial.setCreadoPor(material.getCreadoPor());
                        }
                        if (material.getFechaCreacion() != null) {
                            existingMaterial.setFechaCreacion(material.getFechaCreacion());
                        }
                        if (material.getActualizadoPor() != null) {
                            existingMaterial.setActualizadoPor(material.getActualizadoPor());
                        }
                        if (material.getFechaModificacion() != null) {
                            existingMaterial.setFechaModificacion(material.getFechaModificacion());
                        }

                        return existingMaterial;
                    })
                    .flatMap(materialRepository::save);

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
     * {@code GET  /materials} : get all the materials.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of materials in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<Material>> getAllMaterials(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get all Materials");
        if (eagerload) {
            return materialRepository.findAllWithEagerRelationships().collectList();
        } else {
            return materialRepository.findAll().collectList();
        }
    }

    /**
     * {@code GET  /materials} : get all the materials as a stream.
     * @return the {@link Flux} of materials.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Material> getAllMaterialsAsStream() {
        log.debug("REST request to get all Materials as a stream");
        return materialRepository.findAll();
    }

    /**
     * {@code GET  /materials/:id} : get the "id" material.
     *
     * @param id the id of the material to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the material, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Material>> getMaterial(@PathVariable("id") Long id) {
        log.debug("REST request to get Material : {}", id);
        Mono<Material> material = materialRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(material);
    }

    /**
     * {@code DELETE  /materials/:id} : delete the "id" material.
     *
     * @param id the id of the material to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteMaterial(@PathVariable("id") Long id) {
        log.debug("REST request to delete Material : {}", id);
        return materialRepository
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

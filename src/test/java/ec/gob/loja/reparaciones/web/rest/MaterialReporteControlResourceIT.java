package ec.gob.loja.reparaciones.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import ec.gob.loja.reparaciones.IntegrationTest;
import ec.gob.loja.reparaciones.domain.MaterialReporteControl;
import ec.gob.loja.reparaciones.repository.EntityManager;
import ec.gob.loja.reparaciones.repository.MaterialReporteControlRepository;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link MaterialReporteControlResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class MaterialReporteControlResourceIT {

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final Integer DEFAULT_CANTIDAD_USADA = 1;
    private static final Integer UPDATED_CANTIDAD_USADA = 2;

    private static final String DEFAULT_OBSERVACION = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/material-reporte-controls";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MaterialReporteControlRepository materialReporteControlRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private MaterialReporteControl materialReporteControl;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MaterialReporteControl createEntity(EntityManager em) {
        MaterialReporteControl materialReporteControl = new MaterialReporteControl()
            .codigo(DEFAULT_CODIGO)
            .cantidadUsada(DEFAULT_CANTIDAD_USADA)
            .observacion(DEFAULT_OBSERVACION);
        return materialReporteControl;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MaterialReporteControl createUpdatedEntity(EntityManager em) {
        MaterialReporteControl materialReporteControl = new MaterialReporteControl()
            .codigo(UPDATED_CODIGO)
            .cantidadUsada(UPDATED_CANTIDAD_USADA)
            .observacion(UPDATED_OBSERVACION);
        return materialReporteControl;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(MaterialReporteControl.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        materialReporteControl = createEntity(em);
    }

    @Test
    void createMaterialReporteControl() throws Exception {
        int databaseSizeBeforeCreate = materialReporteControlRepository.findAll().collectList().block().size();
        // Create the MaterialReporteControl
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(materialReporteControl))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the MaterialReporteControl in the database
        List<MaterialReporteControl> materialReporteControlList = materialReporteControlRepository.findAll().collectList().block();
        assertThat(materialReporteControlList).hasSize(databaseSizeBeforeCreate + 1);
        MaterialReporteControl testMaterialReporteControl = materialReporteControlList.get(materialReporteControlList.size() - 1);
        assertThat(testMaterialReporteControl.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testMaterialReporteControl.getCantidadUsada()).isEqualTo(DEFAULT_CANTIDAD_USADA);
        assertThat(testMaterialReporteControl.getObservacion()).isEqualTo(DEFAULT_OBSERVACION);
    }

    @Test
    void createMaterialReporteControlWithExistingId() throws Exception {
        // Create the MaterialReporteControl with an existing ID
        materialReporteControl.setId(1L);

        int databaseSizeBeforeCreate = materialReporteControlRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(materialReporteControl))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MaterialReporteControl in the database
        List<MaterialReporteControl> materialReporteControlList = materialReporteControlRepository.findAll().collectList().block();
        assertThat(materialReporteControlList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllMaterialReporteControlsAsStream() {
        // Initialize the database
        materialReporteControlRepository.save(materialReporteControl).block();

        List<MaterialReporteControl> materialReporteControlList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(MaterialReporteControl.class)
            .getResponseBody()
            .filter(materialReporteControl::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(materialReporteControlList).isNotNull();
        assertThat(materialReporteControlList).hasSize(1);
        MaterialReporteControl testMaterialReporteControl = materialReporteControlList.get(0);
        assertThat(testMaterialReporteControl.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testMaterialReporteControl.getCantidadUsada()).isEqualTo(DEFAULT_CANTIDAD_USADA);
        assertThat(testMaterialReporteControl.getObservacion()).isEqualTo(DEFAULT_OBSERVACION);
    }

    @Test
    void getAllMaterialReporteControls() {
        // Initialize the database
        materialReporteControlRepository.save(materialReporteControl).block();

        // Get all the materialReporteControlList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(materialReporteControl.getId().intValue()))
            .jsonPath("$.[*].codigo")
            .value(hasItem(DEFAULT_CODIGO))
            .jsonPath("$.[*].cantidadUsada")
            .value(hasItem(DEFAULT_CANTIDAD_USADA))
            .jsonPath("$.[*].observacion")
            .value(hasItem(DEFAULT_OBSERVACION));
    }

    @Test
    void getMaterialReporteControl() {
        // Initialize the database
        materialReporteControlRepository.save(materialReporteControl).block();

        // Get the materialReporteControl
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, materialReporteControl.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(materialReporteControl.getId().intValue()))
            .jsonPath("$.codigo")
            .value(is(DEFAULT_CODIGO))
            .jsonPath("$.cantidadUsada")
            .value(is(DEFAULT_CANTIDAD_USADA))
            .jsonPath("$.observacion")
            .value(is(DEFAULT_OBSERVACION));
    }

    @Test
    void getNonExistingMaterialReporteControl() {
        // Get the materialReporteControl
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingMaterialReporteControl() throws Exception {
        // Initialize the database
        materialReporteControlRepository.save(materialReporteControl).block();

        int databaseSizeBeforeUpdate = materialReporteControlRepository.findAll().collectList().block().size();

        // Update the materialReporteControl
        MaterialReporteControl updatedMaterialReporteControl = materialReporteControlRepository
            .findById(materialReporteControl.getId())
            .block();
        updatedMaterialReporteControl.codigo(UPDATED_CODIGO).cantidadUsada(UPDATED_CANTIDAD_USADA).observacion(UPDATED_OBSERVACION);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedMaterialReporteControl.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedMaterialReporteControl))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the MaterialReporteControl in the database
        List<MaterialReporteControl> materialReporteControlList = materialReporteControlRepository.findAll().collectList().block();
        assertThat(materialReporteControlList).hasSize(databaseSizeBeforeUpdate);
        MaterialReporteControl testMaterialReporteControl = materialReporteControlList.get(materialReporteControlList.size() - 1);
        assertThat(testMaterialReporteControl.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testMaterialReporteControl.getCantidadUsada()).isEqualTo(UPDATED_CANTIDAD_USADA);
        assertThat(testMaterialReporteControl.getObservacion()).isEqualTo(UPDATED_OBSERVACION);
    }

    @Test
    void putNonExistingMaterialReporteControl() throws Exception {
        int databaseSizeBeforeUpdate = materialReporteControlRepository.findAll().collectList().block().size();
        materialReporteControl.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, materialReporteControl.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(materialReporteControl))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MaterialReporteControl in the database
        List<MaterialReporteControl> materialReporteControlList = materialReporteControlRepository.findAll().collectList().block();
        assertThat(materialReporteControlList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchMaterialReporteControl() throws Exception {
        int databaseSizeBeforeUpdate = materialReporteControlRepository.findAll().collectList().block().size();
        materialReporteControl.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(materialReporteControl))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MaterialReporteControl in the database
        List<MaterialReporteControl> materialReporteControlList = materialReporteControlRepository.findAll().collectList().block();
        assertThat(materialReporteControlList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamMaterialReporteControl() throws Exception {
        int databaseSizeBeforeUpdate = materialReporteControlRepository.findAll().collectList().block().size();
        materialReporteControl.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(materialReporteControl))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the MaterialReporteControl in the database
        List<MaterialReporteControl> materialReporteControlList = materialReporteControlRepository.findAll().collectList().block();
        assertThat(materialReporteControlList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateMaterialReporteControlWithPatch() throws Exception {
        // Initialize the database
        materialReporteControlRepository.save(materialReporteControl).block();

        int databaseSizeBeforeUpdate = materialReporteControlRepository.findAll().collectList().block().size();

        // Update the materialReporteControl using partial update
        MaterialReporteControl partialUpdatedMaterialReporteControl = new MaterialReporteControl();
        partialUpdatedMaterialReporteControl.setId(materialReporteControl.getId());

        partialUpdatedMaterialReporteControl.codigo(UPDATED_CODIGO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMaterialReporteControl.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedMaterialReporteControl))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the MaterialReporteControl in the database
        List<MaterialReporteControl> materialReporteControlList = materialReporteControlRepository.findAll().collectList().block();
        assertThat(materialReporteControlList).hasSize(databaseSizeBeforeUpdate);
        MaterialReporteControl testMaterialReporteControl = materialReporteControlList.get(materialReporteControlList.size() - 1);
        assertThat(testMaterialReporteControl.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testMaterialReporteControl.getCantidadUsada()).isEqualTo(DEFAULT_CANTIDAD_USADA);
        assertThat(testMaterialReporteControl.getObservacion()).isEqualTo(DEFAULT_OBSERVACION);
    }

    @Test
    void fullUpdateMaterialReporteControlWithPatch() throws Exception {
        // Initialize the database
        materialReporteControlRepository.save(materialReporteControl).block();

        int databaseSizeBeforeUpdate = materialReporteControlRepository.findAll().collectList().block().size();

        // Update the materialReporteControl using partial update
        MaterialReporteControl partialUpdatedMaterialReporteControl = new MaterialReporteControl();
        partialUpdatedMaterialReporteControl.setId(materialReporteControl.getId());

        partialUpdatedMaterialReporteControl.codigo(UPDATED_CODIGO).cantidadUsada(UPDATED_CANTIDAD_USADA).observacion(UPDATED_OBSERVACION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMaterialReporteControl.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedMaterialReporteControl))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the MaterialReporteControl in the database
        List<MaterialReporteControl> materialReporteControlList = materialReporteControlRepository.findAll().collectList().block();
        assertThat(materialReporteControlList).hasSize(databaseSizeBeforeUpdate);
        MaterialReporteControl testMaterialReporteControl = materialReporteControlList.get(materialReporteControlList.size() - 1);
        assertThat(testMaterialReporteControl.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testMaterialReporteControl.getCantidadUsada()).isEqualTo(UPDATED_CANTIDAD_USADA);
        assertThat(testMaterialReporteControl.getObservacion()).isEqualTo(UPDATED_OBSERVACION);
    }

    @Test
    void patchNonExistingMaterialReporteControl() throws Exception {
        int databaseSizeBeforeUpdate = materialReporteControlRepository.findAll().collectList().block().size();
        materialReporteControl.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, materialReporteControl.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(materialReporteControl))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MaterialReporteControl in the database
        List<MaterialReporteControl> materialReporteControlList = materialReporteControlRepository.findAll().collectList().block();
        assertThat(materialReporteControlList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchMaterialReporteControl() throws Exception {
        int databaseSizeBeforeUpdate = materialReporteControlRepository.findAll().collectList().block().size();
        materialReporteControl.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(materialReporteControl))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MaterialReporteControl in the database
        List<MaterialReporteControl> materialReporteControlList = materialReporteControlRepository.findAll().collectList().block();
        assertThat(materialReporteControlList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamMaterialReporteControl() throws Exception {
        int databaseSizeBeforeUpdate = materialReporteControlRepository.findAll().collectList().block().size();
        materialReporteControl.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(materialReporteControl))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the MaterialReporteControl in the database
        List<MaterialReporteControl> materialReporteControlList = materialReporteControlRepository.findAll().collectList().block();
        assertThat(materialReporteControlList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteMaterialReporteControl() {
        // Initialize the database
        materialReporteControlRepository.save(materialReporteControl).block();

        int databaseSizeBeforeDelete = materialReporteControlRepository.findAll().collectList().block().size();

        // Delete the materialReporteControl
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, materialReporteControl.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<MaterialReporteControl> materialReporteControlList = materialReporteControlRepository.findAll().collectList().block();
        assertThat(materialReporteControlList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

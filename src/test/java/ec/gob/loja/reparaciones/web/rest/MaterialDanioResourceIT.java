package ec.gob.loja.reparaciones.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import ec.gob.loja.reparaciones.IntegrationTest;
import ec.gob.loja.reparaciones.domain.MaterialDanio;
import ec.gob.loja.reparaciones.repository.EntityManager;
import ec.gob.loja.reparaciones.repository.MaterialDanioRepository;
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
 * Integration tests for the {@link MaterialDanioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class MaterialDanioResourceIT {

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final Integer DEFAULT_CANTIDAD_PEDIDA = 1;
    private static final Integer UPDATED_CANTIDAD_PEDIDA = 2;

    private static final String DEFAULT_OBSERVACION = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/material-danios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MaterialDanioRepository materialDanioRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private MaterialDanio materialDanio;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MaterialDanio createEntity(EntityManager em) {
        MaterialDanio materialDanio = new MaterialDanio()
            .codigo(DEFAULT_CODIGO)
            .cantidadPedida(DEFAULT_CANTIDAD_PEDIDA)
            .observacion(DEFAULT_OBSERVACION);
        return materialDanio;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MaterialDanio createUpdatedEntity(EntityManager em) {
        MaterialDanio materialDanio = new MaterialDanio()
            .codigo(UPDATED_CODIGO)
            .cantidadPedida(UPDATED_CANTIDAD_PEDIDA)
            .observacion(UPDATED_OBSERVACION);
        return materialDanio;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(MaterialDanio.class).block();
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
        materialDanio = createEntity(em);
    }

    @Test
    void createMaterialDanio() throws Exception {
        int databaseSizeBeforeCreate = materialDanioRepository.findAll().collectList().block().size();
        // Create the MaterialDanio
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(materialDanio))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the MaterialDanio in the database
        List<MaterialDanio> materialDanioList = materialDanioRepository.findAll().collectList().block();
        assertThat(materialDanioList).hasSize(databaseSizeBeforeCreate + 1);
        MaterialDanio testMaterialDanio = materialDanioList.get(materialDanioList.size() - 1);
        assertThat(testMaterialDanio.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testMaterialDanio.getCantidadPedida()).isEqualTo(DEFAULT_CANTIDAD_PEDIDA);
        assertThat(testMaterialDanio.getObservacion()).isEqualTo(DEFAULT_OBSERVACION);
    }

    @Test
    void createMaterialDanioWithExistingId() throws Exception {
        // Create the MaterialDanio with an existing ID
        materialDanio.setId(1L);

        int databaseSizeBeforeCreate = materialDanioRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(materialDanio))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MaterialDanio in the database
        List<MaterialDanio> materialDanioList = materialDanioRepository.findAll().collectList().block();
        assertThat(materialDanioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllMaterialDaniosAsStream() {
        // Initialize the database
        materialDanioRepository.save(materialDanio).block();

        List<MaterialDanio> materialDanioList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(MaterialDanio.class)
            .getResponseBody()
            .filter(materialDanio::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(materialDanioList).isNotNull();
        assertThat(materialDanioList).hasSize(1);
        MaterialDanio testMaterialDanio = materialDanioList.get(0);
        assertThat(testMaterialDanio.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testMaterialDanio.getCantidadPedida()).isEqualTo(DEFAULT_CANTIDAD_PEDIDA);
        assertThat(testMaterialDanio.getObservacion()).isEqualTo(DEFAULT_OBSERVACION);
    }

    @Test
    void getAllMaterialDanios() {
        // Initialize the database
        materialDanioRepository.save(materialDanio).block();

        // Get all the materialDanioList
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
            .value(hasItem(materialDanio.getId().intValue()))
            .jsonPath("$.[*].codigo")
            .value(hasItem(DEFAULT_CODIGO))
            .jsonPath("$.[*].cantidadPedida")
            .value(hasItem(DEFAULT_CANTIDAD_PEDIDA))
            .jsonPath("$.[*].observacion")
            .value(hasItem(DEFAULT_OBSERVACION));
    }

    @Test
    void getMaterialDanio() {
        // Initialize the database
        materialDanioRepository.save(materialDanio).block();

        // Get the materialDanio
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, materialDanio.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(materialDanio.getId().intValue()))
            .jsonPath("$.codigo")
            .value(is(DEFAULT_CODIGO))
            .jsonPath("$.cantidadPedida")
            .value(is(DEFAULT_CANTIDAD_PEDIDA))
            .jsonPath("$.observacion")
            .value(is(DEFAULT_OBSERVACION));
    }

    @Test
    void getNonExistingMaterialDanio() {
        // Get the materialDanio
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingMaterialDanio() throws Exception {
        // Initialize the database
        materialDanioRepository.save(materialDanio).block();

        int databaseSizeBeforeUpdate = materialDanioRepository.findAll().collectList().block().size();

        // Update the materialDanio
        MaterialDanio updatedMaterialDanio = materialDanioRepository.findById(materialDanio.getId()).block();
        updatedMaterialDanio.codigo(UPDATED_CODIGO).cantidadPedida(UPDATED_CANTIDAD_PEDIDA).observacion(UPDATED_OBSERVACION);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedMaterialDanio.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedMaterialDanio))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the MaterialDanio in the database
        List<MaterialDanio> materialDanioList = materialDanioRepository.findAll().collectList().block();
        assertThat(materialDanioList).hasSize(databaseSizeBeforeUpdate);
        MaterialDanio testMaterialDanio = materialDanioList.get(materialDanioList.size() - 1);
        assertThat(testMaterialDanio.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testMaterialDanio.getCantidadPedida()).isEqualTo(UPDATED_CANTIDAD_PEDIDA);
        assertThat(testMaterialDanio.getObservacion()).isEqualTo(UPDATED_OBSERVACION);
    }

    @Test
    void putNonExistingMaterialDanio() throws Exception {
        int databaseSizeBeforeUpdate = materialDanioRepository.findAll().collectList().block().size();
        materialDanio.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, materialDanio.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(materialDanio))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MaterialDanio in the database
        List<MaterialDanio> materialDanioList = materialDanioRepository.findAll().collectList().block();
        assertThat(materialDanioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchMaterialDanio() throws Exception {
        int databaseSizeBeforeUpdate = materialDanioRepository.findAll().collectList().block().size();
        materialDanio.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(materialDanio))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MaterialDanio in the database
        List<MaterialDanio> materialDanioList = materialDanioRepository.findAll().collectList().block();
        assertThat(materialDanioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamMaterialDanio() throws Exception {
        int databaseSizeBeforeUpdate = materialDanioRepository.findAll().collectList().block().size();
        materialDanio.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(materialDanio))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the MaterialDanio in the database
        List<MaterialDanio> materialDanioList = materialDanioRepository.findAll().collectList().block();
        assertThat(materialDanioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateMaterialDanioWithPatch() throws Exception {
        // Initialize the database
        materialDanioRepository.save(materialDanio).block();

        int databaseSizeBeforeUpdate = materialDanioRepository.findAll().collectList().block().size();

        // Update the materialDanio using partial update
        MaterialDanio partialUpdatedMaterialDanio = new MaterialDanio();
        partialUpdatedMaterialDanio.setId(materialDanio.getId());

        partialUpdatedMaterialDanio.cantidadPedida(UPDATED_CANTIDAD_PEDIDA).observacion(UPDATED_OBSERVACION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMaterialDanio.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedMaterialDanio))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the MaterialDanio in the database
        List<MaterialDanio> materialDanioList = materialDanioRepository.findAll().collectList().block();
        assertThat(materialDanioList).hasSize(databaseSizeBeforeUpdate);
        MaterialDanio testMaterialDanio = materialDanioList.get(materialDanioList.size() - 1);
        assertThat(testMaterialDanio.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testMaterialDanio.getCantidadPedida()).isEqualTo(UPDATED_CANTIDAD_PEDIDA);
        assertThat(testMaterialDanio.getObservacion()).isEqualTo(UPDATED_OBSERVACION);
    }

    @Test
    void fullUpdateMaterialDanioWithPatch() throws Exception {
        // Initialize the database
        materialDanioRepository.save(materialDanio).block();

        int databaseSizeBeforeUpdate = materialDanioRepository.findAll().collectList().block().size();

        // Update the materialDanio using partial update
        MaterialDanio partialUpdatedMaterialDanio = new MaterialDanio();
        partialUpdatedMaterialDanio.setId(materialDanio.getId());

        partialUpdatedMaterialDanio.codigo(UPDATED_CODIGO).cantidadPedida(UPDATED_CANTIDAD_PEDIDA).observacion(UPDATED_OBSERVACION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMaterialDanio.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedMaterialDanio))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the MaterialDanio in the database
        List<MaterialDanio> materialDanioList = materialDanioRepository.findAll().collectList().block();
        assertThat(materialDanioList).hasSize(databaseSizeBeforeUpdate);
        MaterialDanio testMaterialDanio = materialDanioList.get(materialDanioList.size() - 1);
        assertThat(testMaterialDanio.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testMaterialDanio.getCantidadPedida()).isEqualTo(UPDATED_CANTIDAD_PEDIDA);
        assertThat(testMaterialDanio.getObservacion()).isEqualTo(UPDATED_OBSERVACION);
    }

    @Test
    void patchNonExistingMaterialDanio() throws Exception {
        int databaseSizeBeforeUpdate = materialDanioRepository.findAll().collectList().block().size();
        materialDanio.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, materialDanio.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(materialDanio))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MaterialDanio in the database
        List<MaterialDanio> materialDanioList = materialDanioRepository.findAll().collectList().block();
        assertThat(materialDanioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchMaterialDanio() throws Exception {
        int databaseSizeBeforeUpdate = materialDanioRepository.findAll().collectList().block().size();
        materialDanio.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(materialDanio))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MaterialDanio in the database
        List<MaterialDanio> materialDanioList = materialDanioRepository.findAll().collectList().block();
        assertThat(materialDanioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamMaterialDanio() throws Exception {
        int databaseSizeBeforeUpdate = materialDanioRepository.findAll().collectList().block().size();
        materialDanio.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(materialDanio))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the MaterialDanio in the database
        List<MaterialDanio> materialDanioList = materialDanioRepository.findAll().collectList().block();
        assertThat(materialDanioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteMaterialDanio() {
        // Initialize the database
        materialDanioRepository.save(materialDanio).block();

        int databaseSizeBeforeDelete = materialDanioRepository.findAll().collectList().block().size();

        // Delete the materialDanio
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, materialDanio.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<MaterialDanio> materialDanioList = materialDanioRepository.findAll().collectList().block();
        assertThat(materialDanioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

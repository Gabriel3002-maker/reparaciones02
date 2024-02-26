package ec.gob.loja.reparaciones.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import ec.gob.loja.reparaciones.IntegrationTest;
import ec.gob.loja.reparaciones.domain.ParametroSistema;
import ec.gob.loja.reparaciones.repository.EntityManager;
import ec.gob.loja.reparaciones.repository.ParametroSistemaRepository;
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
 * Integration tests for the {@link ParametroSistemaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ParametroSistemaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final String DEFAULT_CLASE = "AAAAAAAAAA";
    private static final String UPDATED_CLASE = "BBBBBBBBBB";

    private static final String DEFAULT_VALOR = "AAAAAAAAAA";
    private static final String UPDATED_VALOR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/parametro-sistemas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ParametroSistemaRepository parametroSistemaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private ParametroSistema parametroSistema;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ParametroSistema createEntity(EntityManager em) {
        ParametroSistema parametroSistema = new ParametroSistema()
            .nombre(DEFAULT_NOMBRE)
            .codigo(DEFAULT_CODIGO)
            .clase(DEFAULT_CLASE)
            .valor(DEFAULT_VALOR);
        return parametroSistema;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ParametroSistema createUpdatedEntity(EntityManager em) {
        ParametroSistema parametroSistema = new ParametroSistema()
            .nombre(UPDATED_NOMBRE)
            .codigo(UPDATED_CODIGO)
            .clase(UPDATED_CLASE)
            .valor(UPDATED_VALOR);
        return parametroSistema;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(ParametroSistema.class).block();
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
        parametroSistema = createEntity(em);
    }

    @Test
    void createParametroSistema() throws Exception {
        int databaseSizeBeforeCreate = parametroSistemaRepository.findAll().collectList().block().size();
        // Create the ParametroSistema
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(parametroSistema))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the ParametroSistema in the database
        List<ParametroSistema> parametroSistemaList = parametroSistemaRepository.findAll().collectList().block();
        assertThat(parametroSistemaList).hasSize(databaseSizeBeforeCreate + 1);
        ParametroSistema testParametroSistema = parametroSistemaList.get(parametroSistemaList.size() - 1);
        assertThat(testParametroSistema.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testParametroSistema.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testParametroSistema.getClase()).isEqualTo(DEFAULT_CLASE);
        assertThat(testParametroSistema.getValor()).isEqualTo(DEFAULT_VALOR);
    }

    @Test
    void createParametroSistemaWithExistingId() throws Exception {
        // Create the ParametroSistema with an existing ID
        parametroSistema.setId(1L);

        int databaseSizeBeforeCreate = parametroSistemaRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(parametroSistema))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ParametroSistema in the database
        List<ParametroSistema> parametroSistemaList = parametroSistemaRepository.findAll().collectList().block();
        assertThat(parametroSistemaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = parametroSistemaRepository.findAll().collectList().block().size();
        // set the field null
        parametroSistema.setNombre(null);

        // Create the ParametroSistema, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(parametroSistema))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ParametroSistema> parametroSistemaList = parametroSistemaRepository.findAll().collectList().block();
        assertThat(parametroSistemaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCodigoIsRequired() throws Exception {
        int databaseSizeBeforeTest = parametroSistemaRepository.findAll().collectList().block().size();
        // set the field null
        parametroSistema.setCodigo(null);

        // Create the ParametroSistema, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(parametroSistema))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ParametroSistema> parametroSistemaList = parametroSistemaRepository.findAll().collectList().block();
        assertThat(parametroSistemaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkClaseIsRequired() throws Exception {
        int databaseSizeBeforeTest = parametroSistemaRepository.findAll().collectList().block().size();
        // set the field null
        parametroSistema.setClase(null);

        // Create the ParametroSistema, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(parametroSistema))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ParametroSistema> parametroSistemaList = parametroSistemaRepository.findAll().collectList().block();
        assertThat(parametroSistemaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkValorIsRequired() throws Exception {
        int databaseSizeBeforeTest = parametroSistemaRepository.findAll().collectList().block().size();
        // set the field null
        parametroSistema.setValor(null);

        // Create the ParametroSistema, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(parametroSistema))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ParametroSistema> parametroSistemaList = parametroSistemaRepository.findAll().collectList().block();
        assertThat(parametroSistemaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllParametroSistemasAsStream() {
        // Initialize the database
        parametroSistemaRepository.save(parametroSistema).block();

        List<ParametroSistema> parametroSistemaList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(ParametroSistema.class)
            .getResponseBody()
            .filter(parametroSistema::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(parametroSistemaList).isNotNull();
        assertThat(parametroSistemaList).hasSize(1);
        ParametroSistema testParametroSistema = parametroSistemaList.get(0);
        assertThat(testParametroSistema.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testParametroSistema.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testParametroSistema.getClase()).isEqualTo(DEFAULT_CLASE);
        assertThat(testParametroSistema.getValor()).isEqualTo(DEFAULT_VALOR);
    }

    @Test
    void getAllParametroSistemas() {
        // Initialize the database
        parametroSistemaRepository.save(parametroSistema).block();

        // Get all the parametroSistemaList
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
            .value(hasItem(parametroSistema.getId().intValue()))
            .jsonPath("$.[*].nombre")
            .value(hasItem(DEFAULT_NOMBRE))
            .jsonPath("$.[*].codigo")
            .value(hasItem(DEFAULT_CODIGO))
            .jsonPath("$.[*].clase")
            .value(hasItem(DEFAULT_CLASE))
            .jsonPath("$.[*].valor")
            .value(hasItem(DEFAULT_VALOR));
    }

    @Test
    void getParametroSistema() {
        // Initialize the database
        parametroSistemaRepository.save(parametroSistema).block();

        // Get the parametroSistema
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, parametroSistema.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(parametroSistema.getId().intValue()))
            .jsonPath("$.nombre")
            .value(is(DEFAULT_NOMBRE))
            .jsonPath("$.codigo")
            .value(is(DEFAULT_CODIGO))
            .jsonPath("$.clase")
            .value(is(DEFAULT_CLASE))
            .jsonPath("$.valor")
            .value(is(DEFAULT_VALOR));
    }

    @Test
    void getNonExistingParametroSistema() {
        // Get the parametroSistema
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingParametroSistema() throws Exception {
        // Initialize the database
        parametroSistemaRepository.save(parametroSistema).block();

        int databaseSizeBeforeUpdate = parametroSistemaRepository.findAll().collectList().block().size();

        // Update the parametroSistema
        ParametroSistema updatedParametroSistema = parametroSistemaRepository.findById(parametroSistema.getId()).block();
        updatedParametroSistema.nombre(UPDATED_NOMBRE).codigo(UPDATED_CODIGO).clase(UPDATED_CLASE).valor(UPDATED_VALOR);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedParametroSistema.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedParametroSistema))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ParametroSistema in the database
        List<ParametroSistema> parametroSistemaList = parametroSistemaRepository.findAll().collectList().block();
        assertThat(parametroSistemaList).hasSize(databaseSizeBeforeUpdate);
        ParametroSistema testParametroSistema = parametroSistemaList.get(parametroSistemaList.size() - 1);
        assertThat(testParametroSistema.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testParametroSistema.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testParametroSistema.getClase()).isEqualTo(UPDATED_CLASE);
        assertThat(testParametroSistema.getValor()).isEqualTo(UPDATED_VALOR);
    }

    @Test
    void putNonExistingParametroSistema() throws Exception {
        int databaseSizeBeforeUpdate = parametroSistemaRepository.findAll().collectList().block().size();
        parametroSistema.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, parametroSistema.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(parametroSistema))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ParametroSistema in the database
        List<ParametroSistema> parametroSistemaList = parametroSistemaRepository.findAll().collectList().block();
        assertThat(parametroSistemaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchParametroSistema() throws Exception {
        int databaseSizeBeforeUpdate = parametroSistemaRepository.findAll().collectList().block().size();
        parametroSistema.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(parametroSistema))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ParametroSistema in the database
        List<ParametroSistema> parametroSistemaList = parametroSistemaRepository.findAll().collectList().block();
        assertThat(parametroSistemaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamParametroSistema() throws Exception {
        int databaseSizeBeforeUpdate = parametroSistemaRepository.findAll().collectList().block().size();
        parametroSistema.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(parametroSistema))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ParametroSistema in the database
        List<ParametroSistema> parametroSistemaList = parametroSistemaRepository.findAll().collectList().block();
        assertThat(parametroSistemaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateParametroSistemaWithPatch() throws Exception {
        // Initialize the database
        parametroSistemaRepository.save(parametroSistema).block();

        int databaseSizeBeforeUpdate = parametroSistemaRepository.findAll().collectList().block().size();

        // Update the parametroSistema using partial update
        ParametroSistema partialUpdatedParametroSistema = new ParametroSistema();
        partialUpdatedParametroSistema.setId(parametroSistema.getId());

        partialUpdatedParametroSistema.codigo(UPDATED_CODIGO).clase(UPDATED_CLASE).valor(UPDATED_VALOR);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedParametroSistema.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedParametroSistema))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ParametroSistema in the database
        List<ParametroSistema> parametroSistemaList = parametroSistemaRepository.findAll().collectList().block();
        assertThat(parametroSistemaList).hasSize(databaseSizeBeforeUpdate);
        ParametroSistema testParametroSistema = parametroSistemaList.get(parametroSistemaList.size() - 1);
        assertThat(testParametroSistema.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testParametroSistema.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testParametroSistema.getClase()).isEqualTo(UPDATED_CLASE);
        assertThat(testParametroSistema.getValor()).isEqualTo(UPDATED_VALOR);
    }

    @Test
    void fullUpdateParametroSistemaWithPatch() throws Exception {
        // Initialize the database
        parametroSistemaRepository.save(parametroSistema).block();

        int databaseSizeBeforeUpdate = parametroSistemaRepository.findAll().collectList().block().size();

        // Update the parametroSistema using partial update
        ParametroSistema partialUpdatedParametroSistema = new ParametroSistema();
        partialUpdatedParametroSistema.setId(parametroSistema.getId());

        partialUpdatedParametroSistema.nombre(UPDATED_NOMBRE).codigo(UPDATED_CODIGO).clase(UPDATED_CLASE).valor(UPDATED_VALOR);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedParametroSistema.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedParametroSistema))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ParametroSistema in the database
        List<ParametroSistema> parametroSistemaList = parametroSistemaRepository.findAll().collectList().block();
        assertThat(parametroSistemaList).hasSize(databaseSizeBeforeUpdate);
        ParametroSistema testParametroSistema = parametroSistemaList.get(parametroSistemaList.size() - 1);
        assertThat(testParametroSistema.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testParametroSistema.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testParametroSistema.getClase()).isEqualTo(UPDATED_CLASE);
        assertThat(testParametroSistema.getValor()).isEqualTo(UPDATED_VALOR);
    }

    @Test
    void patchNonExistingParametroSistema() throws Exception {
        int databaseSizeBeforeUpdate = parametroSistemaRepository.findAll().collectList().block().size();
        parametroSistema.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, parametroSistema.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(parametroSistema))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ParametroSistema in the database
        List<ParametroSistema> parametroSistemaList = parametroSistemaRepository.findAll().collectList().block();
        assertThat(parametroSistemaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchParametroSistema() throws Exception {
        int databaseSizeBeforeUpdate = parametroSistemaRepository.findAll().collectList().block().size();
        parametroSistema.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(parametroSistema))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ParametroSistema in the database
        List<ParametroSistema> parametroSistemaList = parametroSistemaRepository.findAll().collectList().block();
        assertThat(parametroSistemaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamParametroSistema() throws Exception {
        int databaseSizeBeforeUpdate = parametroSistemaRepository.findAll().collectList().block().size();
        parametroSistema.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(parametroSistema))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ParametroSistema in the database
        List<ParametroSistema> parametroSistemaList = parametroSistemaRepository.findAll().collectList().block();
        assertThat(parametroSistemaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteParametroSistema() {
        // Initialize the database
        parametroSistemaRepository.save(parametroSistema).block();

        int databaseSizeBeforeDelete = parametroSistemaRepository.findAll().collectList().block().size();

        // Delete the parametroSistema
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, parametroSistema.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<ParametroSistema> parametroSistemaList = parametroSistemaRepository.findAll().collectList().block();
        assertThat(parametroSistemaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

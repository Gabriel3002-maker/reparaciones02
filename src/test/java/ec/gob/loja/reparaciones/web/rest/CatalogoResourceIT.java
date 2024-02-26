package ec.gob.loja.reparaciones.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import ec.gob.loja.reparaciones.IntegrationTest;
import ec.gob.loja.reparaciones.domain.Catalogo;
import ec.gob.loja.reparaciones.repository.CatalogoRepository;
import ec.gob.loja.reparaciones.repository.EntityManager;
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
 * Integration tests for the {@link CatalogoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CatalogoResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/catalogos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CatalogoRepository catalogoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Catalogo catalogo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Catalogo createEntity(EntityManager em) {
        Catalogo catalogo = new Catalogo().nombre(DEFAULT_NOMBRE).codigo(DEFAULT_CODIGO).descripcion(DEFAULT_DESCRIPCION);
        return catalogo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Catalogo createUpdatedEntity(EntityManager em) {
        Catalogo catalogo = new Catalogo().nombre(UPDATED_NOMBRE).codigo(UPDATED_CODIGO).descripcion(UPDATED_DESCRIPCION);
        return catalogo;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Catalogo.class).block();
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
        catalogo = createEntity(em);
    }

    @Test
    void createCatalogo() throws Exception {
        int databaseSizeBeforeCreate = catalogoRepository.findAll().collectList().block().size();
        // Create the Catalogo
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(catalogo))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Catalogo in the database
        List<Catalogo> catalogoList = catalogoRepository.findAll().collectList().block();
        assertThat(catalogoList).hasSize(databaseSizeBeforeCreate + 1);
        Catalogo testCatalogo = catalogoList.get(catalogoList.size() - 1);
        assertThat(testCatalogo.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testCatalogo.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testCatalogo.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    void createCatalogoWithExistingId() throws Exception {
        // Create the Catalogo with an existing ID
        catalogo.setId(1L);

        int databaseSizeBeforeCreate = catalogoRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(catalogo))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Catalogo in the database
        List<Catalogo> catalogoList = catalogoRepository.findAll().collectList().block();
        assertThat(catalogoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = catalogoRepository.findAll().collectList().block().size();
        // set the field null
        catalogo.setNombre(null);

        // Create the Catalogo, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(catalogo))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Catalogo> catalogoList = catalogoRepository.findAll().collectList().block();
        assertThat(catalogoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCodigoIsRequired() throws Exception {
        int databaseSizeBeforeTest = catalogoRepository.findAll().collectList().block().size();
        // set the field null
        catalogo.setCodigo(null);

        // Create the Catalogo, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(catalogo))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Catalogo> catalogoList = catalogoRepository.findAll().collectList().block();
        assertThat(catalogoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllCatalogosAsStream() {
        // Initialize the database
        catalogoRepository.save(catalogo).block();

        List<Catalogo> catalogoList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Catalogo.class)
            .getResponseBody()
            .filter(catalogo::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(catalogoList).isNotNull();
        assertThat(catalogoList).hasSize(1);
        Catalogo testCatalogo = catalogoList.get(0);
        assertThat(testCatalogo.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testCatalogo.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testCatalogo.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    void getAllCatalogos() {
        // Initialize the database
        catalogoRepository.save(catalogo).block();

        // Get all the catalogoList
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
            .value(hasItem(catalogo.getId().intValue()))
            .jsonPath("$.[*].nombre")
            .value(hasItem(DEFAULT_NOMBRE))
            .jsonPath("$.[*].codigo")
            .value(hasItem(DEFAULT_CODIGO))
            .jsonPath("$.[*].descripcion")
            .value(hasItem(DEFAULT_DESCRIPCION));
    }

    @Test
    void getCatalogo() {
        // Initialize the database
        catalogoRepository.save(catalogo).block();

        // Get the catalogo
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, catalogo.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(catalogo.getId().intValue()))
            .jsonPath("$.nombre")
            .value(is(DEFAULT_NOMBRE))
            .jsonPath("$.codigo")
            .value(is(DEFAULT_CODIGO))
            .jsonPath("$.descripcion")
            .value(is(DEFAULT_DESCRIPCION));
    }

    @Test
    void getNonExistingCatalogo() {
        // Get the catalogo
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingCatalogo() throws Exception {
        // Initialize the database
        catalogoRepository.save(catalogo).block();

        int databaseSizeBeforeUpdate = catalogoRepository.findAll().collectList().block().size();

        // Update the catalogo
        Catalogo updatedCatalogo = catalogoRepository.findById(catalogo.getId()).block();
        updatedCatalogo.nombre(UPDATED_NOMBRE).codigo(UPDATED_CODIGO).descripcion(UPDATED_DESCRIPCION);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedCatalogo.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedCatalogo))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Catalogo in the database
        List<Catalogo> catalogoList = catalogoRepository.findAll().collectList().block();
        assertThat(catalogoList).hasSize(databaseSizeBeforeUpdate);
        Catalogo testCatalogo = catalogoList.get(catalogoList.size() - 1);
        assertThat(testCatalogo.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCatalogo.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testCatalogo.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    void putNonExistingCatalogo() throws Exception {
        int databaseSizeBeforeUpdate = catalogoRepository.findAll().collectList().block().size();
        catalogo.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, catalogo.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(catalogo))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Catalogo in the database
        List<Catalogo> catalogoList = catalogoRepository.findAll().collectList().block();
        assertThat(catalogoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCatalogo() throws Exception {
        int databaseSizeBeforeUpdate = catalogoRepository.findAll().collectList().block().size();
        catalogo.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(catalogo))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Catalogo in the database
        List<Catalogo> catalogoList = catalogoRepository.findAll().collectList().block();
        assertThat(catalogoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCatalogo() throws Exception {
        int databaseSizeBeforeUpdate = catalogoRepository.findAll().collectList().block().size();
        catalogo.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(catalogo))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Catalogo in the database
        List<Catalogo> catalogoList = catalogoRepository.findAll().collectList().block();
        assertThat(catalogoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCatalogoWithPatch() throws Exception {
        // Initialize the database
        catalogoRepository.save(catalogo).block();

        int databaseSizeBeforeUpdate = catalogoRepository.findAll().collectList().block().size();

        // Update the catalogo using partial update
        Catalogo partialUpdatedCatalogo = new Catalogo();
        partialUpdatedCatalogo.setId(catalogo.getId());

        partialUpdatedCatalogo.nombre(UPDATED_NOMBRE).codigo(UPDATED_CODIGO).descripcion(UPDATED_DESCRIPCION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCatalogo.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCatalogo))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Catalogo in the database
        List<Catalogo> catalogoList = catalogoRepository.findAll().collectList().block();
        assertThat(catalogoList).hasSize(databaseSizeBeforeUpdate);
        Catalogo testCatalogo = catalogoList.get(catalogoList.size() - 1);
        assertThat(testCatalogo.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCatalogo.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testCatalogo.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    void fullUpdateCatalogoWithPatch() throws Exception {
        // Initialize the database
        catalogoRepository.save(catalogo).block();

        int databaseSizeBeforeUpdate = catalogoRepository.findAll().collectList().block().size();

        // Update the catalogo using partial update
        Catalogo partialUpdatedCatalogo = new Catalogo();
        partialUpdatedCatalogo.setId(catalogo.getId());

        partialUpdatedCatalogo.nombre(UPDATED_NOMBRE).codigo(UPDATED_CODIGO).descripcion(UPDATED_DESCRIPCION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCatalogo.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCatalogo))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Catalogo in the database
        List<Catalogo> catalogoList = catalogoRepository.findAll().collectList().block();
        assertThat(catalogoList).hasSize(databaseSizeBeforeUpdate);
        Catalogo testCatalogo = catalogoList.get(catalogoList.size() - 1);
        assertThat(testCatalogo.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCatalogo.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testCatalogo.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    void patchNonExistingCatalogo() throws Exception {
        int databaseSizeBeforeUpdate = catalogoRepository.findAll().collectList().block().size();
        catalogo.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, catalogo.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(catalogo))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Catalogo in the database
        List<Catalogo> catalogoList = catalogoRepository.findAll().collectList().block();
        assertThat(catalogoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCatalogo() throws Exception {
        int databaseSizeBeforeUpdate = catalogoRepository.findAll().collectList().block().size();
        catalogo.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(catalogo))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Catalogo in the database
        List<Catalogo> catalogoList = catalogoRepository.findAll().collectList().block();
        assertThat(catalogoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCatalogo() throws Exception {
        int databaseSizeBeforeUpdate = catalogoRepository.findAll().collectList().block().size();
        catalogo.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(catalogo))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Catalogo in the database
        List<Catalogo> catalogoList = catalogoRepository.findAll().collectList().block();
        assertThat(catalogoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCatalogo() {
        // Initialize the database
        catalogoRepository.save(catalogo).block();

        int databaseSizeBeforeDelete = catalogoRepository.findAll().collectList().block().size();

        // Delete the catalogo
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, catalogo.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Catalogo> catalogoList = catalogoRepository.findAll().collectList().block();
        assertThat(catalogoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

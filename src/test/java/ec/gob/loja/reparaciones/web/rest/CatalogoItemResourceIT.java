package ec.gob.loja.reparaciones.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import ec.gob.loja.reparaciones.IntegrationTest;
import ec.gob.loja.reparaciones.domain.CatalogoItem;
import ec.gob.loja.reparaciones.repository.CatalogoItemRepository;
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
 * Integration tests for the {@link CatalogoItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CatalogoItemResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String DEFAULT_CATALOGO_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CATALOGO_CODIGO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVO = false;
    private static final Boolean UPDATED_ACTIVO = true;

    private static final String ENTITY_API_URL = "/api/catalogo-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CatalogoItemRepository catalogoItemRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private CatalogoItem catalogoItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CatalogoItem createEntity(EntityManager em) {
        CatalogoItem catalogoItem = new CatalogoItem()
            .nombre(DEFAULT_NOMBRE)
            .codigo(DEFAULT_CODIGO)
            .descripcion(DEFAULT_DESCRIPCION)
            .catalogoCodigo(DEFAULT_CATALOGO_CODIGO)
            .activo(DEFAULT_ACTIVO);
        return catalogoItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CatalogoItem createUpdatedEntity(EntityManager em) {
        CatalogoItem catalogoItem = new CatalogoItem()
            .nombre(UPDATED_NOMBRE)
            .codigo(UPDATED_CODIGO)
            .descripcion(UPDATED_DESCRIPCION)
            .catalogoCodigo(UPDATED_CATALOGO_CODIGO)
            .activo(UPDATED_ACTIVO);
        return catalogoItem;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(CatalogoItem.class).block();
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
        catalogoItem = createEntity(em);
    }

    @Test
    void createCatalogoItem() throws Exception {
        int databaseSizeBeforeCreate = catalogoItemRepository.findAll().collectList().block().size();
        // Create the CatalogoItem
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(catalogoItem))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the CatalogoItem in the database
        List<CatalogoItem> catalogoItemList = catalogoItemRepository.findAll().collectList().block();
        assertThat(catalogoItemList).hasSize(databaseSizeBeforeCreate + 1);
        CatalogoItem testCatalogoItem = catalogoItemList.get(catalogoItemList.size() - 1);
        assertThat(testCatalogoItem.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testCatalogoItem.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testCatalogoItem.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testCatalogoItem.getCatalogoCodigo()).isEqualTo(DEFAULT_CATALOGO_CODIGO);
        assertThat(testCatalogoItem.getActivo()).isEqualTo(DEFAULT_ACTIVO);
    }

    @Test
    void createCatalogoItemWithExistingId() throws Exception {
        // Create the CatalogoItem with an existing ID
        catalogoItem.setId(1L);

        int databaseSizeBeforeCreate = catalogoItemRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(catalogoItem))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CatalogoItem in the database
        List<CatalogoItem> catalogoItemList = catalogoItemRepository.findAll().collectList().block();
        assertThat(catalogoItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = catalogoItemRepository.findAll().collectList().block().size();
        // set the field null
        catalogoItem.setNombre(null);

        // Create the CatalogoItem, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(catalogoItem))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<CatalogoItem> catalogoItemList = catalogoItemRepository.findAll().collectList().block();
        assertThat(catalogoItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCodigoIsRequired() throws Exception {
        int databaseSizeBeforeTest = catalogoItemRepository.findAll().collectList().block().size();
        // set the field null
        catalogoItem.setCodigo(null);

        // Create the CatalogoItem, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(catalogoItem))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<CatalogoItem> catalogoItemList = catalogoItemRepository.findAll().collectList().block();
        assertThat(catalogoItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCatalogoCodigoIsRequired() throws Exception {
        int databaseSizeBeforeTest = catalogoItemRepository.findAll().collectList().block().size();
        // set the field null
        catalogoItem.setCatalogoCodigo(null);

        // Create the CatalogoItem, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(catalogoItem))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<CatalogoItem> catalogoItemList = catalogoItemRepository.findAll().collectList().block();
        assertThat(catalogoItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllCatalogoItemsAsStream() {
        // Initialize the database
        catalogoItemRepository.save(catalogoItem).block();

        List<CatalogoItem> catalogoItemList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(CatalogoItem.class)
            .getResponseBody()
            .filter(catalogoItem::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(catalogoItemList).isNotNull();
        assertThat(catalogoItemList).hasSize(1);
        CatalogoItem testCatalogoItem = catalogoItemList.get(0);
        assertThat(testCatalogoItem.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testCatalogoItem.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testCatalogoItem.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testCatalogoItem.getCatalogoCodigo()).isEqualTo(DEFAULT_CATALOGO_CODIGO);
        assertThat(testCatalogoItem.getActivo()).isEqualTo(DEFAULT_ACTIVO);
    }

    @Test
    void getAllCatalogoItems() {
        // Initialize the database
        catalogoItemRepository.save(catalogoItem).block();

        // Get all the catalogoItemList
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
            .value(hasItem(catalogoItem.getId().intValue()))
            .jsonPath("$.[*].nombre")
            .value(hasItem(DEFAULT_NOMBRE))
            .jsonPath("$.[*].codigo")
            .value(hasItem(DEFAULT_CODIGO))
            .jsonPath("$.[*].descripcion")
            .value(hasItem(DEFAULT_DESCRIPCION))
            .jsonPath("$.[*].catalogoCodigo")
            .value(hasItem(DEFAULT_CATALOGO_CODIGO))
            .jsonPath("$.[*].activo")
            .value(hasItem(DEFAULT_ACTIVO.booleanValue()));
    }

    @Test
    void getCatalogoItem() {
        // Initialize the database
        catalogoItemRepository.save(catalogoItem).block();

        // Get the catalogoItem
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, catalogoItem.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(catalogoItem.getId().intValue()))
            .jsonPath("$.nombre")
            .value(is(DEFAULT_NOMBRE))
            .jsonPath("$.codigo")
            .value(is(DEFAULT_CODIGO))
            .jsonPath("$.descripcion")
            .value(is(DEFAULT_DESCRIPCION))
            .jsonPath("$.catalogoCodigo")
            .value(is(DEFAULT_CATALOGO_CODIGO))
            .jsonPath("$.activo")
            .value(is(DEFAULT_ACTIVO.booleanValue()));
    }

    @Test
    void getNonExistingCatalogoItem() {
        // Get the catalogoItem
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingCatalogoItem() throws Exception {
        // Initialize the database
        catalogoItemRepository.save(catalogoItem).block();

        int databaseSizeBeforeUpdate = catalogoItemRepository.findAll().collectList().block().size();

        // Update the catalogoItem
        CatalogoItem updatedCatalogoItem = catalogoItemRepository.findById(catalogoItem.getId()).block();
        updatedCatalogoItem
            .nombre(UPDATED_NOMBRE)
            .codigo(UPDATED_CODIGO)
            .descripcion(UPDATED_DESCRIPCION)
            .catalogoCodigo(UPDATED_CATALOGO_CODIGO)
            .activo(UPDATED_ACTIVO);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedCatalogoItem.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedCatalogoItem))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CatalogoItem in the database
        List<CatalogoItem> catalogoItemList = catalogoItemRepository.findAll().collectList().block();
        assertThat(catalogoItemList).hasSize(databaseSizeBeforeUpdate);
        CatalogoItem testCatalogoItem = catalogoItemList.get(catalogoItemList.size() - 1);
        assertThat(testCatalogoItem.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCatalogoItem.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testCatalogoItem.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testCatalogoItem.getCatalogoCodigo()).isEqualTo(UPDATED_CATALOGO_CODIGO);
        assertThat(testCatalogoItem.getActivo()).isEqualTo(UPDATED_ACTIVO);
    }

    @Test
    void putNonExistingCatalogoItem() throws Exception {
        int databaseSizeBeforeUpdate = catalogoItemRepository.findAll().collectList().block().size();
        catalogoItem.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, catalogoItem.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(catalogoItem))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CatalogoItem in the database
        List<CatalogoItem> catalogoItemList = catalogoItemRepository.findAll().collectList().block();
        assertThat(catalogoItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCatalogoItem() throws Exception {
        int databaseSizeBeforeUpdate = catalogoItemRepository.findAll().collectList().block().size();
        catalogoItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(catalogoItem))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CatalogoItem in the database
        List<CatalogoItem> catalogoItemList = catalogoItemRepository.findAll().collectList().block();
        assertThat(catalogoItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCatalogoItem() throws Exception {
        int databaseSizeBeforeUpdate = catalogoItemRepository.findAll().collectList().block().size();
        catalogoItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(catalogoItem))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CatalogoItem in the database
        List<CatalogoItem> catalogoItemList = catalogoItemRepository.findAll().collectList().block();
        assertThat(catalogoItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCatalogoItemWithPatch() throws Exception {
        // Initialize the database
        catalogoItemRepository.save(catalogoItem).block();

        int databaseSizeBeforeUpdate = catalogoItemRepository.findAll().collectList().block().size();

        // Update the catalogoItem using partial update
        CatalogoItem partialUpdatedCatalogoItem = new CatalogoItem();
        partialUpdatedCatalogoItem.setId(catalogoItem.getId());

        partialUpdatedCatalogoItem.catalogoCodigo(UPDATED_CATALOGO_CODIGO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCatalogoItem.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCatalogoItem))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CatalogoItem in the database
        List<CatalogoItem> catalogoItemList = catalogoItemRepository.findAll().collectList().block();
        assertThat(catalogoItemList).hasSize(databaseSizeBeforeUpdate);
        CatalogoItem testCatalogoItem = catalogoItemList.get(catalogoItemList.size() - 1);
        assertThat(testCatalogoItem.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testCatalogoItem.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testCatalogoItem.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testCatalogoItem.getCatalogoCodigo()).isEqualTo(UPDATED_CATALOGO_CODIGO);
        assertThat(testCatalogoItem.getActivo()).isEqualTo(DEFAULT_ACTIVO);
    }

    @Test
    void fullUpdateCatalogoItemWithPatch() throws Exception {
        // Initialize the database
        catalogoItemRepository.save(catalogoItem).block();

        int databaseSizeBeforeUpdate = catalogoItemRepository.findAll().collectList().block().size();

        // Update the catalogoItem using partial update
        CatalogoItem partialUpdatedCatalogoItem = new CatalogoItem();
        partialUpdatedCatalogoItem.setId(catalogoItem.getId());

        partialUpdatedCatalogoItem
            .nombre(UPDATED_NOMBRE)
            .codigo(UPDATED_CODIGO)
            .descripcion(UPDATED_DESCRIPCION)
            .catalogoCodigo(UPDATED_CATALOGO_CODIGO)
            .activo(UPDATED_ACTIVO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCatalogoItem.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCatalogoItem))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CatalogoItem in the database
        List<CatalogoItem> catalogoItemList = catalogoItemRepository.findAll().collectList().block();
        assertThat(catalogoItemList).hasSize(databaseSizeBeforeUpdate);
        CatalogoItem testCatalogoItem = catalogoItemList.get(catalogoItemList.size() - 1);
        assertThat(testCatalogoItem.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCatalogoItem.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testCatalogoItem.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testCatalogoItem.getCatalogoCodigo()).isEqualTo(UPDATED_CATALOGO_CODIGO);
        assertThat(testCatalogoItem.getActivo()).isEqualTo(UPDATED_ACTIVO);
    }

    @Test
    void patchNonExistingCatalogoItem() throws Exception {
        int databaseSizeBeforeUpdate = catalogoItemRepository.findAll().collectList().block().size();
        catalogoItem.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, catalogoItem.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(catalogoItem))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CatalogoItem in the database
        List<CatalogoItem> catalogoItemList = catalogoItemRepository.findAll().collectList().block();
        assertThat(catalogoItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCatalogoItem() throws Exception {
        int databaseSizeBeforeUpdate = catalogoItemRepository.findAll().collectList().block().size();
        catalogoItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(catalogoItem))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CatalogoItem in the database
        List<CatalogoItem> catalogoItemList = catalogoItemRepository.findAll().collectList().block();
        assertThat(catalogoItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCatalogoItem() throws Exception {
        int databaseSizeBeforeUpdate = catalogoItemRepository.findAll().collectList().block().size();
        catalogoItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(catalogoItem))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CatalogoItem in the database
        List<CatalogoItem> catalogoItemList = catalogoItemRepository.findAll().collectList().block();
        assertThat(catalogoItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCatalogoItem() {
        // Initialize the database
        catalogoItemRepository.save(catalogoItem).block();

        int databaseSizeBeforeDelete = catalogoItemRepository.findAll().collectList().block().size();

        // Delete the catalogoItem
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, catalogoItem.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<CatalogoItem> catalogoItemList = catalogoItemRepository.findAll().collectList().block();
        assertThat(catalogoItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

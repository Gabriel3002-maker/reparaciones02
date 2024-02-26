package ec.gob.loja.reparaciones.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import ec.gob.loja.reparaciones.IntegrationTest;
import ec.gob.loja.reparaciones.domain.CatalogoItem;
import ec.gob.loja.reparaciones.domain.Material;
import ec.gob.loja.reparaciones.repository.EntityManager;
import ec.gob.loja.reparaciones.repository.MaterialRepository;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

/**
 * Integration tests for the {@link MaterialResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class MaterialResourceIT {

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final Double DEFAULT_VALOR_UNITARIO = 1D;
    private static final Double UPDATED_VALOR_UNITARIO = 2D;

    private static final Integer DEFAULT_STOCK = 1;
    private static final Integer UPDATED_STOCK = 2;

    private static final Boolean DEFAULT_ACTIVO = false;
    private static final Boolean UPDATED_ACTIVO = true;

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String DEFAULT_CREADO_POR = "AAAAAAAAAA";
    private static final String UPDATED_CREADO_POR = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_CREACION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_CREACION = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_ACTUALIZADO_POR = "AAAAAAAAAA";
    private static final String UPDATED_ACTUALIZADO_POR = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_MODIFICACION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_MODIFICACION = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/materials";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MaterialRepository materialRepository;

    @Mock
    private MaterialRepository materialRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Material material;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Material createEntity(EntityManager em) {
        Material material = new Material()
            .codigo(DEFAULT_CODIGO)
            .nombre(DEFAULT_NOMBRE)
            .valorUnitario(DEFAULT_VALOR_UNITARIO)
            .stock(DEFAULT_STOCK)
            .activo(DEFAULT_ACTIVO)
            .descripcion(DEFAULT_DESCRIPCION)
            .creadoPor(DEFAULT_CREADO_POR)
            .fechaCreacion(DEFAULT_FECHA_CREACION)
            .actualizadoPor(DEFAULT_ACTUALIZADO_POR)
            .fechaModificacion(DEFAULT_FECHA_MODIFICACION);
        // Add required entity
        CatalogoItem catalogoItem;
        catalogoItem = em.insert(CatalogoItemResourceIT.createEntity(em)).block();
        material.setCategoria(catalogoItem);
        return material;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Material createUpdatedEntity(EntityManager em) {
        Material material = new Material()
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .valorUnitario(UPDATED_VALOR_UNITARIO)
            .stock(UPDATED_STOCK)
            .activo(UPDATED_ACTIVO)
            .descripcion(UPDATED_DESCRIPCION)
            .creadoPor(UPDATED_CREADO_POR)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .actualizadoPor(UPDATED_ACTUALIZADO_POR)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION);
        // Add required entity
        CatalogoItem catalogoItem;
        catalogoItem = em.insert(CatalogoItemResourceIT.createUpdatedEntity(em)).block();
        material.setCategoria(catalogoItem);
        return material;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Material.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        CatalogoItemResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        material = createEntity(em);
    }

    @Test
    void createMaterial() throws Exception {
        int databaseSizeBeforeCreate = materialRepository.findAll().collectList().block().size();
        // Create the Material
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(material))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Material in the database
        List<Material> materialList = materialRepository.findAll().collectList().block();
        assertThat(materialList).hasSize(databaseSizeBeforeCreate + 1);
        Material testMaterial = materialList.get(materialList.size() - 1);
        assertThat(testMaterial.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testMaterial.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testMaterial.getValorUnitario()).isEqualTo(DEFAULT_VALOR_UNITARIO);
        assertThat(testMaterial.getStock()).isEqualTo(DEFAULT_STOCK);
        assertThat(testMaterial.getActivo()).isEqualTo(DEFAULT_ACTIVO);
        assertThat(testMaterial.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testMaterial.getCreadoPor()).isEqualTo(DEFAULT_CREADO_POR);
        assertThat(testMaterial.getFechaCreacion()).isEqualTo(DEFAULT_FECHA_CREACION);
        assertThat(testMaterial.getActualizadoPor()).isEqualTo(DEFAULT_ACTUALIZADO_POR);
        assertThat(testMaterial.getFechaModificacion()).isEqualTo(DEFAULT_FECHA_MODIFICACION);
    }

    @Test
    void createMaterialWithExistingId() throws Exception {
        // Create the Material with an existing ID
        material.setId(1L);

        int databaseSizeBeforeCreate = materialRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(material))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Material in the database
        List<Material> materialList = materialRepository.findAll().collectList().block();
        assertThat(materialList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = materialRepository.findAll().collectList().block().size();
        // set the field null
        material.setNombre(null);

        // Create the Material, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(material))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Material> materialList = materialRepository.findAll().collectList().block();
        assertThat(materialList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkValorUnitarioIsRequired() throws Exception {
        int databaseSizeBeforeTest = materialRepository.findAll().collectList().block().size();
        // set the field null
        material.setValorUnitario(null);

        // Create the Material, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(material))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Material> materialList = materialRepository.findAll().collectList().block();
        assertThat(materialList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllMaterialsAsStream() {
        // Initialize the database
        materialRepository.save(material).block();

        List<Material> materialList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Material.class)
            .getResponseBody()
            .filter(material::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(materialList).isNotNull();
        assertThat(materialList).hasSize(1);
        Material testMaterial = materialList.get(0);
        assertThat(testMaterial.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testMaterial.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testMaterial.getValorUnitario()).isEqualTo(DEFAULT_VALOR_UNITARIO);
        assertThat(testMaterial.getStock()).isEqualTo(DEFAULT_STOCK);
        assertThat(testMaterial.getActivo()).isEqualTo(DEFAULT_ACTIVO);
        assertThat(testMaterial.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testMaterial.getCreadoPor()).isEqualTo(DEFAULT_CREADO_POR);
        assertThat(testMaterial.getFechaCreacion()).isEqualTo(DEFAULT_FECHA_CREACION);
        assertThat(testMaterial.getActualizadoPor()).isEqualTo(DEFAULT_ACTUALIZADO_POR);
        assertThat(testMaterial.getFechaModificacion()).isEqualTo(DEFAULT_FECHA_MODIFICACION);
    }

    @Test
    void getAllMaterials() {
        // Initialize the database
        materialRepository.save(material).block();

        // Get all the materialList
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
            .value(hasItem(material.getId().intValue()))
            .jsonPath("$.[*].codigo")
            .value(hasItem(DEFAULT_CODIGO))
            .jsonPath("$.[*].nombre")
            .value(hasItem(DEFAULT_NOMBRE))
            .jsonPath("$.[*].valorUnitario")
            .value(hasItem(DEFAULT_VALOR_UNITARIO.doubleValue()))
            .jsonPath("$.[*].stock")
            .value(hasItem(DEFAULT_STOCK))
            .jsonPath("$.[*].activo")
            .value(hasItem(DEFAULT_ACTIVO.booleanValue()))
            .jsonPath("$.[*].descripcion")
            .value(hasItem(DEFAULT_DESCRIPCION))
            .jsonPath("$.[*].creadoPor")
            .value(hasItem(DEFAULT_CREADO_POR))
            .jsonPath("$.[*].fechaCreacion")
            .value(hasItem(DEFAULT_FECHA_CREACION.toString()))
            .jsonPath("$.[*].actualizadoPor")
            .value(hasItem(DEFAULT_ACTUALIZADO_POR))
            .jsonPath("$.[*].fechaModificacion")
            .value(hasItem(DEFAULT_FECHA_MODIFICACION.toString()));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMaterialsWithEagerRelationshipsIsEnabled() {
        when(materialRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(materialRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMaterialsWithEagerRelationshipsIsNotEnabled() {
        when(materialRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(materialRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getMaterial() {
        // Initialize the database
        materialRepository.save(material).block();

        // Get the material
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, material.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(material.getId().intValue()))
            .jsonPath("$.codigo")
            .value(is(DEFAULT_CODIGO))
            .jsonPath("$.nombre")
            .value(is(DEFAULT_NOMBRE))
            .jsonPath("$.valorUnitario")
            .value(is(DEFAULT_VALOR_UNITARIO.doubleValue()))
            .jsonPath("$.stock")
            .value(is(DEFAULT_STOCK))
            .jsonPath("$.activo")
            .value(is(DEFAULT_ACTIVO.booleanValue()))
            .jsonPath("$.descripcion")
            .value(is(DEFAULT_DESCRIPCION))
            .jsonPath("$.creadoPor")
            .value(is(DEFAULT_CREADO_POR))
            .jsonPath("$.fechaCreacion")
            .value(is(DEFAULT_FECHA_CREACION.toString()))
            .jsonPath("$.actualizadoPor")
            .value(is(DEFAULT_ACTUALIZADO_POR))
            .jsonPath("$.fechaModificacion")
            .value(is(DEFAULT_FECHA_MODIFICACION.toString()));
    }

    @Test
    void getNonExistingMaterial() {
        // Get the material
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingMaterial() throws Exception {
        // Initialize the database
        materialRepository.save(material).block();

        int databaseSizeBeforeUpdate = materialRepository.findAll().collectList().block().size();

        // Update the material
        Material updatedMaterial = materialRepository.findById(material.getId()).block();
        updatedMaterial
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .valorUnitario(UPDATED_VALOR_UNITARIO)
            .stock(UPDATED_STOCK)
            .activo(UPDATED_ACTIVO)
            .descripcion(UPDATED_DESCRIPCION)
            .creadoPor(UPDATED_CREADO_POR)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .actualizadoPor(UPDATED_ACTUALIZADO_POR)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedMaterial.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedMaterial))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Material in the database
        List<Material> materialList = materialRepository.findAll().collectList().block();
        assertThat(materialList).hasSize(databaseSizeBeforeUpdate);
        Material testMaterial = materialList.get(materialList.size() - 1);
        assertThat(testMaterial.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testMaterial.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testMaterial.getValorUnitario()).isEqualTo(UPDATED_VALOR_UNITARIO);
        assertThat(testMaterial.getStock()).isEqualTo(UPDATED_STOCK);
        assertThat(testMaterial.getActivo()).isEqualTo(UPDATED_ACTIVO);
        assertThat(testMaterial.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testMaterial.getCreadoPor()).isEqualTo(UPDATED_CREADO_POR);
        assertThat(testMaterial.getFechaCreacion()).isEqualTo(UPDATED_FECHA_CREACION);
        assertThat(testMaterial.getActualizadoPor()).isEqualTo(UPDATED_ACTUALIZADO_POR);
        assertThat(testMaterial.getFechaModificacion()).isEqualTo(UPDATED_FECHA_MODIFICACION);
    }

    @Test
    void putNonExistingMaterial() throws Exception {
        int databaseSizeBeforeUpdate = materialRepository.findAll().collectList().block().size();
        material.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, material.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(material))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Material in the database
        List<Material> materialList = materialRepository.findAll().collectList().block();
        assertThat(materialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchMaterial() throws Exception {
        int databaseSizeBeforeUpdate = materialRepository.findAll().collectList().block().size();
        material.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(material))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Material in the database
        List<Material> materialList = materialRepository.findAll().collectList().block();
        assertThat(materialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamMaterial() throws Exception {
        int databaseSizeBeforeUpdate = materialRepository.findAll().collectList().block().size();
        material.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(material))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Material in the database
        List<Material> materialList = materialRepository.findAll().collectList().block();
        assertThat(materialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateMaterialWithPatch() throws Exception {
        // Initialize the database
        materialRepository.save(material).block();

        int databaseSizeBeforeUpdate = materialRepository.findAll().collectList().block().size();

        // Update the material using partial update
        Material partialUpdatedMaterial = new Material();
        partialUpdatedMaterial.setId(material.getId());

        partialUpdatedMaterial
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .valorUnitario(UPDATED_VALOR_UNITARIO)
            .stock(UPDATED_STOCK)
            .descripcion(UPDATED_DESCRIPCION)
            .creadoPor(UPDATED_CREADO_POR)
            .actualizadoPor(UPDATED_ACTUALIZADO_POR)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMaterial.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedMaterial))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Material in the database
        List<Material> materialList = materialRepository.findAll().collectList().block();
        assertThat(materialList).hasSize(databaseSizeBeforeUpdate);
        Material testMaterial = materialList.get(materialList.size() - 1);
        assertThat(testMaterial.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testMaterial.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testMaterial.getValorUnitario()).isEqualTo(UPDATED_VALOR_UNITARIO);
        assertThat(testMaterial.getStock()).isEqualTo(UPDATED_STOCK);
        assertThat(testMaterial.getActivo()).isEqualTo(DEFAULT_ACTIVO);
        assertThat(testMaterial.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testMaterial.getCreadoPor()).isEqualTo(UPDATED_CREADO_POR);
        assertThat(testMaterial.getFechaCreacion()).isEqualTo(DEFAULT_FECHA_CREACION);
        assertThat(testMaterial.getActualizadoPor()).isEqualTo(UPDATED_ACTUALIZADO_POR);
        assertThat(testMaterial.getFechaModificacion()).isEqualTo(UPDATED_FECHA_MODIFICACION);
    }

    @Test
    void fullUpdateMaterialWithPatch() throws Exception {
        // Initialize the database
        materialRepository.save(material).block();

        int databaseSizeBeforeUpdate = materialRepository.findAll().collectList().block().size();

        // Update the material using partial update
        Material partialUpdatedMaterial = new Material();
        partialUpdatedMaterial.setId(material.getId());

        partialUpdatedMaterial
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .valorUnitario(UPDATED_VALOR_UNITARIO)
            .stock(UPDATED_STOCK)
            .activo(UPDATED_ACTIVO)
            .descripcion(UPDATED_DESCRIPCION)
            .creadoPor(UPDATED_CREADO_POR)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .actualizadoPor(UPDATED_ACTUALIZADO_POR)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMaterial.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedMaterial))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Material in the database
        List<Material> materialList = materialRepository.findAll().collectList().block();
        assertThat(materialList).hasSize(databaseSizeBeforeUpdate);
        Material testMaterial = materialList.get(materialList.size() - 1);
        assertThat(testMaterial.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testMaterial.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testMaterial.getValorUnitario()).isEqualTo(UPDATED_VALOR_UNITARIO);
        assertThat(testMaterial.getStock()).isEqualTo(UPDATED_STOCK);
        assertThat(testMaterial.getActivo()).isEqualTo(UPDATED_ACTIVO);
        assertThat(testMaterial.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testMaterial.getCreadoPor()).isEqualTo(UPDATED_CREADO_POR);
        assertThat(testMaterial.getFechaCreacion()).isEqualTo(UPDATED_FECHA_CREACION);
        assertThat(testMaterial.getActualizadoPor()).isEqualTo(UPDATED_ACTUALIZADO_POR);
        assertThat(testMaterial.getFechaModificacion()).isEqualTo(UPDATED_FECHA_MODIFICACION);
    }

    @Test
    void patchNonExistingMaterial() throws Exception {
        int databaseSizeBeforeUpdate = materialRepository.findAll().collectList().block().size();
        material.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, material.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(material))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Material in the database
        List<Material> materialList = materialRepository.findAll().collectList().block();
        assertThat(materialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchMaterial() throws Exception {
        int databaseSizeBeforeUpdate = materialRepository.findAll().collectList().block().size();
        material.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(material))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Material in the database
        List<Material> materialList = materialRepository.findAll().collectList().block();
        assertThat(materialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamMaterial() throws Exception {
        int databaseSizeBeforeUpdate = materialRepository.findAll().collectList().block().size();
        material.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(material))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Material in the database
        List<Material> materialList = materialRepository.findAll().collectList().block();
        assertThat(materialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteMaterial() {
        // Initialize the database
        materialRepository.save(material).block();

        int databaseSizeBeforeDelete = materialRepository.findAll().collectList().block().size();

        // Delete the material
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, material.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Material> materialList = materialRepository.findAll().collectList().block();
        assertThat(materialList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

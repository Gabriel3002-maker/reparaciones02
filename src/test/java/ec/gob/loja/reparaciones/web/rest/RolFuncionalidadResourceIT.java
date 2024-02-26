package ec.gob.loja.reparaciones.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import ec.gob.loja.reparaciones.IntegrationTest;
import ec.gob.loja.reparaciones.domain.Funcionalidad;
import ec.gob.loja.reparaciones.domain.RolFuncionalidad;
import ec.gob.loja.reparaciones.repository.EntityManager;
import ec.gob.loja.reparaciones.repository.RolFuncionalidadRepository;
import java.time.Duration;
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
 * Integration tests for the {@link RolFuncionalidadResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class RolFuncionalidadResourceIT {

    private static final String DEFAULT_ROL = "AAAAAAAAAA";
    private static final String UPDATED_ROL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVO = false;
    private static final Boolean UPDATED_ACTIVO = true;

    private static final Integer DEFAULT_PRIORIDAD = 1;
    private static final Integer UPDATED_PRIORIDAD = 2;

    private static final String ENTITY_API_URL = "/api/rol-funcionalidads";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RolFuncionalidadRepository rolFuncionalidadRepository;

    @Mock
    private RolFuncionalidadRepository rolFuncionalidadRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private RolFuncionalidad rolFuncionalidad;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RolFuncionalidad createEntity(EntityManager em) {
        RolFuncionalidad rolFuncionalidad = new RolFuncionalidad().rol(DEFAULT_ROL).activo(DEFAULT_ACTIVO).prioridad(DEFAULT_PRIORIDAD);
        // Add required entity
        Funcionalidad funcionalidad;
        funcionalidad = em.insert(FuncionalidadResourceIT.createEntity(em)).block();
        rolFuncionalidad.setFuncionalidad(funcionalidad);
        return rolFuncionalidad;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RolFuncionalidad createUpdatedEntity(EntityManager em) {
        RolFuncionalidad rolFuncionalidad = new RolFuncionalidad().rol(UPDATED_ROL).activo(UPDATED_ACTIVO).prioridad(UPDATED_PRIORIDAD);
        // Add required entity
        Funcionalidad funcionalidad;
        funcionalidad = em.insert(FuncionalidadResourceIT.createUpdatedEntity(em)).block();
        rolFuncionalidad.setFuncionalidad(funcionalidad);
        return rolFuncionalidad;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(RolFuncionalidad.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        FuncionalidadResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        rolFuncionalidad = createEntity(em);
    }

    @Test
    void createRolFuncionalidad() throws Exception {
        int databaseSizeBeforeCreate = rolFuncionalidadRepository.findAll().collectList().block().size();
        // Create the RolFuncionalidad
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(rolFuncionalidad))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the RolFuncionalidad in the database
        List<RolFuncionalidad> rolFuncionalidadList = rolFuncionalidadRepository.findAll().collectList().block();
        assertThat(rolFuncionalidadList).hasSize(databaseSizeBeforeCreate + 1);
        RolFuncionalidad testRolFuncionalidad = rolFuncionalidadList.get(rolFuncionalidadList.size() - 1);
        assertThat(testRolFuncionalidad.getRol()).isEqualTo(DEFAULT_ROL);
        assertThat(testRolFuncionalidad.getActivo()).isEqualTo(DEFAULT_ACTIVO);
        assertThat(testRolFuncionalidad.getPrioridad()).isEqualTo(DEFAULT_PRIORIDAD);
    }

    @Test
    void createRolFuncionalidadWithExistingId() throws Exception {
        // Create the RolFuncionalidad with an existing ID
        rolFuncionalidad.setId(1L);

        int databaseSizeBeforeCreate = rolFuncionalidadRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(rolFuncionalidad))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RolFuncionalidad in the database
        List<RolFuncionalidad> rolFuncionalidadList = rolFuncionalidadRepository.findAll().collectList().block();
        assertThat(rolFuncionalidadList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkRolIsRequired() throws Exception {
        int databaseSizeBeforeTest = rolFuncionalidadRepository.findAll().collectList().block().size();
        // set the field null
        rolFuncionalidad.setRol(null);

        // Create the RolFuncionalidad, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(rolFuncionalidad))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<RolFuncionalidad> rolFuncionalidadList = rolFuncionalidadRepository.findAll().collectList().block();
        assertThat(rolFuncionalidadList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPrioridadIsRequired() throws Exception {
        int databaseSizeBeforeTest = rolFuncionalidadRepository.findAll().collectList().block().size();
        // set the field null
        rolFuncionalidad.setPrioridad(null);

        // Create the RolFuncionalidad, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(rolFuncionalidad))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<RolFuncionalidad> rolFuncionalidadList = rolFuncionalidadRepository.findAll().collectList().block();
        assertThat(rolFuncionalidadList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllRolFuncionalidadsAsStream() {
        // Initialize the database
        rolFuncionalidadRepository.save(rolFuncionalidad).block();

        List<RolFuncionalidad> rolFuncionalidadList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(RolFuncionalidad.class)
            .getResponseBody()
            .filter(rolFuncionalidad::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(rolFuncionalidadList).isNotNull();
        assertThat(rolFuncionalidadList).hasSize(1);
        RolFuncionalidad testRolFuncionalidad = rolFuncionalidadList.get(0);
        assertThat(testRolFuncionalidad.getRol()).isEqualTo(DEFAULT_ROL);
        assertThat(testRolFuncionalidad.getActivo()).isEqualTo(DEFAULT_ACTIVO);
        assertThat(testRolFuncionalidad.getPrioridad()).isEqualTo(DEFAULT_PRIORIDAD);
    }

    @Test
    void getAllRolFuncionalidads() {
        // Initialize the database
        rolFuncionalidadRepository.save(rolFuncionalidad).block();

        // Get all the rolFuncionalidadList
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
            .value(hasItem(rolFuncionalidad.getId().intValue()))
            .jsonPath("$.[*].rol")
            .value(hasItem(DEFAULT_ROL))
            .jsonPath("$.[*].activo")
            .value(hasItem(DEFAULT_ACTIVO.booleanValue()))
            .jsonPath("$.[*].prioridad")
            .value(hasItem(DEFAULT_PRIORIDAD));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRolFuncionalidadsWithEagerRelationshipsIsEnabled() {
        when(rolFuncionalidadRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(rolFuncionalidadRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRolFuncionalidadsWithEagerRelationshipsIsNotEnabled() {
        when(rolFuncionalidadRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(rolFuncionalidadRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getRolFuncionalidad() {
        // Initialize the database
        rolFuncionalidadRepository.save(rolFuncionalidad).block();

        // Get the rolFuncionalidad
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, rolFuncionalidad.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(rolFuncionalidad.getId().intValue()))
            .jsonPath("$.rol")
            .value(is(DEFAULT_ROL))
            .jsonPath("$.activo")
            .value(is(DEFAULT_ACTIVO.booleanValue()))
            .jsonPath("$.prioridad")
            .value(is(DEFAULT_PRIORIDAD));
    }

    @Test
    void getNonExistingRolFuncionalidad() {
        // Get the rolFuncionalidad
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingRolFuncionalidad() throws Exception {
        // Initialize the database
        rolFuncionalidadRepository.save(rolFuncionalidad).block();

        int databaseSizeBeforeUpdate = rolFuncionalidadRepository.findAll().collectList().block().size();

        // Update the rolFuncionalidad
        RolFuncionalidad updatedRolFuncionalidad = rolFuncionalidadRepository.findById(rolFuncionalidad.getId()).block();
        updatedRolFuncionalidad.rol(UPDATED_ROL).activo(UPDATED_ACTIVO).prioridad(UPDATED_PRIORIDAD);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedRolFuncionalidad.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedRolFuncionalidad))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the RolFuncionalidad in the database
        List<RolFuncionalidad> rolFuncionalidadList = rolFuncionalidadRepository.findAll().collectList().block();
        assertThat(rolFuncionalidadList).hasSize(databaseSizeBeforeUpdate);
        RolFuncionalidad testRolFuncionalidad = rolFuncionalidadList.get(rolFuncionalidadList.size() - 1);
        assertThat(testRolFuncionalidad.getRol()).isEqualTo(UPDATED_ROL);
        assertThat(testRolFuncionalidad.getActivo()).isEqualTo(UPDATED_ACTIVO);
        assertThat(testRolFuncionalidad.getPrioridad()).isEqualTo(UPDATED_PRIORIDAD);
    }

    @Test
    void putNonExistingRolFuncionalidad() throws Exception {
        int databaseSizeBeforeUpdate = rolFuncionalidadRepository.findAll().collectList().block().size();
        rolFuncionalidad.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, rolFuncionalidad.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(rolFuncionalidad))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RolFuncionalidad in the database
        List<RolFuncionalidad> rolFuncionalidadList = rolFuncionalidadRepository.findAll().collectList().block();
        assertThat(rolFuncionalidadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchRolFuncionalidad() throws Exception {
        int databaseSizeBeforeUpdate = rolFuncionalidadRepository.findAll().collectList().block().size();
        rolFuncionalidad.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(rolFuncionalidad))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RolFuncionalidad in the database
        List<RolFuncionalidad> rolFuncionalidadList = rolFuncionalidadRepository.findAll().collectList().block();
        assertThat(rolFuncionalidadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamRolFuncionalidad() throws Exception {
        int databaseSizeBeforeUpdate = rolFuncionalidadRepository.findAll().collectList().block().size();
        rolFuncionalidad.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(rolFuncionalidad))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the RolFuncionalidad in the database
        List<RolFuncionalidad> rolFuncionalidadList = rolFuncionalidadRepository.findAll().collectList().block();
        assertThat(rolFuncionalidadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateRolFuncionalidadWithPatch() throws Exception {
        // Initialize the database
        rolFuncionalidadRepository.save(rolFuncionalidad).block();

        int databaseSizeBeforeUpdate = rolFuncionalidadRepository.findAll().collectList().block().size();

        // Update the rolFuncionalidad using partial update
        RolFuncionalidad partialUpdatedRolFuncionalidad = new RolFuncionalidad();
        partialUpdatedRolFuncionalidad.setId(rolFuncionalidad.getId());

        partialUpdatedRolFuncionalidad.activo(UPDATED_ACTIVO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedRolFuncionalidad.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedRolFuncionalidad))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the RolFuncionalidad in the database
        List<RolFuncionalidad> rolFuncionalidadList = rolFuncionalidadRepository.findAll().collectList().block();
        assertThat(rolFuncionalidadList).hasSize(databaseSizeBeforeUpdate);
        RolFuncionalidad testRolFuncionalidad = rolFuncionalidadList.get(rolFuncionalidadList.size() - 1);
        assertThat(testRolFuncionalidad.getRol()).isEqualTo(DEFAULT_ROL);
        assertThat(testRolFuncionalidad.getActivo()).isEqualTo(UPDATED_ACTIVO);
        assertThat(testRolFuncionalidad.getPrioridad()).isEqualTo(DEFAULT_PRIORIDAD);
    }

    @Test
    void fullUpdateRolFuncionalidadWithPatch() throws Exception {
        // Initialize the database
        rolFuncionalidadRepository.save(rolFuncionalidad).block();

        int databaseSizeBeforeUpdate = rolFuncionalidadRepository.findAll().collectList().block().size();

        // Update the rolFuncionalidad using partial update
        RolFuncionalidad partialUpdatedRolFuncionalidad = new RolFuncionalidad();
        partialUpdatedRolFuncionalidad.setId(rolFuncionalidad.getId());

        partialUpdatedRolFuncionalidad.rol(UPDATED_ROL).activo(UPDATED_ACTIVO).prioridad(UPDATED_PRIORIDAD);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedRolFuncionalidad.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedRolFuncionalidad))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the RolFuncionalidad in the database
        List<RolFuncionalidad> rolFuncionalidadList = rolFuncionalidadRepository.findAll().collectList().block();
        assertThat(rolFuncionalidadList).hasSize(databaseSizeBeforeUpdate);
        RolFuncionalidad testRolFuncionalidad = rolFuncionalidadList.get(rolFuncionalidadList.size() - 1);
        assertThat(testRolFuncionalidad.getRol()).isEqualTo(UPDATED_ROL);
        assertThat(testRolFuncionalidad.getActivo()).isEqualTo(UPDATED_ACTIVO);
        assertThat(testRolFuncionalidad.getPrioridad()).isEqualTo(UPDATED_PRIORIDAD);
    }

    @Test
    void patchNonExistingRolFuncionalidad() throws Exception {
        int databaseSizeBeforeUpdate = rolFuncionalidadRepository.findAll().collectList().block().size();
        rolFuncionalidad.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, rolFuncionalidad.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(rolFuncionalidad))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RolFuncionalidad in the database
        List<RolFuncionalidad> rolFuncionalidadList = rolFuncionalidadRepository.findAll().collectList().block();
        assertThat(rolFuncionalidadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchRolFuncionalidad() throws Exception {
        int databaseSizeBeforeUpdate = rolFuncionalidadRepository.findAll().collectList().block().size();
        rolFuncionalidad.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(rolFuncionalidad))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RolFuncionalidad in the database
        List<RolFuncionalidad> rolFuncionalidadList = rolFuncionalidadRepository.findAll().collectList().block();
        assertThat(rolFuncionalidadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamRolFuncionalidad() throws Exception {
        int databaseSizeBeforeUpdate = rolFuncionalidadRepository.findAll().collectList().block().size();
        rolFuncionalidad.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(rolFuncionalidad))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the RolFuncionalidad in the database
        List<RolFuncionalidad> rolFuncionalidadList = rolFuncionalidadRepository.findAll().collectList().block();
        assertThat(rolFuncionalidadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteRolFuncionalidad() {
        // Initialize the database
        rolFuncionalidadRepository.save(rolFuncionalidad).block();

        int databaseSizeBeforeDelete = rolFuncionalidadRepository.findAll().collectList().block().size();

        // Delete the rolFuncionalidad
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, rolFuncionalidad.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<RolFuncionalidad> rolFuncionalidadList = rolFuncionalidadRepository.findAll().collectList().block();
        assertThat(rolFuncionalidadList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

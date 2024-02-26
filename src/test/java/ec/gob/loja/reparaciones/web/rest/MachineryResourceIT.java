package ec.gob.loja.reparaciones.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import ec.gob.loja.reparaciones.IntegrationTest;
import ec.gob.loja.reparaciones.domain.Machinery;
import ec.gob.loja.reparaciones.repository.EntityManager;
import ec.gob.loja.reparaciones.repository.MachineryRepository;
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
 * Integration tests for the {@link MachineryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class MachineryResourceIT {

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final Double DEFAULT_HORAS_TRABAJADAS = 1D;
    private static final Double UPDATED_HORAS_TRABAJADAS = 2D;

    private static final String ENTITY_API_URL = "/api/machinery";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MachineryRepository machineryRepository;

    @Mock
    private MachineryRepository machineryRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Machinery machinery;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Machinery createEntity(EntityManager em) {
        Machinery machinery = new Machinery()
            .codigo(DEFAULT_CODIGO)
            .descripcion(DEFAULT_DESCRIPCION)
            .horasTrabajadas(DEFAULT_HORAS_TRABAJADAS);
        return machinery;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Machinery createUpdatedEntity(EntityManager em) {
        Machinery machinery = new Machinery()
            .codigo(UPDATED_CODIGO)
            .descripcion(UPDATED_DESCRIPCION)
            .horasTrabajadas(UPDATED_HORAS_TRABAJADAS);
        return machinery;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Machinery.class).block();
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
        machinery = createEntity(em);
    }

    @Test
    void createMachinery() throws Exception {
        int databaseSizeBeforeCreate = machineryRepository.findAll().collectList().block().size();
        // Create the Machinery
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(machinery))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Machinery in the database
        List<Machinery> machineryList = machineryRepository.findAll().collectList().block();
        assertThat(machineryList).hasSize(databaseSizeBeforeCreate + 1);
        Machinery testMachinery = machineryList.get(machineryList.size() - 1);
        assertThat(testMachinery.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testMachinery.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testMachinery.getHorasTrabajadas()).isEqualTo(DEFAULT_HORAS_TRABAJADAS);
    }

    @Test
    void createMachineryWithExistingId() throws Exception {
        // Create the Machinery with an existing ID
        machinery.setId(1L);

        int databaseSizeBeforeCreate = machineryRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(machinery))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Machinery in the database
        List<Machinery> machineryList = machineryRepository.findAll().collectList().block();
        assertThat(machineryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllMachineryAsStream() {
        // Initialize the database
        machineryRepository.save(machinery).block();

        List<Machinery> machineryList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Machinery.class)
            .getResponseBody()
            .filter(machinery::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(machineryList).isNotNull();
        assertThat(machineryList).hasSize(1);
        Machinery testMachinery = machineryList.get(0);
        assertThat(testMachinery.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testMachinery.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testMachinery.getHorasTrabajadas()).isEqualTo(DEFAULT_HORAS_TRABAJADAS);
    }

    @Test
    void getAllMachinery() {
        // Initialize the database
        machineryRepository.save(machinery).block();

        // Get all the machineryList
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
            .value(hasItem(machinery.getId().intValue()))
            .jsonPath("$.[*].codigo")
            .value(hasItem(DEFAULT_CODIGO))
            .jsonPath("$.[*].descripcion")
            .value(hasItem(DEFAULT_DESCRIPCION))
            .jsonPath("$.[*].horasTrabajadas")
            .value(hasItem(DEFAULT_HORAS_TRABAJADAS.doubleValue()));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMachineryWithEagerRelationshipsIsEnabled() {
        when(machineryRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(machineryRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMachineryWithEagerRelationshipsIsNotEnabled() {
        when(machineryRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(machineryRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getMachinery() {
        // Initialize the database
        machineryRepository.save(machinery).block();

        // Get the machinery
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, machinery.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(machinery.getId().intValue()))
            .jsonPath("$.codigo")
            .value(is(DEFAULT_CODIGO))
            .jsonPath("$.descripcion")
            .value(is(DEFAULT_DESCRIPCION))
            .jsonPath("$.horasTrabajadas")
            .value(is(DEFAULT_HORAS_TRABAJADAS.doubleValue()));
    }

    @Test
    void getNonExistingMachinery() {
        // Get the machinery
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingMachinery() throws Exception {
        // Initialize the database
        machineryRepository.save(machinery).block();

        int databaseSizeBeforeUpdate = machineryRepository.findAll().collectList().block().size();

        // Update the machinery
        Machinery updatedMachinery = machineryRepository.findById(machinery.getId()).block();
        updatedMachinery.codigo(UPDATED_CODIGO).descripcion(UPDATED_DESCRIPCION).horasTrabajadas(UPDATED_HORAS_TRABAJADAS);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedMachinery.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedMachinery))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Machinery in the database
        List<Machinery> machineryList = machineryRepository.findAll().collectList().block();
        assertThat(machineryList).hasSize(databaseSizeBeforeUpdate);
        Machinery testMachinery = machineryList.get(machineryList.size() - 1);
        assertThat(testMachinery.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testMachinery.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testMachinery.getHorasTrabajadas()).isEqualTo(UPDATED_HORAS_TRABAJADAS);
    }

    @Test
    void putNonExistingMachinery() throws Exception {
        int databaseSizeBeforeUpdate = machineryRepository.findAll().collectList().block().size();
        machinery.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, machinery.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(machinery))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Machinery in the database
        List<Machinery> machineryList = machineryRepository.findAll().collectList().block();
        assertThat(machineryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchMachinery() throws Exception {
        int databaseSizeBeforeUpdate = machineryRepository.findAll().collectList().block().size();
        machinery.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(machinery))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Machinery in the database
        List<Machinery> machineryList = machineryRepository.findAll().collectList().block();
        assertThat(machineryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamMachinery() throws Exception {
        int databaseSizeBeforeUpdate = machineryRepository.findAll().collectList().block().size();
        machinery.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(machinery))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Machinery in the database
        List<Machinery> machineryList = machineryRepository.findAll().collectList().block();
        assertThat(machineryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateMachineryWithPatch() throws Exception {
        // Initialize the database
        machineryRepository.save(machinery).block();

        int databaseSizeBeforeUpdate = machineryRepository.findAll().collectList().block().size();

        // Update the machinery using partial update
        Machinery partialUpdatedMachinery = new Machinery();
        partialUpdatedMachinery.setId(machinery.getId());

        partialUpdatedMachinery.descripcion(UPDATED_DESCRIPCION).horasTrabajadas(UPDATED_HORAS_TRABAJADAS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMachinery.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedMachinery))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Machinery in the database
        List<Machinery> machineryList = machineryRepository.findAll().collectList().block();
        assertThat(machineryList).hasSize(databaseSizeBeforeUpdate);
        Machinery testMachinery = machineryList.get(machineryList.size() - 1);
        assertThat(testMachinery.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testMachinery.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testMachinery.getHorasTrabajadas()).isEqualTo(UPDATED_HORAS_TRABAJADAS);
    }

    @Test
    void fullUpdateMachineryWithPatch() throws Exception {
        // Initialize the database
        machineryRepository.save(machinery).block();

        int databaseSizeBeforeUpdate = machineryRepository.findAll().collectList().block().size();

        // Update the machinery using partial update
        Machinery partialUpdatedMachinery = new Machinery();
        partialUpdatedMachinery.setId(machinery.getId());

        partialUpdatedMachinery.codigo(UPDATED_CODIGO).descripcion(UPDATED_DESCRIPCION).horasTrabajadas(UPDATED_HORAS_TRABAJADAS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMachinery.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedMachinery))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Machinery in the database
        List<Machinery> machineryList = machineryRepository.findAll().collectList().block();
        assertThat(machineryList).hasSize(databaseSizeBeforeUpdate);
        Machinery testMachinery = machineryList.get(machineryList.size() - 1);
        assertThat(testMachinery.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testMachinery.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testMachinery.getHorasTrabajadas()).isEqualTo(UPDATED_HORAS_TRABAJADAS);
    }

    @Test
    void patchNonExistingMachinery() throws Exception {
        int databaseSizeBeforeUpdate = machineryRepository.findAll().collectList().block().size();
        machinery.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, machinery.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(machinery))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Machinery in the database
        List<Machinery> machineryList = machineryRepository.findAll().collectList().block();
        assertThat(machineryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchMachinery() throws Exception {
        int databaseSizeBeforeUpdate = machineryRepository.findAll().collectList().block().size();
        machinery.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(machinery))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Machinery in the database
        List<Machinery> machineryList = machineryRepository.findAll().collectList().block();
        assertThat(machineryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamMachinery() throws Exception {
        int databaseSizeBeforeUpdate = machineryRepository.findAll().collectList().block().size();
        machinery.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(machinery))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Machinery in the database
        List<Machinery> machineryList = machineryRepository.findAll().collectList().block();
        assertThat(machineryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteMachinery() {
        // Initialize the database
        machineryRepository.save(machinery).block();

        int databaseSizeBeforeDelete = machineryRepository.findAll().collectList().block().size();

        // Delete the machinery
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, machinery.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Machinery> machineryList = machineryRepository.findAll().collectList().block();
        assertThat(machineryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

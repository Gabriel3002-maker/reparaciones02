package ec.gob.loja.reparaciones.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import ec.gob.loja.reparaciones.IntegrationTest;
import ec.gob.loja.reparaciones.domain.OrdenBodega;
import ec.gob.loja.reparaciones.repository.EntityManager;
import ec.gob.loja.reparaciones.repository.OrdenBodegaRepository;
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
 * Integration tests for the {@link OrdenBodegaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class OrdenBodegaResourceIT {

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final String DEFAULT_DETALLE_NECESIDAD = "AAAAAAAAAA";
    private static final String UPDATED_DETALLE_NECESIDAD = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/orden-bodegas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrdenBodegaRepository ordenBodegaRepository;

    @Mock
    private OrdenBodegaRepository ordenBodegaRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private OrdenBodega ordenBodega;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdenBodega createEntity(EntityManager em) {
        OrdenBodega ordenBodega = new OrdenBodega().codigo(DEFAULT_CODIGO).detalleNecesidad(DEFAULT_DETALLE_NECESIDAD).fecha(DEFAULT_FECHA);
        return ordenBodega;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdenBodega createUpdatedEntity(EntityManager em) {
        OrdenBodega ordenBodega = new OrdenBodega().codigo(UPDATED_CODIGO).detalleNecesidad(UPDATED_DETALLE_NECESIDAD).fecha(UPDATED_FECHA);
        return ordenBodega;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(OrdenBodega.class).block();
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
        ordenBodega = createEntity(em);
    }

    @Test
    void createOrdenBodega() throws Exception {
        int databaseSizeBeforeCreate = ordenBodegaRepository.findAll().collectList().block().size();
        // Create the OrdenBodega
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ordenBodega))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the OrdenBodega in the database
        List<OrdenBodega> ordenBodegaList = ordenBodegaRepository.findAll().collectList().block();
        assertThat(ordenBodegaList).hasSize(databaseSizeBeforeCreate + 1);
        OrdenBodega testOrdenBodega = ordenBodegaList.get(ordenBodegaList.size() - 1);
        assertThat(testOrdenBodega.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testOrdenBodega.getDetalleNecesidad()).isEqualTo(DEFAULT_DETALLE_NECESIDAD);
        assertThat(testOrdenBodega.getFecha()).isEqualTo(DEFAULT_FECHA);
    }

    @Test
    void createOrdenBodegaWithExistingId() throws Exception {
        // Create the OrdenBodega with an existing ID
        ordenBodega.setId(1L);

        int databaseSizeBeforeCreate = ordenBodegaRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ordenBodega))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the OrdenBodega in the database
        List<OrdenBodega> ordenBodegaList = ordenBodegaRepository.findAll().collectList().block();
        assertThat(ordenBodegaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllOrdenBodegasAsStream() {
        // Initialize the database
        ordenBodegaRepository.save(ordenBodega).block();

        List<OrdenBodega> ordenBodegaList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(OrdenBodega.class)
            .getResponseBody()
            .filter(ordenBodega::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(ordenBodegaList).isNotNull();
        assertThat(ordenBodegaList).hasSize(1);
        OrdenBodega testOrdenBodega = ordenBodegaList.get(0);
        assertThat(testOrdenBodega.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testOrdenBodega.getDetalleNecesidad()).isEqualTo(DEFAULT_DETALLE_NECESIDAD);
        assertThat(testOrdenBodega.getFecha()).isEqualTo(DEFAULT_FECHA);
    }

    @Test
    void getAllOrdenBodegas() {
        // Initialize the database
        ordenBodegaRepository.save(ordenBodega).block();

        // Get all the ordenBodegaList
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
            .value(hasItem(ordenBodega.getId().intValue()))
            .jsonPath("$.[*].codigo")
            .value(hasItem(DEFAULT_CODIGO))
            .jsonPath("$.[*].detalleNecesidad")
            .value(hasItem(DEFAULT_DETALLE_NECESIDAD))
            .jsonPath("$.[*].fecha")
            .value(hasItem(DEFAULT_FECHA.toString()));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOrdenBodegasWithEagerRelationshipsIsEnabled() {
        when(ordenBodegaRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(ordenBodegaRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOrdenBodegasWithEagerRelationshipsIsNotEnabled() {
        when(ordenBodegaRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(ordenBodegaRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getOrdenBodega() {
        // Initialize the database
        ordenBodegaRepository.save(ordenBodega).block();

        // Get the ordenBodega
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, ordenBodega.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(ordenBodega.getId().intValue()))
            .jsonPath("$.codigo")
            .value(is(DEFAULT_CODIGO))
            .jsonPath("$.detalleNecesidad")
            .value(is(DEFAULT_DETALLE_NECESIDAD))
            .jsonPath("$.fecha")
            .value(is(DEFAULT_FECHA.toString()));
    }

    @Test
    void getNonExistingOrdenBodega() {
        // Get the ordenBodega
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingOrdenBodega() throws Exception {
        // Initialize the database
        ordenBodegaRepository.save(ordenBodega).block();

        int databaseSizeBeforeUpdate = ordenBodegaRepository.findAll().collectList().block().size();

        // Update the ordenBodega
        OrdenBodega updatedOrdenBodega = ordenBodegaRepository.findById(ordenBodega.getId()).block();
        updatedOrdenBodega.codigo(UPDATED_CODIGO).detalleNecesidad(UPDATED_DETALLE_NECESIDAD).fecha(UPDATED_FECHA);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedOrdenBodega.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedOrdenBodega))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the OrdenBodega in the database
        List<OrdenBodega> ordenBodegaList = ordenBodegaRepository.findAll().collectList().block();
        assertThat(ordenBodegaList).hasSize(databaseSizeBeforeUpdate);
        OrdenBodega testOrdenBodega = ordenBodegaList.get(ordenBodegaList.size() - 1);
        assertThat(testOrdenBodega.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testOrdenBodega.getDetalleNecesidad()).isEqualTo(UPDATED_DETALLE_NECESIDAD);
        assertThat(testOrdenBodega.getFecha()).isEqualTo(UPDATED_FECHA);
    }

    @Test
    void putNonExistingOrdenBodega() throws Exception {
        int databaseSizeBeforeUpdate = ordenBodegaRepository.findAll().collectList().block().size();
        ordenBodega.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, ordenBodega.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ordenBodega))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the OrdenBodega in the database
        List<OrdenBodega> ordenBodegaList = ordenBodegaRepository.findAll().collectList().block();
        assertThat(ordenBodegaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchOrdenBodega() throws Exception {
        int databaseSizeBeforeUpdate = ordenBodegaRepository.findAll().collectList().block().size();
        ordenBodega.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ordenBodega))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the OrdenBodega in the database
        List<OrdenBodega> ordenBodegaList = ordenBodegaRepository.findAll().collectList().block();
        assertThat(ordenBodegaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamOrdenBodega() throws Exception {
        int databaseSizeBeforeUpdate = ordenBodegaRepository.findAll().collectList().block().size();
        ordenBodega.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ordenBodega))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the OrdenBodega in the database
        List<OrdenBodega> ordenBodegaList = ordenBodegaRepository.findAll().collectList().block();
        assertThat(ordenBodegaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateOrdenBodegaWithPatch() throws Exception {
        // Initialize the database
        ordenBodegaRepository.save(ordenBodega).block();

        int databaseSizeBeforeUpdate = ordenBodegaRepository.findAll().collectList().block().size();

        // Update the ordenBodega using partial update
        OrdenBodega partialUpdatedOrdenBodega = new OrdenBodega();
        partialUpdatedOrdenBodega.setId(ordenBodega.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedOrdenBodega.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdenBodega))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the OrdenBodega in the database
        List<OrdenBodega> ordenBodegaList = ordenBodegaRepository.findAll().collectList().block();
        assertThat(ordenBodegaList).hasSize(databaseSizeBeforeUpdate);
        OrdenBodega testOrdenBodega = ordenBodegaList.get(ordenBodegaList.size() - 1);
        assertThat(testOrdenBodega.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testOrdenBodega.getDetalleNecesidad()).isEqualTo(DEFAULT_DETALLE_NECESIDAD);
        assertThat(testOrdenBodega.getFecha()).isEqualTo(DEFAULT_FECHA);
    }

    @Test
    void fullUpdateOrdenBodegaWithPatch() throws Exception {
        // Initialize the database
        ordenBodegaRepository.save(ordenBodega).block();

        int databaseSizeBeforeUpdate = ordenBodegaRepository.findAll().collectList().block().size();

        // Update the ordenBodega using partial update
        OrdenBodega partialUpdatedOrdenBodega = new OrdenBodega();
        partialUpdatedOrdenBodega.setId(ordenBodega.getId());

        partialUpdatedOrdenBodega.codigo(UPDATED_CODIGO).detalleNecesidad(UPDATED_DETALLE_NECESIDAD).fecha(UPDATED_FECHA);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedOrdenBodega.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdenBodega))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the OrdenBodega in the database
        List<OrdenBodega> ordenBodegaList = ordenBodegaRepository.findAll().collectList().block();
        assertThat(ordenBodegaList).hasSize(databaseSizeBeforeUpdate);
        OrdenBodega testOrdenBodega = ordenBodegaList.get(ordenBodegaList.size() - 1);
        assertThat(testOrdenBodega.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testOrdenBodega.getDetalleNecesidad()).isEqualTo(UPDATED_DETALLE_NECESIDAD);
        assertThat(testOrdenBodega.getFecha()).isEqualTo(UPDATED_FECHA);
    }

    @Test
    void patchNonExistingOrdenBodega() throws Exception {
        int databaseSizeBeforeUpdate = ordenBodegaRepository.findAll().collectList().block().size();
        ordenBodega.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, ordenBodega.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(ordenBodega))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the OrdenBodega in the database
        List<OrdenBodega> ordenBodegaList = ordenBodegaRepository.findAll().collectList().block();
        assertThat(ordenBodegaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchOrdenBodega() throws Exception {
        int databaseSizeBeforeUpdate = ordenBodegaRepository.findAll().collectList().block().size();
        ordenBodega.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(ordenBodega))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the OrdenBodega in the database
        List<OrdenBodega> ordenBodegaList = ordenBodegaRepository.findAll().collectList().block();
        assertThat(ordenBodegaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamOrdenBodega() throws Exception {
        int databaseSizeBeforeUpdate = ordenBodegaRepository.findAll().collectList().block().size();
        ordenBodega.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(ordenBodega))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the OrdenBodega in the database
        List<OrdenBodega> ordenBodegaList = ordenBodegaRepository.findAll().collectList().block();
        assertThat(ordenBodegaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteOrdenBodega() {
        // Initialize the database
        ordenBodegaRepository.save(ordenBodega).block();

        int databaseSizeBeforeDelete = ordenBodegaRepository.findAll().collectList().block().size();

        // Delete the ordenBodega
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, ordenBodega.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<OrdenBodega> ordenBodegaList = ordenBodegaRepository.findAll().collectList().block();
        assertThat(ordenBodegaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

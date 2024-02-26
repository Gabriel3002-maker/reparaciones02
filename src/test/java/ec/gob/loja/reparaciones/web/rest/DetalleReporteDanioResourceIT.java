package ec.gob.loja.reparaciones.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import ec.gob.loja.reparaciones.IntegrationTest;
import ec.gob.loja.reparaciones.domain.DetalleReporteDanio;
import ec.gob.loja.reparaciones.repository.DetalleReporteDanioRepository;
import ec.gob.loja.reparaciones.repository.EntityManager;
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
 * Integration tests for the {@link DetalleReporteDanioResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class DetalleReporteDanioResourceIT {

    private static final Integer DEFAULT_CODIGO = 1;
    private static final Integer UPDATED_CODIGO = 2;

    private static final LocalDate DEFAULT_FECHA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_CONTRIBUYENTE = "AAAAAAAAAA";
    private static final String UPDATED_CONTRIBUYENTE = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION = "BBBBBBBBBB";

    private static final String DEFAULT_REFERENCIA = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCIA = "BBBBBBBBBB";

    private static final Integer DEFAULT_HORAS_TRABAJADAS = 1;
    private static final Integer UPDATED_HORAS_TRABAJADAS = 2;

    private static final String DEFAULT_PERSONAL_RESPONSABLE = "AAAAAAAAAA";
    private static final String UPDATED_PERSONAL_RESPONSABLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/detalle-reporte-danios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DetalleReporteDanioRepository detalleReporteDanioRepository;

    @Mock
    private DetalleReporteDanioRepository detalleReporteDanioRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private DetalleReporteDanio detalleReporteDanio;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DetalleReporteDanio createEntity(EntityManager em) {
        DetalleReporteDanio detalleReporteDanio = new DetalleReporteDanio()
            .codigo(DEFAULT_CODIGO)
            .fecha(DEFAULT_FECHA)
            .contribuyente(DEFAULT_CONTRIBUYENTE)
            .direccion(DEFAULT_DIRECCION)
            .referencia(DEFAULT_REFERENCIA)
            .horasTrabajadas(DEFAULT_HORAS_TRABAJADAS)
            .personalResponsable(DEFAULT_PERSONAL_RESPONSABLE);
        return detalleReporteDanio;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DetalleReporteDanio createUpdatedEntity(EntityManager em) {
        DetalleReporteDanio detalleReporteDanio = new DetalleReporteDanio()
            .codigo(UPDATED_CODIGO)
            .fecha(UPDATED_FECHA)
            .contribuyente(UPDATED_CONTRIBUYENTE)
            .direccion(UPDATED_DIRECCION)
            .referencia(UPDATED_REFERENCIA)
            .horasTrabajadas(UPDATED_HORAS_TRABAJADAS)
            .personalResponsable(UPDATED_PERSONAL_RESPONSABLE);
        return detalleReporteDanio;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(DetalleReporteDanio.class).block();
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
        detalleReporteDanio = createEntity(em);
    }

    @Test
    void createDetalleReporteDanio() throws Exception {
        int databaseSizeBeforeCreate = detalleReporteDanioRepository.findAll().collectList().block().size();
        // Create the DetalleReporteDanio
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(detalleReporteDanio))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the DetalleReporteDanio in the database
        List<DetalleReporteDanio> detalleReporteDanioList = detalleReporteDanioRepository.findAll().collectList().block();
        assertThat(detalleReporteDanioList).hasSize(databaseSizeBeforeCreate + 1);
        DetalleReporteDanio testDetalleReporteDanio = detalleReporteDanioList.get(detalleReporteDanioList.size() - 1);
        assertThat(testDetalleReporteDanio.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testDetalleReporteDanio.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testDetalleReporteDanio.getContribuyente()).isEqualTo(DEFAULT_CONTRIBUYENTE);
        assertThat(testDetalleReporteDanio.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
        assertThat(testDetalleReporteDanio.getReferencia()).isEqualTo(DEFAULT_REFERENCIA);
        assertThat(testDetalleReporteDanio.getHorasTrabajadas()).isEqualTo(DEFAULT_HORAS_TRABAJADAS);
        assertThat(testDetalleReporteDanio.getPersonalResponsable()).isEqualTo(DEFAULT_PERSONAL_RESPONSABLE);
    }

    @Test
    void createDetalleReporteDanioWithExistingId() throws Exception {
        // Create the DetalleReporteDanio with an existing ID
        detalleReporteDanio.setId(1L);

        int databaseSizeBeforeCreate = detalleReporteDanioRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(detalleReporteDanio))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DetalleReporteDanio in the database
        List<DetalleReporteDanio> detalleReporteDanioList = detalleReporteDanioRepository.findAll().collectList().block();
        assertThat(detalleReporteDanioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllDetalleReporteDaniosAsStream() {
        // Initialize the database
        detalleReporteDanioRepository.save(detalleReporteDanio).block();

        List<DetalleReporteDanio> detalleReporteDanioList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(DetalleReporteDanio.class)
            .getResponseBody()
            .filter(detalleReporteDanio::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(detalleReporteDanioList).isNotNull();
        assertThat(detalleReporteDanioList).hasSize(1);
        DetalleReporteDanio testDetalleReporteDanio = detalleReporteDanioList.get(0);
        assertThat(testDetalleReporteDanio.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testDetalleReporteDanio.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testDetalleReporteDanio.getContribuyente()).isEqualTo(DEFAULT_CONTRIBUYENTE);
        assertThat(testDetalleReporteDanio.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
        assertThat(testDetalleReporteDanio.getReferencia()).isEqualTo(DEFAULT_REFERENCIA);
        assertThat(testDetalleReporteDanio.getHorasTrabajadas()).isEqualTo(DEFAULT_HORAS_TRABAJADAS);
        assertThat(testDetalleReporteDanio.getPersonalResponsable()).isEqualTo(DEFAULT_PERSONAL_RESPONSABLE);
    }

    @Test
    void getAllDetalleReporteDanios() {
        // Initialize the database
        detalleReporteDanioRepository.save(detalleReporteDanio).block();

        // Get all the detalleReporteDanioList
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
            .value(hasItem(detalleReporteDanio.getId().intValue()))
            .jsonPath("$.[*].codigo")
            .value(hasItem(DEFAULT_CODIGO))
            .jsonPath("$.[*].fecha")
            .value(hasItem(DEFAULT_FECHA.toString()))
            .jsonPath("$.[*].contribuyente")
            .value(hasItem(DEFAULT_CONTRIBUYENTE))
            .jsonPath("$.[*].direccion")
            .value(hasItem(DEFAULT_DIRECCION))
            .jsonPath("$.[*].referencia")
            .value(hasItem(DEFAULT_REFERENCIA))
            .jsonPath("$.[*].horasTrabajadas")
            .value(hasItem(DEFAULT_HORAS_TRABAJADAS))
            .jsonPath("$.[*].personalResponsable")
            .value(hasItem(DEFAULT_PERSONAL_RESPONSABLE));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDetalleReporteDaniosWithEagerRelationshipsIsEnabled() {
        when(detalleReporteDanioRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(detalleReporteDanioRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDetalleReporteDaniosWithEagerRelationshipsIsNotEnabled() {
        when(detalleReporteDanioRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(detalleReporteDanioRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getDetalleReporteDanio() {
        // Initialize the database
        detalleReporteDanioRepository.save(detalleReporteDanio).block();

        // Get the detalleReporteDanio
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, detalleReporteDanio.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(detalleReporteDanio.getId().intValue()))
            .jsonPath("$.codigo")
            .value(is(DEFAULT_CODIGO))
            .jsonPath("$.fecha")
            .value(is(DEFAULT_FECHA.toString()))
            .jsonPath("$.contribuyente")
            .value(is(DEFAULT_CONTRIBUYENTE))
            .jsonPath("$.direccion")
            .value(is(DEFAULT_DIRECCION))
            .jsonPath("$.referencia")
            .value(is(DEFAULT_REFERENCIA))
            .jsonPath("$.horasTrabajadas")
            .value(is(DEFAULT_HORAS_TRABAJADAS))
            .jsonPath("$.personalResponsable")
            .value(is(DEFAULT_PERSONAL_RESPONSABLE));
    }

    @Test
    void getNonExistingDetalleReporteDanio() {
        // Get the detalleReporteDanio
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingDetalleReporteDanio() throws Exception {
        // Initialize the database
        detalleReporteDanioRepository.save(detalleReporteDanio).block();

        int databaseSizeBeforeUpdate = detalleReporteDanioRepository.findAll().collectList().block().size();

        // Update the detalleReporteDanio
        DetalleReporteDanio updatedDetalleReporteDanio = detalleReporteDanioRepository.findById(detalleReporteDanio.getId()).block();
        updatedDetalleReporteDanio
            .codigo(UPDATED_CODIGO)
            .fecha(UPDATED_FECHA)
            .contribuyente(UPDATED_CONTRIBUYENTE)
            .direccion(UPDATED_DIRECCION)
            .referencia(UPDATED_REFERENCIA)
            .horasTrabajadas(UPDATED_HORAS_TRABAJADAS)
            .personalResponsable(UPDATED_PERSONAL_RESPONSABLE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedDetalleReporteDanio.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedDetalleReporteDanio))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the DetalleReporteDanio in the database
        List<DetalleReporteDanio> detalleReporteDanioList = detalleReporteDanioRepository.findAll().collectList().block();
        assertThat(detalleReporteDanioList).hasSize(databaseSizeBeforeUpdate);
        DetalleReporteDanio testDetalleReporteDanio = detalleReporteDanioList.get(detalleReporteDanioList.size() - 1);
        assertThat(testDetalleReporteDanio.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testDetalleReporteDanio.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testDetalleReporteDanio.getContribuyente()).isEqualTo(UPDATED_CONTRIBUYENTE);
        assertThat(testDetalleReporteDanio.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testDetalleReporteDanio.getReferencia()).isEqualTo(UPDATED_REFERENCIA);
        assertThat(testDetalleReporteDanio.getHorasTrabajadas()).isEqualTo(UPDATED_HORAS_TRABAJADAS);
        assertThat(testDetalleReporteDanio.getPersonalResponsable()).isEqualTo(UPDATED_PERSONAL_RESPONSABLE);
    }

    @Test
    void putNonExistingDetalleReporteDanio() throws Exception {
        int databaseSizeBeforeUpdate = detalleReporteDanioRepository.findAll().collectList().block().size();
        detalleReporteDanio.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, detalleReporteDanio.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(detalleReporteDanio))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DetalleReporteDanio in the database
        List<DetalleReporteDanio> detalleReporteDanioList = detalleReporteDanioRepository.findAll().collectList().block();
        assertThat(detalleReporteDanioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchDetalleReporteDanio() throws Exception {
        int databaseSizeBeforeUpdate = detalleReporteDanioRepository.findAll().collectList().block().size();
        detalleReporteDanio.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(detalleReporteDanio))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DetalleReporteDanio in the database
        List<DetalleReporteDanio> detalleReporteDanioList = detalleReporteDanioRepository.findAll().collectList().block();
        assertThat(detalleReporteDanioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamDetalleReporteDanio() throws Exception {
        int databaseSizeBeforeUpdate = detalleReporteDanioRepository.findAll().collectList().block().size();
        detalleReporteDanio.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(detalleReporteDanio))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the DetalleReporteDanio in the database
        List<DetalleReporteDanio> detalleReporteDanioList = detalleReporteDanioRepository.findAll().collectList().block();
        assertThat(detalleReporteDanioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateDetalleReporteDanioWithPatch() throws Exception {
        // Initialize the database
        detalleReporteDanioRepository.save(detalleReporteDanio).block();

        int databaseSizeBeforeUpdate = detalleReporteDanioRepository.findAll().collectList().block().size();

        // Update the detalleReporteDanio using partial update
        DetalleReporteDanio partialUpdatedDetalleReporteDanio = new DetalleReporteDanio();
        partialUpdatedDetalleReporteDanio.setId(detalleReporteDanio.getId());

        partialUpdatedDetalleReporteDanio
            .codigo(UPDATED_CODIGO)
            .fecha(UPDATED_FECHA)
            .contribuyente(UPDATED_CONTRIBUYENTE)
            .direccion(UPDATED_DIRECCION)
            .horasTrabajadas(UPDATED_HORAS_TRABAJADAS)
            .personalResponsable(UPDATED_PERSONAL_RESPONSABLE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDetalleReporteDanio.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedDetalleReporteDanio))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the DetalleReporteDanio in the database
        List<DetalleReporteDanio> detalleReporteDanioList = detalleReporteDanioRepository.findAll().collectList().block();
        assertThat(detalleReporteDanioList).hasSize(databaseSizeBeforeUpdate);
        DetalleReporteDanio testDetalleReporteDanio = detalleReporteDanioList.get(detalleReporteDanioList.size() - 1);
        assertThat(testDetalleReporteDanio.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testDetalleReporteDanio.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testDetalleReporteDanio.getContribuyente()).isEqualTo(UPDATED_CONTRIBUYENTE);
        assertThat(testDetalleReporteDanio.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testDetalleReporteDanio.getReferencia()).isEqualTo(DEFAULT_REFERENCIA);
        assertThat(testDetalleReporteDanio.getHorasTrabajadas()).isEqualTo(UPDATED_HORAS_TRABAJADAS);
        assertThat(testDetalleReporteDanio.getPersonalResponsable()).isEqualTo(UPDATED_PERSONAL_RESPONSABLE);
    }

    @Test
    void fullUpdateDetalleReporteDanioWithPatch() throws Exception {
        // Initialize the database
        detalleReporteDanioRepository.save(detalleReporteDanio).block();

        int databaseSizeBeforeUpdate = detalleReporteDanioRepository.findAll().collectList().block().size();

        // Update the detalleReporteDanio using partial update
        DetalleReporteDanio partialUpdatedDetalleReporteDanio = new DetalleReporteDanio();
        partialUpdatedDetalleReporteDanio.setId(detalleReporteDanio.getId());

        partialUpdatedDetalleReporteDanio
            .codigo(UPDATED_CODIGO)
            .fecha(UPDATED_FECHA)
            .contribuyente(UPDATED_CONTRIBUYENTE)
            .direccion(UPDATED_DIRECCION)
            .referencia(UPDATED_REFERENCIA)
            .horasTrabajadas(UPDATED_HORAS_TRABAJADAS)
            .personalResponsable(UPDATED_PERSONAL_RESPONSABLE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDetalleReporteDanio.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedDetalleReporteDanio))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the DetalleReporteDanio in the database
        List<DetalleReporteDanio> detalleReporteDanioList = detalleReporteDanioRepository.findAll().collectList().block();
        assertThat(detalleReporteDanioList).hasSize(databaseSizeBeforeUpdate);
        DetalleReporteDanio testDetalleReporteDanio = detalleReporteDanioList.get(detalleReporteDanioList.size() - 1);
        assertThat(testDetalleReporteDanio.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testDetalleReporteDanio.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testDetalleReporteDanio.getContribuyente()).isEqualTo(UPDATED_CONTRIBUYENTE);
        assertThat(testDetalleReporteDanio.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testDetalleReporteDanio.getReferencia()).isEqualTo(UPDATED_REFERENCIA);
        assertThat(testDetalleReporteDanio.getHorasTrabajadas()).isEqualTo(UPDATED_HORAS_TRABAJADAS);
        assertThat(testDetalleReporteDanio.getPersonalResponsable()).isEqualTo(UPDATED_PERSONAL_RESPONSABLE);
    }

    @Test
    void patchNonExistingDetalleReporteDanio() throws Exception {
        int databaseSizeBeforeUpdate = detalleReporteDanioRepository.findAll().collectList().block().size();
        detalleReporteDanio.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, detalleReporteDanio.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(detalleReporteDanio))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DetalleReporteDanio in the database
        List<DetalleReporteDanio> detalleReporteDanioList = detalleReporteDanioRepository.findAll().collectList().block();
        assertThat(detalleReporteDanioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchDetalleReporteDanio() throws Exception {
        int databaseSizeBeforeUpdate = detalleReporteDanioRepository.findAll().collectList().block().size();
        detalleReporteDanio.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(detalleReporteDanio))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DetalleReporteDanio in the database
        List<DetalleReporteDanio> detalleReporteDanioList = detalleReporteDanioRepository.findAll().collectList().block();
        assertThat(detalleReporteDanioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamDetalleReporteDanio() throws Exception {
        int databaseSizeBeforeUpdate = detalleReporteDanioRepository.findAll().collectList().block().size();
        detalleReporteDanio.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(detalleReporteDanio))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the DetalleReporteDanio in the database
        List<DetalleReporteDanio> detalleReporteDanioList = detalleReporteDanioRepository.findAll().collectList().block();
        assertThat(detalleReporteDanioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteDetalleReporteDanio() {
        // Initialize the database
        detalleReporteDanioRepository.save(detalleReporteDanio).block();

        int databaseSizeBeforeDelete = detalleReporteDanioRepository.findAll().collectList().block().size();

        // Delete the detalleReporteDanio
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, detalleReporteDanio.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<DetalleReporteDanio> detalleReporteDanioList = detalleReporteDanioRepository.findAll().collectList().block();
        assertThat(detalleReporteDanioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

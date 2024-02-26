package ec.gob.loja.reparaciones.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import ec.gob.loja.reparaciones.IntegrationTest;
import ec.gob.loja.reparaciones.domain.RegistroDanio;
import ec.gob.loja.reparaciones.repository.EntityManager;
import ec.gob.loja.reparaciones.repository.RegistroDanioRepository;
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
 * Integration tests for the {@link RegistroDanioResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class RegistroDanioResourceIT {

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_FECHA_INICIO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_INICIO = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_FECHA_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_FIN = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION = "BBBBBBBBBB";

    private static final String DEFAULT_PARROQUIA = "AAAAAAAAAA";
    private static final String UPDATED_PARROQUIA = "BBBBBBBBBB";

    private static final String DEFAULT_BARRIO = "AAAAAAAAAA";
    private static final String UPDATED_BARRIO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/registro-danios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RegistroDanioRepository registroDanioRepository;

    @Mock
    private RegistroDanioRepository registroDanioRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private RegistroDanio registroDanio;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RegistroDanio createEntity(EntityManager em) {
        RegistroDanio registroDanio = new RegistroDanio()
            .codigo(DEFAULT_CODIGO)
            .fecha(DEFAULT_FECHA)
            .fechaInicio(DEFAULT_FECHA_INICIO)
            .fechaFin(DEFAULT_FECHA_FIN)
            .direccion(DEFAULT_DIRECCION)
            .parroquia(DEFAULT_PARROQUIA)
            .barrio(DEFAULT_BARRIO);
        return registroDanio;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RegistroDanio createUpdatedEntity(EntityManager em) {
        RegistroDanio registroDanio = new RegistroDanio()
            .codigo(UPDATED_CODIGO)
            .fecha(UPDATED_FECHA)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaFin(UPDATED_FECHA_FIN)
            .direccion(UPDATED_DIRECCION)
            .parroquia(UPDATED_PARROQUIA)
            .barrio(UPDATED_BARRIO);
        return registroDanio;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(RegistroDanio.class).block();
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
        registroDanio = createEntity(em);
    }

    @Test
    void createRegistroDanio() throws Exception {
        int databaseSizeBeforeCreate = registroDanioRepository.findAll().collectList().block().size();
        // Create the RegistroDanio
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(registroDanio))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the RegistroDanio in the database
        List<RegistroDanio> registroDanioList = registroDanioRepository.findAll().collectList().block();
        assertThat(registroDanioList).hasSize(databaseSizeBeforeCreate + 1);
        RegistroDanio testRegistroDanio = registroDanioList.get(registroDanioList.size() - 1);
        assertThat(testRegistroDanio.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testRegistroDanio.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testRegistroDanio.getFechaInicio()).isEqualTo(DEFAULT_FECHA_INICIO);
        assertThat(testRegistroDanio.getFechaFin()).isEqualTo(DEFAULT_FECHA_FIN);
        assertThat(testRegistroDanio.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
        assertThat(testRegistroDanio.getParroquia()).isEqualTo(DEFAULT_PARROQUIA);
        assertThat(testRegistroDanio.getBarrio()).isEqualTo(DEFAULT_BARRIO);
    }

    @Test
    void createRegistroDanioWithExistingId() throws Exception {
        // Create the RegistroDanio with an existing ID
        registroDanio.setId(1L);

        int databaseSizeBeforeCreate = registroDanioRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(registroDanio))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RegistroDanio in the database
        List<RegistroDanio> registroDanioList = registroDanioRepository.findAll().collectList().block();
        assertThat(registroDanioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkCodigoIsRequired() throws Exception {
        int databaseSizeBeforeTest = registroDanioRepository.findAll().collectList().block().size();
        // set the field null
        registroDanio.setCodigo(null);

        // Create the RegistroDanio, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(registroDanio))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<RegistroDanio> registroDanioList = registroDanioRepository.findAll().collectList().block();
        assertThat(registroDanioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllRegistroDaniosAsStream() {
        // Initialize the database
        registroDanioRepository.save(registroDanio).block();

        List<RegistroDanio> registroDanioList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(RegistroDanio.class)
            .getResponseBody()
            .filter(registroDanio::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(registroDanioList).isNotNull();
        assertThat(registroDanioList).hasSize(1);
        RegistroDanio testRegistroDanio = registroDanioList.get(0);
        assertThat(testRegistroDanio.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testRegistroDanio.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testRegistroDanio.getFechaInicio()).isEqualTo(DEFAULT_FECHA_INICIO);
        assertThat(testRegistroDanio.getFechaFin()).isEqualTo(DEFAULT_FECHA_FIN);
        assertThat(testRegistroDanio.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
        assertThat(testRegistroDanio.getParroquia()).isEqualTo(DEFAULT_PARROQUIA);
        assertThat(testRegistroDanio.getBarrio()).isEqualTo(DEFAULT_BARRIO);
    }

    @Test
    void getAllRegistroDanios() {
        // Initialize the database
        registroDanioRepository.save(registroDanio).block();

        // Get all the registroDanioList
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
            .value(hasItem(registroDanio.getId().intValue()))
            .jsonPath("$.[*].codigo")
            .value(hasItem(DEFAULT_CODIGO))
            .jsonPath("$.[*].fecha")
            .value(hasItem(DEFAULT_FECHA.toString()))
            .jsonPath("$.[*].fechaInicio")
            .value(hasItem(DEFAULT_FECHA_INICIO.toString()))
            .jsonPath("$.[*].fechaFin")
            .value(hasItem(DEFAULT_FECHA_FIN.toString()))
            .jsonPath("$.[*].direccion")
            .value(hasItem(DEFAULT_DIRECCION))
            .jsonPath("$.[*].parroquia")
            .value(hasItem(DEFAULT_PARROQUIA))
            .jsonPath("$.[*].barrio")
            .value(hasItem(DEFAULT_BARRIO));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRegistroDaniosWithEagerRelationshipsIsEnabled() {
        when(registroDanioRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(registroDanioRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRegistroDaniosWithEagerRelationshipsIsNotEnabled() {
        when(registroDanioRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(registroDanioRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getRegistroDanio() {
        // Initialize the database
        registroDanioRepository.save(registroDanio).block();

        // Get the registroDanio
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, registroDanio.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(registroDanio.getId().intValue()))
            .jsonPath("$.codigo")
            .value(is(DEFAULT_CODIGO))
            .jsonPath("$.fecha")
            .value(is(DEFAULT_FECHA.toString()))
            .jsonPath("$.fechaInicio")
            .value(is(DEFAULT_FECHA_INICIO.toString()))
            .jsonPath("$.fechaFin")
            .value(is(DEFAULT_FECHA_FIN.toString()))
            .jsonPath("$.direccion")
            .value(is(DEFAULT_DIRECCION))
            .jsonPath("$.parroquia")
            .value(is(DEFAULT_PARROQUIA))
            .jsonPath("$.barrio")
            .value(is(DEFAULT_BARRIO));
    }

    @Test
    void getNonExistingRegistroDanio() {
        // Get the registroDanio
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingRegistroDanio() throws Exception {
        // Initialize the database
        registroDanioRepository.save(registroDanio).block();

        int databaseSizeBeforeUpdate = registroDanioRepository.findAll().collectList().block().size();

        // Update the registroDanio
        RegistroDanio updatedRegistroDanio = registroDanioRepository.findById(registroDanio.getId()).block();
        updatedRegistroDanio
            .codigo(UPDATED_CODIGO)
            .fecha(UPDATED_FECHA)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaFin(UPDATED_FECHA_FIN)
            .direccion(UPDATED_DIRECCION)
            .parroquia(UPDATED_PARROQUIA)
            .barrio(UPDATED_BARRIO);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedRegistroDanio.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedRegistroDanio))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the RegistroDanio in the database
        List<RegistroDanio> registroDanioList = registroDanioRepository.findAll().collectList().block();
        assertThat(registroDanioList).hasSize(databaseSizeBeforeUpdate);
        RegistroDanio testRegistroDanio = registroDanioList.get(registroDanioList.size() - 1);
        assertThat(testRegistroDanio.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testRegistroDanio.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testRegistroDanio.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testRegistroDanio.getFechaFin()).isEqualTo(UPDATED_FECHA_FIN);
        assertThat(testRegistroDanio.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testRegistroDanio.getParroquia()).isEqualTo(UPDATED_PARROQUIA);
        assertThat(testRegistroDanio.getBarrio()).isEqualTo(UPDATED_BARRIO);
    }

    @Test
    void putNonExistingRegistroDanio() throws Exception {
        int databaseSizeBeforeUpdate = registroDanioRepository.findAll().collectList().block().size();
        registroDanio.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, registroDanio.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(registroDanio))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RegistroDanio in the database
        List<RegistroDanio> registroDanioList = registroDanioRepository.findAll().collectList().block();
        assertThat(registroDanioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchRegistroDanio() throws Exception {
        int databaseSizeBeforeUpdate = registroDanioRepository.findAll().collectList().block().size();
        registroDanio.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(registroDanio))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RegistroDanio in the database
        List<RegistroDanio> registroDanioList = registroDanioRepository.findAll().collectList().block();
        assertThat(registroDanioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamRegistroDanio() throws Exception {
        int databaseSizeBeforeUpdate = registroDanioRepository.findAll().collectList().block().size();
        registroDanio.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(registroDanio))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the RegistroDanio in the database
        List<RegistroDanio> registroDanioList = registroDanioRepository.findAll().collectList().block();
        assertThat(registroDanioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateRegistroDanioWithPatch() throws Exception {
        // Initialize the database
        registroDanioRepository.save(registroDanio).block();

        int databaseSizeBeforeUpdate = registroDanioRepository.findAll().collectList().block().size();

        // Update the registroDanio using partial update
        RegistroDanio partialUpdatedRegistroDanio = new RegistroDanio();
        partialUpdatedRegistroDanio.setId(registroDanio.getId());

        partialUpdatedRegistroDanio.fechaFin(UPDATED_FECHA_FIN).parroquia(UPDATED_PARROQUIA).barrio(UPDATED_BARRIO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedRegistroDanio.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedRegistroDanio))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the RegistroDanio in the database
        List<RegistroDanio> registroDanioList = registroDanioRepository.findAll().collectList().block();
        assertThat(registroDanioList).hasSize(databaseSizeBeforeUpdate);
        RegistroDanio testRegistroDanio = registroDanioList.get(registroDanioList.size() - 1);
        assertThat(testRegistroDanio.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testRegistroDanio.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testRegistroDanio.getFechaInicio()).isEqualTo(DEFAULT_FECHA_INICIO);
        assertThat(testRegistroDanio.getFechaFin()).isEqualTo(UPDATED_FECHA_FIN);
        assertThat(testRegistroDanio.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
        assertThat(testRegistroDanio.getParroquia()).isEqualTo(UPDATED_PARROQUIA);
        assertThat(testRegistroDanio.getBarrio()).isEqualTo(UPDATED_BARRIO);
    }

    @Test
    void fullUpdateRegistroDanioWithPatch() throws Exception {
        // Initialize the database
        registroDanioRepository.save(registroDanio).block();

        int databaseSizeBeforeUpdate = registroDanioRepository.findAll().collectList().block().size();

        // Update the registroDanio using partial update
        RegistroDanio partialUpdatedRegistroDanio = new RegistroDanio();
        partialUpdatedRegistroDanio.setId(registroDanio.getId());

        partialUpdatedRegistroDanio
            .codigo(UPDATED_CODIGO)
            .fecha(UPDATED_FECHA)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaFin(UPDATED_FECHA_FIN)
            .direccion(UPDATED_DIRECCION)
            .parroquia(UPDATED_PARROQUIA)
            .barrio(UPDATED_BARRIO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedRegistroDanio.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedRegistroDanio))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the RegistroDanio in the database
        List<RegistroDanio> registroDanioList = registroDanioRepository.findAll().collectList().block();
        assertThat(registroDanioList).hasSize(databaseSizeBeforeUpdate);
        RegistroDanio testRegistroDanio = registroDanioList.get(registroDanioList.size() - 1);
        assertThat(testRegistroDanio.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testRegistroDanio.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testRegistroDanio.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testRegistroDanio.getFechaFin()).isEqualTo(UPDATED_FECHA_FIN);
        assertThat(testRegistroDanio.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testRegistroDanio.getParroquia()).isEqualTo(UPDATED_PARROQUIA);
        assertThat(testRegistroDanio.getBarrio()).isEqualTo(UPDATED_BARRIO);
    }

    @Test
    void patchNonExistingRegistroDanio() throws Exception {
        int databaseSizeBeforeUpdate = registroDanioRepository.findAll().collectList().block().size();
        registroDanio.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, registroDanio.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(registroDanio))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RegistroDanio in the database
        List<RegistroDanio> registroDanioList = registroDanioRepository.findAll().collectList().block();
        assertThat(registroDanioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchRegistroDanio() throws Exception {
        int databaseSizeBeforeUpdate = registroDanioRepository.findAll().collectList().block().size();
        registroDanio.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(registroDanio))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RegistroDanio in the database
        List<RegistroDanio> registroDanioList = registroDanioRepository.findAll().collectList().block();
        assertThat(registroDanioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamRegistroDanio() throws Exception {
        int databaseSizeBeforeUpdate = registroDanioRepository.findAll().collectList().block().size();
        registroDanio.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(registroDanio))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the RegistroDanio in the database
        List<RegistroDanio> registroDanioList = registroDanioRepository.findAll().collectList().block();
        assertThat(registroDanioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteRegistroDanio() {
        // Initialize the database
        registroDanioRepository.save(registroDanio).block();

        int databaseSizeBeforeDelete = registroDanioRepository.findAll().collectList().block().size();

        // Delete the registroDanio
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, registroDanio.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<RegistroDanio> registroDanioList = registroDanioRepository.findAll().collectList().block();
        assertThat(registroDanioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

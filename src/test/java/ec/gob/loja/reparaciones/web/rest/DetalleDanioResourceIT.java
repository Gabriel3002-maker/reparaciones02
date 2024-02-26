package ec.gob.loja.reparaciones.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import ec.gob.loja.reparaciones.IntegrationTest;
import ec.gob.loja.reparaciones.domain.CatalogoItem;
import ec.gob.loja.reparaciones.domain.DetalleDanio;
import ec.gob.loja.reparaciones.repository.DetalleDanioRepository;
import ec.gob.loja.reparaciones.repository.EntityManager;
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
 * Integration tests for the {@link DetalleDanioResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class DetalleDanioResourceIT {

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION_DANIO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION_DANIO = "BBBBBBBBBB";

    private static final String DEFAULT_TECNICO = "AAAAAAAAAA";
    private static final String UPDATED_TECNICO = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_PERSON = "AAAAAAAAAA";
    private static final String UPDATED_NAME_PERSON = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION = "BBBBBBBBBB";

    private static final String DEFAULT_ESTADO_REPARACION = "AAAAAAAAAA";
    private static final String UPDATED_ESTADO_REPARACION = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVACION = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/detalle-danios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DetalleDanioRepository detalleDanioRepository;

    @Mock
    private DetalleDanioRepository detalleDanioRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private DetalleDanio detalleDanio;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DetalleDanio createEntity(EntityManager em) {
        DetalleDanio detalleDanio = new DetalleDanio()
            .codigo(DEFAULT_CODIGO)
            .descripcionDanio(DEFAULT_DESCRIPCION_DANIO)
            .tecnico(DEFAULT_TECNICO)
            .namePerson(DEFAULT_NAME_PERSON)
            .direccion(DEFAULT_DIRECCION)
            .estadoReparacion(DEFAULT_ESTADO_REPARACION)
            .observacion(DEFAULT_OBSERVACION);
        // Add required entity
        CatalogoItem catalogoItem;
        catalogoItem = em.insert(CatalogoItemResourceIT.createEntity(em)).block();
        detalleDanio.setTipoDanio(catalogoItem);
        return detalleDanio;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DetalleDanio createUpdatedEntity(EntityManager em) {
        DetalleDanio detalleDanio = new DetalleDanio()
            .codigo(UPDATED_CODIGO)
            .descripcionDanio(UPDATED_DESCRIPCION_DANIO)
            .tecnico(UPDATED_TECNICO)
            .namePerson(UPDATED_NAME_PERSON)
            .direccion(UPDATED_DIRECCION)
            .estadoReparacion(UPDATED_ESTADO_REPARACION)
            .observacion(UPDATED_OBSERVACION);
        // Add required entity
        CatalogoItem catalogoItem;
        catalogoItem = em.insert(CatalogoItemResourceIT.createUpdatedEntity(em)).block();
        detalleDanio.setTipoDanio(catalogoItem);
        return detalleDanio;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(DetalleDanio.class).block();
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
        detalleDanio = createEntity(em);
    }

    @Test
    void createDetalleDanio() throws Exception {
        int databaseSizeBeforeCreate = detalleDanioRepository.findAll().collectList().block().size();
        // Create the DetalleDanio
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(detalleDanio))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the DetalleDanio in the database
        List<DetalleDanio> detalleDanioList = detalleDanioRepository.findAll().collectList().block();
        assertThat(detalleDanioList).hasSize(databaseSizeBeforeCreate + 1);
        DetalleDanio testDetalleDanio = detalleDanioList.get(detalleDanioList.size() - 1);
        assertThat(testDetalleDanio.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testDetalleDanio.getDescripcionDanio()).isEqualTo(DEFAULT_DESCRIPCION_DANIO);
        assertThat(testDetalleDanio.getTecnico()).isEqualTo(DEFAULT_TECNICO);
        assertThat(testDetalleDanio.getNamePerson()).isEqualTo(DEFAULT_NAME_PERSON);
        assertThat(testDetalleDanio.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
        assertThat(testDetalleDanio.getEstadoReparacion()).isEqualTo(DEFAULT_ESTADO_REPARACION);
        assertThat(testDetalleDanio.getObservacion()).isEqualTo(DEFAULT_OBSERVACION);
    }

    @Test
    void createDetalleDanioWithExistingId() throws Exception {
        // Create the DetalleDanio with an existing ID
        detalleDanio.setId(1L);

        int databaseSizeBeforeCreate = detalleDanioRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(detalleDanio))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DetalleDanio in the database
        List<DetalleDanio> detalleDanioList = detalleDanioRepository.findAll().collectList().block();
        assertThat(detalleDanioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkCodigoIsRequired() throws Exception {
        int databaseSizeBeforeTest = detalleDanioRepository.findAll().collectList().block().size();
        // set the field null
        detalleDanio.setCodigo(null);

        // Create the DetalleDanio, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(detalleDanio))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<DetalleDanio> detalleDanioList = detalleDanioRepository.findAll().collectList().block();
        assertThat(detalleDanioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllDetalleDaniosAsStream() {
        // Initialize the database
        detalleDanioRepository.save(detalleDanio).block();

        List<DetalleDanio> detalleDanioList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(DetalleDanio.class)
            .getResponseBody()
            .filter(detalleDanio::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(detalleDanioList).isNotNull();
        assertThat(detalleDanioList).hasSize(1);
        DetalleDanio testDetalleDanio = detalleDanioList.get(0);
        assertThat(testDetalleDanio.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testDetalleDanio.getDescripcionDanio()).isEqualTo(DEFAULT_DESCRIPCION_DANIO);
        assertThat(testDetalleDanio.getTecnico()).isEqualTo(DEFAULT_TECNICO);
        assertThat(testDetalleDanio.getNamePerson()).isEqualTo(DEFAULT_NAME_PERSON);
        assertThat(testDetalleDanio.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
        assertThat(testDetalleDanio.getEstadoReparacion()).isEqualTo(DEFAULT_ESTADO_REPARACION);
        assertThat(testDetalleDanio.getObservacion()).isEqualTo(DEFAULT_OBSERVACION);
    }

    @Test
    void getAllDetalleDanios() {
        // Initialize the database
        detalleDanioRepository.save(detalleDanio).block();

        // Get all the detalleDanioList
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
            .value(hasItem(detalleDanio.getId().intValue()))
            .jsonPath("$.[*].codigo")
            .value(hasItem(DEFAULT_CODIGO))
            .jsonPath("$.[*].descripcionDanio")
            .value(hasItem(DEFAULT_DESCRIPCION_DANIO))
            .jsonPath("$.[*].tecnico")
            .value(hasItem(DEFAULT_TECNICO))
            .jsonPath("$.[*].namePerson")
            .value(hasItem(DEFAULT_NAME_PERSON))
            .jsonPath("$.[*].direccion")
            .value(hasItem(DEFAULT_DIRECCION))
            .jsonPath("$.[*].estadoReparacion")
            .value(hasItem(DEFAULT_ESTADO_REPARACION))
            .jsonPath("$.[*].observacion")
            .value(hasItem(DEFAULT_OBSERVACION));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDetalleDaniosWithEagerRelationshipsIsEnabled() {
        when(detalleDanioRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(detalleDanioRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDetalleDaniosWithEagerRelationshipsIsNotEnabled() {
        when(detalleDanioRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(detalleDanioRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getDetalleDanio() {
        // Initialize the database
        detalleDanioRepository.save(detalleDanio).block();

        // Get the detalleDanio
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, detalleDanio.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(detalleDanio.getId().intValue()))
            .jsonPath("$.codigo")
            .value(is(DEFAULT_CODIGO))
            .jsonPath("$.descripcionDanio")
            .value(is(DEFAULT_DESCRIPCION_DANIO))
            .jsonPath("$.tecnico")
            .value(is(DEFAULT_TECNICO))
            .jsonPath("$.namePerson")
            .value(is(DEFAULT_NAME_PERSON))
            .jsonPath("$.direccion")
            .value(is(DEFAULT_DIRECCION))
            .jsonPath("$.estadoReparacion")
            .value(is(DEFAULT_ESTADO_REPARACION))
            .jsonPath("$.observacion")
            .value(is(DEFAULT_OBSERVACION));
    }

    @Test
    void getNonExistingDetalleDanio() {
        // Get the detalleDanio
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingDetalleDanio() throws Exception {
        // Initialize the database
        detalleDanioRepository.save(detalleDanio).block();

        int databaseSizeBeforeUpdate = detalleDanioRepository.findAll().collectList().block().size();

        // Update the detalleDanio
        DetalleDanio updatedDetalleDanio = detalleDanioRepository.findById(detalleDanio.getId()).block();
        updatedDetalleDanio
            .codigo(UPDATED_CODIGO)
            .descripcionDanio(UPDATED_DESCRIPCION_DANIO)
            .tecnico(UPDATED_TECNICO)
            .namePerson(UPDATED_NAME_PERSON)
            .direccion(UPDATED_DIRECCION)
            .estadoReparacion(UPDATED_ESTADO_REPARACION)
            .observacion(UPDATED_OBSERVACION);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedDetalleDanio.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedDetalleDanio))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the DetalleDanio in the database
        List<DetalleDanio> detalleDanioList = detalleDanioRepository.findAll().collectList().block();
        assertThat(detalleDanioList).hasSize(databaseSizeBeforeUpdate);
        DetalleDanio testDetalleDanio = detalleDanioList.get(detalleDanioList.size() - 1);
        assertThat(testDetalleDanio.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testDetalleDanio.getDescripcionDanio()).isEqualTo(UPDATED_DESCRIPCION_DANIO);
        assertThat(testDetalleDanio.getTecnico()).isEqualTo(UPDATED_TECNICO);
        assertThat(testDetalleDanio.getNamePerson()).isEqualTo(UPDATED_NAME_PERSON);
        assertThat(testDetalleDanio.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testDetalleDanio.getEstadoReparacion()).isEqualTo(UPDATED_ESTADO_REPARACION);
        assertThat(testDetalleDanio.getObservacion()).isEqualTo(UPDATED_OBSERVACION);
    }

    @Test
    void putNonExistingDetalleDanio() throws Exception {
        int databaseSizeBeforeUpdate = detalleDanioRepository.findAll().collectList().block().size();
        detalleDanio.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, detalleDanio.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(detalleDanio))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DetalleDanio in the database
        List<DetalleDanio> detalleDanioList = detalleDanioRepository.findAll().collectList().block();
        assertThat(detalleDanioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchDetalleDanio() throws Exception {
        int databaseSizeBeforeUpdate = detalleDanioRepository.findAll().collectList().block().size();
        detalleDanio.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(detalleDanio))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DetalleDanio in the database
        List<DetalleDanio> detalleDanioList = detalleDanioRepository.findAll().collectList().block();
        assertThat(detalleDanioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamDetalleDanio() throws Exception {
        int databaseSizeBeforeUpdate = detalleDanioRepository.findAll().collectList().block().size();
        detalleDanio.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(detalleDanio))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the DetalleDanio in the database
        List<DetalleDanio> detalleDanioList = detalleDanioRepository.findAll().collectList().block();
        assertThat(detalleDanioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateDetalleDanioWithPatch() throws Exception {
        // Initialize the database
        detalleDanioRepository.save(detalleDanio).block();

        int databaseSizeBeforeUpdate = detalleDanioRepository.findAll().collectList().block().size();

        // Update the detalleDanio using partial update
        DetalleDanio partialUpdatedDetalleDanio = new DetalleDanio();
        partialUpdatedDetalleDanio.setId(detalleDanio.getId());

        partialUpdatedDetalleDanio.codigo(UPDATED_CODIGO).descripcionDanio(UPDATED_DESCRIPCION_DANIO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDetalleDanio.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedDetalleDanio))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the DetalleDanio in the database
        List<DetalleDanio> detalleDanioList = detalleDanioRepository.findAll().collectList().block();
        assertThat(detalleDanioList).hasSize(databaseSizeBeforeUpdate);
        DetalleDanio testDetalleDanio = detalleDanioList.get(detalleDanioList.size() - 1);
        assertThat(testDetalleDanio.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testDetalleDanio.getDescripcionDanio()).isEqualTo(UPDATED_DESCRIPCION_DANIO);
        assertThat(testDetalleDanio.getTecnico()).isEqualTo(DEFAULT_TECNICO);
        assertThat(testDetalleDanio.getNamePerson()).isEqualTo(DEFAULT_NAME_PERSON);
        assertThat(testDetalleDanio.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
        assertThat(testDetalleDanio.getEstadoReparacion()).isEqualTo(DEFAULT_ESTADO_REPARACION);
        assertThat(testDetalleDanio.getObservacion()).isEqualTo(DEFAULT_OBSERVACION);
    }

    @Test
    void fullUpdateDetalleDanioWithPatch() throws Exception {
        // Initialize the database
        detalleDanioRepository.save(detalleDanio).block();

        int databaseSizeBeforeUpdate = detalleDanioRepository.findAll().collectList().block().size();

        // Update the detalleDanio using partial update
        DetalleDanio partialUpdatedDetalleDanio = new DetalleDanio();
        partialUpdatedDetalleDanio.setId(detalleDanio.getId());

        partialUpdatedDetalleDanio
            .codigo(UPDATED_CODIGO)
            .descripcionDanio(UPDATED_DESCRIPCION_DANIO)
            .tecnico(UPDATED_TECNICO)
            .namePerson(UPDATED_NAME_PERSON)
            .direccion(UPDATED_DIRECCION)
            .estadoReparacion(UPDATED_ESTADO_REPARACION)
            .observacion(UPDATED_OBSERVACION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDetalleDanio.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedDetalleDanio))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the DetalleDanio in the database
        List<DetalleDanio> detalleDanioList = detalleDanioRepository.findAll().collectList().block();
        assertThat(detalleDanioList).hasSize(databaseSizeBeforeUpdate);
        DetalleDanio testDetalleDanio = detalleDanioList.get(detalleDanioList.size() - 1);
        assertThat(testDetalleDanio.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testDetalleDanio.getDescripcionDanio()).isEqualTo(UPDATED_DESCRIPCION_DANIO);
        assertThat(testDetalleDanio.getTecnico()).isEqualTo(UPDATED_TECNICO);
        assertThat(testDetalleDanio.getNamePerson()).isEqualTo(UPDATED_NAME_PERSON);
        assertThat(testDetalleDanio.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testDetalleDanio.getEstadoReparacion()).isEqualTo(UPDATED_ESTADO_REPARACION);
        assertThat(testDetalleDanio.getObservacion()).isEqualTo(UPDATED_OBSERVACION);
    }

    @Test
    void patchNonExistingDetalleDanio() throws Exception {
        int databaseSizeBeforeUpdate = detalleDanioRepository.findAll().collectList().block().size();
        detalleDanio.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, detalleDanio.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(detalleDanio))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DetalleDanio in the database
        List<DetalleDanio> detalleDanioList = detalleDanioRepository.findAll().collectList().block();
        assertThat(detalleDanioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchDetalleDanio() throws Exception {
        int databaseSizeBeforeUpdate = detalleDanioRepository.findAll().collectList().block().size();
        detalleDanio.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(detalleDanio))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DetalleDanio in the database
        List<DetalleDanio> detalleDanioList = detalleDanioRepository.findAll().collectList().block();
        assertThat(detalleDanioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamDetalleDanio() throws Exception {
        int databaseSizeBeforeUpdate = detalleDanioRepository.findAll().collectList().block().size();
        detalleDanio.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(detalleDanio))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the DetalleDanio in the database
        List<DetalleDanio> detalleDanioList = detalleDanioRepository.findAll().collectList().block();
        assertThat(detalleDanioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteDetalleDanio() {
        // Initialize the database
        detalleDanioRepository.save(detalleDanio).block();

        int databaseSizeBeforeDelete = detalleDanioRepository.findAll().collectList().block().size();

        // Delete the detalleDanio
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, detalleDanio.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<DetalleDanio> detalleDanioList = detalleDanioRepository.findAll().collectList().block();
        assertThat(detalleDanioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

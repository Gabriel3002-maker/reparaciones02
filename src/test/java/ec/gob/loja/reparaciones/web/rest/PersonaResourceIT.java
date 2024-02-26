package ec.gob.loja.reparaciones.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import ec.gob.loja.reparaciones.IntegrationTest;
import ec.gob.loja.reparaciones.domain.CatalogoItem;
import ec.gob.loja.reparaciones.domain.Persona;
import ec.gob.loja.reparaciones.repository.EntityManager;
import ec.gob.loja.reparaciones.repository.PersonaRepository;
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
 * Integration tests for the {@link PersonaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PersonaResourceIT {

    private static final String DEFAULT_IDENTIFICACION = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFICACION = "BBBBBBBBBB";

    private static final String DEFAULT_PRIMER_APELLIDO = "AAAAAAAAAA";
    private static final String UPDATED_PRIMER_APELLIDO = "BBBBBBBBBB";

    private static final String DEFAULT_SEGUNDO_APELLIDO = "AAAAAAAAAA";
    private static final String UPDATED_SEGUNDO_APELLIDO = "BBBBBBBBBB";

    private static final String DEFAULT_PRIMER_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_PRIMER_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_SEGUNDO_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_SEGUNDO_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_CELULAR = "AAAAAAAAAA";
    private static final String UPDATED_CELULAR = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO_CONVENCIONAL = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO_CONVENCIONAL = "BBBBBBBBBB";

    private static final String DEFAULT_CORREO = "AAAAAAAAAA";
    private static final String UPDATED_CORREO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/personas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PersonaRepository personaRepository;

    @Mock
    private PersonaRepository personaRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Persona persona;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Persona createEntity(EntityManager em) {
        Persona persona = new Persona()
            .identificacion(DEFAULT_IDENTIFICACION)
            .primerApellido(DEFAULT_PRIMER_APELLIDO)
            .segundoApellido(DEFAULT_SEGUNDO_APELLIDO)
            .primerNombre(DEFAULT_PRIMER_NOMBRE)
            .segundoNombre(DEFAULT_SEGUNDO_NOMBRE)
            .celular(DEFAULT_CELULAR)
            .telefonoConvencional(DEFAULT_TELEFONO_CONVENCIONAL)
            .correo(DEFAULT_CORREO);
        // Add required entity
        CatalogoItem catalogoItem;
        catalogoItem = em.insert(CatalogoItemResourceIT.createEntity(em)).block();
        persona.setTipoIdentificacion(catalogoItem);
        return persona;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Persona createUpdatedEntity(EntityManager em) {
        Persona persona = new Persona()
            .identificacion(UPDATED_IDENTIFICACION)
            .primerApellido(UPDATED_PRIMER_APELLIDO)
            .segundoApellido(UPDATED_SEGUNDO_APELLIDO)
            .primerNombre(UPDATED_PRIMER_NOMBRE)
            .segundoNombre(UPDATED_SEGUNDO_NOMBRE)
            .celular(UPDATED_CELULAR)
            .telefonoConvencional(UPDATED_TELEFONO_CONVENCIONAL)
            .correo(UPDATED_CORREO);
        // Add required entity
        CatalogoItem catalogoItem;
        catalogoItem = em.insert(CatalogoItemResourceIT.createUpdatedEntity(em)).block();
        persona.setTipoIdentificacion(catalogoItem);
        return persona;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Persona.class).block();
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
        persona = createEntity(em);
    }

    @Test
    void createPersona() throws Exception {
        int databaseSizeBeforeCreate = personaRepository.findAll().collectList().block().size();
        // Create the Persona
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(persona))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Persona in the database
        List<Persona> personaList = personaRepository.findAll().collectList().block();
        assertThat(personaList).hasSize(databaseSizeBeforeCreate + 1);
        Persona testPersona = personaList.get(personaList.size() - 1);
        assertThat(testPersona.getIdentificacion()).isEqualTo(DEFAULT_IDENTIFICACION);
        assertThat(testPersona.getPrimerApellido()).isEqualTo(DEFAULT_PRIMER_APELLIDO);
        assertThat(testPersona.getSegundoApellido()).isEqualTo(DEFAULT_SEGUNDO_APELLIDO);
        assertThat(testPersona.getPrimerNombre()).isEqualTo(DEFAULT_PRIMER_NOMBRE);
        assertThat(testPersona.getSegundoNombre()).isEqualTo(DEFAULT_SEGUNDO_NOMBRE);
        assertThat(testPersona.getCelular()).isEqualTo(DEFAULT_CELULAR);
        assertThat(testPersona.getTelefonoConvencional()).isEqualTo(DEFAULT_TELEFONO_CONVENCIONAL);
        assertThat(testPersona.getCorreo()).isEqualTo(DEFAULT_CORREO);
    }

    @Test
    void createPersonaWithExistingId() throws Exception {
        // Create the Persona with an existing ID
        persona.setId(1L);

        int databaseSizeBeforeCreate = personaRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(persona))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Persona in the database
        List<Persona> personaList = personaRepository.findAll().collectList().block();
        assertThat(personaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkIdentificacionIsRequired() throws Exception {
        int databaseSizeBeforeTest = personaRepository.findAll().collectList().block().size();
        // set the field null
        persona.setIdentificacion(null);

        // Create the Persona, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(persona))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Persona> personaList = personaRepository.findAll().collectList().block();
        assertThat(personaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPrimerApellidoIsRequired() throws Exception {
        int databaseSizeBeforeTest = personaRepository.findAll().collectList().block().size();
        // set the field null
        persona.setPrimerApellido(null);

        // Create the Persona, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(persona))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Persona> personaList = personaRepository.findAll().collectList().block();
        assertThat(personaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPrimerNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = personaRepository.findAll().collectList().block().size();
        // set the field null
        persona.setPrimerNombre(null);

        // Create the Persona, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(persona))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Persona> personaList = personaRepository.findAll().collectList().block();
        assertThat(personaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllPersonasAsStream() {
        // Initialize the database
        personaRepository.save(persona).block();

        List<Persona> personaList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Persona.class)
            .getResponseBody()
            .filter(persona::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(personaList).isNotNull();
        assertThat(personaList).hasSize(1);
        Persona testPersona = personaList.get(0);
        assertThat(testPersona.getIdentificacion()).isEqualTo(DEFAULT_IDENTIFICACION);
        assertThat(testPersona.getPrimerApellido()).isEqualTo(DEFAULT_PRIMER_APELLIDO);
        assertThat(testPersona.getSegundoApellido()).isEqualTo(DEFAULT_SEGUNDO_APELLIDO);
        assertThat(testPersona.getPrimerNombre()).isEqualTo(DEFAULT_PRIMER_NOMBRE);
        assertThat(testPersona.getSegundoNombre()).isEqualTo(DEFAULT_SEGUNDO_NOMBRE);
        assertThat(testPersona.getCelular()).isEqualTo(DEFAULT_CELULAR);
        assertThat(testPersona.getTelefonoConvencional()).isEqualTo(DEFAULT_TELEFONO_CONVENCIONAL);
        assertThat(testPersona.getCorreo()).isEqualTo(DEFAULT_CORREO);
    }

    @Test
    void getAllPersonas() {
        // Initialize the database
        personaRepository.save(persona).block();

        // Get all the personaList
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
            .value(hasItem(persona.getId().intValue()))
            .jsonPath("$.[*].identificacion")
            .value(hasItem(DEFAULT_IDENTIFICACION))
            .jsonPath("$.[*].primerApellido")
            .value(hasItem(DEFAULT_PRIMER_APELLIDO))
            .jsonPath("$.[*].segundoApellido")
            .value(hasItem(DEFAULT_SEGUNDO_APELLIDO))
            .jsonPath("$.[*].primerNombre")
            .value(hasItem(DEFAULT_PRIMER_NOMBRE))
            .jsonPath("$.[*].segundoNombre")
            .value(hasItem(DEFAULT_SEGUNDO_NOMBRE))
            .jsonPath("$.[*].celular")
            .value(hasItem(DEFAULT_CELULAR))
            .jsonPath("$.[*].telefonoConvencional")
            .value(hasItem(DEFAULT_TELEFONO_CONVENCIONAL))
            .jsonPath("$.[*].correo")
            .value(hasItem(DEFAULT_CORREO));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPersonasWithEagerRelationshipsIsEnabled() {
        when(personaRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(personaRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPersonasWithEagerRelationshipsIsNotEnabled() {
        when(personaRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(personaRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getPersona() {
        // Initialize the database
        personaRepository.save(persona).block();

        // Get the persona
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, persona.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(persona.getId().intValue()))
            .jsonPath("$.identificacion")
            .value(is(DEFAULT_IDENTIFICACION))
            .jsonPath("$.primerApellido")
            .value(is(DEFAULT_PRIMER_APELLIDO))
            .jsonPath("$.segundoApellido")
            .value(is(DEFAULT_SEGUNDO_APELLIDO))
            .jsonPath("$.primerNombre")
            .value(is(DEFAULT_PRIMER_NOMBRE))
            .jsonPath("$.segundoNombre")
            .value(is(DEFAULT_SEGUNDO_NOMBRE))
            .jsonPath("$.celular")
            .value(is(DEFAULT_CELULAR))
            .jsonPath("$.telefonoConvencional")
            .value(is(DEFAULT_TELEFONO_CONVENCIONAL))
            .jsonPath("$.correo")
            .value(is(DEFAULT_CORREO));
    }

    @Test
    void getNonExistingPersona() {
        // Get the persona
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingPersona() throws Exception {
        // Initialize the database
        personaRepository.save(persona).block();

        int databaseSizeBeforeUpdate = personaRepository.findAll().collectList().block().size();

        // Update the persona
        Persona updatedPersona = personaRepository.findById(persona.getId()).block();
        updatedPersona
            .identificacion(UPDATED_IDENTIFICACION)
            .primerApellido(UPDATED_PRIMER_APELLIDO)
            .segundoApellido(UPDATED_SEGUNDO_APELLIDO)
            .primerNombre(UPDATED_PRIMER_NOMBRE)
            .segundoNombre(UPDATED_SEGUNDO_NOMBRE)
            .celular(UPDATED_CELULAR)
            .telefonoConvencional(UPDATED_TELEFONO_CONVENCIONAL)
            .correo(UPDATED_CORREO);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedPersona.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedPersona))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Persona in the database
        List<Persona> personaList = personaRepository.findAll().collectList().block();
        assertThat(personaList).hasSize(databaseSizeBeforeUpdate);
        Persona testPersona = personaList.get(personaList.size() - 1);
        assertThat(testPersona.getIdentificacion()).isEqualTo(UPDATED_IDENTIFICACION);
        assertThat(testPersona.getPrimerApellido()).isEqualTo(UPDATED_PRIMER_APELLIDO);
        assertThat(testPersona.getSegundoApellido()).isEqualTo(UPDATED_SEGUNDO_APELLIDO);
        assertThat(testPersona.getPrimerNombre()).isEqualTo(UPDATED_PRIMER_NOMBRE);
        assertThat(testPersona.getSegundoNombre()).isEqualTo(UPDATED_SEGUNDO_NOMBRE);
        assertThat(testPersona.getCelular()).isEqualTo(UPDATED_CELULAR);
        assertThat(testPersona.getTelefonoConvencional()).isEqualTo(UPDATED_TELEFONO_CONVENCIONAL);
        assertThat(testPersona.getCorreo()).isEqualTo(UPDATED_CORREO);
    }

    @Test
    void putNonExistingPersona() throws Exception {
        int databaseSizeBeforeUpdate = personaRepository.findAll().collectList().block().size();
        persona.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, persona.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(persona))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Persona in the database
        List<Persona> personaList = personaRepository.findAll().collectList().block();
        assertThat(personaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPersona() throws Exception {
        int databaseSizeBeforeUpdate = personaRepository.findAll().collectList().block().size();
        persona.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(persona))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Persona in the database
        List<Persona> personaList = personaRepository.findAll().collectList().block();
        assertThat(personaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPersona() throws Exception {
        int databaseSizeBeforeUpdate = personaRepository.findAll().collectList().block().size();
        persona.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(persona))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Persona in the database
        List<Persona> personaList = personaRepository.findAll().collectList().block();
        assertThat(personaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePersonaWithPatch() throws Exception {
        // Initialize the database
        personaRepository.save(persona).block();

        int databaseSizeBeforeUpdate = personaRepository.findAll().collectList().block().size();

        // Update the persona using partial update
        Persona partialUpdatedPersona = new Persona();
        partialUpdatedPersona.setId(persona.getId());

        partialUpdatedPersona
            .primerApellido(UPDATED_PRIMER_APELLIDO)
            .primerNombre(UPDATED_PRIMER_NOMBRE)
            .celular(UPDATED_CELULAR)
            .telefonoConvencional(UPDATED_TELEFONO_CONVENCIONAL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPersona.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPersona))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Persona in the database
        List<Persona> personaList = personaRepository.findAll().collectList().block();
        assertThat(personaList).hasSize(databaseSizeBeforeUpdate);
        Persona testPersona = personaList.get(personaList.size() - 1);
        assertThat(testPersona.getIdentificacion()).isEqualTo(DEFAULT_IDENTIFICACION);
        assertThat(testPersona.getPrimerApellido()).isEqualTo(UPDATED_PRIMER_APELLIDO);
        assertThat(testPersona.getSegundoApellido()).isEqualTo(DEFAULT_SEGUNDO_APELLIDO);
        assertThat(testPersona.getPrimerNombre()).isEqualTo(UPDATED_PRIMER_NOMBRE);
        assertThat(testPersona.getSegundoNombre()).isEqualTo(DEFAULT_SEGUNDO_NOMBRE);
        assertThat(testPersona.getCelular()).isEqualTo(UPDATED_CELULAR);
        assertThat(testPersona.getTelefonoConvencional()).isEqualTo(UPDATED_TELEFONO_CONVENCIONAL);
        assertThat(testPersona.getCorreo()).isEqualTo(DEFAULT_CORREO);
    }

    @Test
    void fullUpdatePersonaWithPatch() throws Exception {
        // Initialize the database
        personaRepository.save(persona).block();

        int databaseSizeBeforeUpdate = personaRepository.findAll().collectList().block().size();

        // Update the persona using partial update
        Persona partialUpdatedPersona = new Persona();
        partialUpdatedPersona.setId(persona.getId());

        partialUpdatedPersona
            .identificacion(UPDATED_IDENTIFICACION)
            .primerApellido(UPDATED_PRIMER_APELLIDO)
            .segundoApellido(UPDATED_SEGUNDO_APELLIDO)
            .primerNombre(UPDATED_PRIMER_NOMBRE)
            .segundoNombre(UPDATED_SEGUNDO_NOMBRE)
            .celular(UPDATED_CELULAR)
            .telefonoConvencional(UPDATED_TELEFONO_CONVENCIONAL)
            .correo(UPDATED_CORREO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPersona.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPersona))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Persona in the database
        List<Persona> personaList = personaRepository.findAll().collectList().block();
        assertThat(personaList).hasSize(databaseSizeBeforeUpdate);
        Persona testPersona = personaList.get(personaList.size() - 1);
        assertThat(testPersona.getIdentificacion()).isEqualTo(UPDATED_IDENTIFICACION);
        assertThat(testPersona.getPrimerApellido()).isEqualTo(UPDATED_PRIMER_APELLIDO);
        assertThat(testPersona.getSegundoApellido()).isEqualTo(UPDATED_SEGUNDO_APELLIDO);
        assertThat(testPersona.getPrimerNombre()).isEqualTo(UPDATED_PRIMER_NOMBRE);
        assertThat(testPersona.getSegundoNombre()).isEqualTo(UPDATED_SEGUNDO_NOMBRE);
        assertThat(testPersona.getCelular()).isEqualTo(UPDATED_CELULAR);
        assertThat(testPersona.getTelefonoConvencional()).isEqualTo(UPDATED_TELEFONO_CONVENCIONAL);
        assertThat(testPersona.getCorreo()).isEqualTo(UPDATED_CORREO);
    }

    @Test
    void patchNonExistingPersona() throws Exception {
        int databaseSizeBeforeUpdate = personaRepository.findAll().collectList().block().size();
        persona.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, persona.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(persona))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Persona in the database
        List<Persona> personaList = personaRepository.findAll().collectList().block();
        assertThat(personaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPersona() throws Exception {
        int databaseSizeBeforeUpdate = personaRepository.findAll().collectList().block().size();
        persona.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(persona))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Persona in the database
        List<Persona> personaList = personaRepository.findAll().collectList().block();
        assertThat(personaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPersona() throws Exception {
        int databaseSizeBeforeUpdate = personaRepository.findAll().collectList().block().size();
        persona.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(persona))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Persona in the database
        List<Persona> personaList = personaRepository.findAll().collectList().block();
        assertThat(personaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePersona() {
        // Initialize the database
        personaRepository.save(persona).block();

        int databaseSizeBeforeDelete = personaRepository.findAll().collectList().block().size();

        // Delete the persona
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, persona.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Persona> personaList = personaRepository.findAll().collectList().block();
        assertThat(personaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

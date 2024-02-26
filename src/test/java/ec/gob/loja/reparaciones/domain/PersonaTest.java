package ec.gob.loja.reparaciones.domain;

import static ec.gob.loja.reparaciones.domain.CatalogoItemTestSamples.*;
import static ec.gob.loja.reparaciones.domain.PersonaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ec.gob.loja.reparaciones.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PersonaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Persona.class);
        Persona persona1 = getPersonaSample1();
        Persona persona2 = new Persona();
        assertThat(persona1).isNotEqualTo(persona2);

        persona2.setId(persona1.getId());
        assertThat(persona1).isEqualTo(persona2);

        persona2 = getPersonaSample2();
        assertThat(persona1).isNotEqualTo(persona2);
    }

    @Test
    void tipoIdentificacionTest() throws Exception {
        Persona persona = getPersonaRandomSampleGenerator();
        CatalogoItem catalogoItemBack = getCatalogoItemRandomSampleGenerator();

        persona.setTipoIdentificacion(catalogoItemBack);
        assertThat(persona.getTipoIdentificacion()).isEqualTo(catalogoItemBack);

        persona.tipoIdentificacion(null);
        assertThat(persona.getTipoIdentificacion()).isNull();
    }
}

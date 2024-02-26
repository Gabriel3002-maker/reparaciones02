package ec.gob.loja.reparaciones.domain;

import static ec.gob.loja.reparaciones.domain.OrdenBodegaTestSamples.*;
import static ec.gob.loja.reparaciones.domain.PersonaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ec.gob.loja.reparaciones.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrdenBodegaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrdenBodega.class);
        OrdenBodega ordenBodega1 = getOrdenBodegaSample1();
        OrdenBodega ordenBodega2 = new OrdenBodega();
        assertThat(ordenBodega1).isNotEqualTo(ordenBodega2);

        ordenBodega2.setId(ordenBodega1.getId());
        assertThat(ordenBodega1).isEqualTo(ordenBodega2);

        ordenBodega2 = getOrdenBodegaSample2();
        assertThat(ordenBodega1).isNotEqualTo(ordenBodega2);
    }

    @Test
    void receptorTest() throws Exception {
        OrdenBodega ordenBodega = getOrdenBodegaRandomSampleGenerator();
        Persona personaBack = getPersonaRandomSampleGenerator();

        ordenBodega.setReceptor(personaBack);
        assertThat(ordenBodega.getReceptor()).isEqualTo(personaBack);

        ordenBodega.receptor(null);
        assertThat(ordenBodega.getReceptor()).isNull();
    }
}

package ec.gob.loja.reparaciones.domain;

import static ec.gob.loja.reparaciones.domain.DetalleDanioTestSamples.*;
import static ec.gob.loja.reparaciones.domain.RegistroDanioTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ec.gob.loja.reparaciones.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RegistroDanioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RegistroDanio.class);
        RegistroDanio registroDanio1 = getRegistroDanioSample1();
        RegistroDanio registroDanio2 = new RegistroDanio();
        assertThat(registroDanio1).isNotEqualTo(registroDanio2);

        registroDanio2.setId(registroDanio1.getId());
        assertThat(registroDanio1).isEqualTo(registroDanio2);

        registroDanio2 = getRegistroDanioSample2();
        assertThat(registroDanio1).isNotEqualTo(registroDanio2);
    }

    @Test
    void detalleDanioTest() throws Exception {
        RegistroDanio registroDanio = getRegistroDanioRandomSampleGenerator();
        DetalleDanio detalleDanioBack = getDetalleDanioRandomSampleGenerator();

        registroDanio.setDetalleDanio(detalleDanioBack);
        assertThat(registroDanio.getDetalleDanio()).isEqualTo(detalleDanioBack);

        registroDanio.detalleDanio(null);
        assertThat(registroDanio.getDetalleDanio()).isNull();
    }
}

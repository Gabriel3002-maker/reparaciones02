package ec.gob.loja.reparaciones.domain;

import static ec.gob.loja.reparaciones.domain.ParametroSistemaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ec.gob.loja.reparaciones.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ParametroSistemaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ParametroSistema.class);
        ParametroSistema parametroSistema1 = getParametroSistemaSample1();
        ParametroSistema parametroSistema2 = new ParametroSistema();
        assertThat(parametroSistema1).isNotEqualTo(parametroSistema2);

        parametroSistema2.setId(parametroSistema1.getId());
        assertThat(parametroSistema1).isEqualTo(parametroSistema2);

        parametroSistema2 = getParametroSistemaSample2();
        assertThat(parametroSistema1).isNotEqualTo(parametroSistema2);
    }
}

package ec.gob.loja.reparaciones.domain;

import static ec.gob.loja.reparaciones.domain.FuncionalidadTestSamples.*;
import static ec.gob.loja.reparaciones.domain.RolFuncionalidadTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ec.gob.loja.reparaciones.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RolFuncionalidadTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RolFuncionalidad.class);
        RolFuncionalidad rolFuncionalidad1 = getRolFuncionalidadSample1();
        RolFuncionalidad rolFuncionalidad2 = new RolFuncionalidad();
        assertThat(rolFuncionalidad1).isNotEqualTo(rolFuncionalidad2);

        rolFuncionalidad2.setId(rolFuncionalidad1.getId());
        assertThat(rolFuncionalidad1).isEqualTo(rolFuncionalidad2);

        rolFuncionalidad2 = getRolFuncionalidadSample2();
        assertThat(rolFuncionalidad1).isNotEqualTo(rolFuncionalidad2);
    }

    @Test
    void funcionalidadTest() throws Exception {
        RolFuncionalidad rolFuncionalidad = getRolFuncionalidadRandomSampleGenerator();
        Funcionalidad funcionalidadBack = getFuncionalidadRandomSampleGenerator();

        rolFuncionalidad.setFuncionalidad(funcionalidadBack);
        assertThat(rolFuncionalidad.getFuncionalidad()).isEqualTo(funcionalidadBack);

        rolFuncionalidad.funcionalidad(null);
        assertThat(rolFuncionalidad.getFuncionalidad()).isNull();
    }
}

package ec.gob.loja.reparaciones.domain;

import static ec.gob.loja.reparaciones.domain.FuncionalidadTestSamples.*;
import static ec.gob.loja.reparaciones.domain.FuncionalidadTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ec.gob.loja.reparaciones.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class FuncionalidadTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Funcionalidad.class);
        Funcionalidad funcionalidad1 = getFuncionalidadSample1();
        Funcionalidad funcionalidad2 = new Funcionalidad();
        assertThat(funcionalidad1).isNotEqualTo(funcionalidad2);

        funcionalidad2.setId(funcionalidad1.getId());
        assertThat(funcionalidad1).isEqualTo(funcionalidad2);

        funcionalidad2 = getFuncionalidadSample2();
        assertThat(funcionalidad1).isNotEqualTo(funcionalidad2);
    }

    @Test
    void hijosTest() throws Exception {
        Funcionalidad funcionalidad = getFuncionalidadRandomSampleGenerator();
        Funcionalidad funcionalidadBack = getFuncionalidadRandomSampleGenerator();

        funcionalidad.addHijos(funcionalidadBack);
        assertThat(funcionalidad.getHijos()).containsOnly(funcionalidadBack);
        assertThat(funcionalidadBack.getPadre()).isEqualTo(funcionalidad);

        funcionalidad.removeHijos(funcionalidadBack);
        assertThat(funcionalidad.getHijos()).doesNotContain(funcionalidadBack);
        assertThat(funcionalidadBack.getPadre()).isNull();

        funcionalidad.hijos(new HashSet<>(Set.of(funcionalidadBack)));
        assertThat(funcionalidad.getHijos()).containsOnly(funcionalidadBack);
        assertThat(funcionalidadBack.getPadre()).isEqualTo(funcionalidad);

        funcionalidad.setHijos(new HashSet<>());
        assertThat(funcionalidad.getHijos()).doesNotContain(funcionalidadBack);
        assertThat(funcionalidadBack.getPadre()).isNull();
    }

    @Test
    void padreTest() throws Exception {
        Funcionalidad funcionalidad = getFuncionalidadRandomSampleGenerator();
        Funcionalidad funcionalidadBack = getFuncionalidadRandomSampleGenerator();

        funcionalidad.setPadre(funcionalidadBack);
        assertThat(funcionalidad.getPadre()).isEqualTo(funcionalidadBack);

        funcionalidad.padre(null);
        assertThat(funcionalidad.getPadre()).isNull();
    }
}

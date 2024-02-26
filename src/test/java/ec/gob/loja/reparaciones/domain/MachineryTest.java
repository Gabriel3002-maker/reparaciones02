package ec.gob.loja.reparaciones.domain;

import static ec.gob.loja.reparaciones.domain.CatalogoItemTestSamples.*;
import static ec.gob.loja.reparaciones.domain.MachineryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ec.gob.loja.reparaciones.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MachineryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Machinery.class);
        Machinery machinery1 = getMachinerySample1();
        Machinery machinery2 = new Machinery();
        assertThat(machinery1).isNotEqualTo(machinery2);

        machinery2.setId(machinery1.getId());
        assertThat(machinery1).isEqualTo(machinery2);

        machinery2 = getMachinerySample2();
        assertThat(machinery1).isNotEqualTo(machinery2);
    }

    @Test
    void nombreTest() throws Exception {
        Machinery machinery = getMachineryRandomSampleGenerator();
        CatalogoItem catalogoItemBack = getCatalogoItemRandomSampleGenerator();

        machinery.setNombre(catalogoItemBack);
        assertThat(machinery.getNombre()).isEqualTo(catalogoItemBack);

        machinery.nombre(null);
        assertThat(machinery.getNombre()).isNull();
    }
}

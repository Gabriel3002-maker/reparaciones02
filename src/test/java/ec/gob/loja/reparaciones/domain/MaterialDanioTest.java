package ec.gob.loja.reparaciones.domain;

import static ec.gob.loja.reparaciones.domain.MaterialDanioTestSamples.*;
import static ec.gob.loja.reparaciones.domain.MaterialTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ec.gob.loja.reparaciones.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MaterialDanioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MaterialDanio.class);
        MaterialDanio materialDanio1 = getMaterialDanioSample1();
        MaterialDanio materialDanio2 = new MaterialDanio();
        assertThat(materialDanio1).isNotEqualTo(materialDanio2);

        materialDanio2.setId(materialDanio1.getId());
        assertThat(materialDanio1).isEqualTo(materialDanio2);

        materialDanio2 = getMaterialDanioSample2();
        assertThat(materialDanio1).isNotEqualTo(materialDanio2);
    }

    @Test
    void materialesTest() throws Exception {
        MaterialDanio materialDanio = getMaterialDanioRandomSampleGenerator();
        Material materialBack = getMaterialRandomSampleGenerator();

        materialDanio.addMateriales(materialBack);
        assertThat(materialDanio.getMateriales()).containsOnly(materialBack);
        assertThat(materialBack.getMaterialDanio()).isEqualTo(materialDanio);

        materialDanio.removeMateriales(materialBack);
        assertThat(materialDanio.getMateriales()).doesNotContain(materialBack);
        assertThat(materialBack.getMaterialDanio()).isNull();

        materialDanio.materiales(new HashSet<>(Set.of(materialBack)));
        assertThat(materialDanio.getMateriales()).containsOnly(materialBack);
        assertThat(materialBack.getMaterialDanio()).isEqualTo(materialDanio);

        materialDanio.setMateriales(new HashSet<>());
        assertThat(materialDanio.getMateriales()).doesNotContain(materialBack);
        assertThat(materialBack.getMaterialDanio()).isNull();
    }
}

package ec.gob.loja.reparaciones.domain;

import static ec.gob.loja.reparaciones.domain.MaterialReporteControlTestSamples.*;
import static ec.gob.loja.reparaciones.domain.MaterialTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ec.gob.loja.reparaciones.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MaterialReporteControlTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MaterialReporteControl.class);
        MaterialReporteControl materialReporteControl1 = getMaterialReporteControlSample1();
        MaterialReporteControl materialReporteControl2 = new MaterialReporteControl();
        assertThat(materialReporteControl1).isNotEqualTo(materialReporteControl2);

        materialReporteControl2.setId(materialReporteControl1.getId());
        assertThat(materialReporteControl1).isEqualTo(materialReporteControl2);

        materialReporteControl2 = getMaterialReporteControlSample2();
        assertThat(materialReporteControl1).isNotEqualTo(materialReporteControl2);
    }

    @Test
    void materialesTest() throws Exception {
        MaterialReporteControl materialReporteControl = getMaterialReporteControlRandomSampleGenerator();
        Material materialBack = getMaterialRandomSampleGenerator();

        materialReporteControl.addMateriales(materialBack);
        assertThat(materialReporteControl.getMateriales()).containsOnly(materialBack);
        assertThat(materialBack.getMaterialReporteControl()).isEqualTo(materialReporteControl);

        materialReporteControl.removeMateriales(materialBack);
        assertThat(materialReporteControl.getMateriales()).doesNotContain(materialBack);
        assertThat(materialBack.getMaterialReporteControl()).isNull();

        materialReporteControl.materiales(new HashSet<>(Set.of(materialBack)));
        assertThat(materialReporteControl.getMateriales()).containsOnly(materialBack);
        assertThat(materialBack.getMaterialReporteControl()).isEqualTo(materialReporteControl);

        materialReporteControl.setMateriales(new HashSet<>());
        assertThat(materialReporteControl.getMateriales()).doesNotContain(materialBack);
        assertThat(materialBack.getMaterialReporteControl()).isNull();
    }
}

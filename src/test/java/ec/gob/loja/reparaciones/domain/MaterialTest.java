package ec.gob.loja.reparaciones.domain;

import static ec.gob.loja.reparaciones.domain.CatalogoItemTestSamples.*;
import static ec.gob.loja.reparaciones.domain.MaterialDanioTestSamples.*;
import static ec.gob.loja.reparaciones.domain.MaterialReporteControlTestSamples.*;
import static ec.gob.loja.reparaciones.domain.MaterialTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ec.gob.loja.reparaciones.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MaterialTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Material.class);
        Material material1 = getMaterialSample1();
        Material material2 = new Material();
        assertThat(material1).isNotEqualTo(material2);

        material2.setId(material1.getId());
        assertThat(material1).isEqualTo(material2);

        material2 = getMaterialSample2();
        assertThat(material1).isNotEqualTo(material2);
    }

    @Test
    void categoriaTest() throws Exception {
        Material material = getMaterialRandomSampleGenerator();
        CatalogoItem catalogoItemBack = getCatalogoItemRandomSampleGenerator();

        material.setCategoria(catalogoItemBack);
        assertThat(material.getCategoria()).isEqualTo(catalogoItemBack);

        material.categoria(null);
        assertThat(material.getCategoria()).isNull();
    }

    @Test
    void materialDanioTest() throws Exception {
        Material material = getMaterialRandomSampleGenerator();
        MaterialDanio materialDanioBack = getMaterialDanioRandomSampleGenerator();

        material.setMaterialDanio(materialDanioBack);
        assertThat(material.getMaterialDanio()).isEqualTo(materialDanioBack);

        material.materialDanio(null);
        assertThat(material.getMaterialDanio()).isNull();
    }

    @Test
    void materialReporteControlTest() throws Exception {
        Material material = getMaterialRandomSampleGenerator();
        MaterialReporteControl materialReporteControlBack = getMaterialReporteControlRandomSampleGenerator();

        material.setMaterialReporteControl(materialReporteControlBack);
        assertThat(material.getMaterialReporteControl()).isEqualTo(materialReporteControlBack);

        material.materialReporteControl(null);
        assertThat(material.getMaterialReporteControl()).isNull();
    }
}

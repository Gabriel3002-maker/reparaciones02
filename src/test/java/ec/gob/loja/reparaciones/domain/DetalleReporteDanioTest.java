package ec.gob.loja.reparaciones.domain;

import static ec.gob.loja.reparaciones.domain.DetalleReporteDanioTestSamples.*;
import static ec.gob.loja.reparaciones.domain.MaterialReporteControlTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ec.gob.loja.reparaciones.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DetalleReporteDanioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DetalleReporteDanio.class);
        DetalleReporteDanio detalleReporteDanio1 = getDetalleReporteDanioSample1();
        DetalleReporteDanio detalleReporteDanio2 = new DetalleReporteDanio();
        assertThat(detalleReporteDanio1).isNotEqualTo(detalleReporteDanio2);

        detalleReporteDanio2.setId(detalleReporteDanio1.getId());
        assertThat(detalleReporteDanio1).isEqualTo(detalleReporteDanio2);

        detalleReporteDanio2 = getDetalleReporteDanioSample2();
        assertThat(detalleReporteDanio1).isNotEqualTo(detalleReporteDanio2);
    }

    @Test
    void materialReporteTest() throws Exception {
        DetalleReporteDanio detalleReporteDanio = getDetalleReporteDanioRandomSampleGenerator();
        MaterialReporteControl materialReporteControlBack = getMaterialReporteControlRandomSampleGenerator();

        detalleReporteDanio.setMaterialReporte(materialReporteControlBack);
        assertThat(detalleReporteDanio.getMaterialReporte()).isEqualTo(materialReporteControlBack);

        detalleReporteDanio.materialReporte(null);
        assertThat(detalleReporteDanio.getMaterialReporte()).isNull();
    }
}

package ec.gob.loja.reparaciones.domain;

import static ec.gob.loja.reparaciones.domain.CatalogoItemTestSamples.*;
import static ec.gob.loja.reparaciones.domain.DetalleDanioTestSamples.*;
import static ec.gob.loja.reparaciones.domain.DetalleReporteDanioTestSamples.*;
import static ec.gob.loja.reparaciones.domain.MachineryTestSamples.*;
import static ec.gob.loja.reparaciones.domain.MaterialDanioTestSamples.*;
import static ec.gob.loja.reparaciones.domain.OrdenBodegaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ec.gob.loja.reparaciones.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DetalleDanioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DetalleDanio.class);
        DetalleDanio detalleDanio1 = getDetalleDanioSample1();
        DetalleDanio detalleDanio2 = new DetalleDanio();
        assertThat(detalleDanio1).isNotEqualTo(detalleDanio2);

        detalleDanio2.setId(detalleDanio1.getId());
        assertThat(detalleDanio1).isEqualTo(detalleDanio2);

        detalleDanio2 = getDetalleDanioSample2();
        assertThat(detalleDanio1).isNotEqualTo(detalleDanio2);
    }

    @Test
    void tipoDanioTest() throws Exception {
        DetalleDanio detalleDanio = getDetalleDanioRandomSampleGenerator();
        CatalogoItem catalogoItemBack = getCatalogoItemRandomSampleGenerator();

        detalleDanio.setTipoDanio(catalogoItemBack);
        assertThat(detalleDanio.getTipoDanio()).isEqualTo(catalogoItemBack);

        detalleDanio.tipoDanio(null);
        assertThat(detalleDanio.getTipoDanio()).isNull();
    }

    @Test
    void materialDanioTest() throws Exception {
        DetalleDanio detalleDanio = getDetalleDanioRandomSampleGenerator();
        MaterialDanio materialDanioBack = getMaterialDanioRandomSampleGenerator();

        detalleDanio.setMaterialDanio(materialDanioBack);
        assertThat(detalleDanio.getMaterialDanio()).isEqualTo(materialDanioBack);

        detalleDanio.materialDanio(null);
        assertThat(detalleDanio.getMaterialDanio()).isNull();
    }

    @Test
    void maquinariaTest() throws Exception {
        DetalleDanio detalleDanio = getDetalleDanioRandomSampleGenerator();
        Machinery machineryBack = getMachineryRandomSampleGenerator();

        detalleDanio.setMaquinaria(machineryBack);
        assertThat(detalleDanio.getMaquinaria()).isEqualTo(machineryBack);

        detalleDanio.maquinaria(null);
        assertThat(detalleDanio.getMaquinaria()).isNull();
    }

    @Test
    void ordenBodegaTest() throws Exception {
        DetalleDanio detalleDanio = getDetalleDanioRandomSampleGenerator();
        OrdenBodega ordenBodegaBack = getOrdenBodegaRandomSampleGenerator();

        detalleDanio.setOrdenBodega(ordenBodegaBack);
        assertThat(detalleDanio.getOrdenBodega()).isEqualTo(ordenBodegaBack);

        detalleDanio.ordenBodega(null);
        assertThat(detalleDanio.getOrdenBodega()).isNull();
    }

    @Test
    void detalleReporteDanioTest() throws Exception {
        DetalleDanio detalleDanio = getDetalleDanioRandomSampleGenerator();
        DetalleReporteDanio detalleReporteDanioBack = getDetalleReporteDanioRandomSampleGenerator();

        detalleDanio.setDetalleReporteDanio(detalleReporteDanioBack);
        assertThat(detalleDanio.getDetalleReporteDanio()).isEqualTo(detalleReporteDanioBack);

        detalleDanio.detalleReporteDanio(null);
        assertThat(detalleDanio.getDetalleReporteDanio()).isNull();
    }
}

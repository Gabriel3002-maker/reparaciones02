package ec.gob.loja.reparaciones.domain;

import static ec.gob.loja.reparaciones.domain.CatalogoItemTestSamples.*;
import static ec.gob.loja.reparaciones.domain.CatalogoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ec.gob.loja.reparaciones.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CatalogoItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CatalogoItem.class);
        CatalogoItem catalogoItem1 = getCatalogoItemSample1();
        CatalogoItem catalogoItem2 = new CatalogoItem();
        assertThat(catalogoItem1).isNotEqualTo(catalogoItem2);

        catalogoItem2.setId(catalogoItem1.getId());
        assertThat(catalogoItem1).isEqualTo(catalogoItem2);

        catalogoItem2 = getCatalogoItemSample2();
        assertThat(catalogoItem1).isNotEqualTo(catalogoItem2);
    }

    @Test
    void catalogoTest() throws Exception {
        CatalogoItem catalogoItem = getCatalogoItemRandomSampleGenerator();
        Catalogo catalogoBack = getCatalogoRandomSampleGenerator();

        catalogoItem.setCatalogo(catalogoBack);
        assertThat(catalogoItem.getCatalogo()).isEqualTo(catalogoBack);

        catalogoItem.catalogo(null);
        assertThat(catalogoItem.getCatalogo()).isNull();
    }
}

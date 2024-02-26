package ec.gob.loja.reparaciones.domain;

import static ec.gob.loja.reparaciones.domain.CatalogoItemTestSamples.*;
import static ec.gob.loja.reparaciones.domain.CatalogoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ec.gob.loja.reparaciones.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CatalogoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Catalogo.class);
        Catalogo catalogo1 = getCatalogoSample1();
        Catalogo catalogo2 = new Catalogo();
        assertThat(catalogo1).isNotEqualTo(catalogo2);

        catalogo2.setId(catalogo1.getId());
        assertThat(catalogo1).isEqualTo(catalogo2);

        catalogo2 = getCatalogoSample2();
        assertThat(catalogo1).isNotEqualTo(catalogo2);
    }

    @Test
    void itemsTest() throws Exception {
        Catalogo catalogo = getCatalogoRandomSampleGenerator();
        CatalogoItem catalogoItemBack = getCatalogoItemRandomSampleGenerator();

        catalogo.addItems(catalogoItemBack);
        assertThat(catalogo.getItems()).containsOnly(catalogoItemBack);
        assertThat(catalogoItemBack.getCatalogo()).isEqualTo(catalogo);

        catalogo.removeItems(catalogoItemBack);
        assertThat(catalogo.getItems()).doesNotContain(catalogoItemBack);
        assertThat(catalogoItemBack.getCatalogo()).isNull();

        catalogo.items(new HashSet<>(Set.of(catalogoItemBack)));
        assertThat(catalogo.getItems()).containsOnly(catalogoItemBack);
        assertThat(catalogoItemBack.getCatalogo()).isEqualTo(catalogo);

        catalogo.setItems(new HashSet<>());
        assertThat(catalogo.getItems()).doesNotContain(catalogoItemBack);
        assertThat(catalogoItemBack.getCatalogo()).isNull();
    }
}

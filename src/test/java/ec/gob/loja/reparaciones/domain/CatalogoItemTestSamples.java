package ec.gob.loja.reparaciones.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CatalogoItemTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CatalogoItem getCatalogoItemSample1() {
        return new CatalogoItem().id(1L).nombre("nombre1").codigo("codigo1").descripcion("descripcion1").catalogoCodigo("catalogoCodigo1");
    }

    public static CatalogoItem getCatalogoItemSample2() {
        return new CatalogoItem().id(2L).nombre("nombre2").codigo("codigo2").descripcion("descripcion2").catalogoCodigo("catalogoCodigo2");
    }

    public static CatalogoItem getCatalogoItemRandomSampleGenerator() {
        return new CatalogoItem()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .codigo(UUID.randomUUID().toString())
            .descripcion(UUID.randomUUID().toString())
            .catalogoCodigo(UUID.randomUUID().toString());
    }
}

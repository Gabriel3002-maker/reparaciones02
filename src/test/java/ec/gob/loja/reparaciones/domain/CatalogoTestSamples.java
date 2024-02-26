package ec.gob.loja.reparaciones.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CatalogoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Catalogo getCatalogoSample1() {
        return new Catalogo().id(1L).nombre("nombre1").codigo("codigo1").descripcion("descripcion1");
    }

    public static Catalogo getCatalogoSample2() {
        return new Catalogo().id(2L).nombre("nombre2").codigo("codigo2").descripcion("descripcion2");
    }

    public static Catalogo getCatalogoRandomSampleGenerator() {
        return new Catalogo()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .codigo(UUID.randomUUID().toString())
            .descripcion(UUID.randomUUID().toString());
    }
}

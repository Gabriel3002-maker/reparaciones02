package ec.gob.loja.reparaciones.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MaterialTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Material getMaterialSample1() {
        return new Material()
            .id(1L)
            .codigo("codigo1")
            .nombre("nombre1")
            .stock(1)
            .descripcion("descripcion1")
            .creadoPor("creadoPor1")
            .actualizadoPor("actualizadoPor1");
    }

    public static Material getMaterialSample2() {
        return new Material()
            .id(2L)
            .codigo("codigo2")
            .nombre("nombre2")
            .stock(2)
            .descripcion("descripcion2")
            .creadoPor("creadoPor2")
            .actualizadoPor("actualizadoPor2");
    }

    public static Material getMaterialRandomSampleGenerator() {
        return new Material()
            .id(longCount.incrementAndGet())
            .codigo(UUID.randomUUID().toString())
            .nombre(UUID.randomUUID().toString())
            .stock(intCount.incrementAndGet())
            .descripcion(UUID.randomUUID().toString())
            .creadoPor(UUID.randomUUID().toString())
            .actualizadoPor(UUID.randomUUID().toString());
    }
}

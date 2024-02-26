package ec.gob.loja.reparaciones.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FuncionalidadTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Funcionalidad getFuncionalidadSample1() {
        return new Funcionalidad().id(1L).nombre("nombre1").descripcion("descripcion1").url("url1").icono("icono1");
    }

    public static Funcionalidad getFuncionalidadSample2() {
        return new Funcionalidad().id(2L).nombre("nombre2").descripcion("descripcion2").url("url2").icono("icono2");
    }

    public static Funcionalidad getFuncionalidadRandomSampleGenerator() {
        return new Funcionalidad()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .descripcion(UUID.randomUUID().toString())
            .url(UUID.randomUUID().toString())
            .icono(UUID.randomUUID().toString());
    }
}

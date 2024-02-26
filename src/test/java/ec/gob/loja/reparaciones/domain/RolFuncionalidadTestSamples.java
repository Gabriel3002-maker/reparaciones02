package ec.gob.loja.reparaciones.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class RolFuncionalidadTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static RolFuncionalidad getRolFuncionalidadSample1() {
        return new RolFuncionalidad().id(1L).rol("rol1").prioridad(1);
    }

    public static RolFuncionalidad getRolFuncionalidadSample2() {
        return new RolFuncionalidad().id(2L).rol("rol2").prioridad(2);
    }

    public static RolFuncionalidad getRolFuncionalidadRandomSampleGenerator() {
        return new RolFuncionalidad()
            .id(longCount.incrementAndGet())
            .rol(UUID.randomUUID().toString())
            .prioridad(intCount.incrementAndGet());
    }
}

package ec.gob.loja.reparaciones.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MaterialReporteControlTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static MaterialReporteControl getMaterialReporteControlSample1() {
        return new MaterialReporteControl().id(1L).codigo("codigo1").cantidadUsada(1).observacion("observacion1");
    }

    public static MaterialReporteControl getMaterialReporteControlSample2() {
        return new MaterialReporteControl().id(2L).codigo("codigo2").cantidadUsada(2).observacion("observacion2");
    }

    public static MaterialReporteControl getMaterialReporteControlRandomSampleGenerator() {
        return new MaterialReporteControl()
            .id(longCount.incrementAndGet())
            .codigo(UUID.randomUUID().toString())
            .cantidadUsada(intCount.incrementAndGet())
            .observacion(UUID.randomUUID().toString());
    }
}

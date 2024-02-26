package ec.gob.loja.reparaciones.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MaterialDanioTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static MaterialDanio getMaterialDanioSample1() {
        return new MaterialDanio().id(1L).codigo("codigo1").cantidadPedida(1).observacion("observacion1");
    }

    public static MaterialDanio getMaterialDanioSample2() {
        return new MaterialDanio().id(2L).codigo("codigo2").cantidadPedida(2).observacion("observacion2");
    }

    public static MaterialDanio getMaterialDanioRandomSampleGenerator() {
        return new MaterialDanio()
            .id(longCount.incrementAndGet())
            .codigo(UUID.randomUUID().toString())
            .cantidadPedida(intCount.incrementAndGet())
            .observacion(UUID.randomUUID().toString());
    }
}

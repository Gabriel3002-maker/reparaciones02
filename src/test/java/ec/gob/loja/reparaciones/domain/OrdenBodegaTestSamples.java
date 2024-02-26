package ec.gob.loja.reparaciones.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class OrdenBodegaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static OrdenBodega getOrdenBodegaSample1() {
        return new OrdenBodega().id(1L).codigo("codigo1").detalleNecesidad("detalleNecesidad1");
    }

    public static OrdenBodega getOrdenBodegaSample2() {
        return new OrdenBodega().id(2L).codigo("codigo2").detalleNecesidad("detalleNecesidad2");
    }

    public static OrdenBodega getOrdenBodegaRandomSampleGenerator() {
        return new OrdenBodega()
            .id(longCount.incrementAndGet())
            .codigo(UUID.randomUUID().toString())
            .detalleNecesidad(UUID.randomUUID().toString());
    }
}

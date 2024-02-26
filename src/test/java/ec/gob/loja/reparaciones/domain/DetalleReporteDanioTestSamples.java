package ec.gob.loja.reparaciones.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DetalleReporteDanioTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static DetalleReporteDanio getDetalleReporteDanioSample1() {
        return new DetalleReporteDanio()
            .id(1L)
            .codigo(1)
            .contribuyente("contribuyente1")
            .direccion("direccion1")
            .referencia("referencia1")
            .horasTrabajadas(1)
            .personalResponsable("personalResponsable1");
    }

    public static DetalleReporteDanio getDetalleReporteDanioSample2() {
        return new DetalleReporteDanio()
            .id(2L)
            .codigo(2)
            .contribuyente("contribuyente2")
            .direccion("direccion2")
            .referencia("referencia2")
            .horasTrabajadas(2)
            .personalResponsable("personalResponsable2");
    }

    public static DetalleReporteDanio getDetalleReporteDanioRandomSampleGenerator() {
        return new DetalleReporteDanio()
            .id(longCount.incrementAndGet())
            .codigo(intCount.incrementAndGet())
            .contribuyente(UUID.randomUUID().toString())
            .direccion(UUID.randomUUID().toString())
            .referencia(UUID.randomUUID().toString())
            .horasTrabajadas(intCount.incrementAndGet())
            .personalResponsable(UUID.randomUUID().toString());
    }
}

package ec.gob.loja.reparaciones.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DetalleDanioTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static DetalleDanio getDetalleDanioSample1() {
        return new DetalleDanio()
            .id(1L)
            .codigo("codigo1")
            .descripcionDanio("descripcionDanio1")
            .tecnico("tecnico1")
            .namePerson("namePerson1")
            .direccion("direccion1")
            .estadoReparacion("estadoReparacion1")
            .observacion("observacion1");
    }

    public static DetalleDanio getDetalleDanioSample2() {
        return new DetalleDanio()
            .id(2L)
            .codigo("codigo2")
            .descripcionDanio("descripcionDanio2")
            .tecnico("tecnico2")
            .namePerson("namePerson2")
            .direccion("direccion2")
            .estadoReparacion("estadoReparacion2")
            .observacion("observacion2");
    }

    public static DetalleDanio getDetalleDanioRandomSampleGenerator() {
        return new DetalleDanio()
            .id(longCount.incrementAndGet())
            .codigo(UUID.randomUUID().toString())
            .descripcionDanio(UUID.randomUUID().toString())
            .tecnico(UUID.randomUUID().toString())
            .namePerson(UUID.randomUUID().toString())
            .direccion(UUID.randomUUID().toString())
            .estadoReparacion(UUID.randomUUID().toString())
            .observacion(UUID.randomUUID().toString());
    }
}

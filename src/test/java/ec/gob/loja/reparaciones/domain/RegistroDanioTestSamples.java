package ec.gob.loja.reparaciones.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RegistroDanioTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static RegistroDanio getRegistroDanioSample1() {
        return new RegistroDanio().id(1L).codigo("codigo1").direccion("direccion1").parroquia("parroquia1").barrio("barrio1");
    }

    public static RegistroDanio getRegistroDanioSample2() {
        return new RegistroDanio().id(2L).codigo("codigo2").direccion("direccion2").parroquia("parroquia2").barrio("barrio2");
    }

    public static RegistroDanio getRegistroDanioRandomSampleGenerator() {
        return new RegistroDanio()
            .id(longCount.incrementAndGet())
            .codigo(UUID.randomUUID().toString())
            .direccion(UUID.randomUUID().toString())
            .parroquia(UUID.randomUUID().toString())
            .barrio(UUID.randomUUID().toString());
    }
}

package ec.gob.loja.reparaciones.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ParametroSistemaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ParametroSistema getParametroSistemaSample1() {
        return new ParametroSistema().id(1L).nombre("nombre1").codigo("codigo1").clase("clase1").valor("valor1");
    }

    public static ParametroSistema getParametroSistemaSample2() {
        return new ParametroSistema().id(2L).nombre("nombre2").codigo("codigo2").clase("clase2").valor("valor2");
    }

    public static ParametroSistema getParametroSistemaRandomSampleGenerator() {
        return new ParametroSistema()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .codigo(UUID.randomUUID().toString())
            .clase(UUID.randomUUID().toString())
            .valor(UUID.randomUUID().toString());
    }
}

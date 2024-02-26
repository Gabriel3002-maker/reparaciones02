package ec.gob.loja.reparaciones.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PersonaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Persona getPersonaSample1() {
        return new Persona()
            .id(1L)
            .identificacion("identificacion1")
            .primerApellido("primerApellido1")
            .segundoApellido("segundoApellido1")
            .primerNombre("primerNombre1")
            .segundoNombre("segundoNombre1")
            .celular("celular1")
            .telefonoConvencional("telefonoConvencional1")
            .correo("correo1");
    }

    public static Persona getPersonaSample2() {
        return new Persona()
            .id(2L)
            .identificacion("identificacion2")
            .primerApellido("primerApellido2")
            .segundoApellido("segundoApellido2")
            .primerNombre("primerNombre2")
            .segundoNombre("segundoNombre2")
            .celular("celular2")
            .telefonoConvencional("telefonoConvencional2")
            .correo("correo2");
    }

    public static Persona getPersonaRandomSampleGenerator() {
        return new Persona()
            .id(longCount.incrementAndGet())
            .identificacion(UUID.randomUUID().toString())
            .primerApellido(UUID.randomUUID().toString())
            .segundoApellido(UUID.randomUUID().toString())
            .primerNombre(UUID.randomUUID().toString())
            .segundoNombre(UUID.randomUUID().toString())
            .celular(UUID.randomUUID().toString())
            .telefonoConvencional(UUID.randomUUID().toString())
            .correo(UUID.randomUUID().toString());
    }
}

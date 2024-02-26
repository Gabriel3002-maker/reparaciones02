package ec.gob.loja.reparaciones.repository.rowmapper;

import ec.gob.loja.reparaciones.domain.Persona;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Persona}, with proper type conversions.
 */
@Service
public class PersonaRowMapper implements BiFunction<Row, String, Persona> {

    private final ColumnConverter converter;

    public PersonaRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Persona} stored in the database.
     */
    @Override
    public Persona apply(Row row, String prefix) {
        Persona entity = new Persona();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setIdentificacion(converter.fromRow(row, prefix + "_identificacion", String.class));
        entity.setPrimerApellido(converter.fromRow(row, prefix + "_primer_apellido", String.class));
        entity.setSegundoApellido(converter.fromRow(row, prefix + "_segundo_apellido", String.class));
        entity.setPrimerNombre(converter.fromRow(row, prefix + "_primer_nombre", String.class));
        entity.setSegundoNombre(converter.fromRow(row, prefix + "_segundo_nombre", String.class));
        entity.setCelular(converter.fromRow(row, prefix + "_celular", String.class));
        entity.setTelefonoConvencional(converter.fromRow(row, prefix + "_telefono_convencional", String.class));
        entity.setCorreo(converter.fromRow(row, prefix + "_correo", String.class));
        entity.setTipoIdentificacionId(converter.fromRow(row, prefix + "_tipo_identificacion_id", Long.class));
        entity.setUsuarioId(converter.fromRow(row, prefix + "_usuario_id", Long.class));
        return entity;
    }
}

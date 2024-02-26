package ec.gob.loja.reparaciones.repository.rowmapper;

import ec.gob.loja.reparaciones.domain.RegistroDanio;
import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link RegistroDanio}, with proper type conversions.
 */
@Service
public class RegistroDanioRowMapper implements BiFunction<Row, String, RegistroDanio> {

    private final ColumnConverter converter;

    public RegistroDanioRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link RegistroDanio} stored in the database.
     */
    @Override
    public RegistroDanio apply(Row row, String prefix) {
        RegistroDanio entity = new RegistroDanio();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setCodigo(converter.fromRow(row, prefix + "_codigo", String.class));
        entity.setFecha(converter.fromRow(row, prefix + "_fecha", LocalDate.class));
        entity.setFechaInicio(converter.fromRow(row, prefix + "_fecha_inicio", LocalDate.class));
        entity.setFechaFin(converter.fromRow(row, prefix + "_fecha_fin", LocalDate.class));
        entity.setDireccion(converter.fromRow(row, prefix + "_direccion", String.class));
        entity.setParroquia(converter.fromRow(row, prefix + "_parroquia", String.class));
        entity.setBarrio(converter.fromRow(row, prefix + "_barrio", String.class));
        entity.setDetalleDanioId(converter.fromRow(row, prefix + "_detalle_danio_id", Long.class));
        return entity;
    }
}

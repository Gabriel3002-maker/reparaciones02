package ec.gob.loja.reparaciones.repository.rowmapper;

import ec.gob.loja.reparaciones.domain.Machinery;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Machinery}, with proper type conversions.
 */
@Service
public class MachineryRowMapper implements BiFunction<Row, String, Machinery> {

    private final ColumnConverter converter;

    public MachineryRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Machinery} stored in the database.
     */
    @Override
    public Machinery apply(Row row, String prefix) {
        Machinery entity = new Machinery();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setCodigo(converter.fromRow(row, prefix + "_codigo", String.class));
        entity.setDescripcion(converter.fromRow(row, prefix + "_descripcion", String.class));
        entity.setHorasTrabajadas(converter.fromRow(row, prefix + "_horas_trabajadas", Double.class));
        entity.setNombreId(converter.fromRow(row, prefix + "_nombre_id", Long.class));
        return entity;
    }
}

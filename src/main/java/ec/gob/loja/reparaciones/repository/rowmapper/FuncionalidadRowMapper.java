package ec.gob.loja.reparaciones.repository.rowmapper;

import ec.gob.loja.reparaciones.domain.Funcionalidad;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Funcionalidad}, with proper type conversions.
 */
@Service
public class FuncionalidadRowMapper implements BiFunction<Row, String, Funcionalidad> {

    private final ColumnConverter converter;

    public FuncionalidadRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Funcionalidad} stored in the database.
     */
    @Override
    public Funcionalidad apply(Row row, String prefix) {
        Funcionalidad entity = new Funcionalidad();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNombre(converter.fromRow(row, prefix + "_nombre", String.class));
        entity.setDescripcion(converter.fromRow(row, prefix + "_descripcion", String.class));
        entity.setUrl(converter.fromRow(row, prefix + "_url", String.class));
        entity.setActivo(converter.fromRow(row, prefix + "_activo", Boolean.class));
        entity.setIcono(converter.fromRow(row, prefix + "_icono", String.class));
        entity.setVisible(converter.fromRow(row, prefix + "_visible", Boolean.class));
        entity.setPadreId(converter.fromRow(row, prefix + "_padre_id", Long.class));
        return entity;
    }
}

package ec.gob.loja.reparaciones.repository.rowmapper;

import ec.gob.loja.reparaciones.domain.RolFuncionalidad;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link RolFuncionalidad}, with proper type conversions.
 */
@Service
public class RolFuncionalidadRowMapper implements BiFunction<Row, String, RolFuncionalidad> {

    private final ColumnConverter converter;

    public RolFuncionalidadRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link RolFuncionalidad} stored in the database.
     */
    @Override
    public RolFuncionalidad apply(Row row, String prefix) {
        RolFuncionalidad entity = new RolFuncionalidad();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setRol(converter.fromRow(row, prefix + "_rol", String.class));
        entity.setActivo(converter.fromRow(row, prefix + "_activo", Boolean.class));
        entity.setPrioridad(converter.fromRow(row, prefix + "_prioridad", Integer.class));
        entity.setFuncionalidadId(converter.fromRow(row, prefix + "_funcionalidad_id", Long.class));
        return entity;
    }
}

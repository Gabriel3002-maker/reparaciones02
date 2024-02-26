package ec.gob.loja.reparaciones.repository.rowmapper;

import ec.gob.loja.reparaciones.domain.MaterialReporteControl;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link MaterialReporteControl}, with proper type conversions.
 */
@Service
public class MaterialReporteControlRowMapper implements BiFunction<Row, String, MaterialReporteControl> {

    private final ColumnConverter converter;

    public MaterialReporteControlRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link MaterialReporteControl} stored in the database.
     */
    @Override
    public MaterialReporteControl apply(Row row, String prefix) {
        MaterialReporteControl entity = new MaterialReporteControl();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setCodigo(converter.fromRow(row, prefix + "_codigo", String.class));
        entity.setCantidadUsada(converter.fromRow(row, prefix + "_cantidad_usada", Integer.class));
        entity.setObservacion(converter.fromRow(row, prefix + "_observacion", String.class));
        return entity;
    }
}

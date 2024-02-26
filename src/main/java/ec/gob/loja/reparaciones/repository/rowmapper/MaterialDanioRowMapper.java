package ec.gob.loja.reparaciones.repository.rowmapper;

import ec.gob.loja.reparaciones.domain.MaterialDanio;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link MaterialDanio}, with proper type conversions.
 */
@Service
public class MaterialDanioRowMapper implements BiFunction<Row, String, MaterialDanio> {

    private final ColumnConverter converter;

    public MaterialDanioRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link MaterialDanio} stored in the database.
     */
    @Override
    public MaterialDanio apply(Row row, String prefix) {
        MaterialDanio entity = new MaterialDanio();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setCodigo(converter.fromRow(row, prefix + "_codigo", String.class));
        entity.setCantidadPedida(converter.fromRow(row, prefix + "_cantidad_pedida", Integer.class));
        entity.setObservacion(converter.fromRow(row, prefix + "_observacion", String.class));
        return entity;
    }
}

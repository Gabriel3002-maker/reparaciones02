package ec.gob.loja.reparaciones.repository.rowmapper;

import ec.gob.loja.reparaciones.domain.OrdenBodega;
import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link OrdenBodega}, with proper type conversions.
 */
@Service
public class OrdenBodegaRowMapper implements BiFunction<Row, String, OrdenBodega> {

    private final ColumnConverter converter;

    public OrdenBodegaRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link OrdenBodega} stored in the database.
     */
    @Override
    public OrdenBodega apply(Row row, String prefix) {
        OrdenBodega entity = new OrdenBodega();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setCodigo(converter.fromRow(row, prefix + "_codigo", String.class));
        entity.setDetalleNecesidad(converter.fromRow(row, prefix + "_detalle_necesidad", String.class));
        entity.setFecha(converter.fromRow(row, prefix + "_fecha", LocalDate.class));
        entity.setReceptorId(converter.fromRow(row, prefix + "_receptor_id", Long.class));
        return entity;
    }
}

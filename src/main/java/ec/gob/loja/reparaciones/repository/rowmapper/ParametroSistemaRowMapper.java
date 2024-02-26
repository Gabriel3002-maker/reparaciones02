package ec.gob.loja.reparaciones.repository.rowmapper;

import ec.gob.loja.reparaciones.domain.ParametroSistema;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ParametroSistema}, with proper type conversions.
 */
@Service
public class ParametroSistemaRowMapper implements BiFunction<Row, String, ParametroSistema> {

    private final ColumnConverter converter;

    public ParametroSistemaRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ParametroSistema} stored in the database.
     */
    @Override
    public ParametroSistema apply(Row row, String prefix) {
        ParametroSistema entity = new ParametroSistema();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNombre(converter.fromRow(row, prefix + "_nombre", String.class));
        entity.setCodigo(converter.fromRow(row, prefix + "_codigo", String.class));
        entity.setClase(converter.fromRow(row, prefix + "_clase", String.class));
        entity.setValor(converter.fromRow(row, prefix + "_valor", String.class));
        return entity;
    }
}

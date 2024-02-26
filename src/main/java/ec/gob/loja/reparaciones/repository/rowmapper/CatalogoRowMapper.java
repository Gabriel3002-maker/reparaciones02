package ec.gob.loja.reparaciones.repository.rowmapper;

import ec.gob.loja.reparaciones.domain.Catalogo;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Catalogo}, with proper type conversions.
 */
@Service
public class CatalogoRowMapper implements BiFunction<Row, String, Catalogo> {

    private final ColumnConverter converter;

    public CatalogoRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Catalogo} stored in the database.
     */
    @Override
    public Catalogo apply(Row row, String prefix) {
        Catalogo entity = new Catalogo();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNombre(converter.fromRow(row, prefix + "_nombre", String.class));
        entity.setCodigo(converter.fromRow(row, prefix + "_codigo", String.class));
        entity.setDescripcion(converter.fromRow(row, prefix + "_descripcion", String.class));
        return entity;
    }
}

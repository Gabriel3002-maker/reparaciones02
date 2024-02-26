package ec.gob.loja.reparaciones.repository.rowmapper;

import ec.gob.loja.reparaciones.domain.CatalogoItem;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link CatalogoItem}, with proper type conversions.
 */
@Service
public class CatalogoItemRowMapper implements BiFunction<Row, String, CatalogoItem> {

    private final ColumnConverter converter;

    public CatalogoItemRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link CatalogoItem} stored in the database.
     */
    @Override
    public CatalogoItem apply(Row row, String prefix) {
        CatalogoItem entity = new CatalogoItem();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNombre(converter.fromRow(row, prefix + "_nombre", String.class));
        entity.setCodigo(converter.fromRow(row, prefix + "_codigo", String.class));
        entity.setDescripcion(converter.fromRow(row, prefix + "_descripcion", String.class));
        entity.setCatalogoCodigo(converter.fromRow(row, prefix + "_catalogo_codigo", String.class));
        entity.setActivo(converter.fromRow(row, prefix + "_activo", Boolean.class));
        entity.setCatalogoId(converter.fromRow(row, prefix + "_catalogo_id", Long.class));
        return entity;
    }
}

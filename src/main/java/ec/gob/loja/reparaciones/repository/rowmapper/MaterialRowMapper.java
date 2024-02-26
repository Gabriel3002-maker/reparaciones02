package ec.gob.loja.reparaciones.repository.rowmapper;

import ec.gob.loja.reparaciones.domain.Material;
import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Material}, with proper type conversions.
 */
@Service
public class MaterialRowMapper implements BiFunction<Row, String, Material> {

    private final ColumnConverter converter;

    public MaterialRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Material} stored in the database.
     */
    @Override
    public Material apply(Row row, String prefix) {
        Material entity = new Material();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setCodigo(converter.fromRow(row, prefix + "_codigo", String.class));
        entity.setNombre(converter.fromRow(row, prefix + "_nombre", String.class));
        entity.setValorUnitario(converter.fromRow(row, prefix + "_valor_unitario", Double.class));
        entity.setStock(converter.fromRow(row, prefix + "_stock", Integer.class));
        entity.setActivo(converter.fromRow(row, prefix + "_activo", Boolean.class));
        entity.setDescripcion(converter.fromRow(row, prefix + "_descripcion", String.class));
        entity.setCreadoPor(converter.fromRow(row, prefix + "_creado_por", String.class));
        entity.setFechaCreacion(converter.fromRow(row, prefix + "_fecha_creacion", LocalDate.class));
        entity.setActualizadoPor(converter.fromRow(row, prefix + "_actualizado_por", String.class));
        entity.setFechaModificacion(converter.fromRow(row, prefix + "_fecha_modificacion", LocalDate.class));
        entity.setCategoriaId(converter.fromRow(row, prefix + "_categoria_id", Long.class));
        entity.setMaterialDanioId(converter.fromRow(row, prefix + "_material_danio_id", Long.class));
        entity.setMaterialReporteControlId(converter.fromRow(row, prefix + "_material_reporte_control_id", Long.class));
        return entity;
    }
}

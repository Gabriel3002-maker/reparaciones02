package ec.gob.loja.reparaciones.repository.rowmapper;

import ec.gob.loja.reparaciones.domain.DetalleDanio;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link DetalleDanio}, with proper type conversions.
 */
@Service
public class DetalleDanioRowMapper implements BiFunction<Row, String, DetalleDanio> {

    private final ColumnConverter converter;

    public DetalleDanioRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link DetalleDanio} stored in the database.
     */
    @Override
    public DetalleDanio apply(Row row, String prefix) {
        DetalleDanio entity = new DetalleDanio();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setCodigo(converter.fromRow(row, prefix + "_codigo", String.class));
        entity.setDescripcionDanio(converter.fromRow(row, prefix + "_descripcion_danio", String.class));
        entity.setTecnico(converter.fromRow(row, prefix + "_tecnico", String.class));
        entity.setNamePerson(converter.fromRow(row, prefix + "_name_person", String.class));
        entity.setDireccion(converter.fromRow(row, prefix + "_direccion", String.class));
        entity.setEstadoReparacion(converter.fromRow(row, prefix + "_estado_reparacion", String.class));
        entity.setObservacion(converter.fromRow(row, prefix + "_observacion", String.class));
        entity.setTipoDanioId(converter.fromRow(row, prefix + "_tipo_danio_id", Long.class));
        entity.setMaterialDanioId(converter.fromRow(row, prefix + "_material_danio_id", Long.class));
        entity.setMaquinariaId(converter.fromRow(row, prefix + "_maquinaria_id", Long.class));
        entity.setOrdenBodegaId(converter.fromRow(row, prefix + "_orden_bodega_id", Long.class));
        entity.setDetalleReporteDanioId(converter.fromRow(row, prefix + "_detalle_reporte_danio_id", Long.class));
        return entity;
    }
}

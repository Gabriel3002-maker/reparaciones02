package ec.gob.loja.reparaciones.repository.rowmapper;

import ec.gob.loja.reparaciones.domain.DetalleReporteDanio;
import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link DetalleReporteDanio}, with proper type conversions.
 */
@Service
public class DetalleReporteDanioRowMapper implements BiFunction<Row, String, DetalleReporteDanio> {

    private final ColumnConverter converter;

    public DetalleReporteDanioRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link DetalleReporteDanio} stored in the database.
     */
    @Override
    public DetalleReporteDanio apply(Row row, String prefix) {
        DetalleReporteDanio entity = new DetalleReporteDanio();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setCodigo(converter.fromRow(row, prefix + "_codigo", Integer.class));
        entity.setFecha(converter.fromRow(row, prefix + "_fecha", LocalDate.class));
        entity.setContribuyente(converter.fromRow(row, prefix + "_contribuyente", String.class));
        entity.setDireccion(converter.fromRow(row, prefix + "_direccion", String.class));
        entity.setReferencia(converter.fromRow(row, prefix + "_referencia", String.class));
        entity.setHorasTrabajadas(converter.fromRow(row, prefix + "_horas_trabajadas", Integer.class));
        entity.setPersonalResponsable(converter.fromRow(row, prefix + "_personal_responsable", String.class));
        entity.setMaterialReporteId(converter.fromRow(row, prefix + "_material_reporte_id", Long.class));
        return entity;
    }
}

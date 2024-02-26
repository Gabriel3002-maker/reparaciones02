package ec.gob.loja.reparaciones.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class DetalleReporteDanioSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("codigo", table, columnPrefix + "_codigo"));
        columns.add(Column.aliased("fecha", table, columnPrefix + "_fecha"));
        columns.add(Column.aliased("contribuyente", table, columnPrefix + "_contribuyente"));
        columns.add(Column.aliased("direccion", table, columnPrefix + "_direccion"));
        columns.add(Column.aliased("referencia", table, columnPrefix + "_referencia"));
        columns.add(Column.aliased("horas_trabajadas", table, columnPrefix + "_horas_trabajadas"));
        columns.add(Column.aliased("personal_responsable", table, columnPrefix + "_personal_responsable"));

        columns.add(Column.aliased("material_reporte_id", table, columnPrefix + "_material_reporte_id"));
        return columns;
    }
}

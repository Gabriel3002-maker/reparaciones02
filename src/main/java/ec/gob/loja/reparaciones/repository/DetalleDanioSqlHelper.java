package ec.gob.loja.reparaciones.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class DetalleDanioSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("codigo", table, columnPrefix + "_codigo"));
        columns.add(Column.aliased("descripcion_danio", table, columnPrefix + "_descripcion_danio"));
        columns.add(Column.aliased("tecnico", table, columnPrefix + "_tecnico"));
        columns.add(Column.aliased("name_person", table, columnPrefix + "_name_person"));
        columns.add(Column.aliased("direccion", table, columnPrefix + "_direccion"));
        columns.add(Column.aliased("estado_reparacion", table, columnPrefix + "_estado_reparacion"));
        columns.add(Column.aliased("observacion", table, columnPrefix + "_observacion"));

        columns.add(Column.aliased("tipo_danio_id", table, columnPrefix + "_tipo_danio_id"));
        columns.add(Column.aliased("material_danio_id", table, columnPrefix + "_material_danio_id"));
        columns.add(Column.aliased("maquinaria_id", table, columnPrefix + "_maquinaria_id"));
        columns.add(Column.aliased("orden_bodega_id", table, columnPrefix + "_orden_bodega_id"));
        columns.add(Column.aliased("detalle_reporte_danio_id", table, columnPrefix + "_detalle_reporte_danio_id"));
        return columns;
    }
}

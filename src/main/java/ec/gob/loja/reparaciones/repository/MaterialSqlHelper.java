package ec.gob.loja.reparaciones.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class MaterialSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("codigo", table, columnPrefix + "_codigo"));
        columns.add(Column.aliased("nombre", table, columnPrefix + "_nombre"));
        columns.add(Column.aliased("valor_unitario", table, columnPrefix + "_valor_unitario"));
        columns.add(Column.aliased("stock", table, columnPrefix + "_stock"));
        columns.add(Column.aliased("activo", table, columnPrefix + "_activo"));
        columns.add(Column.aliased("descripcion", table, columnPrefix + "_descripcion"));
        columns.add(Column.aliased("creado_por", table, columnPrefix + "_creado_por"));
        columns.add(Column.aliased("fecha_creacion", table, columnPrefix + "_fecha_creacion"));
        columns.add(Column.aliased("actualizado_por", table, columnPrefix + "_actualizado_por"));
        columns.add(Column.aliased("fecha_modificacion", table, columnPrefix + "_fecha_modificacion"));

        columns.add(Column.aliased("categoria_id", table, columnPrefix + "_categoria_id"));
        columns.add(Column.aliased("material_danio_id", table, columnPrefix + "_material_danio_id"));
        columns.add(Column.aliased("material_reporte_control_id", table, columnPrefix + "_material_reporte_control_id"));
        return columns;
    }
}

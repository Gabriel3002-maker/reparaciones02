package ec.gob.loja.reparaciones.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class CatalogoItemSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("nombre", table, columnPrefix + "_nombre"));
        columns.add(Column.aliased("codigo", table, columnPrefix + "_codigo"));
        columns.add(Column.aliased("descripcion", table, columnPrefix + "_descripcion"));
        columns.add(Column.aliased("catalogo_codigo", table, columnPrefix + "_catalogo_codigo"));
        columns.add(Column.aliased("activo", table, columnPrefix + "_activo"));

        columns.add(Column.aliased("catalogo_id", table, columnPrefix + "_catalogo_id"));
        return columns;
    }
}

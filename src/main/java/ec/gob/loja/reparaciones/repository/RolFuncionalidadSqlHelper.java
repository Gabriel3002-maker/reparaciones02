package ec.gob.loja.reparaciones.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class RolFuncionalidadSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("rol", table, columnPrefix + "_rol"));
        columns.add(Column.aliased("activo", table, columnPrefix + "_activo"));
        columns.add(Column.aliased("prioridad", table, columnPrefix + "_prioridad"));

        columns.add(Column.aliased("funcionalidad_id", table, columnPrefix + "_funcionalidad_id"));
        return columns;
    }
}

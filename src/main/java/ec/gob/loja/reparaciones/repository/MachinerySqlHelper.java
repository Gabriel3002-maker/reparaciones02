package ec.gob.loja.reparaciones.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class MachinerySqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("codigo", table, columnPrefix + "_codigo"));
        columns.add(Column.aliased("descripcion", table, columnPrefix + "_descripcion"));
        columns.add(Column.aliased("horas_trabajadas", table, columnPrefix + "_horas_trabajadas"));

        columns.add(Column.aliased("nombre_id", table, columnPrefix + "_nombre_id"));
        return columns;
    }
}

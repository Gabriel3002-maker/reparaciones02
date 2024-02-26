package ec.gob.loja.reparaciones.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class MaterialDanioSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("codigo", table, columnPrefix + "_codigo"));
        columns.add(Column.aliased("cantidad_pedida", table, columnPrefix + "_cantidad_pedida"));
        columns.add(Column.aliased("observacion", table, columnPrefix + "_observacion"));

        return columns;
    }
}

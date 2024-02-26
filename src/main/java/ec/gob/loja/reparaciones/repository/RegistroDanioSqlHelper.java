package ec.gob.loja.reparaciones.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class RegistroDanioSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("codigo", table, columnPrefix + "_codigo"));
        columns.add(Column.aliased("fecha", table, columnPrefix + "_fecha"));
        columns.add(Column.aliased("fecha_inicio", table, columnPrefix + "_fecha_inicio"));
        columns.add(Column.aliased("fecha_fin", table, columnPrefix + "_fecha_fin"));
        columns.add(Column.aliased("direccion", table, columnPrefix + "_direccion"));
        columns.add(Column.aliased("parroquia", table, columnPrefix + "_parroquia"));
        columns.add(Column.aliased("barrio", table, columnPrefix + "_barrio"));

        columns.add(Column.aliased("detalle_danio_id", table, columnPrefix + "_detalle_danio_id"));
        return columns;
    }
}

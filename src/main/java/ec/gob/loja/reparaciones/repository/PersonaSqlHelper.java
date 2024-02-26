package ec.gob.loja.reparaciones.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class PersonaSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("identificacion", table, columnPrefix + "_identificacion"));
        columns.add(Column.aliased("primer_apellido", table, columnPrefix + "_primer_apellido"));
        columns.add(Column.aliased("segundo_apellido", table, columnPrefix + "_segundo_apellido"));
        columns.add(Column.aliased("primer_nombre", table, columnPrefix + "_primer_nombre"));
        columns.add(Column.aliased("segundo_nombre", table, columnPrefix + "_segundo_nombre"));
        columns.add(Column.aliased("celular", table, columnPrefix + "_celular"));
        columns.add(Column.aliased("telefono_convencional", table, columnPrefix + "_telefono_convencional"));
        columns.add(Column.aliased("correo", table, columnPrefix + "_correo"));

        columns.add(Column.aliased("tipo_identificacion_id", table, columnPrefix + "_tipo_identificacion_id"));
        columns.add(Column.aliased("usuario_id", table, columnPrefix + "_usuario_id"));
        return columns;
    }
}

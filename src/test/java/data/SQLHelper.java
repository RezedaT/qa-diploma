package data;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLHelper {
    private static QueryRunner runner = new QueryRunner();
    private static String url = System.getProperty("spring.datasource.url");
    private static String user = System.getProperty("spring.datasource.username");
    private static String password = System.getProperty("spring.datasource.password");

    @SneakyThrows
    public static Connection qetConn() {
        return DriverManager.getConnection(url, user, password);
    }
    @SneakyThrows
    public static String getPaymentStatus() {
        String statusSQL = "SELECT status FROM payment_entity ORDER BY created DESC LIMIT 1";
        return getResult(statusSQL);
    }

    @SneakyThrows
    public static String getCreditStatus() {
        String statusSQL = "SELECT status FROM credit_request_entity ORDER BY created DESC LIMIT 1";
        return getResult(statusSQL);
    }
    @SneakyThrows
    private static String getResult(String query) {
        String result = "";
        var runner = new QueryRunner();
        try (var conn = qetConn()) {
            result = runner.query(conn, query, new ScalarHandler<String>());
        }
        return result;
    }

    @SneakyThrows
    public static void cleanDatabase() {
        var connection = qetConn();
        runner.execute(connection, "DELETE FROM credit_request_entity");
        runner.execute(connection, "DELETE FROM order_entity");
        runner.execute(connection, "DELETE FROM payment_entity");
    }
}

//RT

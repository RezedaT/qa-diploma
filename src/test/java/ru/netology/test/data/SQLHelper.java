package ru.netology.test.data;


import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;

public class SQLHelper {
    private static final QueryRunner runner = new QueryRunner();
    private static final String url = System.getProperty("db.url");
    private static final String user = System.getProperty("db.user");
    private static final String password = System.getProperty("db.pass");

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
    public static void cleanDataBase() {
        var connection = qetConn();
        runner.execute(connection, "DELETE FROM credit_request_entity");
        runner.execute(connection, "DELETE FROM order_entity");
        runner.execute(connection, "DELETE FROM payment_entity");
    }
}

//RT

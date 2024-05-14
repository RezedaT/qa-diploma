package ru.netology.test.data;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLHelper {
    private static final QueryRunner runner = new QueryRunner();
    private static final String url = System.getProperty("db.url");
    private static final String user = System.getProperty("db.user");
    private static final String password = System.getProperty("db.pass");

    @SneakyThrows
    public static Connection getConn() {
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
        try (var conn = getConn()) {
            result = runner.query(conn, query, new ScalarHandler<String>());
        }
        return result;
    }

    @SneakyThrows
    public static void cleanDataBase() {
        var connection = getConn();
        runner.execute(connection, "DELETE FROM credit_request_entity");
        runner.execute(connection, "DELETE FROM order_entity");
        runner.execute(connection, "DELETE FROM payment_entity");
    }
    @SneakyThrows
    public static String RowCount() throws SQLException {
        var code = "SELECT COUNT(*) FROM order_entity;";
        Long count = null;
        try (var conn = getConn()) {
            count = runner.query(conn, code, new ScalarHandler<>());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return String.valueOf(count);
    }
    @SneakyThrows
    public static String approvedRowCountPaymentCard() throws SQLException {
        var code = "SELECT COUNT(*) FROM payment_entity WHERE status='APPROVED';";
        Long count = null;
        try (var conn = getConn()) {
            count = runner.query(conn, code, new ScalarHandler<>());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return String.valueOf(count);
    }
    @SneakyThrows
    public static String declineRowCountPaymentCard() throws SQLException {
        var code = "SELECT COUNT(*) FROM payment_entity WHERE status='DECLINED';";
        Long count = null;
        try (var conn = getConn()) {
            count = runner.query(conn, code, new ScalarHandler<>());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
            return String.valueOf(count);
        }
    }


//RT
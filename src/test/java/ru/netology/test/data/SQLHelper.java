package ru.netology.test.data;

import java.sql.Connection;
import java.sql.DriverManager;
import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

public class SQLHelper {
  private static final QueryRunner runner = new QueryRunner();

  public enum PaymentStatus {
    APPROVED,
    DECLINED
  }

  @SneakyThrows
  public static Connection getConn() {
    return DriverManager.getConnection(
        ConfigurationProperties.dbUrl,
        ConfigurationProperties.dbUser,
        ConfigurationProperties.dbPass);
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
  public static int getAmountSQL() {
    var amount = "SELECT amount FROM payment_entity order by created desc LIMIT 1";
    var conn = getConn();
    var result = runner.query(conn, amount, new ScalarHandler<Integer>());
    return result;
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
    try (var connection = getConn()) {
      runner.execute(connection, "DELETE FROM credit_request_entity");
      runner.execute(connection, "DELETE FROM order_entity");
      runner.execute(connection, "DELETE FROM payment_entity");
    }
  }

  @SneakyThrows
  public static String getOrdersCount() {
    var code = "SELECT COUNT(*) FROM order_entity";
    Long count = null;
    try (var conn = getConn()) {
      count = runner.query(conn, code, new ScalarHandler<>());
      return String.valueOf(count);
    }
  }

  @SneakyThrows
  public static String getPaymentsCount(PaymentStatus status) {
    var code = String.format("SELECT COUNT(*) FROM payment_entity WHERE status='%s'", status);
    Long count;
    try (var conn = getConn()) {
      count = runner.query(conn, code, new ScalarHandler<>());
      return String.valueOf(count);
    }
  }

  @SneakyThrows
  public static String getCreditRequestCount(PaymentStatus status) {
    var code =
        String.format("SELECT COUNT(*) FROM credit_request_entity WHERE status='%s'", status);
    Long count;
    try (var conn = getConn()) {
      count = runner.query(conn, code, new ScalarHandler<>());
      return String.valueOf(count);
    }
  }
}

// RT

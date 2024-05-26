package ru.netology.test.data;

public final class ConfigurationProperties {

  public static String dbUrl = System.getProperty("db.url");
  public static String dbUser = System.getProperty("db.user");
  public static String dbPass = System.getProperty("db.pass");

  public static String appUrl = System.getProperty("app.url");
  public static Boolean selenideHeadless =
      Boolean.parseBoolean(System.getProperty("selenide.headless"));
}

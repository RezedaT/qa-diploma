package ru.netology.test.test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static ru.netology.test.data.DataHelper.*;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import ru.netology.test.data.ConfigurationProperties;
import ru.netology.test.data.SQLHelper;

public class DebitPurchaseAPITest {

  private static final RequestSpecification requestSpec =
      new RequestSpecBuilder()
          .setBaseUri(ConfigurationProperties.appUrl)
          .setAccept(ContentType.JSON)
          .setContentType(ContentType.JSON)
          .log(LogDetail.ALL)
          .build();

  @BeforeAll
  static void setUpAll() {
    SelenideLogger.addListener("allure", new AllureSelenide());
  }

  @AfterAll
  static void tearDownAll() {
    SelenideLogger.removeListener("allure");
  }

  @AfterEach
  void tearDown() {
    SQLHelper.cleanDataBase();
  }

  @Test
  @DisplayName("API test. Buy by approved card / Оплата зарегистрированной картой")
  void shouldAPIPaymentByApprovedCardWithValidValues() {
    var approvedCard = generateValidCard();
    given()
        .spec(requestSpec)
        .body(approvedCard)
        .when()
        .post("/api/v1/pay")
        .then()
        .statusCode(200)
        .body("status", equalTo("APPROVED"));
  }

  @Test
  @DisplayName("API test. Buy by declined card / Оплата отклоненной картой")
  void shouldAPIPaymentByDeclineCardWithValidValues() {
    var declinedCard = generateDeclinedCard();
    given()
        .spec(requestSpec)
        .body(declinedCard)
        .when()
        .post("/api/v1/pay")
        .then()
        .statusCode(200)
        .body("status", equalTo("DECLINED"));
  }

  @Test
  @DisplayName(
      "API test. Buy by Random number card / "
          + "Оплата картой со случайным набором 16 числовых символов")
  void shouldAPIPaymentByRandomNumberCardWithValidValues() {
    var card = generateValidCard();
    card.setNumber(generateRandomCardNumber());
    given()
        .spec(requestSpec)
        .body(card)
        .when()
        .post("/api/v1/pay")
        .then()
        .statusCode(500)
        .body("status", equalTo("Internal Server Error"));
  }

  @Test
  @DisplayName(
      "API test. Payment by approved card, invalid values / "
          + "Оплата зарегистрированной картой, невалидные данные")
  void shouldAPIPaymentByApprovedCardWithInvalidValues() {
    var approvedCard = generateValidCard();
    approvedCard.setMonth("00");
    approvedCard.setYear("");
    approvedCard.setOwner("00");
    approvedCard.setCvc("");
    given()
        .spec(requestSpec)
        .body(approvedCard)
        .when()
        .post("/api/v1/pay")
        .then()
        .statusCode(400)
        .body("error", equalTo("Bad Request"));
  }
}

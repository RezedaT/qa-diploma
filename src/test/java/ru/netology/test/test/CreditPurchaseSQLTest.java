package ru.netology.test.test;

import static com.codeborne.selenide.Selenide.open;
import static io.restassured.RestAssured.given;
import static ru.netology.test.data.DataHelper.generateDeclinedCard;
import static ru.netology.test.data.DataHelper.generateValidCard;
import static ru.netology.test.data.SQLHelper.*;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import ru.netology.test.data.CardData;
import ru.netology.test.data.SQLHelper;
import ru.netology.test.page.CreditPurchase;
import ru.netology.test.page.Dashboard;

public class CreditPurchaseSQLTest {

  private static final String appUrl = System.getProperty("app.url");

  private static final RequestSpecification requestSpec =
      new RequestSpecBuilder()
          .setBaseUri(appUrl)
          .setAccept(ContentType.JSON)
          .setContentType(ContentType.JSON)
          .log(LogDetail.ALL)
          .build();

  private static final Boolean selenideHeadless =
      Boolean.parseBoolean(System.getProperty("selenide.headless"));

  Dashboard dashboard;
  CreditPurchase creditPurchase;

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

  @BeforeEach
  void setup() {
    SQLHelper.cleanDataBase();
    open(appUrl);
    Configuration.holdBrowserOpen = false;
    Configuration.headless = selenideHeadless;
    dashboard = new Dashboard();
    creditPurchase = dashboard.chooseCreditPurchase();
  }

  @Test
  @DisplayName("Просмотр статуса в СУБД MySQL отклоненного пользователя")
  void databaseQueryDeclinedStatusTest() {
    var declinedCard = generateDeclinedCard();
    creditPurchase.fillAndSubmitForm(declinedCard);
    creditPurchase.getSuccessNotificationContent();
    //    creditPurchase.clearForm();
    var PaymentStatus = SQLHelper.getCreditStatus();
    Assertions.assertEquals("DECLINED", PaymentStatus);
  }

  @Test
  @DisplayName("Просмотр статуса в СУБД MySQL зарегистрированного пользователя")
  void databaseQueryApprovedStatusTest() {
    var approvedCard = generateValidCard();
    creditPurchase.fillAndSubmitForm(approvedCard);
    creditPurchase.getSuccessNotificationContent();
    //    creditPurchase.clearForm();
    var PaymentStatus = SQLHelper.getCreditStatus();
    Assertions.assertEquals("APPROVED", PaymentStatus);
  }

  @Test
  @DisplayName("Просмотр оплаты тура в СУБД MySQL зарегистрированного пользователя")
  void databaseQueryApprovedAmountTest() {
    var approvedCard = generateValidCard();
    creditPurchase.fillAndSubmitForm(approvedCard);
    creditPurchase.getSuccessNotificationContent();
    creditPurchase.clearForm();
    var PaymentAmount = SQLHelper.getAmountSQL();
    Assertions.assertEquals(450000, PaymentAmount);
  }

  @SneakyThrows
  @Test
  @DisplayName("Status cards in db / Статус карт в базе данных")
  void shouldTestCardsStatus() {

    var approvedCard1 = generateValidCard();
    creditPurchase.fillAndSubmitForm(approvedCard1);
    creditPurchase.getSuccessNotificationContent();
    creditPurchase.clearForm();

    var approvedCard2 = generateValidCard();
    creditPurchase.fillAndSubmitForm(approvedCard2);
    creditPurchase.getSuccessNotificationContent();
    creditPurchase.clearForm();

    var approvedCard3 = generateValidCard();
    creditPurchase.fillAndSubmitForm(approvedCard3);
    creditPurchase.getSuccessNotificationContent();
    creditPurchase.clearForm();

    var declinedCard1 = generateDeclinedCard();
    creditPurchase.fillAndSubmitForm(declinedCard1);
    creditPurchase.getSuccessNotificationContent(); // TODO: this message is wrong
    creditPurchase.clearForm();

    var declinedCard2 = generateDeclinedCard();
    creditPurchase.fillAndSubmitForm(declinedCard2);
    creditPurchase.getSuccessNotificationContent(); // TODO: this message is wrong
    creditPurchase.clearForm();

    Assertions.assertEquals("5", OrdersCount());
    Assertions.assertEquals("3", approvedCreditRequestCount());
    Assertions.assertEquals("2", declinedCreditRequestCount());
  }

  @SneakyThrows
  @Test
  @DisplayName("Status cards in db / Статус карт в базе данных v2")
  void shouldTestCardsStatusV2() {
    // when
    callApiEndpoint(generateValidCard());
    callApiEndpoint(generateDeclinedCard());
    callApiEndpoint(generateValidCard());
    callApiEndpoint(generateDeclinedCard());
    callApiEndpoint(generateValidCard());
    // then
    Assertions.assertEquals("5", OrdersCount());
    Assertions.assertEquals("3", approvedCreditRequestCount());
    Assertions.assertEquals("2", declinedCreditRequestCount());
  }

  private void callApiEndpoint(CardData cardData) {
    given().spec(requestSpec).body(cardData).when().post("/api/v1/credit").then().statusCode(200);
  }
}

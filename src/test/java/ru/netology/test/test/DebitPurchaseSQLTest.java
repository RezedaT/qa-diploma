package ru.netology.test.test;

import static com.codeborne.selenide.Selenide.open;
import static ru.netology.test.data.DataHelper.generateDeclinedCard;
import static ru.netology.test.data.DataHelper.generateValidCard;
import static ru.netology.test.data.SQLHelper.*;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import ru.netology.test.data.ConfigurationProperties;
import ru.netology.test.data.SQLHelper;
import ru.netology.test.page.Dashboard;
import ru.netology.test.page.DebitPurchase;

public class DebitPurchaseSQLTest {

  Dashboard dashboard;
  DebitPurchase debitPurchase;

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
    open(ConfigurationProperties.appUrl);
    Configuration.holdBrowserOpen = false;
    Configuration.headless = ConfigurationProperties.selenideHeadless;
    dashboard = new Dashboard();
    debitPurchase = dashboard.chooseDebitPurchase();
  }

  @Test
  @DisplayName("SQL test. Status declined card in db / Статус отклоненной карты в базе данных")
  void databaseQueryDeclinedStatusTest() {
    var declinedCard = generateDeclinedCard();
    debitPurchase.fillFormAndSubmit(declinedCard);
    debitPurchase.getSuccessNotificationContent();
    var paymentStatus = SQLHelper.getPaymentStatus();
    Assertions.assertEquals("DECLINED", paymentStatus);
  }

  @Test
  @DisplayName(
      "SQL test. Status approved card in db / Статус зарегистрированной карты в базе данных")
  void databaseQueryApprovedStatusTest() {
    var approvedCard = generateValidCard();
    debitPurchase.fillFormAndSubmit(approvedCard);
    debitPurchase.getSuccessNotificationContent();
    var paymentStatus = SQLHelper.getPaymentStatus();
    Assertions.assertEquals("APPROVED", paymentStatus);
  }

  // в базе сумма хранится в копейках
  @Test
  @DisplayName("SQL test. Buy by approved card / Оплата зарегистрированной картой")
  void databaseQueryApprovedAmountTest() {
    var approvedCard = generateValidCard();
    debitPurchase.fillFormAndSubmit(approvedCard);
    debitPurchase.getSuccessNotificationContent();
    var paymentAmount = SQLHelper.getAmountSQL();
    Assertions.assertEquals(4500000, paymentAmount);
  }

  @Test
  @DisplayName("SQL test. Buy by declined card / Оплата отклоненной картой")
  void databaseQueryDeclinedAmountTest() {
    var declinedCard = generateDeclinedCard();
    debitPurchase.fillFormAndSubmit(declinedCard);
    debitPurchase.getSuccessNotificationContent();
    var paymentAmount = SQLHelper.getAmountSQL();
    Assertions.assertEquals(0, paymentAmount);
  }

  @SneakyThrows
  @Test
  @DisplayName("SQL test. Status cards in db / Статус карт в базе данных")
  void shouldTestCardsStatus() {

    var approvedCard1 = generateValidCard();
    debitPurchase.fillFormAndSubmit(approvedCard1);
    debitPurchase.getSuccessNotificationContent();
    debitPurchase.clearForm();

    var approvedCard2 = generateValidCard();
    debitPurchase.fillFormAndSubmit(approvedCard2);
    debitPurchase.getSuccessNotificationContent();
    debitPurchase.clearForm();

    var approvedCard3 = generateValidCard();
    debitPurchase.fillFormAndSubmit(approvedCard3);
    debitPurchase.getSuccessNotificationContent();
    debitPurchase.clearForm();

    var declinedCard1 = generateDeclinedCard();
    debitPurchase.fillFormAndSubmit(declinedCard1);
    debitPurchase.getSuccessNotificationContent();
    debitPurchase.clearForm();

    var declinedCard2 = generateDeclinedCard();
    debitPurchase.fillFormAndSubmit(declinedCard2);
    debitPurchase.getSuccessNotificationContent();
    debitPurchase.clearForm();
    Assertions.assertEquals("5", getOrdersCount());
    Assertions.assertEquals("3", getPaymentsCount(PaymentStatus.APPROVED));
    Assertions.assertEquals("2", getPaymentsCount(PaymentStatus.DECLINED));
  }
}

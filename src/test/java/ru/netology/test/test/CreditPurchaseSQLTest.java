package ru.netology.test.test;

import static com.codeborne.selenide.Selenide.open;
import static ru.netology.test.data.DataHelper.CardData.*;
import static ru.netology.test.data.SQLHelper.*;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import ru.netology.test.data.SQLHelper;
import ru.netology.test.page.CreditPurchase;
import ru.netology.test.page.Dashboard;

public class CreditPurchaseSQLTest {

  private static final String appUrl = System.getProperty("app.url");
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
    open(appUrl);
    Configuration.holdBrowserOpen = false;
    Configuration.headless = selenideHeadless;
    dashboard = new Dashboard();
    creditPurchase = dashboard.chooseCreditPurchase();
  }

  @SneakyThrows
  @Test
  void shouldTestCardsStatus() {

    var approvedCard1 =
        getCardWithParam(
            getNumberApprovedCard(),
            generateValidCardExpireMonth(),
            generateValidCardExpireYear(),
            generateValidCardOwnerName(),
            generateValidCardCVC());
    creditPurchase.fillForm(approvedCard1);
    creditPurchase.getSuccessNotificationContent();
    creditPurchase.clearForm();

    var approvedCard2 =
        getCardWithParam(
            getNumberApprovedCard(),
            generateValidCardExpireMonth(),
            generateValidCardExpireYear(),
            generateValidCardOwnerName(),
            generateValidCardCVC());
    creditPurchase.fillForm(approvedCard2);
    creditPurchase.getSuccessNotificationContent();
    creditPurchase.clearForm();

    var approvedCard3 =
        getCardWithParam(
            getNumberApprovedCard(),
            generateValidCardExpireMonth(),
            generateValidCardExpireYear(),
            generateValidCardOwnerName(),
            generateValidCardCVC());
    creditPurchase.fillForm(approvedCard3);
    creditPurchase.getSuccessNotificationContent();
    creditPurchase.clearForm();

    var declinedCard1 =
        getCardWithParam(
            getNumberDeclinedCard(),
            generateValidCardExpireMonth(),
            generateValidCardExpireYear(),
            generateValidCardOwnerName(),
            generateValidCardCVC());
    creditPurchase.fillForm(declinedCard1);
    creditPurchase.getSuccessNotificationContent();
    creditPurchase.clearForm();

    var declinedCard2 =
        getCardWithParam(
            getNumberDeclinedCard(),
            generateValidCardExpireMonth(),
            generateValidCardExpireYear(),
            generateValidCardOwnerName(),
            generateValidCardCVC());
    creditPurchase.fillForm(declinedCard2);
    creditPurchase.getSuccessNotificationContent();
    creditPurchase.clearForm();
    Assertions.assertEquals("5", RowCount());
    Assertions.assertEquals("3", approvedRowCountPaymentCard());
    Assertions.assertEquals("2", declineRowCountPaymentCard());
  }
}

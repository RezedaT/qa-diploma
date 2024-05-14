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
import ru.netology.test.page.Dashboard;
import ru.netology.test.page.DebitPurchase;

// Сохранение в базу платежа
// Сохраняется ли он вообще
// Записываются ли идентификаторы куда надо
// Равна ли записанная сумма той, что указана на странице
// Тот ли сохраняется статус, что был получен от гейта (из data.json эмулятора)
// Тот ли статус был в сообщении

public class DebitPurchaseSQLTest {

  private static final String appUrl = System.getProperty("app.url");

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
    open(appUrl);
    Configuration.holdBrowserOpen = true;
    dashboard = new Dashboard();
    debitPurchase = dashboard.chooseDebitPurchase();
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
    debitPurchase.fillForm(approvedCard1);
    debitPurchase.getSuccessNotificationContent();
    debitPurchase.clearForm();

    var approvedCard2 =
        getCardWithParam(
            getNumberApprovedCard(),
            generateValidCardExpireMonth(),
            generateValidCardExpireYear(),
            generateValidCardOwnerName(),
            generateValidCardCVC());
    debitPurchase.fillForm(approvedCard2);
    debitPurchase.getSuccessNotificationContent();
    debitPurchase.clearForm();

    var approvedCard3 =
        getCardWithParam(
            getNumberApprovedCard(),
            generateValidCardExpireMonth(),
            generateValidCardExpireYear(),
            generateValidCardOwnerName(),
            generateValidCardCVC());
    debitPurchase.fillForm(approvedCard3);
    debitPurchase.getSuccessNotificationContent();
    debitPurchase.clearForm();

    var declinedCard1 =
        getCardWithParam(
            getNumberDeclinedCard(),
            generateValidCardExpireMonth(),
            generateValidCardExpireYear(),
            generateValidCardOwnerName(),
            generateValidCardCVC());
    debitPurchase.fillForm(declinedCard1);
    debitPurchase.getSuccessNotificationContent();
    debitPurchase.clearForm();

    var declinedCard2 =
        getCardWithParam(
            getNumberDeclinedCard(),
            generateValidCardExpireMonth(),
            generateValidCardExpireYear(),
            generateValidCardOwnerName(),
            generateValidCardCVC());
    debitPurchase.fillForm(declinedCard2);
    debitPurchase.getSuccessNotificationContent();
    debitPurchase.clearForm();
    Assertions.assertEquals("5", RowCount());
    Assertions.assertEquals("3", approvedRowCountPaymentCard());
    Assertions.assertEquals("2", declineRowCountPaymentCard());
  }
}

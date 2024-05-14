package ru.netology.test.test;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import ru.netology.test.data.SQLHelper;
import ru.netology.test.page.CreditPurchase;
import ru.netology.test.page.Dashboard;

public class CreditPurchaseTest {
  private static final String appUrl = System.getProperty("app.url");

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
    Configuration.headless = true;
    this.dashboard = new Dashboard();
    this.creditPurchase = dashboard.chooseCreditPurchase();
  }

  @ParameterizedTest(name = "Тестовые данные из credit-purchase.csv, строка: {index}")
  @CsvFileSource(resources = "/credit-purchase.csv", numLinesToSkip = 1)
  void testFieldsCreditPurchase(
      String cardNum,
      String cardNumError,
      String month,
      String monthError,
      String year,
      String yearError,
      String owner,
      String ownerError,
      String cvc,
      String cvcError,
      String remoteSuccessMessage,
      String remoteErrorMessage) {
    // given
    creditPurchase.setCardNum(cardNum).setMonth(month).setYear(year).setOwner(owner).setCvc(cvc);
    // when
    creditPurchase.clickSubmit();
    // then
    assertEquals(creditPurchase.getCardNumError(), cardNumError);
    assertEquals(creditPurchase.getMonthError(), monthError);
    assertEquals(creditPurchase.getYearError(), yearError);
    assertEquals(creditPurchase.getOwnerError(), ownerError);
    assertEquals(creditPurchase.getCvcError(), cvcError);
    if (remoteSuccessMessage != null) {
      assertEquals(creditPurchase.getSuccessNotificationContent(), remoteSuccessMessage);
    }
    if (remoteErrorMessage != null) {
      assertEquals(creditPurchase.getErrorNotificationContent(), remoteErrorMessage);
    }
  }
}

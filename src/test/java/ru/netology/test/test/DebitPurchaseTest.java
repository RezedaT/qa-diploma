package ru.netology.test.test;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.test.data.DataHelper.*;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.test.data.ConfigurationProperties;
import ru.netology.test.data.SQLHelper;
import ru.netology.test.page.Dashboard;
import ru.netology.test.page.DebitPurchase;

public class DebitPurchaseTest {

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
    open(ConfigurationProperties.appUrl);
    Configuration.holdBrowserOpen = false;
    Configuration.headless = ConfigurationProperties.selenideHeadless;
    this.dashboard = new Dashboard();
    this.debitPurchase = dashboard.chooseDebitPurchase();
  }

  @Test
  @DisplayName("Buy by approved card / Оплата зарегистрированной картой")
  void shouldPaymentByApprovedCardWithValidValues() {
    var approvedCard = generateValidCard();
    debitPurchase.fillFormAndSubmit(approvedCard);
    debitPurchase.getSuccessNotificationContent();
    assertEquals("APPROVED", SQLHelper.getPaymentStatus());
  }

  @Test
  @DisplayName("Buy by declined card / Оплата отклоненной картой")
  void shouldPaymentByDeclineCardWithValidValues() {
    var declinedCard = generateDeclinedCard();
    debitPurchase.fillFormAndSubmit(declinedCard);
    debitPurchase.getErrorNotificationContent();
    assertEquals("DECLINED", SQLHelper.getPaymentStatus());
  }

  @Test
  @DisplayName("Empty card number field / Не заполнено поле номер карты")
  void shouldTestEmptyCardNumberField() {
    var card = generateValidCard();
    card.setNumber("");
    debitPurchase.fillFormAndSubmit(card);
    assertEquals("Поле обязательно для заполнения", debitPurchase.getCardNumError());
  }

  @Test
  @DisplayName("15 digits in the card number field / 15 числовых символов в номере карты")
  void shouldTestNumberLess16Digits() {
    var card = generateValidCard();
    card.setNumber("4444 4444 4444 444");
    debitPurchase.fillFormAndSubmit(card);
    assertEquals("Неверный формат", debitPurchase.getCardNumError());
  }

  @Test
  @DisplayName("Random number card / случайный набор 16 числовых символов в номере карты")
  void shouldTestRandomNumberCard() {
    var card = generateValidCard();
    card.setNumber(generateRandomCardNumber());
    debitPurchase.fillFormAndSubmit(card);
    assertEquals(
        "Ошибка! Банк отказал в проведении операции.", debitPurchase.getErrorNotificationContent());
  }

  @Test
  @DisplayName("Empty month field / Не заполнено поле месяц")
  void shouldTestEmptyMonthField() {
    var card = generateValidCard();
    card.setMonth("");
    debitPurchase.fillFormAndSubmit(card);
    assertEquals("Поле обязательно для заполнения", debitPurchase.getMonthError());
  }

  @Test
  @DisplayName("00 in month field / 00 в поле месяц")
  void shouldTestZeroInMonthField() {
    var card = generateValidCard();
    card.setMonth("00");
    debitPurchase.fillFormAndSubmit(card);
    assertEquals("Неверный формат", debitPurchase.getMonthError());
  }

  @Test
  @DisplayName("13 in month field / 13 в поле месяц")
  void shouldTest13InMonthField() {
    var card = generateValidCard();
    card.setMonth("13");
    debitPurchase.fillFormAndSubmit(card);
    assertEquals("Неверно указан срок действия карты", debitPurchase.getMonthError());
  }

  @Test
  @DisplayName("Previous month in month field / Значение ранее текущего месяца в поле месяц")
  void shouldTestPreviousMonth() {
    var card = generateValidCard();
    card.setMonth(generateDate(-1, "MM"));
    card.setYear(generateDate(0, "YY"));
    debitPurchase.fillFormAndSubmit(card);
    assertEquals("Истёк срок действия карты", debitPurchase.getMonthError());
  }

  @Test
  @DisplayName("1 digits in month field / Одно числовое значение в поле месяц")
  void shouldTestOneNumInMonth() {
    var card = generateValidCard();
    card.setMonth("1");
    debitPurchase.fillFormAndSubmit(card);
    assertEquals("Неверный формат", debitPurchase.getMonthError());
  }

  @Test
  @DisplayName("Special characters in month field / Специмволы в поле месяц")
  void shouldTestSpecialCharactersInMonth() {
    var card = generateValidCard();
    card.setMonth("&*$ @#");
    debitPurchase.fillFormAndSubmit(card);
    assertEquals("Неверный формат", debitPurchase.getMonthError());
  }

  @Test
  @DisplayName("Empty year field / Не заполнено поле год")
  void shouldTestEmptyYearField() {
    var card = generateValidCard();
    card.setYear("");
    debitPurchase.fillFormAndSubmit(card);
    assertEquals("Поле обязательно для заполнения", debitPurchase.getYearError());
  }

  @Test
  @DisplayName("Previous year in year field / Значение ранее текущего годя в поле год")
  void shouldTestPreviousYear() {
    var card = generateValidCard();
    card.setYear(generateDate(-13, "YY"));
    debitPurchase.fillFormAndSubmit(card);
    assertEquals("Истёк срок действия карты", debitPurchase.getYearError());
  }

  @Test
  @DisplayName("Over 5 years  in year field / Значение более 5 лет от текщего года в поле год")
  void shouldTestOverFiveYears() {
    var card = generateValidCard();
    card.setYear(generateDate(62, "YY"));
    debitPurchase.fillFormAndSubmit(card);
    assertEquals("Неверно указан срок действия карты", debitPurchase.getYearError());
  }

  @Test
  @DisplayName("1 digits in year field / Одно числовое значение в поле год")
  void shouldTestOneNumInYear() {
    var card = generateValidCard();
    card.setYear("3");
    debitPurchase.fillFormAndSubmit(card);
    assertEquals("Неверный формат", debitPurchase.getYearError());
  }

  @Test
  @DisplayName("Special characters in year field / Специмволы в поле год")
  void shouldTestSpecialCharactersInYear() {
    var card = generateValidCard();
    card.setYear("$^* ))");
    debitPurchase.fillFormAndSubmit(card);
    assertEquals("Неверный формат", debitPurchase.getYearError());
  }

  @Test
  @DisplayName("Empty holder field / Не заполнено поле владелец")
  void shouldTestEmptyHolderField() {
    var card = generateValidCard();
    card.setOwner("");
    debitPurchase.fillFormAndSubmit(card);
    assertEquals("Поле обязательно для заполнения", debitPurchase.getOwnerError());
  }

  @Test
  @DisplayName("1 character in holder field / 1 латинский символ в поле владелец")
  void shouldTestOneCharacterInYear() {
    var card = generateValidCard();
    card.setOwner("F");
    debitPurchase.fillFormAndSubmit(card);
    assertEquals("Неверный формат", debitPurchase.getOwnerError());
  }

  @Test
  @DisplayName("65 character in holder field / 65 латинских символов в поле владелец")
  void shouldTest65CharacterInYear() {
    var card = generateValidCard();
    card.setOwner("nameMoreThanSixtyFourCharacters NameMoreThanSixtyFourCharacters");
    debitPurchase.fillFormAndSubmit(card);
    assertEquals("Неверный формат", debitPurchase.getOwnerError());
  }

  @Test
  @DisplayName("Cyrillic in the Holder field / Символы на кириллице в поле владелец")
  void shouldTestCyrillicInHolderField() {
    var card = generateValidCard();
    card.setOwner(generateValidCardOwnerNameRus());
    debitPurchase.fillFormAndSubmit(card);
    assertEquals("Неверный формат", debitPurchase.getOwnerError());
  }

  @Test
  @DisplayName("Special characters in the Holder field / Специмволы в поле владелец")
  void shouldTestSpecialCharactersInHolderField() {
    var card = generateValidCard();
    card.setOwner("@ )))&^? !");
    debitPurchase.fillFormAndSubmit(card);
    assertEquals("Неверный формат", debitPurchase.getOwnerError());
  }

  @Test
  @DisplayName("Digits in the holder field / Цифры в поле владелец")
  void shouldTestDigitsInHolderField() {
    var card = generateValidCard();
    card.setOwner("123 456 789");
    debitPurchase.fillFormAndSubmit(card);
    assertEquals("Неверный формат", debitPurchase.getOwnerError());
  }

  @Test
  @DisplayName("Empty CVC field / Не заполнено поле CVC")
  void shouldTestEmptyCVCField() {
    var card = generateValidCard();
    card.setCvc("");
    debitPurchase.fillFormAndSubmit(card);
    assertEquals("Поле обязательно для заполнения", debitPurchase.getCvcError());
  }

  @Test
  @DisplayName("1 digits in CVC field/ Одно числовое значение в поле CVC")
  void shouldTestOneNumInCVC() {
    var card = generateValidCard();
    card.setCvc("3");
    debitPurchase.fillFormAndSubmit(card);
    assertEquals("Неверный формат", debitPurchase.getCvcError());
  }

  @Test
  @DisplayName("000 in CVC field/ 000 в поле CVC")
  void shouldTestZeroInCVC() {
    var card = generateValidCard();
    card.setCvc("000");
    debitPurchase.fillFormAndSubmit(card);
    assertEquals("Неверный формат", debitPurchase.getCvcError());
  }

  @Test
  @DisplayName("Characters in CVC field / Латинские символ в поле CVC ")
  void shouldTestLettersInCVCField() {
    var card = generateValidCard();
    card.setCvc("LPI");
    debitPurchase.fillFormAndSubmit(card);
    assertEquals("Неверный формат", debitPurchase.getCvcError());
  }
}

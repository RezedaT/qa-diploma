package ru.netology.test.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.test.data.SQLHelper;
import ru.netology.test.page.Dashboard;
import ru.netology.test.page.DebitPurchase;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ru.netology.test.data.DataHelper.CardData.*;
import static ru.netology.test.data.DataHelper.faker;

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

    @AfterAll
    static void teardown() {
        SQLHelper.cleanDataBase();
    }

    @BeforeEach
    void setup() {
        open("http://localhost:8080");
        Configuration.holdBrowserOpen = true;
        dashboard = new Dashboard();
        debitPurchase = dashboard.chooseDebitPurchase();
    }

    @Test
    @DisplayName("Buy by approved card / Оплата зарегистрированной картой")
    void shouldPaymentByApprovedCardWithValidValues() {
        var approvedCard = getCardWithParam(getNumberApprovedCard(),
                generateValidCardExpireMonth(), generateValidCardExpireYear(),
                generateValidCardOwnerName(), generateValidCardCVV());
        debitPurchase.fillingForm(approvedCard);
        debitPurchase.getSuccessNotificationContent();
        assertEquals("APPROVED", SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Buy by declined card / Оплата отклоненной картой")
    void shouldPaymentByDeclineCardWithValidValues() {
        var declinedCard = getCardWithParam(getNumberDeclinedCard(),
                generateValidCardExpireMonth(), generateValidCardExpireYear(),
                generateValidCardOwnerName(), generateValidCardCVV());
        debitPurchase.fillingForm(declinedCard);
        debitPurchase.getErrorNotificationContent();
        assertEquals("DECLINED", SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Empty form / Отправка пустой формы")
    void shouldTestEmptyForm() {
        var card = getCardWithParam("", "", "", "", "");
        debitPurchase.fillingForm(card);
        debitPurchase.getCardNumError();
        debitPurchase.getMonthError();
        debitPurchase.getYearError();
        debitPurchase.getOwnerError();
        debitPurchase.getCvcError();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Empty card number field / Не заполнено поле номер карты")
    void shouldTestEmptyCardNumberField() {
        var card = getCardWithParam("",
                generateValidCardExpireMonth(), generateValidCardExpireYear(),
                generateValidCardOwnerName(), generateValidCardCVV());
        debitPurchase.fillingForm(card);
        debitPurchase.getCardNumError();
    }

    @Test
    @DisplayName("15 digits in the card number field / 15 числовых символов в номере карты")
    void shouldTestNumberLess16Digits() {
        var card = getCardWithParam("4444 4444 4444 444",
                generateValidCardExpireMonth(), generateValidCardExpireYear(),
                generateValidCardOwnerName(), generateValidCardCVV());
        debitPurchase.fillingForm(card);
        debitPurchase.getCardNumError();
    }

    @Test
    @DisplayName("Random number card / случайный набор 16 числовых символов в номере карты")
    void shouldTestRandomNumberCard() {
        var card = getCardWithParam(faker.business().creditCardNumber(),
                generateValidCardExpireMonth(), generateValidCardExpireYear(),
                generateValidCardOwnerName(), generateValidCardCVV());
        debitPurchase.fillingForm(card);
        debitPurchase.getCardNumError();
    }

    @Test
    @DisplayName("Empty month field / Не заполнено поле месяц")
    void shouldTestEmptyMonthField() {
        var card = getCardWithParam(getNumberApprovedCard(),
                "", generateValidCardExpireYear(),
                generateValidCardOwnerName(), generateValidCardCVV());
        debitPurchase.fillingForm(card);
        debitPurchase.getMonthError();
    }

    @Test
    @DisplayName("00 in month field / 00 в поле месяц")
    void shouldTestZeroInMonthField() {
        var card = getCardWithParam(getNumberApprovedCard(),
                "00", generateValidCardExpireYear(),
                generateValidCardOwnerName(), generateValidCardCVV());
        debitPurchase.fillingForm(card);
        debitPurchase.getMonthError();
    }

    @Test
    @DisplayName("13 in month field / 13 в поле месяц")
    void shouldTest13InMonthField() {
        var card = getCardWithParam(getNumberApprovedCard(),
                "13", generateValidCardExpireYear(),
                generateValidCardOwnerName(), generateValidCardCVV());
        debitPurchase.fillingForm(card);
        debitPurchase.getMonthError();
    }

    @Test
    @DisplayName("Previous month in month field / Значение ранее текущего месяца в поле месяц")
    void shouldTestPreviousMonth() {
        var card = getCardWithParam(getNumberApprovedCard(),
                generateDate(-1, "MM"),
                generateDate(0, "YY"),
                generateValidCardOwnerName(), generateValidCardCVV());
        debitPurchase.fillingForm(card);
        debitPurchase.getMonthError();
    }

    @Test
    @DisplayName("1 digits in month field / Одно числовое значение в поле месяц")
    void shouldTestOneNumInMonth() {
        var card = getCardWithParam(getNumberApprovedCard(),
                "2", generateValidCardExpireYear(),
                generateValidCardOwnerName(), generateValidCardCVV());
        debitPurchase.fillingForm(card);
        debitPurchase.getMonthError();
    }

    @Test
    @DisplayName("Special characters in month field / Специмволы в поле месяц")
    void shouldTestSpecialCharactersInMonth() {
        var card = getCardWithParam(getNumberApprovedCard(),
                "&*$ @#",generateValidCardExpireYear(),
                generateValidCardOwnerName(), generateValidCardCVV());
        debitPurchase.fillingForm(card);
        debitPurchase.getMonthError();
    }

    @Test
    @DisplayName("Empty year field / Не заполнено поле год")
    void shouldTestEmptyYearField() {
        var card = getCardWithParam(getNumberApprovedCard(),
                generateValidCardExpireMonth(),(""),
                generateValidCardOwnerName(), generateValidCardCVV());
        debitPurchase.fillingForm(card);
        debitPurchase.getYearError();
    }

    @Test
    @DisplayName("Previous year in year field / Значение ранее текущего годя в поле год")
    void shouldTestPreviousYear() {
        var card = getCardWithParam(getNumberApprovedCard(),
                generateValidCardExpireMonth(),
                generateDate(-13, "YY"),
                generateValidCardOwnerName(), generateValidCardCVV());
        debitPurchase.fillingForm(card);
        debitPurchase.getYearError();
    }

    @Test
    @DisplayName("Over 5 years  in year field / Значение более 5 лет от текщего года в поле год")
    void shouldTestOverFiveYears() {
        var card = getCardWithParam(getNumberApprovedCard(),
                generateValidCardExpireMonth(),
                generateDate(-62, "YY"),
                generateValidCardOwnerName(), generateValidCardCVV());
        debitPurchase.fillingForm(card);
        debitPurchase.getYearError();
    }

    @Test
    @DisplayName("1 digits in year field / Одно числовое значение в поле год")
    void shouldTestOneNumInYear() {
        var card = getCardWithParam(getNumberApprovedCard(),
                generateValidCardExpireMonth(),"3",
                generateValidCardOwnerName(), generateValidCardCVV());
        debitPurchase.fillingForm(card);
        debitPurchase.getYearError();
    }

    @Test
    @DisplayName("Special characters in year field / Специмволы в поле год")
    void shouldTestSpecialCharactersInYear() {
        var card = getCardWithParam(getNumberApprovedCard(),
                generateValidCardExpireMonth(),"$^* ))",
                generateValidCardOwnerName(), generateValidCardCVV());
        debitPurchase.fillingForm(card);
        debitPurchase.getYearError();
    }

    @Test
    @DisplayName("Empty holder field / Не заполнено поле владелец")
    void shouldTestEmptyHolderField() {
        var card = getCardWithParam(getNumberApprovedCard(),
                generateValidCardExpireMonth(),generateValidCardExpireYear(),
                "", generateValidCardCVV());
        debitPurchase.fillingForm(card);
        debitPurchase.getOwnerError();
    }

    @Test
    @DisplayName("1 character in holder field / 1 латинский символ в поле владелец")
    void shouldTestOneCharacterInYear(){
    var card = getCardWithParam(getNumberApprovedCard(),
            generateValidCardExpireMonth(),generateValidCardExpireYear(),
            "F", generateValidCardCVV());
        debitPurchase.fillingForm(card);
        debitPurchase.getOwnerError();
}

    @Test
    @DisplayName("65 character in holder field / 65 латинских символов в поле владелец")
    void shouldTest65CharacterInYear(){
        var card = getCardWithParam(getNumberApprovedCard(),
                generateValidCardExpireMonth(),generateValidCardExpireYear(),
                "nameMoreThanSixtyFourCharacters NameMoreThanSixtyFourCharacters", generateValidCardCVV());
        debitPurchase.fillingForm(card);
        debitPurchase.getOwnerError();
    }

    @Test
    @DisplayName("Cyrillic in the Holder field / Символы на кириллице в поле владелец")
    void shouldTestCyrillicInHolderField(){
        var card = getCardWithParam(getNumberApprovedCard(),
                generateValidCardExpireMonth(),generateValidCardExpireYear(),
                generateValidCardOwnerNameRus(), generateValidCardCVV());
        debitPurchase.fillingForm(card);
        debitPurchase.getOwnerError();
    }

    @Test
    @DisplayName("Special characters in the Holder field / Специмволы в поле владелец")
    void shouldTestSpecialCharactersInHolderField(){
        var card = getCardWithParam(getNumberApprovedCard(),
                generateValidCardExpireMonth(),generateValidCardExpireYear(),
                "@ )))&^? !", generateValidCardCVV());
        debitPurchase.fillingForm(card);
        debitPurchase.getOwnerError();
    }

    @Test
    @DisplayName("Digits in the holder field / Цифры в поле владелец")
    void shouldTestDigitsInHolderField(){
        var card = getCardWithParam(getNumberApprovedCard(),
                generateValidCardExpireMonth(),generateValidCardExpireYear(),
                "123 456 789", generateValidCardCVV());
        debitPurchase.fillingForm(card);
        debitPurchase.getOwnerError();
    }

    @Test
    @DisplayName("Empty CVC field / Не заполнено поле CVC")
    void shouldTestEmptyCVCField(){
        var card = getCardWithParam(getNumberApprovedCard(),
                generateValidCardExpireMonth(),generateValidCardExpireYear(),
                generateValidCardOwnerName(), "");
        debitPurchase.fillingForm(card);
        debitPurchase.getCvcError();
    }

    @Test
    @DisplayName("1 digits in CVC field/ Одно числовое значение в поле CVC")
    void shouldTestOneNumInCVC() {
        var card = getCardWithParam(getNumberApprovedCard(),
                generateValidCardExpireMonth(),generateValidCardExpireYear(),
                generateValidCardOwnerName(), "3");
        debitPurchase.fillingForm(card);
        debitPurchase.getCvcError();
    }

    @Test
    @DisplayName("000 in CVC field/ 000 в поле CVC")
    void shouldTestZeroInCVC() {
        var card = getCardWithParam(getNumberApprovedCard(),
                generateValidCardExpireMonth(),generateValidCardExpireYear(),
                generateValidCardOwnerName(), "000");
        debitPurchase.fillingForm(card);
        debitPurchase.getCvcError();
    }

    @Test
    @DisplayName("Characters in CVC field / Латинские символ в поле CVC ")
    void shouldTestLettersInCVVField() {
        var card = getCardWithParam(getNumberApprovedCard(),
                generateValidCardExpireMonth(),generateValidCardExpireYear(),
                generateValidCardOwnerName(), "LPI");
        debitPurchase.fillingForm(card);
        debitPurchase.getCvcError();
    }

}








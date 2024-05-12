package ru.netology.test.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import ru.netology.test.data.SQLHelper;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.test.page.CreditPurchase;
import ru.netology.test.page.Dashboard;
import ru.netology.test.page.DebitPurchase;

import static com.codeborne.selenide.Selenide.open;
import static ru.netology.test.data.DataHelper.CardData.*;
import static ru.netology.test.data.DataHelper.faker;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ru.netology.test.page.DebitPurchase.*;

public class CreditPurchaseTest {

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

    @AfterAll
    static void teardown() {
        SQLHelper.cleanDataBase();
    }

    @BeforeEach
    void setup() {
        open("http://localhost:8080");
        Configuration.holdBrowserOpen = true;
        dashboard = new Dashboard();
        creditPurchase = dashboard.chooseCreditPurchase();
    }

    @Test
    @DisplayName("Credit by approved card / Оплата в кредит зарегистрированной картой")
    void shouldPaymentByApprovedCardWithValidValues() {
        var approvedCard = getCardWithParam(getNumberApprovedCard(),
                generateValidCardExpireMonth(), generateValidCardExpireYear(),
                generateValidCardOwnerName(), generateValidCardCVV());
        fillingForm(approvedCard);
        successNotification();
        assertEquals("APPROVED", SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Credit by declined card / Оплата в кредит отклоненной картой")
    void shouldPaymentByDeclineCardWithValidValues() {
        var declinedCard = getCardWithParam(getNumberDeclinedCard(),
                generateValidCardExpireMonth(), generateValidCardExpireYear(),
                generateValidCardOwnerName(), generateValidCardCVV());
        fillingForm(declinedCard);
        errorNotification();
        assertEquals("DECLINED", SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Empty form / Отправка пустой формы")
    void shouldTestEmptyForm() {
        var approvedCard = getCardWithParam("", "", "", "", "");
        fillingForm(approvedCard);
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Empty card number field / Не заполнен номер карты")
    void shouldTestEmptyCardNumberField() {
        var card = getCardWithParam("",
                generateValidCardExpireMonth(), generateValidCardExpireYear(),
                generateValidCardOwnerName(), generateValidCardCVV());
        fillingForm(card);
        requiredField();
    }

    @Test
    @DisplayName("15 digits in the card number field / 15 числовых символов в номере карты")
    void shouldTestNumberLess16Digits() {
        var card = getCardWithParam("4444 4444 4444 444",
                generateValidCardExpireMonth(), generateValidCardExpireYear(),
                generateValidCardOwnerName(), generateValidCardCVV());
        fillingForm(card);
        invalidFormat();
    }

    @Test
    @DisplayName("Random number card / случайный набор 16 числовых символов в номере карты")
    void shouldTestRandomNumberCard() {
        var card = getCardWithParam(faker.business().creditCardNumber(),
                generateValidCardExpireMonth(), generateValidCardExpireYear(),
                generateValidCardOwnerName(), generateValidCardCVV());
        fillingForm(card);
        errorNotification();
    }

    @Test
    @DisplayName("Empty month field / Не заполнено поле месяц")
    void shouldTestEmptyMonthField() {
        var card = getCardWithParam(getNumberApprovedCard(),
                "", generateValidCardExpireYear(),
                generateValidCardOwnerName(), generateValidCardCVV());
        fillingForm(card);
        requiredField();
    }

    @Test
    @DisplayName("00 in month field / 00 в поле месяц")
    void shouldTestZeroInMonthField() {
        var card = getCardWithParam(getNumberApprovedCard(),
                "00", generateValidCardExpireYear(),
                generateValidCardOwnerName(), generateValidCardCVV());
        fillingForm(card);
        invalidFormat();
    }

    @Test
    @DisplayName("13 in month field / 13 в поле месяц")
    void shouldTest13InMonthField() {
        var card = getCardWithParam(getNumberApprovedCard(),
                "13", generateValidCardExpireYear(),
                generateValidCardOwnerName(), generateValidCardCVV());
        fillingForm(card);
        incorrectDeadline();
    }

    @Test
    @DisplayName("Previous month in month field / Значение ранее текущего месяца в поле месяц")
    void shouldTestPreviousMonth() {
        var card = getCardWithParam(getNumberApprovedCard(),
                generateDate(-1, "MM"),
                generateDate(0, "YY"),
                generateValidCardOwnerName(), generateValidCardCVV());
        fillingForm(card);
        incorrectDeadline();
    }

    @Test
    @DisplayName("1 digits in the month field / Одно числовое значение в поле месяц")
    void shouldTestOneNumInMonth() {
        var card = getCardWithParam(getNumberApprovedCard(),
                "2", generateValidCardExpireYear(),
                generateValidCardOwnerName(), generateValidCardCVV());
        fillingForm(card);
        invalidFormat();
    }

    @Test
    @DisplayName("Special characters in month field / Специмволы в поле месяц")
    void shouldTestSpecialCharactersInMonth() {
        var card = getCardWithParam(getNumberApprovedCard(),
                "&*$ @#",generateValidCardExpireYear(),
                generateValidCardOwnerName(), generateValidCardCVV());
        fillingForm(card);
        invalidFormat();
    }

    @Test
    @DisplayName("Empty year field / Не заполнено поле год")
    void shouldTestEmptyYearField() {
        var card = getCardWithParam(getNumberApprovedCard(),
                generateValidCardExpireMonth(),(""),
                generateValidCardOwnerName(), generateValidCardCVV());
        fillingForm(card);
        requiredField();
    }

    @Test
    @DisplayName("Previous year in year field / Значение ранее текущего годя в поле год")
    void shouldTestPreviousYear() {
        var card = getCardWithParam(getNumberApprovedCard(),
                generateValidCardExpireMonth(),
                generateDate(-13, "YY"),
                generateValidCardOwnerName(), generateValidCardCVV());
        fillingForm(card);
        deadlineIsOver();
    }

    @Test
    @DisplayName("Over 5 years in year field / Значение более 5 лет от текщего года в поле год")
    void shouldTestOverFiveYears() {
        var card = getCardWithParam(getNumberApprovedCard(),
                generateValidCardExpireMonth(),
                generateDate(-62, "YY"),
                generateValidCardOwnerName(), generateValidCardCVV());
        fillingForm(card);
        deadlineIsOver();
    }

    @Test
    @DisplayName("1 digit in year field / Одно числовое значение в поле год")
    void shouldTestOneNumInYear() {
        var card = getCardWithParam(getNumberApprovedCard(),
                generateValidCardExpireMonth(),"3",
                generateValidCardOwnerName(), generateValidCardCVV());
        fillingForm(card);
        invalidFormat();
    }
    @Test
    @DisplayName("Special characters in year field / Специмволы в поле год")
    void shouldTestSpecialCharactersInYear() {
        var card = getCardWithParam(getNumberApprovedCard(),
                generateValidCardExpireMonth(),"$^* ))",
                generateValidCardOwnerName(), generateValidCardCVV());
        fillingForm(card);
        invalidFormat();
    }

    @Test
    @DisplayName("Empty holder field / Не заполнено поле владелец")
    void shouldTestEmptyHolderField() {
        var card = getCardWithParam(getNumberApprovedCard(),
                generateValidCardExpireMonth(),generateValidCardExpireYear(),
                "", generateValidCardCVV());
        fillingForm(card);
        requiredField();
    }

    @Test
    @DisplayName("1 character in holder field / 1 латинский символ в поле владелец")
    void shouldTestOneCharacterInYear(){
        var card = getCardWithParam(getNumberApprovedCard(),
                generateValidCardExpireMonth(),generateValidCardExpireYear(),
                "F", generateValidCardCVV());
        fillingForm(card);
        invalidFormat();
    }

    @Test
    @DisplayName("65character in holder field / 65 латинский символ в поле владелец")
    void shouldTest65CharacterInYear(){
        var card = getCardWithParam(getNumberApprovedCard(),
                generateValidCardExpireMonth(),generateValidCardExpireYear(),
                "nameMoreThanSixtyFourCharacters NameMoreThanSixtyFourCharacters", generateValidCardCVV());
        fillingForm(card);
        invalidFormat();
    }

    @Test
    @DisplayName("Cyrillic in the Holder field / Символы на кириллице в поле владелец")
    void shouldTestCyrillicInHolderField(){
        var card = getCardWithParam(getNumberApprovedCard(),
                generateValidCardExpireMonth(),generateValidCardExpireYear(),
                generateValidCardOwnerNameRus(), generateValidCardCVV());
        fillingForm(card);
        invalidFormat();
    }

    @Test
    @DisplayName("Special characters in the Holder field / Специмволы в поле владелец")
    void shouldTestSpecialCharactersInHolderField(){
        var card = getCardWithParam(getNumberApprovedCard(),
                generateValidCardExpireMonth(),generateValidCardExpireYear(),
                "@ )))&^? !", generateValidCardCVV());
        fillingForm(card);
        invalidFormat();
    }

    @Test
    @DisplayName("Digits in the Holder field / Цифры в поле владелец")
    void shouldTestDigitsInHolderField(){
        var card = getCardWithParam(getNumberApprovedCard(),
                generateValidCardExpireMonth(),generateValidCardExpireYear(),
                "123 456 789", generateValidCardCVV());
        fillingForm(card);
        invalidFormat();
    }
}

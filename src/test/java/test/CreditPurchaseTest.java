package test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import data.SQLHelper;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import page.CreditPurchase;
import page.Dashboard;
import page.DebitPurchase;

import static com.codeborne.selenide.Selenide.open;
import static data.DataHelper.CardData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static page.DebitPurchase.*;

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
                generateValidCardOwnerName(), generateValidCardCVV(),
                generateValidCardExpireMonth(),
                generateValidCardExpireYear());
        fillingForm(approvedCard);
        successNotification();
        assertEquals("APPROVED", SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Credit by declined card / Оплата в кредит отклоненной картой")
    void shouldPaymentByDeclineCardWithValidValues() {
        var declinedCard = getCardWithParam(getNumberDeclinedCard(),
                generateValidCardOwnerName(), generateValidCardCVV(),
                generateValidCardExpireMonth(),
                generateValidCardExpireYear());
        fillingForm(declinedCard);
        errorNotification();
        assertEquals("DECLINED", SQLHelper.getCreditStatus());
    }
}


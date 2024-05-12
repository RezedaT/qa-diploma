package test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import data.SQLHelper;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import page.Dashboard;
import page.DebitPurchase;

import static com.codeborne.selenide.Selenide.open;
import static data.DataHelper.CardData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static page.DebitPurchase.*;

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
                generateValidCardOwnerName(), generateValidCardCVV(),
                generateValidCardExpireMonth(),
                generateValidCardExpireYear());
        fillingForm(approvedCard);
        successNotification();
        assertEquals("APPROVED", SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Buy by declined card / Оплата отклоненной картой")
    void shouldPaymentByDeclineCardWithValidValues() {
        var declinedCard = getCardWithParam(getNumberDeclinedCard(),
                generateValidCardOwnerName(), generateValidCardCVV(),
                generateValidCardExpireMonth(),
                generateValidCardExpireYear());
        fillingForm(declinedCard);
        errorNotification();
        assertEquals("DECLINED", SQLHelper.getPaymentStatus());
    }
}

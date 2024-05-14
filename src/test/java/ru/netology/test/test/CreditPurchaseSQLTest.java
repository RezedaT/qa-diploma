package ru.netology.test.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import ru.netology.test.data.SQLHelper;
import ru.netology.test.page.CreditPurchase;
import ru.netology.test.page.Dashboard;

import static com.codeborne.selenide.Selenide.open;
import static ru.netology.test.data.DataHelper.CardData.*;
import static ru.netology.test.data.SQLHelper.*;

public class CreditPurchaseSQLTest {

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
        SQLHelper.cleanDataBase();
        open("http://localhost:8080");
        Configuration.holdBrowserOpen = true;
        dashboard = new Dashboard();
        creditPurchase = dashboard.chooseCreditPurchase();

    }

    @SneakyThrows
    @Test
    void shouldTestCardsStatus() {

        var approvedCard1 = getCardWithParam(getNumberApprovedCard(),
                generateValidCardExpireMonth(), generateValidCardExpireYear(),
                generateValidCardOwnerName(), generateValidCardCVV());
        creditPurchase.fillingForm(approvedCard1);
        creditPurchase.getSuccessNotificationContent();
        creditPurchase.clearingForm();


        var approvedCard2 = getCardWithParam(getNumberApprovedCard(),
                generateValidCardExpireMonth(), generateValidCardExpireYear(),
                generateValidCardOwnerName(), generateValidCardCVV());
        creditPurchase.fillingForm(approvedCard2);
        creditPurchase.getSuccessNotificationContent();
        creditPurchase.clearingForm();

        var approvedCard3 = getCardWithParam(getNumberApprovedCard(),
                generateValidCardExpireMonth(), generateValidCardExpireYear(),
                generateValidCardOwnerName(), generateValidCardCVV());
        creditPurchase.fillingForm(approvedCard3);
        creditPurchase.getSuccessNotificationContent();
        creditPurchase.clearingForm();

        var declinedCard1 = getCardWithParam(getNumberDeclinedCard(),
                generateValidCardExpireMonth(), generateValidCardExpireYear(),
                generateValidCardOwnerName(), generateValidCardCVV());
        creditPurchase.fillingForm(declinedCard1);
        creditPurchase.getSuccessNotificationContent();
        creditPurchase.clearingForm();

        var declinedCard2 = getCardWithParam(getNumberDeclinedCard(),
                generateValidCardExpireMonth(), generateValidCardExpireYear(),
                generateValidCardOwnerName(), generateValidCardCVV());
        creditPurchase.fillingForm(declinedCard2);
        creditPurchase.getSuccessNotificationContent();
        creditPurchase.clearingForm();
        Assertions.assertEquals("5", RowCount());
        Assertions.assertEquals("3", approvedRowCountPaymentCard());
        Assertions.assertEquals("2", declineRowCountPaymentCard());
    }
}




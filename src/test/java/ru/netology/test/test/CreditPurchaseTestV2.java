package ru.netology.test.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import ru.netology.test.data.SQLHelper;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import ru.netology.test.page.CreditPurchase;
import ru.netology.test.page.Dashboard;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreditPurchaseTestV2 {
    Dashboard dashboard;

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
        Configuration.holdBrowserOpen = false;
        dashboard = new Dashboard();
        dashboard.chooseCreditPurchase();
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/credit-purchase.csv", numLinesToSkip = 1)
    void testFieldsCreditPurchase(String cardNum, String cardNumError, String month, String monthError,
                          String year, String yearError, String owner, String ownerError, String cvc, String cvcError,
                          String remoteSuccessMessage, String remoteErrorMessage) {
        var creditPurchase = new CreditPurchase();
        creditPurchase.setCardNum(cardNum).setMonth(month).setYear(year).setOwner(owner).setCvc(cvc);
        creditPurchase.clickSubmit();
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

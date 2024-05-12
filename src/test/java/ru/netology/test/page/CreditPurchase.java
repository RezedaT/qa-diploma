package ru.netology.test.page;

import com.codeborne.selenide.SelenideElement;
import credit_purchase.CreditPurchagePageV2;
import ru.netology.test.data.DataHelper;
import org.openqa.selenium.Keys;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class CreditPurchase {

    private SelenideElement header= $x("//h3[text()='Кредит по данным карты']");
//    private SelenideElement cardNumberField = $x("//span[text()='Номер карты']/..//input");
//    private SelenideElement monthField = $x("//span[text()='Месяц']/..//input");
//    private SelenideElement yearField = $x("//span[text()='Год']/..//input");
//    private SelenideElement holderField = $x("//span[text()='Владелец']/..//input");
//    private SelenideElement cvcField = $x("//span[contains(text(),'CVV')]/..//input");
//    private SelenideElement continueButton = $x("//span[text()='Продолжить']");
    private SelenideElement closePopupWindow = $(".icon-button__text");

    private final SelenideElement cardNumberField;
    private final SelenideElement monthField;
    private final SelenideElement yearField;
    private final SelenideElement holderField;
    private final SelenideElement cvcField;
    private final SelenideElement continueButton;
    private final SelenideElement successNotification;
    private final SelenideElement errorNotification;

    public CreditPurchase() {
        var fields = $$(".input");
        cardNumberField = fields.get(0);
        monthField = fields.get(1);
        yearField = fields.get(2);
        holderField = fields.get(3);
        cvcField = fields.get(4);
        continueButton = $("form button");
        successNotification = $(".notification_status_ok");
        errorNotification = $(".notification_status_error");
        header
                .shouldBe(visible)
                .shouldHave(text("Кредит по данным карты"));
    }
    public void clickSubmit() {
        continueButton.click();
    }

    public CreditPurchase setCardNum(String value) {
        cardNumberField .$("input").setValue(value);
        return this;
    }
    public CreditPurchase setMonth(String value) {
        monthField.$("input").setValue(value);
        return this;
    }
    public CreditPurchase setYear(String value) {
        yearField.$("input").setValue(value);
        return this;
    }

    public CreditPurchase setOwner(String value) {
        holderField.$("input").setValue(value);
        return this;
    }

    public CreditPurchase setCvc(String value) {
        cvcField.$("input").setValue(value);
        return this;
    }

    public String getCardNumError() {
        return cardNumberField.$(".input__sub").exists() ? cardNumberField.$(".input__sub").text() : null;
    }

    public String getMonthError() {
        return monthField.$(".input__sub").exists() ? monthField.$(".input__sub").text() : null;
    }

    public String getYearError() {
        return yearField.$(".input__sub").exists() ? yearField.$(".input__sub").text() : null;
    }

    public String getOwnerError() {
        return holderField.$(".input__sub").exists() ? holderField.$(".input__sub").text() : null;
    }

    public String getCvcError() {
        return cvcField.$(".input__sub").exists() ? cvcField.$(".input__sub").text() : null;
    }

    public String getSuccessNotificationContent() {
        return successNotification.$(".notification__content").shouldBe(visible, Duration.ofSeconds(15)).text();
    }
    public String getErrorNotificationContent() {
        return errorNotification.$(".notification__content").shouldBe(visible, Duration.ofSeconds(15)).text();
    }


    public void fillingForm(DataHelper.CardData cardData) {
        cardNumberField.setValue(cardData.getNumber());
        monthField.setValue(cardData.getMonth());
        yearField.setValue(cardData.getYear());
        holderField.setValue(cardData.getHolder());
        cvcField.setValue(cardData.getCvc());
        continueButton.click();
    }

    public void clearingForm() {
        cardNumberField.sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        monthField.sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        yearField.sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        holderField.sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        cvcField.sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        closePopupWindow.click();
    }
}

//RT




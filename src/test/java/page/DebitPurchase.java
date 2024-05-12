package page;

import com.codeborne.selenide.SelenideElement;
import data.DataHelper;
import org.openqa.selenium.Keys;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class DebitPurchase {
    //поля//
    public static final SelenideElement header = $x("//h3[text()='Оплата по карте']");
    public static final SelenideElement cardNumberField = $x("//span[text()='Номер карты']/..//input");
    public static final SelenideElement monthField = $x("//span[text()='Месяц']/..//input");
    public static final SelenideElement yearField = $x("//span[text()='Год']/..//input");
    public static final SelenideElement holderField = $x("//span[text()='Владелец']/..//input");
    public static final SelenideElement cvcField = $x("//span[contains(text(),'CVV')]/..//input");
    public static final SelenideElement continueButton = $x("//span[text()='Продолжить']");
    public static final SelenideElement closePopupWindow = $(".icon-button__text");

    //собщения//
    private static final SelenideElement successNotification = $x("//div[contains(@class,'notification_status_ok')]");

    private static final SelenideElement errorNotification = $x("//div[contains(@class, 'notification_status_error')]");

    public static final SelenideElement invalidFormat = $x("//span[contains(text(), 'Неверный формат')]");

    public static final SelenideElement requiredField = $x("//span[contains(text(), 'Поле обязательно')]");

    public static final SelenideElement incorrectDeadline = $x("//span[contains(text(), 'Неверно указан срок')]");

    public static final SelenideElement deadlineIsOver = $x("//span[contains(text(), 'Истёк срок')]");

    public static void successNotification() {
        successNotification.
                shouldBe(visible, Duration.ofSeconds(15)).shouldHave(text("Успешно Операция одобрена Банком."));
    }

    public static void errorNotification() {
        errorNotification.
                shouldBe(visible, Duration.ofSeconds(15)).shouldHave(text("Ошибка! Банк отказал в проведении операции."));
    }

    public static void requiredField() {
        requiredField.
                shouldBe(visible).shouldHave(text("Поле обязательно для заполнения"));
    }

    public static void invalidFormat() {
        invalidFormat.
                shouldBe(visible).shouldHave(text("Неверный формат"));
    }

    public static void incorrectDeadline() {
        incorrectDeadline.
                shouldBe(visible).shouldHave(text("Неверно указан срок действия карты"));
    }

    public static void deadlineIsOver() {
        deadlineIsOver.
                shouldBe(visible).shouldHave(text("Истёк срок действия карты"));
    }

    public DebitPurchase() {
         header
                .shouldBe(visible)
                .shouldHave(text("Оплата по карте"));
    }

    public static void fillingForm(DataHelper.CardData cardData) {
        cardNumberField.setValue(cardData.getNumber());
        monthField.setValue(cardData.getMonth());
        yearField.setValue(cardData.getYear());
        holderField.setValue(cardData.getHolder());
        cvcField.setValue(cardData.getCvc());
        continueButton.click();
    }


    public static void clearingForm() {
        cardNumberField.sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        monthField.sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        yearField.sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        holderField.sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        cvcField.sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        closePopupWindow.click();
    }
}

//RT
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
    private SelenideElement header = $x("//h3[text()='Оплата по карте']");
    private static SelenideElement cardNumberField = $x("//span[text()='Номер карты']/..//input");
    private SelenideElement monthField = $x("//span[text()='Месяц']/..//input");
    private SelenideElement yearField = $x("//span[text()='Год']/..//input");
    private SelenideElement holderField = $x("//span[text()='Владелец']/..//input");
    private SelenideElement cvcField = $x("//span[contains(text(),'CVV')]/..//input");
    private SelenideElement continueButton = $x("//span[text()='Продолжить']");
    private SelenideElement closePopupWindow = $(".icon-button__text");

    //собщения//
    private static final SelenideElement successNotification = $x("//div[contains(@class,'notification_status_ok')]");

    private final SelenideElement errorNotification = $x("//div[contains(@class, 'notification_status_error')]");

    private final SelenideElement invalidFormat = $x("//span[contains(text(), 'Неверный формат')]");

    private final SelenideElement requiredField = $x("//span[contains(text(), 'Поле обязательно')]");

    private final SelenideElement incorrectDeadline = $x("//span[contains(text(), 'Неверно указан срок')]");

    private final SelenideElement deadlineIsOver = $x("//span[contains(text(), 'Истёк срок')]");

    public static void successNotification() {
        successNotification.
                shouldBe(visible, Duration.ofSeconds(15)).shouldHave(text("Успешно Операция одобрена Банком."));
    }

    public void errorNotification() {
        errorNotification.
                shouldBe(visible, Duration.ofSeconds(15)).shouldHave(text("Ошибка! Банк отказал в проведении операции."));
    }

    public void requiredField() {
        requiredField.
                shouldBe(visible).shouldHave(text("Поле обязательно для заполнения"));
    }

    public void invalidFormat() {
        invalidFormat.
                shouldBe(visible).shouldHave(text("Неверный формат"));
    }

    public void incorrectDeadline() {
        incorrectDeadline.
                shouldBe(visible).shouldHave(text("Неверно указан срок действия карты"));
    }

    public void deadlineIsOver() {
        deadlineIsOver.
                shouldBe(visible).shouldHave(text("Истёк срок действия карты"));
    }

    public DebitPurchase() {
         header
                .shouldBe(visible)
                .shouldHave(text("Оплата по карте"));
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
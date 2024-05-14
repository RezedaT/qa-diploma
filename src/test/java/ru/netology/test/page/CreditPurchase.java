package ru.netology.test.page;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

import com.codeborne.selenide.SelenideElement;
import java.time.Duration;
import org.openqa.selenium.Keys;
import ru.netology.test.data.DataHelper;

public class CreditPurchase {

  public static final SelenideElement closePopupWindow1 = $(".notification_status_ok button");
  public static final SelenideElement closePopupWindow2 = $(".notification_status_error button");
  private final SelenideElement cardNumberField;
  private final SelenideElement monthField;
  private final SelenideElement yearField;
  private final SelenideElement ownerField;
  private final SelenideElement cvcField;
  private final SelenideElement continueButton;
  private final SelenideElement successNotification;
  private final SelenideElement errorNotification;
  private SelenideElement header = $x("//h3[text()='Кредит по данным карты']");

  public CreditPurchase() {
    var fields = $$(".input");
    cardNumberField = fields.get(0);
    monthField = fields.get(1);
    yearField = fields.get(2);
    ownerField = fields.get(3);
    cvcField = fields.get(4);
    continueButton = $("form button");
    successNotification = $(".notification_status_ok");
    errorNotification = $(".notification_status_error");
    header.shouldBe(visible, Duration.ofSeconds(5)).shouldHave(text("Кредит по данным карты"));
  }

  public void clickSubmit() {
    continueButton.click();
  }

  public CreditPurchase setCardNum(String value) {
    cardNumberField.$("input").setValue(value);
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
    ownerField.$("input").setValue(value);
    return this;
  }

  public CreditPurchase setCvc(String value) {
    cvcField.$("input").setValue(value);
    return this;
  }

  public String getCardNumError() {
    return cardNumberField.$(".input__sub").exists()
        ? cardNumberField.$(".input__sub").text()
        : null;
  }

  public String getMonthError() {
    return monthField.$(".input__sub").exists() ? monthField.$(".input__sub").text() : null;
  }

  public String getYearError() {
    return yearField.$(".input__sub").exists() ? yearField.$(".input__sub").text() : null;
  }

  public String getOwnerError() {
    return ownerField.$(".input__sub").exists() ? ownerField.$(".input__sub").text() : null;
  }

  public String getCvcError() {
    return cvcField.$(".input__sub").exists() ? cvcField.$(".input__sub").text() : null;
  }

  public String getSuccessNotificationContent() {
    return successNotification
        .$(".notification__content")
        .shouldBe(visible, Duration.ofSeconds(15))
        .text();
  }

  public String getErrorNotificationContent() {
    return errorNotification
        .$(".notification__content")
        .shouldBe(visible, Duration.ofSeconds(15))
        .text();
  }

  public void fillForm(DataHelper.CardData cardData) {
    setCardNum(cardData.getNumber())
            .setMonth(cardData.getMonth())
            .setYear(cardData.getYear())
            .setOwner(cardData.getOwner())
            .setCvc(cardData.getCvc());
    continueButton.click();
  }

  public void clearForm() {
    cardNumberField.sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
    monthField.sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
    yearField.sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
    ownerField.sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
    cvcField.sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
    if (closePopupWindow1.isDisplayed()) {
      closePopupWindow1.click();
    }
    if (closePopupWindow2.isDisplayed()) {
      closePopupWindow2.click();
    }
  }
}

// RT

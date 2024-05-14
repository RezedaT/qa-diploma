package ru.netology.test.page;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

import com.codeborne.selenide.SelenideElement;
import java.time.Duration;
import org.openqa.selenium.Keys;
import ru.netology.test.data.DataHelper;

public class DebitPurchase {

  public static final SelenideElement header = $x("//h3[text()='Оплата по карте']");

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

  public DebitPurchase() {
    var fields = $$(".input");
    cardNumberField = fields.get(0);
    monthField = fields.get(1);
    yearField = fields.get(2);
    ownerField = fields.get(3);
    cvcField = fields.get(4);
    continueButton = $("form button");
    successNotification = $(".notification_status_ok");
    errorNotification = $(".notification_status_error");
    header.shouldBe(visible);
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
    cardNumberField.$("input").setValue(cardData.getNumber());
    monthField.$("input").setValue(cardData.getMonth());
    yearField.$("input").setValue(cardData.getYear());
    ownerField.$("input").setValue(cardData.getOwner());
    cvcField.$("input").setValue(cardData.getCvc());
    continueButton.click();
  }

  public void clearForm() {
    cardNumberField.$("input").setValue("");
    monthField.$("input").setValue("");
    yearField.$("input").setValue("");
    ownerField.$("input").setValue("");
    cvcField.$("input").setValue("");
    if (closePopupWindow1.isDisplayed()) {
      closePopupWindow1.click();
    }
    if (closePopupWindow2.isDisplayed()) {
      closePopupWindow2.click();
    }
  }
}

// RT

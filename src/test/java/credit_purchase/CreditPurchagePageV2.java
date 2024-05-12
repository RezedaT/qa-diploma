package credit_purchase;

import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CreditPurchagePageV2 {

    private final SelenideElement cardNum;
    private final SelenideElement month;
    private final SelenideElement year;
    private final SelenideElement owner;
    private final SelenideElement cvc;
    private final SelenideElement submitButton;
    private final SelenideElement successNotification;
    private final SelenideElement errorNotification;

    public CreditPurchagePageV2() {
        var fields = $$(".input");
        cardNum = fields.get(0);
        month = fields.get(1);
        year = fields.get(2);
        owner = fields.get(3);
        cvc = fields.get(4);
        submitButton = $("form button");
        successNotification = $(".notification_status_ok");
        errorNotification = $(".notification_status_error");
    }

    public void clickSubmit() {
        submitButton.click();
    }

    public CreditPurchagePageV2 setCardNum(String value) {
        cardNum.$("input").setValue(value);
        return this;
    }

    public CreditPurchagePageV2 setMonth(String value) {
        month.$("input").setValue(value);
        return this;
    }

    public CreditPurchagePageV2 setYear(String value) {
        year.$("input").setValue(value);
        return this;
    }

    public CreditPurchagePageV2 setOwner(String value) {
        owner.$("input").setValue(value);
        return this;
    }

    public CreditPurchagePageV2 setCvc(String value) {
        cvc.$("input").setValue(value);
        return this;
    }

    public String getCardNumError() {
        return cardNum.$(".input__sub").exists() ? cardNum.$(".input__sub").text() : null;
    }

    public String getMonthError() {
        return month.$(".input__sub").exists() ? month.$(".input__sub").text() : null;
    }

    public String getYearError() {
        return year.$(".input__sub").exists() ? year.$(".input__sub").text() : null;
    }

    public String getOwnerError() {
        return owner.$(".input__sub").exists() ? owner.$(".input__sub").text() : null;
    }

    public String getCvcError() {
        return cvc.$(".input__sub").exists() ? cvc.$(".input__sub").text() : null;
    }

    public String getSuccessNotificationContent() {
        return successNotification.$(".notification__content").shouldBe(visible, Duration.ofSeconds(15)).text();
    }
    public String getErrorNotificationContent() {
        return errorNotification.$(".notification__content").shouldBe(visible, Duration.ofSeconds(15)).text();
    }

}

package page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;

public class Dashboard {
    private SelenideElement header = $x("//h2[text()='Путешествие дня']");
    private SelenideElement buyButton = $x("//span[text()='Купить']");
    private SelenideElement creditButton = $x("//span[text()='Купить в кредит']");

    public Dashboard() {
        header.shouldBe(visible);
    }

    public DebitPurchase chooseDebitPurchase() {
        buyButton.click();
        return new DebitPurchase();
    }

    public CreditPurchase chooseCreditPurchase() {
        creditButton.click();
        return new CreditPurchase();
    }
}

//RT
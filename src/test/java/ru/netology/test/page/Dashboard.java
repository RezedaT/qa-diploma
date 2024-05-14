package ru.netology.test.page;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;

import com.codeborne.selenide.SelenideElement;
import java.time.Duration;

public class Dashboard {
  private SelenideElement header = $x("//h2[text()='Путешествие дня']");
  private SelenideElement buyButton = $x("//span[text()='Купить']/../..");
  private SelenideElement creditButton = $x("//span[text()='Купить в кредит']/../..");

  public Dashboard() {
    header.shouldBe(visible, Duration.ofSeconds(5));
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

// RT

package ru.netology.test.data;

import com.github.javafaker.Faker;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;

public class DataHelper {
  public static final Faker faker = new Faker(new Locale("en"));
  private static final Faker faker1 = new Faker(new Locale("ru"));

  @Value
  public static class CardData {
    String number;
    String month;
    String year;
    String owner;
    String cvc;

    public static CardData getCardWithParam(
        String number, String month, String year, String owner, String cvc) {
      return new CardData(number, month, year, owner, cvc);
    }

    public static String getNumberApprovedCard() {
      return "4444 4444 4444 4441";
    }

    public static String getNumberDeclinedCard() {
      return "4444 4444 4444 4442";
    }

    public static String generateValidCardExpireMonth() {
      int randomNumber = faker.number().numberBetween(1, 12);
      var result = String.valueOf(randomNumber);
      return StringUtils.leftPad(result, 2, "0");
    }

    public static String generateValidCardExpireYear() {
      int nextYear = Year.now().getValue() + 1;
      int randomYear = faker.number().numberBetween(nextYear, nextYear + 3);
      return String.valueOf(randomYear);
    }

    public static String generateValidCardOwnerName() {
      return faker.name().firstName() + " " + faker.name().lastName();
    }

    public static String generateValidCardOwnerNameRus() {
      return faker1.name().firstName() + " " + faker1.name().lastName();
    }

    public static String generateValidCardCVC() {
      return faker.numerify("###");
    }

    public static String generateDate(int month, String formatPattern) {
      return LocalDate.now().plusMonths(month).format(DateTimeFormatter.ofPattern(formatPattern));
    }

    public static String getPaymentAmount() {
      return "4500000";
    }
  }
}

package ru.netology.test.data;

import com.github.javafaker.Faker;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

public class DataHelper {
  public static final String approvedCardNumber = "4444 4444 4444 4441";
  public static final String declinedCardNumber = "4444 4444 4444 4442";

  private static final Faker faker = new Faker(new Locale("en"));
  private static final Faker faker1 = new Faker(new Locale("ru"));

  public static CardData generateValidCard() {
    return new CardData(
        approvedCardNumber,
        generateValidCardExpireMonth(),
        generateValidCardExpireYear(),
        generateValidCardOwnerName(),
        generateValidCardCVC());
  }

  public static CardData generateDeclinedCard() {
    return new CardData(
        declinedCardNumber,
        generateValidCardExpireMonth(),
        generateValidCardExpireYear(),
        generateValidCardOwnerName(),
        generateValidCardCVC());
  }

  public static String generateRandomCardNumber() {
    String number;
    do {
      number = faker.business().creditCardNumber();
    } while (approvedCardNumber.equals(number) || declinedCardNumber.equals(number));
    return number;
  }

  public static String generateValidCardExpireMonth() {
    int randomNumber = DataHelper.faker.number().numberBetween(1, 12);
    var result = String.valueOf(randomNumber);
    return StringUtils.leftPad(result, 2, "0");
  }

  public static String generateValidCardExpireYear() {
    int nextYear = Year.now().getValue() % 1000 + 1;
    int randomYear = DataHelper.faker.number().numberBetween(nextYear, nextYear + 3);
    return String.valueOf(randomYear);
  }

  public static String generateValidCardOwnerName() {
    return DataHelper.faker.name().firstName() + " " + DataHelper.faker.name().lastName();
  }

  public static String generateValidCardOwnerNameRus() {
    return DataHelper.faker1.name().firstName() + " " + DataHelper.faker1.name().lastName();
  }

  public static String generateValidCardCVC() {
    return DataHelper.faker.numerify("###");
  }

  public static String generateDate(int month, String formatPattern) {
    return LocalDate.now().plusMonths(month).format(DateTimeFormatter.ofPattern(formatPattern));
  }
}

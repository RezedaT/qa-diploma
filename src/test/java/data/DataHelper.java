package data;

import com.github.javafaker.Faker;
import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import lombok.Value;

import java.util.Locale;
import org.apache.commons.lang3.StringUtils;

public class DataHelper {
    private static final Faker faker = new Faker(new Locale("en"));
    private static final Faker faker1 = new Faker(new Locale("ru"));

//    private DataHelper() {
//    }

    @Value
    public static class CardData {
        String number;
        String holder;
        String cvc;
        String month;
        String year;

        public static CardData getCardWithParam(String number, String holder, String cvc, String month, String year) {
            return new CardData(number, holder, cvc, month, year);
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
            int randomYear = faker.number().numberBetween(24, 28);
            return String.valueOf(randomYear);
        }

        public static String generateValidCardOwnerName() {
            return faker.name().firstName() + " " + faker.name().lastName();
        }

        public static String generateValidCardCVV() {
            return faker.numerify("###");
        }

        public static String getPaymentAmount() {
            return "4500000";
        }

//        public static List<DataJsonItem> getDataJsonItems(String fileName) throws IOException {
//            var mapper = new ObjectMapper();
//            return mapper.readValue(new File(fileName), new TypeReference<>() {
//            });
//        }

//        @Value
//        public static class DataJsonItem {
//            String number;
//            String status;
//
//            public DataJsonItem(
//                    @JsonProperty("number") String cardNumber,
//                    @JsonProperty("status") String cardStatus) {
//                this.number = number;
//                this.status = status;
//            }
//        }
    }
}



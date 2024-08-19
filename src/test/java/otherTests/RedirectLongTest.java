package otherTests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RedirectLongTest {
    @Test
    @DisplayName("Долгий редирект")
    public void getLongRedirect() {
        Response responseFirst = RestAssured
                .given()
                .redirects()
                .follow(false)
                .when()
                .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();

        String firstLocationUrl = responseFirst.getHeader("Location");
        int firstStatusCode = responseFirst.statusCode();
        System.out.println("Переход по первому редиректу. URL: " + firstLocationUrl);

        if (firstStatusCode != 301) {
            System.out.println("Редиректа нет");
        } else {
            Response responseSecond = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .when()
                    .get(firstLocationUrl)
                    .andReturn();

            String secondLocationUrl = responseSecond.getHeader("Location");
            int secondStatusCode = responseSecond.statusCode();
            System.out.println("Переход по второму редиректу. URL: " + secondLocationUrl);

            if (secondStatusCode != 301) {
                System.out.println("Количество редиректов - 1");
            } else {
                Response responseThird = RestAssured
                        .given()
                        .redirects()
                        .follow(false)
                        .when()
                        .get(secondLocationUrl)
                        .andReturn();


                String thirdLocationUrl = responseThird.getHeader("Location");
                int thirdStatusCode = responseThird.getStatusCode();
                System.out.println("Переход по третьему редиректу. URL: " + thirdLocationUrl);

                if (thirdStatusCode != 301) {
                    System.out.println("Количество редиректов: 2");
                } else {
                    Response responseFourth = RestAssured
                            .given()
                            .redirects()
                            .follow(false)
                            .when()
                            .get(thirdLocationUrl)
                            .andReturn();

                    String fourthLocationUrl = responseFourth.getHeader("Location");
                    int fourthStatusCode = responseFourth.getStatusCode();
                    System.out.println("Переход по четвертому редиректу. URL: " + fourthLocationUrl);

                    if (fourthStatusCode != 301) {
                        System.out.println("Количество редиректов - 3");
                    }
                }
            }
        }
    }
}


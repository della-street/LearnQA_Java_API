package otherTests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RedirectTest {
    @Test
    @DisplayName("Получение адреса первого редиректа")
    public void getRedirect() {
        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .when()
                .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();

        String redirect = response.getHeader("Location");
        System.out.println(redirect);


    }
}

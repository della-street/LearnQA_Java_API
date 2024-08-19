package otherTests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetCookieTest {
    @Test
    @DisplayName("Получение cookies")
    public void getCookie() {
        Response responseWithCookie = RestAssured
                .get(" https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();

        String cookie = responseWithCookie.getCookie("HomeWork");
        String expectedCookie = "hw_value";
        assertEquals(expectedCookie, cookie, "The cookie is unexpected: " + cookie);
    }
}

package otherTests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetCookieTest {
    @Test
    @DisplayName("Получение cookies")
    public void getCookie() {
        Response responseWithCookie = RestAssured
                .get(" https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();

        Map<String, String> cookies = responseWithCookie.getCookies();
        String expectedCookieValue = "hw_value";
        assertTrue(cookies.containsValue(expectedCookieValue), "Response doesn't have cookie " + expectedCookieValue);
    }
}

package otherTests;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetHeaderTest {
    @Test
    @DisplayName("Получение хэдера")
    public void getHeader() {
        Response responseWithHeader = RestAssured
                .get("https://playground.learnqa.ru/api/homework_header")
                .andReturn();

        Headers headers = responseWithHeader.getHeaders();
        String expectedHeaderValue = "x-secret-homework-header";

        assertTrue(headers.hasHeaderWithName(expectedHeaderValue), "Response doesn't have header " + expectedHeaderValue);

    }
}

package otherTests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestRestAssuredJUnit {
    /* @Test
     @DisplayName("Ассерт")
     public void testRestAssured() {
         Response response = RestAssured
                 .get("https://playground.learnqa.ru/api/map")
                 .andReturn();

         assertTrue(response.statusCode() == 201, "Unexpected status code");
     }

     @Test
     @DisplayName("Assert Equals")
     public void testRestAssured() {
         Response response = RestAssured
                 .get("https://playground.learnqa.ru/api/map")
                 .andReturn();

         assertEquals(200, response.statusCode(), "Unexpected status code");

     */
    @ParameterizedTest
    @ValueSource(strings = {"", "John", "Pete"})
    @DisplayName("Параметризованные тесты")
    public void testHelloMethodWithoutName(String name) {
        Map<String, String> queryParams = new HashMap<>();

        if (name.length() > 0) {
            queryParams.put("name", name);
        }
        JsonPath response = RestAssured
                .given()
                .queryParams(queryParams)
                .get("https://playground.learnqa.ru/api/hello")
                .jsonPath();

        String answer = response.getString("answer");
        String expectedName = (name.length()>0) ? name : "someone";
        assertEquals("Hello, " + expectedName, answer, "The answer is not expected");
    }


}


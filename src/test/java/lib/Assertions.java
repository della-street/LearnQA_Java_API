package lib;

import io.restassured.response.Response;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Assertions {
    public static void assertJsonByName(Response Response, String name, int expectedValue) {
        Response.then().assertThat().body("$", hasKey(name));

        int value = Response.jsonPath().getInt(name);
        assertEquals(expectedValue, value, "JsonValue isn't equal to expected value");
    }

    public static void assertJsonByName(Response Response, String name, String expectedValue) {
        Response.then().assertThat().body("$", hasKey(name));

        String value = Response.jsonPath().getString(name);
        assertEquals(expectedValue, value, "JsonValue isn't equal to expected value");
    }

    /*Проверка на соответствие ожидаемого и фактического ответа сервера*/
    public static void assertResponseTextEquals(Response Response, String expectedAnswer) {
        assertEquals(
                expectedAnswer,
                Response.asString(),
                "Response text isn't as expected"
        );
    }

    /*Проверка на то, что часть ответа сервера совпадает с ожидаемым ответом*/
    public static void assertResponseTextContains(Response Response, String expectedAnswer) {
        assertTrue(
                Response.asString().contains(expectedAnswer),
                "Response text doesn't contain expected answer"
        );
    }

    /*Проверка на соответствие ожидаемого и фактического статус-кода*/
    public static void assertResponseCodeEquals(Response Response, int expectedStatusCode) {
        assertEquals(
                expectedStatusCode,
                Response.statusCode(),
                "Status Code text isn't as expected"
        );
    }

    /*Проверка, что в Json ответа есть нужное поле*/
    public static void assertJsonHasField(Response Response, String expectedFieldName) {
        Response.then().assertThat().body("$", hasKey(expectedFieldName));
    }

    /*Проверка, что в Json ответа есть несколько нужных полей*/
    public static void assertJsonHasFields(Response Response, String[] expectedFieldNames) {
        for (String expectedFieldName : expectedFieldNames) {
            Assertions.assertJsonHasField(Response, expectedFieldName);
        }
    }

        /*Проверка, что в Json ответа отсутствуют некоторые поля*/
        public static void assertJsonHasNotField (Response Response, String unexpectedFieldName){
            Response.then().assertThat().body("$", not(hasKey(unexpectedFieldName)));
        }
    }






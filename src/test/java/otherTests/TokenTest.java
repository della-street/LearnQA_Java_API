package otherTests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class TokenTest {
    @Test
    @DisplayName("Токены")
    public void makeTaskWithToken() throws InterruptedException {

        JsonPath getToken = RestAssured
                .when()
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();

        String token = getToken.get("token");

        Map<String, String> params = new HashMap<>();
        params.put("token", token);

        JsonPath response = RestAssured
                .given()
                .queryParams(params)
                .when()
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();

        String status = response.get("Status");
        String statusJobIsReady = "Job is ready";

        if (status != statusJobIsReady) {
            Thread.sleep(17000);

            JsonPath responseForJobIsDone = RestAssured
                    .given()
                    .queryParams(params)
                    .when()
                    .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                    .jsonPath();

            String result = responseForJobIsDone.get("result");
            String statusForJobDone = responseForJobIsDone.get("status");

            if (result != null) {
                System.out.println("Статус задачи: " + statusForJobDone + ". Поле result присутствует.");
            }
        }
    }
}

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GetJsonTest {
    @Test
    @DisplayName("Парсинг JSON")
    public void getJson() {
        JsonPath json = RestAssured
                .given()
                .when()
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();

        String answer = json.get("messages[1].message");
        System.out.println(answer);
    }
}

import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class TestPlaygroundLearnQA {
    /* @Test
     public void testRestAssured() {
         Map<String, String> params = new HashMap<>();
         params.put("name", "Liz");

         Response response = RestAssured
                 .given()
                 .queryParams(params)
                 .get("https://playground.learnqa.ru/api/hello")
                 .andReturn();
         response.prettyPrint();
     }

     */
   /* @Test
    public void testRestAssured() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "Liz");

        JsonPath response = RestAssured
                .given()
                .queryParams(params)
                .get("https://playground.learnqa.ru/api/hello")
                .jsonPath();

        String name = response.get("answer2");
        if (name == null) {
            System.out.println("Key \"answer2\" is absent");
        } else {
            System.out.println(name);
        }
    }*/

    @Test
    @DisplayName("Отправка post-запроса с параметрами в теле запроса")
    public void testRestAssured() {
        Map<String, Object> body = new HashMap<>();
        body.put("param1", "value1");
        body.put("param2", "value2");
        Response response = RestAssured
                .given()
                /*.body("param1=value1&param2=value2") - так можно передать параметры строкой. А можно json - см. дальше*/
                /*.body("{\"param1\":\"value1\",\"param2\":\"value2\"}") - а можно json положить в коллекцию, см. дальше*/
                .body(body)
                .post("https://playground.learnqa.ru/api/check_type")
                .andReturn();
        response.print();
    }
}

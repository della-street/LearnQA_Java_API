package otherTests;

import io.restassured.http.Headers;
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
   @DisplayName("Передача параметров в массиве/коллекции")
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

   /*
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
    /*
                .body(body)
                .post("https://playground.learnqa.ru/api/check_type")
                .andReturn();
        response.print();
    }*/


    /*@Test
    @DisplayName("Получение статус-кода")
    public void testRestAssured() {

        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .when()
                .get("https://playground.learnqa.ru/api/get_303")
                .andReturn();
        int statusCode = response.getStatusCode();
        System.out.println(statusCode);

    }*/

   /* @Test
    @DisplayName("Работа с заголовками")
    public void testRestAssured() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("MyHeader1", "myValue1");
        headers.put("MyHeader2", "myValue2");

        Response response = RestAssured
                .given()
                .when()
                .get("https://playground.learnqa.ru/api/get_303")
                .andReturn();

        response.prettyPrint();

        String locationHeader = response.getHeader("Location");
        System.out.println(locationHeader);
    }
*/
    /*@Test
    @DisplayName("Куки. Авторизация")
    public void testRestAssured() {
        HashMap<String, String> data = new HashMap<>();
        data.put("login", "secret_login");
        data.put("password", "secret_pass");

        Response response = RestAssured
                .given()
                .body(data)
                .when()
                .post("https://playground.learnqa.ru/api/get_auth_cookie")
                .andReturn();

        System.out.println("\nPretty text:");
        response.prettyPrint();

        System.out.println("\nHeaders:");
        Headers responseHeaders = response.getHeaders();
        System.out.println(responseHeaders);

        System.out.println("\nCookies:");
        Map<String, String> responseCookies = response.getCookies();
        System.out.println(responseCookies);
        */

    @Test
    @DisplayName("Авторизация. Передача кук")
    public void testRestAssured() {
        HashMap<String, String> data = new HashMap<>();
        data.put("login", "secret_login");
        data.put("password", "secret_pass");

        Response responseForGet = RestAssured
                .given()
                .body(data)
                .when()
                .post("https://playground.learnqa.ru/api/get_auth_cookie")
                .andReturn();

        String responseForCookie = responseForGet.getCookie("auth_cookie");

        Map<String, String> cookie = new HashMap<>();
        if(responseForCookie != null) {
            cookie.put("auth_cookie", responseForCookie);
        }

        Response responseForCheck = RestAssured
                .given()
                .body(data)
                .cookies(cookie)
                .when()
                .post("https://playground.learnqa.ru/api/check_auth_cookie")
                .andReturn();


    }
}

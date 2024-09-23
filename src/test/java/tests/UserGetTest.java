package tests;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserGetTest extends BaseTestCase {

    private ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    @DisplayName("Получаем данные пользователя, не будучи залогиненными в систему. Проверяем, что виден только один параметр - юзернейм. Остальные - не видно")
    public void testGetUserDataNotAuth() {
        Response responseUserData = RestAssured
                .get("https://playground.learnqa.ru/api/user/2")
                .andReturn();

        Assertions.assertJsonHasField(responseUserData, "username");
        Assertions.assertJsonHasNotField(responseUserData, "firstName");
        Assertions.assertJsonHasNotField(responseUserData, "lastName");
        Assertions.assertJsonHasNotField(responseUserData, "email");
    }

    @Test
    @DisplayName("Получение данных профиля юзера, будучи залогиненным под этим же юзером")
    public void testGetUserDetailsAuthAsSameUser() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");
        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");

        Response responseUserData = RestAssured
                .given()
                .header("x-csrf-token", header)
                .cookie("auth_sid", cookie)
                .get("https://playground.learnqa.ru/api/user/2/")
                .andReturn();

        String[] expectedFields = {"username", "firstName", "lastName", "email"};
        Assertions.assertJsonHasFields(responseUserData, expectedFields);
    }

    @Test
    @Description("Получение данных одного пользователя из-под логина другим пользователем")
    @DisplayName("Получение данных другого пользователя")
    public void testGetAnotherUserDetails() {

        //Логин под пользователем с ID=2
        Response responseGetAuthData = apiCoreRequests.makePostRequest(
                        "https://playground.learnqa.ru/api/user/login",
                        DataGenerator.getLoginData())
                .andReturn();

        String token = this.getHeader(responseGetAuthData, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuthData, "auth_sid");

        //Получение данных пользователя с ID=3
        Response getAnotherUserData = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/3", token, cookie)
                .andReturn();

        Assertions.assertJsonHasField(getAnotherUserData, "username");
        Assertions.assertJsonByName(getAnotherUserData, "username", "arsbatyrov");
        Assertions.assertJsonHasNotField(getAnotherUserData, "id");
        Assertions.assertJsonHasNotField(getAnotherUserData, "email");
        Assertions.assertJsonHasNotField(getAnotherUserData, "firstName");
        Assertions.assertJsonHasNotField(getAnotherUserData, "lastName");
    }

}

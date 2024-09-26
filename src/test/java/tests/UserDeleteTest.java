package tests;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import lib.ApiCoreRequests;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;


public class UserDeleteTest extends BaseTestCase {
    ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    @Description("Удаление пользователя с ID = 2, будучи залогиненным под этим пользователем")
    public void testDeleteExistingUser() {
        //Авторизация под пользователем с ID = 2
        Response login = apiCoreRequests.makePostRequest(
                "https://playground.learnqa.ru/api/user/login",
                DataGenerator.getLoginData()
        );

        String token = login.getHeader("x-csrf-token");
        String cookie = login.getCookie("auth_sid");

        //Удаление пользователя с ID = 2
        String userId = "2";
        Response deleteUser = apiCoreRequests.makeDeleteRequest(
                " https://playground.learnqa.ru/api/user/" + userId,
                token,
                cookie);

        Assertions.assertResponseTextContains(deleteUser, "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");
    }

    @Test
    @DisplayName("Создание и удаление пользователя")
    @Description("Создание и удаление пользователя с проверкой его удаления")
    public void testCreateAndDeleteUser(){
        //Создание пользователя
        Map<String, String> userData = DataGenerator.getRegistrationData();
        JsonPath createUser = apiCoreRequests.makePostRequestToCreateUserWithJsonAnswer(
                "https://playground.learnqa.ru/api/user/",
                userData
        );

        String userId = createUser.getString("id");

        //Логин
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests.makePostRequest(
                "https://playground.learnqa.ru/api/user/login",
                authData);
        String cookie = responseGetAuth.getCookie("auth_sid");
        String token = responseGetAuth.getHeader("x-csrf-token");

        //Удаление пользователя
        Response deleteUser = apiCoreRequests.makeDeleteRequest(
                "https://playground.learnqa.ru/api/user/" + userId,
                token,
                cookie);

        //Получение данных пользователя
        Response getUserData = apiCoreRequests.makeGetRequest(
                "https://playground.learnqa.ru/api/user/" + userId,
                token,
                cookie
        );

        Assertions.assertResponseTextEquals(getUserData, "User not found");
    }

    @Test
    @DisplayName("Удаление пользователя с авторизацией под другим пользователем")
    @Description("Удаление пользователя, будучи авторизованным под другим пользователем")
    public void testDeleteAnotherUser(){
        //Создание пользователя
        Map<String, String> userData = DataGenerator.getRegistrationData();
        JsonPath createUser = apiCoreRequests.makePostRequestToCreateUserWithJsonAnswer(
                "https://playground.learnqa.ru/api/user/",
                userData
        );

        String userId = createUser.getString("id");

        //Логин
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests.makePostRequest(
                "https://playground.learnqa.ru/api/user/login",
                authData);
        String cookie = responseGetAuth.getCookie("auth_sid");
        String token = responseGetAuth.getHeader("x-csrf-token");

        //Удаление пользователя
        Response deleteUser = apiCoreRequests.makeDeleteRequest(
                "https://playground.learnqa.ru/api/user/" + userId + "1",
                token,
                cookie);
        Assertions.assertResponseTextContains(deleteUser, "This user can only delete their own account.");
    }
}

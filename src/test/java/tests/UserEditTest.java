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

public class UserEditTest extends BaseTestCase {
    ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    public void testEditJustCreatedTest() {
        //Generate user
        Map<String, String> userData = DataGenerator.getRegistrationData();

        JsonPath resposeCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .jsonPath();

        String userId = resposeCreateAuth.getString("id");

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        //EDIT USER
        String newName = "ChangedName";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = RestAssured
                .given()
                .header("x-csrf-token", this.getHeader(responseGetAuth, "x-csrf-token"))
                .cookie("auth_sid", this.getCookie(responseGetAuth, "auth_sid"))
                .body(editData)
                .put("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();

        //GET
        Response responseUserData = RestAssured
                .given()
                .header("x-csrf-token", this.getHeader(responseGetAuth, "x-csrf-token"))
                .cookie("auth_sid", this.getCookie(responseGetAuth, "auth_sid"))
                .get("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();

        Assertions.assertJsonByName(responseUserData, "firstName", newName);
    }

    @Test
    @Description("Изменение данных пользователя без авторизации")
    public void testEditUserDataWithoutAuthorization() {
        String newName = "ChangedName";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response editDataWithoutAuthorization = apiCoreRequests.makePutRequest(
                "https://playground.learnqa.ru/api/user/3",
                "x-csrf-token",
                "auth_sid",
                editData
        );
        Assertions.assertResponseTextContains(editDataWithoutAuthorization, "Auth token not supplied");
    }

    @Test
    @Description("Изменение данных пользователя с авторизацией под другим пользователем")
    public void testEditAnotherUserData() {
        //Создание пользователя
        Response createUser = apiCoreRequests.makePostRequestToCreateUser(
                "https://playground.learnqa.ru/api/user/",
                DataGenerator.getRegistrationData()
        );

        String firstUserToken = createUser.getHeader("x-csrf-token");
        String firstUserCookie = createUser.getCookie("auth_sid");

        //Редактирование пользователя с ID = 3
        String newName = "ChangedName";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response editAnotherUserData = apiCoreRequests.makePutRequest(
                "https://playground.learnqa.ru/api/user/3",
                firstUserToken,
                firstUserCookie,
                editData
        );

        Assertions.assertResponseTextContains(editAnotherUserData, "Auth token not supplied");
    }

    @Test
    @Description("Изменение email пользователя, будучи авторизованными тем же пользователем, на новый email без символа @")
    public void testEditUserByAddingInvalidEmail(){
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

        //Изменение емейла на невалидный
        String newEmail = "testtest.com";
        Map<String, String> editData = new HashMap<>();
        editData.put("email", newEmail);

        Response editAnotherUserData = apiCoreRequests.makePutRequest(
                "https://playground.learnqa.ru/api/user/" + userId,
                token,
                cookie,
                editData
        );

        Assertions.assertResponseTextContains(editAnotherUserData, "Invalid email format");
    }

    @Test
    @Description("Изменение имени пользователя, будучи авторизованными тем же пользователем, на новый на имя длиной в 1 символ")
    public void testEditUserByAddingShortFirstName(){
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

        //Изменение емейла на невалидный
        String newFirstName = "a";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newFirstName);

        Response editAnotherUserData = apiCoreRequests.makePutRequest(
                "https://playground.learnqa.ru/api/user/" + userId,
                token,
                cookie,
                editData
        );

        Assertions.assertResponseTextContains(editAnotherUserData, "The value for field `firstName` is too short");
    }
}

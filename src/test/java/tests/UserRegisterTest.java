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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserRegisterTest extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    @Test
    public void createUserWithExistingEmail() {
        String email = "vinkotov@example.com";

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();

        Assertions.assertResponseTextEquals(responseCreateAuth, "Users with email '" + email + "' already exists");
        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
    }


    @Test
    public void createUserSuccessfully() {
        String email = DataGenerator.getRandomEmail();

        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();

        Assertions.assertResponseCodeEquals(responseCreateAuth, 200);
        Assertions.assertJsonHasField(responseCreateAuth, "id");
    }

    @Test
    @Description("This test creates user with wrong e-mail without @")
    @DisplayName("Creating user with e-mail w/o @")
    public void createUserWithWrongEmail() {
        String email = "testtest.com";

        Map<String, String> registerData = new HashMap<>();
        registerData.put("email", email);
        registerData = DataGenerator.getRegistrationData(registerData);

        Response responseCreateUserWithWrongEmail = apiCoreRequests.
                makePostRequestToCreateUser("https://playground.learnqa.ru/api/user/", registerData);

        String expectedResult = "Invalid email format";
        Assertions.assertResponseTextEquals(responseCreateUserWithWrongEmail, expectedResult);
   }
}

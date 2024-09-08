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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    @Test
    @Description("This test creates user with short first name")
    @DisplayName("Creating user with short first name")
    public void createUserWithShortName() {
        String firstName = "a";

        Map<String, String> registerData = new HashMap<>();
        registerData.put("firstName", firstName);
        registerData = DataGenerator.getRegistrationData(registerData);

        Response responseCreateUserWithShortName = apiCoreRequests.
                makePostRequestToCreateUser("https://playground.learnqa.ru/api/user/", registerData);

        String expectedResult = "The value of 'firstName' field is too short";

        Assertions.assertResponseTextEquals(responseCreateUserWithShortName, expectedResult);
    }


    @Test
    @Description("This test creates user with long first name")
    @DisplayName("Creating user with long first name")
    public void createUserWithLongName() {
        String firstName = "мммммммммммммммммммммммммммммм" +
                "мммммммммммммммммммммммммммммммммммммммм" +
                "ммммммммsddffffffffffffffffffff" +
                "fffffffffffffffffffffffffffffffffff" +
                "ffffffffffffffffffffffffffffffffffff" +
                "fffffffffffffffffffffffffffffffffff" +
                "ffffffffffffffffffffffffffffffffffff" +
                "fffffffffffffffffffffffff";

        Map<String, String> registerData = new HashMap<>();
        registerData.put("firstName", firstName);
        registerData = DataGenerator.getRegistrationData(registerData);

        Response responseCreateUserWithLongName = apiCoreRequests.
                makePostRequestToCreateUser("https://playground.learnqa.ru/api/user/", registerData);

        String expectedResult = "The value of 'firstName' field is too long";

        Assertions.assertResponseTextEquals(responseCreateUserWithLongName, expectedResult);
    }

    public static List<Map<String, String>> getDataWithEmptyParameters() {
        List<Map <String, String>> hashMaps = new ArrayList<>();

        Map<String, String> dataWithoutEmail = DataGenerator.getRegistrationDataWithoutEmail();
        hashMaps.add(dataWithoutEmail);

        Map<String, String> dataWithoutPassword = DataGenerator.getRegistrationDataWithoutPassword();
        hashMaps.add(dataWithoutPassword);

        Map<String, String> dataWithoutUserName = DataGenerator.getRegistrationDataWithoutUserName();
        hashMaps.add(dataWithoutUserName);

        Map<String, String> dataWithoutFirstName = DataGenerator.getRegistrationDataWithoutFirstName();
        hashMaps.add(dataWithoutFirstName);

        Map<String, String> dataWithoutLastName = DataGenerator.getRegistrationDataWithoutLastName();
        hashMaps.add(dataWithoutLastName);

        return hashMaps;
    }

    @ParameterizedTest
    @Description("User creating with empty parameters in register data")
    @DisplayName("User creating with empty register data")
    @MethodSource("getDataWithEmptyParameters")
    public void testCreateUserWithEmptyRegisterData(Map<String, String> registerData) {
        Response responseCreateUserWithEmptyRegisterParameters = apiCoreRequests.
                makePostRequestToCreateUser("https://playground.learnqa.ru/api/user/", registerData);
        System.out.println(responseCreateUserWithEmptyRegisterParameters.asString());

        String expectedAnswer = "The following required params are missed";
        Assertions.assertResponseTextContains(responseCreateUserWithEmptyRegisterParameters, expectedAnswer);

    }
}

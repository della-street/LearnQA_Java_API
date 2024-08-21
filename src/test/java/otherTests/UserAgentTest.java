package otherTests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class UserAgentTest {
    @ParameterizedTest
    @ValueSource(strings = {"Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30",
            "Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.77 Mobile/15E148 Safari/604.1",
            "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.100.0",
            "Mozilla/5.0 (iPad; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1"
    })
    @DisplayName("Получение параметров из User-Agent")
    public void getUserAgentParameters(String userAgent) {
        Map<String, String> headerUserAgent = new HashMap<>();
        headerUserAgent.put("User-Agent", userAgent);
        JsonPath responseJson = RestAssured
                .given()
                .headers(headerUserAgent)
                .get("https://playground.learnqa.ru/ajax/api/user_agent_check")
                .jsonPath();

        /*Получение переменных для параметров из User-Agent из ответа сервера*/
        String platform = responseJson.getString("platform");
        String device = responseJson.getString("device");
        String browser = responseJson.getString("browser");

        /*Ожидаемые User-Agent и их параметры*/
        Map<String, Map<String, String>> expectedValuesOfUserAgent = Map.of(
                "Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30", Map.of("platform", "Mobile", "browser", "No", "device", "Android"),
                "Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.77 Mobile/15E148 Safari/604.1", Map.of("platform", "Mobile", "browser", "Chrome", "device", "iOS"),
                "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)", Map.of("platform", "Googlebot", "browser", "Unknown", "device", "Unknown"),
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.100.0", Map.of("platform", "Web", "browser", "Chrome", "device", "No"),
                "Mozilla/5.0 (iPad; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1", Map.of("platform", "Mobile", "browser", "No", "device", "iPhone"
                ));

        Map<String, String> expectedValues = expectedValuesOfUserAgent.get(userAgent);
        String expectedDevice = expectedValues.get("device");
        String expectedBrowser = expectedValues.get("browser");
        String expectedPlatform = expectedValues.get("platform");

        assertEquals(expectedPlatform, platform, "Wrong platform");
        assertEquals(expectedBrowser, browser, "Wrong browser");
        assertEquals(expectedDevice, device, "Wrong device");
    }
}


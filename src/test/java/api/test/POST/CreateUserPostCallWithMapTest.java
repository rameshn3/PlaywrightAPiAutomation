package api.test.POST;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CreateUserPostCallWithMapTest {

    private static String emailId;
    Playwright playwright;
    APIRequest request;
    APIRequestContext apiRequestContext;

    @BeforeTest
    public void setup() {
        playwright = Playwright.create();
        request = playwright.request();
        apiRequestContext = request.newContext();
    }

    @AfterTest
    public void tearDown() {
        playwright.close();
    }

    public static String getRandomEmail() {
        emailId = "rameshqapwapi" + System.currentTimeMillis() + "@gmail.com";
        return emailId;
    }

    @Test
    public void createUserPostCallThroughMapTest() throws IOException {

        Map<String, Object> postreqBody = new HashMap<>();
        postreqBody.put("name", "Ramesh QA");
        postreqBody.put("email", getRandomEmail());
        postreqBody.put("gender", "male");
        postreqBody.put("status", "active");

        APIResponse apiPostResponse = apiRequestContext.post("https://gorest.co.in/public/v2/users", RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setHeader("Authorization", "Bearer a9e39b8c371a1459ef03571b260f045c4a951ca6a5a9b71da5db39b0d9e8aba1")
                .setData(postreqBody));

        System.out.println(apiPostResponse.status() + " : " + apiPostResponse.statusText());

        if (apiPostResponse.status() == 422) {
            // Log the response body for debugging
            System.out.println("Error Response Body: " + apiPostResponse.text());
        }

        Assert.assertEquals(apiPostResponse.status(), 201, "Expected HTTP status 201 Created");
        Assert.assertEquals(apiPostResponse.statusText(), "Created");

        String apiresponseText = apiPostResponse.text();
        System.out.println(apiresponseText);

        // Print API response in pretty format
        System.out.println("Print API response in pretty format");
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonResponse = objectMapper.readTree(apiPostResponse.body());
        String jsonPrettyResponse = jsonResponse.toPrettyString();
        System.out.println(jsonPrettyResponse);

        // Get the ID from the response
        String userId = jsonResponse.get("id").asText();
        System.out.println("User ID: " + userId);

        // GET: Call to fetch the same user by ID
        APIResponse apiGETResponse = apiRequestContext.get("https://gorest.co.in/public/v2/users/" + userId, RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setHeader("Authorization", "Bearer a9e39b8c371a1459ef03571b260f045c4a951ca6a5a9b71da5db39b0d9e8aba1"));

        System.out.println(apiGETResponse.status() + " : " + apiGETResponse.statusText());
        Assert.assertEquals(apiGETResponse.status(), 200);
        Assert.assertEquals(apiGETResponse.statusText(), "OK");

        // Print the response text
        String responseText = apiGETResponse.text();
        System.out.println(responseText);

        // Print the response body in pretty format
        ObjectMapper objectMapper1 = new ObjectMapper();
        JsonNode jsonGetResponse = objectMapper1.readTree(apiGETResponse.body());
        String jsonGetResponsePretty = jsonGetResponse.toPrettyString();
        System.out.println(jsonGetResponsePretty);

        Assert.assertTrue(apiGETResponse.text().contains(userId));
        Assert.assertTrue(apiGETResponse.text().contains("Ramesh QA"));
        Assert.assertTrue(apiGETResponse.text().contains(emailId));
    }
}

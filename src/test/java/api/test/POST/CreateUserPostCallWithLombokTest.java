package api.test.POST;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.api.data.User;
import org.api.data.Users;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.io.IOException;

public class CreateUserPostCallWithLombokTest {

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
    public void createUserPostCallWithLombokTest() throws IOException {
        Users users = Users.builder()
                .name("Chennu Ramesh")
                .email(getRandomEmail())
                .gender("male")
                .status("active")
                .build();

      ObjectMapper objectMapper = new ObjectMapper();
//        String requestBody = objectMapper.writeValueAsString(users);

        APIResponse apiPostResponse = apiRequestContext.post("https://gorest.co.in/public/v2/users", RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setHeader("Authorization", "Bearer a9e39b8c371a1459ef03571b260f045c4a951ca6a5a9b71da5db39b0d9e8aba1")
                .setData(users));

        System.out.println(apiPostResponse.status() + " : " + apiPostResponse.statusText());

        if (apiPostResponse.status() == 422) {
            // Log the response body for debugging
            System.out.println("Error Response Body: " + apiPostResponse.text());
        }

        Assert.assertEquals(apiPostResponse.status(), 201, "Expected HTTP status 201 Created");
        Assert.assertEquals(apiPostResponse.statusText(), "Created");

        String apiresponseText = apiPostResponse.text();
        System.out.println(apiresponseText);

        // Convert response text/json to POJO -- deserialization
        Users actUser = objectMapper.readValue(apiresponseText, Users.class);
        System.out.println("Actual user from the response---->");
        System.out.println(actUser);

        // Get the ID from the response
        String userId = actUser.getId();
        System.out.println("User ID: " + userId);

        Assert.assertEquals(actUser.getName(), users.getName());
        Assert.assertEquals(actUser.getEmail(), users.getEmail());
        Assert.assertEquals(actUser.getStatus(), users.getStatus());
        Assert.assertEquals(actUser.getGender(), users.getGender());
        Assert.assertNotNull(actUser.getId());
    }
}

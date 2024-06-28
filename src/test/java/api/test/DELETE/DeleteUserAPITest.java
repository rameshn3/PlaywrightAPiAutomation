package api.test.DELETE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.api.data.Users;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;

public class DeleteUserAPITest {

    //1. post - user id = 123
    //2. Delete - user id - /123
    //3. get -- user id /123
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
    public void deleteUserDeleteCallWithLombokTest() throws IOException {
        Users users = Users.builder()
                .name("Chennuboina Ramesh")
                .email(getRandomEmail())
                .gender("male")
                .status("active")
                .build();



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
        ObjectMapper objectMapper = new ObjectMapper();
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

        System.out.println("---------------DELETE CALL----------------");
        //2. Delete Call - delete user:
        APIResponse apideleteResponse = apiRequestContext.delete("https://gorest.co.in/public/v2/users/"+userId, RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setHeader("Authorization", "Bearer a9e39b8c371a1459ef03571b260f045c4a951ca6a5a9b71da5db39b0d9e8aba1"));


        System.out.println(apideleteResponse.status() + " : " + apideleteResponse.statusText());
        System.out.println(apideleteResponse.url());
        if (apideleteResponse.status() == 422) {
            // Log the response body for debugging
            System.out.println("Error Response Body: " + apideleteResponse.text());
        }

        Assert.assertEquals(apideleteResponse.status(), 204, "Expected HTTP status 204 No Content");
        Assert.assertEquals(apideleteResponse.statusText(), "No Content");

        String apideleteresponseText = apideleteResponse.text();
        System.out.println(apideleteresponseText);


        //GET - user
        //3. Get the deleted user with GET CALL:
        APIResponse apiGETResponse = apiRequestContext.get("https://gorest.co.in/public/v2/users/" + userId, RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setHeader("Authorization", "Bearer a9e39b8c371a1459ef03571b260f045c4a951ca6a5a9b71da5db39b0d9e8aba1"));

        System.out.println(apiGETResponse.status() + " : " + apiGETResponse.statusText());
        Assert.assertEquals(apiGETResponse.status(), 404);
        Assert.assertEquals(apiGETResponse.statusText(), "Not Found");

        // Print the response text
        String getresponseText = apiGETResponse.text();
        System.out.println(getresponseText);

        Assert.assertTrue(getresponseText.contains("Resource not found"));

    }
}

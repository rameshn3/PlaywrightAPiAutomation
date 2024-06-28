package api.test.PUT;

import com.fasterxml.jackson.databind.JsonNode;
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

public class UpdateUserPutCallWithLombokTest {

    //1. post - user id = 123
    //2. put - user id - /123
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
    public void updateUserPutCallWithLombokTest() throws IOException {
        Users users = Users.builder()
                .name("Chennu Ramesh")
                .email(getRandomEmail())
                .gender("male")
                .status("active")
                .build();


//Step1: Create a User with POST
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

       //Step2:update status active to inactive
        users.setName("Ramesh QA Automation Platform");
        users.setStatus("inactive");
        System.out.println("---------------PUT CALL----------------");
        //2. PUT Call - update user:
        APIResponse apiPutResponse = apiRequestContext.put("https://gorest.co.in/public/v2/users/"+userId, RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setHeader("Authorization", "Bearer a9e39b8c371a1459ef03571b260f045c4a951ca6a5a9b71da5db39b0d9e8aba1")
                .setData(users));

        System.out.println(apiPutResponse.status() + " : " + apiPutResponse.statusText());
        System.out.println(apiPutResponse.url());
        if (apiPutResponse.status() == 422) {
            // Log the response body for debugging
            System.out.println("Error Response Body: " + apiPutResponse.text());
        }

        Assert.assertEquals(apiPutResponse.status(), 200, "Expected HTTP status 200 Created");
        Assert.assertEquals(apiPutResponse.statusText(), "OK");

        String apiPutresponseText = apiPutResponse.text();
        System.out.println(apiPutresponseText);

        // Convert response text/json to POJO -- deserialization
        ObjectMapper objectMapper1 = new ObjectMapper();
        Users actPutUser = objectMapper1.readValue(apiPutresponseText, Users.class);
        System.out.println("Actual user from the response---->");
        System.out.println(actPutUser);

        Assert.assertEquals(actPutUser.getName(), users.getName());
        Assert.assertEquals(actPutUser.getEmail(), users.getEmail());
        Assert.assertEquals(actPutUser.getStatus(), users.getStatus());
        Assert.assertEquals(actPutUser.getGender(), users.getGender());
        Assert.assertEquals(actPutUser.getId(),userId);
        Assert.assertNotNull(actPutUser.getId());
        System.out.println("---------------GET CALL----------------");
        //GET - user
        //3. Get the updates user with GET CALL:
        APIResponse apiGETResponse = apiRequestContext.get("https://gorest.co.in/public/v2/users/" + userId, RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setHeader("Authorization", "Bearer a9e39b8c371a1459ef03571b260f045c4a951ca6a5a9b71da5db39b0d9e8aba1"));

        System.out.println(apiGETResponse.status() + " : " + apiGETResponse.statusText());
        Assert.assertEquals(apiGETResponse.status(), 200);
        Assert.assertEquals(apiGETResponse.statusText(), "OK");

        // Print the response text
        String getresponseText = apiGETResponse.text();
        System.out.println(getresponseText);

        // Print the response body in pretty format
        ObjectMapper objectMapper2 = new ObjectMapper();
       Users actGETUsers = objectMapper2.readValue(getresponseText,Users.class);

//validations
        Assert.assertEquals(actGETUsers.getEmail(),users.getEmail());
        Assert.assertEquals(actGETUsers.getGender(),users.getGender());
        Assert.assertEquals(actGETUsers.getName(), users.getName());
        Assert.assertEquals(actGETUsers.getId(),userId);
        Assert.assertEquals(actGETUsers.getStatus(), users.getStatus());


    }
}

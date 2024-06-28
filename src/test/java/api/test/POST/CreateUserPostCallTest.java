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

public class CreateUserPostCallTest {

    Playwright playwright;
    APIRequest request;
    APIRequestContext apiRequestContext;

    @BeforeTest
    public void setup(){
        playwright = Playwright.create();
        request = playwright.request();
        apiRequestContext = request.newContext();
    }

    @AfterTest
    public void tearDown(){
        playwright.close();
    }
public static String getRandomEmail(){
        return "rameshqapwapi"+ System.currentTimeMillis()+" @gmail.com";
}

    @Test
    public void createUserPostCallTest() throws IOException {

        String postReqData="{\n" +
                "    \"name\": \"Ramaesh\",\n" +
                "    \"gender\": \"male\",\n" +
                "    \"email\": \"ramesh1@gmail.com\",\n" +
                "    \"status\": \"active\"\n" +
                "}";
       APIResponse apiPostResponse = apiRequestContext.post("https://gorest.co.in/public/v2/users", RequestOptions.create()
                .setHeader("Content-Type","application/json")
                .setHeader("Authorization","Bearer a9e39b8c371a1459ef03571b260f045c4a951ca6a5a9b71da5db39b0d9e8aba1")
                .setData(postReqData));

        System.out.println(apiPostResponse.status()+" : "+apiPostResponse.statusText());
        Assert.assertEquals(apiPostResponse.status(),201);
        Assert.assertEquals(apiPostResponse.statusText(),"Created");

        String apiresponseText = apiPostResponse.text();
        System.out.println(apiresponseText);
        //print api prettyresponse
        System.out.println("print api response in pretty format");
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonResponse = objectMapper.readTree(apiPostResponse.body());
        String jsonPrettyResponse = jsonResponse.toPrettyString();
        System.out.println(jsonPrettyResponse);

        //get the id from response
        String userId = jsonResponse.get("id").asText();
        System.out.println("user id :"+userId);

        //GET : call to fetch the same user by id

       APIResponse apiGETResponse = apiRequestContext.get("https://gorest.co.in/public/v2/users/"+ userId, RequestOptions.create()
                .setHeader("Content-Type","application/json")
                .setHeader("Authorization","Bearer a9e39b8c371a1459ef03571b260f045c4a951ca6a5a9b71da5db39b0d9e8aba1"));

        System.out.println(apiGETResponse.status()+" : "+apiGETResponse.statusText());
        Assert.assertEquals(apiGETResponse.status(),200);
        Assert.assertEquals(apiGETResponse.statusText(),"OK");

        //print the resposne text
        String responseText = apiGETResponse.text();
        System.out.println(responseText);

        //print the response body in pretty format
        ObjectMapper objectMapper1  = new ObjectMapper();
        JsonNode jsonGetResponse = objectMapper1.readTree(apiGETResponse.body());
        String jsonGetResponsePretty=jsonGetResponse.toPrettyString();
        System.out.println(jsonGetResponsePretty);

        Assert.assertTrue(apiGETResponse.text().contains(userId));
        Assert.assertTrue(apiGETResponse.text().contains("Ramaesh"));
      //  Assert.assertTrue(apiGETResponse.text().contains(email));
    }


}

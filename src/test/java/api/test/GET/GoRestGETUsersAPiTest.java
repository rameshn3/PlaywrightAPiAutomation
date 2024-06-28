package api.test.GET;
import java.io.IOException;
import java.lang.*;
import java.util.Map;

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

public class GoRestGETUsersAPiTest {

    Playwright playwright;
    APIRequest request;

    APIRequestContext apiRequestContext;

    @BeforeTest
    public void setup(){
        //create playwright object
        playwright = Playwright.create();
        request = playwright.request();
        apiRequestContext = request.newContext();
    }

@AfterTest
    public void tearDown(){
        playwright.close();
}

@Test
    public void getAllUsersTest() throws IOException {

    APIResponse apiGETResponse = apiRequestContext.get("https://gorest.co.in/public/v2/users", RequestOptions.create()
            .setHeader("Content-Type", "application/json")
            .setHeader("Authorization", "Bearer a9e39b8c371a1459ef03571b260f045c4a951ca6a5a9b71da5db39b0d9e8aba1"));


    System.out.println(apiGETResponse.status()+" : "+apiGETResponse.statusText());
    Assert.assertEquals(apiGETResponse.status(),200);
    String apiGetResponseText=apiGETResponse.text();
    System.out.println("api resposne text is:"+apiGetResponseText);

    //parsing the json
    System.out.println("----print api json response----");
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode jsonResponse = objectMapper.readTree(apiGETResponse.body());
    String jsonPrettyResponse = jsonResponse.toPrettyString();
    System.out.println("json pretty response:"+jsonPrettyResponse);

    //print API Url
    System.out.println(apiGETResponse.url());

    Map<String, String> headers = apiGETResponse.headers();
    System.out.println(headers);
    Assert.assertEquals(headers.get("content-type"),"application/json; charset=utf-8");
    Assert.assertEquals(headers.get("x-download-options"), "noopen");
}

@Test
    public void getSpecificUser() throws IOException {
    APIResponse apiGETResponse = apiRequestContext.get("https://gorest.co.in/public/v2/users/6965762", RequestOptions.create()
            .setHeader("Content-Type", "application/json")
            .setHeader("Authorization", "Bearer a9e39b8c371a1459ef03571b260f045c4a951ca6a5a9b71da5db39b0d9e8aba1"));


    System.out.println(apiGETResponse.status()+" : "+apiGETResponse.statusText());
    Assert.assertEquals(apiGETResponse.status(),200);
    String apiGetResponseText=apiGETResponse.text();
    System.out.println("api resposne text is:"+apiGetResponseText);

    //parsing the json
    System.out.println("----print api json response----");
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode jsonResponse = objectMapper.readTree(apiGETResponse.body());
    String jsonPrettyResponse = jsonResponse.toPrettyString();
    System.out.println("json pretty response:"+jsonPrettyResponse);

    //print API Url
    System.out.println(apiGETResponse.url());

    Map<String, String> headers = apiGETResponse.headers();
    System.out.println(headers);
    Assert.assertEquals(headers.get("content-type"),"application/json; charset=utf-8");
    Assert.assertEquals(headers.get("x-download-options"), "noopen");
}

@Test
    public void getAllUsersByQueryParams() throws IOException {
    APIResponse apiGETResponse = apiRequestContext.get("https://gorest.co.in/public/v2/users", RequestOptions.create()
            .setHeader("Content-Type", "application/json")
            .setHeader("Authorization", "Bearer a9e39b8c371a1459ef03571b260f045c4a951ca6a5a9b71da5db39b0d9e8aba1")
            .setQueryParam("gender","male")
            .setQueryParam("status","active"));


    System.out.println(apiGETResponse.status()+" : "+apiGETResponse.statusText());
    Assert.assertEquals(apiGETResponse.status(),200);
    String apiGetResponseText=apiGETResponse.text();
    System.out.println("api resposne text is:"+apiGetResponseText);

    System.out.println("----print api json response----");
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode jsonResponse = objectMapper.readTree(apiGETResponse.body());
    String jsonPrettyResponse = jsonResponse.toPrettyString();
    System.out.println("json pretty response:"+jsonPrettyResponse);

    //print API Url
    System.out.println(apiGETResponse.url());

    Map<String, String> headers = apiGETResponse.headers();
    System.out.println(headers);
    Assert.assertEquals(headers.get("content-type"),"application/json; charset=utf-8");
    Assert.assertEquals(headers.get("x-download-options"), "noopen");
}

}

package steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.authentication.FormAuthConfig;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import utils.Constants;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.requestSpecification;
import static io.restassured.config.EncoderConfig.encoderConfig;

public class FatSecretPlatformStepDef {
    RequestSpecification request;
    private static String token;
    private  static Response response;
    @Given("User generates the authorization token")
    public void userGeneratesTheAuthorizationToken() {
        RestAssured.baseURI  =  Constants.FATSECRET_OAUTHURL;
        request = RestAssured.given().header("Content-type","application/json").contentType("multipart/form-data").multiPart("grant_type","client_credentials").multiPart("scope","basic");
        response=request.auth().preemptive().basic(Constants.CLIENT_ID,Constants.CLIENT_SECRET)
                .post("/token");
        token = getJsonPath(response,"access_token");
    }

    public String getJsonPath(Response response, String key) {
        String resp = response.asString();
        JsonPath js = new JsonPath(resp);
        return js.get(key).toString();
    }


    @When("User searches for {string}")
    public void userSearchesFor(String foodSearch) {
        RestAssured.baseURI  =  Constants.FATSECRET_BASEURI;
        request  =  RestAssured.given().headers("Authorization","Bearer "+token,"Content-Type","application/json")
                .queryParam("method","foods.search")
                .queryParam("format","json")
                .queryParam("search_expression",foodSearch);
        response = request.post();
    }

    @Then("Status code must be {int}")
    public void statusCodeMustBe(int statusCode) {
        Assert.assertEquals(response.getStatusCode(),statusCode);
    }

    @And("The food ID is {string} and food type is {string}")
    public void theFoodIDIsAndFoodTypeIs(String foodID, String foodType) {
        Assert.assertEquals(getJsonPath(response,"foods.food[0].food_id"),foodID);
        Assert.assertEquals(getJsonPath(response,"foods.food[0].food_type"),foodType);
    }

    @When("User searches for {string} with incorrect token")
    public void userSearchesForWithIncorrectToken(String foodSearch) {
        RestAssured.baseURI  =  Constants.FATSECRET_BASEURI;
        request  =  RestAssured.given().headers("Authorization","Bearer "+"abcde","Content-Type","application/json")
                .queryParam("method","foods.search")
                .queryParam("format","json")
                .queryParam("search_expression",foodSearch);
        response = request.post();
    }

    @Then("Error code in response should be {string}")
    public void errorCodeInResponseShouldBe(String errorCode) {
        Assert.assertEquals(getJsonPath(response,"error.code"),errorCode);
    }
}

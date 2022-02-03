package steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import utils.Constants;

public class CurrentWeatherStepDef {
    String apiKey;
    String lat;
    String lang;
    RequestSpecification request;
    private  static Response response;
    @Given("User has {string} API key")
    public void userHasValidAPIKey(String keyType) {
        if(keyType.equalsIgnoreCase("valid"))
        apiKey= Constants.API_KEY;
        else
            apiKey="invalid";
    }

//
    @When("User sends latitude as {int} and longitude as {int} to the request")
    public void userSendsLatitudeAsAndLongitudeAsToTheRequest(int latitude, int longitude) {
        RestAssured.baseURI  =  Constants.WEATHER_BASEURL;
        request  =  RestAssured.given().queryParam("lat",latitude).queryParam("lon",longitude).queryParam("appid",apiKey);
        response = request.get("/weather");
        System.out.println(response.body().asString());
    }

    @Then("The status code must be {int}")
    public void theStatusCodeMustBe(int statusCode) {
        Assert.assertEquals(response.getStatusCode(),statusCode);
    }

    @And("The country is {string} and location is {string}")
    public void theCountryIsAndLocationIs(String country, String location) {
        Assert.assertEquals(getJsonPath(response,"sys.country"),country);
        Assert.assertEquals(getJsonPath(response,"name"),location);
    }
    public String getJsonPath(Response response, String key) {
        String resp = response.asString();
        JsonPath js = new JsonPath(resp);
        return js.get(key).toString();
    }

    @And("The response message contains {string}")
    public void theResponseMessageContains(String respMessage) {
        Assert.assertTrue(getJsonPath(response,"message").contains(respMessage));
    }
}

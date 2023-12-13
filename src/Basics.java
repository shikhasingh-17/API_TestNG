import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.testng.Assert;
import org.testng.annotations.Test;

import files.ReusableCode;
import files.payload;
public class Basics {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Add Place -> Update place with new address-> get place to validate if new address is present
		//Validate AddPlaces API
		// Given - all input
		//When- Submit method - resource
		// Then - Validate
		
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		
		//Add Place and retrieve Place ID
		System.out.println("Add Place Start");
		String response = 
				given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json").body(payload.addPlace())
				.when().post("maps/api/place/add/json")
				.then().assertThat().statusCode(200).body("scope", equalTo("APP")).header("Content-Type", "application/json;charset=UTF-8").extract().response().asString();
		System.out.println("Response is:" + response);
		
		JsonPath js = new JsonPath(response);
		String placeID = js.getString("place_id");
		System.out.println("Place ID is" + placeID);
		System.out.println("Add Place End");
		
		//Update Place with new address
		String newAddress = "70 winter walk, USA";
		given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
		.body("{\r\n"
				+ "\"place_id\":\""+placeID+"\",\r\n"
				+ "\"address\":\"70 winter walk, USA\",\r\n"
				+ "\"key\":\"qaclick123\"\r\n"
				+ "}")
		.when().put("maps/api/place/update/json")
		.then().log().all().assertThat().body("msg", equalTo("Address successfully updated"));
		
		//Get place API
		String getResponse = given().log().all().queryParams("key", "qaclick123", "place_id",placeID)
		.when().get("maps/api/place/get/json")
		.then().log().all().statusCode(200).extract().response().asString();
		
		JsonPath js2 = ReusableCode.rawToJson(getResponse);
		
		String actualAddress = js2.getString("address");
		
		System.out.println(actualAddress);
		
		Assert.assertEquals(actualAddress, newAddress);
		
		System.out.println("Test End");

	}
	
	
	//demo to pass body directly from a json file instead of writing that in our code
	@Test
	public void loadJsonFromFile() throws IOException {
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		given().log().all().queryParam("key", "qaclick123").body(new String (Files.readAllBytes(Paths.get("C:\\Users\\yuvra\\Downloads\\DemoProject\\DemoProject\\src\\files\\addplace.json"))))
		.when().post("maps/api/place/add/json")
		.then().log().all().statusCode(200).body("scope", equalTo("APP"));
	}

}

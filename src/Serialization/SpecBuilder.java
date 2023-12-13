package Serialization;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseBodyData;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojo.AddPlace;
import pojo.Location;

import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.List;

public class SpecBuilder {
	public static void main(String[] args) {
			
		AddPlace a = new AddPlace();
		a.setAccuracy(50);
		a.setAddress("29, side layout, cohen 09");
		a.setLanguage("english");
		a.setName("Frontline house");
		a.setPhone_number("\"(+91) 983 893 3937");
		a.setWebsite("http://google.com");
		List<String> myList = new ArrayList<String>();
		myList.add("shoe Park");
		myList.add("shop");
		a.setTypes(myList);
		Location l = new Location();
		l.setLat(-38.383494);
		l.setLng(33.427362);
		a.setLocation(l);
		
		RequestSpecification reqSpec = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addQueryParam("key", "qaClick123").setContentType(ContentType.JSON).build();
		ResponseSpecification resSpec = new ResponseSpecBuilder().expectContentType(ContentType.JSON).expectStatusCode(200).build();
		
		
		
		RequestSpecification req = given().spec(reqSpec).body(a);
		Response response = req.when().post("/maps/api/place/add/json")
		.then().spec(resSpec).extract().response();
		String strResponse = response.asString();
		System.out.println(strResponse);
	}

}

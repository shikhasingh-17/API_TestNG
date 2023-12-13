package Serialization;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBodyData;
import io.restassured.response.ValidatableResponse;
import pojo.AddPlace;
import pojo.Location;

import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.List;

public class GoogleMap {
	public static void main(String[] args) {
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		
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
		
		
		Response res = given().queryParam("key", "qaClick123").body(a)
		.when().post("/maps/api/place/add/json")
		.then().assertThat().statusCode(200).extract().response();
		String strResponse = ((ResponseBodyData) res).asString();
		System.out.println(strResponse);
	}

}

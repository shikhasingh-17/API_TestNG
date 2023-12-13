

import static org.hamcrest.Matchers.*;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import files.ReusableCode;
import files.payload;
import io.restassured.RestAssured;

import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.given;



public class DynamicJson {
	
	@Test(dataProvider="BooksData")
	public void addBook(String isbn, String aisle) {
		
		RestAssured.baseURI = "http://216.10.245.166";
		//Sending dynamic data to the payload
		String response = given().log().all().body(payload.addBook(isbn,aisle)).
				when().post("/Library/Addbook.php").
				then().log().all().statusCode(200).extract().response().asString();
		JsonPath js = ReusableCode.rawToJson(response);
		String bookID = js.get("ID");
		System.out.println(bookID);
		
	}
	@Test
	public void deleteBook(String bookID) {
		
		RestAssured.baseURI = "http://216.10.245.166";
		given().body(bookID).
		when().post("/Library/DeleteBook.php").
		then().statusCode(200).body("msg",equalTo("book is successfully deleted"));
	}
	
	
	@DataProvider(name = "BooksData")
	public Object[][] getData() {
		//initializing and returning a multidimensional array object
		return new Object[][] {{"234","asds"},{"5678","fdfs"},{"8798","tetr"}};
	}
	
	@DataProvider(name = "BookID")
	public void getBookID() {
		//return new Object[] {}
	}

}

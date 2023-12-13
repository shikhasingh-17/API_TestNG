import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import pojo.EcomLogin;
import pojo.LoginResponse;
import pojo.OrderDetails;
import pojo.Orders;

import static io.restassured.RestAssured.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EComE2ETest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EcomLogin login = new EcomLogin();
		login.setUserEmail("shikhasingh@gmail.com");
		login.setUserPassword("Unix_11!");
		
		RequestSpecification reqSpec = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com/").
				setContentType(ContentType.JSON).build();
		
		//Login
		RequestSpecification req = given().log().all().spec(reqSpec).body(login);
		LoginResponse res = req.when().log().all().post("/api/ecom/auth/login").then().extract().response().as(LoginResponse.class);
		String token = res.getToken();
		String userId = res.getUserId();
		System.out.println(token);
		System.out.println(userId);
		
		
		//Create Product
		RequestSpecification reqSpecAuth = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com/")
				.addHeader("authorization",token).build();
		RequestSpecification reqCP = given().log().all().spec(reqSpecAuth).param("productName", "Laptop")
				.param("productAddedBy", userId).param("productCategory", "fashion")
				.param("productSubCategory", "shirts").param("productPrice", "11500")
				.param("productDescription","Dell Laptop").param("productFor", "women")
				.multiPart("productImage",new File("C:/Users/yuvra/Downloads/repository-open-graph-template.png"));
		String resCP = reqCP.when().post("/api/ecom/product/add-product").then().log().all().extract().response().asString();
		JsonPath js = new JsonPath(resCP);
		String prodId = js.get("productId");
		System.out.println(prodId);		
		
		//Create Order
		OrderDetails od = new OrderDetails();
		od.setCountry("India");
		od.setProductOrderedId(prodId);
		List<OrderDetails> myList = new ArrayList<OrderDetails>();
		myList.add(od);
		Orders o = new Orders();
		o.setOrders(myList);
		
		RequestSpecification reqSpecCO = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com/").setContentType(ContentType.JSON)
				.addHeader("authorization",token).build();
		RequestSpecification reqCO = given().log().all().spec(reqSpecCO).body(o);
		String resCO = reqCO.when().post("/api/ecom/order/create-order").then().log().all().extract().response().asString();
		JsonPath js2 = new JsonPath(resCO);
		String orderID = js2.get("orders[0]");
		System.out.println(orderID);	
		
		
		//View order
		RequestSpecification reqVO = given().log().all().spec(reqSpecAuth).queryParam("id", orderID);
		String resVO = reqVO.when().get("/api/ecom/order/get-orders-details").then().extract().response().asString();
		JsonPath js3 = new JsonPath(resVO);
		System.out.println(js3.getString("message"));
		
		//Delete Product
		RequestSpecification reqDP = given().log().all().spec(reqSpecAuth).pathParam("productId",prodId);
		//String resDP = reqDP.when().delete("/api/ecom/product/delete-product/"+prodId).then().extract().response().asString();
		String resDP = reqDP.when().delete("/api/ecom/product/delete-product/{productId}").then().extract().response().asString();
		JsonPath js4 = new JsonPath(resDP);
		System.out.println(js4.getString("message"));

	}

}

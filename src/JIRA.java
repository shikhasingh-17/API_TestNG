import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

import java.io.File;
import java.util.Base64;

import org.testng.Assert;

public class JIRA {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		RestAssured.baseURI = "http://localhost:8080";	
		SessionFilter session = new SessionFilter();
		
		
		//Login
	
		String response = given().log().all().headers("Content-Type","application/json").body("{\r\n"
				+ "    \"username\": \"mishikhamay2017\",\r\n"
				+ "    \"password\": \"Unix_11!\"\r\n"
				+ "}").filter(session).when().post("/rest/auth/1/session").then().log().all().assertThat().
		statusCode(200).extract().response().asString();
		
		JsonPath js = new JsonPath(response);
		String sessionID = js.getString("session.value");
		System.out.println(sessionID);
		 
		String userName = "mishikhamay2017";
		String base64User = Base64.getEncoder().encodeToString(userName.getBytes());
		String password = "Unix_11!";
		String base64Pwd = Base64.getEncoder().encodeToString(password.getBytes());
		
		
		//Add Comment
		String expectedComment = "This Comment is added by Automation";
		String response2 = given().pathParam("issueIdOrKey", "10005").log().all().headers("Content-Type","application/json")
		.body("{\r\n"
				+ "    \"body\": \""+expectedComment+"\",\r\n"
				+ "    \"visibility\": {\r\n"
				+ "        \"type\": \"role\",\r\n"
				+ "        \"value\": \"Administrators\"\r\n"
				+ "    }\r\n"
				+ "}").filter(session).when().post("/rest/api/2/issue/{issueIdOrKey}/comment").then().log().all().assertThat().statusCode(201).extract().body().asString();
		
		JsonPath js2 = new JsonPath(response2);
		String commentId = (js2.getString("id"));
		System.out.println(commentId);
		
		//Add Attachment
		given().pathParam("key", "10005").header("X-Atlassian-Token", "no-check").header("Content-Type","multipart/form-data").multiPart("file", new File("jira.txt")).filter(session).
		when().post("/rest/api/2/issue/{key}/attachments").then().log().all().assertThat().statusCode(200);
		
		//GetIssue
		String response3 = given().filter(session).pathParam("key", "10005").queryParam("fields", "comment").log().all().when().get("/rest/api/2/issue/{key}").then().log().all().assertThat().statusCode(200).extract().body().asString();
		
		JsonPath js3 = new JsonPath(response3);		
		
		//Validate the comment is added successfully
		int commentCount = js3.getInt("fields.comment.comments.size()");
		for(int i = 0;i<commentCount;i++) {
			String issueCommentID = js3.get("fields.comment.comments["+i+"].id").toString();
			System.out.println(issueCommentID);
			if(issueCommentID.equalsIgnoreCase(commentId)) {
				System.out.println("inside 1st if");
				String actualComment = js3.get("fields.comment.comments["+i+"].body").toString();
				Assert.assertEquals(actualComment, expectedComment);
				if(actualComment.equalsIgnoreCase(expectedComment)) {
					System.out.println("inside 2 if");
					System.out.println("Comment Successfully Added");
				}
			}
		}

	}

}

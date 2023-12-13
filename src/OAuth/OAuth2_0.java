package OAuth;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import pojo.API;
import pojo.GetCourse;
import pojo.WebAutomation;

import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;

public class OAuth2_0 {
	
	public static void main(String args[]) {
		
		//Request to get code
		/*System.setProperty("webdriver.firefox.driver", "C:/Shikha/SelDrivers/chromedriver_win32/geckodriver.exe");
		/*
		 * ChromeOptions options = new ChromeOptions();
		 * options.addArguments("--remote-allow-origins=*");
		 
		//WebDriver driver = new ChromeDriver(options);
		WebDriver driver = new FirefoxDriver();
		driver.get("https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php");
		driver.findElement(By.cssSelector("input[type = 'email']")).sendKeys("shikha.techie1@gmail.com");
		driver.findElement(By.cssSelector("input[type = 'email']")).sendKeys(Keys.ENTER);
		driver.findElement(By.cssSelector("input[type = 'password']")).sendKeys("Iw!llNOT4get");
		driver.findElement(By.cssSelector("input[type = 'password']")).sendKeys(Keys.ENTER);
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(driver.getCurrentUrl()); */
		
		String url = "https://rahulshettyacademy.com/getCourse.php?code=4%2F0AfJohXnwy2rZs_ImsY6tw7DnXJoeS0ylHhEu1zH0n4GwSDh6y3W71LDJI8jtaR9zU_VEog&scope=email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+openid&authuser=0&prompt=none";
		String partialCode = url.split("code=")[1];
		String actualCode = partialCode.split("&scope")[0];
		
		
		//Request to get access token
		String accessTokenResponse = given().urlEncodingEnabled(false).log().all()
				.queryParams("code",actualCode).queryParams("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com").queryParams("client_secret","erZOWM9g3UtwNRj340YYaK_W").queryParams("redirect_uri","https://rahulshettyacademy.com/getCourse.php").queryParams("grant_type","authorization_code").
		when().post("https://www.googleapis.com/oauth2/v4/token").asString();
		JsonPath js2 = new JsonPath(accessTokenResponse);
		String accessToken = js2.get("access_token");
		
		
		//ActualRequest - Using deserialization
		//expect().defaultParser is required because 
		GetCourse response = given().log().all().queryParam("access_token", accessToken).expect().defaultParser(Parser.JSON)
				.when()
		.get("https://rahulshettyacademy.com/getCourse.php").as(GetCourse.class);
		System.out.println(response.getLinkedIn());
		System.out.println(response.getCourses().getWebAutomation().get(1).getCourseTitle());
		
		List<WebAutomation>  wa = response.getCourses().getWebAutomation();
		
		/*
		 * List<String> fruits = new ArrayList<String>();
		 * 
		 * for(String i: fruits) { System.out.println(i); }
		 */
		
		for(WebAutomation i:wa) {
			System.out.println(i.getCourseTitle());
			if(i.getCourseTitle().equalsIgnoreCase("Selenium Webdriver Java") ) {
				System.out.println(i.getPrice());
			}
		}
		
		//List comparison
		String[] expCourseTitles = {"Rest Assured Automation using Java","SoapUI Webservices testing"};
		ArrayList<String> apiCourses = new ArrayList<String>();
		
		List<API> ac = response.getCourses().getApi();
		
		for(API j:ac) {
			apiCourses.add(j.getCourseTitle());
		}
		
		List<String> expCourses = Arrays.asList(expCourseTitles);
		Assert.assertTrue(apiCourses.equals(expCourses));	
		
	
	}


}


import files.ReusableCode;
import io.restassured.path.json.JsonPath;

public class ComplexJsonParsing {
	
	public static void main (String args[]) {
		JsonPath js1 = new JsonPath(ReusableCode.dummyResponse());
		int count = js1.getInt("courses.size()");
		System.out.println(count);
		
		//Print purchase amount
		
		System.out.println(js1.getInt("dashboard.purchaseAmount"));
		
		//Print course title
		System.out.println(js1.getString("courses[0].title"));
		
		//. Print All course titles and their respective Prices
		for(int i=0;i<count;i++) {
			System.out.println("Course Title is : " + js1.getString("courses["+i+"].title") + " Price is : " + js1.getString("courses["+i+"].price") );
		}
		//5. Print no of copies sold by RPA Course
		System.out.println("Number of copies sold by RPA course " + js1.getString("courses[2].copies"));
		
		//6. Print no of copies sold if course title is RPA Course
		for(int i=0;i<count;i++) {
			String title = js1.getString("courses["+i+"].title");
			if(title.equalsIgnoreCase("RPA")) {
				System.out.println(js1.getString("courses["+i+"].copies"));
				break;
			}
		}
		
		

	}
	
	
	
	

	

}

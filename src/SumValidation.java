import org.testng.Assert;
import org.testng.annotations.Test;

import files.ReusableCode;
import io.restassured.path.json.JsonPath;

public class SumValidation {
	
	@Test
	public void verifySumOfCoursesPurchased() {
		
		JsonPath js1 = new JsonPath(ReusableCode.dummyResponse());
		
		//7. Verify if Sum of all Course prices matches with Purchase Amount
		int sum = 0;
		int count = js1.getInt("courses.size()");
		for(int i = 0;i<count;i++) {
			sum = sum + (js1.getInt("courses["+i+"].price")*js1.getInt("courses["+i+"].copies"));
		}
		System.out.println(sum);
		Assert.assertEquals(sum, js1.getInt("dashboard.purchaseAmount"));
		
	}

}

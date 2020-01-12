package testcases;
import org.testng.annotations.Test;

import base.TestBase;

public class NextDoorPageTitleMatch extends TestBase{

	@Test()
	public void verifyTitle(){
		boolean result= titlevalidation(OR.getProperty("expectedPageTitle"));
		if(result==true){
			System.out.println("PASS");
		}else{
			System.out.println("FAIL");
		}
		
	}
	
}

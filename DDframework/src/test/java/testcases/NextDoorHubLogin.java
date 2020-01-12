package testcases;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import base.TestBase;
import utilities.TestUtil;

public class NextDoorHubLogin extends TestBase {
	
	@Test(dataProvider="getData")
	public void nextDoorHubLogin(String userName, String passWord) throws InterruptedException{
		
		type("username",userName);
		type("password",passWord);
		click("loginBtn");
	
		
		Assert.assertTrue(isElementPresent(OR.getProperty("loginprofile")), "Login not successful");
		Thread.sleep(3000);
		
		System.out.println("Navigate to Google URL");
		driver.navigate().to(OR.getProperty("GoogleURL"));
		System.out.println("Navigate Back to Next Hub Website");
		driver.navigate().back();
		System.out.println("Print ULR of Current URL");
		String url = driver.getCurrentUrl();
		System.out.println("URL Of Current URL:-"+url);
		System.out.println("Navigate Forward");
		driver.navigate().forward();
		System.out.println("Closing Browser");
		driver.close();
	}
	
	
	@DataProvider
	public static Object[][] getData(){
		
		return TestUtil.getDataFromExcel("UserLogin");
	}
}

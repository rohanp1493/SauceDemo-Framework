package tests;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import driver.DriverFactory;
import io.qameta.allure.Attachment;

public class BaseTest {
	
	@BeforeMethod
	public void setUp() {
		DriverFactory.initDriver();
		System.out.println("Browser Opend");
	}
	
	@AfterMethod
	public void tearDown(ITestResult results) {
		if(results.getStatus()==ITestResult.FAILURE) {
			System.out.println("Test Failed: "+ results.getName());
			
			//Take screeshot and attached to allure reports
			takeScreenshot();
		}
		
		//Close browser whether pass or fail
		DriverFactory.quitDriver();
	}
	
	//Take screenshot method
	
	@Attachment(
	        value = "Screenshot on Failure",
	        type  = "image/png"
	    )
	public byte[] takeScreenshot(){
		WebDriver driver = DriverFactory.getDriver();
		
		//Take Screenshot selenium buit in method
		return ((TakesScreenshot) driver)
			    .getScreenshotAs(OutputType.BYTES);
		
		
		
	}

}

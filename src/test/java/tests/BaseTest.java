package tests;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import driver.DriverFactory;
import io.qameta.allure.Attachment;
import utils.AIHelper;

public class BaseTest {
	
	@BeforeMethod
	public void setUp() {
		DriverFactory.initDriver();
		System.out.println("Browser Opend");
	}
	
	@AfterMethod(alwaysRun = true)
	public void tearDown(ITestResult result) {

	    // Print result for debugging
	    System.out.println(
	        "Test: " + result.getName()
	        + " | Status: "
	        + getStatusName(result.getStatus()));

	    if (result.getThrowable() != null) {
	        System.out.println(
	            "Reason: "
	            + result.getThrowable()
	                .getMessage());
	    }

	    if (result.getStatus()
	            == ITestResult.FAILURE) {
	        takeScreenshot();
	        attachAIAnalysis(
	            AIHelper.analyzeFailure(
	                result.getName(),
	                result.getThrowable()
	                    .getMessage(), ""));
	    }

	    DriverFactory.quitDriver();
	}

	private String getStatusName(int status) {
	    switch (status) {
	        case ITestResult.SUCCESS: return "PASS";
	        case ITestResult.FAILURE: return "FAIL";
	        case ITestResult.SKIP:    return "SKIP";
	        default: return "UNKNOWN";
	    }
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
	
	@Attachment(
	        value = "AI Failure Analysis",
	        type = "text/plain"
	    )
	    public String attachAIAnalysis(String analysis) {
	        return analysis;
	    }

}

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
	
	@AfterMethod
	public void tearDown(ITestResult result) {

        if (result.getStatus()
                == ITestResult.FAILURE) {

            System.out.println("Test FAILED: "
                + result.getName());

            // Take screenshot
            takeScreenshot();

            // Get error details
            String testName = result.getName();
            String errorMsg = result
                .getThrowable()
                .getMessage();

            // Get page HTML at time of failure
            String pageSource = "";
            try {
                pageSource = DriverFactory
                    .getDriver()
                    .getPageSource();
            } catch (Exception e) {
                pageSource = "Could not get page source";
            }

            // Ask Claude to analyze the failure
            System.out.println(
                "Asking AI to analyze failure...");
            String aiAnalysis =
                AIHelper.analyzeFailure(
                    testName,
                    errorMsg,
                    pageSource);

            System.out.println(
                "AI Analysis: " + aiAnalysis);

            // Attach AI analysis to Allure report
            attachAIAnalysis(aiAnalysis);
        }

        // Always close browser
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
	
	@Attachment(
	        value = "AI Failure Analysis",
	        type = "text/plain"
	    )
	    public String attachAIAnalysis(String analysis) {
	        return analysis;
	    }

}

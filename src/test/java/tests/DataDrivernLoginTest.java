package tests;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import config.ConfigReader;
import driver.DriverFactory;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import pages.InventoryPage;
import pages.LoginPage;
import utils.ExcelUtils;

@Epic("SauceDemo Application")
@Feature("Data Driven Login Tests")

public class DataDrivernLoginTest extends BaseTest{
	
	//This class will contain data driven login tests using Excel file as data source
	
	LoginPage loginPage;
	InventoryPage inventoryPage;
	ConfigReader config;
	
	//path to excel file and sheet name
	private static String excelFilePath = "src/test/resources/testdata.xlsx";
	private static String sheetName = "LoginData";
	
	//Setup page objects before each test
	@BeforeMethod
	public void setUpPages() {
		config = ConfigReader.getInstance();
		WebDriver driver = DriverFactory.getDriver();
		loginPage = new LoginPage(driver);
		inventoryPage = new InventoryPage(driver);
		loginPage.navigateApp(config.get("base.url"));
	}
	
	@DataProvider(name = "loginData")
	public Object[][] getLoginData() {
		//Use ExcelUtils to read data from excel file and return as 2D array
		List<Map<String, String>> testData = ExcelUtils.getTestData(excelFilePath, sheetName);
		
		Object[][] dataArray = new Object[testData.size()][1];
		
		for(int i=0; i< testData.size(); i++) {
			dataArray[i][0] = testData.get(i);
		}
		return dataArray;
	}
	
	@Test(dataProvider = "loginData")
	@Story("Login with multiple sets of credentials from Excel file")
	@Severity(SeverityLevel.CRITICAL)
	@Description("This test will attempt to login with multiple sets of credentials "
			+ "read from Excel file and verify the outcome")
	public void testLoginWithExcelData(Map<String, String> data) {
		
		//Read username, password and expected result from data map
		String username = data.get("username");
		String password = data.get("password");
		String testType = data.get("testType");
		String expectedResult = data.get("expectedResult");
		
		System.out.println("\nRunning test:  " + testType);
		System.out.println("Testing login with Username: " + username + ","
				+ " Password: " + password);
		System.out.println("Expected Result: " + expectedResult);
		
		//Perform login action
		loginPage.login(username, password);
		
		//Wait for a moment to allow page to load after login attempt
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//Assert the outcome based on expected result
		if(expectedResult.equalsIgnoreCase("success")) {
			//For successful login, verify that we are on inventory page
			String pageTitle = inventoryPage.getPageTitle();
			System.out.println("Current Page Title: " + pageTitle);
			assert pageTitle.equals("Products") : "Expected to be on Products page after successful login";
		} else if(expectedResult.equalsIgnoreCase("error")) {
			//For invalid login, verify that error message is displayed
			boolean isErrorDisplayed = loginPage.isErrorDisplayed();
			System.out.println("Is error message displayed: " + isErrorDisplayed);
			assert isErrorDisplayed : "Expected error message to be displayed for invalid login";
		} else {
			System.out.println("Invalid expected result value in test data: " + expectedResult);
		}
	}
	
	
	

}

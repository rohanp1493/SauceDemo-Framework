package tests;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import config.ConfigReader;
import driver.DriverFactory;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import pages.InventoryPage;
import pages.LoginPage;

//Epic = top level group(like a module)
@Epic("SauceDemo Application")

//Feature= Feature being tested
@Feature("Login Functionality")

public class LoginTest extends BaseTest{
	
	WebDriver driver;
	LoginPage loginPage;
	InventoryPage inventoryPage;
	ConfigReader config;
	
	//Initialize the Browser
	@BeforeMethod
	public void setUpPages() {
		config = ConfigReader.getInstance();
		WebDriver driver = DriverFactory.getDriver();
		
//	public void setUp() {
//		config = ConfigReader.getInstance();
//		
//		//Setup Chrome options
//		/*ChromeOptions options = new ChromeOptions();
//		options.addArguments("--start-maximized");
//		options.addArguments("--disable-notifications");
//
//		// Disable all password features
//		Map<String, Object> prefs = new HashMap<>();
//		prefs.put("credentials_enable_service", false);
//		prefs.put("profile.password_manager_enabled", false);
//		prefs.put("profile.password_manager_leak_detection", false);
//		prefs.put("safebrowsing.enabled", false);
//		prefs.put("safebrowsing.disable_download_protection", true);
//		options.setExperimentalOption("prefs", prefs);
//		
//		driver = new ChromeDriver(options);*/
//		DriverFactory.initDriver();
//		driver =  DriverFactory.getDriver();
//		System.out.println("Broswer Opened");
		
		
		// Create page objects
        loginPage     = new LoginPage(driver);
        inventoryPage = new InventoryPage(driver);

        // Open login page
        loginPage.navigateApp(config.get("base.url"));

	}
	
//	@AfterMethod
//	public void tearDown() {
//		DriverFactory.quitDriver();
//		System.out.println("✅ Browser closed");
//	}
	
	
	//TEST 1- Valid Login
	@Test
	@Story("User logs in with valid crenditials")
	@Description("Verify that valid username and password user"
			+ "redirects to products page")
	@Severity(SeverityLevel.CRITICAL)
	public void validLoginTest() {
		System.out.println("Running: validLoginTest");
		
		loginPage.login(config.get("username"), config.get("userpassword"));
		try {
			Thread.sleep(2000);
		} catch(Exception e) {
			
		}
		
		//Assertion
		Assert.assertTrue(inventoryPage.isPageLoaded(),"Products page should be visible after login");
		Assert.assertEquals(inventoryPage.getPageTitle(), "Products", "Page title should be Products");
		
		System.out.println("PASS: validLoginTest");
		
	}
	
	// TEST 2 — Invalid Login
    // Checks that wrong credentials show error message
    // ───────────────────────────────────────────────────
    @Test
    @Story("User logs in with invalid crenditials")
	@Description("Verify that invalid username and password user"
			+ "get to error message")
	@Severity(SeverityLevel.NORMAL)
    public void invalidLoginTest() {
        System.out.println("Running: invalidLoginTest");

        // Action — login with WRONG credentials
        loginPage.login("wrong_user", "wrong_password");

        // Wait for error to appear
        try { Thread.sleep(1000); } catch (Exception e) {}

        // Assert — error message should be showing
        Assert.assertTrue(
            loginPage.isErrorDisplayed(),
            "Error message should appear for wrong credentials"
        );

        // Assert — check error message text
        String errorText = loginPage.getErrorMessage();
        System.out.println("Error message: " + errorText);

        Assert.assertTrue(
            errorText.contains("Username and password"),
            "Error should mention username and password"
        );

        System.out.println("PASS: invalidLoginTest");
    }

    // ───────────────────────────────────────────────────
    // TEST 3 — Empty Username
    // Checks that empty username shows error
    // ───────────────────────────────────────────────────
    @Test
    @Story("User sees error with empty username")
    @Description("Verify that empty username field "
            + "shows username required error")
	@Severity(SeverityLevel.NORMAL)
    public void emptyUsernameTest() {
        System.out.println("Running: emptyUsernameTest");

        // Action — login with empty username
        loginPage.login("", "secret_sauce");

        // Wait for error
        try { Thread.sleep(1000); } catch (Exception e) {}

        // Assert — error should appear
        Assert.assertTrue(
            loginPage.isErrorDisplayed(),
            "Error should appear when username is empty"
        );

        String errorText = loginPage.getErrorMessage();
        System.out.println("Error message: " + errorText);

        Assert.assertTrue(
            errorText.contains("Username is required"),
            "Error should say username is required"
        );

        System.out.println("PASS: emptyUsernameTest");
    }

    // ───────────────────────────────────────────────────
    // TEST 4 — Empty Password
    // Checks that empty password shows error
    // ───────────────────────────────────────────────────
    @Test
    @Story("User sees error with empty password")
    @Description("Verify that empty password field "
        + "shows password required error")
    @Severity(SeverityLevel.NORMAL)
    public void emptyPasswordTest() {
        System.out.println("Running: emptyPasswordTest");

        // Action — login with empty password
        loginPage.login("standard_user", "");

        // Wait for error
        try { Thread.sleep(1000); } catch (Exception e) {}

        // Assert — error should appear
        Assert.assertTrue(
            loginPage.isErrorDisplayed(),
            "Error should appear when password is empty"
        );

        String errorText = loginPage.getErrorMessage();
        System.out.println("Error message: " + errorText);

        Assert.assertTrue(
            errorText.contains("Password is required"),
            "Error should say password is required"
        );

        System.out.println("PASS: emptyPasswordTest");
    }
    
    //Step() helper
    @Step("{0}")
    private void step(String description) {
        System.out.println("Step: " + description);
    }
}
		

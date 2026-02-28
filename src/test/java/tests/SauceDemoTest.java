package tests;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import config.ConfigReader;
import pages.InventoryPage;
import pages.LoginPage;


public class SauceDemoTest {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		
		ConfigReader config =  ConfigReader.getInstance();
		
		//Set up the browser
		ChromeOptions options = new ChromeOptions();

		// Use a completely fresh temporary Chrome profile
		// Fresh profile = no password history = no dialogs!
//		String tempProfile = System.getProperty("java.io.tmpdir") 
//		    + "selenium-chrome-profile";
//		options.addArguments("--user-data-dir=" + tempProfile);

		options.addArguments("--start-maximized");
		options.addArguments("--disable-notifications");

		// Disable all password features
		Map<String, Object> prefs = new HashMap<>();
		prefs.put("credentials_enable_service", false);
		prefs.put("profile.password_manager_enabled", false);
		prefs.put("profile.password_manager_leak_detection", false);
		prefs.put("safebrowsing.enabled", false);
		prefs.put("safebrowsing.disable_download_protection", true);
		options.setExperimentalOption("prefs", prefs);

		// Exclude switches that might re-enable features
//		options.setExperimentalOption("excludeSwitches", 
//		    java.util.Arrays.asList(
//		        "enable-automation",
//		        "enable-logging"
//		    ));
		WebDriver driver = new ChromeDriver(options);
		System.out.println("Broswer Opened");
		
		//Create Page Objects
		LoginPage loginpage = new LoginPage(driver);
		InventoryPage inventoryPage = new InventoryPage(driver);
		
		//Test: Valid Login
		System.out.println("\n-----TEST 1: Valid Login");
		
		//Step 1: open login page
		loginpage.navigateApp(config.get("base.url"));
		Thread.sleep(1000);
		
		//Step 2: Login with valid credentials
		loginpage.login(config.get("username"), config.get("userpassword"));
		
		//Wait for 2 second
		Thread.sleep(2000);
		
		//Step 3: Verify we landed on inventory page
		if(inventoryPage.isPageLoaded()) {
			System.out.println("Pass: Login Successful!");
			System.out.println("Page title: "+ inventoryPage.getPageTitle());
			System.out.println("Products on page: "+ inventoryPage.getProductCount());
		
		}else {
			System.out.println("Fail: Login failed !");
		}
		
		//Test: Add to Cart
		System.out.println("\n-----------TEST 2: Add to Cart");
		
		//Step 1:- Get first product name
		String firstProduct = inventoryPage.getFirstProductName();
		System.out.println("First Product: "+ firstProduct);
		
		//Step 2 : Add first item to cart
		inventoryPage.addFirstItemToCart();
		Thread.sleep(2000);
		
		//Step 3: Verify Cart count
		String cartCount = inventoryPage.getCartCount();
		System.out.println("Cart Count: "+ cartCount);
		
		if(cartCount.equals("1")) {
			System.out.println("Pass: Item added to cart!");
		}else {
			System.out.println("Fail: Cart count is wrong!");
		}
		
		//Test: Logout
		System.out.println("\n--------------TEST 3: Logout");
		
		//Step:1 Logout
		inventoryPage.logout();
		Thread.sleep(2000);
		
		//Step 2: Verify we are back to login page
		String currentUrl = driver.getCurrentUrl();
		System.out.println("Current URL: "+ currentUrl);
		
		if(currentUrl.contains("saucedemo.com")) {
			System.out.println("Pass: Logout successful!");
		}else {
			System.out.println("Fail: Logout failed");
		}
		
		//Navigate the website
		/*String url = config.get("base.url");
		driver.get(url);
		System.out.println("Navigated to Sauce demo app");
		
		//Wait for 1 second
		Thread.sleep(1000);
		
		//get credentials for config file
		/*String username = config.get("username");
		String password = config.get("userpassword");
		//Find Elements on login page
		driver.findElement(By.id("user-name")).sendKeys(username);
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.id("login-button")).click();*/
		
		//Wait for Page to load
		/*Thread.sleep(2000);
		
		//Verify the title
		String title = driver.getTitle();
		System.out.println("Page Title is :-" + title);
		
		//check if the current url is inventory
		String currentUrl = driver.getCurrentUrl();
		if(currentUrl.contains("inventory")){
			System.out.println("Login is Success");
		}	
		else {
			System.out.println("Login is Failed");
		}*/
		
		//Tear down the browser
		driver.close();
		driver.quit();
		System.out.println("✅ Browser closed");
		
		
		
		

	}

}

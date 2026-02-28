package driver;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import config.ConfigReader;

public class DriverFactory {
	
	//Create ThreadLocal - each thread gets its own driver
	private static ThreadLocal<WebDriver> driverThread= new ThreadLocal<>();
	
	//initiate driver
	public static void initDriver() {
		ConfigReader config =  ConfigReader.getInstance();
		
		//Read browser name from config.properties
		String browser = config.get("browser").toLowerCase();
		
		//Read headless setting from config.properties
		boolean headless = config.getBoolean("headless");
		
		System.out.println("Opening Browser: " + browser);
		
		
		//create the correct browser
		WebDriver driver;
		switch(browser) {
		case "chrome":
			driver = createChrome(headless);
			break;
		case "firefox":
			driver = createFirefox(headless);
			break;
		default:
			throw new RuntimeException(
					"Browser not supported: "+ browser
					+ ". User chrome or firefox");
		}
		
		// How long to wait for elements to appear
        // Uses implicit.wait value from config.properties
		driver.manage().timeouts()
			.implicitlyWait(Duration.ofSeconds(
					config.getInt("implicit.wait")));
		
		// How long to wait for page to load
        // Uses page.load.timeout from config.properties
        driver.manage().timeouts()
            .pageLoadTimeout(Duration.ofSeconds(
                config.getInt("page.load.timeout")));
        
        //stored the driver for this thread
        driverThread.set(driver);
        System.out.println("Browser opened successfully");
	}
	
	public static WebDriver getDriver() {
        WebDriver driver = driverThread.get();
        if (driver == null) {
            throw new RuntimeException(
                "Driver is null! "
                + "Did you call initDriver() first?");
        }
        return driver;
    }

	private static WebDriver createChrome(boolean headless) {
		//Setup Chrome options
		ChromeOptions options = new ChromeOptions();
		
		//Open in maximized
		options.addArguments("--window-size=1920,1080");
		
        // No popup notifications
		options.addArguments("--disable-notifications");

		// Disable all password features
		options.addArguments("--password-store=basic");
	    options.addArguments(
	            "--disable-features=PasswordCheck");
	    options.addArguments(
	            "--disable-features="
	            + "SafeBrowsingEnhancedProtection");
		
		// Chrome internal preferences
		Map<String, Object> prefs = new HashMap<>();
		
		// Turn off password manager
		prefs.put("credentials_enable_service", false);
		prefs.put("profile.password_manager_enabled", false);
		
		// Turn off breach detection
        // This is what shows "Change your password"
		prefs.put("profile.password_manager_leak_detection", false);
		
		// Turn off safe browsing
		prefs.put("safebrowsing.enabled", false);
		prefs.put("safebrowsing.disable_download_protection", true);
		
		options.setExperimentalOption("prefs", prefs);
		
		//headless mode
		// false = you see browser (good for learning)
        // true  = invisible (good for CI/CD)
		if(headless) {
			options.addArguments("--headless=new");
			options.addArguments("--no-sandbox");
			options.addArguments("--disable-dev-shm-usage");
			options.addArguments("--disable-gpu");
			options.addArguments("--disable-notifications");
			options.addArguments("--start-maximized");
		} else {
			//local mode
			options.addArguments("--window-size=1920,1080");		}
		return new ChromeDriver(options);
	}

	private static WebDriver createFirefox(boolean headless) {
		FirefoxOptions options = new FirefoxOptions();
        if (headless) {
            options.addArguments("--headless");
            options.addArguments("--window-size=1920,1080");
        }
		return new FirefoxDriver(options);
	}
	

	public static void quitDriver() {
		WebDriver driver = driverThread.get();
		if(driver != null) {
			driver.quit();
			// Remove from ThreadLocal
            // Without this → memory leak!
            driverThread.remove();
            System.out.println("Browser closed");
		}
	}


}

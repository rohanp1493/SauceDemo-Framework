package utils;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import driver.DriverFactory;

public class WaitUtils {
	
	//Set a default timeout for explicit waits
	private static final int DEFAULT_TIMEOUT = 10; // seconds
	
	//create a WebDriverWait object with default timeout
	private static WebDriverWait getWait(int seconds) {
	return new WebDriverWait(DriverFactory.getDriver(), 
			Duration.ofSeconds(seconds));
	}
	
	//Wait for an element to be visible using default timeout
	public static WebElement waitForVisible(By locator) {
		return getWait(DEFAULT_TIMEOUT).until(ExpectedConditions.
				visibilityOfElementLocated(locator));
	}
	
	//Wait for element to be visible using custom timeout version
	public static WebElement waitForVisible(By locator, int seconds) {
		return getWait(seconds).until(ExpectedConditions.
				visibilityOfElementLocated(locator));
	}
	
	//Wait for an element to be clickable using default timeout
	public static WebElement WaitForElementClickable(By locator) {
		return getWait(DEFAULT_TIMEOUT).until(ExpectedConditions.
				elementToBeClickable(locator));
	}
	
	//Wait for element to be clickable using custom timeout version
	public static WebElement waitForElementClickable(By locator, int seconds) {
		return getWait(seconds).until(ExpectedConditions.
				elementToBeClickable(locator));
	}
	
	//Wait until url contains specific text
	public static boolean waitForUrl(String urlFragment) {
		return getWait(DEFAULT_TIMEOUT).until(ExpectedConditions.
				urlContains(urlFragment));
	}
	
	//wait until element contains specific text
	public static Boolean waitForElementText(By locator, String text) {
		return getWait(DEFAULT_TIMEOUT).until(ExpectedConditions.
				textToBePresentInElementLocated(locator, text));
	}
	
	//Wait until element is invisible
	public static Boolean waitForElementInvisible(By locator) {
		return getWait(DEFAULT_TIMEOUT).until(ExpectedConditions.
				invisibilityOfElementLocated(locator));
	}
	
	//wait until element exists in html
	public static WebElement waitForPresent(By locator) {
		return getWait(DEFAULT_TIMEOUT).until(ExpectedConditions.
				presenceOfElementLocated(locator));
	}
	
	//Safely check if element is visible without throwing exception
	public static boolean isDisplayed(By locator) {
		try {
			return DriverFactory.getDriver().findElement(locator).isDisplayed();
		} catch(Exception e) {
			return false;
		}
	}
	
	//Wait until element's attribute contains specific value
	public static boolean waitForElementAttributeContains(By locator, 
			String attribute, String value) {
		return getWait(DEFAULT_TIMEOUT).until(ExpectedConditions.
				attributeContains(locator, attribute, value));
	}
	
	//Wait until number of elements are expected count
	public static boolean waitForElementCount(By locator, int count) {
		return getWait(DEFAULT_TIMEOUT).until(
				driver -> driver.findElements(locator).size() == count);
	}
	
	//Wait for page is loaded
	public static void waitForPageLoaded() {
		 getWait(DEFAULT_TIMEOUT).until(
				driver -> ((String)((org.openqa.selenium.JavascriptExecutor)
						driver).executeScript("return document.readyState"))
						.equals("complete"));
		System.out.println("Page is loaded");
	}
			
			
}

package pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CartPage {
	
	//Initialize WebDriver driver
	private WebDriver driver;
	
	//Locators
	//Page Title
	private By pageTitle = By.cssSelector(".title");
	
	//All cart item names
	private By cartItemNames = By.cssSelector(".inventory_item_name");
	
	//All remove buttons
	private By removeButtons = By.cssSelector(".btn_inventory");
	
	//Checkout button
	private By checkoutButton = By.cssSelector("button.btn_action");
	
	//Continue Shopping button
	private By continueShoppingButton = By.cssSelector("button.btn_secondary");
	
	//Cart items container
	private By cartItems = By.cssSelector(".cart_item");
	
	//Create Constructor
	public CartPage(WebDriver driver) {
		this.driver = driver;
	}
	
	//Actions
	//Check if Cart page is loaded
	public boolean isPageLoaded() {
		try {
			return driver.findElement(pageTitle).isDisplayed();
		} catch(Exception e) {
			return false;
		}
	}
	
	//Verify the Page title
	public String getPageTitle() {
		return driver.findElement(pageTitle).getText();
	}
	
	//Count how many items are in cart
	public int getCartItemCount() {
		List<WebElement> items = driver.findElements(cartItems);
		return items.size();
	}
	
	//Get Name of first item in cart
	public String getFirstCartItemName() {
		List<WebElement> items = driver.findElements(cartItemNames);
		return items.get(0).getText();
	}
	
	//Remove first item from cart
	public void removeFirstItem() {
		List<WebElement> buttons = driver.findElements(removeButtons);
		buttons.get(0).click();
		System.out.println("Removed first item from cart");
	}
	
	//Click Checkout button
	public void clickCheckout() {
		driver.findElement(checkoutButton).click();
		System.out.println("Clicked on Checkout button");
	}
	
	//Click Continue Shopping button
	public void clickContinueShopping() {
		driver.findElement(continueShoppingButton).click();
		System.out.println("Clicked on Continue Shopping button");
	}
	
	//Get current URL
	public String getCurrentUrl() {
		return driver.getCurrentUrl();
	}
	
}

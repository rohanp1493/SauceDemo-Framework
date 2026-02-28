package pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class InventoryPage {
	
	//Initialize WebDriver driver
	private WebDriver driver;
	
	//Locators
	//Page Title
	private By pageTitle = By.cssSelector(".title");
	
	//All Product names texts on Page
	private By productNames = By.cssSelector(".inventory_item_name");
	
	//All add to carts buttons
	private By addCartButtons = By.cssSelector(".btn_inventory");
	
	//Shopping Cart icon
	private By shoppingCart = By.cssSelector("a.shopping_cart_link");
	
	//Hamburger Menu Button 
	private By menuButton = By.xpath("//button[contains(text(),'Open Menu')]");
	
	//Logout link inside Menu 
	private By logoutButton = By.xpath("//a[@id='logout_sidebar_link']");
	
	//Create Constructor
	public InventoryPage(WebDriver driver) {
		this.driver = driver;
	}
	
	//Actions
	//Check if Inventory page is loaded
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
	
	//Count how many products are present on page
	public int getProductCount() {
		List<WebElement> products =  driver.findElements(productNames);
		return products.size();
	}
	
	//Get Name of the first product
	public String getFirstProductName() {
		List<WebElement> products = driver.findElements(productNames);
		return products.get(0).getText();
		
	}
	
	//Add first Item to cart
	public void addFirstItemToCart() {
		List<WebElement> buttons = driver.findElements(addCartButtons);
		System.out.println(buttons.get(0).getText());
		buttons.get(0).click();
		System.out.println("Added first item to cart");
	}
	
	//Get count of items in carts
	public String getCartCount() {
		try {
			String count = driver.findElement(shoppingCart).getText();
			System.out.println(count);
			return driver.findElement(shoppingCart).getText();
		} catch(Exception e) {
			return "0";
		}
	}
	
	//Click on Shopping Cart
	public void clickCart() {
		driver.findElement(shoppingCart).click();
		System.out.println("Clicked on Cart icon");
	}
	
	//Logout
	//open Menu => links
	/*public void logout() {
		driver.findElement(menuButton).click();
		System.out.println("Menu is Opened");
		
		//Wait to menu is opened
		try {
			Thread.sleep(1000);
		}catch(Exception e) {
			
		}
		driver.findElement(logoutButton).click();
		System.out.println("Clicked on Logout");
	}*/
	public void logout() {
	    // Use JavaScript click — more reliable than normal click
	    // Normal click sometimes misses if element has layers on top
	    org.openqa.selenium.WebElement menu =
	        driver.findElement(menuButton);

	    ((org.openqa.selenium.JavascriptExecutor) driver)
	        .executeScript("arguments[0].click();", menu);

	    System.out.println("Opened menu");

	    // Wait for menu animation to complete
	    try { Thread.sleep(1000); } catch (Exception e) {}

	    driver.findElement(logoutButton).click();
	    System.out.println("Clicked logout");
	}
	
	public String getCurrentUrl() {
		return driver.getCurrentUrl();
	}
	

}

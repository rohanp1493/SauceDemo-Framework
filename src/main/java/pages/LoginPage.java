package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {
	
	//Initialize the Webdriver
	private WebDriver driver;
	
	
	//Locators
	private By userName = By.id("user-name");
	private By userPassword = By.id("password");
	private By loginButton = By.id("login-button");
	private By errorMessage = By.cssSelector("h3[data-test='error']");
	
	//Create a constructor
	public LoginPage(WebDriver driver) {
		this.driver = driver;
	}
	
	//Actions
	//Go to Login Page
	public void navigateApp(String url) {
		driver.get(url);
	}
	
	//Enter username in username field
	public void enterUsername(String username) {
		driver.findElement(userName).clear();
		driver.findElement(userName).sendKeys(username);
		System.out.println("Entered Username: "+ username);
	}
	
	//Enter password in password field
	public void enterPassword(String password) {
		driver.findElement(userPassword).clear();
		driver.findElement(userPassword).sendKeys(password);
		System.out.println("Entered password: "+ password);
	}
	
	//Click on Login Button
	public void clickLogin() {
		driver.findElement(loginButton).click();
		System.out.println("Clicked Login Button");
	}
	
	
	//Perform in single option
	public void login(String username, String password) {
		enterUsername(username);
		enterPassword(password);
		clickLogin();
	}
	
	//Error Verification
	public boolean isErrorDisplayed() {
		try {
			return driver.findElement(errorMessage).isDisplayed();
		} catch(Exception e) {
			return false;
		}
	}
		
	//Get the error Message text
	
	/*public String getErrorMessage() {
		String errormessage = driver.findElement(errorMessage).getText();
		return errormessage;
	}*/
	
	public String getErrorMessage() {
	    try {
	        return driver.findElement(errorMessage).getText();
	    } catch (Exception e) {
	        System.out.println("Could not find error message: "
	            + e.getMessage());
	        return "";
	    }
	}
		
}

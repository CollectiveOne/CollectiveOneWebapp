	/* This module tests Login, Sign Up and Forgot Password
	 * 
	 * */

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
	
public class AuthenticationModule {
	public WebDriver driver;
	
		public AuthenticationModule() {
			
			//path of the ChromeDriver or FirefoxDriver
			String exePath = "F:/CollectiveOne/CollectiveOneWebapp/frontend/bin/chromedriver.exe";
			System.setProperty("webdriver.chrome.driver", exePath);
			
			driver = new ChromeDriver();
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			driver.get("http://localhost:8080");
		}

		private boolean checkLogin() {
			try {

			    (driver.findElement(By.id("T_loginButton"))).click();
			    
			    WebElement emailInput_Modal = driver.findElement(By.name("email"));
			    WebElement passwordInput_Modal = driver.findElement(By.name("password"));
			    WebElement loginButton_Modal = driver.findElement(By.className("auth0-lock-submit"));
	
			    emailInput_Modal.sendKeys("saswat4friends@gmail.com");
			    passwordInput_Modal.sendKeys("helloworld");
			    loginButton_Modal.click();
			    
			}catch(Exception exception) {
				System.out.println("Failed : " + exception);
				return false;
			}
			driver.navigate().refresh();
			return true;
		}

		private boolean checkSignUp() {
			try {
			    (driver.findElement(By.id("T_loginButton"))).click();
			    (driver.findElement(By.xpath("//*[contains(text(),'Sign Up')]"))).click();
			    
			    WebElement emailInput_Modal = driver.findElement(By.name("email"));
			    WebElement passwordInput_Modal = driver.findElement(By.name("password"));
			    WebElement signupButton_Modal = driver.findElement(By.className("auth0-lock-submit"));
	
			    emailInput_Modal.sendKeys("saswat4friends@gmail.com");
			    passwordInput_Modal.sendKeys("helloworld");
			    signupButton_Modal.click();
			    
			}catch(Exception exception) {
				System.out.println("Failed : " + exception);
				return false;
			}
			driver.navigate().refresh();
			return true;
		}
		
		
		public static void main(String args[]) {

			AuthenticationModule authmodule = new AuthenticationModule();
			//ModuleResults moduleResults = new ModuleResults();
			System.out.println("Testing Authentication Modals");
			
			System.out.println("1. Testing Login Modal");
			if(authmodule.checkLogin())
				System.out.println("Login Modal Passed");
			else
				System.out.println("Login Modal Failed");
			
			System.out.println("2. Testing Sign Up Modal");
			if(authmodule.checkSignUp())
				System.out.println("Sign Up Modal Passed");
			else
				System.out.println("Sign Up Modal Failed");
			
			authmodule.driver.close();

		}


	}

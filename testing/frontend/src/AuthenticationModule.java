	/* This module tests Login, Sign Up and Forgot Password
	 * 
	 * */

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
	
public class AuthenticationModule {

		public boolean checkLogin(String email, String password, WebDriver driver) {
			try {

			    (driver.findElement(By.id("T_loginButton"))).click();
			    
			    WebElement emailInput_Modal = driver.findElement(By.name("email"));
			    WebElement passwordInput_Modal = driver.findElement(By.name("password"));
			    WebElement loginButton_Modal = driver.findElement(By.className("auth0-lock-submit"));
	
			    emailInput_Modal.sendKeys(email);
			    passwordInput_Modal.sendKeys(password);
			    loginButton_Modal.click();
			    
			}catch(Exception exception) {
				System.out.println("Failed : " + exception);
				return false;
			}
			//driver.navigate().refresh();
			return true;
		}

		public boolean checkSignUp(String email, String password, WebDriver driver) {
			try {
			    (driver.findElement(By.id("T_loginButton"))).click();
			    (driver.findElement(By.xpath("//*[contains(text(),'Sign Up')]"))).click();
			    
			    WebElement emailInput_Modal = driver.findElement(By.name("email"));
			    WebElement passwordInput_Modal = driver.findElement(By.name("password"));
			    WebElement signupButton_Modal = driver.findElement(By.className("auth0-lock-submit"));
	
			    emailInput_Modal.sendKeys(email);
			    passwordInput_Modal.sendKeys(password);
			    signupButton_Modal.click();
			    
			}catch(Exception exception) {
				System.out.println("Failed : " + exception);
				return false;
			}
			driver.navigate().refresh();
			return true;
		}

		public boolean checkSoialLogin(WebDriver driver) {
			try {

			    (driver.findElement(By.id("T_loginButton"))).click();
			    
			    WebElement googleLogIn_Modal = driver.findElement(By.xpath("//*[contains(text(),'Log in with Google')]"));
			    WebElement facebookLogIn_Modal = driver.findElement(By.xpath("//*[contains(text(),'Log in with Facebook')]"));
	
			    googleLogIn_Modal.click();
			    //facebookLogIn_Modal.click();
			    
			}catch(Exception exception) {
				System.out.println("Failed : " + exception);
				return false;
			}
			//driver.navigate().refresh();
			return true;
		}

//		
//		public static void main(String args[]) {
//
//			AuthenticationModule authmodule = new AuthenticationModule();
//			//ModuleResults moduleResults = new ModuleResults();
//			System.out.println("Testing Authentication Modals");
//			
//			System.out.println("1. Testing Login Modal");
////			if(authmodule.checkLogin("philh@x.com","123456", driver))
////				System.out.println("Login Modal Passed");
////			else
////				System.out.println("Login Modal Failed");
//			
//			System.out.println("2. Testing Sign Up Modal");
//			if(authmodule.checkSignUp())
//				System.out.println("Sign Up Modal Passed");
//			else
//				System.out.println("Sign Up Modal Failed");
//			
//			authmodule.driver.close();
//
//		}


	}

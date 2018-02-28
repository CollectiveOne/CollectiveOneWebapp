import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class ModulesTesting {
	
	private WebDriver driver;

	public ModulesTesting() {

		//path of the ChromeDriver or FirefoxDriver
		String exePath = "F:/CollectiveOne/CollectiveOneWebapp/frontend/bin/chromedriver.exe";
		System.setProperty("webdriver.chrome.driver", exePath);
		
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		driver.get("http://localhost:8080");
	}
	
	public boolean testAuthentication() {
		try {
			AuthenticationModule authModule = new AuthenticationModule("check");
			System.out.println("Testing Authentication Modal");
			if(authModule.checkLogin("philh@x.com","123456",driver))
				return true;

		}catch(Exception exception){
			System.out.println("Failed : " + exception);
			return false;
		}
		return true;
	}
	
	public boolean testInitiatives() {
		try {
			InitiativesModule initiativeModule = new InitiativesModule();
			System.out.println("2. Testing Initiative Modals");
			if(initiativeModule.editInitiative(driver))
				return true;
			
		}catch(Exception exception){
			System.out.println("Failed : " + exception);
			return false;
		}
		
		return true;
	}
	
	public static void main(String args[]) {

		
		ModulesTesting moduleTesting = new ModulesTesting();
		
		System.out.println("Testing Modules");
		
		if(moduleTesting.testAuthentication())
			System.out.println("Authentication Modal Passed");
		else {
			System.out.println("Authentication Modal Failed");
		}
		
		if(moduleTesting.testInitiatives())
			System.out.println("Initiatives Modal Passed");
		else {
			System.out.println("Initiatives Modal Failed");
		}
		
	}


}

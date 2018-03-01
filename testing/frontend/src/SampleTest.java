import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;


public class SampleTest {
	public static void main(String args[]) {
		String exePath = "F:/CollectiveOne/CollectiveOneWebapp/frontend/bin/chromedriver.exe";
		
		System.setProperty("webdriver.chrome.driver", exePath);
		
		WebDriver driver = new ChromeDriver();
		driver.get("http://localhost:8080");
	    WebElement exploreButton = driver.findElement(By.id("T_loginButton"));
	    exploreButton.click();
	    
	    WebElement emailInput = driver.findElement(By.name("email"));
	    WebElement passwordInput = driver.findElement(By.name("password"));

	    WebElement loginButton = driver.findElement(By.className("auth0-lock-submit"));

	    
	    emailInput.sendKeys("saswat4friends@gmail.com");
	    passwordInput.sendKeys("helloworld");
	    loginButton.click();
	    //driver.close();

	}

}

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProfileModule {
	
	public WebDriver driver;

	public boolean editProfile(WebDriver driver) {
		(driver.findElement(By.id("T_goToHomePage"))).click();
		try {
		    
			WebDriverWait wait = new WebDriverWait(driver,10);
			//click on testing initiative
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[contains(text(),'testing')]")));
			(driver.findElement(By.id("T_headerUserOptions"))).click();
			
			Thread.sleep(100);
			(driver.findElement(By.id("T_profileUserOptions"))).click();
			
//			//enter the name of user to add
 			wait.until(ExpectedConditions.elementToBeClickable(By.id("T_editProfileButton")));
 			(driver.findElement(By.id("T_editProfileButton"))).click();
			
 		    WebElement usernameEditProfile = driver.findElement(By.id("T_usernameProfileEdit"));
		    WebElement shortBioEditProfile = driver.findElement(By.id("T_bioProfileEdit"));
		    WebElement twitterURLButtonModal = driver.findElement(By.id("T_twitterProfileEdit"));
		    WebElement facebookURLButtonModal = driver.findElement(By.id("T_facebookProfileEdit"));
		    WebElement linkedINURLButtonModal = driver.findElement(By.id("T_linkedinProfileEdit"));
		    WebElement acceptEditProfileButton = driver.findElement(By.id("T_buttonAcceptProfileEdit"));
		    
		    usernameEditProfile.clear();
		    shortBioEditProfile.clear();
		    twitterURLButtonModal.clear();
		    facebookURLButtonModal.clear();
		    linkedINURLButtonModal.clear();
		    
		    usernameEditProfile.sendKeys("samplename");
		    shortBioEditProfile.sendKeys("Sample Bio");
		    twitterURLButtonModal.sendKeys("https://twitter.com/sample");
		    facebookURLButtonModal.sendKeys("https://www.facebook.com/sample");
		    linkedINURLButtonModal.sendKeys("https://www.linkedin.com/in/sample");
		    
		    acceptEditProfileButton.click();
									
			return true;
		    
		}catch(Exception exception) {
			System.out.println("Failed @ editProfile(): " + exception);
			return false;
		}
	}


}
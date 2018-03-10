import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SearchModule {
public WebDriver driver;
	
	public SearchModule() {

	}


	public boolean checkHeaderNotification(WebDriver driver) {
		try {
			
		    (driver.findElement(By.id("T_notificationButton"))).click();
		    
		    WebElement showMoreButton = driver.findElement(By.id("T_showMoreButton"));
		    showMoreButton.click();
			return true;
		    
		}catch(Exception exception) {
			System.out.println("Failed @ checkHeaderNotification(): " + exception);
			return false;
		}
	}


	public boolean checkInitiativeNotification(WebDriver driver) {
		try {
		    
			WebDriverWait wait = new WebDriverWait(driver,10);
			//waiting for the animation to finish
			(new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.xpath("//*[contains(text(),'testing')]")));
			(driver.findElement(By.xpath("//*[contains(text(),'testing')]"))).click();

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("edit-container")));
		    (driver.findElement(By.id("T_editMenuButton"))).click();
		    (driver.findElement(By.id("T_notificationInitiativeButton"))).click();

			wait.until(ExpectedConditions.elementToBeClickable(By.id("T_notificationNoSubscribedButton")));
		    (driver.findElement(By.id("T_notificationNoSubscribedButton"))).click();
		    (driver.findElement(By.id("T_notificationYesSubscribedButton"))).click();
		    (driver.findElement(By.id("T_notificationEmailNeverButton"))).click();
		    (driver.findElement(By.id("T_notificationEmailImmediatelyButton"))).click();
		    (driver.findElement(By.id("T_notificationEmailDayButton"))).click();
		    (driver.findElement(By.id("T_notificationEmailWeekButton"))).click();
		    
		    (driver.findElement(By.id("T_notificationAcceptButton"))).click();
			
		    return true;
		    
		}catch(Exception exception) {
			System.out.println("Failed @ checkInitiativeNotification(): " + exception);
			return false;
		}
	}
	
	public static void main(String args[]) {


	}


}
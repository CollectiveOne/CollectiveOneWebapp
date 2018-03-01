import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PeopleModule {
	public WebDriver driver;

	public boolean addPeople(WebDriver driver) {
		try {
		    
			WebDriverWait wait = new WebDriverWait(driver,10);
			//waiting for the animation to finish
			(new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.xpath("//*[contains(text(),'testing')]")));
			(driver.findElement(By.xpath("//*[contains(text(),'testing')]"))).click();

			(new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.id("T_peopleTab")));
			(driver.findElement(By.id("T_peopleTab"))).click();
			
			return true;
		    
		}catch(Exception exception) {
			System.out.println("Failed @ addPeople(): " + exception);
			return false;
		}
	}


	public boolean deletePeople(WebDriver driver) {
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
			System.out.println("Failed @ deletePeople(): " + exception);
			return false;
		}
	}
	
	public static void main(String args[]) {


	}


}
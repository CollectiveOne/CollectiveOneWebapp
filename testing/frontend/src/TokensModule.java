import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TokensModule {


	public boolean createTokens(WebDriver driver) {
		(driver.findElement(By.id("T_goToHomePage"))).click();
		try {
		    
			WebDriverWait wait = new WebDriverWait(driver,10);
			//click on testing initiative
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[contains(text(),'testing')]")));
			(driver.findElement(By.xpath("//*[contains(text(),'testing')]"))).click();
			
			Thread.sleep(100);

 			wait.until(ExpectedConditions.elementToBeClickable(By.id("T_addBrandNewTokensModal")));
 			(driver.findElement(By.id("T_addBrandNewTokensModal"))).click();

 			wait.until(ExpectedConditions.elementToBeClickable(By.id("T_amountCreate_NewTokens")));
 		    WebElement amountCreate_Modal = driver.findElement(By.id("T_amountCreate_NewTokens"));
		    WebElement percentageCreate_Modal = driver.findElement(By.id("T_percentageCreate_NewTokens"));
		    WebElement motive_Modal = driver.findElement(By.id("T_motive_NewTokens"));
		    WebElement notes_Modal = driver.findElement(By.id("T_notes_NewTokens"));
		    WebElement cancelButton_Modal = driver.findElement(By.id("T_cancelButton_NewTokens"));
		    WebElement acceptButton_Modal = driver.findElement(By.id("T_acceptButton_NewTokens"));
		    
		    amountCreate_Modal.clear();
		    percentageCreate_Modal.clear();
		    motive_Modal.clear();
		    notes_Modal.clear();
		    
		    amountCreate_Modal.sendKeys("100");
		    //percentageCreate_Modal.sendKeys("Sample Bio");
		    motive_Modal.sendKeys("Sample Motive");
		    notes_Modal.sendKeys("Sample Notes");
		    
		    acceptButton_Modal.click();
									
			return true;
		    
		}catch(Exception exception) {
			System.out.println("Failed @ createTokens(): " + exception);
			return false;
		}
	}



}
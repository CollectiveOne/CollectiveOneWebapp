import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PeopleModule {
	public WebDriver driver;

	public boolean addPeople(WebDriver driver) {
		try {
		    
			WebDriverWait wait = new WebDriverWait(driver,10);
			//click on testing initiative
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[contains(text(),'testing')]")));
			(driver.findElement(By.xpath("//*[contains(text(),'testing')]"))).click();
			//click on people tab
			wait.until(ExpectedConditions.elementToBeClickable(By.id("T_peopleTab")));
			(driver.findElement(By.id("T_peopleTab"))).click();
			
			//enter the name of user to add
			wait.until(ExpectedConditions.elementToBeClickable(By.tagName("input")));
		    WebElement selectUserModal = driver.findElement(By.tagName("input"));
		    WebElement addUserButtonModal = driver.findElement(By.id("T_addButton"));
			
		    selectUserModal.click();
		    
			//to select from the list
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[contains(text(),'carla')]")));
			(driver.findElement(By.xpath("//*[contains(text(),'carla')]"))).click();
		    
			//select the role of user
			Select dropdown = new Select(driver.findElement(By.id("T_memberRoleDropDown")));
			dropdown.selectByValue("EDITOR");
			
		    Thread.sleep(1000);
		    addUserButtonModal.click();
						
			
			return true;
		    
		}catch(Exception exception) {
			System.out.println("Failed @ addPeople(): " + exception);
			return false;
		}
	}


	public boolean deletePeople(WebDriver driver) {
		try {
		    
			String name = "carla"; //name of user to be deleted
		    
			WebDriverWait wait = new WebDriverWait(driver,10);
			//click on testing initiative
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[contains(text(),'testing')]")));
			(driver.findElement(By.xpath("//*[contains(text(),'testing')]"))).click();
			//click on people tab
			wait.until(ExpectedConditions.elementToBeClickable(By.id("T_peopleTab")));
			(driver.findElement(By.id("T_peopleTab"))).click();
			
			//enter the name of user to add T_peopleDeleteIcon
			wait.until(ExpectedConditions.elementToBeClickable(By.tagName("input")));
		    WebElement usernameTable = driver.findElement(By.cssSelector("table tbody tr #T_username"));
		    //System.out.println(usernameTable.getText());
		    
		    
		    
		    int rowCount = driver.findElements(By.tagName("tr")).size();
		    

		    for (int i = 1; i <= rowCount; i++) 
		    {
		        String sCellValue = driver.findElement(By.xpath("//table/tbody/tr["+ i +"]/td[2]")).getText();
		        System.out.println(sCellValue);
		        if (sCellValue.equalsIgnoreCase(name)) 
		        {
		        	System.out.println(driver.findElement(By.xpath("//table/tbody/tr["+ i +"]/td[4]/i")));
		        	wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//table/tbody/tr["+ i +"]/td[4]/i")));
		            (driver.findElement(By.xpath("//table/tbody/tr["+ i +"]/td[4]/i"))).click();

					Thread.sleep(1000);
		            (driver.findElement(By.id("T_confirmButton_DeletePople"))).click();
		            
		            break;
		        }
		    }

		    //deleteUserButtonModal.click();
						
			
			return true;
		    
		}catch(Exception exception) {
			System.out.println("Failed @ deletePeople(): " + exception);
			return false;
		}
	}
	
	public static void main(String args[]) {


	}


}
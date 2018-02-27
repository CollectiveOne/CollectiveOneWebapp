import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class InitiativesModule {

			public InitiativesModule() {
				// TODO Auto-generated constructor stub
			}

			public boolean createInitiative(WebDriver driver) {
				try {

				    (driver.findElement(By.id("T_createInitiativeButton"))).click();
				    
				    WebElement namelInput_Modal = driver.findElement(By.id("T_nameInput"));
				    WebElement purposeInput_Modal = driver.findElement(By.id("T_purposeInput"));
				    WebElement numberTokensInput = driver.findElement(By.id("T_numberTokensInput"));
				    WebElement tokenNameInput = driver.findElement(By.id("T_tokenNameInput"));
				    WebElement cancelButton_Modal = driver.findElement(By.id("T_cancelButton"));
				    WebElement acceptButton_Modal = driver.findElement(By.id("T_acceptButton"));
		
				    namelInput_Modal.sendKeys("Hello Test 2 ");
				    purposeInput_Modal.sendKeys("To  check modasl");
				    numberTokensInput.sendKeys("1000");
				    tokenNameInput.clear();
				    tokenNameInput.sendKeys("Coins");
				    acceptButton_Modal.click();
				    
				}catch(Exception exception) {
					System.out.println("Failed : " + exception);
					return false;
				}
				//driver.navigate().refresh();
				return true;
			}


			public boolean editInitiative(WebDriver driver) {
				try {

				    (driver.findElement(By.id("T_createInitiativeButton"))).click();
				    
				    WebElement namelInput_Modal = driver.findElement(By.id("T_nameInput"));
				    WebElement purposeInput_Modal = driver.findElement(By.id("T_purposeInput"));
				    WebElement numberTokensInput = driver.findElement(By.id("T_numberTokensInput"));
				    WebElement tokenNameInput = driver.findElement(By.id("T_tokenNameInput"));
				    WebElement cancelButton_Modal = driver.findElement(By.id("T_cancelButton"));
				    WebElement acceptButton_Modal = driver.findElement(By.id("T_acceptButton"));
		
				    namelInput_Modal.sendKeys("Hello Test 2 ");
				    purposeInput_Modal.sendKeys("To  check modasl");
				    numberTokensInput.sendKeys("1000");
				    tokenNameInput.sendKeys("Coins");
				    tokenNameInput.clear();
				    //acceptButton_Modal.click();
				    
				}catch(Exception exception) {
					System.out.println("Failed : " + exception);
					return false;
				}
				//driver.navigate().refresh();
				return true;
			}

			
			public static void main(String args[]) {

				//initModule.driver.close();

			}


		}

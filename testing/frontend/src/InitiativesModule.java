import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

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
		
				    namelInput_Modal.sendKeys("Hello Test 2");
				    purposeInput_Modal.sendKeys("To  check modasl");
				    numberTokensInput.sendKeys("1000");
				    tokenNameInput.clear();
				    tokenNameInput.sendKeys("Coins");
				    acceptButton_Modal.click();
					return true;
				    
				}catch(Exception exception) {
					System.out.println("Failed @ createInitiative(): " + exception);
					return false;
				}
				//driver.navigate().refresh();
			}


			public boolean editInitiative(WebDriver driver) {
				try {
					WebDriverWait wait = new WebDriverWait(driver,10);
					//waiting for the animation to finish
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(),'Hello Test 2')]")));
					(driver.findElement(By.xpath("//*[contains(text(),'Hello Test 2')]"))).click();
					
					//waiting for the animation to finish
					wait.until(ExpectedConditions.elementToBeClickable(By.className("edit-container")));
					
				    (driver.findElement(By.id("T_editMenuButton"))).click();
				    (driver.findElement(By.id("T_editInitiativeButton"))).click();
					
				    //waiting for the animation to finish
				    wait.until(ExpectedConditions.elementToBeClickable(By.id("T_nameInputEdit")));
				    wait.until(ExpectedConditions.elementToBeClickable(By.id("T_purposeInputEdit")));
					
				    WebElement nameInput_ModalEdit = driver.findElement(By.id("T_nameInputEdit"));
				    WebElement disableButton_Modal = driver.findElement(By.id("T_disableVisionButton"));
				    WebElement enableButton_Modal = driver.findElement(By.id("T_enableVisionButton"));
				    WebElement privateButton_Modal = driver.findElement(By.id("T_private_visibilityButton"));
				    WebElement ecosystemButton_Modal = driver.findElement(By.id("T_ecosystem_visibilityButton"));
				    WebElement publicButton_Modal = driver.findElement(By.id("T_public_visibilityButton"));
				    WebElement acceptButton_Modal = driver.findElement(By.id("T_acceptButtonEditInitiative"));
				    
				    nameInput_ModalEdit.clear(); //for not appending
				    nameInput_ModalEdit.sendKeys("Hello Test 3");
				    enableButton_Modal.click();
				    disableButton_Modal.click();
				    privateButton_Modal.click();
				    ecosystemButton_Modal.click();
				    publicButton_Modal.click();
				    acceptButton_Modal.click();
					return true;
				    
				}catch(Exception exception) {
					System.out.println("Failed @ editInitiative(): " + exception);
					return false;
				}
			}


			public boolean deleteInitiative(WebDriver driver) {
				try {
					WebDriverWait wait = new WebDriverWait(driver,10);
					//waiting for the animation to finish
					(new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.xpath("//*[contains(text(),'Hello Test 3')]")));
					(driver.findElement(By.xpath("//*[contains(text(),'Hello Test 3')]"))).click();

					wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("edit-container")));
				    (driver.findElement(By.id("T_editMenuButton"))).click();
				    (driver.findElement(By.id("T_editInitiativeButton"))).click();

				    Thread.sleep(2000);
				    wait.until(ExpectedConditions.elementToBeClickable(By.id("T_deleteInitiativeButton")));
				    WebElement deleteButton = driver.findElement(By.id("T_deleteInitiativeButton"));	    
				    Actions action = new Actions(driver);
				    action.moveToElement(deleteButton).click().perform(); //since, the element is at bottom
				    deleteButton.click();
				    
				    wait.until(ExpectedConditions.elementToBeClickable(By.id("T_confirmDeleteInitiativeButton")));
				    WebElement confirmdeleteButton = driver.findElement(By.id("T_confirmDeleteInitiativeButton"));
				    action.moveToElement(confirmdeleteButton).click().perform(); //since, the element is at bottom
				    confirmdeleteButton.click();

					return true;
				    
				}catch(Exception exception) {
					System.out.println("Failed @ deleteInitiative(): " + exception);
					return false;
				}
				//driver.navigate().refresh();
			}
			
			public static void main(String args[]) {

				//initModule.driver.close();

			}


		}

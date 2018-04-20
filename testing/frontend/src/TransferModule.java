import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TransferModule {

	public TransferModule() {
		// TODO Auto-generated constructor stub
	}

	public boolean transferToUsersDirect(WebDriver driver) {
		(driver.findElement(By.id("T_goToHomePage"))).click();
		try {
			WebDriverWait wait = new WebDriverWait(driver,10);
			//waiting for the animation to finish
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(),'testing')]")));
			(driver.findElement(By.xpath("//*[contains(text(),'testing')]"))).click();
			
			//click on transfer tab
			wait.until(ExpectedConditions.elementToBeClickable(By.id("T_transferTab")));
			(driver.findElement(By.id("T_transferTab"))).click();
			
			//waiting for the animation to finish
			wait.until(ExpectedConditions.elementToBeClickable(By.id("T_transferModal")));
			
		    (driver.findElement(By.id("T_transferModal"))).click();
		    (driver.findElement(By.id("T_transferModalUser"))).click();
			
		    //waiting for the animation to finish
		    wait.until(ExpectedConditions.elementToBeClickable(By.id("T_selectMemberTransferModal")));
		    wait.until(ExpectedConditions.elementToBeClickable(By.id("T_amountToBeTransferred")));
			
		    WebElement memberInput_ModalTransfer = driver.findElement(By.id("T_selectMemberTransferModal"));
		    //WebElement disableButton_Modal = driver.findElement(By.id("T_disableVisionButton"));
		    WebElement amountTransferred_ModalTransfer = driver.findElement(By.id("T_amountToBeTransferred"));
		    WebElement percentageTransferred_ModalTransfer = driver.findElement(By.id("T_percentageToBeTransferred"));
		    WebElement motiveModal = driver.findElement(By.id("T_motiveModal"));
		    WebElement notesModal = driver.findElement(By.id("T_notesModal"));
		    WebElement acceptButton_ModalTransfer = driver.findElement(By.id("T_acceptButton_ModalTransfer"));
		    
		    memberInput_ModalTransfer.click();
		    //waiting for the animation to finish
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[contains(text(),'emily')]")));
			(driver.findElement(By.xpath("//*[contains(text(),'emily')]"))).click();
			
		    amountTransferred_ModalTransfer.sendKeys("350");
		    percentageTransferred_ModalTransfer.sendKeys("20");
		    motiveModal.sendKeys("Motive Sample");
		    notesModal.sendKeys("Notes Sample");
		    acceptButton_ModalTransfer.click();
			return true;
		    
		}catch(Exception exception) {
			System.out.println("Failed @ editInitiative(): " + exception);
			return false;
		}
}
	public boolean transferToUsersPeer(WebDriver driver) {
		try {
			WebDriverWait wait = new WebDriverWait(driver,10);
			//waiting for the animation to finish
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(),'testing')]")));
			(driver.findElement(By.xpath("//*[contains(text(),'testing')]"))).click();
			
			//click on transfer tab
			wait.until(ExpectedConditions.elementToBeClickable(By.id("T_transferTab")));
			(driver.findElement(By.id("T_transferTab"))).click();
			
			//waiting for the animation to finish
			wait.until(ExpectedConditions.elementToBeClickable(By.id("T_transferModal")));
			
		    (driver.findElement(By.id("T_transferModal"))).click();
		    (driver.findElement(By.id("T_transferModalUser"))).click();
			
		    //go to peer reviewed
		    wait.until(ExpectedConditions.elementToBeClickable(By.id("T_peerReviewedTransferUser")));
		    (driver.findElement(By.id("T_peerReviewedTransferUser"))).click();
		    	    
		    Thread.sleep(1000);
		    //waiting for the animation to finish
		    wait.until(ExpectedConditions.elementToBeClickable(By.id("T_selectMemberTransferModal")));
		    wait.until(ExpectedConditions.elementToBeClickable(By.id("T_amountToBeTransferred")));
			
		    WebElement memberInput_ModalTransfer = driver.findElement(By.id("T_selectMemberTransferModal"));
		    //WebElement disableButton_Modal = driver.findElement(By.id("T_disableVisionButton"));
		    WebElement amountTransferred_ModalTransfer = driver.findElement(By.id("T_amountToBeTransferred"));
		    WebElement percentageTransferred_ModalTransfer = driver.findElement(By.id("T_percentageToBeTransferred"));
		    WebElement motiveModal = driver.findElement(By.id("T_motiveModal"));
		    WebElement notesModal = driver.findElement(By.id("T_notesModal"));
		    WebElement acceptButton_ModalTransfer = driver.findElement(By.id("T_acceptButton_ModalTransfer"));
		    
		    memberInput_ModalTransfer.click();
		    //waiting for the animation to finish
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[contains(text(),'emily')]")));
			(driver.findElement(By.xpath("//*[contains(text(),'emily')]"))).click();
			
		    amountTransferred_ModalTransfer.sendKeys("350");
		    percentageTransferred_ModalTransfer.sendKeys("20");
		    motiveModal.sendKeys("Motive Sample");
		    notesModal.sendKeys("Notes Sample");
		    acceptButton_ModalTransfer.click();
			return true;
		    
		}catch(Exception exception) {
			System.out.println("Failed @ editInitiative(): " + exception);
			return false;
		}
}



	public boolean transferToInitiatives(WebDriver driver) {
		try {
			WebDriverWait wait = new WebDriverWait(driver,10);
			//waiting for the animation to finish
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(),'testing')]")));
			(driver.findElement(By.xpath("//*[contains(text(),'testing')]"))).click();
			
			//click on transfer tab
			wait.until(ExpectedConditions.elementToBeClickable(By.id("T_transferTab")));
			(driver.findElement(By.id("T_transferTab"))).click();
			
			//waiting for the animation to finish
			wait.until(ExpectedConditions.elementToBeClickable(By.id("T_transferModal")));
			
		    (driver.findElement(By.id("T_transferModal"))).click();
		    (driver.findElement(By.id("T_transferModalUser"))).click();
			
		    //waiting for the animation to finish
		    wait.until(ExpectedConditions.elementToBeClickable(By.id("T_selectMemberTransferModal")));
		    wait.until(ExpectedConditions.elementToBeClickable(By.id("T_amountToBeTransferred")));
			
		    WebElement memberInput_ModalTransfer = driver.findElement(By.id("T_selectMemberTransferModal"));
		    //WebElement disableButton_Modal = driver.findElement(By.id("T_disableVisionButton"));
		    WebElement amountTransferred_ModalTransfer = driver.findElement(By.id("T_amountToBeTransferred"));
		    WebElement percentageTransferred_ModalTransfer = driver.findElement(By.id("T_percentageToBeTransferred"));
		    WebElement motiveModal = driver.findElement(By.id("T_motiveModal"));
		    WebElement notesModal = driver.findElement(By.id("T_notesModal"));
		    WebElement acceptButton_ModalTransfer = driver.findElement(By.id("T_acceptButton_ModalTransfer"));
		    
		    memberInput_ModalTransfer.click();
		    //waiting for the animation to finish
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[contains(text(),'emily')]")));
			(driver.findElement(By.xpath("//*[contains(text(),'emily')]"))).click();
			
		    amountTransferred_ModalTransfer.sendKeys("350");
		    percentageTransferred_ModalTransfer.sendKeys("20");
		    motiveModal.sendKeys("Motive Sample");
		    notesModal.sendKeys("Notes Sample");
		    acceptButton_ModalTransfer.click();
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

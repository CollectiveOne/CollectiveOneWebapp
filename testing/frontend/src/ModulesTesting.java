import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class ModulesTesting {
	
	private WebDriver driver;

	public ModulesTesting() {

		//path of the ChromeDriver or FirefoxDriver
		String exePath = "F:/CollectiveOne/CollectiveOneWebapp/frontend/bin/chromedriver.exe";
		System.setProperty("webdriver.chrome.driver", exePath);
		
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
		//driver.manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		driver.get("http://localhost:8080");
	}
	

	private void pause() {
		// TODO Auto-generated method stub
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public List<Boolean> testAuthentication() {

		AuthenticationModule authModule = new AuthenticationModule();
		List<Boolean> result = new ArrayList<Boolean>();
		System.out.println("1. Testing Authentication");
		try {
			
			result.add(authModule.checkLogin("philh@x.com","123456",driver));pause();
			//result.add(authModule.checkSignUp("saswat4friends1@gmail.com","123456",driver));pause();
			//result.add(authModule.checkSoialLogin(driver));pause();

		}catch(Exception exception){
			System.out.println("Failed @ testAuthentication(): " + exception);
			
		}
		return result;
	}
	
	public List<Boolean> testInitiatives() {

		InitiativesModule initiativeModule = new InitiativesModule();
		List<Boolean> result = new ArrayList<Boolean>();

		System.out.println("2. Testing Initiative Modals");
		
		try {
			result.add(initiativeModule.createInitiative(driver));pause();
			result.add(initiativeModule.editInitiative(driver));pause();
			result.add(initiativeModule.deleteInitiative(driver));pause();
		}catch(Exception exception){
			System.out.println("Failed @ testInitiatives(): " + exception);
			
		}
		
		return result;
	}

	private List<Boolean> testNotifications() {

		NotificationsModule notificationModule = new NotificationsModule();
		List<Boolean> result = new ArrayList<Boolean>();

		System.out.println("3. Testing Notifications And Filters");
		
		try {
			//result.add(notificationModule.checkHeaderNotification(driver));pause();
			//result.add(notificationModule.checkInitiativeNotification(driver));pause();
			result.add(notificationModule.checkTagFillter(driver));pause();
		}catch(Exception exception){
			System.out.println("Failed @ testNotificationsAndFilters(): " + exception);
		}
		
		return result;
	}

	private List<Boolean> testPeople() {

		PeopleModule peopleModule = new PeopleModule();
		List<Boolean> result = new ArrayList<Boolean>();

		System.out.println("4. Testing People");
		
		try {
			result.add(peopleModule.addPeople(driver));pause();
			result.add(peopleModule.deletePeople(driver));pause();
		}catch(Exception exception){
			System.out.println("Failed @ testPeople(): " + exception);
			
		}
		
		return result;
	}

	private List<Boolean> testTransfers() {

		TransferModule transferModule = new TransferModule();
		List<Boolean> result = new ArrayList<Boolean>();

		System.out.println("5. Testing Transfers");
		
		try {
			result.add(transferModule.transferToUsersDirect(driver));pause();
			result.add(transferModule.transferToUsersPeer(driver));pause();
		}catch(Exception exception){
			System.out.println("Failed @ testTransfers(): " + exception);
			
		}
		
		return result;
	}

	private List<Boolean> testProfile() {
		
		ProfileModule profileModdule = new ProfileModule();
		List<Boolean> result = new ArrayList<Boolean>();

		System.out.println("6. Testing Profile");
		
		try {
			result.add(profileModdule.editProfile(driver));pause();
		}catch(Exception exception){
			System.out.println("Failed @ testProfile(): " + exception);
			
		}
		
		return result;
	}


	private List<Boolean> testTokens() {
		
		TokensModule tokensModule = new TokensModule();
		List<Boolean> result = new ArrayList<Boolean>();

		System.out.println("7. Testing Tokens");
		
		try {
			result.add(tokensModule.createTokens(driver));pause();
		}catch(Exception exception){
			System.out.println("Failed @ testTokens(): " + exception);
			 
		}
		
		return result;
	}
	public static void main(String args[]) {

		List<Boolean> result_authentication;
		List<Boolean> result_initiatives;
		List<Boolean> result_notifications;
		List<Boolean> result_people;
		List<Boolean> result_transfer;
		List<Boolean> result_profile;
		List<Boolean> result_tokens;
		try {
			ModulesTesting moduleTesting = new ModulesTesting();
			
			System.out.println("Testing Modules\n");

			//Testing Authentication
			result_authentication = moduleTesting.testAuthentication();
			
			System.out.println(" - Logging In Modal " + (result_authentication.get(0) ? "Passed" : "Failed"));
//			System.out.println(" - Sign Up Modal " + (result_authentication.get(1) ? "Passed" : "Failed"));
//			System.out.println(" - Social Login Modal " + (result_authentication.get(2) ? "Passed" : "Failed"));
			
			//Testing Initiative
			result_initiatives = moduleTesting.testInitiatives();
			
			System.out.println(" - Creating Initiative Modal " + (result_initiatives.get(0) ? "Passed" : "Failed"));
			System.out.println(" - Editing Initiative Modal " + (result_initiatives.get(1) ? "Passed" : "Failed"));
			System.out.println(" - Deleting Initiative Modal " + (result_initiatives.get(2) ? "Passed" : "Failed"));

			//Testing Notifications
			result_notifications = moduleTesting.testNotifications();
			
			System.out.println(" - Header Notifications " + (result_notifications.get(0) ? "Passed" : "Failed"));
			System.out.println(" - Initiative Notifications " + (result_notifications.get(1) ? "Passed" : "Failed"));
			System.out.println(" - Tag Filter Search " + (result_notifications.get(0) ? "Passed" : "Failed"));
	
//			//Testing People
			result_people = moduleTesting.testPeople();
			
			System.out.println(" - Add People" + (result_people.get(0) ? "Passed" : "Failed"));
			System.out.println(" -  Delete People " + (result_notifications.get(1) ? "Passed" : "Failed"));
			
			//Testing Transfer
			result_transfer = moduleTesting.testTransfers();
			
			System.out.println(" - Transfering Tokens To User (Direct) " + (result_transfer.get(0) ? "Passed" : "Failed"));
			System.out.println(" - Transfering Tokens To User (Peer Reviewed) " + (result_transfer.get(1) ? "Passed" : "Failed"));
			
			//Testing Profile
			result_profile = moduleTesting.testProfile();
			
			System.out.println(" - Editing Profile " + (result_profile.get(0) ? "Passed" : "Failed"));
			
			//Testing Tokens
			result_tokens = moduleTesting.testTokens();
			
			System.out.println(" - Creating Brand New Tokens " + (result_tokens.get(0) ? "Passed" : "Failed"));
			
		}catch(Exception exception) {
			System.out.println("Failed @ ModuleTesting() : " + exception);
		}
		
	}



}

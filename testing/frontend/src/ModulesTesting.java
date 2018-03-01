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
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		driver.get("http://localhost:8080");
	}
	
	public boolean testAuthentication() {
		try {
			AuthenticationModule authModule = new AuthenticationModule("check");
			System.out.println("1. Testing Authentication Modal");
			if(authModule.checkLogin("philh@x.com","123456",driver))
				return true;

		}catch(Exception exception){
			System.out.println("Failed @ testAuthentication(): " + exception);
			return false;
		}
		return true;
	}
	
	public List<Boolean> testInitiatives() {

		InitiativesModule initiativeModule = new InitiativesModule();
		List<Boolean> result = new ArrayList<Boolean>();

		System.out.println("2. Testing Initiative Modals");
		
		try {
			result.add(initiativeModule.createInitiative(driver));
			result.add(initiativeModule.editInitiative(driver));
			result.add(initiativeModule.deleteInitiative(driver));
		}catch(Exception exception){
			System.out.println("Failed @ testInitiatives(): " + exception);
			
		}
		
		return result;
	}

	private List<Boolean> testNotifications() {

		NotificationsModule notificationModule = new NotificationsModule();
		List<Boolean> result = new ArrayList<Boolean>();

		System.out.println("3. Testing Notifications");
		
		try {
			result.add(notificationModule.checkHeaderNotification(driver));
			result.add(notificationModule.checkInitiativeNotification(driver));
		}catch(Exception exception){
			System.out.println("Failed @ testNotifications(): " + exception);
			
		}
		
		return result;
	}

	private List<Boolean> testPeople() {

		PeopleModule notificationModule = new PeopleModule();
		List<Boolean> result = new ArrayList<Boolean>();

		System.out.println("4. Testing People");
		
		try {
			result.add(notificationModule.addPeople(driver));
			//result.add(notificationModule.deletePeople(driver));
		}catch(Exception exception){
			System.out.println("Failed @ testNotifications(): " + exception);
			
		}
		
		return result;
	}

	public static void main(String args[]) {


		List<Boolean> result_initiatives;
		List<Boolean> result_notifications;
		List<Boolean> result_people;
		
		ModulesTesting moduleTesting = new ModulesTesting();
		
		System.out.println("Testing Modules");
		
		if(moduleTesting.testAuthentication())
			System.out.println("Authentication Modal Passed");
		else {
			System.out.println("Authentication Modal Failed");
		}
		
		//Testing Initiative
		//result_initiatives.push(moduleTesting.testInitiatives());
		
//		result_initiatives = moduleTesting.testInitiatives();
//		
//		System.out.println(" - Creating Initiative Modal " + (result_initiatives.get(0) ? "Passed" : "Failed"));
//		System.out.println(" - Editing Initiative Modal " + (result_initiatives.get(1) ? "Passed" : "Failed"));
//		System.out.println(" - Deleting Initiative Modal " + (result_initiatives.get(2) ? "Passed" : "Failed"));
//		
//		result_notifications = moduleTesting.testNotifications();
//		
//		System.out.println(" - Header Notifications " + (result_notifications.get(0) ? "Passed" : "Failed"));
//		System.out.println(" - Initiative Notifications " + (result_notifications.get(1) ? "Passed" : "Failed"));

		result_people = moduleTesting.testPeople();
		
		System.out.println(" - Add People" + (result_people.get(0) ? "Passed" : "Failed"));
		//System.out.println(" -  Edit People " + (result_notifications.get(1) ? "Passed" : "Failed"));
	}


}

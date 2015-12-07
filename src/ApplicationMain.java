import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ApplicationMain {

	private static String username = "";
	private static String password = "";
	private static String fromStationString = "CHENNAI CENTRAL - MAS";
	private static String toStationString = "HYDERABAD DECAN - HYB";
	private static String journeyDateString = ""; //DD-MM-YYYY
	private static String trainNo = "";
	private static String trainClass = "SL";
	private static String DebitCardBank = "State Bank of India";
	private static String DebitCardNumber = "";
	private static int DebitCardExpiryMonth = 11;
	private static int DebitCardExpiryYear = 2022;
	private static String DebitCardHolderName = "";
	private static String ATMPin = "";
	
	
	//Number of passengers must be less than or equal to 6
	//Senior citizens not supported as of yet.. Alert boxes need to be handled
	@SuppressWarnings("serial")
	private static List<Passenger> passengers = new ArrayList<Passenger>(){{
		add(new Passenger("Prakash D", 23, "Male", "", false));
	}};
	
	
	static int captchaWaitTime = 8000;
	

	static WebDriver driver;
	
	static void waitForCaptchaTyping() throws InterruptedException{
		System.out.println("Waiting to enter captcha");
		Thread.sleep(captchaWaitTime);
	}
	
	static void loginIRCTC() throws InterruptedException{
		driver.get("https://www.irctc.co.in/");
		WebElement usernameBox = driver.findElement(By.id("usernameId"));
		usernameBox.sendKeys(username);
		WebElement passwordBox = driver.findElement(By.xpath("//input[@type='password']"));
		passwordBox.sendKeys(password);
		WebElement captchaBox = driver.findElement(By.xpath("//input[@class='loginCaptcha']"));
		captchaBox.click();		
		waitForCaptchaTyping();
		WebElement loginButton = driver.findElement(By.id("loginbutton"));
		loginButton.click();
	}
	
	
	static void searchTrains(){
		WebElement fromStationBox = driver.findElement(By.id("jpform:fromStation"));
		WebElement toStationBox = driver.findElement(By.id("jpform:toStation"));
		WebElement journeyDateBox = driver.findElement(By.id("jpform:journeyDateInputDate"));
		fromStationBox.sendKeys(fromStationString);
		toStationBox.sendKeys(toStationString);	
		journeyDateBox.sendKeys(journeyDateString);
		WebElement submitButton = driver.findElement(By.id("jpform:jpsubmit"));
		submitButton.click();		
	}
	
	static void selectThatkal(){
		driver.findElement(By.xpath("//td[input[@name='quota']][2]/input")).click();
	}
	
	static void selectTrainAndCoach(){
		WebElement trainTable = driver.findElement(By.id("avlAndFareForm:trainbtwnstns:tb"));
		WebElement trainRow = trainTable.findElement(By.xpath(".//tr[td//text()[starts-with(., '"+trainNo+"')]]"));
		WebElement trainClassElement = trainRow.findElement(By.linkText(trainClass));
		trainClassElement.click();
		WebDriverWait wait = new WebDriverWait(driver, 8000);
		By bookNowXpath = By.linkText("Book Now");
		wait.until(ExpectedConditions.visibilityOfElementLocated(bookNowXpath));
		WebElement bookNowLink = driver.findElement(bookNowXpath);
		bookNowLink.click();
	}
	
	static void fillPassengerDetails() throws InterruptedException{
		By passengerTableBodyXpath = By.xpath("//tbody[@id='addPassengerForm:psdetail:tb']");
		WebDriverWait wait = new WebDriverWait(driver, 8000);
		wait.until(ExpectedConditions.visibilityOfElementLocated(passengerTableBodyXpath));
		WebElement passengerBody = driver.findElement(passengerTableBodyXpath);
		for(int i=0;i<passengers.size(); i++){
			Passenger passenger = passengers.get(i);
			WebElement rowElement = passengerBody.findElement(By.xpath(".//tr["+(i+1)+"]"));
			WebElement nameElement = rowElement.findElement(By.xpath(".//td[2]/input"));
			WebElement ageElement = rowElement.findElement(By.xpath(".//td[3]/input"));
			WebElement genderElement = rowElement.findElement(By.xpath(".//td[4]/select/option[contains(text(), '"+passenger.gender+"')]"));			
			nameElement.sendKeys(passenger.name);
			ageElement.sendKeys(Integer.toString(passenger.age));
			genderElement.click();
			if(passenger.berthPreference != null && passenger.berthPreference.length() > 0){
				WebElement berthElement = rowElement.findElement(By.xpath(".//td[5]/select/option[contains(text(), '"+passenger.berthPreference+"')]"));
				berthElement.click();
			}
//			if(passenger.isSeniorCitizen){
//				WebElement seniorCitizenElement = rowElement.findElement(By.xpath(".//td[6]/input"));
//				seniorCitizenElement.click();
//			}		
		}
		WebElement captchaElement = driver.findElement(By.xpath("//input[contains(@class,'captcha-answer')]"));
		captchaElement.click();
		waitForCaptchaTyping();
		WebElement nextButton = driver.findElement(By.xpath("//input[@id='validate']"));
		nextButton.click();
	}
	
	static void selectBank(){
		driver.findElement(By.xpath("//table[@class='paymentOption']//td[@id='DEBIT_CARD']")).click();
		WebElement listOfBanks = driver.findElement(By.xpath("//td[@class='selected-bank-list DEBIT_CARD']"));
		WebElement bankElement = listOfBanks.findElement(By.xpath(".//td[text()[contains(.,'"+DebitCardBank+"')]]/input"));
		bankElement.click();
		WebElement makePaymentButton = driver.findElement(By.xpath("//input[@id='validate']"));
		makePaymentButton.click();
	}
	
	static void fillSBICardDetails() throws InterruptedException{
		System.out.println("Filling card details..");
		driver.findElement(By.id("debitCardNumber")).sendKeys(DebitCardNumber);
		driver.findElement(By.xpath("//select[@id='debiMonth']/option[@value='"+DebitCardExpiryMonth+"']")).click();
		driver.findElement(By.xpath("//select[@id='debiYear']/option[@value='"+DebitCardExpiryYear+"']")).click();		
		driver.findElement(By.id("debitCardholderName")).sendKeys(DebitCardHolderName);
		driver.findElement(By.id("cardPin")).sendKeys(ATMPin);
		driver.findElement(By.id("passline")).click();
		waitForCaptchaTyping();
		driver.findElement(By.xpath("//div[@id='proceedCancel']/input[@id='proceed']")).click();
	}
	
	public static void main(String args[]) throws InterruptedException{
		driver = new FirefoxDriver(DesiredCapabilities.firefox());
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		try{
			loginIRCTC();
		}
		catch(Exception e){
			System.out.println("RESOLVE ISSUE WITHIN 8 seconds");
			Thread.sleep(8000);
			loginIRCTC();
		};
		try{
			searchTrains();
		}
		catch(Exception e){
			System.out.println("RESOLVE ISSUE WITHIN 8 seconds");
			Thread.sleep(8000);			
			searchTrains();
		}
		try{
			selectThatkal();
		}
		catch(Exception e){
			System.out.println("RESOLVE ISSUE WITHIN 8 seconds");
			Thread.sleep(8000);			
			selectThatkal();
		}
		try{
			selectTrainAndCoach();
		}
		catch(Exception e){
			System.out.println("RESOLVE ISSUE WITHIN 8 seconds");
			Thread.sleep(8000);	
			selectTrainAndCoach();
		}
		try{
			fillPassengerDetails();
		}
		catch(Exception e){
			System.out.println("RESOLVE ISSUE WITHIN 8 seconds");
			Thread.sleep(8000);			
			fillPassengerDetails();
		}
		try{
			selectBank();
		}
		catch(Exception e){
			System.out.println("RESOLVE ISSUE WITHIN 8 seconds");
			Thread.sleep(8000);	
			selectBank();
		}
		if(DebitCardBank.compareTo("State Bank of India")==0){
			fillSBICardDetails();
		}
	}
}

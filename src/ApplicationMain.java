import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ApplicationMain {

	private static String username = "cegwitty";
	private static String password = "DoNotForma";
	private static String fromStationString = "CHENNAI CENTRAL - MAS";
	private static String toStationString = "HYDERABAD DECAN - HYB";
	private static String journeyDateString = "09-02-2016"; //DD-MM-YYYY
	private static String trainNo = "12759";
	private static String trainClass = "SL";
	
	static int captchaWaitTime = 8000;
	

	static WebDriver driver;
	static void loginIRCTC() throws InterruptedException{
		driver.get("https://www.irctc.co.in/");
		WebElement usernameBox = driver.findElement(By.id("usernameId"));
		usernameBox.sendKeys(username);
		WebElement passwordBox = driver.findElement(By.xpath("//input[@type='password']"));
		passwordBox.sendKeys(password);
		System.out.println("Waiting to enter captcha");
		Thread.sleep(captchaWaitTime);
		System.out.println("Clicking login button");
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
		//TODO : Click on captcha box automatically to prepare typing
		WebElement submitButton = driver.findElement(By.id("jpform:jpsubmit"));
		submitButton.click();		
	}
	
	static void selectTrainAndCoach(){
		WebElement trainTable = driver.findElement(By.id("avlAndFareForm:trainbtwnstns:tb"));
		WebElement trainRow = trainTable.findElement(By.xpath(".//tr[td//text()[starts-with(., '"+trainNo+"')]]"));
		WebElement trainClassElement = trainRow.findElement(By.linkText(trainClass));
		trainClassElement.click();
		WebDriverWait wait = new WebDriverWait(driver, 8000);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Book Now")));
		WebElement bookNowLink = driver.findElement(By.linkText("Book Now"));
		bookNowLink.click();
	}
	
	static void fillPassengerDetails(){
		WebElement passengerBody = driver.findElement(By.xpath("addPassengerForm:psdetail:tb"));
		
	}
	
	
	public static void main(String args[]) throws InterruptedException{
		driver = new FirefoxDriver(DesiredCapabilities.firefox());
		driver.manage().window().maximize();
		loginIRCTC();
		searchTrains();
		selectTrainAndCoach();
		
	}
}

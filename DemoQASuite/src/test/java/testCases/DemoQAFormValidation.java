package testCases;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.edge.EdgeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class DemoQAFormValidation {
	
		  public static WebDriver driver;
		  public static WebElement element;
	
		  @Test
		  public void test() throws Exception {
			      
			  
			  	driver = WebDriverManager.chromedriver().create();
			  	
			  	driver.get("https://demoqa.com/automation-practice-form");
			  	driver.manage().window().maximize();
			  	Thread.sleep(4000);
//			  	driver.findElement(By.xpath("//*[normalize-space()='Book Store Application']"));
//			  	locateElement("XPATH","//*[normalize-space()='Book Store Application']").click();
//			  	Thread.sleep(3000);
//			  	locateElement("XPATH","//div[normalize-space()='Forms']").click();
//				Thread.sleep(2000);
//			  	locateElement("XPATH","//span[normalize-space()='Practice Form']").click();
				Thread.sleep(1000);
			  	locateElement("CSS", "input#firstName").sendKeys("First Name");
			  	locateElement("CSS", "input#lastName").sendKeys("Last Name");
			  	locateElement("CSS", "input#userEmail[class$='form-control']").sendKeys("email.email");
			  //locateElement("CSS", "input#gender-radio-1").click();
			  	locateElement("CSS", "input#userNumber").sendKeys("mobileNum");
			  	//Date
			    locateElement("CSS","input#dateOfBirthInput").sendKeys("09 Dec 2023");
			  	System.out.println("Date");
			  	locateElement("XPATH", "//*[@id='subjectsContainer']/div/div[1]").sendKeys("SubjectContainer");
			  	System.out.println("subject");
			  	locateElement("CSS", "label[for='hobbies-checkbox-1']").click();
			  	System.out.println("Checkbox");
			  	//Upload Picture
			  	locateElement("css","input#uploadPicture").sendKeys("./DemoQASuite/src/test/resources/OtherDocuments/sampleFile.jpeg");
			  	locateElement("css","textarea[placeholder='Current Address']").sendKeys("Current Address");
			  	locateElement("XPATH","(//div[@class=' css-tlfecz-indicatorContainer'])[1]").click();
			  		locateElement("XPATH","//div[text()='NCR']").click();
			  	locateElement("XPATH","(//div[@class=' css-tlfecz-indicatorContainer'])[2]").click();
			  		locateElement("XPATH","//div[text()='Delhi']").click();
			  	locateElement("CSS","button#submit").click();
			  	
			  	
		  }
		  
		  public static WebElement locateElement(String locatorType , String locator) {
			  switch (locatorType.toLowerCase()) {
			  case "xpath":
				  element = driver.findElement(By.xpath(locator));
			  case "css":
				  element = driver.findElement(By.cssSelector(locator));
				  		  }
			return element;
		  }
		  
		  @AfterTest
		  public void afterTestMethod() {
			  driver.quit();
		  }
}

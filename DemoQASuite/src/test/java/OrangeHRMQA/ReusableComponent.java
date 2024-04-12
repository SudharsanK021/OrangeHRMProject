package OrangeHRMQA;


import static org.testng.Assert.*;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.github.bonigarcia.wdm.WebDriverManager;


public class ReusableComponent {

	public static WebDriver driver;
	public static WebElement element;
	public static String recordCountStr;
	public static int recCountInt;
	public static String cellValue;
	public static List <List<String>> commonTableRecord; 
	public static int nestedArraySize;
	public static int recordTableRowIndex = 0 ; 
	public static int recordTableColumnIndex = 0 ; 



	//Soft Assert instantiation
	SoftAssert softassert = new SoftAssert();

	@BeforeTest
	public void browserInitialization() throws Exception{

		driver = WebDriverManager.chromedriver().create();

		driver.get("https://opensource-demo.orangehrmlive.com/");

		driver.manage().window().maximize();

		Thread.sleep(5000);

	}

	@Parameters({"loginUserName","loginPassword" })
	@Test(testName = "ValidLogin", enabled = true, priority = 0)
	public void login(String username, String password) throws Exception {

		String forgotPassword = locateElement("XPATH","//p[normalize-space()='Forgot your password?']").getText();
		String LoginPageHeader = locateElement("XPATH","//h5").getText();

		//Login Page

		softassert.assertEquals(forgotPassword, "Forgot your password?");
		softassert.assertEquals(LoginPageHeader, "Login");
		locateElement("CSS","input[name='username']").sendKeys(username);
		locateElement("CSS","input[name='password']").sendKeys(password);

		//Assert the Elements before Login
		softassert.assertAll();

		Thread.sleep(3000);

		locateElement("CSS","button[type='submit']").click();

		Thread.sleep(5000);

		System.out.println("LoginDone");


	}


	@Parameters({"loginUserName","loginPassword" })
	@Test(testName = "InvalidLogin", enabled = false, priority = 1)
	public void invalidLogin(String username, String password) throws Exception {

		locateElement("CSS","input[name='username']").sendKeys("username");
		locateElement("CSS","input[name='password']").sendKeys("password", Keys.ENTER);

		Thread.sleep(2000);

		softassert.assertEquals(locateElement("XPATH","//p[text()='Invalid credentials']").getText(), "Invalid credentials", "Credentials entered are valid");
		softassert.assertAll();



	}

	@Test(testName = "DashboardAssertion", enabled = true, priority = 1)
	public void dashboardAssertionUponLogin() {


		assertEquals(true,locateElement("XPATH","//img[@alt='client brand banner']").isDisplayed(),"Sucess1");
		assertEquals("Dashboard",locateElement("XPATH","//header//*[text()='Dashboard']").getText(),"Sucess2");
		//assertEquals("Paul Collings",driver.findElement(By.cssSelector("p.oxd-userdropdown-name")).getText(),"Login is unsuccessfull");
		softassert.assertAll();
		System.out.println("DashboardDone");

	}


	// Leave Assignment Workflow
	@Test(testName = "LeavesScreen", enabled = true, priority = 2)
	public void navigateToLeavesScreen() throws Exception {

		WebElement leaveMenu = driver.findElement(By.xpath("//span[text()='Leave']")); 
		assertEquals("Leave",leaveMenu.getText(),"Leave menu is not present");
		leaveMenu.click();

		Thread.sleep(3000);

		softassert.assertAll();
		System.out.println("LeavesDone");

	}

	@Test
	public void validaterequiredFields() throws Exception{

		Thread.sleep(2000);
		locateElement("XPATH","//*[text()=' Assign ']").click();
		Thread.sleep(2000);

		assertEquals("Required",locateElement("XPATH","(//*[text()='Required'])[1]").getText());
		assertEquals("Required",locateElement("XPATH","(//*[text()='Required'])[2]").getText());
		assertEquals("Required",locateElement("XPATH","(//*[text()='Required'])[3]").getText());


	}

	@Parameters("UserName")
	@Test(testName = "AssignLeavesScreen", enabled = true, priority = 3)
	public void assignLeavesToUser(String userName) throws Exception {


		WebElement assignLeave = driver.findElement(By.xpath("//*[text()='Assign Leave']"));
		assertEquals("Assign Leave",assignLeave.getText(),"Assign Leave menu is not present");
		assignLeave.click();

		Thread.sleep(3000);
		//validaterequiredFields();

		LocalDate currentDateNow = LocalDate.now();
		String currentDate = currentDateNow.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));


		//		LocalDate nextDateNow  = currentDateNow.plusDays(1);
		//		String nextDayDate = nextDateNow.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		locateElement("XPATH","//input[@placeholder='Type for hints...']").sendKeys(userName);
		Thread.sleep(3000);
		locateElement("XPATH","//*[text()='"+userName+"']").click();
		Thread.sleep(3000);
		locateElement("XPATH","//*[text()='-- Select --']").click();
		Thread.sleep(2000);
		locateElement("XPATH","//*[text()='CAN - Bereavement']").click();
		Thread.sleep(2000);
		locateElement("XPATH","//*[text()='From Date']//following::input[1]").sendKeys(currentDate);
		//		Thread.sleep(5000);
		//		WebElement nextDate = locateElement("XPATH","//*[text()='From Date']//following::input[2]");
		//		nextDate.click();
		//		nextDate.clear();
		//		nextDate.sendKeys(nextDayDate);
		Thread.sleep(2000);
		locateElement("XPATH","//*[text()='Comments']//following::textarea[1]").sendKeys("Comments");
		Thread.sleep(2000);
		locateElement("XPATH","//*[text()=' Assign ']").click();

		Thread.sleep(3000);

		//Validate Remaining Amount of Leaves
		remainingLeaves();

		Thread.sleep(3000);
		softassert.assertAll();

	}

	@Parameters({"UserName"})
	@Test(testName = "GenerateEmployeeLeaveReport", enabled = true, priority = 3)
	public void generateEmployeeLeaveReport(String userName) throws Exception{

		Thread.sleep(3000);

		locateElement("Xpath", "//div[@class='oxd-topbar-body']//*[normalize-space()='Reports']").click();
		Thread.sleep(3000);
		locateElement("Xpath", "//*[normalize-space()='Leave Entitlements and Usage Report']").click();
		Thread.sleep(3000);
		locateElement("Xpath", "//*[text()='Employee']").click();
		Thread.sleep(3000);
		locateElement("Xpath", "//*[text()='Employee Name']//following::input[1]").sendKeys(userName);
		Thread.sleep(3000);
		locateElement("XPATH","//*[text()='"+userName+"']").click();
		Thread.sleep(3000);
		locateElement("Xpath", "//*[text()=' Generate ']").click();
		Thread.sleep(3000);

		// Retrieve Record Count
		recordCountStr = locateElement("Xpath", "//div[@class='oxd-report-table-header']//span[1]").getText();
		recCountInt = retrieveRecordCount(recordCountStr);

		retrieveTableRecords(recCountInt);

		softassert.assertAll();




	}

	////////////////*******************************************************************************************************
	////////////////*******************************************************************************************************
	////////////////*******************************************************************************************************

	@Test(testName = "GenerateMyLeaveReport", enabled = true, priority = 3)
	public void generateMyLeaveReport() throws Exception{

		Thread.sleep(3000);

		locateElement("Xpath", "//div[@class='oxd-topbar-body']//*[normalize-space()='Reports']").click();
		Thread.sleep(3000);
		locateElement("Xpath", "//*[normalize-space()='My Leave Entitlements and Usage Report']").click();
		Thread.sleep(3000);
		locateElement("Xpath", "//*[text()=' Generate ']").click();
		Thread.sleep(3000);

		// Retrieve Record Count
		recordCountStr = locateElement("Xpath", "//div[@class='oxd-report-table-header']//span[1]").getText();
		recCountInt = retrieveRecordCount(recordCountStr);

		/*
		//retrieveTableRecords(recCountInt);

		commonTableRecord = retrieveTableRecords(recCountInt);


		nestedArraySize = commonTableRecord.size();

		// Fetch Table Records

		fetchRecordsFromNestedArray(commonTableRecord);

		 */	   
		
		commonTableRecord = fetchColumnValues(recCountInt);
		
		fetchRecordsFromNestedArray(commonTableRecord);
		
		String filePath = "C:\\Users\\sudharsa\\OneDrive - Capgemini\\Documents\\Eclipse Workspace\\Practice\\DemoQASuite\\src\\test\\java\\OrangeHRMQA\\LeaveReport.xlsx";
		
		writeIntoExcel(filePath, "MyLeaveReport" , commonTableRecord);

		softassert.assertAll();

	}  

	public static WebElement locateElement(String locatorType , String locator) {
		switch (locatorType.toLowerCase()) {
		case "xpath":
		{
			element = driver.findElement(By.xpath(locator));
			break;
		}
		case "css":
		{
			element = driver.findElement(By.cssSelector(locator));
			break;
		}
		}
		return element;
	}


	public void remainingLeaves() throws Exception {

		String remainingLeaves = locateElement("XPATH", "//*[text()='Leave Balance']//following::p[1]").getText();
		System.out.println(remainingLeaves);
		if(remainingLeaves.substring(1) == "0" || remainingLeaves=="Balance not sufficient") {

			String confirmSubmissionMsg = "Employee does not have sufficient leave balance for leave request. Click OK to confirm leave assignment.";

			assertEquals(confirmSubmissionMsg, locateElement("XPATH", "//*[text()='Employee does not have sufficient leave balance for leave request. Click OK to confirm leave assignment.']").getText());
			assertEquals("Confirm Leave Assignment", locateElement("XPATH", "//*[text()='Confirm Leave Assignment']']").getText());

			Thread.sleep(3000);
			locateElement("XPATH", "//*[text()=' Ok ']").click();


		}


	}

	public int retrieveRecordCount(String recordCountStr) {

		String rC = recordCountStr.substring(recordCountStr.indexOf("(")+1, recordCountStr.indexOf(")"));

		return Integer.parseInt(rC);

	}

	public List <List<String>> retrieveTableRecords(int rowCount) {

		List<WebElement> headerElements = driver.findElements(By.xpath("//div[@class='rgHeaderCell']"));
		List<WebElement> bodyElements 	= driver.findElements(By.xpath("//div[contains(@class,'rgCell')]"));

		int tableColumnCount = headerElements.size();

		List <List<String>> recordTable = new ArrayList<>(); 
		List<String> recordTableRow = new ArrayList<>();

		int recordTableRowIndex = 0 ; 

		// Insert Header Data
		if(headerElements.size() != 0 ) {

			for (int tableHeaderCrawler = 0; tableHeaderCrawler < headerElements.size(); tableHeaderCrawler++) {

				recordTableRow.add(tableHeaderCrawler, headerElements.get(tableHeaderCrawler).getText());	

				System.out.println(headerElements.get(tableHeaderCrawler).getText());

			}

		}else {
			System.out.println("Table Header is blank");
		}
		recordTable.add(recordTableRowIndex, recordTableRow);

		recordTableRow = new ArrayList<>();

		recordTableRowIndex = recordTableRowIndex + 1;



		//Insert Body Data
		if(bodyElements.size() != 0 ) {

			int tableBodyCrawler = 0;
			int totalColumnCrawled = 0;

			while(totalColumnCrawled < bodyElements.size()) {

				recordTableRow.add(tableBodyCrawler, bodyElements.get(totalColumnCrawled).getText());

				tableBodyCrawler++;
				totalColumnCrawled++;

				if(tableBodyCrawler % tableColumnCount == 0) {

					tableBodyCrawler = 0 ;

					recordTable.add(recordTableRowIndex, recordTableRow);

					recordTableRow = new ArrayList<>();

					recordTableRowIndex++;


				}

			}

		}else {
			System.out.println("Table Body is blank");
		}

		return recordTable;

	}


	public List<List<String>> fetchColumnValues(int rowCount) {


		List<WebElement> headerElements = driver.findElements(By.xpath("//div[@class='rgHeaderCell']"));
		//List<WebElement> bodyElements 	= driver.findElements(By.xpath("//div[contains(@class,'rgCell')]"));
		
		int horizontalrowIndex = 1 ; 

		int tableColumnCount = headerElements.size();

		List <List<String>> recordTable = new ArrayList<>(); 
		List<String> recordTableRow = new ArrayList<>();

		// Insert Header Data
		if(headerElements.size() != 0 ) {

			for (int tableHeaderCrawler = 0; tableHeaderCrawler < tableColumnCount; tableHeaderCrawler++) {

				recordTableRow.add(tableHeaderCrawler, headerElements.get(tableHeaderCrawler).getText());	

				System.out.println(headerElements.get(tableHeaderCrawler).getText());

			}

		}else {

			System.out.println("Table Header is blank");

		}

		recordTable.add(recordTableRowIndex, recordTableRow);

		recordTableRow = new ArrayList<>();

		recordTableRowIndex = recordTableRowIndex + 1;

		recordTableColumnIndex = recordTableColumnIndex + 1 ;

		System.out.println("Row Index from Header : "+recordTableRowIndex);

		// Vertical Retrieval

		for (int i = 0; i < rowCount*2 ; i++) {

			if ( i < rowCount) {

				recordTableRow.add(0, driver.findElement(By.xpath("(//div[contains(@class,'rgCell')])["+(i+1)+"]")).getText());

				System.out.println(" Vertical Row Index : "+recordTableRowIndex);

				recordTable.add(recordTableRowIndex, recordTableRow);

				recordTableRow = new ArrayList<>();

				recordTableRowIndex = recordTableRowIndex + 1;

				recordTableColumnIndex = recordTableColumnIndex + 1 ; 

			}
			else {

				// Horizontal Retrieval

				for (int j = 0; j < (tableColumnCount -1); j++) {

					recordTableRow.add(0, driver.findElement(By.xpath("(//div[contains(@class,'rgCell')])["+recordTableColumnIndex+"]")).getText());

					System.out.println("Column Index : "+recordTableColumnIndex);

					recordTableColumnIndex = recordTableColumnIndex + 1 ; 

				}

				//recordTableRowIndex = 1 ; 

				//recordTable.add(recordTableRowIndex, recordTableRow);
				
				recordTable.get(horizontalrowIndex).addAll(recordTableRow);

				recordTableRow = new ArrayList<>();

				horizontalrowIndex = horizontalrowIndex + 1;

			}

		}

		System.out.println(recordTable.size());
		return recordTable;


	}
	
	public void writeIntoExcel(String filePath, String sheetName, List<List<String>> tableData) throws Exception{
		
		XSSFWorkbook testWorkBook = new XSSFWorkbook();

		XSSFSheet sheet = testWorkBook.createSheet(sheetName);
		
		for (int i = 0; i < tableData.size(); i++) {
			
				List<String> tableRowData = tableData.get(i);
				
				Row tRow  = sheet.createRow(i);
				
				for (int j = 0; j < tableRowData.size(); j++) {
					
					tRow.createCell(j).setCellValue(tableRowData.get(j));
					
				}
			
		}
		
		File file = new File(filePath);

		FileOutputStream output = new FileOutputStream(file);

		testWorkBook.write(output);
		testWorkBook.close();
		output.close();
		
		
		System.out.println("Table Data written into Excel successfully");
		
	}






	//////////////////////////////
	// Get Size of Nested Array
	//////////////////////////////

	public void fetchRecordsFromNestedArray(List<List<String>> nestedArray) {

		//Retrieve the Nested List values

		for (int i = 0; i < nestedArray.size(); i++) {

			List <String> tempRow = nestedArray.get(i);

			for (int j = 0; j < tempRow.size(); j++) {

				System.out.print(" "+tempRow.get(j));

			}System.out.println();
		}


	}


	public void mayCommentLater(int rowCount) {

		List <List<String>> recordTable = new ArrayList<>(); 
		List<String> rowElements = new ArrayList<>();
		List<WebElement> columnElements = driver.findElements(By.xpath("//div[@class='rgHeaderCell']"));
		int columnCount = columnElements.size();

		//////////////////////////////////////
		// Retrieve Header Record First
		//////////////////////////////////////
		for (int i = 0; i < columnCount; i++) {

			cellValue = locateElement("XPATH", "(//div[@class='rgHeaderCell'])["+(i+1)+"]").getText();
			rowElements.add(i, cellValue);
		}


		//////////////////////////////////////
		//Insert header Record into TableList
		//////////////////////////////////////
		recordTable.add(0, rowElements);
		rowElements = new ArrayList<String>();



		Iterator<WebElement> wB = columnElements.iterator();


		while(wB.hasNext()) {
			System.out.println(wB.next().getText());
		}

		//////////////////////////////////////
		// Retrieve Table Body Records
		//////////////////////////////////////

		List<WebElement> bodyElements = driver.findElements(By.xpath("//div[contains(@class,'rgCell')]"));
		wB = bodyElements.iterator();


		while(wB.hasNext()) {
			System.out.println(wB.next().getText());

		}

	}


	@AfterTest
	public void browserTearDown() {

		driver.quit();

	}

}

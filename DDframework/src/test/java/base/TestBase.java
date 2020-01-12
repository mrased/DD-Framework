package base;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import utilities.ExcelReader;

/**
 * We will be initializing in this TestBase class: WebDriver, Properties, Logs
 * and Excel File Reader
 * 
 * @author SMR
 *
 */
public class TestBase {
	public static final Properties OR = new Properties();
	public static final Properties Config = new Properties();
	//public static final Logger log = Logger.getLogger("devpinoyLogger");
	public static Logger log = Logger.getLogger("devpinoyLogger");
	public static final ExcelReader excel = new ExcelReader(
			System.getProperty("user.dir") + "\\src\\test\\resources\\excel\\testdata.xlsx");

	public boolean blnResult = false;

	public static WebDriver driver;
	public static FileInputStream fis;
	public static WebDriverWait wait;

	@BeforeSuite
	public void setUp() {
		if (driver == null) {
			loadConfigFile();
			loadORFile();
		}

		// Browser= FF
		if (Config.getProperty("browser").equals("firefox")) {
			launchInFireFox();

			// Browser= Chrome
		} else if (Config.getProperty("browser").equals("chrome")) {
			launchInChrome();

			// Browser=IE
		} else if (Config.getProperty("browser").equals("ie")) {
			launchInIE();
		}

		/**********************************************************************************
		 * Navigate to URL, Maximize the browser window and set Implicit and
		 * Explicit wait
		 ***********************************************************************************/
		driver.get(Config.getProperty("testsiteurl"));
		log.debug("Navigated to: " + Config.getProperty("testsiteurl"));
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Integer.parseInt(Config.getProperty("implicit.wait")),
				TimeUnit.SECONDS);
		wait = new WebDriverWait(driver, Integer.parseInt(Config.getProperty("explicit.wait")));
		click("loginprofile");
	}

	private void launchInIE() {
		System.setProperty("webdriver.ie.driver",
				System.getProperty("user.dir") + "\\src\\test\\resources\\executables\\IEDriverServer.exe");
		driver = new InternetExplorerDriver();
		log.debug("Launching IE");
	}

	private void launchInChrome() {
		/****************************************************
		 * Handaling Chrome Pop-Up
		 *****************************************************/

		// Create a map to store preferences
		Map<String, Object> prefs = new HashMap<String, Object>();

		// Pass the argument 1 to allow and 2 to block
		prefs.put("profile.default_content_setting_values.notifications", 2);
		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("prefs", prefs);
		System.setProperty("webdriver.chrome.driver",
				System.getProperty("user.dir") + "\\src\\test\\resources\\executables\\chromedriver.exe");
		driver = new ChromeDriver(options);
		log.debug("Launching Chrome");
	}

	private void launchInFireFox() {
		System.setProperty("webdriver.gekodriver.driver",
				System.getProperty("user.dir") + "\\src\\test\\resources\\executables\\geckodriver.exe");
		driver = new FirefoxDriver();
		DesiredCapabilities capabilities = DesiredCapabilities.firefox();
		capabilities.setCapability("marionette", true);
		log.debug("Launching Firefox");
	}

	private void loadORFile() {
		try {
			fis = new FileInputStream(
					System.getProperty("user.dir") + "\\src\\test\\resources\\properties\\OR.properties");
			log.debug("OR properties file found");

			OR.load(fis);
			log.debug("OR properties file loaded");
		} catch (Exception e) {
			log.error("Problem loading the OR file - ", e);
		}
	}

	private void loadConfigFile() {
		try {
			fis = new FileInputStream(
					System.getProperty("user.dir") + "\\src\\test\\resources\\properties\\Config.properties");
			log.debug("Config properties file found");

			BasicConfigurator.configure();
			Config.load(fis);
			log.info("Config properties file loaded");
		} catch (Exception e) {
			log.error("Problem loading the config file - ", e);
		}
	}

	/*****************************************************************************************
	 * Custom isElementPresent method, return true/false if locator element is
	 * present/absent
	 ******************************************************************************************/
	public static boolean isElementPresent(String locator) {
		try {
			driver.findElement(By.xpath(locator));
			return true;
		} catch (NoSuchElementException e) {
			log.error("Element Not Found - ", e);
			return false;
		}

	}

	/*******************************
	 * Create custom Keyword - Click
	 *********************************/
	public void click(String locator) {
		try {
			driver.findElement(By.xpath(OR.getProperty(locator))).click();
		} catch (NoSuchElementException e) {
			log.error("Element Not Found - ", e);
		}
	}

	/*******************************
	 * Custom Page Title
	 *********************************/
	public boolean titlevalidation(String expectedTitle) {
		if (driver.getTitle().equals(expectedTitle)) {
			Assert.assertTrue(driver.getTitle().equals(expectedTitle), "FAILED- title not matched");
			blnResult = true;
		} else {
			blnResult = false;
		}
		return blnResult;
	}

	/***********************************************
	 * Create custom Keyword - Type <String, String>
	 ************************************************/
	public void type(String locator, String value) {
		try {
			driver.findElement(By.xpath(OR.getProperty(locator))).sendKeys(value);
		} catch (NoSuchElementException e) {
			log.error("Element Not Found - ", e);
		}
	}

	/***********************************************
	 * Create custom Keyword - Type <String, Keys>
	 ************************************************/
	public void type(String locator, Keys value) {
		try {
			driver.findElement(By.xpath(OR.getProperty(locator))).sendKeys(value);
		} catch (NoSuchElementException e) {
			log.error("Element Not Found - ", e);
		}
	}

	/********************************
	 * Create custom Keyword - Select
	 **********************************/
	public void select(String locator, String value) {
		try {
			WebElement dropdown = driver.findElement(By.xpath(OR.getProperty(locator)));
			Select select = new Select(dropdown);
			select.selectByVisibleText(value);
		} catch (NoSuchElementException e) {
			log.error("Element Not Found - ", e);
		}
	}

	@AfterSuite
	public void tearDown() {

		/**************************
		 * Quit WebDriver driver
		 ***************************/
		driver.quit();
		log.debug("Test Execution Completed !!!");
	}
}

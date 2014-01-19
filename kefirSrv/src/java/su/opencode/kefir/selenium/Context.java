/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.selenium;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import java.util.concurrent.TimeUnit;

import static su.opencode.kefir.util.StringUtils.concat;

public class Context
{
	private Context(String siteURL, String appContext) {
		driver = getWebDriver();
		startBrowser(siteURL, appContext);
	}

	/**
	 * Init Context instance
	 *
	 * @param siteURL		http://localhost:8080
	 * @param appContext /KefirSample
	 */
	public static void initInstance(String siteURL, String appContext) {
		if (context == null)
			context = new Context(siteURL, appContext);
	}

	private WebDriver getWebDriver() {
		String browserType = System.getProperty("browser_type");
		if (browserType == null)
			browserType = BROWSER_FF;

		log.info(concat("browser_type=", browserType));
		switch (browserType)
		{
			case BROWSER_IE: return new InternetExplorerDriver();
			case BROWSER_CH: return new ChromeDriver();
			case BROWSER_FF:
			default: return new FirefoxDriver();
		}
	}

	private void startBrowser(String siteURL, String appContext) {
		driver.get(concat(siteURL, "/", appContext));
		driver.manage().timeouts().implicitlyWait(DEFAULT_IMPLICITLY_WAIT_SEC, TimeUnit.SECONDS);
	}

	public static int getSeleniumSpeed() {
		if (seleniumSpeed != -1)
			return seleniumSpeed;

		final String seleniumSpeedProperty = System.getProperty("seleniumSpeed");
		seleniumSpeed = seleniumSpeedProperty == null || seleniumSpeedProperty.isEmpty()
			? DEFAULT_SELENIUM_SPEED
			: Integer.parseInt(seleniumSpeedProperty);

		log.info(concat("seleniumSpeed=", seleniumSpeed));

		return seleniumSpeed;
	}

	public static String getResultPath() {
		return DEFAULT_SELENIUM_RESULT_PATH;
	}

	public static Context getInstance() {
		if (context != null)
			return context;

		throw new IllegalStateException("Context is not initialized");
	}

	public WebDriver getDriver() {
		if (driver != null)
			return driver;

		throw new IllegalStateException("WebBrowser is not initialized");
	}

	public static void close() {
		if (context != null)
		{
			context.driver.quit();
			context = null;
		}
	}

	private static final Logger log = Logger.getLogger(Context.class);

	private static final String DEFAULT_SELENIUM_RESULT_PATH = "d:/SeleniumResults";
	private static final int DEFAULT_SELENIUM_SPEED = 30;
	public static final int DEFAULT_IMPLICITLY_WAIT_SEC = 30;

	public static final String BROWSER_IE = "ie";
	public static final String BROWSER_FF = "ff";
	public static final String BROWSER_CH = "ch";


	private static Context context;
	private static int seleniumSpeed = -1;

	private WebDriver driver;
}
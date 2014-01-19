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
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import su.opencode.kefir.selenium.Context;
import su.opencode.kefir.util.DateUtils;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static su.opencode.kefir.util.StringUtils.concat;

public class WebDriverPage
{
	public WebDriverPage() {
		final boolean isValid = webDriverWait.until(getPageIsValidCondition());

		if (!isValid)
			throw new RuntimeException(concat(sb, "timeout. Страница не загружена!"));
	}

	public WebDriverPage(String pageTitle) {
		final boolean isValid = webDriverWait.until(getPageIsValidCondition());

		if (!isValid)
			throw new RuntimeException(concat(sb, "timeout. Страница ", pageTitle, " не загружена!"));
	}

	private ExpectedCondition<Boolean> getPageIsValidCondition() {
		return new ExpectedCondition<Boolean>()
		{
			public Boolean apply(WebDriver d) {
				return isValid();
			}
		};
	}

	public boolean isValid() {
		return true;
	}

	public boolean isTextPresentInHtml(String text) {
		log.info(concat(sb, "Is '", text, "' present in Html"));
		return findElementByXpath("//html").getText().contains(text);
	}

	public boolean isElementPresentById(String id) {
		log.info(concat(sb, "Is element present by id = ", id));
		return isElementPresentBy(By.id(id));
	}

	public void clickById(String id) {
		log.info(concat(sb, "Click by id =  ", id));
		findElementBy(By.id(id)).click();
	}

	public void setLocalComboBoxValueById(String id, String value) {
		setComboBoxValueById(id, value, false);
	}

	public void setComboBoxValueById(String id, String value) {
		setComboBoxValueById(id, value, true);
	}

	private void setComboBoxValueById(String id, String value, boolean isRemote) {
		log.info(concat(sb, "Set value in comboBox element id =  ", id, "; value = ", value));
		if (value == null)
			return;

		final String comboBoxParentNodeXpath = concat(sb, "//*[@id='x-form-el-", id, "']/div");
		final WebElement comboBoxParent = findElementByXpath(comboBoxParentNodeXpath);
		if (!comboBoxParent.getAttribute("class").contains("x-form-field-wrap"))
			throw new RuntimeException(concat(sb, "ComboBox not found by id = ", id));

		final WebElement comboBoxImgElement = findElementByXpath(concat(sb, comboBoxParentNodeXpath, "/img"));
		comboBoxImgElement.click();

		final String comboListDivXpath =
			"//*[@class='x-layer x-combo-list ' and contains(@style, 'visibility: visible;')]/div/div";

		if (isRemote)
		{
			final WebElement comboElement = findElementById(id);
			comboElement.click();
			comboElement.clear();
			comboElement.sendKeys(value);
			sleep();

			waitUntilElementEnabled(By.xpath(concat(sb, comboListDivXpath, "[@class='loading-indicator']")));
			waitUntilElementsDisabled(By.xpath(concat(sb, comboListDivXpath, "[contains(@class, 'x-combo-list-item')]")));
		}

		final By by = By.xpath(comboListDivXpath);
		final List<WebElement> comboBoxElements = findElements(by);
		for (WebElement comboBoxElement : comboBoxElements)
		{
			if (comboBoxElement.getText().equals(value))
				comboBoxElement.click();
		}
	}

	protected void sleep() {
		try
		{
			Thread.sleep(DEFAULT_SLEEP_TIME);
		}
		catch (InterruptedException e)
		{
			throw new RuntimeException(e);
		}
	}

	public void waitUntilElementEnabledId(String id) {
		waitUntilElementEnabled(By.id(id));
	}

	public void waitUntilElementEnabledXpath(String xpath) {
		waitUntilElementEnabled(By.xpath(xpath));
	}

	private void waitUntilElementEnabled(final By by) {
		webDriverWait.until(new ExpectedCondition<Boolean>()
		{
			public Boolean apply(WebDriver d) {
				final WebElement el = findElementOrNull(by);
				return el == null || !el.isEnabled();
			}
		});
	}

	private void waitUntilElementsEnabled(final By by) {
		webDriverWait.until(new ExpectedCondition<Boolean>()
		{
			public Boolean apply(WebDriver d) {
				final List<WebElement> el = findElementsOrNull(by);
				return el == null || el.size() == 0;
			}
		});
	}

	private void waitUntilElementsDisabled(final By by) {
		webDriverWait.until(new ExpectedCondition<Boolean>()
		{
			public Boolean apply(WebDriver d) {
				final List<WebElement> el = findElementsOrNull(by);
				return el != null && el.size() != 0;
			}
		});
	}

	public void setCheckboxValueById(String id, Boolean value) {
		log.info(concat(sb, "Set value in checkbox element id =  ", id, "; value = ", value));
		if (value == null)
			return;

		final WebElement e = findElementById(id);
		if (!e.getAttribute(TYPE_ELEMENT_ATTRIBUTE).equals(CHECKBOX_TYPE))
			throw new RuntimeException(concat(sb, "Checkbox not found by id = ", id));

		if ((!value && e.isSelected()) || (value && !e.isSelected()))
			e.click();
	}

	public void setDateValueById(String id, String value) {
		log.info(concat(sb, "Set value in date element id =  ", id, "; value = ", value));
		if (value == null)
			return;

		final WebElement e = findElementById(id);
		e.sendKeys("");
		e.click();
		e.sendKeys(Keys.HOME);
		e.sendKeys(value);
	}

	public void setValueById(String id, Object value) {
		log.info(concat(sb, "Set value in element id =  ", id, "; value = ", value));
		if (value == null)
			return;

		setTextFieldValue(id, value);
	}

	public void setValueAndPressEnterById(String id, Object value) {
		log.info(concat(sb, "Set value in element and press Enter id =  ", id, "; value = ", value));
		if (value == null)
			return;

		setTextFieldValue(id, value)
			.sendKeys(Keys.ENTER);
	}

	private WebElement setTextFieldValue(String id, Object value) {
		return setTextFieldValue(findElementById(id), value);
	}

	private WebElement setTextFieldValue(WebElement e, Object value) {
		e.click();
		e.clear();
		e.sendKeys(value.toString());

		return e;
	}

	private boolean isElementPresentBy(By by) {
		return isDisplayed(findElementBy(by));
	}

	private boolean isDisplayed(WebElement webElement) {
		return webElement.isDisplayed();
	}

	private WebElement findElementByXpath(String xpath) {
		return findElementBy(By.xpath(xpath));
	}

	private WebElement findElementById(String id) {
		return findElementBy(By.id(id));
	}

	private WebElement findElementBy(By by) {
		try
		{
			return findElement(by);
		}
		catch (org.openqa.selenium.NoSuchElementException e)
		{
			throw new RuntimeException(e);
		}
	}

	public void sendEnterKey(String id) {
		sendKeys(id, Keys.ENTER);
	}

	private void sendKeys(String id, Keys key) {
		findElementById(id)
			.sendKeys(key);
	}

	public void clickByXpath(String xpath) {
		final WebElement el = findElementByXpath(xpath);
		el.click();
	}

	private WebElement findElementOrNull(By by) {
		timeouts.implicitlyWait(0, TimeUnit.SECONDS);
		try
		{
			return findElement(by);
		}
		catch (org.openqa.selenium.NoSuchElementException ignored)
		{
			return null;
		}
		finally
		{
			timeouts.implicitlyWait(Context.DEFAULT_IMPLICITLY_WAIT_SEC, TimeUnit.SECONDS);
		}
	}

	private WebElement findElement(By by) {
		return driver.findElement(by);
	}

	public int getElementsCountByXpath(String xpath) {
		final List<WebElement> elementList = findElementsOrNull(By.xpath(xpath));
		if (elementList == null)
			return 0;

		final int size = elementList.size();

		return size == 1 && elementList.get(0).getText().isEmpty() ? 0 : size;
	}

	private List<WebElement> findElementsOrNull(By by) {
		timeouts.implicitlyWait(0, TimeUnit.SECONDS);
		try
		{
			return findElements(by);
		}
		catch (org.openqa.selenium.NoSuchElementException ignored)
		{
			return null;
		}
		finally
		{
			timeouts.implicitlyWait(Context.DEFAULT_IMPLICITLY_WAIT_SEC, TimeUnit.SECONDS);
		}
	}

	private List<WebElement> findElements(By by) {
		return driver.findElements(by);
	}

	public String getValueById(String id) {
		return findElementById(id).getAttribute(VALUE_ELEMENT_ATTRIBUTE);
	}

	public boolean isEqual(String elementId, Object objectValue) {
		final WebElement webElement = findElementById(elementId);
		if (objectValue instanceof Boolean)
		{
			final Boolean aBoolean = (Boolean) objectValue;
			return (webElement.isSelected() && aBoolean) || (!webElement.isSelected() && !aBoolean);
		}

		final String fieldValue = webElement.getAttribute(VALUE_ELEMENT_ATTRIBUTE);
		if (objectValue instanceof Double)
			return Double.parseDouble(fieldValue) == (Double) objectValue;

		if (objectValue instanceof Date)
			return fieldValue.equals(DateUtils.getDayMonthYear((Date) objectValue));

		return objectValue == null ? true : fieldValue.equalsIgnoreCase(objectValue.toString());
	}

	protected void setUploadValueByXpath(String xpath, String fileName) {
		findElementByXpath(xpath)
			.sendKeys(fileName);
	}

	private static final String VALUE_ELEMENT_ATTRIBUTE = "value";
	private static final String TYPE_ELEMENT_ATTRIBUTE = "type";
	private static final String CHECKBOX_TYPE = "checkbox";

	private static final Logger log = Logger.getLogger(WebDriverPage.class);
	private static final int DEFAULT_TIMEOUT_SECONDS = 60;
	private static final int DEFAULT_SLEEP_TIME = 1000;

	private WebDriver driver = Context.getInstance().getDriver();
	private WebDriverWait webDriverWait = new WebDriverWait(driver, DEFAULT_TIMEOUT_SECONDS);
	private WebDriver.Timeouts timeouts = driver.manage().timeouts();

	protected static final StringBuffer sb = new StringBuffer();
}

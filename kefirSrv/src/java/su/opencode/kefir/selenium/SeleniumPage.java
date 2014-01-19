/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.selenium;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriverCommandProcessor;
import org.openqa.selenium.android.AndroidDriver;
import su.opencode.kefir.selenium.Context;
//import su.opencode.kefir.selenium.pages.MainPage;
import su.opencode.kefir.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import static su.opencode.kefir.util.StringUtils.concat;

public abstract class SeleniumPage
{
	public abstract boolean isValid();

	protected SeleniumPage(String pageName) // конструктор для страницы без ожидания полной загрузки страницы
	{
		initPage(false, pageName);
	}

	protected SeleniumPage(boolean loadPage, String pageName) // конструктор для страницы с ожиданием полной загрузки страницы
	{
		initPage(loadPage, pageName);
	}

	private void initPage(boolean loadPage, String pageName) {
		if (loadPage)
			browser.waitForPageToLoad(TIMEOUT);

		init(pageName);
	}

	protected void init(String pageName) // первичная проверка страницы
	{
		for (int second = 0; ; second++)
		{
			if (isErrorPresent()) // не появилась ли ошибка
				break;
			if (isValid())				// метод переопределен у каждой страницы, проверяет, действительно ли загружена та страница
				break;

			if (second >= (DEFAULT_TIMEOUT_SECONDS * 2)) // проверка на таймаут
				throw new RuntimeException("timeout. Страница " + pageName + " не загружена!");

			try
			{
				Thread.sleep(THREAD_TIMEOUT_MILLISECONDS);
			}
			catch (InterruptedException e)
			{
				throw new RuntimeException(e);
			}
		}
	}

	public boolean isErrorPresent() {
		return isErrorMessageBoxPresent();
	}

	protected boolean isErrorMessageBoxPresent() {
		if (!isElementPresent("errorMessageBox"))
			return false;
		String errorTitle = getText("//div[@id='errorMessageBox']/div/div/div/div/span");			 // title ошибки
		String errorText = getText("//div[@id='errorMessageBox']/div[2]/div/div/div/div/div/div[2]/span"); // текст ошибки

		SeleniumPage.addToLog(concat("Произошла ОШИБКА! ", errorTitle, "\n"));
		SeleniumPage.addToLog(concat(errorText, "\n"));	// добавление к логу текста ошибки
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH-mm-ss");
		String dat = dateFormat.format(new Date());
		browser.captureScreenshot(StringUtils.concat(Context.getResultPath(), "\\", dat, ".png"));
		SeleniumPage.addToLog("Скриншот создан");
		click(
			"//div[@id='errorMessageBox']/div[2]/div[2]/div/div/div/div/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr[2]/td[2]/em/button");

		throw new RuntimeException(errorText);
	}

	protected void waitText(String text) {
		waitText(text, DEFAULT_TIMEOUT_SECONDS);
	}

	protected void focus(String locator) {
		browser.focus(locator);
	}

	protected void waitText(String text, int timeoutSeconds) {
		final int maxTimeoutSeconds = timeoutSeconds * (1000 / THREAD_TIMEOUT_MILLISECONDS);
		for (int second = 0; ; second++)
		{
			if (browser.isTextPresent(text))
				break;

			isErrorMessageBoxPresent();

			if (second >= maxTimeoutSeconds)
				throw new RuntimeException("timeout");

			try
			{
				Thread.sleep(THREAD_TIMEOUT_MILLISECONDS);
			}
			catch (InterruptedException e)
			{
				throw new RuntimeException(e);
			}
		}
	}

	protected void wait(String elementId, int timeoutSeconds) {
		final int maxTimeoutSeconds = timeoutSeconds * (1000 / THREAD_TIMEOUT_MILLISECONDS);
		for (int second = 0; ; second++)
		{
			if (browser.isElementPresent(elementId))
				break;

			isErrorMessageBoxPresent();

			if (second >= maxTimeoutSeconds)
				throw new RuntimeException("timeout");

			try
			{
				Thread.sleep(THREAD_TIMEOUT_MILLISECONDS);
			}
			catch (InterruptedException e)
			{
				throw new RuntimeException(e);
			}
		}
	}

	protected void waitUntilTextElementNotEquals(String elementId, String value) // ожидает пока elementId перестанет иметь значение value
	{
		waitUntilTextElementNotEquals(elementId, value, DEFAULT_TIMEOUT_SECONDS);
	}

	protected void waitUntilTextElementNotEquals(String elementId, String value, int timeoutSeconds) // ожидает пока elementId перестанет иметь значение value
	{
		final int maxTimeoutSeconds = timeoutSeconds * (1000 / THREAD_TIMEOUT_MILLISECONDS);
		for (int second = 0; ; second++)
		{
			if (!browser.getText(elementId).equals(value))
				break;
			isErrorMessageBoxPresent();
			if (second >= maxTimeoutSeconds)
				throw new RuntimeException("timeout");

			try
			{
				Thread.sleep(THREAD_TIMEOUT_MILLISECONDS);
			}
			catch (InterruptedException e)
			{
				throw new RuntimeException(e);
			}
		}
	}

	protected void waitForPageToLoad(String time) {
		browser.waitForPageToLoad(time);
	}

	protected void waitUntilTextNotDisappear(String value, int timeoutSeconds)// ожидает пока текст value не исчезнет
	{
		final int maxTimeoutSeconds = timeoutSeconds * (1000 / THREAD_TIMEOUT_MILLISECONDS);
		for (int second = 0; ; second++)
		{
			if (!isTextPresent(value))
				break;

			isErrorMessageBoxPresent();
			if (second >= maxTimeoutSeconds)
				throw new RuntimeException("timeout");
			try
			{
				Thread.sleep(THREAD_TIMEOUT_MILLISECONDS);
			}
			catch (InterruptedException e)
			{
				throw new RuntimeException(e);
			}
		}
	}

	public void waitText(String text, String errorMessage) {
		try
		{
			waitText(text);
		}
		catch (Exception e)
		{
			throw new RuntimeException(errorMessage + text);
		}
	}

	protected void wait(String elementId) {
		wait(elementId, DEFAULT_TIMEOUT_SECONDS);
	}

	public void chooseComboBox(String comboBoxId, String value) //выбирает значение value у комбобокса comboBoxId
	{
		click(comboBoxId);
		waitText(value);
		final String xPath = concat("//div[@class='x-layer x-combo-list ' and contains(@style, 'visibility: visible')]",
			"/div/div[text()='", value, "']");
		click(xPath);
		addToLog(concat(sb, "Выбор ", value));
	}

	public void clickArrow(String locator)// кликает по стрелочке у комбо-бокса locator
	{
		click("//input[contains(@id, '" + locator + "')]/../img");
	}

	public void chooseComboBoxDynamic(String comboId, String value) //выбирает значение value у комбобокса comboBoxId
	{
		clickArrow(comboId);
		waitText(value);
		final String xPath = concat("//div[@class='x-layer x-combo-list ' and contains(@style, 'visibility: visible')]",
			"/div/div[text()='", value, "']");
		click(xPath);
		addToLog(concat(sb, "Выбор ", value));
	}

	public <T extends SeleniumPage> T checkField(String value, String field, String messageWord, Class<T> aClass)// проверка, что в поле field находится значение value.
	// messageWord - текст ошибки
	{
		addToLog(concat(sb, "Проверка на наличие значения ", value, " в поле ", field));

		if (value.equalsIgnoreCase(getValue(field)))
			return getNewInstance(aClass);

		throw new RuntimeException(concat(sb, "Поле ", messageWord, " сохранено неверно!"));
	}

	protected void clickTreeCell(String treeId, String value) // используется в тесте справочника брендов
	{
		click(concat("//div[@id='", treeId, "']//div[2]/div/ul/div/li/div/a/span[text()='", value, "']"));
	}

	protected void click(String elementId) {
		click(elementId, DEFAULT_TIMEOUT_SECONDS);
	}

	protected void click(String elementId, int maxTimeoutSeconds) {
		wait(elementId, maxTimeoutSeconds);
		browser.click(elementId);
		addToLog(concat(sb, "Нажатие ", elementId));
	}

	protected void doubleClick(String elementId, int maxTimeoutSeconds) {
		wait(elementId, maxTimeoutSeconds);
		browser.doubleClick(elementId);
		addToLog(concat(sb, "Двойное нажатие ", elementId));
	}

	protected void doubleClick(String elementId) {
		wait(elementId, DEFAULT_TIMEOUT_SECONDS);
		browser.doubleClick(elementId);
		addToLog(concat(sb, "Двойное нажатие ", elementId));
	}

	protected boolean isVisible(String elementId) {
		wait(elementId, DEFAULT_TIMEOUT_SECONDS);
		return browser.isVisible(elementId);
	}

	protected void clickButtonTable(String tableId) {
		click(concat("//table[@id='", tableId, "']"));
		addToLog(concat(sb, "Нажатие ", tableId));
	}

	protected void clickButtonInput(String inputText) {
		click(concat("//input[@value='", inputText, "']"));
		addToLog(concat(sb, "Нажатие на текст ", inputText));
	}

	public void clickButtonText(String buttonText) {
		String locator = concat("//button[text()='", buttonText, "']");
		click(concat("//button[text()='", buttonText, "']"));
		addToLog(concat(sb, "Нажатие на текст ", buttonText));
	}

	protected void clickWindowText(String nameWindow) {
		click("//span[contains(text(), '" + nameWindow + "')]");
		addToLog(concat(sb, "Открытие вкладки ", nameWindow));
	}

	protected void clickButtonContainsText(String buttonText) {
		String locator = concat("//button[contains(text(), buttonText)]");
		click(locator);
		addToLog(concat(sb, "Нажатие на текст ", buttonText));
	}

//	public MainPage clickExitButton(String locator) {
//		click(locator);
//		addToLog("Выход");
//		return null;
//	}

	protected String getValue(String locator) // получить значение
	{
		wait(locator);
		addToLog(concat(sb, "Получить значение ", locator));
		return browser.getValue(locator);
	}

	protected String getText(String locator)// получить значение
	{
		wait(locator);
		addToLog(concat(sb, "Получить значение ", locator));
		return browser.getText(locator);
	}

	protected void setText(String locator, String value)// установить значение value в locator
	{
		wait(locator);
		addToLog(concat(sb, "Ввод значения ", value, " в ", locator));
		browser.type(locator, value);
	}

	protected void typeKeys(String locator, String value) // нажатие на клавишу
	{
		wait(locator);
		browser.type(locator, value);
	}

	protected void keyDown(String locator, String keySequence) // нажатие на клавишу
	{
		wait(locator);
		browser.keyDown(locator, keySequence);
	}

	protected boolean isElementPresent(String locator)//проверка на существование элемента  locator
	{
		if (!locator.equalsIgnoreCase("errorMessageBox"))
		{
			addToLog(concat(sb, "Проверка элемента ", locator, " на существование"));
		}
		return browser.isElementPresent(locator);
	}

	protected boolean isTextPresent(String s)//проверка на существование текста
	{
		addToLog(concat(sb, "Проверка текста ", s, " на существование"));
		return browser.isTextPresent(s);
	}

	protected void clickAt(String locator, String coordString) {
		wait(locator);
		browser.clickAt(locator, coordString);
	}

	protected void doubleClickAt(String locator, String coordString) {
		wait(locator);
		browser.doubleClickAt(locator, coordString);
	}

	protected void clickAt(String locator) {
		wait(locator);
		browser.clickAt(locator, "");
	}

	public <T extends SeleniumPage> T exit(Class<T> returnedPage) {
		click(EXIT_WINDOW_XPATH);
		return getNewInstance(returnedPage);
	}

	protected Number getXpathCount(String xPath) //количество элементов по xPath
	{
		wait(xPath);
		return browser.getXpathCount(xPath);
	}

	protected <T extends SeleniumPage> T getNewInstance(Class<T> aClass) {// создание нового экземпляра
		try
		{
			return aClass.newInstance();
		}
		catch (InstantiationException e)
		{
			throw new RuntimeException(e);
		}
		catch (IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
	}

	public static void addToLog(String message) {// добавить сообщение message в лог
		logText.append(message).append("\n");
	}

	public static StringBuffer getLogText() {// получить лог
		return logText;
	}

	public static void clearLogText() {// очистить лог
		logText.delete(0, logText.length());
	}

	private static StringBuffer logText = new StringBuffer();
	private static StringBuffer sb = new StringBuffer();

	private static final Logger log = Logger.getLogger(SeleniumPage.class);

	private static final int DEFAULT_TIMEOUT_SECONDS = 120;
	private static final int THREAD_TIMEOUT_MILLISECONDS = 100;

	public static final String TIMEOUT = concat(Integer.toString(DEFAULT_TIMEOUT_SECONDS), "000");

	protected static final String EXIT_WINDOW_XPATH =
		"//div[2]/div/table/tbody/tr/td[2]/table/tbody/tr/td/table/tbody/tr/td[2]/table/tbody/tr[2]/td[2]/em/button";

	private Selenium browser = new DefaultSelenium(new WebDriverCommandProcessor("", new AndroidDriver()));
}
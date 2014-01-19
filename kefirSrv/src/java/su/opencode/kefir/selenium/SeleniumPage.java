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

	protected SeleniumPage(String pageName) // ����������� ��� �������� ��� �������� ������ �������� ��������
	{
		initPage(false, pageName);
	}

	protected SeleniumPage(boolean loadPage, String pageName) // ����������� ��� �������� � ��������� ������ �������� ��������
	{
		initPage(loadPage, pageName);
	}

	private void initPage(boolean loadPage, String pageName) {
		if (loadPage)
			browser.waitForPageToLoad(TIMEOUT);

		init(pageName);
	}

	protected void init(String pageName) // ��������� �������� ��������
	{
		for (int second = 0; ; second++)
		{
			if (isErrorPresent()) // �� ��������� �� ������
				break;
			if (isValid())				// ����� ������������� � ������ ��������, ���������, ������������� �� ��������� �� ��������
				break;

			if (second >= (DEFAULT_TIMEOUT_SECONDS * 2)) // �������� �� �������
				throw new RuntimeException("timeout. �������� " + pageName + " �� ���������!");

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
		String errorTitle = getText("//div[@id='errorMessageBox']/div/div/div/div/span");			 // title ������
		String errorText = getText("//div[@id='errorMessageBox']/div[2]/div/div/div/div/div/div[2]/span"); // ����� ������

		SeleniumPage.addToLog(concat("��������� ������! ", errorTitle, "\n"));
		SeleniumPage.addToLog(concat(errorText, "\n"));	// ���������� � ���� ������ ������
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH-mm-ss");
		String dat = dateFormat.format(new Date());
		browser.captureScreenshot(StringUtils.concat(Context.getResultPath(), "\\", dat, ".png"));
		SeleniumPage.addToLog("�������� ������");
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

	protected void waitUntilTextElementNotEquals(String elementId, String value) // ������� ���� elementId ���������� ����� �������� value
	{
		waitUntilTextElementNotEquals(elementId, value, DEFAULT_TIMEOUT_SECONDS);
	}

	protected void waitUntilTextElementNotEquals(String elementId, String value, int timeoutSeconds) // ������� ���� elementId ���������� ����� �������� value
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

	protected void waitUntilTextNotDisappear(String value, int timeoutSeconds)// ������� ���� ����� value �� ��������
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

	public void chooseComboBox(String comboBoxId, String value) //�������� �������� value � ���������� comboBoxId
	{
		click(comboBoxId);
		waitText(value);
		final String xPath = concat("//div[@class='x-layer x-combo-list ' and contains(@style, 'visibility: visible')]",
			"/div/div[text()='", value, "']");
		click(xPath);
		addToLog(concat(sb, "����� ", value));
	}

	public void clickArrow(String locator)// ������� �� ��������� � �����-����� locator
	{
		click("//input[contains(@id, '" + locator + "')]/../img");
	}

	public void chooseComboBoxDynamic(String comboId, String value) //�������� �������� value � ���������� comboBoxId
	{
		clickArrow(comboId);
		waitText(value);
		final String xPath = concat("//div[@class='x-layer x-combo-list ' and contains(@style, 'visibility: visible')]",
			"/div/div[text()='", value, "']");
		click(xPath);
		addToLog(concat(sb, "����� ", value));
	}

	public <T extends SeleniumPage> T checkField(String value, String field, String messageWord, Class<T> aClass)// ��������, ��� � ���� field ��������� �������� value.
	// messageWord - ����� ������
	{
		addToLog(concat(sb, "�������� �� ������� �������� ", value, " � ���� ", field));

		if (value.equalsIgnoreCase(getValue(field)))
			return getNewInstance(aClass);

		throw new RuntimeException(concat(sb, "���� ", messageWord, " ��������� �������!"));
	}

	protected void clickTreeCell(String treeId, String value) // ������������ � ����� ����������� �������
	{
		click(concat("//div[@id='", treeId, "']//div[2]/div/ul/div/li/div/a/span[text()='", value, "']"));
	}

	protected void click(String elementId) {
		click(elementId, DEFAULT_TIMEOUT_SECONDS);
	}

	protected void click(String elementId, int maxTimeoutSeconds) {
		wait(elementId, maxTimeoutSeconds);
		browser.click(elementId);
		addToLog(concat(sb, "������� ", elementId));
	}

	protected void doubleClick(String elementId, int maxTimeoutSeconds) {
		wait(elementId, maxTimeoutSeconds);
		browser.doubleClick(elementId);
		addToLog(concat(sb, "������� ������� ", elementId));
	}

	protected void doubleClick(String elementId) {
		wait(elementId, DEFAULT_TIMEOUT_SECONDS);
		browser.doubleClick(elementId);
		addToLog(concat(sb, "������� ������� ", elementId));
	}

	protected boolean isVisible(String elementId) {
		wait(elementId, DEFAULT_TIMEOUT_SECONDS);
		return browser.isVisible(elementId);
	}

	protected void clickButtonTable(String tableId) {
		click(concat("//table[@id='", tableId, "']"));
		addToLog(concat(sb, "������� ", tableId));
	}

	protected void clickButtonInput(String inputText) {
		click(concat("//input[@value='", inputText, "']"));
		addToLog(concat(sb, "������� �� ����� ", inputText));
	}

	public void clickButtonText(String buttonText) {
		String locator = concat("//button[text()='", buttonText, "']");
		click(concat("//button[text()='", buttonText, "']"));
		addToLog(concat(sb, "������� �� ����� ", buttonText));
	}

	protected void clickWindowText(String nameWindow) {
		click("//span[contains(text(), '" + nameWindow + "')]");
		addToLog(concat(sb, "�������� ������� ", nameWindow));
	}

	protected void clickButtonContainsText(String buttonText) {
		String locator = concat("//button[contains(text(), buttonText)]");
		click(locator);
		addToLog(concat(sb, "������� �� ����� ", buttonText));
	}

//	public MainPage clickExitButton(String locator) {
//		click(locator);
//		addToLog("�����");
//		return null;
//	}

	protected String getValue(String locator) // �������� ��������
	{
		wait(locator);
		addToLog(concat(sb, "�������� �������� ", locator));
		return browser.getValue(locator);
	}

	protected String getText(String locator)// �������� ��������
	{
		wait(locator);
		addToLog(concat(sb, "�������� �������� ", locator));
		return browser.getText(locator);
	}

	protected void setText(String locator, String value)// ���������� �������� value � locator
	{
		wait(locator);
		addToLog(concat(sb, "���� �������� ", value, " � ", locator));
		browser.type(locator, value);
	}

	protected void typeKeys(String locator, String value) // ������� �� �������
	{
		wait(locator);
		browser.type(locator, value);
	}

	protected void keyDown(String locator, String keySequence) // ������� �� �������
	{
		wait(locator);
		browser.keyDown(locator, keySequence);
	}

	protected boolean isElementPresent(String locator)//�������� �� ������������� ��������  locator
	{
		if (!locator.equalsIgnoreCase("errorMessageBox"))
		{
			addToLog(concat(sb, "�������� �������� ", locator, " �� �������������"));
		}
		return browser.isElementPresent(locator);
	}

	protected boolean isTextPresent(String s)//�������� �� ������������� ������
	{
		addToLog(concat(sb, "�������� ������ ", s, " �� �������������"));
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

	protected Number getXpathCount(String xPath) //���������� ��������� �� xPath
	{
		wait(xPath);
		return browser.getXpathCount(xPath);
	}

	protected <T extends SeleniumPage> T getNewInstance(Class<T> aClass) {// �������� ������ ����������
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

	public static void addToLog(String message) {// �������� ��������� message � ���
		logText.append(message).append("\n");
	}

	public static StringBuffer getLogText() {// �������� ���
		return logText;
	}

	public static void clearLogText() {// �������� ���
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
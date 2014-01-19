/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.selenium.test;

import junit.framework.TestCase;
import org.apache.log4j.Logger;
import su.opencode.kefir.selenium.AbstractTestCase;
import su.opencode.kefir.selenium.SeleniumPage;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static su.opencode.kefir.util.ObjectUtils.getNewInstance;
import static su.opencode.kefir.util.StringUtils.concat;

public class RunAllTests extends TestCase
{
	public void executeAllTests() {
		for (Class aClass : getClasses())
			executeTest(aClass);

		if (testFailed)
			throw new RuntimeException(concat(sb, failureTestsCount, " �� ", testsCount, " ������ ����������� ���������!"));
	}

	private void executeTest(Class aClass) {
		final AbstractTestCase testCase = getNewInstance(aClass);
		for (Method method : aClass.getDeclaredMethods())
		{
			if (method.getModifiers() == Modifier.PUBLIC && method.getName().substring(0, TEST.length()).equals(TEST))
				executeTest(testCase, method);
		}
	}

	private void executeTest(AbstractTestCase testCase, Method method) {
		final String simpleName = concat(sb, testCase.getClass().getSimpleName(), ".", method.getName());

		try
		{
			testsCount++;
			testCase.setUp();

			final String message = concat(sb, "���� ", simpleName, "\n");
			SeleniumPage.clearLogText();
			SeleniumPage.addToLog(message);
			log.info(message);

			method.invoke(testCase);

			log.info(concat(sb, "[success] ���� ", simpleName, " ���������� �������!"));
		}
		catch (IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
		catch (Exception e)
		{
			testFailed = true;
			failureTestsCount++;
			log.error(concat(sb, "[failure] ���� ", simpleName, " ���������� ��������!\n", SeleniumPage.getLogText()), e);
		}
		finally
		{
			testCase.tearDown();
		}
	}

	private Class[] getClasses() {
		return new Class[] {

		};
	}

	private static final Logger log = Logger.getLogger(RunAllTests.class);
	private static final StringBuffer sb = new StringBuffer();
	private static final String TEST = "test";

	private boolean testFailed = false;
	private int failureTestsCount = 0;
	private int testsCount = 0;
}

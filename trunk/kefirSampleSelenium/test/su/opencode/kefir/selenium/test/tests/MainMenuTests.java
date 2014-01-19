/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.selenium.test.tests;

import junit.framework.TestCase;
import org.testng.annotations.*;
import su.opencode.kefir.selenium.AbstractDataProvider;
import su.opencode.kefir.selenium.AbstractTestCase;
import su.opencode.kefir.selenium.test.tests.ChooseEntityTests;
import su.opencode.kefir.selenium.test.tests.ComboBoxEntityTests;
import su.opencode.kefir.selenium.test.tests.TestEntityTests;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

import static su.opencode.kefir.util.ObjectUtils.getNewInstance;
import static su.opencode.kefir.util.ObjectUtils.invokeMethod;

public class MainMenuTests extends TestCase
{
	@Test
	public void testAllMenuButtonsTests() {
		for (Class<AbstractTestCase> test : tests)
			runTests(test);
	}

	private void runTests(Class<AbstractTestCase> testClass) {
		invokedMethods.clear();
		testDatas = null;
		final AbstractTestCase testCase = getNewInstance(testClass);
		try
		{
			invokeMethodIfAnnotationPresent(testCase, BeforeSuite.class);

			for (Method m : testClass.getDeclaredMethods())
				runTestMethod(testCase, m);
		}
		catch (NoSuchMethodException e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			invokeMethodIfAnnotationPresent(testCase, AfterSuite.class);
		}
	}

	private void runTestMethod(AbstractTestCase testCase, Method method) throws NoSuchMethodException {
		final Test testngAnnotation = method.getAnnotation(Test.class);
		if (testngAnnotation == null)
			return;

		final String[] dependsOnMethods = testngAnnotation.dependsOnMethods();
		if (dependsOnMethods.length == 0)
			runTestMethod(testCase, method, testngAnnotation);
		else
		{
			for (String dependsMethodName : dependsOnMethods)
			{
				final Method dependsMethod = testCase.getClass().getMethod(dependsMethodName, method.getParameterTypes());
				if (!invokedMethods.contains(dependsMethod))
				{
					runTestMethod(testCase, dependsMethod);
					runTestMethod(testCase, method, testngAnnotation);
				}
			}
		}
	}

	private void runTestMethod(AbstractTestCase testCase, Method method, Test testngAnnotation) {
		if (invokedMethods.contains(method))
			return;

		for (Object[] testData : getTestData(testngAnnotation))
		{
			try
			{
				invokeMethodIfAnnotationPresent(testCase, BeforeTest.class);
				invokeMethod(testCase, method, testData);
			}
			finally
			{
				invokeMethodIfAnnotationPresent(testCase, AfterTest.class);
			}
		}
		invokedMethods.add(method);
	}

	@SuppressWarnings("unchecked")
	private List<Object[]> getTestData(Test testngAnnotation) {
		if (testDatas != null)
			return testDatas;

		testDatas = new ArrayList<>();
		final Class dataProviderClass = testngAnnotation.dataProviderClass();
		for (Method m : dataProviderClass.getMethods())
		{
			if (isAnnotationPresent(m, DataProvider.class))
			{
				final AbstractDataProvider provider = getNewInstance(dataProviderClass);
				Iterator<Object[]> iterator = (Iterator<Object[]>) invokeMethod(provider, m);
				while (iterator.hasNext())
					testDatas.add(iterator.next());

				break;
			}
		}

		return testDatas;
	}

	private <T extends Annotation> void invokeMethodIfAnnotationPresent(AbstractTestCase testCase, Class<T> aClass) {
		for (Method m : testCase.getClass().getMethods())
		{
			if (isAnnotationPresent(m, aClass))
			{
				invokeMethod(testCase, m);
				break;
			}
		}
	}

	private boolean isAnnotationPresent(Method m, Class<? extends Annotation> aClass) {
		return m.getAnnotation(aClass) != null;
	}

	private static Class[] tests = new Class[] {
		ComboBoxEntityTests.class
		, ChooseEntityTests.class
		, TestEntityTests.class
	};

	private Set<Method> invokedMethods = new HashSet<>();
	private List<Object[]> testDatas = null;
}

/**
 Copyright 2013 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.util.test.objectUtils;

import org.apache.log4j.BasicConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;
import su.opencode.kefir.util.ObjectUtils;

import java.util.Date;

import static su.opencode.kefir.util.StringUtils.concat;

public class ObjectUtilsTests
{
	@BeforeClass
	public static void oneTimeSetUp() throws Exception {
		BasicConfigurator.configure();
	}

	@Test
	public void runTests() {
		testListClassFields();
		testListFieldsDifferences();
		testInstanceToString();
	}

	public void testListClassFields() {
		System.out.println("\n\n\t\t== testing ObjectUtils.listClassFields()");
		ObjectUtils.listClassFields(ListFieldsClass.class);
	}

	public void testListFieldsDifferences() {
		System.out.println("\n\n\t\t== testing ObjectUtils.listFieldsDifferences()");
		ObjectUtils.listFieldsDifferences(FieldsClass1.class, FieldsClass2.class);
	}

	public void testInstanceToString() {
		System.out.println("\n\n\t\t== testing ObjectUtils.instanceToString()");

		ListFieldsClass instance = new ListFieldsClass();
		instance.setNoModifierParent( new ListFieldsClass() );
		instance.setPrivateSampleEnum( SampleEnum.firstEnumValue );
		instance.setPrivateDate( new Date() );

		String instanceAsString = ObjectUtils.instanceToString(instance, false);
		System.out.println( concat("instance as string (no statics):\n", instanceAsString) );

		System.out.println();

		String instanceAsStringWithStatics = ObjectUtils.instanceToString(instance, true);
		System.out.println( concat("instance as string (with statics):\n", instanceAsStringWithStatics) );
	}
}
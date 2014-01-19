/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.util;

import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static su.opencode.kefir.util.StringUtils.*;

public class ObjectUtils
{
	public static Field getField(Class containingClass, String fieldName) {
		try
		{
			return containingClass.getDeclaredField(fieldName);
		}
		catch (NoSuchFieldException e)
		{
			throw new RuntimeException(e);
		}
	}
	public static Field getFieldOrNull(Class containingClass, String fieldName) {
		try
		{
			return containingClass.getDeclaredField(fieldName);
		}
		catch (NoSuchFieldException e)
		{
			return null;
		}
	}
	public static Method getMethod(Class containingClass, String methodName) {
		try
		{
			return containingClass.getDeclaredMethod(methodName);
		}
		catch (NoSuchMethodException e)
		{
			throw new RuntimeException(e);
		}
	}

	public static Class getType(Class containingClass, String fieldName) {
		try
		{
			return containingClass.getDeclaredField(fieldName).getType();
		}
		catch (NoSuchFieldException e)
		{
			try
			{
				return containingClass.getDeclaredMethod( getGetterName(fieldName) ).getReturnType();
			}
			catch (NoSuchMethodException e1)
			{
				throw new RuntimeException(e1);
			}
		}
	}

	public static String getGetterName(String fieldName) {
		return concat(GET_METHOD_NAME_PREFIX, capitalize(fieldName));
	}
	public static String getBooleanGetterName(String fieldName) {
		return concat(BOOLEAN_GET_METHOD_NAME_PREFIX, capitalize(fieldName));
	}
	public static String getSetterName(String fieldName) {
		return concat(SET_METHOD_NAME_PREFIX, capitalize(fieldName));
	}
	public static String getFieldNameByGetterName(String getterName) {
		if ( !getterName.startsWith(GET_METHOD_NAME_PREFIX) )
			throw new IllegalArgumentException( concat("Method name \"", getterName, "\" does not start with \"", GET_METHOD_NAME_PREFIX, "\"") );

		return decapitalize(getterName.substring(GET_METHOD_NAME_PREFIX.length()));
	}
	public static String getFieldNameBySetterName(String setterName) {
		if ( !setterName.startsWith(SET_METHOD_NAME_PREFIX) )
			throw new IllegalArgumentException( concat("Method name \"", setterName, "\" does not start with \"", SET_METHOD_NAME_PREFIX, "\"") );

		return decapitalize(setterName.substring(SET_METHOD_NAME_PREFIX.length()));
	}
	public static String getFieldNameByGetter(Method getter) {
		return getFieldNameByGetterName(getter.getName());
	}
	public static String getFieldNameBySetter(Method setter) {
		return getFieldNameBySetterName(setter.getName());
	}


	public static Object getNewInstance(String className) {
		return getNewInstance(getClassForName(className));
	}

	public static Class getClassForName(String className) {
		try
		{
			return Class.forName(className);
		}
		catch (ClassNotFoundException e)
		{
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T getNewInstance(Class aClass) {
		try
		{
			return (T) aClass.newInstance();
		}
		catch (IllegalAccessException | InstantiationException e)
		{
			throw new RuntimeException(e);
		}
	}

	public static String getMethodName(String prefix, String fieldName) {
		return concat(prefix, fieldName.substring(0, 1).toUpperCase(), fieldName.substring(1));
	}

	public static Object invokeMethod(Object obj, Method method) {
		try
		{
			return method.invoke(obj);
		}
		catch (IllegalAccessException | InvocationTargetException e)
		{
			throw new RuntimeException(e);
		}
	}

	public static Object invokeMethod(Object obj, Method method, Object... args) {
		try
		{
			return method.invoke(obj, args);
		}
		catch (IllegalAccessException | InvocationTargetException e)
		{
			throw new RuntimeException(e);
		}
	}

	public static Method returnGetMethod(Class aClass, String fieldName) {
		if (fieldName == null)
			return null;

		try
		{
			final Field field = aClass.getDeclaredField(fieldName);
			return returnGetMethod(aClass, field);
		}
		catch (NoSuchFieldException e)
		{
			return null;
		}
	}
	public static Method returnGetMethod(Class aClass, Field f) {
		if (f == null)
			return null;

		return returnGetterMethod(aClass, f.getName(), f.getType());
	}
	public static Method returnGetterMethod(Class containingClass, String fieldName, Class fieldType) {
		String typeName = fieldType.getName();

		String methodName;
		if (typeName.equals(boolean.class.getName())/* || typeName.equals(Boolean.class.getName())*/)
			methodName = getBooleanGetterName(fieldName);
		else
			methodName = getGetterName(fieldName);

		try
		{
			return containingClass.getMethod(methodName);
		}
		catch (Exception e)
		{
			return null;
		}
	}

	public static Method returnSetMethod(Class aClass, Field f, Class type) {
		return returnSetterMethod(aClass, f.getName(), type);
	}
	public static Method returnSetterMethod(Class containingClass, String fieldName, Class fieldType) {
		String setterName = getSetterName(fieldName);
		try
		{
			return containingClass.getMethod(setterName, fieldType);
		}
		catch (NoSuchMethodException e)
		{
			return null;
		}
	}

	public static void executeSetter(Object instance, String fieldName, Class fieldType, Object value) {
		Method setter = returnSetterMethod(instance.getClass(), fieldName, fieldType);
		if (setter == null)
			throw new IllegalArgumentException( concat("Setter method \"", getSetterName(fieldName), "(", fieldType.getName(), ")\" (for field \"", fieldName, "\" with type ", fieldType.getName(), ") is not found in class ", instance.getClass().getName()) );

		invokeMethod(instance, setter, value);
	}

	@SuppressWarnings("unchecked")
	public static <T> T executeGetter(Object instance, String fieldName, Class<T> fieldType) {
		Method getter = returnGetterMethod(instance.getClass(), fieldName, fieldType);
		if (getter == null)
			throw new IllegalArgumentException( concat("Getter method \"", getGetterName(fieldName), "()\" (for field \"", fieldName, "\" with type ", fieldType.getName(), ") is not found in class ", instance.getClass().getName()) );

		return (T) invokeMethod(instance, getter);
	}

	public static boolean haveSameTypes(Field f1, Field f2) {
		return areSameClasses(f1.getType(), f2.getType());
	}
	public static boolean haveSameModifiers(Field f1, Field f2) {
		return f1.getModifiers() == f2.getModifiers();
	}

	public static boolean areSameClasses(Class c1, Class c2) {
		return c1.getName().equals( c2.getName() ); // todo: implement this correctly
	}

	public static String toString(Field field, StringBuffer sb) {
		String modifiers = Modifier.toString(field.getModifiers());
		return concat( sb, modifiers, StringUtils.empty(modifiers) ? "" : " ", field.getType().getName(), " ", field.getName() );
	}
	public static String toString(Field field) {
		return toString(field, new StringBuffer());
	}

	public static void listClassFields(Class c) {
		StringBuffer sb = new StringBuffer();

		logger.info( concat(sb, "Listing the fields of class ", c.getName(), ":") );

		for (Field field : c.getDeclaredFields())
			logger.info( toString(field, sb) );
	}

	public static void listFieldsDifferences(Class c1, Class c2) {
		StringBuffer sb = new StringBuffer();

		List<Field> class1UniqueFields = new ArrayList<>();
		for (Field class1Field : c1.getDeclaredFields())
		{
			try
			{
				Field class2FieldWithSameName = c2.getDeclaredField( class1Field.getName() );
				if ( !haveSameTypes(class1Field, class2FieldWithSameName) || !haveSameModifiers(class1Field, class2FieldWithSameName) )
					class1UniqueFields.add(class1Field);
			}
			catch (NoSuchFieldException e)
			{
				class1UniqueFields.add(class1Field);
			}
		}

		logger.info( concat(sb, "\n\tUnique fields for class ", c1.getName(), ":") );
		for (Field class1UniqueField : class1UniqueFields)
			logger.info( toString(class1UniqueField, sb) );

		List<Field> class2UniqueFields = new ArrayList<>();
		for (Field class2Field : c2.getDeclaredFields())
		{
			try
			{
				Field class1FieldWithSameName = c1.getDeclaredField( class2Field.getName() );
				if ( !haveSameTypes(class2Field, class1FieldWithSameName) || !haveSameModifiers(class2Field, class1FieldWithSameName) )
					class2UniqueFields.add(class2Field);
			}
			catch (NoSuchFieldException e)
			{
				class2UniqueFields.add(class2Field);
			}
		}

		logger.info( concat(sb, "\n\tUnique fields for class ", c2.getName(), ":") );
		for (Field class2UniqueField : class2UniqueFields)
			logger.info( toString(class2UniqueField, sb) );
	}

	public static boolean booleanIsTrue(Boolean value) {
		return (value != null) && value;
	}

	public static boolean empty(Collection collection) {
		return (collection == null) || ( collection.isEmpty() );
	}
	public static boolean notEmpty(Collection collection) {
		return (collection != null) && ( !collection.isEmpty() );
	}

	public static String instanceToString(Object instance, boolean listStatics, DateFormat dateFormat) {
		try
		{
			StringBuilder sb = new StringBuilder();

			sb.append("[");

			for (Field field : instance.getClass().getDeclaredFields())
			{
				field.setAccessible(true); // otherwise getting private fields fails

				boolean appendField = true;
				if ( !listStatics )
				{
					if ( Modifier.isStatic(field.getModifiers()) )
						appendField = false;
				}

				if (appendField)
				{
					Object fieldValue = field.get(instance);
					if (fieldValue instanceof Date)
						fieldValue = dateFormat.format( (Date) fieldValue );

					sb.append("\n\t").append(field.getName()).append(": ").append(fieldValue);
				}
			}

			sb.append("\n]");
			return sb.toString();
		}
		catch (IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
	}
	public static String instanceToString(Object instance, boolean listStatics) {
		return instanceToString(instance, listStatics, DateUtils.getDayMonthYearFormat()); // default do not list static fields
	}
	public static String instanceToString(Object instance, DateFormat dateFormat) {
		return instanceToString(instance, false, dateFormat); // default do not list static fields
	}
	public static String instanceToString(Object instance) {
		return instanceToString(instance, false, DateUtils.getDayMonthYearFormat()); // default do not list static fields
	}

	private static final Logger logger = Logger.getLogger(ObjectUtils.class);

	public static final String GET_METHOD_NAME_PREFIX = "get";
	public static final String BOOLEAN_GET_METHOD_NAME_PREFIX = "is";
	public static final String SET_METHOD_NAME_PREFIX = "set";
}
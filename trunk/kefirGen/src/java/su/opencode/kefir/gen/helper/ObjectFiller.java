/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 20.03.2012 17:38:07$
*/
package su.opencode.kefir.gen.helper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Properties;

import static su.opencode.kefir.util.FileUtils.close;

/**
 * Заполняет поля объекта из property файла, ключи которого имеют названия, равные названиям полей класса.
 */
public class ObjectFiller
{
	public static <T> T createObject(InputStream in, Class<T> objectClass) {
		try
		{
			Properties properties = new Properties();
			properties.load(in);

			T object = objectClass.newInstance();
			for (Field field : objectClass.getDeclaredFields())
			{
				String propertyValue = properties.getProperty(field.getName());

				field.setAccessible(true); // access to private field

				if (field.getType().equals(Class.class) && propertyValue != null)
					field.set(object, Class.forName(propertyValue)); // установить класс по имени
				else
					field.set(object, propertyValue);
			}

			return object;
		}
		catch (IOException | InstantiationException | IllegalAccessException | SecurityException | IllegalArgumentException | ClassNotFoundException e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			close(in);
		}
	}

	public static <T> T createObject(String fileName, Class<T> objectClass) {
		try
		{
			InputStream in = new FileInputStream(fileName);
			return createObject(in, objectClass);
		}
		catch (FileNotFoundException e)
		{
			throw new RuntimeException(e);
		}
	}
}
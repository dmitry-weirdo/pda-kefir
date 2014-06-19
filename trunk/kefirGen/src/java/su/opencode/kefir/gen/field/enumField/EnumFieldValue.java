/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.field.enumField;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Помечает значение энума, которое может быть перенесено в js-store и js-renderer.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumFieldValue
{
	/**
	 * @return значение ключа, которое будет использоваться для константы в js-хэше, представляющем энум.
	 * По умолчанию — название поля энума-класса большими буквами.
	 */
	String hashName() default "";

	/**
	 * @return значение, которое будет использоваться для константы в js-хэше, представляющем энум.
	 * По умолчанию — строка, равная название поля энум-класса.
	 */
	String hashValue() default "";

	/**
	 * @return строка, которая будет отображаться как значение в store (то есть будет отображаться в localComboBox)
	 */
	String storeValue();

	/**
	 * @return строка, которая будет выдаваться для значения в рендерере
	 * По умолчанию — равно {@linkplain #storeValue значению, отображаемому в store}.
	 */
	String rendererValue() default "";
}
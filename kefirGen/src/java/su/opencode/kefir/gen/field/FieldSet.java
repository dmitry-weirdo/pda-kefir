/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.field;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = { ElementType.TYPE })
@Retention(value = RetentionPolicy.RUNTIME)
public @interface FieldSet
{
	String id();
	String title();

	/**
	 * @return тип лэйаута филдсета, например, "table".
	 * <br/>
	 * По умолчанию — тип не указывается, поля будут находится в вертикальном порядке.
	 */
	String layout() default "";

	/**
	 * @return конфиг разметки (layout) филдсета.
	 */
	String layoutConfig() default "";


	public static final String TABLE_LAYOUT = "table";
	public static final String TWO_COLUMNS_LAYOUT_CONFIG = "{ columns: 2 }";
}
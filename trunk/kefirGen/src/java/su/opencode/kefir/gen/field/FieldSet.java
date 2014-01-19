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
	 * @return ��� ������� ��������, ��������, "table".
	 * <br/>
	 * �� ��������� � ��� �� �����������, ���� ����� ��������� � ������������ �������.
	 */
	String layout() default "";

	/**
	 * @return ������ �������� (layout) ��������.
	 */
	String layoutConfig() default "";


	public static final String TABLE_LAYOUT = "table";
	public static final String TWO_COLUMNS_LAYOUT_CONFIG = "{ columns: 2 }";
}
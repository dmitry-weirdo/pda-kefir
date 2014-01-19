/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ rybakin
 $Revision$
 $Date: 29.11.2010 13:14:38$
*/
package su.opencode.kefir.srv.json;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = { ElementType.TYPE })
@Retention(value = RetentionPolicy.RUNTIME)
public @interface ViewConfig
{
	/**
	 * @return �������� js viewConfig. ��������������, ��� viewConfig ��������� � ����� viewConfigs.js.
	 */
	String viewConfig() default "";

	/**
	 * @return <code>true</code>, ���� ������� ���� � ������ ���������
	 * <br/>
	 * <code>false</code>, ���� ������� ���.
	 */
	boolean hasLegend() default true;

	/**
	 * @return ����� ������ ������� � ������ ����������� ������ ������ ���������.
	 */
	String legendLabel() default "";

	/**
	 * @return ��� ���� VO, �������� �������� ����� �����������
	 * � {@linkplain #legendLabel() ������ ������ �������} ��� ������ ������ ������ ���������.
	 */
	String legendLabelVOField() default "";

	/**
	 * @return ���� VO ��������, ��� ������� ��� ��������� �� �������� true ��� false
	 * ���������� ������ � ������ ��������� � ������������ ��������������� ���� �������, ���� ��� ����.
	 */
	LegendField[] legendFields() default {};

	public static final String VIEW_CONFIG_PROPERTY_NAME = "viewConfig";
	public static final String DEFAULT_ROW_CLASS = "x-grid3-row";
	public static final String SELECTED_ROW_CLASS = "x-grid3-row-selected";
}
/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.field.enumField;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static su.opencode.kefir.gen.fileWriter.js.JsFileWriter.RENDERER_NAMESPACE;

/**
 * �������� ����� �����, ������� ����� ���� ����������� � ���� js-����,
 * ������� ����� �������������� � store (��� ������ �������� ���������� �����������)
 * � renderer (��� ����������� � �������) ����� ����� ����.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumField
{
	/**
	 * @return ��� sql-�������, ����������� �������� ���� ���� ��������.
	 * �� ��������� � (��� ���� � ������ ��������, ��� ������ ����� ����� �������� ���������).
	 */
	String sqlColumnName() default "";

	/**
	 * @return ���������, � ������� ����� ���������� ���, �������������� ����.
	 * �� ��������� � ����� ������, � ������� ��������� ����� �����.
	 */
	String hashNamespace() default "";

	/**
	 * @return �������� ����, ��������������� ���� � js.
	 * �� ��������� � ����� ����� ������ �����.
	 */
	String hashName() default "";


	/**
	 * @return ���������, � ������� ����� ���������� ��������� store, �������������� ����.
	 * �� ��������� � ����� ������, � ������� ��������� ����� �����.
	 */
	String storeNamespace() default "";

	/**
	 * @return �������� store, ��������������� ����.
	 * �� ��������� � (��� ������ ����� + "Store").
	 */
	String storeName() default "";

	/**
	 * @return ��� ���� �� ��������� � store.
	 */
	String storeValueFieldName() default DEFAULT_VALUE_FIELD_NAME;

	/**
	 * @return ��� ���� � ������������ ��������� � store.
	 */
	String storeDisplayFieldName() default DEFAULT_DISPLAY_FIELD_NAME;


	/**
	 * @return ���������, � ������� ����� ���������� ��������, �������������� ����.
	 */
	String rendererNamespace() default DEFAULT_RENDERER_NAMESPACE;

	/**
	 * @return �������� ���������, ��������������� ����.
	 * �� ��������� � (��� ������ ����� + "Renderer").
	 */
	String rendererName() default "";

	/**
	 * @return �����, � ������� ����� ���������� {@linkplain su.opencode.kefir.srv.renderer.CellRenderer CellRenderer} ��� �����.
	 * �� ��������� � �����, � ������� ��������� ����� �����.
	 */
	String rendererClassPackage() default "";

	/**
	 * @return simpleName ������ {@linkplain su.opencode.kefir.srv.renderer.CellRenderer CellRendrerer} ��� �����.
	 * �� ��������� � (��� ����� + "CellRenderer")
	 */
	String rendererClassName() default "";

	/**
	 * @return �������� ��������� ��� ������ Renderers ����������, ��������� ������� ����� ������ �������� js-��������� �����.
	 * �� ��������� � (�������� ������ ����� {@linkplain su.opencode.kefir.gen.ExtEntityUtils#getConstantName(String) ��� ���������} + {@linkplain #RENDERER_CONSTANT_NAME_POSTFIX "_RENDERER"}).
	 */
	String rendererConstantName() default "";

	public static final String DEFAULT_VALUE_FIELD_NAME = "id";
	public static final String DEFAULT_DISPLAY_FIELD_NAME = "name";
	public static final String DEFAULT_RENDERER_NAMESPACE = RENDERER_NAMESPACE;
	public static final String RENDERER_CONSTANT_NAME_POSTFIX = "_RENDERER";
}
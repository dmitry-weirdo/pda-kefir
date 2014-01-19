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
 * ѕомечает класс энума, который может быть представлен в виде js-хэша,
 * который будет использоватьс€ в store (дл€ набора значений локального комбобокаса)
 * и renderer (дл€ отображени€ в таблице) полей этого типа.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumField
{
	/**
	 * @return им€ sql-столбца, содержащего значение энум пол€ сущности.
	 * ѕо умолчанию Ч (им€ пол€ в классе сущности, где каждое новое слово отделено подчерком).
	 */
	String sqlColumnName() default "";

	/**
	 * @return неймспейс, в котором будет находитьс€ хэш, представл€ющий энум.
	 * ѕо умолчанию Ч равен пакету, в котором находитс€ класс энума.
	 */
	String hashNamespace() default "";

	/**
	 * @return название хэша, представл€ющего энум в js.
	 * ѕо умолчанию Ч равен имени класса энума.
	 */
	String hashName() default "";


	/**
	 * @return неймспейс, в котором будет находитьс€ локальный store, представл€ющий энум.
	 * ѕо умолчанию Ч равен пакету, в котором находитс€ класс энума.
	 */
	String storeNamespace() default "";

	/**
	 * @return название store, представл€ющего энум.
	 * ѕо умолчанию Ч (им€ класса энума + "Store").
	 */
	String storeName() default "";

	/**
	 * @return им€ пол€ со значением в store.
	 */
	String storeValueFieldName() default DEFAULT_VALUE_FIELD_NAME;

	/**
	 * @return им€ пол€ с отображаемым значением в store.
	 */
	String storeDisplayFieldName() default DEFAULT_DISPLAY_FIELD_NAME;


	/**
	 * @return неймспейс, в котором будет находитьс€ рендерер, обрабатывающий энум.
	 */
	String rendererNamespace() default DEFAULT_RENDERER_NAMESPACE;

	/**
	 * @return название рендерера, обрабатывающего энум.
	 * ѕо умолчанию Ч (им€ класса энума + "Renderer").
	 */
	String rendererName() default "";

	/**
	 * @return пакет, в котором будет находитьс€ {@linkplain su.opencode.kefir.srv.renderer.CellRenderer CellRenderer} дл€ энума.
	 * ѕо умолчанию Ч пакет, в котором находитс€ класс энума.
	 */
	String rendererClassPackage() default "";

	/**
	 * @return simpleName класса {@linkplain su.opencode.kefir.srv.renderer.CellRenderer CellRendrerer} дл€ энума.
	 * ѕо умолчанию Ч (им€ энума + "CellRenderer")
	 */
	String rendererClassName() default "";

	/**
	 * @return название константы дл€ класса Renderers приложени€, значением которой будет полное название js-рендерера энума.
	 * ѕо умолчанию Ч (название класса энума {@linkplain su.opencode.kefir.gen.ExtEntityUtils#getConstantName(String) как константа} + {@linkplain #RENDERER_CONSTANT_NAME_POSTFIX "_RENDERER"}).
	 */
	String rendererConstantName() default "";

	public static final String DEFAULT_VALUE_FIELD_NAME = "id";
	public static final String DEFAULT_DISPLAY_FIELD_NAME = "name";
	public static final String DEFAULT_RENDERER_NAMESPACE = RENDERER_NAMESPACE;
	public static final String RENDERER_CONSTANT_NAME_POSTFIX = "_RENDERER";
}
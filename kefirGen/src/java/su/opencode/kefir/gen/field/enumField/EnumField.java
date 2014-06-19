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
 * Помечает класс энума, который может быть представлен в виде js-хэша,
 * который будет использоваться в store (для набора значений локального комбобокаса)
 * и renderer (для отображения в таблице) полей этого типа.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumField
{
	/**
	 * @return имя sql-столбца, содержащего значение энум поля сущности.
	 * По умолчанию — (имя поля в классе сущности, где каждое новое слово отделено подчерком).
	 */
	String sqlColumnName() default "";

	/**
	 * @return неймспейс, в котором будет находиться хэш, представляющий энум.
	 * По умолчанию — равен пакету, в котором находится класс энума.
	 */
	String hashNamespace() default "";

	/**
	 * @return название хэша, представляющего энум в js.
	 * По умолчанию — равен имени класса энума.
	 */
	String hashName() default "";


	/**
	 * @return неймспейс, в котором будет находиться локальный store, представляющий энум.
	 * По умолчанию — равен пакету, в котором находится класс энума.
	 */
	String storeNamespace() default "";

	/**
	 * @return название store, представляющего энум.
	 * По умолчанию — (имя класса энума + "Store").
	 */
	String storeName() default "";

	/**
	 * @return имя поля со значением в store.
	 */
	String storeValueFieldName() default DEFAULT_VALUE_FIELD_NAME;

	/**
	 * @return имя поля с отображаемым значением в store.
	 */
	String storeDisplayFieldName() default DEFAULT_DISPLAY_FIELD_NAME;


	/**
	 * @return неймспейс, в котором будет находиться рендерер, обрабатывающий энум.
	 */
	String rendererNamespace() default DEFAULT_RENDERER_NAMESPACE;

	/**
	 * @return название рендерера, обрабатывающего энум.
	 * По умолчанию — (имя класса энума + "Renderer").
	 */
	String rendererName() default "";

	/**
	 * @return пакет, в котором будет находиться {@linkplain su.opencode.kefir.srv.renderer.CellRenderer CellRenderer} для энума.
	 * По умолчанию — пакет, в котором находится класс энума.
	 */
	String rendererClassPackage() default "";

	/**
	 * @return simpleName класса {@linkplain su.opencode.kefir.srv.renderer.CellRenderer CellRendrerer} для энума.
	 * По умолчанию — (имя энума + "CellRenderer")
	 */
	String rendererClassName() default "";

	/**
	 * @return название константы для класса Renderers приложения, значением которой будет полное название js-рендерера энума.
	 * По умолчанию — (название класса энума {@linkplain su.opencode.kefir.gen.ExtEntityUtils#getConstantName(String) как константа} + {@linkplain #RENDERER_CONSTANT_NAME_POSTFIX "_RENDERER"}).
	 */
	String rendererConstantName() default "";

	public static final String DEFAULT_VALUE_FIELD_NAME = "id";
	public static final String DEFAULT_DISPLAY_FIELD_NAME = "name";
	public static final String DEFAULT_RENDERER_NAMESPACE = RENDERER_NAMESPACE;
	public static final String RENDERER_CONSTANT_NAME_POSTFIX = "_RENDERER";
}
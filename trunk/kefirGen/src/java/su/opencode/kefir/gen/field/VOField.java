/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.field;

import su.opencode.kefir.srv.json.ColumnModel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Помечает поле связанной сущности ({@linkplain su.opencode.kefir.gen.test.ComboBoxEntity ComboBoxEntity} или {@linkplain su.opencode.kefir.gen.test.ChooseEntity ChooseEntity}),
 * Поле может быть полем не самой связанной сущности, а полем ее поля, являющегося сущностью, итд.
 * В этом случаи уровни иерархии разделяются точкой.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface VOField
{
	/**
	 * @return название поля связанной сущности.
	 */
	String name();

	/**
	 * @return название поля, создаваемого в VO классе.
	 * <br/>
	 * По умолчанию —
	 * (
	 * 	&lt;имя поля, на котором стоит аннотация @ChooseField или @ComboBoxField&gt;
	 * +
	 * {@linkplain #name название поля связанной сущности}, в котором поля после точки, кроме первого, написаны с большой буквы, а точки удалены
	 * )
	 */
	String voFieldName() default "";

	/**
	 * @return тип поля, которое будет создано в VO классе.
	 * <br/>
	 * По умолчанию — равно названию типа поля связанной сущности.
	 */
	String voFieldClassName() default "";

	/**
	 * @return возможна ли сортировка по полям связанной сущности
	 * <br/>
	 * По умолчанию - равно возможности сортировки @ColumnModel аннотации, стоящей на поле связанной сущности.
	 */
	boolean sortable() default ColumnModel.DEFAULT_SORTABLE;

	/**
	 * @return {@linkplain su.opencode.kefir.srv.json.ColumnModel#header() заголовок поля таблицы}, которое будет создано в VO классе.
	 * <br/>
	 * По умолчанию - равно заголовоку @ColumnModel аннотации, стоящей на поле связанной сущности,
	 * а если аннотации нет - то лейблу поля формы связанной сущности.
	 */
	String header() default "";

	/**
	 * @return рендерер поля, которое будет создано в VO классе.
	 * <br/>
	 * По умолчанию — равно рендереру поля связанной сущности.
	 */
	String renderer() default "";

	/**
	 * @return значение параметра сортировки, которое будет использоваться для поля.
	 * <br/>
	 * По умолчанию — равно {@linkplain #name названию поля связанной сущности}.
	 */
	String sortParam() default "";

	/**
	 * @return код группы столбцов таблицы, в которой нахоится столбец поля связанной сущности.
	 * Этот код должен быть одним из {@linkplain su.opencode.kefir.srv.json.ColumnGroup#id id} элементов {@linkplain su.opencode.kefir.srv.json.ColumnGroup @ColumngGroup},
	 * определенных в аннотации {@linkplain su.opencode.kefir.srv.json.ColumnGroups @ColumnGroups} к классу сущности.
	 * <br/>
	 * По умолчанию — столбец не находится в группе столбцов.
	 */
	String groupId() default "";

	/**
	 * @return текст всплывающей подсказки к сгененированному столбцу таблицы.
	 * <br/>
	 * По умолчанию — текст указан не будет, и всплывающая подсказка будет по умолчанию равна {@linkplain ColumnModel#header заголовку столбца}.
	 */
	String tooltip() default "";

	/**
	 * @return {@linkplain su.opencode.kefir.gen.field.FieldSet#id() код} {@linkplain FieldSet филдсета}, в котором находится поле.
	 * <br/>
	 * Код должен быть одним из кодов филдсетов, определенных в аннотации {@linkplain FieldSets FieldSets} к классу сущности.
	 */
	String fieldSetId() default "";
}
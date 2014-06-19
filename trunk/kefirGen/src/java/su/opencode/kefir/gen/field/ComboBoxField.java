/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.field;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Аннотация поля, указыывающая, что поле является комбобоксом, выбирающим значение из списка, получаемого по урл.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ComboBoxField
{
	/**
	 * @return <code>true</code> если для поля нужно добавить many-to-one маппинг в маппинг сущности в orm.xml,<br/>
	 * <code>false</code> — если маппинг добавлять не надо
	 */
	boolean addManyToOneMapping() default true;

	/**
	 * @return имя sql-столбца, с помощью которого связанная сущность соединяется с таблицей сущности.
	 * По умолчанию — (имя поля в классе сущности, где каждое новое слово отделено подчерком + "_id").
	 */
	String joinColumnName() default "";

	/**
	 * @return код поля.
	 *         По умолчанию — ( {@linkplain su.opencode.kefir.gen.ExtEntity#jsFieldPrefix() префикс js-поля формы сущности} + "-" + название поля )
	 */
	String id() default "";

	/**
	 * @return имя поля.
	 *         По умолчанию — равно имени поля, на котором стоит аннотация
	 */
	String name() default "";

	/**
	 * @return имя скрытого поле, в котором будет сабмититься значение комбобокса.
	 *         По умолчанию — равно {@linkplain #name() имени поля}.
	 */
	String hiddenName() default "";

	/**
	 * @return метка поля
	 */
	String label();

	/**
	 * @return максимальная длина поля
	 */
	int maxLength();

	/**
	 * @return ширина поля в пикселах
	 */
	int width();

	/**
	 * @return ширина списка в пикселах.
	 *         По умолчанию — равна ширине поля
	 */
	int listWidth() default 0;

	/**
	 * @return <code>true</code> — если поле можно не обязательно для заполнения, <br/>
	 *         <code>false</code> — если поле обязательно для заполнения
	 */
	boolean allowBlank() default true;

	/**
	 * @return <code>true</code> — если поле обязательно выбирается из списка, <br/>
	 *         <code>false</code> — если возможно произвольное заполнение поля.
	 */
	boolean forceSelection() default true;

	/**
	 * @return <code>true</code> — если в поле можно вводить значение, <br/>
	 *         <code>false</code> — если в поле нельзя вводить значение.
	 */
	boolean editable() default true;

	/**
	 * @return <code>true</code> — если поле транслирует свое значение в uppercase, <br/>
	 *         <code>false</code> — в противном случае
	 */
	boolean uppercase() default true;

	/**
	 * @return рендерер, выполняемый при заполнении поля.
	 *         По умолчанию — рендерер отсутствует.
	 */
	String renderer() default "";

	/**
	 * @return vtype поля
	 *         По умолчанию — рендерер отсутствует
	 */
	String vtype() default "";

	/**
	 * @return урл сервлета, который выдает список значений в json для комбобокса.
	 */
	String url();

	/**
	 * @return параметр запроса, который передается в сервлет при вводе значения в поле.
	 */
	String queryParam() default DEFAULT_QUERY_PARAM;

	/**
	 * @return поля хранилища комбобокса.
	 * По умолчанию — id типа int и name типа string
	 * // todo: возможно, генерировать поле id всегда автоматически, здесь указывать только другие поля
	 */
	ComboBoxStoreField[] fields() default {};

	/**
	 * @return поля связанной комбо-сущности, которые будут добавлены в сгенерированный VO класс и {@linkplain su.opencode.kefir.srv.json.ColumnModel помечены} как столбцы таблицы.
	 */
	VOField[] voFields() default {};

	/**
	 * @return название id-поля объекта.
	 * Согласно этому полю выставляется значение в комбобокс при заполнении формы для просмотра\изменения\удаления.
	 */
	String valueField() default DEFAULT_VALUE_FIELD_NAME;

	/**
	 * @return поле объекта, полученного из сервлета которое отображается в комбобоксе.
	 */
	String displayField() default DEFAULT_DISPLAY_FIELD_NAME;

	/**
	 * @return поле сортировки, которое передается в сервлет
	 */
	String sortBy() default "name";

	/**
	 * @return порядок сортировки, которое передается в сервлет
	 */
	String sortDir() default "asc";

	/**
	 * @return <code>true</code> — если при наборе значения с клавиатуры в комбобокс выполняется предупреждающее автозаполнение, <br/>
	 *         <code>false</code> — в противном случае
	 */
	boolean typeAhead() default false;

	/**
	 * @return {@linkplain su.opencode.kefir.gen.field.FieldSet#id() код} {@linkplain FieldSet филдсета}, в котором находится поле.
	 * <br/>
	 * Код должен быть одним из кодов филдсетов, определенных в аннотации {@linkplain FieldSets FieldSets} к классу сущности.
	 */
	String fieldSetId() default "";

	public static final String DEFAULT_QUERY_PARAM = "name";
	public static final String DEFAULT_VALUE_FIELD_NAME = "id";
	public static final String DEFAULT_DISPLAY_FIELD_NAME = "name";
}
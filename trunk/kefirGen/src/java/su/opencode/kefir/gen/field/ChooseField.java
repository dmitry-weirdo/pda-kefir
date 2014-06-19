/**
 * Copyright 2012 LLC "Open Code"
 * http://www.o-code.ru
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */
package su.opencode.kefir.gen.field;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Аннотация поля, указыывающая, что поле является полем выбора из списка других сущностей через таблицу выбора.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ChooseField
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
	 * @return <code>true</code> — если поле можно не обязательно для заполнения, <br/>
	 *         <code>false</code> — если поле обязательно для заполнения
	 */
	boolean allowBlank() default true;

	/**
	 * @return код поля CRUD-формы, содержащей id выбранной сущности.
	 *         По умолчанию — ( {@linkplain su.opencode.kefir.gen.ExtEntity#jsFieldPrefix() префикс js-поля формы сущности} + "-" + название поля )
	 */
	String idFieldId() default "";

	/**
	 * @return имя поля CRUD-формы, содержащей id выбранной сущности.
	 * По умолчанию — равно имени поля.
	 */
	String name() default "";

	/**
	 * @return имя поля выбираемой сущности, содержащего id выбираемой сущности
	 */
	String idFieldName() default "id";

	/**
	 * @return дополнительные параметры, передаваемые в обработчик кнопки выбора.
	 */
	ChooseFieldInitParam[] initParams() default {};

	/**
	 * @return названия видимые поля, которые выбираются из сущности, <b>не включая id</b>.
	 * Поля будут записываться в readonly текстовые поля
	 * // todo: возможно, нужно будет сделать специальную аннотацию с обязательным именем
	 */
	ChooseFieldTextField[] fields();

	/**
	 * @return поля связанной сущности, которые будут добавлены в сгенерированный VO класс и {@linkplain su.opencode.kefir.srv.json.ColumnModel помечены} как столбцы таблицы.
	 */
	VOField[] voFields() default {};

	/**
	 * @return название группы полей, которое будет содержать выбранные поля выбранной сущности в основной форме.
	 */
	String fieldSetName();

	/**
 	 * @return id кнопки выбора
	 * По умолчанию — ( {@linkplain su.opencode.kefir.gen.ExtEntity#jsFieldPrefix() префикс js-поля формы сущности} + "-" + название поля + "-choose" )
	 */
	String chooseButtonId() default "";

	/**
	 * @return текст на кнопке выбора сущности в форме с полем
	 */
	String chooseButtonText() default "Выбрать";

	/**
	 * @return стиль кнопки выбора
	 */
	String chooseButtonStyle() default "{ marginLeft: 10, marginBottom: 5 }";

	/**
	 * @return полное название (включая неймспейс) функции, которая будет вызываться при нажатии кнопки выбора.
	 * По умолчанию — определяется согласно классу поля: если класс имеет аннотацию ExtEntity, то использутся данные из нее, иначе — данные по умолчанию.
	 */
	String chooseFunctionName() default "";

	/**
	 * @return название параметра функции выбора, в который будет передаваться обработчик успешного выбора (successHandler).
	 * По умолчанию — определяется согласно классу поля: если класс имеет аннотацию ExtEntity, то использутся данные из нее, иначе — данные по умолчанию.
	 */
	String chooseFunctionSuccessHandlerParamName() default "";


	/**
 	 * @return id кнопки выбора
	 * По умолчанию — ( {@linkplain su.opencode.kefir.gen.ExtEntity#jsFieldPrefix() префикс js-поля формы сущности} + "-" + название поля + "-show" )
	 */
	String showButtonId() default "";

	/**
	 * @return текст на кнопке просмотра сущности в форме с полем
	 */
	String showButtonText() default "Просмотреть";

	/**
	 * @return стиль кнопки просмотра сущности
	 */
	String showButtonStyle() default "{ marginLeft: 10, marginBottom: 5 }";

	/**
	 * @return полное название (включая неймспейс) функции, которая будет вызываться при нажатии кнопки просмотра выбранной сущности.
	 * По умолчанию — определяется согласно классу поля: если класс имеет аннотацию ExtEntity, то использутся данные из нее, иначе — данные по умолчанию.
	 */
	String showFunctionName() default "";

	/**
	 * @return название параметра функции просмотра сущноси, в который будет передаваться id сущности.
	 * По умолчанию — определяется согласно классу поля: если класс имеет аннотацию ExtEntity, то использутся данные из нее, иначе — данные по умолчанию.
	 */
	String showFunctionIdParamName() default "";

	/**
	 * @return {@linkplain su.opencode.kefir.gen.field.FieldSet#id() код} {@linkplain FieldSet филдсета}, в котором находится поле.
	 * <br/>
	 * Код должен быть одним из кодов филдсетов, определенных в аннотации {@linkplain FieldSets FieldSets} к классу сущности.
	 */
	String fieldSetId() default "";
}
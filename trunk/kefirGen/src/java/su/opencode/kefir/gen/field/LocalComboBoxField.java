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
 * јннотаци€ пол€, указыывающа€, что поле €вл€етс€ комбобоксом, выбирающим значение из локального js-хранилища.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LocalComboBoxField
{
	/**
	 * @return код пол€.
	 *         ѕо умолчанию Ч ( {@linkplain su.opencode.kefir.gen.ExtEntity#jsFieldPrefix() префикс js-пол€ формы сущности} + "-" + название пол€ )
	 */
	String id() default "";

	/**
	 * @return им€ пол€.
	 *         ѕо умолчанию Ч равно имени пол€, на котором стоит аннотаци€
	 */
	String name() default "";

	/**
	 * @return им€ скрытого поле, в котором будет сабмититьс€ значение комбобокса.
	 *         ѕо умолчанию Ч равно {@linkplain #name() имени пол€}.
	 * @see su.opencode.kefir.gen.field.ExtEntityFieldsUtils#getLocalComboBoxHiddenName(LocalComboBoxField, java.lang.reflect.Field)
	 */
	String hiddenName() default "";

	/**
	 * @return метка пол€
	 */
	String label();

	/**
	 * @return максимальна€ длина пол€
	 */
	int maxLength();

	/**
	 * @return ширина пол€ в пикселах
	 */
	int width();

	/**
	 * @return ширина списка в пикселах.
	 *         ѕо умолчанию Ч равна ширине пол€
	 * @see su.opencode.kefir.gen.field.ExtEntityFieldsUtils#getLocalComboBoxListWidth(LocalComboBoxField, java.lang.reflect.Field)
	 */
	int listWidth() default DEFAULT_LIST_WIDTH;

	/**
	 * @return <code>true</code> Ч если поле можно не об€зательно дл€ заполнени€, <br/>
	 *         <code>false</code> Ч если поле об€зательно дл€ заполнени€
	 */
	boolean allowBlank() default true;

	/**
	 * @return <code>true</code> Ч если поле об€зательно выбираетс€ из списка, <br/>
	 *         <code>false</code> Ч если возможно произвольное заполнение пол€.
	 */
	boolean forceSelection() default true;

	/**
	 * @return <code>true</code> Ч если в поле можно вводить значение, <br/>
	 *         <code>false</code> Ч если в поле нельз€ вводить значение.
	 */
	boolean editable() default false;

	/**
	 * @return <code>true</code> Ч если поле транслирует свое значение в uppercase, <br/>
	 *         <code>false</code> Ч в противном случае
	 */
	boolean uppercase() default false;

	/**
	 * @return рендерер, выполн€емый при заполнении пол€.
	 *         ѕо умолчанию Ч рендерер отсутствует.
	 */
	String renderer() default "";

	/**
	 * @return vtype пол€
	 *         ѕо умолчанию Ч рендерер отсутствует
	 */
	String vtype() default "";

	/**
	 * @return название js-переменной, содержащий локальное хранилище дл€ комбобокса.
	 *         ѕо умолчанию Ч (полное им€ класса пол€ (предполагаетс€ enum) + "Store").
	 * @see su.opencode.kefir.gen.field.ExtEntityFieldsUtils#getLocalComboBoxStore(LocalComboBoxField, java.lang.reflect.Field)
	 */
	String store() default "";

	/**
	 * @return название id-пол€ объекта.
	 * —огласно этому полю выставл€етс€ значение в комбобокс при заполнении формы дл€ просмотра\изменени€\удалени€.
	 */
	String valueField() default DEFAULT_VALUE_FIELD_NAME;

	/**
	 * @return поле объекта, полученного из сервлета которое отображаетс€ в комбобоксе.
	 */
	String displayField() default DEFAULT_DISPLAY_FIELD_NAME;

	/**
	 * @return {@linkplain su.opencode.kefir.gen.field.FieldSet#id() код} {@linkplain FieldSet филдсета}, в котором находитс€ поле.
	 * <br/>
	 *  од должен быть одним из кодов филдсетов, определенных в аннотации {@linkplain FieldSets FieldSets} к классу сущности.
	 */
	String fieldSetId() default "";


	public static final int DEFAULT_LIST_WIDTH = 0;
	public static final String DEFAULT_VALUE_FIELD_NAME = "id";
	public static final String DEFAULT_DISPLAY_FIELD_NAME = "name";
}
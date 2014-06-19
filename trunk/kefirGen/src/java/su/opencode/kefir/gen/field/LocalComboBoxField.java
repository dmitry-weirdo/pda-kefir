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
 * Аннотация поля, указыывающая, что поле является комбобоксом, выбирающим значение из локального js-хранилища.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LocalComboBoxField
{
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
	 * @see su.opencode.kefir.gen.field.ExtEntityFieldsUtils#getLocalComboBoxHiddenName(LocalComboBoxField, java.lang.reflect.Field)
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
	 * @see su.opencode.kefir.gen.field.ExtEntityFieldsUtils#getLocalComboBoxListWidth(LocalComboBoxField, java.lang.reflect.Field)
	 */
	int listWidth() default DEFAULT_LIST_WIDTH;

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
	boolean editable() default false;

	/**
	 * @return <code>true</code> — если поле транслирует свое значение в uppercase, <br/>
	 *         <code>false</code> — в противном случае
	 */
	boolean uppercase() default false;

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
	 * @return название js-переменной, содержащий локальное хранилище для комбобокса.
	 *         По умолчанию — (полное имя класса поля (предполагается enum) + "Store").
	 * @see su.opencode.kefir.gen.field.ExtEntityFieldsUtils#getLocalComboBoxStore(LocalComboBoxField, java.lang.reflect.Field)
	 */
	String store() default "";

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
	 * @return {@linkplain su.opencode.kefir.gen.field.FieldSet#id() код} {@linkplain FieldSet филдсета}, в котором находится поле.
	 * <br/>
	 * Код должен быть одним из кодов филдсетов, определенных в аннотации {@linkplain FieldSets FieldSets} к классу сущности.
	 */
	String fieldSetId() default "";


	public static final int DEFAULT_LIST_WIDTH = 0;
	public static final String DEFAULT_VALUE_FIELD_NAME = "id";
	public static final String DEFAULT_DISPLAY_FIELD_NAME = "name";
}
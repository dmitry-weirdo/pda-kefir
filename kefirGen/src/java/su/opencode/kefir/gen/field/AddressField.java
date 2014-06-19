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
 * Помечает поле адреса сущности.
 * VO сущности должно содержать поле адреса.
 * Для поля адреса в форме сущности создаются текствое поле, содержащее значение адреса как строку,
 * и кнопка "Изменить", по нажатию на которую открывается форма редактирования адреса.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AddressField
{
	/**
	 * @return <code>true</code> если для адресного поля нужно добавить one-to-one маппинг в маппинг сущности в orm.xml,<br/>
	 * <code>false</code> — если маппинг добавлять не надо
	 */
	boolean addOneToOneMapping() default true;

	/**
	 * @return имя sql-столбца, с помощью которого связанная сущность соединяется с таблицей адреса.
	 * По умолчанию — (имя поля в классе сущности, где каждое новое слово отделено подчерком + "_id").
	 */
	String joinColumnName() default "";

	/**
	 * @return название js-функции получения адреса в виде строки.
	 * функция должна находиться в неймспейсе формы адреса.
	 */
	String getAddressStrFunctionName() default "getAddressStr";

	/**
	 * @return название js-функции инициализации формы адреса.
	 */
	String initFunctionName() default "init";

	/**
	 * @return <code>true</code> — если используется сокращенная форма адреса для здания (без индекса, корпуса и офиса),<br/>
	 * <code>false</code> — в случае обычного, полного адреса.
	 */
	boolean building() default false;

	/**
 	 * @return id кнопки изменения адреса.
	 * По умолчанию — ( {@linkplain su.opencode.kefir.gen.ExtEntity#jsFieldPrefix() префикс js-поля формы сущности} + "-" + название поля)
	 */
	String textFieldId() default "";

	/**
	 * @return имя текстового поля, содержащего адрес как строку.
	 * <br/>
	 * По умолчанию — (имя поля + "Full")
	 */
	String textFieldName() default "";

	/**
	 * @return метка текстового поля
	 */
	String textFieldLabel();

	/**
	 * @return максимальная длина текстового поля
	 */
	int textFieldMaxLength() default 400;

	/**
	 * @return ширина текстового поля в пикселах
	 */
	int textFieldWidth() default 400;

	/**
	 * @return <code>true</code> — если поле можно не обязательно для заполнения, <br/>
	 *         <code>false</code> — если поле обязательно для заполнения.
	 * <br/>
	 * Влияет на соответстсвующий признак текстового поля с адресом как строкой.
	 */
	boolean allowBlank() default true;

	/**
	 * @return <code>true</code> — если поле формы, куда помещается полное значение адреса, транслирует свое значение в uppercase, <br/>
	 *         <code>false</code> — в противном случае
	 */
	boolean uppercase() default false;

	/**
 	 * @return id кнопки изменения адреса.
	 * По умолчанию — ( {@linkplain su.opencode.kefir.gen.ExtEntity#jsFieldPrefix() префикс js-поля формы сущности} + "-" + название поля + "-update" )
	 */
	String updateButtonId() default "";

	/**
	 * @return текст кнопки изменения адреса.
	 */
	String updateButtonText() default "Изменить";

	/**
	 * @return стиль кнопки изменения адреса
	 */
	String updateButtonStyle() default "{ marginLeft: 10 }";

	/**
	 * @return стиль columnPanel, содержащей текстовое поле и кнопку изменения.
	 */
	String columnPanelStyle() default "{ padding: 5 }";

	/**
	 * @return ширина formPanel, содержащей текстовое поле.
	 */
	int textFieldFormPanelWidth() default 610;

	/**
	 * @return ширина лейблов в formPanel, содержащей текстовое поле.
	 */
	int textFieldFormPanelLabelWidth() default 150;

	/**
	 * @return заголовок формы с адресом, используемой для редактирования поля.
	 */
	String addressWindowTitle();

	/**
	 * @return поле в VO, в котором будет содержаться Json-объект для Address поля.
	 * По умолчанию — равно имени поля.
	 */
	String voFieldName() default "";

	/**
	 * @return {@linkplain su.opencode.kefir.gen.field.FieldSet#id() код} {@linkplain FieldSet филдсета}, в котором находится поле.
	 * <br/>
	 * Код должен быть одним из кодов филдсетов, определенных в аннотации {@linkplain FieldSets FieldSets} к классу сущности.
	 */
	String fieldSetId() default "";
}
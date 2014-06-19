/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.field.searchField;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * Аннотация, помечающая поле в таблице и списке выбора сущнсостей, по которому будет
 * выполняться поиск с помощнью TwinTriggerField.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SearchField
{
	/**
	 * @return id для SearchField.
	 * По умолчанию — (id грида (таблицы поиска или выбора) + '-' + имя поля + "SearchField").
	 * Для полей из поля выбора (ChooseField) по умолчанию — (id грида (таблицы поиска или выбора) + '-' + имя поля выбора + имя поля выбираемой сущности + "SearchField")
	 */
	String id() default "";

	/**
	 * @return лэйбл, располагаемый в тулбаре перед полем поиска через пробел
	 */
	String label();

	/**
	 * @return номер ряда, в котором находится поле поиска.
	 */
	 int row() default FIRST_ROW_NUM;

	/**
	 * @return имя параметра, который будет передаваться в сервлет списка\выбора из списка сущностей.
	 * По умолчанию — для обычных полей — имя поля.
	 * Для полей выбора по умолчанию — (название поля выбора + {@linkplain #chooseFieldFieldName() название поля выбираемой сущности}).<br/>
	 * С этим же именем будет генериться параметр для FilterConfig.
	 */
	String paramName() default "";

	/**
	 * @return имя параметра, который будет использоваться в FilterConfig для поиска (фильтра) по полю.
	 * По умолчанию — для обычных полей — имя поля.
	 * Для полей выбора по умолчанию — (название поля выбора + "." + {@linkplain #chooseFieldFieldName() название поля выбираемой сущности}).<br/>
	 */
	String sqlParamName() default "";

	/**
	 * @return отношение, которое будет участвовать в sql-запросе для этого поля.
	 */
	Relation relation() default Relation.like;

	/**
	 * @return имя поля связанной сущности, по которому выполняется поиск.
	 * Используется только для связанных сущностей (ChooseField).
	 */
	String chooseFieldFieldName() default "name";

	/**
	 * @return максимальная длина поля
	 * По умолчанию — равна maxLength из аннотации поля, на котором стоит SearchField.
	 */
	int maxLength() default DEFAULT_MAX_LENGTH_VALUE;

	/**
	 * @return ширина поля в пикселах
	 */
	int width() default 300;

	/**
	 * @return <code>true</code> — если поле транслирует свое значение в uppercase, <br/>
	 *         <code>false</code> — в противном случае
	 */
	boolean uppercase() default false;

	/**
	 * @return маска ввода поля. Будет использоваться как параметр для Ext.ux.InputTextMask.
	 * <br/>
	 * По умолчанию — маска отсутствует.
	 */
	String mask() default "";

	/**
	 * @return <code>true</code> — если поле поиска становится фокусированным при открытии списка и выбора из списка сущностей.
	 * <br/>
	 * <code>false</code> — если поле не становится фокусированным.
	 * В случае, если нет полей с указанным признаком true, фокусируемым станет первое поле поиска (если оно есть).
	 */
	boolean defaultFocused() default false;

	public static final int DEFAULT_MAX_LENGTH_VALUE = -1;
	public static final int FIRST_ROW_NUM = 0;
}
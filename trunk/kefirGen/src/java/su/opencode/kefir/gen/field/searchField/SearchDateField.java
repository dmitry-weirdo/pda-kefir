/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.field.searchField;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Аннотация, помечающая поле в таблице и списке выбора сущнсостей, по которому будет
 * выполняться поиск с помощнью TwinTriggerField.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SearchDateField
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
	String paramDateName() default "";

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


	public static final int DEFAULT_MAX_LENGTH_VALUE = -1;
}
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
 * јннотаци€, помечающа€ поле в таблице и списке выбора сущнсостей, по которому будет
 * выполн€тьс€ поиск с помощнью TwinTriggerField.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SearchDateField
{
	/**
	 * @return id дл€ SearchField.
	 * ѕо умолчанию Ч (id грида (таблицы поиска или выбора) + '-' + им€ пол€ + "SearchField").
	 * ƒл€ полей из пол€ выбора (ChooseField) по умолчанию Ч (id грида (таблицы поиска или выбора) + '-' + им€ пол€ выбора + им€ пол€ выбираемой сущности + "SearchField")
	 */
	String id() default "";

	/**
	 * @return лэйбл, располагаемый в тулбаре перед полем поиска через пробел
	 */
	String label();

	/**
	 * @return им€ параметра, который будет передаватьс€ в сервлет списка\выбора из списка сущностей.
	 * ѕо умолчанию Ч дл€ обычных полей Ч им€ пол€.
	 * ƒл€ полей выбора по умолчанию Ч (название пол€ выбора + {@linkplain #chooseFieldFieldName() название пол€ выбираемой сущности}).<br/>
	 * — этим же именем будет генеритьс€ параметр дл€ FilterConfig.
	 */
	String paramName() default "";

	/**
	 * @return им€ параметра, который будет использоватьс€ в FilterConfig дл€ поиска (фильтра) по полю.
	 * ѕо умолчанию Ч дл€ обычных полей Ч им€ пол€.
	 * ƒл€ полей выбора по умолчанию Ч (название пол€ выбора + "." + {@linkplain #chooseFieldFieldName() название пол€ выбираемой сущности}).<br/>
	 */
	String paramDateName() default "";

	/**
	 * @return отношение, которое будет участвовать в sql-запросе дл€ этого пол€.
	 */
	Relation relation() default Relation.like;

	/**
	 * @return им€ пол€ св€занной сущности, по которому выполн€етс€ поиск.
	 * »спользуетс€ только дл€ св€занных сущностей (ChooseField).
	 */
	String chooseFieldFieldName() default "name";

	/**
	 * @return максимальна€ длина пол€
	 * ѕо умолчанию Ч равна maxLength из аннотации пол€, на котором стоит SearchField.
	 */
	int maxLength() default DEFAULT_MAX_LENGTH_VALUE;

	/**
	 * @return ширина пол€ в пикселах
	 */
	int width() default 300;

	/**
	 * @return <code>true</code> Ч если поле транслирует свое значение в uppercase, <br/>
	 *         <code>false</code> Ч в противном случае
	 */
	boolean uppercase() default false;


	public static final int DEFAULT_MAX_LENGTH_VALUE = -1;
}
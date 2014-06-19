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
 * Аннотация, помечающая поле, которое учитывается в FilterConfig,
 * но не создается как поле поиска в списке сущностей.
 */
@Target(ElementType.FIELD) // todo: наверное, нужна возможность ставить на класс для полей, которых нет в сущности
@Retention(RetentionPolicy.RUNTIME)
public @interface FilterConfigField
{
	/**
	 * @return имя поля, который будет создаваться в FilterConfig.
	 * По умолчанию — для обычных полей — имя поля.
	 * Для полей выбора по умолчанию — (название поля выбора + {@linkplain #chooseFieldFieldName() название поля выбираемой сущности}).<br/>
	 */
	String name() default "";

	/**
	 * @return тип поля, который будет создавтаься в FilterConfig.
	 * По умолчанию — для обычных полей — тип поля.
	 */
	String type() default "";

	/**
	 * @return имя параметра, который будет использоваться в FilterConfig для поиска (фильтра) по полю.
	 * По умолчанию — для обычных полей — имя поля.
	 * Для полей выбора по умолчанию — (название поля выбора + "." + {@linkplain #chooseFieldFieldName() название поля выбираемой сущности}).<br/>
	 */
	String sqlParamName() default "";

	/**
	 * @return значение, на равенство которому будет выполняться запрос.
	 * По умолчанию — значение поля FilterConfig.
	 */
	String sqlParamValue() default "";

	/**
	 * @return <code>true</code> — если перед sql параметром в запросе нужно подставлять (entityPrefix + ".").<br/>
	 * <code>false</code> — если нужно подставлять параметр "как есть" (актуально для полей join-сущностей)
	 */
	boolean addEntityPrefix() default true;

	/**
	 * @return отношение, которое будет участвовать в sql-запросе для этого поля.
	 */
	Relation relation() default Relation.equal;

	/**
	 * @return имя поля связанной сущности, по которому выполняется поиск.
	 * Используется только для связанных сущностей (ChooseField).
	 */
	String chooseFieldFieldName() default ID_FIELD_NAME;

	/**
	 * @return тип поля связанной сущности, по которому выполняется поиск.
	 * Используется только для связанных сущностей (ChooseField).
	 */
	String chooseFieldFieldType() default "Integer";

	/**
	 * @return <code>true</code> - если при fromJson поле преобразуется в верхний регистр,
	 * <code>false</code> - в противном случае.
	 */
	boolean uppercase() default true;

	/**
	 * @return <code>true</code> - если в параметр фильтрации сервлета
	 * нужно передавать не поле из сущности, а саму переменную фильтра
	 * <code>false</code> - если в параметр нужно передавать параметр переменной
	 */
	boolean filterPassSelfAsParam() default false;

	/**
	 * @return имя поля, которое будет браться из связанной сущности при фильтрации.
	 * Если явно не указано, то определяется аналогично {@linkplain #name имени поля FilterConfig}.
	 */
	String filterFieldName() default "";

	/**
	 * @return <code>true</code> — если поле нужно добавлять
	 * в параметры фильтрации списка сущностей и выбора из списка сущностей.
	 * <br/>
	 * <code>false</code> — в противном случае.
	 */
	boolean listFilterParam() default true;

	/**
	 * @return имя параметра конфига функций инициализации
	 * списков выбора и выбора из списка функций, в который будет передаваться параметр фильтрации.
	 * <br/>
	 * По умолчанию — имя класса поля с маленькой буквы.
	 */
	String listInitFunctionParamName() default "";

	/**
	 * @return строка, отображающаяся как часть заголовка списка сущностей
	 * при наличии фильтра по полю.
	 * (эта часть имеет вид &lt;ключ&gt;: &lt;значение&gt;)
	 */
	String listWindowFilterTitle() default "";

	/**
	 * @return <code>true</code> - если в заголовок окна списка от параметра фильтрации
	 * пишется только {@linkplain #listWindowFilterTitle() основная часть}.
	 * <br/>,
	 * <code>false</code> - если в заголовок окна списка пишется и основная часть,
	 * и, через двоеточие, параметр сущности.
	 */
	boolean listWindowTitleOnly() default false;

	/**
	 * @return имя параметра связанной сущности,
	 * значение который будет подставляться в часть заголовка списка сущностей.
	 * (эта часть имеет вид &lt;ключ&gt;: &lt;значение&gt;)
	 */
	String listWindowTitleParamName() default "name";

	/**
	 * @return <code>true</code> — если поле нужно добавлять
	 * в параметры фильтрации CRUD-формы.
	 * <br/>
	 * <code>false</code> — в противном случае.
	 */
	boolean formFilterParam() default true;

	/**
	 * @return имя параметра конфига функций инициализации
	 * CRUD-формы, в который будет передаваться параметр фильтрации.
	 * <br/>
	 * По умолчанию — имя класса поля с маленькой буквы.
	 */
	String formInitFunctionsParamName() default "";


	public static final String ID_FIELD_NAME = "id";
	public static final String NULL_VALUE = "null";
}
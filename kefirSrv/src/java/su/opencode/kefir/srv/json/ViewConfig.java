/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ rybakin
 $Revision$
 $Date: 29.11.2010 13:14:38$
*/
package su.opencode.kefir.srv.json;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = { ElementType.TYPE })
@Retention(value = RetentionPolicy.RUNTIME)
public @interface ViewConfig
{
	/**
	 * @return Название js viewConfig. Пркдполагается, что viewConfig находится в файле viewConfigs.js.
	 */
	String viewConfig() default "";

	/**
	 * @return <code>true</code>, если легенда есть в списке сущностей
	 * <br/>
	 * <code>false</code>, если легенды нет.
	 */
	boolean hasLegend() default true;

	/**
	 * @return текст лейбла легенды в случае невыбранной строки списка сущностей.
	 */
	String legendLabel() default "";

	/**
	 * @return имя поля VO, значение которого будет добавляться
	 * к {@linkplain #legendLabel() тексту лейбла легенды} при выборе строки списка сущностей.
	 */
	String legendLabelVOField() default "";

	/**
	 * @return Поля VO сущности, для которых при равенстве их значений true или false
	 * выделяется строка в списке сущностей и отображаются соответствующие поля легенды, если она есть.
	 */
	LegendField[] legendFields() default {};

	public static final String VIEW_CONFIG_PROPERTY_NAME = "viewConfig";
	public static final String DEFAULT_ROW_CLASS = "x-grid3-row";
	public static final String SELECTED_ROW_CLASS = "x-grid3-row-selected";
}
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

@Target(value = { ElementType.FIELD, ElementType.METHOD })
@Retention(value = RetentionPolicy.RUNTIME)
public @interface ColumnModel
{
	boolean checkbox() default false;
	boolean hidden() default false;
	boolean sortable() default DEFAULT_SORTABLE;
	String header() default "";
	String tooltip() default "";
	String renderer() default "";
	String editor() default "";
	String sortParam() default "";
	int width() default DEFAULT_WIDTH;
	String groupId() default "";
	ColumnModelSort sort() default @ColumnModelSort(sortDir = SortDirection.ASC, sortOrder = -1);

	/**
	 * @return порядковый номер столбца, должен начинаться с 0
	 */
	int order() default 0;
	ColumnModelPlugin[] plugins() default {};

	public static final int DEFAULT_WIDTH = 100;
	public static final boolean DEFAULT_SORTABLE = true;
	public static final String ORDER = "order";
	public static final String WIDTH = "width";
	public static final String GROUP_ID = "groupId";
	public static final String SORT_DIR = "sortDir";
	public static final String SORT_ORDER = "sortOrder";
	public static final String CHECKBOX = "checkbox";
	public static final String HIDDEN = "hidden";
	public static final String SORTABLE = "sortable";
	public static final String HEADER = "header";
	public static final String TOOLTIP = "tooltip";
	public static final String RENDERER = "renderer";
	public static final String EDITOR = "editor";
	public static final String DATA_INDEX = "dataIndex";
}
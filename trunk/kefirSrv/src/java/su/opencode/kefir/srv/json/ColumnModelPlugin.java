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

@Target(value = { ElementType.ANNOTATION_TYPE })
@Retention(value = RetentionPolicy.RUNTIME)
public @interface ColumnModelPlugin
{
	String name();
	String value();

	public static String SUMMARY = "summary";
	public static String SUMMARY_TYPE = "summaryType";
	public static String COUNT = "count";
	public static String SUM = "sum";
	public static String SUMMARY_RENDER = "summaryRenderer";
	public static String TOTAL_INSPECTION_RENDER = "ru.kg.gtn.render.totalInspections";
}
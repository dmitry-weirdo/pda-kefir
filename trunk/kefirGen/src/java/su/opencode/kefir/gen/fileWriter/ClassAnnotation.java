/*
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 05.04.2012 15:57:27$
*/
package su.opencode.kefir.gen.fileWriter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static su.opencode.kefir.util.StringUtils.concat;

public class ClassAnnotation
{
	public ClassAnnotation(String annotationClass) {
		this.annotationClass = annotationClass;
		this.params = new HashMap<String, Object>();
		this.keys = new LinkedList<String>();
	}
	public ClassAnnotation(Class annotationClass) {
		this.annotationClass = annotationClass.getSimpleName();
		this.params = new HashMap<String, Object>();
		this.keys = new LinkedList<String>();
	}

	public String getIndent() {
		return indent;
	}
	public void setIndent(String indent) {
		this.indent = indent;
	}
	public boolean isAlignChildren() {
		return alignChildren;
	}
	public void setAlignChildren(boolean alignChildren) {
		this.alignChildren = alignChildren;
	}

	public void put(String paramName, Object paramValue) {
		params.put( paramName, paramValue );

		if ( !keys.contains(paramName) )
			keys.add(paramName);
	}
	public void putString(String paramName, String paramValue) {
		params.put( paramName, concat(sb, "\"", paramValue, "\"") );

		if ( !keys.contains(paramName) )
			keys.add(paramName);
	}

	/**
	 * @return значение хэша в строчку
	 */
	public String toString() {
		if (indent != null) // указан отступ -> писать вертикалн
			return toStringAligned(indent);

		if (params.isEmpty())
			return concat(sb, "@", annotationClass, "()");

		sb.delete(0, sb.length());
		sb.append("@").append(annotationClass).append("( ");

		for (String key : keys)
		{
			sb.append(key).append(" = ").append(params.get(key)).append(FIELDS_SEPARATOR);
		}
		sb.delete(sb.length() - FIELDS_SEPARATOR.length(), sb.length()); // remove trailing ", "

		sb.append(" )");
		return sb.toString();
	}

	/**
	 * @param indent начальный отступ аннотации. На этот отступ будет отстоять от начала строки заркывающая скобка параметров аннотации.
	 * @return значение аннотации, отформатированное по кодстайлу и оттабленное указанным отступом.
	 * Вначале отступ не ставится (ибо сама аннотация может быть параметром другой аннотации.
	 * Перенос строки в конце не ставится, ибо после закрывающей скобки может быть нужна запятая
	 */
	public String toStringAligned(String indent) {
		if (params.isEmpty())
			return concat(sb, "@", annotationClass, "()");

		sb.delete(0, sb.length());
		sb.append("@").append(annotationClass).append("(\n");

		for (String key : keys)
		{
			Object value = params.get(key);
			String valueToWrite = value.toString();
			if (value instanceof ClassAnnotation)
			{
				if (this.alignChildren)
				{
					ClassAnnotation annotation = (ClassAnnotation) value;
					valueToWrite = annotation.toStringAligned( concat(indent, "\t") );
				}
			}

			sb.append(indent).append("\t").append(key).append(" = ").append(valueToWrite).append(FIELDS_SEPARATOR_SIMPLE).append(NEW_LINE);
		}

		sb.delete(
			sb.length() - NEW_LINE.length() - FIELDS_SEPARATOR_SIMPLE.length(),
			sb.length() - NEW_LINE.length()
		); // remove trailing ",", but not remove last "\n"

		sb.append(indent).append(")");
		return sb.toString();
	}

	/**
	 * Краткое имя класса аннотации
	 */
	private String annotationClass;

	/**
	 * Если не <code>null</code>, то в toString пишется аннотация с параметрами один в строчку с указанным отступом
	 */
	private String indent = null;

	/**
	 * Если <code>true</code>, то при выравненном написании все непосредственно вложенные хэши
	 * будут отформатированы, вне зависимости от их {@linkplain #indent свойства indent}.
	 */
	private boolean alignChildren = false;

	private List<String> keys; // сохраняет ключи в порядке их добавления

	private Map<String, Object> params; // пары "название параметра аннотации- значение"

	private StringBuffer sb = new StringBuffer();
	public static final String FIELDS_SEPARATOR_SIMPLE = ",";
	public static final String FIELDS_SEPARATOR = ", ";
	public static final String NEW_LINE = "\n";
}
/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.appender.js;

import static su.opencode.kefir.util.StringUtils.concat;
import su.opencode.kefir.gen.appender.Appender;
import static su.opencode.kefir.gen.fileWriter.js.JsFileWriter.JS_FILE_ENCODING;
import static su.opencode.kefir.gen.fileWriter.js.JsFileWriter.EXT_NAMESPACE_FUNCTION_NAME;

import java.io.IOException;
import java.util.List;

public abstract class JsAppender extends Appender
{
	protected String getEncoding() {
		return JS_FILE_ENCODING;
	}

	protected void appendNamespace(List<String> fileLines, String namespace) {
		fileLines.add( concat(sb, EXT_NAMESPACE_FUNCTION_NAME, "('", namespace, "');") );
	}

	/**
	 * ѕишет строковую константу 1-го уровн€ вложенности в функцию-неймспейс.
	 * @param fileLines массив строк, в который добавл€етс€ строка
	 * @param name название константы
	 * @param value значение константы (без оборачивающих кавычек)
	 * @throws java.io.IOException при ошибке записи в файл
	 */
	protected void appendConstant(List<String> fileLines, String name, String value) throws IOException {
		fileLines.add( concat(sb, "\tvar ", name, " = '", value, "';") );
	}
	/**
	 * ѕишет константу 1-го уровн€ вложенности с кавычками или без в функцию-неймспейс.
	 * @param fileLines массив строк, в который добавл€етс€ строка
	 * @param name название константы
	 * @param value значение константы (без оборачивающих кавычек)
	 * @param quoted <code>true</code> Ч если нужно оборачивать значение кавычками, <code>false</code> Ч если значение нужно писать как есть
	 * @throws IOException при ошибке записи в файл
	 */
	protected void appendConstant(List<String> fileLines, String name, String value, boolean quoted) throws IOException {
		if (quoted)
			appendConstant(fileLines, name, value);
		else
			fileLines.add( concat(sb, "\tvar ", name, " = ", value, ";") );
	}


	protected String filePath;
}
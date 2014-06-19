/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.appender.jsInclude;

import su.opencode.kefir.gen.appender.Appender;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static su.opencode.kefir.gen.fileWriter.js.JsFileWriter.JS_FILE_ENCODING_FOR_INCLUDE;
import static su.opencode.kefir.gen.project.xml.html.HtmlFileWriter.JS_SCRIPT_TYPE;
import static su.opencode.kefir.gen.project.xml.html.HtmlFileWriter.SCRIPT_ELEMENT_NAME;
import static su.opencode.kefir.util.StringUtils.concat;

public class JsIncludeAppender extends Appender
{
	public JsIncludeAppender(String includeFilePath, String baseJsPath, String dir, String fileName) {
		this.includeFilePath = includeFilePath;
		this.baseJsPath = baseJsPath;
		this.dir = dir;
		this.fileName = fileName;
	}
	protected String getEncoding() {
		return JSP_FILE_ENCODING;
	}

	public void appendJsInclude() throws IOException {
		File file = new File(includeFilePath);
		List<String> fileLines = readLinesFromFile(file);

		appendLines(fileLines);
		writeLinesToFile(file, fileLines);
	}
	private void appendLines(List<String> fileLines) {
		String appendMarker = APPEND_INCLUDE_MARKER;

		for (int i = 0; i < fileLines.size(); i++)
		{
			String fileString = fileLines.get(i);
			if (fileString.contains(appendMarker))
			{
				List<String> appendedFileLines = new LinkedList<String>();

				appendedFileLines.addAll( fileLines.subList(0, i) );
				appendJsInclude(appendedFileLines, fileLines);
				appendedFileLines.addAll( fileLines.subList(i, fileLines.size()) );

				fileLines.clear();
				fileLines.addAll(appendedFileLines);

				return;
			}
		}
	}
	private void appendJsInclude(List<String> fileLinesToAppend, List<String> allFileLines) {
		String srcParam = concat( sb, "src=\"", getSrcPath(), "\"" );

		for (String fileLine : allFileLines)
		{ // если этот файл уже включен -> ничего не делать
			if (fileLine.contains(srcParam))
			{
				logger.info( concat(sb, "Js file \"", getSrcPath(), "\" is already included in file \"", includeFilePath, "\"") );
				return;
			}
		}

		fileLinesToAppend.add(getInclude());
	}


	private String getInclude() {
		// <script type="text/javascript" src="./js/leasing/parcelForm.js" charset="UTF-8"></script>
		return concat(sb, "<", SCRIPT_ELEMENT_NAME, " type=\"", JS_SCRIPT_TYPE, "\" src=\"", getSrcPath(), "\" charset=\"", JS_FILE_ENCODING_FOR_INCLUDE, "\"></", SCRIPT_ELEMENT_NAME, ">");
	}
	private String getSrcPath() {
		String basePath = baseJsPath;
		basePath = basePath.replaceAll(BACK_SEPARATOR, SEPARATOR); // заменить "\" на "/"
		if (basePath.endsWith(SEPARATOR)) // удалить конечный "/"
			basePath = basePath.substring(0, basePath.length() - 1);


		String fileDir = dir;
		fileDir = fileDir.replaceAll(BACK_SEPARATOR, SEPARATOR); // заменить "\" на "/"
		if (!fileDir.startsWith(SEPARATOR)) // добавить начальный "/"
			fileDir = concat(sb, SEPARATOR, fileDir);

		if (!fileDir.endsWith(SEPARATOR)) // добавить конечный "/"
			fileDir = concat(fileDir, SEPARATOR);


		String jsFileName = fileName;
		if (jsFileName.startsWith(SEPARATOR)) // удалить начальный "/"
			jsFileName = jsFileName.substring(SEPARATOR.length());

		if (!fileName.endsWith(JS_FILE_EXTENSION))
			jsFileName = concat(fileName, JS_FILE_EXTENSION);

		return concat(sb, basePath, fileDir, jsFileName);
	}

	/**
	 * Полный путь к файлу с инклюдами джаваскриптов (application.jspf).
	 */
	private String includeFilePath;

	/**
	 * Базовый путь к js-файлам в &lt;script src="..."/&gt;. Как правило, равен ".js".
	 */
	private String baseJsPath;

	/**
	 * Путь к включаемому файлу относительно базовой директории (например, "/leasing").
	 */
	private String dir;

	/**
	 * Название включаемого файла (например, "parcelForm.js").
	 */
	private String fileName;

	public static final String SEPARATOR = "/";
	public static final String BACK_SEPARATOR = "\\\\";
	public static final String JS_FILE_EXTENSION = ".js";
	public static final String JSP_FILE_ENCODING = "Cp1251";
	public static final String APPEND_INCLUDE_MARKER = "${APPEND_INCLUDE}";
}
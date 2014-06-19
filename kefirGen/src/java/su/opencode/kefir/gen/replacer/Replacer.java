/*
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 04.04.2012 15:11:37$
*/
package su.opencode.kefir.gen.replacer;

import org.apache.log4j.Logger;
import su.opencode.kefir.gen.appender.Appender;
import su.opencode.kefir.gen.project.ProjectConfig;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static su.opencode.kefir.util.StringUtils.concat;

public abstract class Replacer extends Appender
{
	protected Replacer(String templateFilePath, String filePath) {
		this.filePath = filePath;
		this.templateFilePath = templateFilePath;
	}
	protected Replacer(String templateFilePath, String filePath, ProjectConfig config) {
		this.templateFilePath = templateFilePath;
		this.filePath = filePath;
		this.config = config;
	}

	public void createFile() throws IOException {
		fillValues();

		File templateFile = new File(templateFilePath);
		List<String> fileLines = readLinesFromFile(templateFile);

		List<String> replacedFileLines = new ArrayList<String>();

		for (String fileLine : fileLines)
			replacedFileLines.add( replace(fileLine) );

		File file = new File(filePath);
		writeLinesToFile(file, replacedFileLines);
	}

	protected String replace(String str) {
		Pattern pattern = Pattern.compile( getMarkerPattern() );
		Matcher matcher = pattern.matcher(str);

		sb.delete(0, sb.length());

		while ( matcher.find() )
		{
			String key = getKey( matcher.group() );

			String replacement = values.get(key);
			if (replacement == null)
				throw new IllegalArgumentException( concat(sb, "Unknown key for replacement: \"", key, "\"") );

			logger.info( concat("replaced \"", key, "\" with \"", replacement, "\"") );

			matcher.appendReplacement(sb, replacement);
		}

		matcher.appendTail(sb);
		return sb.toString();
	}
	private String getKey(String group) {
		return group.substring( getMarkerStart().length(), group.length() - getMarkerEnd().length() );
	}

	/**
	 * @return маркер, означающий начало подстроки-ключа
	 */
	protected abstract String getMarkerStart();

	/**
	 * @return маркер, означающий окончание подстроки-ключа
	 */
	protected abstract String getMarkerEnd();

	/**
	 * @return паттерн для выделения подстрок-ключей.
	 */
	protected abstract String getMarkerPattern();

	/**
	 * Заполнение мэпа values парами "ключ-значение".
	 */
	protected abstract void fillValues();

	protected Map<String, String> values = new HashMap<String, String>();

	protected String templateFilePath;
	protected String filePath;
	protected ProjectConfig config;

	protected StringBuffer sb = new StringBuffer();
	protected static final Logger logger = Logger.getLogger(Replacer.class);
}
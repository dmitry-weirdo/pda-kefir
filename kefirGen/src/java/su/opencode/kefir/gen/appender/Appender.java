/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.appender;

import static su.opencode.kefir.gen.ExtEntityUtils.isInPackage;
import su.opencode.kefir.gen.fileWriter.ClassFileWriter;
import su.opencode.kefir.gen.fileWriter.FileWriter;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static su.opencode.kefir.util.StringUtils.concat;
import org.apache.log4j.Logger;

public abstract class Appender
{
	protected abstract String getEncoding();

	protected List<String> readLinesFromFile(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), getEncoding()));

		try
		{
			List<String> fileLines = new LinkedList<String>();

			String tmp;
			while ( (tmp = reader.readLine()) != null )
				fileLines.add(tmp);

			return fileLines;
		}
		finally
		{
			reader.close();
		}
	}
	protected void writeLinesToFile(File file, List<String> fileStrings) throws IOException {
		try
		{
			out = new FileWriter(file, getEncoding());
			for (int i = 0; i < fileStrings.size() - 1; i++)
				out.writeLn(fileStrings.get(i));

			out.write(fileStrings.get(fileStrings.size() - 1)); // last string without new line
		}
		finally
		{
			if (out != null)
				out.close();
		}
	}

	protected void appendEmptyLine(List<String> fileLines) {
		fileLines.add("");
	}

	protected List<String> getImports(List<String> fileStrings) {
		List<String> imports = new ArrayList<String>();

		for (String fileString : fileStrings)
			if (fileString.startsWith("import "))
				imports.add(fileString);

		return imports;
	}

	protected List<String> getPrivateFields(List<String> fileStrings) {
		List<String> fields = new ArrayList<String>();

		for (String fileString : fileStrings)
			if (fileString.startsWith("\tprivate "))
				fields.add(fileString);

		return fields;
	}

	protected String getPackage(List<String> fileStrings) {
		for (String fileString : fileStrings)
		{
			if (fileString.startsWith("package "))
			{
				return fileString.substring("package ".length(), fileString.indexOf(";")).trim();
			}
		}

		return null;
	}

	protected void addImport(Class classToImport, List<String> fileLines, List<String> imports) {
		addImport(classToImport.getName(), fileLines, imports);
	}
	protected void addImport(String className, List<String> fileLines, List<String> imports) {
		String packageName = getPackage(fileLines); // начальные строки заведомо содержат package

		if ( isInPackage(className, packageName) )
			return; // если импортирумемый класс находится в том же пакете, что и класс, в который добавляют -> импорт не нужен

		String newImport = ClassFileWriter.getImport(sb, className);

		if (imports.contains(newImport))
		{
			logger.info( concat(sb, "import \"", newImport, "\" is already present" ) );
			return; // импорт уже присутствует в списке -> добавлять его повторно не нужно
		}

		fileLines.add(newImport);
	}
	protected void addStaticImport(String className, String methodName, List<String> fileLines, List<String> imports) {
		String newImport = ClassFileWriter.getStaticImport(sb, className, methodName);
		if (imports.contains(newImport))
		{
			logger.info( concat(sb, "import \"", newImport, "\" is already present" ) );
			return; // импорт уже присутствует в списке -> добавлять его повторно не нужно
		}

		fileLines.add(newImport);
	}
	protected void addStaticImport(Class classToImport, String methodName, List<String> fileLines, List<String> imports) {
		addStaticImport(classToImport.getName(), methodName, fileLines, imports);
	}

	protected void addField(String fieldType, String fieldName, String annotationClassName, List<String> fileLines, List<String> fields) {
		String newField = concat(sb, "\tprivate ", fieldType, " ", fieldName, ";");

		if (fields.contains(newField)) // поле уже присутствует в списке // todo: нормальная проверка (с trim итд)
		{
			logger.info( concat(sb, "private field \"", newField, "\" already present" ) );
			return;
		}

		fileLines.add( concat(sb, "\t", annotationClassName) );
		fileLines.add(newField);
		fileLines.add("");
	}

	protected void addPublicStringConstant(String fieldName, String fieldValue, List<String> fileLines, List<String> allFileLines) {
		String newField = concat(sb, "\tpublic static final String ", fieldName, " = \"", fieldValue, "\";");

		if (allFileLines.contains(newField)) // поле уже присутствует в списке // todo: нормальная проверка (с trim итд)
		{
			logger.info( concat(sb, "public static final String field \"", newField, "\" already present" ) );
			return;
		}

		fileLines.add(newField);
	}

	protected void addComment(String comment, String indent, List<String> fileLines) {
		fileLines.add( concat(sb, indent, "// ", comment) );
	}
	protected void addComment(String comment, List<String> fileLines) {
		addComment(comment, "\t\t", fileLines);
	}

	protected FileWriter out;
	protected StringBuffer sb = new StringBuffer();

	protected static final Logger logger = Logger.getLogger(Appender.class);
}
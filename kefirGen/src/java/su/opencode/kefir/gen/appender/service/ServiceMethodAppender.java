/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.appender.service;

import su.opencode.kefir.gen.ExtEntity;
import su.opencode.kefir.gen.appender.Appender;
import su.opencode.kefir.gen.fileWriter.LocalServiceFileWriter;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static su.opencode.kefir.gen.ExtEntityUtils.*;
import static su.opencode.kefir.gen.fileWriter.ClassFileWriter.CLASS_FILE_ENCODING;
import static su.opencode.kefir.gen.fileWriter.LocalServiceFileWriter.APPEND_IMPORT_MARKER;
import static su.opencode.kefir.gen.fileWriter.LocalServiceFileWriter.APPEND_METHOD_DECLARATION_MARKER;
import static su.opencode.kefir.util.FileUtils.FILE_SEPARATOR;
import static su.opencode.kefir.util.StringUtils.concat;

/**
 * Добавляет описание методов сервиса в указанный сервис.
 * <br/>
 * Если класс сервиса не существует, он создается с помощью {@linkplain su.opencode.kefir.gen.fileWriter.LocalServiceFileWriter LocalServiceFileWriter}
 * <br/>
 * Если класс сервиса существует, то методы добавляются в место указанное {@linkplain su.opencode.kefir.gen.fileWriter.LocalServiceFileWriter#APPEND_METHOD_DECLARATION_MARKER маркером}.
 */
public abstract class ServiceMethodAppender extends Appender
{
	public ServiceMethodAppender(String baseDir, ExtEntity extEntity, Class entityClass) {
		this.baseDir = baseDir;
		this.extEntity = extEntity;
		this.entityClass = entityClass;
	}
	protected String getEncoding() {
		return CLASS_FILE_ENCODING;
	}

	public void appendMethods() throws IOException {
		File file = getServiceClassFile();
		List<String> fileLines = readLinesFromFile(file);

		appendLines(fileLines, AppendMode.imports);
		appendLines(fileLines, AppendMode.declaration);
		writeLinesToFile(file, fileLines);
	}
	private File getServiceClassFile() throws IOException {
		String packageName = getPackageName( getServiceClassName(extEntity, entityClass) );
		String classSimpleName = getServiceClassSimpleName(extEntity, entityClass);

		String dirPath = concat(sb, baseDir, FILE_SEPARATOR, packageName.replace(".", FILE_SEPARATOR));
		new File(dirPath).mkdirs(); // если директорий нет, создать их
		String filePath = concat(sb, dirPath, FILE_SEPARATOR, classSimpleName, ".java");
		File file = new File(filePath);

		if ( !file.exists() )
		{ // создать класс сервиса, если он не существует
			new LocalServiceFileWriter(baseDir, packageName, classSimpleName).createFile();
			file = new File(filePath);
		}

		return file;
	}

	private void appendLines(List<String> fileLines, AppendMode mode) throws IOException {
		String appendMarker = getAppendMarker(mode);
		List<String> imports = getImports(fileLines);

		if (mode == AppendMode.declaration && methodIsPresent(fileLines))
			return;

		for (int i = 0; i < fileLines.size(); i++)
		{
			String fileString = fileLines.get(i);
			if (fileString.contains(appendMarker))
			{
				List<String> appendedFileLines = new LinkedList<String>();

				appendedFileLines.addAll( fileLines.subList(0, i) );
				appendContent(appendedFileLines, imports, mode);
				appendedFileLines.addAll( fileLines.subList(i, fileLines.size()) );

				fileLines.clear();
				fileLines.addAll(appendedFileLines);

				return;
			}
		}
	}
	private boolean methodIsPresent(List<String> fileLines) {
		List<String> signatures = getMethodSignatures();

		for (String fileLine : fileLines)
			for (String signature : signatures)
				if (fileLine.contains(signature))
				{
					logger.info(getMethodIsPresentLogMessage(signature));
					return true;
				}

		return false;
	}
	private String getMethodIsPresentLogMessage(String signature) {
		return concat(sb, "method \"", signature, "\" is already present");
	}

	private String getAppendMarker(AppendMode mode) {
		switch (mode)
		{
			case imports: return APPEND_IMPORT_MARKER;
			case declaration: return APPEND_METHOD_DECLARATION_MARKER;

			default: throw new IllegalArgumentException( concat(sb, "Incorrect AppendMode:", mode) );
		}
	}
	private void appendContent(List<String> appendedFileLines, List<String> imports, AppendMode mode) {
		switch (mode)
		{
			case imports: appendImports(appendedFileLines, imports); break;
			case declaration: appendMethods(appendedFileLines); break;

			default: throw new IllegalArgumentException( concat(sb, "Incorrect AppendMode:", mode) );
		}
	}

	protected String getSignatureString(String signature) {
		return concat(sb, "\t", signature, ";");
	}
	protected void addSignature(List<String> fileLines, String signature) {
		fileLines.add( getSignatureString(signature) );
	}

	/**
	 * Добавляет необходимые импорты для методов сервиса.
	 * @param fileLines строки, считанные из файла, добавлять нужно в их конец, позиция строк уже стоит перед маркером.
	 * @param imports строки, содержащие уже имеющиеся импорты (нужно проверять, что они не совпадают с добавляемыми импортами).
	 */
	protected abstract void appendImports(List<String> fileLines, List<String> imports);

	/**
	 * @return список сигнатур методов, добавляемых в сервис.
	 */
	protected abstract List<String> getMethodSignatures();

	/**
	 * Добавляет методы сервиса перед указанным маркером.
	 * @param fileLines строки, считанные из файла, добавлять нужно в их конец, позиция строк уже стоит перед маркером.
	 */
	protected abstract void appendMethods(List<String> fileLines);

	protected String baseDir;
	protected ExtEntity extEntity;
	protected Class entityClass;

	public enum AppendMode { imports, declaration }
}
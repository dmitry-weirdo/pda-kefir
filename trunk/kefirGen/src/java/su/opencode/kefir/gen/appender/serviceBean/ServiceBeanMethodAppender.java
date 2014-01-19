/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.appender.serviceBean;

import static su.opencode.kefir.util.FileUtils.FILE_SEPARATOR;
import static su.opencode.kefir.util.StringUtils.concat;
import static su.opencode.kefir.util.StringUtils.decapitalize;
import su.opencode.kefir.gen.ExtEntity;
import static su.opencode.kefir.gen.ExtEntityUtils.*;
import su.opencode.kefir.gen.appender.Appender;
import su.opencode.kefir.gen.fileWriter.ClassFileWriter;
import su.opencode.kefir.gen.fileWriter.LocalServiceBeanFileWriter;
import static su.opencode.kefir.gen.fileWriter.LocalServiceBeanFileWriter.*;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * ��������� ����������� ������� ������� � ��������� �����, ����������� ������.
 * <br/>
 * ���� ����� �� ����������, �� ��������� � ������� {@linkplain su.opencode.kefir.gen.fileWriter.LocalServiceBeanFileWriter LocalServiceBeanFileWriter}
 * <br/>
 * ���� ����� ���������� ������� ����������, �� ������ ����������� � ����� ������� {@linkplain su.opencode.kefir.gen.fileWriter.LocalServiceBeanFileWriter#APPEND_METHOD_IMPLEMENTATION_MARKER ��������}.
 */
public abstract class ServiceBeanMethodAppender extends Appender
{
	public ServiceBeanMethodAppender(String baseDir, ExtEntity extEntity, Class entityClass) {
		this.baseDir = baseDir;
		this.extEntity = extEntity;
		this.entityClass = entityClass;
	}
	protected String getEncoding() {
		return ClassFileWriter.CLASS_FILE_ENCODING;
	}
	public void appendMethods() throws IOException {
		File file = getServiceClassFile();
		List<String> fileLines = readLinesFromFile(file);

		appendLines(fileLines, AppendMode.imports);
		appendLines(fileLines, AppendMode.implementation);
		appendLines(fileLines, AppendMode.fields);
		writeLinesToFile(file, fileLines);
	}
	private File getServiceClassFile() throws IOException {
		String packageName = getPackageName( getServiceBeanClassName(extEntity, entityClass) );
		String classSimpleName = getServiceBeanClassSimpleName(extEntity, entityClass);

		String dirPath = concat(sb, baseDir, FILE_SEPARATOR, packageName.replace(".", FILE_SEPARATOR));
		new File(dirPath).mkdirs(); // ���� ���������� ���, ������� ��
		String filePath = concat(sb, dirPath, FILE_SEPARATOR, classSimpleName, ".java");
		File file = new File(filePath);

		if ( !file.exists() )
		{ // ������� ����� �������, ���� �� �� ����������
			new LocalServiceBeanFileWriter(baseDir, packageName, classSimpleName, getServiceClassName(extEntity, entityClass)).createFile();
			file = new File(filePath);
		}

		return file;
	}

	private void appendLines(List<String> fileLines, AppendMode mode) throws IOException {
		String appendMarker = getAppendMarker(mode);
		List<String> imports = getImports(fileLines);
		List<String> fields = getPrivateFields(fileLines);

		if (mode == AppendMode.implementation && methodIsPresent(fileLines))
			return;

		for (int i = 0; i < fileLines.size(); i++)
		{
			String fileString = fileLines.get(i);
			if (fileString.contains(appendMarker))
			{
				List<String> appendedFileLines = new LinkedList<>();

				appendedFileLines.addAll( fileLines.subList(0, i) );
				appendContent(appendedFileLines, imports, fields, mode);
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
		return concat(sb, "method implementation \"", signature, "\" is already present");
	}
	private String getAppendMarker(AppendMode mode) {
		switch (mode)
		{
			case imports: return APPEND_IMPORT_MARKER;
			case implementation: return APPEND_METHOD_IMPLEMENTATION_MARKER;
			case fields: return APPEND_FIELD_MARKER;

			default: throw new IllegalArgumentException( concat(sb, "Incorrect AppendMode:", mode) );
		}
	}
	private void appendContent(List<String> appendedFileLines, List<String> imports, List<String> fields, AppendMode mode) {
		switch (mode)
		{
			case imports: appendImports(appendedFileLines, imports); break;
			case implementation: appendMethods(appendedFileLines); break;
			case fields: appendFields(appendedFileLines, fields); break;

			default: throw new IllegalArgumentException( concat(sb, "Incorrect AppendMode:", mode) );
		}
	}

	protected void addMethodStart(List<String> fileLines, String signature) {
		fileLines.add( getMethodStartString(signature) );
	}
	protected String getMethodStartString(String signature) {
		return concat(sb, "\tpublic ", signature, " {");
	}

	protected void addMethodEnd(List<String> fileLines) {
		fileLines.add( getMethodEndString() );
	}
	protected String getMethodEndString() {
		return "\t}";
	}

	/**
	 * ��������� ����������� ������� ��� ������� ���������� �������.
	 * @param fileLines ������, ��������� �� �����, ��������� ����� � �� �����, ������� ����� ��� ����� ����� ��������.
	 * @param imports ������, ���������� ��� ��������� ������� (����� ���������, ��� ��� �� ��������� � ������������ ���������).
	 */
	protected abstract void appendImports(List<String> fileLines, List<String> imports);

	/**
	 * @return ������ �������� �������, ����������� � ������.
	 */
	protected abstract List<String> getMethodSignatures();

	/**
	 * ��������� ���������� ������� ������� ����� ��������� ��������.
	 * @param fileLines ������, ��������� �� �����, ��������� ����� � �� �����, ������� ����� ��� ����� ����� ��������.
	 */
	protected abstract void appendMethods(List<String> fileLines);

	/**
	 * ��������� ���� ���������� �������.
	 * @param fileLines ������, ��������� �� �����, ��������� ����� � �� �����, ������� ����� ��� ����� ����� ��������.
	 * @param fields ������, ���������� ��� ��������� private ���� (����� ���������, ��� ��� �� ��������� � ������������ ������� �� �����).
	 */
	protected void appendFields(List<String> fileLines, List<String> fields) {
		// default do nothing
	}

	protected void appendEjbField(String serviceClassFullName, List<String> fileLines, List<String> fields) {
		if ( serviceClassFullName.equals( getServiceClassName(extEntity, entityClass) ) ) // ��������� ���������� ������ ������� �� ������ ����
			return;

		String simpleName = getSimpleName(serviceClassFullName);
		addField(simpleName, decapitalize(simpleName), "@EJB", fileLines, fields );
	}

	protected String baseDir;
	protected ExtEntity extEntity;
	protected Class entityClass;

	public enum AppendMode { imports, implementation, fields }
}
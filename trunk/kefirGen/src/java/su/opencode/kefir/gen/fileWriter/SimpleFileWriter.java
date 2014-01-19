/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.fileWriter;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;

import static su.opencode.kefir.util.FileUtils.FILE_SEPARATOR;
import static su.opencode.kefir.util.StringUtils.concat;

public abstract class SimpleFileWriter
{
	public SimpleFileWriter() {
	}
	public SimpleFileWriter(String baseDir, String fileName, String className) {
		this.baseDir = baseDir;
		this.fileName = fileName;
		this.className = className;
	}

	public void createFile() throws IOException {
		final File file = createClassFile(baseDir, fileName);
		if (file.exists())
		{
			if (failIfFileExists)
			{
				throw new IllegalStateException(concat(sb, "file \"", file.getAbsolutePath(), "\" already exists"));
			}
			else
			{ // �������� ������������ ���� ��� ����
				final String overwritten = overwriteIfFileExists ? "overwritten" : "not overwritten";
				logger.info(concat(sb, "file \"", file.getAbsolutePath(), "\" already exists. It is ", overwritten, "."));

				if (!overwriteIfFileExists)
					return;
			}
		}

		out = new FileWriter(file, CLASS_FILE_ENCODING);

		try
		{
			writeFile();
		}
		finally
		{
			out.close();
		}
	}

	protected abstract void writeFile() throws IOException;

	/**
	 * ����� ����������� � ��������� ��������.
	 *
	 * @param indent	������ (��� �������, �������� ���� ��� ��������� �����)
	 * @param comment �������� �����������
	 * @throws java.io.IOException ��� ������ ������ � ����
	 */
	protected void writeComment(String indent, String comment) throws IOException {
		out.writeLn(indent, "// ", comment);
	}

	/**
	 * ����� ����������� 1-�� ������ ����������� (� ����� ����� �������).
	 *
	 * @param comment �������� �����������
	 * @throws java.io.IOException ��� ������ ������ � ����
	 */
	protected void writeComment(String comment) throws IOException {
		writeComment(TAB, comment);
	}

	private File createClassFile(String baseDir, String fileName) {
		new File(baseDir).mkdirs(); // ���� ���������� ���, ������� ��
		return new File(concat(sb, baseDir, FILE_SEPARATOR, fileName));
	}

	protected boolean failIfFileExists = true;
	protected boolean overwriteIfFileExists = false;

	protected static final String TAB = "\t";
	protected static final String DOUBLE_TAB = "\t\t";
	protected static final String TRIPLE_TAB = "\t\t\t";
	protected static final String QUADRUPLE_TAB = "\t\t\t\t";

	protected String baseDir;
	protected String fileName;
	protected String className;

	protected FileWriter out;
	protected StringBuffer sb = new StringBuffer();

	private static final Logger logger = Logger.getLogger(SimpleFileWriter.class);

	public static final String DEFAULT_LOGGER_FIELD_NAME = "logger";
	public static final String CLASS_FILE_ENCODING = "Cp1251";
	public static final String CLASS_FILE_EXTENSION = ".java";
}
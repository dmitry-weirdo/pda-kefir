/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 28.03.2012 12:20:53$
*/
package su.opencode.kefir.gen.project.sql;

import org.apache.log4j.Logger;
import su.opencode.kefir.gen.fileWriter.FileWriter;
import su.opencode.kefir.gen.project.ProjectConfig;

import java.io.File;
import java.io.IOException;

import static su.opencode.kefir.util.FileUtils.FILE_SEPARATOR;
import static su.opencode.kefir.util.FileUtils.createDirs;
import static su.opencode.kefir.util.StringUtils.concat;

public abstract class SqlFileWriter
{
	public SqlFileWriter(String baseDir, String dir, String fileName) {
		this.baseDir = baseDir;
		this.dir = dir;
		this.fileName = fileName;
	}
	protected SqlFileWriter(String baseDir, String dir, String fileName, ProjectConfig config) {
		this.baseDir = baseDir;
		this.dir = dir;
		this.fileName = fileName;
		this.config = config;
	}

	public void createFile() throws IOException {
		File file = createSqlFile();
		if (file.exists())
		{ // todo: возможность внешним образом задавать перезапись существующих файлов
			if (failIfFileExists)
			{
				throw new IllegalStateException( concat(sb, "file \"", file.getAbsolutePath(), "\" already exists") );
			}
			else
			{ // оставить существующий файл как есть
				logger.info( concat(sb, "file \"", file.getAbsolutePath(), "\" already exists. It is not overwritten.") );
				return;
			}
		}

		out = new FileWriter(file, SQL_FILE_ENCODING);

		try
		{
			writeFile();
		}
		finally
		{
			out.close();
		}
	}

	private File createSqlFile() {
		String dirPath = concat(sb, baseDir, FILE_SEPARATOR, dir);
		createDirs(dirPath); // если директорий нет, создать их

		String filePath;
		if (fileName.endsWith(SQL_FILE_EXTENSION)) // если имя файла уже содержит ".sql", не добавлять его
			filePath = concat(sb, dirPath, FILE_SEPARATOR, fileName);
		else
			filePath = concat(sb, dirPath, FILE_SEPARATOR, fileName, SQL_FILE_EXTENSION); // добавить ".sql" к имени файла

		return new File(filePath);
	}

	protected void writeComment(String comment) throws IOException {
		out.writeLn(SQL_COMMENT_PREFIX, comment);
	}

	protected void writeCreateTableHeader(String tableName) throws IOException {
		out.writeLn("create table ", tableName);
		out.writeLn("(");
	}
	protected void writeCreateTableFooter() throws IOException {
		out.writeLn(");");
	}

	protected void writeCreateGenerator(String generatorName) throws IOException {
		out.writeLn( getCreateGeneratorStr(sb, generatorName) );
	}
	public static String getCreateGeneratorStr(StringBuffer sb, String generatorName) {
		return concat(sb, "create generator ", generatorName, ";");
	}

	protected void writeSetGenerator(String generatorName, int value) throws IOException {
		out.writeLn( getSetGeneratorStr(sb, generatorName, value) );
	}
	public static String getSetGeneratorStr(StringBuffer sb, String generatorName, int value) {
		return concat(sb, "set generator ", generatorName, " to ", Integer.toString(value), ";");
	}

	protected void writeSetGenerator(String generatorName) throws IOException { // default set to 0
		writeSetGenerator(generatorName, DEFAULT_GENERATOR_VALUE);
	}

	protected void writeDropTable(String tableName) throws IOException {
		out.writeLn( getDropTableStr(sb, tableName) );
	}
	public static String getDropTableStr(StringBuffer sb, String tableName) {
		return concat(sb, "drop table ", tableName, ";");
	}
	protected void writeDropTable(Class entityClass) throws IOException {
		writeDropTable(getSqlTableName(entityClass));
	}

	protected void writeDropGenerator(String generatorName) throws IOException {
		out.writeLn( getDropGeneratorStr(sb, generatorName) );
	}
	public static String getDropGeneratorStr(StringBuffer sb, String generatorName) {
		return concat(sb, "drop generator ", generatorName, ";");
	}
	protected void writeDropGenerator(Class entityClass) throws IOException {
		writeDropGenerator(getGeneratorName(entityClass));
	}

	protected void writeCommit() throws IOException {
		out.write("commit;");
	}

	public static String getSqlTableName(String className) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < className.length(); i++)
		{
			char ch = className.charAt(i);

			if ( i != 0 && Character.isUpperCase(ch) ) // перед первым символом в строке подчеркивание не ставится
				sb.append(TABLE_NAME_SEPARATOR).append(ch);
			else
				sb.append(ch);
		}

		return sb.toString();
	}
	public static String getSqlTableName(Class entityClass) {
		return getSqlTableName(entityClass.getSimpleName());
	}
	public static String getGeneratorName(String className) {
		return concat( getSqlTableName(className).toLowerCase(), GENERATOR_NAME_POSTFIX);
	}
	public static String getGeneratorName(Class entityClass) {
		return getGeneratorName(entityClass.getSimpleName());
	}

	protected abstract void writeFile() throws IOException;

	protected boolean failIfFileExists = false; // если sql файл уже есть, оставить его как есть

	protected String baseDir;
	protected String dir;
	protected String fileName;

	protected ProjectConfig config;

	protected FileWriter out;
	protected StringBuffer sb = new StringBuffer();

	private static final Logger logger = Logger.getLogger(SqlFileWriter.class);

	public static final String SQL_FILE_ENCODING = "UTF8";
	public static final String SQL_FILE_EXTENSION = ".sql";

	public static final int DEFAULT_GENERATOR_VALUE = 0;
	public static final String SQL_COMMENT_PREFIX = "-- ";
	public static final String TABLE_NAME_SEPARATOR = "_";
	public static final String GENERATOR_NAME_POSTFIX = "_gen";
}
/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 23.03.2012 12:30:35$
*/
package su.opencode.kefir.gen.fileWriter.css;

import org.apache.log4j.Logger;
import su.opencode.kefir.gen.fileWriter.FileWriter;

import java.io.File;
import java.io.IOException;

import static su.opencode.kefir.util.FileUtils.FILE_SEPARATOR;
import static su.opencode.kefir.util.FileUtils.createDirs;
import static su.opencode.kefir.util.StringUtils.concat;

public abstract class CssFileWriter
{
	public CssFileWriter(String baseDir, String dir, String fileName) {
		this.baseDir = baseDir;
		this.dir = dir;
		this.fileName = fileName;
	}

	public void createFile() throws IOException {
		File file = createCssFile();
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

		out = new FileWriter(file, CSS_FILE_ENCODING);

		try
		{
			writeFile();
		}
		finally
		{
			out.close();
		}
	}

	private File createCssFile() {
		String dirPath = concat(sb, baseDir, FILE_SEPARATOR, dir);
		createDirs(dirPath); // если директорий нет, создать их

		String filePath;
		if (fileName.endsWith(CSS_FILE_EXTENSION)) // если имя файла уже содержит ".css", не добавлять его
			filePath = concat(sb, dirPath, FILE_SEPARATOR, fileName);
		else
			filePath = concat(sb, dirPath, FILE_SEPARATOR, fileName, CSS_FILE_EXTENSION); // добавить ".css" к имени файла

		return new File(filePath);
	}

	protected void writeComment(String comment) throws IOException {
		out.writeLn("/* ", comment, " */");
	}

	protected abstract void writeFile() throws IOException;

	protected boolean failIfFileExists = false;

	protected String baseDir;
	protected String dir;
	protected String fileName;

	protected FileWriter out;
	protected StringBuffer sb = new StringBuffer();

	private static final Logger logger = Logger.getLogger(CssFileWriter.class);

	public static final String CSS_FILE_ENCODING = "UTF8";
	public static final String CSS_FILE_EXTENSION = ".css";
}
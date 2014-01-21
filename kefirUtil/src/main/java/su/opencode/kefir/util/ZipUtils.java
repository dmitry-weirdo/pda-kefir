/**
 Copyright 2013 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.util;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import static su.opencode.kefir.util.StringUtils.concat;

public class ZipUtils
{
	public static void main(String[] args) throws IOException {
		StringBuffer sb = new StringBuffer();
		if (args == null || args.length != ARGUMENTS_COUNT)
		{
			System.out.println( concat(sb, "Usage: ", ZipUtils.class.getName(), " <dir_to_zip> <zip_file_absolute_name> <directory_to_unzip_absolute_path>") );
			return;
		}

		BasicConfigurator.configure();

		String srcDirPath = args[0];
		logger.info( concat(sb, "Directory to zip: ", srcDirPath) );

		String zipFileName = args[1];
		logger.info( concat(sb, "Zip file full name: ", zipFileName) );

		String unzippedDirPath = args[2];
		logger.info( concat(sb, "Directory to unzip: ", unzippedDirPath) );

		zip(srcDirPath, zipFileName);
		unzip(zipFileName, unzippedDirPath);
	}

	public static void zip(File dir, String filePath) {
		zip( dir.getPath(), filePath);
	}

	public static void zip(String dirPath, String filePath) {
		try
			(FileOutputStream zipFileOut = new FileOutputStream(filePath); ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(zipFileOut), Charset.forName(ZIP_FILE_ENCODING)))
		{
			zipDir(dirPath, dirPath, out);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	private static void zipDir(String rootDirPath, String dirPath, ZipOutputStream zos) throws IOException {
		StringBuffer sb = new StringBuffer();
		logger.info("\n");
		logger.info( concat(sb, "zipping dir: " + dirPath) );

		File zipDir = new File(dirPath);

		// empty directory -> zipDir.list() will not work
		if (zipDir.list().length == 0)
		{
			logger.info( concat(sb, "zipping empty directory: " + zipDir.getPath()) );
			String zipDirName = concat(zipDir.getPath().substring(rootDirPath.length() + FileUtils.FILE_SEPARATOR.length()), FileUtils.STRAIGHT_FILE_SEPARATOR); // нужно добавить "/" к имени директории, чтобы она сохранялась

			// replace back slashes with slashes, otherwise linux will see subdirectories as file names with slashes
			zipDirName = zipDirName.replace(FileUtils.FILE_SEPARATOR, FileUtils.STRAIGHT_FILE_SEPARATOR);

			logger.info( concat(sb, "empty dir zip name: ", zipDirName) );
			ZipEntry anEntry = new ZipEntry(zipDirName); // отрезаем корневую директорию, иначе пишется абсолютный путь файла (полная структура директорий)
			zos.putNextEntry(anEntry);

			return;
		}

		byte[] readBuffer = new byte[BUFFER_SIZE];
		int bytesIn;

		// loop through dirList, and zip the files
		for (File file : zipDir.listFiles())
		{
			if (file.isDirectory())
			{
				// if the File object is a directory, call this
				// function again to add its content recursively
				String filePath = file.getPath();
				zipDir(rootDirPath, filePath, zos);
				continue;
			}

			// if we reached here, the File object file was not a directory
			try
				(FileInputStream fis = new FileInputStream(file)) // create a FileInputStream on top of file
			{
				// отрезаем корневую директорию, иначе пишется абслютный путь файла (полная структура директорий)
				String fileAbsolutePath = file.getAbsolutePath();
				String zipEntryName = fileAbsolutePath.substring(rootDirPath.length());
				if (zipEntryName.startsWith(FileUtils.FILE_SEPARATOR))
					zipEntryName = zipEntryName.substring(FileUtils.FILE_SEPARATOR.length());

				// replace back slashes with slashes, otherwise linux will see subdirectories as file names with slashes
				zipEntryName = zipEntryName.replace(FileUtils.FILE_SEPARATOR, FileUtils.STRAIGHT_FILE_SEPARATOR);

				logger.info("\n");
				logger.info( concat(sb
					,  "zipping file:"
					, "\nfile.getPath(): ", file.getPath()
					, "\nfile.getName(): ", file.getName()
					, "\nzip entry name: ", zipEntryName
				) );

				ZipEntry zipEntry = new ZipEntry(zipEntryName);
				zos.putNextEntry(zipEntry);
				// now write the content of the file to the ZipOutputStream
				while ((bytesIn = fis.read(readBuffer)) != -1)
					zos.write(readBuffer, 0, bytesIn);

				zos.closeEntry();
			}
		}
	}

	public static List<String> unzip(String fileName, File dir) {
		return unzip(fileName, dir.getPath());
	}

	/**
	 * Разархивирует zip-файл с заданным именем в заданную директорию.
	 * @param fileName полное имя zip-файла.
	 * @param dirPath директория, в которую извлекается zip-файл.
	 * @return список имен разархивированных файлов.
	 * @throws RuntimeException ошибка извлечения.
	 */
	public static List<String> unzip(String fileName, String dirPath) {
		try
		{
			StringBuffer sb = new StringBuffer();

			logger.info( concat(sb, "\n\nunzipping file: ", fileName, "\nto path: ", dirPath) );

			FileUtils.createDir(dirPath); // create output dir if it did not exist

			ZipFile zipFile = new ZipFile(fileName, Charset.forName(ZIP_FILE_ENCODING));
			Enumeration entries = zipFile.entries();

			List<String> unzippedFileNames = new ArrayList<>();
			while (entries.hasMoreElements())
			{
				ZipEntry entry = (ZipEntry) entries.nextElement();

				if (entry.isDirectory() || entry.getName().endsWith(FileUtils.FILE_SEPARATOR) )
				{ // extract directory (assume directories are stored parents first then children)
					logger.info("\n");
					logger.info( concat(sb, "unzipping directory: ", entry.getName()) );

					String directoryPath = concat(dirPath, FileUtils.FILE_SEPARATOR, entry.getName());
					logger.info( concat(sb, "directory Path: ", directoryPath) );

					FileUtils.createDirs(directoryPath);
				}
				else
				{ // extract file
					logger.info("\n");
					logger.info( concat(sb, "unzipping file: ", entry.getName()) );

					String filePath = concat(dirPath, FileUtils.FILE_SEPARATOR, entry.getName());
					logger.info( concat(sb, "file path: ", filePath) );

					if ( !FileUtils.fileExists(filePath) )
					{
						File file = new File(filePath);

						// непустая директория может отсутствовать как отдельная сущность в zip-файле, поэтому создаем родителскую директорию извлекаемого файла
						String parentDirectoryPath = file.getParent();
						logger.info( concat(sb, "creating file's parent directory: ", parentDirectoryPath) );
						FileUtils.createDirs(parentDirectoryPath);

						logger.info( concat(sb, "creating file: ", filePath) );
						file.createNewFile();
					}

					copyInputStream(zipFile.getInputStream(entry), new BufferedOutputStream( new FileOutputStream(filePath) ) );
					unzippedFileNames.add(entry.getName());
				}
			}

			zipFile.close();

			return unzippedFileNames;
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	public static void copyInputStream(InputStream in, File outFile) throws IOException {
		copyInputStream( in, new FileOutputStream(outFile) );
	}
	public static void copyInputStream(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[BUFFER_SIZE];
		int len;

		while ((len = in.read(buffer)) >= 0)
			out.write(buffer, 0, len);

		in.close();
		out.close();
	}

	private static final String ZIP_FILE_ENCODING = "Cp866";
//	private static final String ZIP_FILE_ENCODING = "UTF-8";
//	private static final String ZIP_FILE_ENCODING = "Cp1251";

	private static final int BUFFER_SIZE = 1024;
	private static final Logger logger = Logger.getLogger(ZipUtils.class);

	public static final String ZIP_FILE_EXTENSION = ".zip";

	public static final int ARGUMENTS_COUNT = 3;
}
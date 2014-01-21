/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ rybakin
 $Revision$
 $Date: 17.09.2010 14:42:29$
*/
package su.opencode.kefir.util;

import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static su.opencode.kefir.util.StringUtils.concat;
import static su.opencode.kefir.util.StringUtils.empty;

public class FileUtils
{
	public static boolean isDirExists(String fileName) {
		return new File(fileName).isDirectory();
	}
	public static boolean fileExists(String fileName) {
		return fileExists(new File(fileName));
	}
	public static boolean fileExists(File f) {
		return f.exists() && !f.isDirectory();
	}
	public static boolean createDir(String dirName) {
		return createDir(new File(dirName));
	}
	private static boolean createDir(File dir) {
		return dir.exists() || dir.mkdir();
	}
	public static void createDirs(String dirPath) {
		if ( ! new File(dirPath).mkdirs() ) // если директорий нет, создать их
			log.info( concat("Cannot create dir \"", dirPath, "\"" ) );
	}
	public static boolean deleteDir(String dirName) {
		return deleteDir(new File(dirName));
	}
	public static boolean deleteDir(File dir) {
		if (!dir.exists())
			return true;

		for (File f : dir.listFiles())
		{
			if (f.isDirectory())
				deleteDir(f);
			else
				deleteFile(f);
		}

		return dir.delete();
	}
	public static boolean deleteFile(String fileName) {
		return deleteFile(new File(fileName));
	}
	public static boolean deleteFile(File f) {
		if (!f.exists())
			return true;

		if (f.delete())
		{
			log.debug( concat("File \"", f.getAbsolutePath(), "\" deleted successfully") );
			return true;
		}

		log.debug( concat("Cannot delete file \"", f.getAbsolutePath(), "\"") );
		return false;
	}
	public static void close(Closeable closeable) {
		try
		{
			if (closeable != null)
				closeable.close();
		}
		catch (IOException e)
		{
			throw new RuntimeException(e.getMessage());
		}
	}
	public static void writeLineToFile(String line, OutputStream os, String charset) throws IOException {
		if (line == null || line.isEmpty())
			return;

		os.write(line.getBytes(charset));
	}
	public static void deleteFilesByExtension(String dirPath, String extension) {
		final StringBuffer sb = new StringBuffer();
		final File dir = new File(dirPath);
		if (!dir.isDirectory())
			throw new RuntimeException( concat(sb, dirPath, " is not a directory") );

		for (File f : dir.listFiles())
		{
			if (!f.getName().endsWith(extension))
				continue;

			if (!f.delete())
				log.error( concat(sb, "Can't delete file ", f.getPath()) );
			else
				log.info( concat(sb, "Deleted file ", f.getPath()));
		}
	}
	public static String getPath(String folder, String fileName) {
		final int length = folder.length() - 1;
		if (folder.charAt(length) == '\\' || folder.charAt(length) == '/')
			return concat(folder, fileName);

		return concat(folder, FILE_SEPARATOR, fileName);
	}
	public static void strToFile(String fileName, String str, String charsetName) {
		OutputStream os = null;
		try
		{
			os = new FileOutputStream(fileName);
			os.write(charsetName == null || charsetName.isEmpty() ? str.getBytes() : str.getBytes(charsetName));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e.getMessage());
		}
		finally
		{
			close(os);
		}
	}
	/**
	 * Копирует файл из одной директории в другую.
	 * Если задан один и тот же путь (включая имя файла) — ничего не происходит.
	 * @param src Исходный путь к файлу
	 * @param dest Полный путь, в который копируется файл, <b>включая имя файла</b>
	 */
	public static void copyFile(String src, String dest) {
		if (src.equals(dest))
			return;

		final File f1 = new File(src);
		if (!f1.exists())
			throw new RuntimeException( concat("File '", src, "' does not exist") );

		final File f2 = new File(dest);
		try
		{
			final InputStream in = new FileInputStream(f1);
			final OutputStream out = new FileOutputStream(f2);

			final byte[] buf = new byte[BUF_SIZE];
			int len;
			while ((len = in.read(buf)) > 0)
			{
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
			log.debug( concat("File '", src, "' copied to '", dest, "' successfully") );
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * Копирует все непосредственно вложенные файлы (не директории) из одной директории в другую.
	 * @param srcDirPath директория, из которой выполняется копирование
	 * @param destDirPath директория, в которую выполняется копирование
	 */
	public static void copyAllFiles(String srcDirPath, String destDirPath) {
		File srcDir = new File(srcDirPath);
		if (!srcDir.isDirectory())
			throw new IllegalArgumentException(concat("\"", srcDirPath, "\" is not a directory"));

		File destDir = new File(destDirPath);
		if (!destDir.isDirectory())
			throw new IllegalArgumentException(concat("\"", srcDirPath, "\" is not a directory"));

		for (File file : srcDir.listFiles())
			if (!file.isDirectory())
				copyFile(file.getAbsolutePath(), concat(destDirPath, FILE_SEPARATOR, file.getName()));
	}

	public static void execute(String cmd) {
		log.debug(cmd);
		try
		{
			final Process p = Runtime.getRuntime().exec(cmd);

			final ProcessHandler error = new ProcessHandler(p.getErrorStream(), ProcessHandler.ERR_TYPE);
			error.start();

			final ProcessHandler out = new ProcessHandler(p.getInputStream(), ProcessHandler.OUT_TYPE);
			out.start();

			p.waitFor();
			if (error.isError())
				throw new RuntimeException();
		}
		catch (IOException e)
		{
			throw new RuntimeException(e.getMessage());
		}
		catch (InterruptedException e)
		{
			throw new RuntimeException(e.getMessage());
		}

		log.debug( concat("Command '", cmd, "' executed successfully") );
	}
	public static File getFileIfExist(String fileName) {
		final File file = new File(fileName);
		if (!file.exists())
			throw new RuntimeException( concat("File '", fileName, "' does not exist") );

		return file;
	}
	public static void clearFile(String filename) throws IOException {
		new FileOutputStream(filename, false).close();
	}

	public static void writeToFile(String filename, String str) throws IOException {
		FileOutputStream out = null;
		try
		{
			out = new FileOutputStream(filename, true);
			out.write(str.getBytes());
			out.write("\n".getBytes());
		}
		finally
		{
			close(out);
		}
	}
	public static void writeToFile(String fileName, String strToWrite, String encoding) throws IOException {
		OutputStreamWriter out = null;
		FileOutputStream fos = null;

		try
		{
			fos = new FileOutputStream(fileName, true);
			out = new OutputStreamWriter(fos, encoding);
			out.write(strToWrite);
		}
		finally
		{
//			close(fos); // closing both fails with "Stream Closed"
			close(out);
		}
	}

	public static List<String> getQueries(String filename) throws IOException {
		List<String> queries = new ArrayList<String>();

		BufferedReader br = null;
		try
		{
			br = createBufferedReader(new File(filename));

			String str;
			while ((str = br.readLine()) != null)
				queries.add(str);
		}
		finally
		{
			close(br);
		}

		return queries;
	}

	public static BufferedReader createBufferedReader(File file) throws IOException {
		return new BufferedReader(new InputStreamReader(new FileInputStream(file)));
	}

	public static byte[] readFile(String filePath) {
		File file = new File(filePath);
		int fileSize = ((Long) file.length()).intValue();
		byte[] result = new byte[fileSize];

		FileInputStream fis = null;
		try
		{
			fis = new FileInputStream(filePath);
			fis.read(result);
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			close(fis);
		}

		return result;
	}
	public static void writeToFile(String filePath, byte[] bytesToWrite) {
		FileOutputStream fos = null;

		try
		{
			fos = new FileOutputStream(filePath);
			fos.write(bytesToWrite);
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			close(fos);
		}
	}

	public static byte[] getFile(String filePath) {
		return getFile(getFileIfExist(filePath));
	}
	public static byte[] getFile(File file) {
		try
		{
			return getFile(new FileInputStream(file));
		}
		catch (FileNotFoundException e)
		{
			throw new RuntimeException(e);
		}
	}
	public static byte[] getFile(InputStream fis) {
		final byte[] buf = new byte[BUF_SIZE];
		try
		{
			final StringBuffer sb = new StringBuffer();
			int len;
			while ((len = fis.read(buf)) > 0)
				sb.append(new String(buf, 0, len));

			return sb.toString().getBytes();
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	public static File getAttachmentsDir(ServletContext context) {
		String dir = context.getInitParameter(ATTACHMENTS_DIR_PARAM_NAME);
//		if (dir == null)
//			throw new IllegalArgumentException( concat("Context parameter not specified: 'attachmentsDir'"));
		boolean useTempDir = empty(dir) || dir.equals(ATTACHMENTS_DIR_TEMPORARY_PARAM_VALUE);

		File file = useTempDir ? (File) context.getAttribute("javax.servlet.context.tempdir") : new File(dir);
		if (useTempDir)
			file = file.getParentFile().getParentFile().getParentFile().getParentFile();

		log.info( concat("attachments dir: ", file.getAbsolutePath()) );

		return file;
	}

	private static final Logger log = Logger.getLogger(FileUtils.class);

	public static final int BUF_SIZE = 1024 * 1024;
	public static final String FILE_SEPARATOR = System.getProperty("os.name").contains("Win") ? "\\" : "/";
	public static final String STRAIGHT_FILE_SEPARATOR = "/";
	public static final String TEMP_DIRECTORY = System.getProperty("java.io.tmpdir");
	public static final String EXTENSION_SEPARATOR = ".";

	public static final String CURRENT_DIR = ".";
	public static final String UPPER_DIR = "..";

	public static final String ATTACHMENTS_DIR_PARAM_NAME = "attachmentsDir";
	public static final String ATTACHMENTS_DIR_TEMPORARY_PARAM_VALUE = "#temporary";
}
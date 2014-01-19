/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.fileWriter;

import java.io.*;

public class FileWriter
{
	public FileWriter(File file, String encoding) throws FileNotFoundException, UnsupportedEncodingException {
		this.out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), encoding));
	}

	public void write(String str) throws IOException {
		out.write(str);
	}
	public void writeLine(String str) throws IOException {
		write(str);
		writeLine();
	}
	public void writeLine() throws IOException {
		write(NEW_LINE);
	}
	public void writeLn(String str) throws IOException { // shortcut for writeLine
		writeLine(str);
	}
	public void writeLn() throws IOException { // shortcut for writeLine
		writeLine();
	}
	public void write(String... strings) throws IOException {
		for (String str : strings)
			write(str);
	}
	public void writeLn(String... strings) throws IOException {
		write(strings);
		writeLn();
	}
	public void write(Object... objects) throws IOException {
		for (Object object : objects)
			write(object.toString());
	}
	public void writeLn(Object... objects) throws IOException {
		write(objects);
		writeLn();
	}
	public void close() throws IOException {
		out.close();
	}

	public static final String NEW_LINE = "\n";

	private Writer out;
}
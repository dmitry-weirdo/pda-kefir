/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.fileWriter;

import su.opencode.kefir.srv.SortConfig;

import javax.ejb.Local;
import java.io.IOException;
import java.util.List;

/**
 * Создает новый локальный интерфейс без методов. Интерфейс содержит маркеры вставки методов и импортов.
 */
public class LocalServiceFileWriter extends ClassFileWriter
{
	public LocalServiceFileWriter(String baseDir, String packageName, String className) {
		super(baseDir, packageName, className);
	}
	protected void writeImports() throws IOException {
		writeImport(SortConfig.class);
		out.writeLn();
		writeImport(Local.class);
		writeImport(List.class);
		out.writeLn();
		out.writeLn("//\t", APPEND_IMPORT_MARKER);
	}

	protected void writeClassBody() throws IOException {
		writeEmptyAnnotation("", Local.class);
		writeInterfaceHeader();
		out.writeLn("\t// ", APPEND_METHOD_DECLARATION_MARKER);
		writeClassFooter();
	}

	public static final String APPEND_IMPORT_MARKER = "${APPEND_IMPORT}";
	public static final String APPEND_METHOD_DECLARATION_MARKER = "${APPEND_METHOD_DECLARATION}";
}
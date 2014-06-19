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
import su.opencode.kefir.srv.SortConfig;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.util.List;

import static su.opencode.kefir.gen.ExtEntityUtils.*;
import static su.opencode.kefir.util.StringUtils.concat;

/**
 * Создает новую реализацию локального интерфейса без методов.
 * Класс интерфейс содержит маркеры вставки методов и импортов.
 */
public class LocalServiceBeanFileWriter extends ClassFileWriter
{
	public LocalServiceBeanFileWriter(String baseDir, String packageName, String className, String interfaceName) {
		super(baseDir, packageName, className);
		this.interfaceName = interfaceName;
	}
	protected void writeImports() throws IOException {
		writeImport(Logger.class);
		writeImport(SortConfig.class);
		out.writeLn();
		writeImport(Stateless.class);
		writeImport(EntityManager.class);
		writeImport(PersistenceContext.class);
		writeImport(List.class);
		out.writeLn();

		if ( !samePackage( getFullName(packageName, className), interfaceName) ) // если интерфейс находится в другом пакете, сделать его импорт
			writeImport(interfaceName);

		out.writeLn("// ", APPEND_IMPORT_MARKER);
	}

	protected void writeClassBody() throws IOException {
		writeEmptyAnnotation("", Stateless.class);
		writeImplementsClassHeader(getSimpleName(interfaceName));
		out.writeLn("\t// ", APPEND_METHOD_IMPLEMENTATION_MARKER);
		out.writeLn();

		out.writeLn("\t// ", APPEND_FIELD_MARKER);

		writeFieldDeclarationWithAnnotation(EM_FIELD_NAME, EntityManager.class, PersistenceContext.class);

		writeFieldDeclaration(SB_FIELD_NAME, StringBuffer.class, concat(sb, "new ", StringBuffer.class.getSimpleName(), "()"));
		out.writeLn();

		writeLoggerFieldDeclaration();
		writeClassFooter();
	}

	private String interfaceName;

	public static final String EM_FIELD_NAME = "em";
	public static final String SB_FIELD_NAME = "sb";
	public static final String ATTACHMENT_SERVICE_FIELD_NAME = "attachmentService";

	public static final String APPEND_IMPORT_MARKER = "${APPEND_IMPORT}";
	public static final String APPEND_METHOD_IMPLEMENTATION_MARKER = "${APPEND_METHOD_IMPLEMENTATION}";
	public static final String APPEND_FIELD_MARKER = "${APPEND_FIELD}";
}
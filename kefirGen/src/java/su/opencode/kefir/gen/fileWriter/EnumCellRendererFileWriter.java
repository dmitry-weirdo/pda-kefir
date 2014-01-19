/*
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 06.04.2012 16:35:48$
*/
package su.opencode.kefir.gen.fileWriter;

import su.opencode.kefir.gen.field.enumField.EnumField;
import su.opencode.kefir.gen.field.enumField.EnumFieldValue;
import su.opencode.kefir.srv.renderer.CellRenderer;
import su.opencode.kefir.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.Field;

import static su.opencode.kefir.gen.field.ExtEntityFieldsUtils.*;
import static su.opencode.kefir.gen.fileWriter.EntityFilterConfigFileWriter.CONCAT_METHOD_NAME;

public class EnumCellRendererFileWriter extends ClassFileWriter
{
	public EnumCellRendererFileWriter(String baseDir, EnumField enumField, Class enumClass) {
		super(baseDir, null, null);

		this.packageName = getEnumFieldRendererClassPackage(enumField, enumClass);
		this.className = getEnumFieldRendererClassSimpleName(enumField, enumClass);
		this.enumField = enumField;
		this.enumClass = enumClass;
	}

	@Override
	protected void writeImports() throws IOException {
		writeImport(CellRenderer.class);
		writeImport(enumClass);
		out.writeLn();
		writeStaticImport(StringUtils.class, CONCAT_METHOD_NAME);
	}
	@Override
	protected void writeClassBody() throws IOException {
		writeImplementsClassHeader(CellRenderer.class);
		writeRenderMethod();
		writeClassFooter();
	}
	private void writeRenderMethod() throws IOException {
		String paramName = "value";

		out.writeLn("\tpublic ", String.class.getSimpleName(), " ", RENDER_METHOD_NAME, "(", Object.class.getSimpleName(), " ", paramName, ") {");

		out.writeLn("\t\tif (", paramName, " == null)");
		out.writeLn("\t\t\treturn null;");
		out.writeLn();

		out.writeLn("\t\tif ( !(", paramName, " instanceof ", enumClass.getSimpleName(), ") )");
		out.writeLn(
			"\t\t\tthrow new ", ClassCastException.class.getSimpleName(),
			"( ",
				CONCAT_METHOD_NAME, "(", paramName, ".getClass().getName(), \" can't be cast to \", ", enumClass.getSimpleName(), ".class.getName())",
			" );"
		);
		out.writeLn();

		out.writeLn("\t\tswitch ( (", enumClass.getSimpleName(), ") ", paramName, " )");
		out.writeLn("\t\t{");

		for (Field field : enumClass.getDeclaredFields())
		{
			if (field.isEnumConstant() && hasEnumFieldValueAnnotation(field))
			{
				EnumFieldValue enumFieldValue = getEnumFieldValueAnnotation(field);
				out.writeLn("\t\t\tcase ", field.getName(), ": return \"", getEnumFieldValueRendererValue(enumFieldValue), "\";");
			}
		}

		out.writeLn(
			"\t\t\tdefault: throw new ", IllegalArgumentException.class.getSimpleName(), "( ",
				CONCAT_METHOD_NAME, "(\"Incorrect \", ", enumClass.getSimpleName(), ".class.getName(), \" value: \\\"\", ", paramName, ", \"\\\"\")",
			" );"
		);
		out.writeLn("\t\t}"); // end of switch

		out.writeLn("\t}");
	}

	private EnumField enumField;
	private Class enumClass;

	public static final String RENDER_METHOD_NAME = "render";
}
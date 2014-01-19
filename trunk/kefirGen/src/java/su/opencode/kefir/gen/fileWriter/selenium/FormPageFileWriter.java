/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.fileWriter.selenium;

import su.opencode.kefir.gen.ExtEntity;
import su.opencode.kefir.gen.ExtEntityUtils;
import su.opencode.kefir.gen.field.*;
import su.opencode.kefir.gen.fileWriter.ClassFileWriter;
import su.opencode.kefir.selenium.AbstractFormPage;
import su.opencode.kefir.selenium.AttachmentsElement;
import su.opencode.kefir.srv.json.JsonObject;
import su.opencode.kefir.srv.renderer.RenderersFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import static su.opencode.kefir.gen.ExtEntityUtils.*;
import static su.opencode.kefir.gen.fileWriter.selenium.AddressFormPageFileWriter.ADDRESS_FORM_SIMPLE_NAME;
import static su.opencode.kefir.util.ObjectUtils.returnGetMethod;
import static su.opencode.kefir.util.StringUtils.*;
import static su.opencode.kefir.util.StringUtils.concat;

public class FormPageFileWriter extends ClassFileWriter
{
	public FormPageFileWriter(String baseDir, ExtEntity extEntity, Class entityClass) {
		super(baseDir, getSeleniumPagesPackageName(entityClass), getSeleniumFormPageClassName(entityClass));

		this.extEntity = extEntity;
		this.entityClass = entityClass;
		addressFormPage = concat(sb, getSeleniumMainPagePackage(), ".", ADDRESS_FORM_SIMPLE_NAME);

		failIfFileExists = false;
		overwriteIfFileExists = true;
	}
	@Override
	protected void writeImports() throws IOException {
		for (String s : getImportsClasses())
			writeImport(s);
	}
	@Override
	protected void writeClassBody() throws IOException {
		final String className = entityClass.getSimpleName();

		out.writeLn("public class ", this.className, " extends ", ABSTRACT_FORM_PAGE.getSimpleName());
		out.writeLn("{");

		writeConstructor();

		writeIsValidMethod();
		writeIsFormEqual(className);
		writeFillForm(className);
		writePrivateMethods();

		writeFields();

		out.writeLn("}");
	}
	private void writeFillForm(String className) throws IOException {
		out.writeLn(TAB, "public ", this.className, " fillForm(", JSON_OBJECT.getSimpleName(), " jsonObject) {");
		out.writeLn(DOUBLE_TAB, "sleep();");
		out.writeLn(DOUBLE_TAB, "final ", className, " entity = (", className, ") jsonObject;");
		out.writeLn();

		boolean previousNewLine = false;
		final Field[] fields = entityClass.getDeclaredFields();
		final int length = fields.length;
		for (int i = 0; i < length; i++)
		{
			final Field f = fields[i];
			final String fName = f.getName();
			if (f.isAnnotationPresent(IdField.class) || f.getAnnotations().length == 0
				|| f.isAnnotationPresent(SqlColumn.class))
			{
				continue;
			}

			final Method getMethod = returnGetMethod(entityClass, f);
			if (getMethod == null)
				continue;

			final String methodName = getMethod.getName();
			if (f.isAnnotationPresent(NumberField.class))
			{
				if (!previousNewLine)
					out.writeLn();

				final String renderer = f.getAnnotation(NumberField.class).renderer();
				final boolean empty = renderer.isEmpty();
				final String renderMethod = empty ? "" : "renderer.getFloatCellRenderedValue(";
				out.writeLn(DOUBLE_TAB, "setValueById(", getFieldName(fName), ", ", renderMethod, "entity.", methodName, "()", empty ? "" : ")", ");");
				out.writeLn();
				previousNewLine = true;

				continue;
			}

			if (f.isAnnotationPresent(DateField.class))
			{
				if (!previousNewLine)
					out.writeLn();

				final String renderer = f.getAnnotation(DateField.class).renderer();
				final boolean empty = renderer.isEmpty();
				final String renderMethod = empty ? "" : "renderer.getDateCellRenderedValue(";
				out.writeLn(DOUBLE_TAB, "setDateValueById(", getFieldName(fName), ", ", renderMethod, "entity.", methodName, "()", empty ? "" : ")", ");");
				out.writeLn();
				previousNewLine = true;

				continue;
			}

			if (f.isAnnotationPresent(CheckboxField.class))
			{
				if (!previousNewLine)
					out.writeLn();

				out.writeLn(DOUBLE_TAB, "setCheckboxValueById(", getFieldName(fName), ", entity.", methodName, "());");
				out.writeLn();
				previousNewLine = true;

				continue;
			}

			if (f.isAnnotationPresent(LocalComboBoxField.class))
			{
				if (!f.getType().isEnum())
					throw new RuntimeException(concat(f.getName(), "Enum field required"));

				if (!previousNewLine)
					out.writeLn();

				out.writeLn(DOUBLE_TAB, "setLocalComboBoxValueById(", getFieldName(fName), ", getEnumValue(entity.", methodName, "()));");
				out.writeLn();
				previousNewLine = true;

				continue;
			}

			if (f.isAnnotationPresent(ComboBoxField.class))
			{
				if (!previousNewLine)
					out.writeLn();

				final Class fType = f.getType();
				final ComboBoxField annotation = f.getAnnotation(ComboBoxField.class);
				final String method = returnGetMethod(fType, annotation.queryParam()).getName();
				out.writeLn(DOUBLE_TAB, "final ", fType.getSimpleName(), " comboBoxEntity = entity.", methodName, "();");
				out.writeLn(DOUBLE_TAB, "setComboBoxValueById(", getFieldName(fName), ", comboBoxEntity == null ? null : comboBoxEntity.", method, "());");
				out.writeLn();
				previousNewLine = true;

				continue;
			}

			if (f.isAnnotationPresent(ChooseField.class))
			{
				if (!previousNewLine)
					out.writeLn();

				final Class fType = f.getType();
				final String fTypeSimpleName = fType.getSimpleName();
				out.writeLn(DOUBLE_TAB, "fill", fTypeSimpleName, "(entity.", methodName, "());");
				out.writeLn();
				previousNewLine = true;

				final String formPageClassName = getSeleniumFormPageClassName(fType);
				final String method = concat(sb, TAB, "private void fill", fTypeSimpleName, "(", fTypeSimpleName, " chooseEntity) {\n",
					DOUBLE_TAB, "if (chooseEntity == null)\n",
					TRIPLE_TAB, "return;\n\n",
					DOUBLE_TAB, "clickById(", getChooseButtonFieldName(fName), ");\n",
					DOUBLE_TAB, "new ", getSeleniumChooseListPageClassName(fType), "().chooseEntity(chooseEntity);\n",
					TAB, "}\n",
					TAB, "public ", formPageClassName, " show", fTypeSimpleName, "() {\n",
					DOUBLE_TAB, "clickById(", getShowButtonFieldName(fName), ");\n",
					DOUBLE_TAB, "return new ", formPageClassName, "(null);\n",
					TAB, "}\n");

				privateMethods.add(method);

				continue;
			}

			if (f.isAnnotationPresent(AttachmentsField.class))
			{
				if (!previousNewLine)
					out.writeLn();

				out.writeLn(DOUBLE_TAB, "uploadAttachments(", fName, ", entity.", methodName, "());");
				out.writeLn();
				previousNewLine = true;

				continue;
			}

			if (f.isAnnotationPresent(AddressField.class))
			{
				if (!previousNewLine)
					out.writeLn();

				out.writeLn(DOUBLE_TAB, "fillAddress(entity.", methodName, "(), ", getFieldName(fName), ", ", getTitleFieldName(fName), ");");
				out.writeLn();
				previousNewLine = true;

				final String method = concat(sb, TAB, "private void fillAddress(Address address, String addressFieldId, String addressWindowTitle) {\n",
					DOUBLE_TAB, "if (address == null)\n",
					DOUBLE_TAB, "\treturn;\n",
					"\n",
					DOUBLE_TAB, "clickById(addressFieldId);\n",
					DOUBLE_TAB, "new AddressFormPage(addressWindowTitle)\n",
					DOUBLE_TAB, "\t.fillForm(address)\n",
					DOUBLE_TAB, "\t.clickSaveButton();\n",
					"\t}\n");

				privateMethods.add(method);

				continue;
			}

			out.writeLn(DOUBLE_TAB, "setValueById(", getFieldName(fName), ", entity.", methodName, "());");
			previousNewLine = false;
		}

		if (!previousNewLine)
			out.writeLn();

		out.writeLn(DOUBLE_TAB, "return this;");
		out.writeLn(TAB, "}");
	}
	private void writePrivateMethods() throws IOException {
		for (String s : privateMethods)
			out.write(s);
	}
	private void writeIsFormEqual(String className) throws IOException {
		out.writeLn(TAB, "public boolean isFormEqual(JsonObject jsonObject) {");
		out.writeLn(DOUBLE_TAB, "final ", className, " entity = (", className, ") jsonObject;");
		out.writeLn();

		boolean isFirst = true;
		boolean previousNewLine = false;
		final StringBuffer lines = new StringBuffer();
		final Field[] fields = entityClass.getDeclaredFields();
		final int length = fields.length;
		for (int i = 0; i < length; i++)
		{
			final boolean isLast = i != length - 1;
			final Field f = fields[i];
			if (f.isAnnotationPresent(IdField.class) || f.getAnnotations().length == 0
				|| f.isAnnotationPresent(AddressField.class) || f.isAnnotationPresent(AttachmentsField.class)
				|| f.isAnnotationPresent(SqlColumn.class))
			{
				continue;
			}

			final String fName = f.getName();
			final Method getMethod = returnGetMethod(entityClass, f);
			if (getMethod == null)
				continue;

			final String methodName = getMethod.getName();
			final String isFirstLine = isFirst ? "\t\treturn " : "\n\t\t\t&& ";

			final Class fType = f.getType();
			final String fTypeSimpleName = fType.getSimpleName();
			if (f.isAnnotationPresent(ChooseField.class))
			{
				final String capitalize = capitalize(fName);
				if (!previousNewLine)
					append(lines, "\n");

				append(lines, isFirstLine, "get", capitalize, "Equals(entity)");
				if (isLast)
					append(lines, "\n");

				final StringBuffer sbb = new StringBuffer();
				boolean flag = true;
				final ChooseField annotation = f.getAnnotation(ChooseField.class);
				for (ChooseFieldTextField field : annotation.fields())
				{
					final String str = flag ? "\t\treturn " : "\n\t\t\t&& ";
					final String fieldName = field.name();
					append(sbb, str, "isEqual(", getChooseFieldName(fName, fieldName), ", ", fName,
						" == null ? \"\" : ", fName, ".", returnGetMethod(fType, fieldName).getName(), "())");

					if (flag)
						flag = false;
				}
				sbb.append(";\n");

				final String method = concat(sb, TAB, "private boolean get", capitalize, "Equals(", className, " entity) {\n",
					DOUBLE_TAB, "final ", fTypeSimpleName, " ", fName, " = entity.", methodName, "();\n",
					sbb,
					TAB, "}\n");

				privateMethods.add(method);
				previousNewLine = true;

				continue;
			}

			if (f.isAnnotationPresent(ComboBoxField.class))
			{
				final String capitalize = capitalize(fName);
				if (!previousNewLine)
					append(lines, "\n");

				append(lines, isFirstLine, "get", capitalize, "Equals(entity)");
				if (isLast)
					append(lines, "\n");

				final ComboBoxField annotation = f.getAnnotation(ComboBoxField.class);
				final String method = concat(sb, TAB, "private boolean get", capitalize, "Equals(", className, " entity) {\n",
					DOUBLE_TAB, "final ", fTypeSimpleName, " ", fName, " = entity.", methodName, "();\n",
					DOUBLE_TAB, "final String value = ", fName, " == null ? \"\" : ", fName, ".get", capitalize(annotation.queryParam()), "();\n\n",
					DOUBLE_TAB, "return isEqual(", getFieldName(fName), ", value);\n",
					TAB, "}\n");

				privateMethods.add(method);
				previousNewLine = true;

				continue;
			}

			if (f.isAnnotationPresent(LocalComboBoxField.class))
			{
				if (!fType.isEnum())
					throw new RuntimeException("Enum field required");

				if (!previousNewLine)
					append(lines, "\n");

				append(lines, isFirstLine, "isEqual(", getFieldName(fName), ", getEnumValue(entity.", methodName, "()))");
				if (isLast)
					append(lines, "\n");

				final LocalComboBoxField annotation = f.getAnnotation(LocalComboBoxField.class);
				final String method = concat(sb, TAB, "private String getEnumValue(", fTypeSimpleName, " anEnum) {\n",
					DOUBLE_TAB, "return renderer.getCellRenderedValue(\"", annotation.renderer(), "\", anEnum);\n",
					TAB, "}\n");

				privateMethods.add(method);
				previousNewLine = true;

				continue;
			}

			append(lines, isFirstLine, "isEqual(", getFieldName(fName), ", entity.", methodName, "())");
			previousNewLine = false;

			if (isFirst)
				isFirst = false;
		}

		appendLine(lines, ";");
		out.writeLn(lines.toString());

		out.writeLn(TAB, "}");
	}
	private void writeFields() throws IOException {
		final String className = ExtEntityUtils.getJsFieldPrefix(extEntity, entityClass);

		out.writeLn();
		out.writeLn(TAB, STRING_VARIABLE, "WINDOW_ID = \"", className, "FormWindow\";");
		out.writeLn();
		out.writeLn(TAB, STRING_VARIABLE, "SAVE_BUTTON_ID = \"", className, "-save\";");
		out.writeLn(TAB, STRING_VARIABLE, "CANCEL_BUTTON_ID = \"", className, "-cancel\";");
		out.writeLn(TAB, STRING_VARIABLE, "CLOSE_BUTTON_ID = \"", className, "-close\";");
		out.writeLn();

		boolean previousNewLine = false;
		for (Field f : entityClass.getDeclaredFields())
		{
			final String fName = f.getName();
			if (f.isAnnotationPresent(IdField.class) || f.getAnnotations().length == 0)
				continue;

			if (f.isAnnotationPresent(ChooseField.class))
			{
				if (!previousNewLine)
					out.writeLn();

				final ChooseField chooseFieldAnnotation = f.getAnnotation(ChooseField.class);

				final String buttonId = chooseFieldAnnotation.chooseButtonId();
				final String chooseButton = !buttonId.equals("") ? buttonId : concat(sb, className, "-", fName, "-choose");
				out.writeLn(TAB, STRING_VARIABLE, getChooseButtonFieldName(fName), " = \"", chooseButton, "\";");

				final String buttonId1 = chooseFieldAnnotation.showButtonId();
				final String showButton = !buttonId1.equals("") ? buttonId1 : concat(sb, className, "-", fName, "-show");
				out.writeLn(TAB, STRING_VARIABLE, getShowButtonFieldName(fName), " = \"", showButton, "\";");

				for (ChooseFieldTextField ff : chooseFieldAnnotation.fields())
				{
					final String fieldName = ff.name();
					final String chooseFieldNameId = getChooseFieldName(fName, fieldName);
					out.writeLn(TAB, STRING_VARIABLE, chooseFieldNameId, " = \"", className, "-", fName, "-", fieldName, "\";");
				}

				out.writeLn();
				previousNewLine = true;

				continue;
			}

			if (f.isAnnotationPresent(AttachmentsField.class))
			{
				if (!previousNewLine)
					out.writeLn();

				final String attachmentFieldName = getFieldName(fName);
				final String simpleName = ATTACHMENTS_ELEMENT.getSimpleName();
				out.writeLn(TAB, STRING_VARIABLE, attachmentFieldName, " = \"", className, "-", fName, "\";");
				out.writeLn(TAB, "private ", simpleName, " ", fName, " = new ", simpleName, "(", attachmentFieldName, ");");
				out.writeLn();
				previousNewLine = true;

				continue;
			}

			if (f.isAnnotationPresent(AddressField.class))
			{
				if (!previousNewLine)
					out.writeLn();

				final AddressField fAnnotation = f.getAnnotation(AddressField.class);
				final String buttonId = fAnnotation.updateButtonId();
				final String jsFieldName = !buttonId.equals("") ? buttonId : concat(sb, className, "-", fName, "-update");
				out.writeLn(TAB, STRING_VARIABLE, getFieldName(fName), " = \"", jsFieldName, "\";");
				out.writeLn(TAB, STRING_VARIABLE, getTitleFieldName(fName), " = \"", fAnnotation.addressWindowTitle(), "\";");
				out.writeLn();
				previousNewLine = true;

				continue;
			}

			previousNewLine = false;
			out.writeLn(TAB, STRING_VARIABLE, getFieldName(fName), " = \"", className, "-", fName, "\";");
		}

		if (isRenderPresent)
		{
			final String simpleName = RENDERERS_FACTORY.getSimpleName();
			out.writeLn();
			out.writeLn(TAB, "private static final ", simpleName, " renderer = new ", simpleName, "();");
		}
	}
	private String getTitleFieldName(String fName) {
		return concat(sb, fName.toUpperCase(), "_TITLE");
	}
	private String getFieldName(String fName) {
		return concat(sb, fName.toUpperCase(), "_ID");
	}
	private String getChooseFieldName(String fName, String name) {
		return concat(sb, fName.toUpperCase(), "_", name.toUpperCase(), "_FIELD_ID");
	}
	private String getChooseButtonFieldName(String fName) {
		return concat(sb, fName.toUpperCase(), "_CHOOSE_BUTTON_ID");
	}
	private String getShowButtonFieldName(String fName) {
		return concat(sb, fName.toUpperCase(), "_SHOW_BUTTON_ID");
	}
	private void writeIsValidMethod() throws IOException {
		out.writeLn(TAB, "public boolean isValid() {");
		out.writeLn(DOUBLE_TAB, "return isElementPresentById(WINDOW_ID);");
		out.writeLn(TAB, "}");
	}
	private void writeConstructor() throws IOException {
		out.writeLn(TAB, "public ", this.className, "(String title) {");
		out.writeLn(DOUBLE_TAB, "super(title);");
		out.writeLn();
		out.writeLn(DOUBLE_TAB, "windowId = WINDOW_ID;");
		out.writeLn();
		out.writeLn(DOUBLE_TAB, "saveButtonId = SAVE_BUTTON_ID;");
		out.writeLn(DOUBLE_TAB, "cancelButtonId = CANCEL_BUTTON_ID;");
		out.writeLn(DOUBLE_TAB, "closeButtonId = CLOSE_BUTTON_ID;");
		out.writeLn(TAB, "}");
		out.writeLn();
	}
	private TreeSet<String> getImportsClasses() {
		final TreeSet<String> importClassesSet = new TreeSet<>();

		importClassesSet.add(entityClass.getName());
		importClassesSet.add(ABSTRACT_FORM_PAGE.getName());
		importClassesSet.add(addressFormPage);
		importClassesSet.add(JSON_OBJECT.getName());
		for (Field f : entityClass.getDeclaredFields())
		{
			final Class fType = f.getType();
			final Class superclass = fType.getSuperclass();
			final String fTypeName = fType.getName();
			if (superclass != null && superclass.equals(JSON_OBJECT))
			{
				importClassesSet.add(fTypeName);
//				if (f.isAnnotationPresent(AddressField.class))
//					importClassesSet.add(ADDRESS_FORM_PAGE);

				continue;
			}

			if (f.isAnnotationPresent(AttachmentsField.class))
			{
				importClassesSet.add(ATTACHMENTS_ELEMENT.getName());
				continue;
			}

//			if (f.isAnnotationPresent(NumberField.class))
//			{
//				importIfNotEmpty(importClassesSet, f.getAnnotation(NumberField.class).renderer());
//				addRendererFactoryImport(importClassesSet);
//				continue;
//			}
//
//			if (f.isAnnotationPresent(TextField.class))
//			{
//				importIfNotEmpty(importClassesSet, f.getAnnotation(TextField.class).renderer());
//				addRendererFactoryImport(importClassesSet);
//				continue;
//			}
//
//			if (f.isAnnotationPresent(IntegerField.class))
//			{
//				importIfNotEmpty(importClassesSet, f.getAnnotation(IntegerField.class).renderer());
//				addRendererFactoryImport(importClassesSet);
//				continue;
//			}
//
//			if (f.isAnnotationPresent(SpinnerField.class))
//			{
//				importIfNotEmpty(importClassesSet, f.getAnnotation(SpinnerField.class).renderer());
//				addRendererFactoryImport(importClassesSet);
//				continue;
//			}
//
//			if (f.isAnnotationPresent(LocalComboBoxField.class))
//			{
//				importIfNotEmpty(importClassesSet, f.getAnnotation(LocalComboBoxField.class).renderer());
//				addRendererFactoryImport(importClassesSet);
//
//				if (fType.isEnum())
//					importClassesSet.add(fTypeName);
//				continue;
//			}
//
//			if (f.isAnnotationPresent(ComboBoxField.class))
//			{
//				importIfNotEmpty(importClassesSet, f.getAnnotation(ComboBoxField.class).renderer());
//				addRendererFactoryImport(importClassesSet);
//				continue;
//			}
//
//			if (f.isAnnotationPresent(DateField.class))
//			{
//				importIfNotEmpty(importClassesSet, f.getAnnotation(DateField.class).renderer());
//				addRendererFactoryImport(importClassesSet);
//				continue;
//			}

			addRendererFactoryImport(importClassesSet);

			if (fType.isEnum())
				importClassesSet.add(fTypeName);
		}

		return importClassesSet;
	}
	private void importIfNotEmpty(TreeSet<String> importClassesSet, String renderer) {
		if (!renderer.isEmpty())
			importClassesSet.add(renderer);
	}
	private void addRendererFactoryImport(TreeSet<String> importClassesSet) {
		if (isRenderPresent)
			return;

		importClassesSet.add(RENDERERS_FACTORY.getName());
		isRenderPresent = true;
	}
	private final String addressFormPage;

	private static final String STRING_VARIABLE = "private static final String ";
	private static final Class<AbstractFormPage> ABSTRACT_FORM_PAGE = AbstractFormPage.class;
	private static final Class<AttachmentsElement> ATTACHMENTS_ELEMENT = AttachmentsElement.class;
	private static final Class<RenderersFactory> RENDERERS_FACTORY = RenderersFactory.class;
	private static final Class<JsonObject> JSON_OBJECT = JsonObject.class;

	private Set<String> privateMethods = new HashSet<>();
	private boolean isRenderPresent = false;
	private ExtEntity extEntity;
	private Class entityClass;
}
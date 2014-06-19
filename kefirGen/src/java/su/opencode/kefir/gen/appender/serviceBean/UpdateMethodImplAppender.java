/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.appender.serviceBean;

import su.opencode.kefir.gen.ExtEntity;
import su.opencode.kefir.util.EntityManagerUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static su.opencode.kefir.gen.ExtEntityUtils.*;
import static su.opencode.kefir.gen.field.ExtEntityFieldsUtils.getAddressFields;
import static su.opencode.kefir.gen.field.ExtEntityFieldsUtils.hasAddressFields;
import static su.opencode.kefir.gen.fileWriter.LocalServiceBeanFileWriter.EM_FIELD_NAME;
import static su.opencode.kefir.gen.project.address.AddressClassFileWriter.ADDRESS_CLASS_SIMPLE_NAME;
import static su.opencode.kefir.gen.project.address.AddressClassFileWriter.IS_EMPTY_METHOD_NAME;
import static su.opencode.kefir.util.EntityManagerUtils.MERGE_METHOD_NAME;
import static su.opencode.kefir.util.EntityManagerUtils.REMOVE_METHOD_NAME;
import static su.opencode.kefir.util.ObjectUtils.getGetterName;
import static su.opencode.kefir.util.ObjectUtils.getSetterName;
import static su.opencode.kefir.util.StringUtils.capitalize;
import static su.opencode.kefir.util.StringUtils.concat;

public class UpdateMethodImplAppender extends ServiceBeanMethodAppender
{
	public UpdateMethodImplAppender(String baseDir, ExtEntity extEntity, Class entityClass, Class addressClass) {
		super(baseDir, extEntity, entityClass);
		this.addressClass = addressClass;
	}

	protected void appendImports(List<String> fileLines, List<String> imports) {
		addImport( entityClass.getName(), fileLines, imports ); // если сущность не в том же пакете, что и реализация сервиса -> добавить импорт сущности

		if ( hasAddressFields(entityClass) )
		{
			// add address class import
			addImport(addressClass, fileLines, imports);

			addStaticImport(EntityManagerUtils.class, MERGE_METHOD_NAME, fileLines, imports);
			addStaticImport(EntityManagerUtils.class, REMOVE_METHOD_NAME, fileLines, imports);
		}
	}
	@Override
	protected List<String> getMethodSignatures() {
		List<String> signatures = new ArrayList<>();
		signatures.add(getSignature());
		return signatures;
	}

	protected void appendMethods(List<String> fileLines) {
		String className = entityClass.getSimpleName();
		String paramName = getSimpleName(entityClass);
		String existingEntityVarName = concat(sb, "existing", className);

		addMethodStart(fileLines, getSignature());

		fileLines.add( concat(sb,
			"\t\t", className, " ", existingEntityVarName, " = ", getGetMethodName(extEntity, entityClass), "(", ID_PARAM_NAME, ");"
		) );
		fileLines.add( concat(sb, "\t\t", "if (", existingEntityVarName, " == null)") );
		fileLines.add("\t\t\treturn;");
		appendEmptyLine(fileLines);

		if ( hasAddressFields(entityClass) )
			writeMergeAddressFields(fileLines, paramName, existingEntityVarName);

		fileLines.add( concat(sb, "\t\t", paramName, ".setId(", ID_PARAM_NAME, ");") );
		fileLines.add( concat(sb, "\t\t", EM_FIELD_NAME, ".merge(", paramName, ");") );

		addMethodEnd(fileLines);
	}
	private void writeMergeAddressFields(List<String> fileLines, String entityVarName, String existingEntityVarName) {
		for (Field field : getAddressFields(entityClass))
		{
			addComment( concat(sb, "merge ", field.getName()), fileLines );
			String existingAddressVarName = concat( sb, "existing", capitalize(field.getName()) );
			String addressVarName = field.getName();
			String getterName = getGetterName(field.getName());
			String setterName = getSetterName(field.getName());

			fileLines.add( concat(sb, "\t\t", ADDRESS_CLASS_SIMPLE_NAME, " ", existingAddressVarName, " = ", existingEntityVarName, ".", getterName, "();") ); // Address existingJuridicalAddress = existingDeveloper.getJuridicalAddress();
			fileLines.add( concat(sb, "\t\t", ADDRESS_CLASS_SIMPLE_NAME, " ", addressVarName, " = ", entityVarName, ".", getterName, "();") ); // Address juridicalAddress = developer.getJuridicalAddress();

			fileLines.add( concat(sb, "\t\tif (", addressVarName, " != null && ", addressVarName, ".", IS_EMPTY_METHOD_NAME, "())") ); // if (juridicalAddress != null && juridicalAddress.isEmpty())
			fileLines.add( concat(sb, "\t\t\t", addressVarName, " = null;")); // juridicalAddress = null;
			appendEmptyLine(fileLines);

			fileLines.add( concat(sb, "\t\tif (", addressVarName, " == null)") ); // if (juridicalAddress == null)
			fileLines.add("\t\t{");
			fileLines.add( concat(sb, "\t\t\t", existingEntityVarName, ".", setterName, "(null);") ); // existingDeveloper.setJuridicalAddress(null);
			fileLines.add( concat(sb, "\t\t\t", entityVarName, ".", setterName, "(null);") ); // developer.setJuridicalAddress(null);
			fileLines.add( concat(sb, "\t\t\t", REMOVE_METHOD_NAME, "(", EM_FIELD_NAME, ", ", existingAddressVarName, ");") ); // remove(em, existingJuridicalAddress);
			fileLines.add("\t\t}");
			fileLines.add("\t\telse");
			fileLines.add("\t\t{");
			fileLines.add( concat(sb, "\t\t\t", MERGE_METHOD_NAME, "(", EM_FIELD_NAME, ", ", existingAddressVarName, ", ", addressVarName, ");") );// merge(em, existingJuridicalAddress, juridicalAddress);
			fileLines.add("\t\t}");
			appendEmptyLine(fileLines);
		}
	}

	private String getSignature() {
		return concat(sb,
			"void ", getUpdateMethodName(extEntity, entityClass), "(", Integer.class.getSimpleName(), " ", ID_PARAM_NAME, ", ", entityClass.getSimpleName(), " ", getSimpleName(entityClass), ")"
		);
	}

	protected Class addressClass;
	private static final String ID_PARAM_NAME = "id";
}
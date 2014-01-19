/*
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 06.04.2012 11:50:05$
*/
package su.opencode.kefir.gen.project.xml.orm;

import su.opencode.kefir.gen.fileWriter.ClassField;
import su.opencode.kefir.gen.project.ProjectConfig;
import su.opencode.kefir.gen.project.address.AddressClassFileWriter;

import java.io.IOException;
import java.util.List;

import static su.opencode.kefir.gen.ExtEntityUtils.getFullName;
import static su.opencode.kefir.gen.project.ProjectConfigUtils.getAddressClassPackage;
import static su.opencode.kefir.gen.project.address.AddressClassFileWriter.ADDRESS_CLASS_SIMPLE_NAME;
import static su.opencode.kefir.gen.project.sql.appender.CreateAddressTableAppender.ADDRESS_SQL_GENERATOR_NAME;

public class AddressMappingAppender extends EntityMappingAppender
{
	public AddressMappingAppender(String filePath, ProjectConfig config) {
		super(filePath, config);
	}

	@Override
	protected void appendEntityMapping(List<String> fileLinesToAppend, List<String> allFileLines) throws IOException {
		startEntity(fileLinesToAppend, getFullName( getAddressClassPackage(config), ADDRESS_CLASS_SIMPLE_NAME));
		appendSequenceGenerator(fileLinesToAppend, ADDRESS_GENERATOR_NAME, ADDRESS_SQL_GENERATOR_NAME);

		startAttributes(fileLinesToAppend);
		appendId(fileLinesToAppend, ADDRESS_GENERATOR_NAME);

		for (ClassField classField : AddressClassFileWriter.getFields())
			appendBasic(fileLinesToAppend, classField.getName());

		endAttributes(fileLinesToAppend);

		endEntity(fileLinesToAppend);

		appendEmptyLine(fileLinesToAppend);
	}

	public static final String ADDRESS_GENERATOR_NAME = "addressGenerator";
}
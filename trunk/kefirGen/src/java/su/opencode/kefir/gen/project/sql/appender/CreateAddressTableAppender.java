/*
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 03.04.2012 15:51:05$
*/
package su.opencode.kefir.gen.project.sql.appender;

import su.opencode.kefir.gen.project.ProjectConfig;
import su.opencode.kefir.gen.project.sql.SqlTable;

import java.io.IOException;
import java.util.List;

import static su.opencode.kefir.gen.project.sql.SqlTableField.getIntegerField;
import static su.opencode.kefir.gen.project.sql.SqlTableField.getVarcharField;

public class CreateAddressTableAppender extends CreateTablesSqlAppender
{
	public CreateAddressTableAppender(String filePath, ProjectConfig config) {
		super(filePath, config);
	}

	@Override
	protected void appendCreateTable(List<String> fileLinesToAppend, List<String> allFileLines) throws IOException {
		SqlTable table = new SqlTable(ADDRESS_SQL_TABLE_NAME, "Address table (may be changed depending upon application needs)");
		table.addIdField();
		table.addField( getIntegerField("zip_code", false) );
		table.addField( getVarcharField("subject", 51, false) );
		table.addField( getVarcharField("district", 50, false) );
		table.addField( getVarcharField("city", 50, false) );
		table.addField( getVarcharField("city_district", 50, false) );
		table.addField( getVarcharField("locality", 50, false) );
		table.addField( getVarcharField("street", 50, false) );
		table.addField( getVarcharField("block", 255, false) ); // квартал
		table.addField( getVarcharField("house", 15, false) );
		table.addField( getVarcharField("building", 5, false) );
		table.addField( getVarcharField("apartment", 10, false) );

		table.setGeneratorName(ADDRESS_SQL_GENERATOR_NAME);
		table.writeCreateTable(fileLinesToAppend);
	}

	public static final String ADDRESS_SQL_TABLE_NAME = "Address";
	public static final String ADDRESS_SQL_GENERATOR_NAME = "address_gen";
}
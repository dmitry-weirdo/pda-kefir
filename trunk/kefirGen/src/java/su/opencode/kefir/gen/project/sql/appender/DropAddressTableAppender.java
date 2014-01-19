/*
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 03.04.2012 18:20:48$
*/
package su.opencode.kefir.gen.project.sql.appender;

import su.opencode.kefir.gen.project.ProjectConfig;

import java.io.IOException;
import java.util.List;

import static su.opencode.kefir.gen.project.sql.SqlFileWriter.getDropGeneratorStr;
import static su.opencode.kefir.gen.project.sql.SqlFileWriter.getDropTableStr;
import static su.opencode.kefir.gen.project.sql.appender.CreateAddressTableAppender.ADDRESS_SQL_GENERATOR_NAME;
import static su.opencode.kefir.gen.project.sql.appender.CreateAddressTableAppender.ADDRESS_SQL_TABLE_NAME;

public class DropAddressTableAppender extends DropTablesSqlAppender
{
	public DropAddressTableAppender(String filePath, ProjectConfig config) {
		super(filePath, config);
	}

	@Override
	protected void appendDropTable(List<String> fileLinesToAppend, List<String> allFileLines) throws IOException {
		fileLinesToAppend.add("");
		fileLinesToAppend.add( getDropTableStr(sb, ADDRESS_SQL_TABLE_NAME) );
		fileLinesToAppend.add( getDropGeneratorStr(sb, ADDRESS_SQL_GENERATOR_NAME) );
	}
}
/*
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 17.04.2012 16:48:30$
*/
package su.opencode.kefir.gen.project.sql.appender;

import su.opencode.kefir.gen.ExtEntity;

import java.io.IOException;
import java.util.List;

import static su.opencode.kefir.gen.ExtEntityUtils.getSqlSequenceName;
import static su.opencode.kefir.gen.ExtEntityUtils.getSqlTableName;
import static su.opencode.kefir.gen.project.sql.SqlFileWriter.getDropGeneratorStr;
import static su.opencode.kefir.gen.project.sql.SqlFileWriter.getDropTableStr;
import static su.opencode.kefir.util.StringUtils.concat;

public class DropExtEntityTableAppender extends DropTablesSqlAppender
{
	public DropExtEntityTableAppender(String fileName, ExtEntity extEntity, Class entityClass) {
		super(fileName, null);
		this.extEntity = extEntity;
		this.entityClass = entityClass;
	}

	@Override
	protected void appendDropTable(List<String> fileLinesToAppend, List<String> allFileLines) throws IOException {
		String tableName = getSqlTableName(extEntity, entityClass);
		String dropTableStr = getDropTableStr(sb, tableName);

		for (String line : allFileLines)
		{
			if (line.endsWith(dropTableStr))
			{
				logger.info( concat(sb, entityClass.getName(), " class drop table (table name: \"", tableName, "\") is already present in dropTables.sql."));
				return;
			}
		}

		fileLinesToAppend.add("");
		fileLinesToAppend.add( getDropTableStr(sb, tableName) );
		fileLinesToAppend.add( getDropGeneratorStr(sb, getSqlSequenceName(extEntity, entityClass)) );
	}

	private ExtEntity extEntity;
	private Class entityClass;
}
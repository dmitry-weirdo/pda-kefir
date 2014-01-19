/*
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 03.04.2012 13:34:26$
*/
package su.opencode.kefir.gen.project.sql;

import static su.opencode.kefir.gen.project.sql.SqlTableField.getName;
import static su.opencode.kefir.util.StringUtils.concat;

public class SqlTableForeignKey
{
	public SqlTableForeignKey(String fieldName, String tableName) {
		this.fieldName = fieldName;
		this.tableName = tableName;
		this.tableFieldName = SqlTablePrimaryKey.ID_FIELD_NAME;
	}
	public SqlTableForeignKey(String fieldName, String tableName, String tableFieldName) {
		this.fieldName = fieldName;
		this.tableName = tableName;
		this.tableFieldName = tableFieldName;
	}

	@Override
	public String toString() {
		return concat( "foreign key(", getName(fieldName), ") references ", tableName, "(", getName(tableFieldName), ")");
	}

	private String fieldName;
	private String tableName;
	private String tableFieldName;
}
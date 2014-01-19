/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 03.04.2012 13:13:46$
*/
package su.opencode.kefir.gen.project.sql;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SqlTablePrimaryKey
{
	public SqlTablePrimaryKey() {
		this.fields.add(ID_FIELD_NAME);
	}
	public SqlTablePrimaryKey(String field) {
		this.fields.add(field);
	}
	public SqlTablePrimaryKey(String[] fields) {
		this.fields.addAll(Arrays.asList(fields));
	}

	public void addField(String field) {
		fields.add(field);
	}

	public Set<String> getFields() {
		return fields;
	}
	public void setFields(Set<String> fields) {
		this.fields = fields;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("primary key (");

		for (String field : fields)
			sb.append(SqlTableField.getName(field)).append(FIELDS_SEPARATOR);

		sb.delete(sb.length() - FIELDS_SEPARATOR.length(), sb.length());

		sb.append(")");

		return sb.toString();
	}

	private Set<String> fields = new HashSet<String>();

	public static final String ID_FIELD_NAME = "id";
	public static final String FIELDS_SEPARATOR = ", ";
}
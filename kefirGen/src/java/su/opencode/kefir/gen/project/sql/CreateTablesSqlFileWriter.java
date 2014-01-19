/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 29.03.2012 16:06:23$
*/
package su.opencode.kefir.gen.project.sql;

import su.opencode.kefir.gen.project.ProjectConfig;
import su.opencode.kefir.srv.attachment.Attachment;
import su.opencode.kefir.srv.dynamicGrid.DynamicGrid;

import java.io.IOException;

import static su.opencode.kefir.gen.project.sql.SqlTableField.*;
import static su.opencode.kefir.gen.project.sql.appender.CreateTablesSqlAppender.APPEND_CREATE_TABLE_MARKER;
import static su.opencode.kefir.util.StringUtils.concat;

public class CreateTablesSqlFileWriter extends SqlFileWriter
{
	public CreateTablesSqlFileWriter(String baseDir, String dir, String fileName, ProjectConfig config) {
		super(baseDir, dir, fileName, config);
	}

	@Override
	protected void writeFile() throws IOException {
		writeComment(concat(sb, config.projectName, " create tables script (utf-8)"));
		out.writeLn();

		writeDynamicGridTable();
		writeAttachmentTable();

		// marker for address table appending
		writeComment(APPEND_CREATE_TABLE_MARKER);
		out.writeLn();

		writeCommit();
	}
	private void writeDynamicGridTable() throws IOException {
		SqlTable table = new SqlTable(DynamicGrid.class, "Width of columns, whether it's hidden or not and sort order for each user");
		table.addField( getIntegerField("order", true) );
		table.addField( getVarcharField("login", 32, true) );
		table.addField( getVarcharField("entity_name", 255, true) );
		table.addField( getVarcharField("column_name", 100, true) );
		table.addField( getIntegerField("width", true) );
		table.addField( getVarcharField("sort_dir", 4, false) );
		table.addField( getSmallintField("sort_order", false) );
		table.addField( getVarcharField("group_id", 255, false) );
		table.addField( getSmallintField("hidden", false) );

		table.addUnique(new SqlTableUnique(new String[]{ "login", "entity_name", "column_name"}));

		table.writeCreateTable(out);
	}

	private void writeAttachmentTable() throws IOException {
		SqlTable table = new SqlTable(Attachment.class, "File attachment");

		table.addIdField();

		table.addField( getVarcharField("entity_name", 255, true) );
		table.addField( getVarcharField("entity_field_name", 255, true) );
		table.addField( getIntegerField("entity_id", false) );

		table.addField( getVarcharField("file_name", 255, true) );
		table.addField( getIntegerField("file_size", true) );
		table.addField( getVarcharField("content_type", 255, true) );
		table.addField( getBlobField("data") );

		table.setGeneratorName(Attachment.class);
		table.writeCreateTable(out);
	}
}
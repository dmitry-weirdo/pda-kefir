/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 30.03.2012 15:28:33$
*/
package su.opencode.kefir.gen.project.sql;

import su.opencode.kefir.gen.project.ProjectConfig;
import su.opencode.kefir.srv.attachment.Attachment;
import su.opencode.kefir.srv.dynamicGrid.DynamicGrid;

import java.io.IOException;

import static su.opencode.kefir.gen.project.sql.appender.DropTablesSqlAppender.APPEND_DROP_TABLE_MARKER;
import static su.opencode.kefir.util.StringUtils.concat;

public class DropTablesSqlFileWriter extends SqlFileWriter
{
	public DropTablesSqlFileWriter(String baseDir, String dir, String fileName, ProjectConfig config) {
		super(baseDir, dir, fileName, config);
	}

	@Override
	protected void writeFile() throws IOException {
		writeComment(concat(sb, config.projectName, " drop tables script (utf-8)"));
		out.writeLn();

		// marker for drop table appending
		writeComment(APPEND_DROP_TABLE_MARKER);
		out.writeLn();

		writeDropAttachmentTable();
		out.writeLn();
		writeDropDynamicGridTable();
		out.writeLn();

		writeCommit();
	}
	private void writeDropAttachmentTable() throws IOException {
		writeDropTable(Attachment.class);
		writeDropGenerator(Attachment.class);
	}
	private void writeDropDynamicGridTable() throws IOException {
		writeDropTable(DynamicGrid.class);
	}
}
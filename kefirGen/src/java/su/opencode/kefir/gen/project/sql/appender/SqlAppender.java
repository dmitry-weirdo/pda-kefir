/*
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 03.04.2012 15:21:41$
*/
package su.opencode.kefir.gen.project.sql.appender;

import su.opencode.kefir.gen.appender.Appender;
import su.opencode.kefir.gen.project.ProjectConfig;

import static su.opencode.kefir.gen.project.sql.SqlFileWriter.SQL_FILE_ENCODING;

public abstract class SqlAppender extends Appender
{
	public SqlAppender(String filePath) {
		this.filePath = filePath;
	}
	protected SqlAppender(String filePath, ProjectConfig config) {
		this.filePath = filePath;
		this.config = config;
	}

	@Override
	protected String getEncoding() {
		return SQL_FILE_ENCODING;
	}

	protected String filePath;
	protected ProjectConfig config;
}
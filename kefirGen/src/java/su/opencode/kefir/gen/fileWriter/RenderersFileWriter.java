/*
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 10.04.2012 14:12:26$
*/
package su.opencode.kefir.gen.fileWriter;

import java.io.IOException;
import java.io.InputStream;

import static su.opencode.kefir.gen.appender.RenderersAppender.APPEND_RENDERER_CONSTANT_MARKER;
import static su.opencode.kefir.gen.project.ProjectConfigUtils.RENDERER_PROPERTIES_FILE_NAME;

public class RenderersFileWriter extends ClassFileWriter
{
	public RenderersFileWriter(String baseDir, String packageName, String className) {
		super(baseDir, packageName, className);
	}
	@Override
	protected void writeImports() throws IOException {
		writeImport(InputStream.class);
	}
	@Override
	protected void writeClassBody() throws IOException {
		writeClassHeader();

		writerGetRenderInputStream();
		out.writeLn();

		writeComment(APPEND_RENDERER_CONSTANT_MARKER);
		out.writeLn();

		writePublicStringConstant(RENDERER_PROPERTIES_FILE_NAME_CONSTANT_NAME, RENDERER_PROPERTIES_FILE_NAME);
		writeClassFooter();
	}
	private void writerGetRenderInputStream() throws IOException {
		out.writeLn("\tpublic static ", InputStream.class.getSimpleName(), " ", GET_RENDER_INPUT_STREAM_METHOD_NAME, "() {");
		out.writeLn("\t\treturn ", className, ".class.getClassLoader().getResourceAsStream(", RENDERER_PROPERTIES_FILE_NAME_CONSTANT_NAME, ");");
		out.writeLn("\t}");
	}

	public static final String GET_RENDER_INPUT_STREAM_METHOD_NAME = "getRenderInputStream";
	public static final String RENDERER_PROPERTIES_FILE_NAME_CONSTANT_NAME = "RENDERER_PROPERTIES_FILE_NAME";
}
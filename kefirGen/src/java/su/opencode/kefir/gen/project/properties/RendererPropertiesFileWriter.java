/*
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 10.04.2012 14:03:31$
*/
package su.opencode.kefir.gen.project.properties;

import su.opencode.kefir.gen.project.ProjectConfig;

import java.io.IOException;

import static su.opencode.kefir.gen.project.properties.RendererPropertiesAppender.APPEND_RENDERER_MARKER;
import static su.opencode.kefir.util.StringUtils.concat;

public class RendererPropertiesFileWriter extends PropertiesFileWriter
{
	public RendererPropertiesFileWriter(String fileName, ProjectConfig config) {
		super(fileName, config);
	}

	@Override
	protected void writeProperties() throws IOException {
		writeComment( concat(sb, " ", APPEND_RENDERER_MARKER) ); // write append marker
	}
}
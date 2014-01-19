/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.sampleSrv.render;

import java.io.InputStream;

public class Renderers
{
	public static InputStream getRenderInputStream() {
		return Renderers.class.getClassLoader().getResourceAsStream(RENDERER_PROPERTIES_FILE_NAME);
	}

	// ${APPEND_RENDERER_CONSTANT}
	public static final String TEST_ENUM_RENDERER = "Kefir.render.TestEnumRenderer";

	public static final String RENDERER_PROPERTIES_FILE_NAME = "renderer.properties";
}
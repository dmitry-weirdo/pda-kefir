/*
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 04.04.2012 16:35:15$
*/
package su.opencode.kefir.gen.replacer;

import su.opencode.kefir.gen.project.ProjectConfig;

public abstract class KefirReplacer extends Replacer
{
	protected KefirReplacer(String templateFilePath, String filePath) {
		super(templateFilePath, filePath);
	}
	protected KefirReplacer(String templateFilePath, String filePath, ProjectConfig config) {
		super(templateFilePath, filePath, config);
	}

	@Override
	protected String getMarkerStart() {
		return MARKER_START;
	}
	@Override
	protected String getMarkerEnd() {
		return MARKER_END;
	}
	@Override
	protected String getMarkerPattern() {
		return MARKER_PATTERN;
	}

	public static final String MARKER_START = "${";
	public static final String MARKER_END = "}";

	public static final String MARKER_PATTERN = "\\$\\{.+?\\}"; // ? is for ungreedy matching
}
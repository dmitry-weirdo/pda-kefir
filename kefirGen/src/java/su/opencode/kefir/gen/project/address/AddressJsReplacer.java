/*
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 06.04.2012 13:15:07$
*/
package su.opencode.kefir.gen.project.address;

import su.opencode.kefir.gen.project.ProjectConfig;
import su.opencode.kefir.gen.replacer.KefirReplacer;

import static su.opencode.kefir.gen.fileWriter.js.JsFileWriter.JS_FILE_ENCODING;
import static su.opencode.kefir.gen.project.ProjectConfigUtils.getAddressFullFormJsNamespace;
import static su.opencode.kefir.gen.project.ProjectConfigUtils.getAddressJsNamespace;

public class AddressJsReplacer extends KefirReplacer
{
	public AddressJsReplacer(String templateFilePath, String filePath, ProjectConfig config) {
		super(templateFilePath, filePath, config);
	}

	@Override
	protected String getEncoding() {
		return JS_FILE_ENCODING;
	}

	@Override
	protected void fillValues() {
		values.put(JS_NAMESPACE_KEY, getAddressJsNamespace(config));
		values.put(FORM_JS_NAMESPACE_KEY, getAddressFullFormJsNamespace(config));
	}

	public static final String JS_NAMESPACE_KEY = "JS_NAMESPACE";
	public static final String FORM_JS_NAMESPACE_KEY = "FORM_JS_NAMESPACE";
}
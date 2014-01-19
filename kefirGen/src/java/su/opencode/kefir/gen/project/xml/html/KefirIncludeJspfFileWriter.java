/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 26.03.2012 14:50:50$
*/
package su.opencode.kefir.gen.project.xml.html;

import org.xml.sax.SAXException;
import su.opencode.kefir.gen.ExtEntityUtils;
import su.opencode.kefir.gen.project.ProjectConfig;

import java.io.IOException;

import static su.opencode.kefir.gen.fileWriter.js.JsFileWriter.EXT_BLANK_IMAGE_URL_CONSTANT_NAME;
import static su.opencode.kefir.gen.project.ProjectConfigUtils.getExtContextPath;
import static su.opencode.kefir.gen.project.ProjectConfigUtils.getKefirStaticContextPath;
import static su.opencode.kefir.util.StringUtils.concat;

public class KefirIncludeJspfFileWriter extends HtmlFileWriter
{
	public KefirIncludeJspfFileWriter(String fileName, ProjectConfig config) {
		super(fileName, config);
		extContextPath = getExtContextPath(config);
		kefirStaticContextPath = getKefirStaticContextPath(config);
	}

	@Override
	protected void writeContent() throws IOException, SAXException {
		writeExtJsInclude();
		writer.writeLn();
		writer.writeLn();
		writeSpinnerFieldInclude();
		writer.writeLn();
		writer.writeLn();
		writeColumnHeaderGroupInclude();
		writer.writeLn();
		writer.writeLn();
		writeCheckColumnInclude();
		writer.writeLn();
		writer.writeLn();
		writeMultipleSortingInclude();
		writer.writeLn();
		writer.writeLn();
		writeInputTextMaskInclude();
		writer.writeLn();
		writer.writeLn();
		writeGridSummaryInclude();
		writer.writeLn();
		writer.writeLn();
		writeToolbarStyleInclude();
		writer.writeLn();
		writer.writeLn();
		writeLiveGridInclude();
		writer.writeLn();
		writer.writeLn();
		writeFileUploadInclude();
		writer.writeLn();
		writer.writeLn();
		writeKefirDefaultStylesInclude();
		writer.writeLn();
		writer.writeLn();
		writeKefirCommonScriptsInclude();
		writer.writeLn();

		writeExtBlankImageUrlScript();
	}
	private void writeExtJsInclude() throws SAXException {
		writer.writeComment("Extjs", true);
		writeCssInclude( getExtHref("resources/css/ext-all.css") );
		writeJsInclude( getExtHref("adapter/ext/ext-base.js") );
		writeJsInclude( getExtHref("ext-all-debug.js") ); 		// todo: include ext-all.js instead of ext-all-debug.js
		writeJsInclude( getExtHref("src/locale/ext-lang-ru.js") );
	}
	private void writeSpinnerFieldInclude() throws SAXException {
		writer.writeComment("Spinner field", true);
		writeCssInclude( getExtHref("examples/ux/css/Spinner.css") );
		writeJsInclude( getExtHref("examples/ux/Spinner.js") );
		writeJsInclude( getExtHref("examples/ux/SpinnerField.js") );
	}
	private void writeColumnHeaderGroupInclude() throws SAXException {
		writer.writeComment("Column header group", true);
		writeCssInclude( getExtHref("examples/ux/css/ColumnHeaderGroup.css") );
		writeJsInclude( getExtHref("examples/ux/ColumnHeaderGroup.js") );
	}
	private void writeCheckColumnInclude() throws SAXException {
		writer.writeComment("Check column", true);
		writeJsInclude( getExtHref("examples/ux/CheckColumn.js") );
	}
	private void writeMultipleSortingInclude() throws SAXException {
		writer.writeComment("Multiple sorting", true);
		writeCssInclude( getExtHref("examples/grid/multiple-sorting.css") );
		writeJsInclude( getExtHref("examples/ux/ToolbarDroppable.js") );
		writeJsInclude( getKefirStaticHref("plugins/Reorderer.js") );
		writeJsInclude( getKefirStaticHref("plugins/ToolbarReorderer.js") );
	}
	private void writeInputTextMaskInclude() throws SAXException {
		writer.writeComment("Input mask for text fields and search fields", true);
		writeJsInclude( getKefirStaticHref("plugins/InputTextMask.js") );
	}
	private void writeGridSummaryInclude() throws SAXException {
		writer.writeComment("Grid summary", true);
		writeJsInclude( getKefirStaticHref("gridSummary/gridSummary.js") ); // todo: think whether it's neeeded or only livegridSummary is ok
		writeJsInclude( getKefirStaticHref("gridSummary/liveGridSummary.js") );
	}
	private void writeToolbarStyleInclude() throws SAXException {
		writer.writeComment("Styles for normal buttons in toolbar", true);
		writeCssInclude( getKefirStaticHref("css/toolbar.css") );
	}
	private void writeLiveGridInclude() throws SAXException {
		writer.writeComment("LiveGrid", true);
		writeCssInclude( getKefirStaticHref("livegrid/resources/css/ext-ux-livegrid.css") );
		writeJsInclude( getKefirStaticHref("livegrid/livegrid-all-debug.js") );
		writeJsInclude( getKefirStaticHref("livegrid/src/locale/livegrid-lang-ru.js") );
		writeJsInclude( getKefirStaticHref("livegrid/src/CheckboxSelectionModel.js") );
	}
	private void writeFileUploadInclude() throws SAXException {
		writer.writeComment("File upload", true);

		writeCssInclude( getKefirStaticHref("fileupload/css/fileuploadfield.css") );
		writeCssInclude( getKefirStaticHref("fileupload/css/filetree.css") );
		writeCssInclude( getKefirStaticHref("fileupload/css/icons.css") );

		writeJsInclude( getKefirStaticHref("fileupload/FileUploadField.js") );
		writeJsInclude( getKefirStaticHref("fileupload/ExtendedUploadField.js") );
		writeJsInclude( getKefirStaticHref("fileupload/Ext.ux.FileUploader.js") );
		writeJsInclude( getKefirStaticHref("fileupload/Ext.ux.UploadPanel.js") );
		writeJsInclude( getKefirStaticHref("fileupload/MultiUploadPanel.js") );
	}
	private void writeKefirDefaultStylesInclude() throws SAXException {
		writer.writeComment("Kefir default styles", true);

		writeCssInclude( getKefirStaticHref("css/main.css") );
	}
	private void writeKefirCommonScriptsInclude() throws SAXException {
		writer.writeComment("Kefir common scripts", true);

		writeJsInclude( getKefirStaticHref("extUtils/common.js") );
		writeJsInclude( getKefirStaticHref("extUtils/searchField.js") );
		writeJsInclude( getKefirStaticHref("extUtils/stringUtils.js") );
		writeJsInclude( getKefirStaticHref("extUtils/common-dynamicLiveGrid.js") );
		writeJsInclude( getKefirStaticHref("extUtils/fieldsFactory.js") );
		writeJsInclude( getKefirStaticHref("extUtils/Kefir.form.MultiUploadPanel.js") );
		writeJsInclude( getKefirStaticHref("extUtils/Kefir.constant.js") );
		writeJsInclude( getKefirStaticHref("extUtils/Kefir.render.js") );
		writeJsInclude( getKefirStaticHref("extUtils/Kefir.vtype.js") );
		writeJsInclude( getKefirStaticHref("extUtils/Kefir.mainMenu.MainMenu.js") );
	}
	private void writeExtBlankImageUrlScript() throws SAXException {
		writer.clearAttributes();
		writer.addAttribute("type", JS_SCRIPT_TYPE);

		writer.startElement(SCRIPT_ELEMENT_NAME, true);
		writer.characters( concat(sb, EXT_BLANK_IMAGE_URL_CONSTANT_NAME, " = '", getExtHref("resources/images/default/s.gif"), "';") );
		writer.endElement(SCRIPT_ELEMENT_NAME);
	}

	private String getExtHref(String href) {
		return concat(sb, extContextPath, ExtEntityUtils.HTML_URL_SEPARATOR, href);
	}
	private String getKefirStaticHref(String href) {
		return concat(sb, kefirStaticContextPath, ExtEntityUtils.HTML_URL_SEPARATOR, href);
	}

	private String extContextPath;
	private String kefirStaticContextPath;
}
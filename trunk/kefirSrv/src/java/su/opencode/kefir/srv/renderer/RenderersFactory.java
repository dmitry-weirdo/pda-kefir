/**
 Copyright 2011 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.srv.renderer;

import su.opencode.kefir.util.ObjectUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import static su.opencode.kefir.util.FileUtils.close;
import static su.opencode.kefir.util.StringUtils.concat;

public class RenderersFactory
{

	public RenderersFactory() {
		loadDefaultProperties(DEFAULT_RENDERER_PROPERTIES);
	}

	public void loadRenderProperties(InputStream in) {
		if (!loadRenderProperties && in != null)
		{
			loadRenderProperties = true;
			loadPropertiesAndCloseStream(in);
		}
	}

	private void loadDefaultProperties(String propertiesFileName) {
		if (propertiesFileName == null)
			return;

		try
		{
			final InputStream in = this.getClass().getClassLoader().getResourceAsStream(propertiesFileName);
			loadPropertiesAndCloseStream(in);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	private void loadPropertiesAndCloseStream(InputStream in) {
		try
		{
			properties.load(in);
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			close(in);
		}
	}

	public String getFloatCellRenderedValue(Number value) {
		return getCellRenderedValue(FLOAT_RENDERER, value);
	}

	public String getDateCellRenderedValue(Date value) {
		return getCellRenderedValue(DATE_RENDERER, value);
	}

	public String getCellRenderedValue(String jsRendererName, Object value) {
		return getCellRenderer(jsRendererName).render(value);
	}

	private CellRenderer getCellRenderer(String jsRendererName) {
		final String javaPropertyClassName = properties.getProperty(jsRendererName);
		if (javaPropertyClassName == null)
			return new StringCellRenderer();

		final Object o = ObjectUtils.getNewInstance(javaPropertyClassName);

		if (o instanceof CellRenderer)
			return (CellRenderer) o;

		throw new RuntimeException(concat(javaPropertyClassName, " can't be cast to CellRenderer"));
	}

	public static String getRendererConstantName(String value) {
		switch (value)
		{
			case FLOAT_RENDERER: return FLOAT_RENDERER_CONSTANT_NAME;
			case DATE_RENDERER: return DATE_RENDERER_CONSTANT_NAME;
			case DONT_SHOW_NIL_RENDERER: return DONT_SHOW_NIL_RENDERER_CONSTANT_NAME;
			case DONT_SHOW_NIL_FLOAT_RENDERER: return DONT_SHOW_NIL_FLOAT_RENDERER_CONSTANT_NAME;
			case STRING_RENDERER: return STRING_RENDERER_CONSTANT_NAME;
			case BOOLEAN_RENDERER: return BOOLEAN_RENDERER_CONSTANT_NAME;
			case LOGIN_RENDERER: return LOGIN_RENDERER_CONSTANT_NAME;
			case SEX_RENDERER: return SEX_RENDERER_CONSTANT_NAME;

			default: return concat("\"", value, "\"");
		}
	}

	private static final String DEFAULT_RENDERER_PROPERTIES = "su/opencode/kefir/srv/renderer/defaultRenderer.properties"; // in _kefirSrv.jar defaulRenderer.properties is in the classes root
	private static final Properties properties = new Properties();

	public static final String DONT_SHOW_NIL_RENDERER = "Kefir.render.dontShowNilRender";
	public static final String DONT_SHOW_NIL_FLOAT_RENDERER = "Kefir.render.dontShowNilFloatRender";
	public static final String STRING_RENDERER = "Kefir.render.stringRenderer";
	public static final String DATE_RENDERER = "Kefir.render.dateRenderer";
	public static final String FLOAT_RENDERER = "Kefir.render.floatRenderer";
	public static final String BOOLEAN_RENDERER = "Kefir.render.booleanRenderer";
	public static final String LOGIN_RENDERER = "Kefir.render.loginRenderer";
	public static final String SEX_RENDERER = "Kefir.render.sexRenderer";

	public static final String FLOAT_RENDERER_CONSTANT_NAME = "FLOAT_RENDERER";
	public static final String DATE_RENDERER_CONSTANT_NAME = "DATE_RENDERER";
	public static final String DONT_SHOW_NIL_RENDERER_CONSTANT_NAME = "DONT_SHOW_NIL_RENDERER";
	public static final String DONT_SHOW_NIL_FLOAT_RENDERER_CONSTANT_NAME = "DONT_SHOW_NIL_FLOAT_RENDERER";
	public static final String STRING_RENDERER_CONSTANT_NAME = "STRING_RENDERER";
	public static final String BOOLEAN_RENDERER_CONSTANT_NAME = "BOOLEAN_RENDERER";
	public static final String LOGIN_RENDERER_CONSTANT_NAME = "LOGIN_RENDERER";
	public static final String SEX_RENDERER_CONSTANT_NAME = "SEX_RENDERER";


	private boolean loadRenderProperties = false;
}
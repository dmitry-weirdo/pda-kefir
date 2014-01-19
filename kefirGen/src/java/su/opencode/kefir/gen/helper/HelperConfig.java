/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.helper;

import java.io.InputStream;

import static su.opencode.kefir.util.FileUtils.FILE_SEPARATOR;
import static su.opencode.kefir.util.StringUtils.cat;

/**
 * Parameter object ��� �������� � ������� {@linkplain su.opencode.kefir.gen.helper.GeneratorServiceHelper GeneratorServiceHelper}.
 */
public class HelperConfig
{
	public static HelperConfig create(String propertiesFileName) {
		return ObjectFiller.createObject(propertiesFileName, HelperConfig.class);
	}
	public static HelperConfig create(InputStream in) {
		return ObjectFiller.createObject(in, HelperConfig.class);
	}
	public void setPathPrefix(String path) {
		final StringBuffer sb = new StringBuffer();
		if (path.length() != path.lastIndexOf(FILE_SEPARATOR))
			path = cat(sb, path, FILE_SEPARATOR);

		srvSrcDir = cat(sb, path, srvSrcDir);
		ormXmlPath = cat(sb, path, ormXmlPath);
		createTablesSqlPath = cat(sb, path, createTablesSqlPath);
		dropTablesSqlPath = cat(sb, path, dropTablesSqlPath);
		webSrcDir = cat(sb, path, webSrcDir);
		webXmlPath = cat(sb, path, webXmlPath);
		jbossWebXmlPath = cat(sb, path, jbossWebXmlPath);
		jsDir = cat(sb, path, jsDir);
		jsIncludeFilePath = cat(sb, path, jsIncludeFilePath);
		mainMenuJsFilePath = cat(sb, path, mainMenuJsFilePath);
		viewConfigsJsPath = cat(sb, path, viewConfigsJsPath);
		mainCssPath = cat(sb, path, mainCssPath);
		constantsJsFilePath = cat(sb, path, constantsJsFilePath);
		localStoresJsFilePath = cat(sb, path, localStoresJsFilePath);
		renderersJsFilePath = cat(sb, path, renderersJsFilePath);
		rendererPropertiesFilePath = cat(sb, path, rendererPropertiesFilePath);
		renderersClassFilePath = cat(sb, path, renderersClassFilePath);
		seleniumSrcDir = cat(sb, path, seleniumSrcDir);
		seleniumTestDir = cat(sb, path, seleniumTestDir);
		seleniumTestDataDir = cat(sb, path, seleniumTestDataDir);
	}

	/**
	 * ����� ��������. ������ ����� ��������� {@linkplain su.opencode.kefir.gen.ExtEntity ExtEntity}.
	 */
	public Class entityClass;

	/**
	 * ����� ������. ��������� ��� ��������� ������ update ��� ��������, ������� ���� ������.
	 */
	public Class addressClass;

	/**
	 * ����� ����������. ������������ ��� ��������� inputStream �� ����� renderers.properties ����������.
	 */
	public Class renderersClass;

	/**
	 * ������ ��� ������ �������. ������������ ��� {@linkplain GeneratorServiceHelper#generateLocalInterface(HelperConfig) ��������� ���������� �������}.
	 */
	public String serviceClassName;

	/**
	 * ���� � ���������� ��������� �����.
	 */
	public String srvSrcDir;

	/**
	 * ���� � ����� orm.xml.
	 */
	public String ormXmlPath;

	/**
	 * ���� � ����� createTables.sql.
	 */
	public String createTablesSqlPath;

	/**
	 * ���� � ����� dropTables.sql.
	 */
	public String dropTablesSqlPath;

	/**
	 * ���� � ���������� ���-�����.
	 */
	public String webSrcDir;

	/**
	 * ���� � ����� web.xml.
	 */
	public String webXmlPath;

	/**
	 * ���� � ����� jboss-web.xml.
	 */
	public String jbossWebXmlPath;

	/**
	 * ���� � ���������� js.
	 */
	public String jsDir;

	/**
	 * ���� � ����� � ��������� js-������ (application.jspf).
	 */
	public String jsIncludeFilePath;

	/**
	 * ������� ���� � js-������ � &lt;script src="..."/&gt;. ��� �������, ����� ".js".
	 */
	public String baseJsPath;

	/**
	 * ���� � js-����� � ������� ����, ���� ����� ����������� ������.
	 */
	public String mainMenuJsFilePath;

	/**
	 * ���� � ����� viewConfigs.js, � ������� ����� ����������� viewConfig.
	 */
	public String viewConfigsJsPath;

	/**
	 * ���� � ����� main.css, � ������� ����� ����������� ����� ��� viewConfig � �������.
	 */
	public String mainCssPath;

	// for enum

	/**
	 * ����� �����. ������ ���� ������ � ����� ��������� {@linkplain su.opencode.kefir.gen.field.enumField.EnumField EnumField}
	 */
	public Class enumClass;

	/**
	 * ���� � �����, ���� ����� ��������� js-���������.
	 */
	public String constantsJsFilePath;

	/**
	 * ���� � �����, ���� ����� ����������� ��������� ��������� (Ext.data.ArrayStore).
	 */
	public String localStoresJsFilePath;

	/**
	 * ���� � �����, ���� ����� ����������� ���������.
	 */
	public String renderersJsFilePath;

	/**
	 * ���� � ��������-�����, ���� ����� ����������� ������������ CellRenderer-�� � jsRendererod ��� ������.
	 */
	public String rendererPropertiesFilePath;

	/**
	 * ���� � ����� Renderers.java, � ������� ����� ����������� ��������� ��� js-����������.
	 */
	public String renderersClassFilePath;

	/**
	 * ���� � ���������� selenium �������.
	 */
	public String seleniumSrcDir;

	/**
	 * ���� � ������ selenium �������, ���� ����� ����������� TestCase � DataProvider.
	 */
	public String seleniumTestDir;

	/**
	 * ���� � �������� ������, ������� ����� ��������� � ����� selenium �������.
	 */
	public String seleniumTestDataDir;

	/**
	 * ����� ��� MainPage
	 */
	public String seleniumMainPagePackage;
}
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
 * Parameter object для передачи в функции {@linkplain su.opencode.kefir.gen.helper.GeneratorServiceHelper GeneratorServiceHelper}.
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
	 * Класс сущности. Должен иметь аннотацию {@linkplain su.opencode.kefir.gen.ExtEntity ExtEntity}.
	 */
	public Class entityClass;

	/**
	 * Класс адреса. Необходим для генерации метода update для сущности, имеющей поля адреса.
	 */
	public Class addressClass;

	/**
	 * Класс рендереров. Используется для получения inputStream от файла renderers.properties приложения.
	 */
	public Class renderersClass;

	/**
	 * Полное имя класса сервиса. Используется при {@linkplain GeneratorServiceHelper#generateLocalInterface(HelperConfig) генерации локального сервиса}.
	 */
	public String serviceClassName;

	/**
	 * Путь к исходникам серверной части.
	 */
	public String srvSrcDir;

	/**
	 * Путь к файлу orm.xml.
	 */
	public String ormXmlPath;

	/**
	 * Путь к файлу createTables.sql.
	 */
	public String createTablesSqlPath;

	/**
	 * Путь к файлу dropTables.sql.
	 */
	public String dropTablesSqlPath;

	/**
	 * Путь к исходникам веб-части.
	 */
	public String webSrcDir;

	/**
	 * Путь к файлу web.xml.
	 */
	public String webXmlPath;

	/**
	 * Путь к файлу jboss-web.xml.
	 */
	public String jbossWebXmlPath;

	/**
	 * Путь к исходникам js.
	 */
	public String jsDir;

	/**
	 * Путь к файлу с инклюдами js-файлов (application.jspf).
	 */
	public String jsIncludeFilePath;

	/**
	 * Базовый путь к js-файлам в &lt;script src="..."/&gt;. Как правило, равен ".js".
	 */
	public String baseJsPath;

	/**
	 * Путь к js-файлу с главным меню, куда будут добалвяться кнопки.
	 */
	public String mainMenuJsFilePath;

	/**
	 * Путь к файлу viewConfigs.js, в который будут добавляться viewConfig.
	 */
	public String viewConfigsJsPath;

	/**
	 * Путь к файлу main.css, в который будут добавляться стили для viewConfig и легенды.
	 */
	public String mainCssPath;

	// for enum

	/**
	 * Класс энума. Должен быть энумом и иметь аннотацию {@linkplain su.opencode.kefir.gen.field.enumField.EnumField EnumField}
	 */
	public Class enumClass;

	/**
	 * Путь к файлу, куда будут добавлять js-константы.
	 */
	public String constantsJsFilePath;

	/**
	 * Путь к файлу, куда будут добавляться локальные хранилища (Ext.data.ArrayStore).
	 */
	public String localStoresJsFilePath;

	/**
	 * Путь к файлу, куда будут добавляться рендереры.
	 */
	public String renderersJsFilePath;

	/**
	 * Путь к проперти-файлу, куда будут добавляться соответствия CellRenderer-ов и jsRendererod для энумов.
	 */
	public String rendererPropertiesFilePath;

	/**
	 * Путь к файлу Renderers.java, в который будут добавляться константы для js-рендереров.
	 */
	public String renderersClassFilePath;

	/**
	 * Путь к исходникам selenium страниц.
	 */
	public String seleniumSrcDir;

	/**
	 * Путь к тестам selenium страниц, куда будут добавляться TestCase и DataProvider.
	 */
	public String seleniumTestDir;

	/**
	 * Путь к тестовым данным, которые будут вводиться в формы selenium страниц.
	 */
	public String seleniumTestDataDir;

	/**
	 * Пакет для MainPage
	 */
	public String seleniumMainPagePackage;
}
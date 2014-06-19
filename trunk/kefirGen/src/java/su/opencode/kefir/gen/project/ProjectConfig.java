/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 20.03.2012 18:55:56$
*/
package su.opencode.kefir.gen.project;

public class ProjectConfig
{
	/**
	 * Базовая директория, внутри которой создается проект.
	 */
	public String baseDir;

	/**
	 * Название проекта.
	 */
	public String projectName;

	/**
	 * Название серверного модуля проекта.
	 * По умолчанию — (название проекта + "Srv")
	 */
	public String srvModuleName;

	/**
	 * Название веб-модуля проекта.
	 * По умолчанию — (название проекта + "Web")
	 */
	public String webModuleName;

	/**
	 * Название Selenium-модуля проекта.
	 * По умолчанию — (название проекта + "Selenium")
	 */
	public String seleniumModuleName;

	/**
	 * Путь к директории, содержащей общие библиотеки для selenium-части приложения, относительно базовой директории selenium-части.
	 * По умолчанию — "lib"
	 */
	public String seleniumLibPath;

	/**
	 * Пакет для тестов
	 */
	public String seleniumBasePackage;

	/**
	 * Полное название базового (корневого) пакета для серверного модуля.
	 */
	public String srvBasePackage;

	/**
	 * Полное название пакета, в котором будут находиться класс Renderers и файл renderers.properties с конфигом Excel-рендереров приложения.
	 * По умолчанию — ({@linkplain #srvBasePackage базовый пакет серверного модуля} + ".render")
	 */
	public String renderersPackage;

	/**
	 * Полное название базового (корневого) пакета для веб-модуля.
	 */
	public String webBasePackage;

	/**
	 * Название persistenceUnit (указывается в persistence.xml).
	 * По умолчанию равно названию проекта.
	 */
	public String persistenceUnitName;

	/**
	 * Название jtaDataSource без префикса "java' (указывается в persistence.xml).
	 * По умолчанию — (название проекта + "DS").
	 */
	public String jtaDataSourceName;

	/**
	 * ContextPath приложения. Указывается в элементе &lt;context-root&gt; в jboss-web.xml.
	 * По умолчанию - равно названию проекта
	 */
	public String contextPath;

	/**
	 * ContextPath, по которому находится extJS. Используется при включении js-файлов в kefirInclude.jspf.
	 * По умолчанию - "/ext"
	 */
	public String extContextPath;

	/**
	 * ContextPath, по которому задеплоен kefirStatic.war. Используется при включении js-файлов в kefirInclude.jspf.
	 * По умолчанию - "/kefirStatic"
	 */
	public String kefirStaticContextPath;

	/**
	 * Заголовок (title) html-страницы index.jsp.
	 */
	public String htmlTitle;

	/**
	 * Заголовок главного меню.
	 * По умолчанию - равен {@linkplain #htmlTitle заголовку html-страницы}.
	 */
	public String mainMenuTitle;

	/**
	 * Ширина кнопки главного меню.
	 * По умолчанию — {@linkplain ProjectConfigUtils#DEFAULT_MAIN_MENU_BUTTON_WIDTH 400 пискселов}.
	 */
	public String mainMenuButtonWidth;

	/**
	 * Js-неймспейс, в котором будет создан js-класс "MainMenu" приложения с главным мению приложения.
	 * По умолчанию — равно названию проекта.
	 */
	public String mainMenuNamespace;

	/**
	 * Пакет, в котором будет создаваться класс для адреса.
	 * По умолчанию — {@linkplain #srvBasePackage равен базовому пакету для серверного модуля}.
	 */
	public String addressClassPackage;

	/**
	 * Js-namespace для формы адреса.
	 * По умолчанию — пакет, в котором находится класс сущности + "address".
	 */
	public String addressJsNamespace;

	/**
	 * Js form namespace для формы адреса.
	 * По умолчанию "Address".
	 */
	public String addressFormJsNamespace;

	/**
	 * Путь к директории, содержащей общие библиотеки для серверной и веб частей приложения, относительно базовой директории проекта.
	 * По умолчанию — "lib"
	 */
	public String commonLibPath;

	/**
	 * Путь к директории, содержащей библиотеки для серверной части приложения, относительно базовой директории серверной части.
	 * По умолчанию — "lib"
	 */
	public String srvLibPath;

	/**
	 * Путь к директории, содержащей общие библиотеки для веб-части приложения, относительно базовой директории веб-части.
	 * По умолчанию — "lib"
	 */
	public String webLibPath;

	/**
	 * Путь к директории, из которой будут браться файлы для занесения в lib директории созданного проекта.
	 * Директория должна содержать поддиректории "common", "srv", "web" и "selenium".
	 */
	public String libPath;

	/**
	 * Путь к директории, из которой будут браться темплейт-файлы для генерации проекта.
	 * Директория должна содержать темплейт-файл addressTemplate.js.
	 */
	public String templatePath;

	/**
	 * Название jar-файла, собираемого для серверной части.
	 * По умолчанию — (название серверного модуля + ".jar")
	 */
	public String srvJarName;

	/**
	 * Название war-файла, собираемого для веб части.
	 * По умолчанию — (название веб-модуля + ".war")
	 */
	public String webWarName;

	/**
	 * Название ear-файла, собираемого для приложения и деплоемого на сервер.
	 * По умолчанию — (название проекта + ".ear")
	 */
	public String earName;

	/**
	 * Путь для деплоя приложения.
	 * Будет перенесен в build.properties для сгенерированного проекта.
	 */
	public String deployPath;

	/**
	 * Путь для деплоя общих библиотек приложения (kefirUtil, json итд)
	 * Будет перенесен в build.properties для сгенерированного проекта.
	 */
	public String deployLib;
}
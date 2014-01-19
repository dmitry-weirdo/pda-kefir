/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.helper;

import java.io.IOException;

public interface GeneratorServiceHelper
{
	/**
	 * Генерирует пустой локальный сервис, его пустую имплементацию и его маппинг в web.xml и jboss-web.xml
	 * с именованиями по умолчанию, определяемыми исходя из полного имени класса сервиса.
	 * @param config конфиг, в котором должны содержаться поля:
	 * <ul>
	 * 	<li>{@linkplain HelperConfig#srvSrcDir srvSrcDir} путь с исходниками, в котором будут располагаться сервис и его реализация</li>
	 * 	<li>{@linkplain HelperConfig#webXmlPath webXmlPath} путь к файлу web.xml</li>
	 * 	<li>{@linkplain HelperConfig#jbossWebXmlPath jbossWebXmlPath} путь к файлу jboss-web.xml</li>
	 * 	<li>{@linkplain HelperConfig#serviceClassName serviceClassName} полное имя класса сервиса</li>
	 * </ul>
	 * @throws IOException в случае ошибки записи какого-либо файла
	 */
	void generateLocalInterface(HelperConfig config) throws IOException;

	/**
	 * Добавляет orm-маппинг класса ExtEntity класса на основании его полей, создание таблицы сущности в createTables.sql и ее удаление в dropTables.sql.
	 * Если маппинг уже присутствует в orm.xml, createTables.sql или dropTables.sql, то ничего не происходит для соответствующих файлов.
	 * @param config конфиг, в котором должны содержаться поля:
	 * <ul>
	 * 	<li>{@linkplain HelperConfig#entityClass entityClass} класс сущности, должен содержать аннотацию ExtEntity</li>
	 * 	<li>{@linkplain HelperConfig#ormXmlPath ormXmlPath} путь к orm.xml, в который будет добавляться маппинг</li>
	 * 	<li>{@linkplain HelperConfig#createTablesSqlPath createTablesSqlPath} путь к createTables.sql, в который будет добавляться создание таблицы</li>
	 * 	<li>{@linkplain HelperConfig#dropTablesSqlPath dropTablesSqlPath} путь к dropTables.sql, в который будет добавляться удаление таблицы</li>
	 * </ul>
	 * @throws IOException в случае ошибки записи какого-либо файла
	 */
	void generateEntityMapping(HelperConfig config) throws IOException;

	/**
	 * Генерирует VO для класса сущности в серверной части приложения.
	 * Полное имя класса сущности определяется исходя из {@linkplain su.opencode.kefir.gen.ExtEntity#listVoClassName() свойства listVoClassName} его @ExtEntity аннотации.
	 * При наличии @ViewConfig аннотации на сущности, создается viewConfig в viewConfigs.js и необходимые стили в main.css для списка сущностей и легенды
	 * @param config конфиг, в котором должны содержаться поля:
	 * <ul>
	 * 	<li>{@linkplain HelperConfig#entityClass entityClass} класс сущности, должен содержать аннотацию ExtEntity</li>
	 * 	<li>{@linkplain HelperConfig#srvSrcDir srvSrcDir} путь к исходникам серверной части</li>
	 * 	<li>{@linkplain HelperConfig#viewConfigsJsPath viewConfigsJsPath} путь к viewConfigs.js</li>
	 * 	<li>{@linkplain HelperConfig#mainCssPath mainCssPath} путь к main.css</li>
	 * </ul>
	 * @throws IOException в случае ошибки записи какого-либо файла
	 */
	void generateEntityVO(HelperConfig config) throws IOException;

	/**
	 * Генерирует необходимые компоненты для отображения списка сущностей и выбора из списка сущностей:
	 * <ul>
	 * 	<li>методы локального сервиса;</li>
	 * 	<li>FilterConfig</li>
	 * 	<li>сервлет, получающий список сущностей</li>
	 * 	<li>сервлет, выгружающий в Excel список сущностей</li>
	 * 	<li>js-файл, отображающий список сущностей</li>
	 * 	<li>js-файл, отображающий выбор из списка сущностей</li>
	 * 	<li>в главное меню добавляется кнопка с переходом на список сущностей, если {@linkplain su.opencode.kefir.gen.ExtEntity#hasListMainMenuButton() соответствующий признак} выставлен</li>
	 * </ul>
	 * @param config конфиг, в котором должны содержаться поля:
	 * <ul>
	 * 	<li>{@linkplain HelperConfig#entityClass entityClass} класс сущности, должен содержать аннотацию ExtEntity</li>
	 * 	<li>{@linkplain HelperConfig#renderersClass renderersClass} класс сущности Renderers, содержащий {@linkplain su.opencode.kefir.gen.fileWriter.RenderersFileWriter#GET_RENDER_INPUT_STREAM_METHOD_NAME метод получения InputStream для файла renderers.properties приложения}</li>
	 * 	<li>{@linkplain HelperConfig#srvSrcDir srvSrcDir} путь к исходникам серверной части</li>
	 * 	<li>{@linkplain HelperConfig#webSrcDir webSrcDir} путь к исходникам веб-части</li>
	 * 	<li>{@linkplain HelperConfig#webXmlPath webXmlPath} путь к файлу web.xml</li>
	 * 	<li>{@linkplain HelperConfig#jsDir jsDir} путь к исходникам js</li>
	 * 	<li>{@linkplain HelperConfig#jsIncludeFilePath jsIncludeFilePath} путь к файлу с инклюдами js (application.jspf)</li>
	 * 	<li>{@linkplain HelperConfig#baseJsPath baseJsPath} базовый путь к js-файлам при инклюде (например, "./js")</li>
	 * </ul>
	 * @throws IOException в случае ошибки записи какого-либо файла
	 */
	void generateEntityList(HelperConfig config) throws IOException;

	/**
	 * Генерирует сервисные методы для получения по id, создания, изменения, редактирования и удаления сущностей:
	 * <ul>
	 * 	<li>методы локального сервиса для этих действий</li>
	 * </ul>
	 * @param config конфиг, в котором должны содержаться поля:
	 * <ul>
	 * 	<li>{@linkplain HelperConfig#entityClass entityClass} класс сущности, должен содержать аннотацию ExtEntity</li>
	 * 	<li>{@linkplain HelperConfig#srvSrcDir srvSrcDir} путь к исходникам серверной части</li>
	 * </ul>
	 * @throws IOException в случае ошибки записи какого-либо файла
	 */
	void generateEntityServiceCrudMethods(HelperConfig config) throws IOException;

	/**
	 * Генерирует необходимые компоненты для получения по id, создания, изменения, редактирования и удаления сущностей:
	 * <ul>
	 * 	<li>методы локального сервиса для этих действий</li>
	 * 	<li>сервлеты и их маппинги для этих действий</li>
	 * 	<li>js-файл, отображающий crud-формы для сущности</li>
	 * </ul>
	 * @param config конфиг, в котором должны содержаться поля:
	 * <ul>
	 * 	<li>{@linkplain HelperConfig#entityClass entityClass} класс сущности, должен содержать аннотацию ExtEntity</li>
	 * 	<li>{@linkplain HelperConfig#addressClass addressClass} класс адреса, должен содержать аннотацию ExtEntity</li>
	 * 	<li>{@linkplain HelperConfig#srvSrcDir srvSrcDir} путь к исходникам серверной части</li>
	 * 	<li>{@linkplain HelperConfig#webSrcDir webSrcDir} путь к исходникам веб-части</li>
	 * 	<li>{@linkplain HelperConfig#webXmlPath webXmlPath} путь к файлу web.xml</li>
	 * 	<li>{@linkplain HelperConfig#jsDir jsDir} путь к исходникам js</li>
	 * 	<li>{@linkplain HelperConfig#jsIncludeFilePath jsIncludeFilePath} путь к файлу с инклюдами js (application.jspf)</li>
	 * 	<li>{@linkplain HelperConfig#baseJsPath baseJsPath} базовый путь к js-файлам при инклюде (например, "./js")</li>
	 * </ul>
	 * @throws IOException в случае ошибки записи какого-либо файла
	 */
	void generateEntityCrud(HelperConfig config) throws IOException;

	/**
	 * Генерирует необходимые js-компоненты для работы с enum-полем в js:
	 * <ul>
	 * 	<li>Хэш, содержащий значения энума, в качестве значений используются строки со значениями энума</li>
	 * 	<li>Local store, хранящий значения энума и отображаемые значения для использования в LocalComboBox</li>
	 * 	<li>Рендерер, обрабатывающий строковые значения энума для отображения в таблице</li>
	 * 	<li>{@linkplain su.opencode.kefir.srv.renderer.CellRenderer CellRenderer}, обрабатывающий строковые значения энума для экспорта в Excel</li>
	 * 	<li>Добавляется соответствие js-рендерера и сгенерированного CellRenderer в renders.properties приложения</li>
	 * </ul>
	 * @param config конфиг, в котором должны содержаться поля:
	 * <ul>
	 * 	<li>{@linkplain HelperConfig#enumClass enumClass} класс энума, должен содержать аннотацию ExtField</li>
	 * 	<li>{@linkplain HelperConfig#constantsJsFilePath constantsJsFilePath} путь к js-файлу с хэшами-константами</li>
	 * 	<li>{@linkplain HelperConfig#localStoresJsFilePath localStoresJsFilePath} путь к js-файлу со store</li>
	 * 	<li>{@linkplain HelperConfig#renderersJsFilePath renderersJsFilePath} путь к js-файлу с рендерерами</li>
	 * 	<li>{@linkplain HelperConfig#srvSrcDir srvSrcDir} путь к исходникам серверной части</li>
	 * 	<li>{@linkplain HelperConfig#rendererPropertiesFilePath rendererPropertiesFilePath} путь к файлу renderer.properties</li>
	 * 	<li>{@linkplain HelperConfig#renderersClassFilePath renderersClassFilePath} путь к файлу Renderers.java</li>
	 * </ul>
	 * @throws IOException в случае ошибки записи какого-либо файла
	 */
	void generateEnumJs(HelperConfig config) throws IOException;

	/**
	 * Генерирует необходимые классы Page, DataProvider, TestCase для тестирования сущности при помощи Selenium:
	 * <ul>
	 *   <li>FormPage - класс, с помощью которого можно вводить тестовые данные в форму</li>
	 *   <li>ListPage - класс, который описывает работу со списком сцщности</li>
	 *   <li>DataProvider - класс, который загружает тестовые данные</li>
	 *   <li>TestCase - тест, в котором выполняется создание, просморт, изменение, удаление и создание, если не найдено нужной сущности</li>
	 * </ul>
	 * @param config конфиг, в котором должны содержаться поля:
	 * <ul>
	 * 	<li>{@linkplain HelperConfig#entityClass entityClass} класс сущности, должен содержать аннотацию ExtEntity</li>
	 * 	<li>{@linkplain HelperConfig#seleniumSrcDir seleniumSrcDir} путь к исходникам selenium страниц</li>
	 * 	<li>{@linkplain HelperConfig#seleniumTestDir seleniumTestDir} путь к тестам selenium страниц, куда будут добавляться TestCase и DataProvider</li>
	 * </ul>
	 * @throws IOException в случае ошибки записи какого-либо файла
	 */
	void generateEntityTests(HelperConfig config) throws IOException;

	void generateMainPage(HelperConfig config, String mainMenuTitle) throws IOException;
}
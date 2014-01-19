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
	 * ���������� ������ ��������� ������, ��� ������ ������������� � ��� ������� � web.xml � jboss-web.xml
	 * � ������������ �� ���������, ������������� ������ �� ������� ����� ������ �������.
	 * @param config ������, � ������� ������ ����������� ����:
	 * <ul>
	 * 	<li>{@linkplain HelperConfig#srvSrcDir srvSrcDir} ���� � �����������, � ������� ����� ������������� ������ � ��� ����������</li>
	 * 	<li>{@linkplain HelperConfig#webXmlPath webXmlPath} ���� � ����� web.xml</li>
	 * 	<li>{@linkplain HelperConfig#jbossWebXmlPath jbossWebXmlPath} ���� � ����� jboss-web.xml</li>
	 * 	<li>{@linkplain HelperConfig#serviceClassName serviceClassName} ������ ��� ������ �������</li>
	 * </ul>
	 * @throws IOException � ������ ������ ������ ������-���� �����
	 */
	void generateLocalInterface(HelperConfig config) throws IOException;

	/**
	 * ��������� orm-������� ������ ExtEntity ������ �� ��������� ��� �����, �������� ������� �������� � createTables.sql � �� �������� � dropTables.sql.
	 * ���� ������� ��� ������������ � orm.xml, createTables.sql ��� dropTables.sql, �� ������ �� ���������� ��� ��������������� ������.
	 * @param config ������, � ������� ������ ����������� ����:
	 * <ul>
	 * 	<li>{@linkplain HelperConfig#entityClass entityClass} ����� ��������, ������ ��������� ��������� ExtEntity</li>
	 * 	<li>{@linkplain HelperConfig#ormXmlPath ormXmlPath} ���� � orm.xml, � ������� ����� ����������� �������</li>
	 * 	<li>{@linkplain HelperConfig#createTablesSqlPath createTablesSqlPath} ���� � createTables.sql, � ������� ����� ����������� �������� �������</li>
	 * 	<li>{@linkplain HelperConfig#dropTablesSqlPath dropTablesSqlPath} ���� � dropTables.sql, � ������� ����� ����������� �������� �������</li>
	 * </ul>
	 * @throws IOException � ������ ������ ������ ������-���� �����
	 */
	void generateEntityMapping(HelperConfig config) throws IOException;

	/**
	 * ���������� VO ��� ������ �������� � ��������� ����� ����������.
	 * ������ ��� ������ �������� ������������ ������ �� {@linkplain su.opencode.kefir.gen.ExtEntity#listVoClassName() �������� listVoClassName} ��� @ExtEntity ���������.
	 * ��� ������� @ViewConfig ��������� �� ��������, ��������� viewConfig � viewConfigs.js � ����������� ����� � main.css ��� ������ ��������� � �������
	 * @param config ������, � ������� ������ ����������� ����:
	 * <ul>
	 * 	<li>{@linkplain HelperConfig#entityClass entityClass} ����� ��������, ������ ��������� ��������� ExtEntity</li>
	 * 	<li>{@linkplain HelperConfig#srvSrcDir srvSrcDir} ���� � ���������� ��������� �����</li>
	 * 	<li>{@linkplain HelperConfig#viewConfigsJsPath viewConfigsJsPath} ���� � viewConfigs.js</li>
	 * 	<li>{@linkplain HelperConfig#mainCssPath mainCssPath} ���� � main.css</li>
	 * </ul>
	 * @throws IOException � ������ ������ ������ ������-���� �����
	 */
	void generateEntityVO(HelperConfig config) throws IOException;

	/**
	 * ���������� ����������� ���������� ��� ����������� ������ ��������� � ������ �� ������ ���������:
	 * <ul>
	 * 	<li>������ ���������� �������;</li>
	 * 	<li>FilterConfig</li>
	 * 	<li>�������, ���������� ������ ���������</li>
	 * 	<li>�������, ����������� � Excel ������ ���������</li>
	 * 	<li>js-����, ������������ ������ ���������</li>
	 * 	<li>js-����, ������������ ����� �� ������ ���������</li>
	 * 	<li>� ������� ���� ����������� ������ � ��������� �� ������ ���������, ���� {@linkplain su.opencode.kefir.gen.ExtEntity#hasListMainMenuButton() ��������������� �������} ���������</li>
	 * </ul>
	 * @param config ������, � ������� ������ ����������� ����:
	 * <ul>
	 * 	<li>{@linkplain HelperConfig#entityClass entityClass} ����� ��������, ������ ��������� ��������� ExtEntity</li>
	 * 	<li>{@linkplain HelperConfig#renderersClass renderersClass} ����� �������� Renderers, ���������� {@linkplain su.opencode.kefir.gen.fileWriter.RenderersFileWriter#GET_RENDER_INPUT_STREAM_METHOD_NAME ����� ��������� InputStream ��� ����� renderers.properties ����������}</li>
	 * 	<li>{@linkplain HelperConfig#srvSrcDir srvSrcDir} ���� � ���������� ��������� �����</li>
	 * 	<li>{@linkplain HelperConfig#webSrcDir webSrcDir} ���� � ���������� ���-�����</li>
	 * 	<li>{@linkplain HelperConfig#webXmlPath webXmlPath} ���� � ����� web.xml</li>
	 * 	<li>{@linkplain HelperConfig#jsDir jsDir} ���� � ���������� js</li>
	 * 	<li>{@linkplain HelperConfig#jsIncludeFilePath jsIncludeFilePath} ���� � ����� � ��������� js (application.jspf)</li>
	 * 	<li>{@linkplain HelperConfig#baseJsPath baseJsPath} ������� ���� � js-������ ��� ������� (��������, "./js")</li>
	 * </ul>
	 * @throws IOException � ������ ������ ������ ������-���� �����
	 */
	void generateEntityList(HelperConfig config) throws IOException;

	/**
	 * ���������� ��������� ������ ��� ��������� �� id, ��������, ���������, �������������� � �������� ���������:
	 * <ul>
	 * 	<li>������ ���������� ������� ��� ���� ��������</li>
	 * </ul>
	 * @param config ������, � ������� ������ ����������� ����:
	 * <ul>
	 * 	<li>{@linkplain HelperConfig#entityClass entityClass} ����� ��������, ������ ��������� ��������� ExtEntity</li>
	 * 	<li>{@linkplain HelperConfig#srvSrcDir srvSrcDir} ���� � ���������� ��������� �����</li>
	 * </ul>
	 * @throws IOException � ������ ������ ������ ������-���� �����
	 */
	void generateEntityServiceCrudMethods(HelperConfig config) throws IOException;

	/**
	 * ���������� ����������� ���������� ��� ��������� �� id, ��������, ���������, �������������� � �������� ���������:
	 * <ul>
	 * 	<li>������ ���������� ������� ��� ���� ��������</li>
	 * 	<li>�������� � �� �������� ��� ���� ��������</li>
	 * 	<li>js-����, ������������ crud-����� ��� ��������</li>
	 * </ul>
	 * @param config ������, � ������� ������ ����������� ����:
	 * <ul>
	 * 	<li>{@linkplain HelperConfig#entityClass entityClass} ����� ��������, ������ ��������� ��������� ExtEntity</li>
	 * 	<li>{@linkplain HelperConfig#addressClass addressClass} ����� ������, ������ ��������� ��������� ExtEntity</li>
	 * 	<li>{@linkplain HelperConfig#srvSrcDir srvSrcDir} ���� � ���������� ��������� �����</li>
	 * 	<li>{@linkplain HelperConfig#webSrcDir webSrcDir} ���� � ���������� ���-�����</li>
	 * 	<li>{@linkplain HelperConfig#webXmlPath webXmlPath} ���� � ����� web.xml</li>
	 * 	<li>{@linkplain HelperConfig#jsDir jsDir} ���� � ���������� js</li>
	 * 	<li>{@linkplain HelperConfig#jsIncludeFilePath jsIncludeFilePath} ���� � ����� � ��������� js (application.jspf)</li>
	 * 	<li>{@linkplain HelperConfig#baseJsPath baseJsPath} ������� ���� � js-������ ��� ������� (��������, "./js")</li>
	 * </ul>
	 * @throws IOException � ������ ������ ������ ������-���� �����
	 */
	void generateEntityCrud(HelperConfig config) throws IOException;

	/**
	 * ���������� ����������� js-���������� ��� ������ � enum-����� � js:
	 * <ul>
	 * 	<li>���, ���������� �������� �����, � �������� �������� ������������ ������ �� ���������� �����</li>
	 * 	<li>Local store, �������� �������� ����� � ������������ �������� ��� ������������� � LocalComboBox</li>
	 * 	<li>��������, �������������� ��������� �������� ����� ��� ����������� � �������</li>
	 * 	<li>{@linkplain su.opencode.kefir.srv.renderer.CellRenderer CellRenderer}, �������������� ��������� �������� ����� ��� �������� � Excel</li>
	 * 	<li>����������� ������������ js-��������� � ���������������� CellRenderer � renders.properties ����������</li>
	 * </ul>
	 * @param config ������, � ������� ������ ����������� ����:
	 * <ul>
	 * 	<li>{@linkplain HelperConfig#enumClass enumClass} ����� �����, ������ ��������� ��������� ExtField</li>
	 * 	<li>{@linkplain HelperConfig#constantsJsFilePath constantsJsFilePath} ���� � js-����� � ������-�����������</li>
	 * 	<li>{@linkplain HelperConfig#localStoresJsFilePath localStoresJsFilePath} ���� � js-����� �� store</li>
	 * 	<li>{@linkplain HelperConfig#renderersJsFilePath renderersJsFilePath} ���� � js-����� � �����������</li>
	 * 	<li>{@linkplain HelperConfig#srvSrcDir srvSrcDir} ���� � ���������� ��������� �����</li>
	 * 	<li>{@linkplain HelperConfig#rendererPropertiesFilePath rendererPropertiesFilePath} ���� � ����� renderer.properties</li>
	 * 	<li>{@linkplain HelperConfig#renderersClassFilePath renderersClassFilePath} ���� � ����� Renderers.java</li>
	 * </ul>
	 * @throws IOException � ������ ������ ������ ������-���� �����
	 */
	void generateEnumJs(HelperConfig config) throws IOException;

	/**
	 * ���������� ����������� ������ Page, DataProvider, TestCase ��� ������������ �������� ��� ������ Selenium:
	 * <ul>
	 *   <li>FormPage - �����, � ������� �������� ����� ������� �������� ������ � �����</li>
	 *   <li>ListPage - �����, ������� ��������� ������ �� ������� ��������</li>
	 *   <li>DataProvider - �����, ������� ��������� �������� ������</li>
	 *   <li>TestCase - ����, � ������� ����������� ��������, ��������, ���������, �������� � ��������, ���� �� ������� ������ ��������</li>
	 * </ul>
	 * @param config ������, � ������� ������ ����������� ����:
	 * <ul>
	 * 	<li>{@linkplain HelperConfig#entityClass entityClass} ����� ��������, ������ ��������� ��������� ExtEntity</li>
	 * 	<li>{@linkplain HelperConfig#seleniumSrcDir seleniumSrcDir} ���� � ���������� selenium �������</li>
	 * 	<li>{@linkplain HelperConfig#seleniumTestDir seleniumTestDir} ���� � ������ selenium �������, ���� ����� ����������� TestCase � DataProvider</li>
	 * </ul>
	 * @throws IOException � ������ ������ ������ ������-���� �����
	 */
	void generateEntityTests(HelperConfig config) throws IOException;

	void generateMainPage(HelperConfig config, String mainMenuTitle) throws IOException;
}
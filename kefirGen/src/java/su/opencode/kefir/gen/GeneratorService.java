/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen;

import su.opencode.kefir.gen.field.enumField.EnumField;
import su.opencode.kefir.gen.project.ProjectConfig;

import java.io.IOException;

public interface GeneratorService
{
	// java classes methods
	void generateEmptyServlet(String baseDir, String packageName, String className) throws IOException;

	void generateLocalService(String baseDir, String packageName, String className) throws IOException;
	void appendLocalServiceReference(String webXmlPath, String serviceClassName, String serviceReferenceName) throws IOException;
	void appendLocalServiceReference(String webXmlPath, ExtEntity extEntity, Class entityClass) throws IOException;

	void generateLocalServiceBean(String baseDir, String packageName, String className, String interfaceName) throws IOException;
	void appendLocalServiceBeanReference(String jbossWebXmlPath, String serviceReferenceName, String serviceJndiName) throws IOException;
	void appendLocalServiceBeanReference(String jbossWebXmlPath, ExtEntity extEntity, Class entityClass) throws IOException;

	// orm-mapping
	void appendOrmMapping(String ormXmlPath, ExtEntity extEntity, Class entityClass) throws IOException;
	void appendCreateTable(String createTablesSqlPath, ExtEntity extEntity, Class entityClass) throws IOException;
	void appendDropTable(String dropTablesSqlPath, ExtEntity extEntity, Class entityClass) throws IOException;

	// entity VO
	void generateVO(String baseDir, Class renderersClass, ExtEntity extEntity, Class entityClass) throws IOException;

	// view config
	void generateViewConfig(String viewConfigJsPath, Class entityClass) throws IOException;
	void generateViewConfigStyles(String mainCssPath, Class entityClass) throws IOException;

	// list
	void generateEntityQueryBuilder(String baseDir, ExtEntity extEntity, Class entityClass) throws IOException;
	void generateEntityFilterConfig(String baseDir, ExtEntity extEntity, Class entityClass) throws IOException;

	void generateServiceListMethods(String baseDir, ExtEntity extEntity, Class entityClass) throws IOException;
	void generateServiceBeanListMethods(String baseDir, ExtEntity extEntity, Class entityClass) throws IOException;

	void generateListServlet(String baseDir, ExtEntity extEntity, Class entityClass) throws IOException;
	void generateListServletMapping(String webXmlPath, ExtEntity extEntity, Class entityClass) throws IOException;

	// list to Excel
	void generateListToExcelServlet(String baseDir, ExtEntity extEntity, Class entityClass, Class renderersClass) throws IOException;
	void generateListToExcelServletMapping(String webXmlPath, ExtEntity extEntity, Class entityClass) throws IOException;

	// js methods
	void generateListScript(String baseDir, String jsIncludeFilePath, String baseJsPath, ExtEntity extEntity, Class entityClass) throws IOException;
	void generateChooseScript(String baseDir, String jsIncludeFilePath, String baseJsPath, ExtEntity extEntity, Class entityClass) throws IOException;

	// list main menu button
	void generateListMainMenuButton(String mainMenuJsPath, ExtEntity extEntity, Class entityClass) throws IOException;


	// get
	void generateServiceGetMethod(String baseDir, ExtEntity extEntity, Class entityClass) throws IOException;
	void generateServiceBeanGetMethod(String baseDir, ExtEntity extEntity, Class entityClass) throws IOException;

	void generateGetServlet(String baseDir, ExtEntity extEntity, Class entityClass) throws IOException;
	void generateGetServletMapping(String webXmlPath, ExtEntity extEntity, Class entityClass) throws IOException;


	// create
	void generateServiceCreateMethod(String baseDir, ExtEntity extEntity, Class entityClass) throws IOException;
	void generateServiceBeanCreateMethod(String baseDir, ExtEntity extEntity, Class entityClass) throws IOException;

	void generateCreateServlet(String baseDir, ExtEntity extEntity, Class entityClass) throws IOException;
	void generateCreateServletMapping(String webXmlPath, ExtEntity extEntity, Class entityClass) throws IOException;


	// update
	void generateServiceUpdateMethod(String baseDir, ExtEntity extEntity, Class entityClass) throws IOException;
	void generateServiceBeanUpdateMethod(String baseDir, ExtEntity extEntity, Class entityClass, Class addressClass) throws IOException;

	void generateUpdateServlet(String baseDir, ExtEntity extEntity, Class entityClass) throws IOException;
	void generateUpdateServletMapping(String webXmlPath, ExtEntity extEntity, Class entityClass) throws IOException;


	// delete
	void generateServiceDeleteMethod(String baseDir, ExtEntity extEntity, Class entityClass) throws IOException;
	void generateServiceBeanDeleteMethod(String baseDir, ExtEntity extEntity, Class entityClass) throws IOException;

	void generateDeleteServlet(String baseDir, ExtEntity extEntity, Class entityClass) throws IOException;
	void generateDeleteServletMapping(String webXmlPath, ExtEntity extEntity, Class entityClass) throws IOException;


	// js methods for CRUD form
	void generateFormScript(String baseDir, String jsIncludeFilePath, String baseJsPath, ExtEntity extEntity, Class entityClass) throws IOException;


	// enum methods
	void generateEnumHash(String filePath, EnumField enumField, Class enumClass) throws IOException;
	void generateEnumStore(String filePath, EnumField enumField, Class enumClass) throws IOException;
	void generateEnumRenderer(String filePath, EnumField enumField, Class enumClass) throws IOException;
	void generateEnumCellRenderer(String baseDir, String rendererPropertiesFilePath, String renderersClassFilePath, EnumField enumField, Class enumClass) throws IOException;

	//selenium tests
	public void generateFormPage(String baseDir, ExtEntity extEntity, Class entityClass) throws IOException;
	public void generateMainPage(String baseDir, String mainMenuTitle) throws IOException;
	public void generateListPage(String baseDir, ExtEntity extEntity, Class entityClass) throws IOException;
	public void generateChooseListPage(String baseDir, ExtEntity extEntity, Class entityClass) throws IOException;
	public void generateDataProvider(String baseDir, Class entityClass) throws IOException;
	public void generateTest(String baseDir, Class entityClass) throws IOException;
	public void generateTestData(String baseDir, Class entityClass) throws IOException;
}
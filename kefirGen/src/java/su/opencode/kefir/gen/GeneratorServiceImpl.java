/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen;

import su.opencode.kefir.gen.appender.RenderersAppender;
import su.opencode.kefir.gen.appender.css.ViewConfigStylesAppender;
import su.opencode.kefir.gen.appender.js.*;
import su.opencode.kefir.gen.appender.jsInclude.ChooseScriptIncludeAppender;
import su.opencode.kefir.gen.appender.jsInclude.FormLayoutScriptIncludeAppender;
import su.opencode.kefir.gen.appender.jsInclude.FormScriptIncludeAppender;
import su.opencode.kefir.gen.appender.jsInclude.ListScriptIncludeAppender;
import su.opencode.kefir.gen.appender.selenium.MainPageAppender;
import su.opencode.kefir.gen.appender.service.*;
import su.opencode.kefir.gen.appender.serviceBean.*;
import su.opencode.kefir.gen.appender.servlet.*;
import su.opencode.kefir.gen.field.enumField.EnumField;
import su.opencode.kefir.gen.fileWriter.*;
import su.opencode.kefir.gen.fileWriter.js.ChooseJsFileWriter;
import su.opencode.kefir.gen.fileWriter.js.FormJsFileWriter;
import su.opencode.kefir.gen.fileWriter.js.FormLayoutJsFileWriter;
import su.opencode.kefir.gen.fileWriter.js.ListJsFileWriter;
import su.opencode.kefir.gen.fileWriter.selenium.*;
import su.opencode.kefir.gen.fileWriter.servlet.*;
import su.opencode.kefir.gen.project.properties.RendererPropertiesAppender;
import su.opencode.kefir.gen.project.sql.appender.CreateExtEntityTableAppender;
import su.opencode.kefir.gen.project.sql.appender.DropExtEntityTableAppender;
import su.opencode.kefir.gen.project.xml.orm.ExtEntityMappingAppender;

import java.io.IOException;

import static su.opencode.kefir.gen.field.ExtEntityFieldsUtils.getViewConfigAnnotation;
import static su.opencode.kefir.gen.field.ExtEntityFieldsUtils.hasViewConfigAnnotation;

public class GeneratorServiceImpl implements GeneratorService
{
	public void generateEmptyServlet(String baseDir, String packageName, String className) throws IOException {
		new EmptyJsonServletFileWriter(baseDir, packageName, className).createFile();
	}

	public void generateLocalService(String baseDir, String packageName, String className) throws IOException {
		new LocalServiceFileWriter(baseDir, packageName, className).createFile();
	}
	public void appendLocalServiceReference(String webXmlPath, String serviceClassName, String serviceReferenceName) throws IOException {
		new ServiceReferenceAppender(webXmlPath, serviceClassName, serviceReferenceName).appendEjbLocalReference();
	}
	public void appendLocalServiceReference(String webXmlPath, ExtEntity extEntity, Class entityClass) throws IOException {
		new ServiceReferenceAppender(webXmlPath, extEntity, entityClass).appendEjbLocalReference();
	}

	public void generateLocalServiceBean(String baseDir, String packageName, String className, String interfaceName) throws IOException {
		new LocalServiceBeanFileWriter(baseDir, packageName, className, interfaceName).createFile();
	}
	public void appendLocalServiceBeanReference(String jbossWebXmlPath, String serviceReferenceName, String serviceJndiName) throws IOException {
		new ServiceBeanReferenceAppender(jbossWebXmlPath, serviceReferenceName, serviceJndiName).appendEjbLocalReference();
	}
	public void appendLocalServiceBeanReference(String jbossWebXmlPath, ExtEntity extEntity, Class entityClass) throws IOException {
		new ServiceBeanReferenceAppender(jbossWebXmlPath, extEntity, entityClass).appendEjbLocalReference();
	}

	// mapping
	public void appendOrmMapping(String ormXmlPath, ExtEntity extEntity, Class entityClass) throws IOException {
		new ExtEntityMappingAppender(ormXmlPath, extEntity, entityClass).appendEntityMapping();
	}
	public void appendCreateTable(String createTablesSqlPath, ExtEntity extEntity, Class entityClass) throws IOException {
		new CreateExtEntityTableAppender(createTablesSqlPath, extEntity, entityClass).appendCreateTable();
	}
	public void appendDropTable(String dropTablesSqlPath, ExtEntity extEntity, Class entityClass) throws IOException {
		new DropExtEntityTableAppender(dropTablesSqlPath, extEntity, entityClass).appendDropTable();
	}

	// vo
	public void generateVO(String baseDir, Class renderersClass, ExtEntity extEntity, Class entityClass) throws IOException {
		new VOClassFileWriter(baseDir, null, null, extEntity, entityClass, renderersClass).createFile();
	}

	public void generateViewConfig(String viewConfigJsPath, Class entityClass) throws IOException {
		if ( !hasViewConfigAnnotation(entityClass) )
			return;

		new ViewConfigAppender(viewConfigJsPath, getViewConfigAnnotation(entityClass), entityClass).appendViewConfig();
	}
	public void generateViewConfigStyles(String mainCssPath, Class entityClass) throws IOException {
		if ( !hasViewConfigAnnotation(entityClass) )
			return;

		new ViewConfigStylesAppender(mainCssPath, getViewConfigAnnotation(entityClass), entityClass).appendStyles();
	}

	// list
	public void generateListServlet(String baseDir, ExtEntity extEntity, Class entityClass) throws IOException {
		new ListServletFileWriter(baseDir, extEntity, entityClass).createFile();
	}
	public void generateListServletMapping(String webXmlPath, ExtEntity extEntity, Class entityClass) throws IOException {
		new ListServletMappingAppender(webXmlPath, extEntity, entityClass).appendServletMapping();
	}

	public void generateListToExcelServlet(String baseDir, ExtEntity extEntity, Class entityClass, Class renderersClass) throws IOException {
		new ListToExcelServletFileWriter(baseDir, extEntity, entityClass, renderersClass).createFile();
	}
	public void generateListToExcelServletMapping(String webXmlPath, ExtEntity extEntity, Class entityClass) throws IOException {
		new ListToExcelServletMappingAppender(webXmlPath, extEntity, entityClass).appendServletMapping();
	}

	public void generateEntityQueryBuilder(String baseDir, ExtEntity extEntity, Class entityClass) throws IOException {
		new EntityQueryBuilderFileWriter(baseDir, extEntity, entityClass).createFile();
	}
	public void generateEntityFilterConfig(String baseDir, ExtEntity extEntity, Class entityClass) throws IOException {
		new EntityFilterConfigFileWriter(baseDir, extEntity, entityClass).createFile();
	}
	public void generateServiceListMethods(String baseDir, ExtEntity extEntity, Class entityClass) throws IOException {
		new ListMethodsAppender(baseDir, extEntity, entityClass).appendMethods();
	}
	public void generateServiceBeanListMethods(String baseDir, ExtEntity extEntity, Class entityClass) throws IOException {
		new ListMethodsImplAppender(baseDir, extEntity, entityClass).appendMethods();
	}

	// js methods
	public void generateListScript(String baseDir, String jsIncludeFilePath, String baseJsPath, ExtEntity extEntity, Class entityClass) throws IOException {
		new ListJsFileWriter(baseDir, extEntity, entityClass).createFile();
		new ListScriptIncludeAppender(jsIncludeFilePath, baseJsPath, extEntity, entityClass).appendJsInclude();
	}
	public void generateChooseScript(String baseDir, String jsIncludeFilePath, String baseJsPath, ExtEntity extEntity, Class entityClass) throws IOException {
		new ChooseJsFileWriter(baseDir, extEntity, entityClass).createFile();
		new ChooseScriptIncludeAppender(jsIncludeFilePath, baseJsPath, extEntity, entityClass).appendJsInclude();
	}

	// add list js button to main menu
	public void generateListMainMenuButton(String mainMenuJsPath, ExtEntity extEntity, Class entityClass) throws IOException {
		new ListMainMenuButtonAppender(mainMenuJsPath, extEntity, entityClass).appendMainMenuButton();
	}

	// get
	public void generateServiceGetMethod(String baseDir, ExtEntity extEntity, Class entityClass) throws IOException {
		new GetMethodsAppender(baseDir, extEntity, entityClass).appendMethods();
	}
	public void generateServiceBeanGetMethod(String baseDir, ExtEntity extEntity, Class entityClass) throws IOException {
		new GetMethodsImplAppender(baseDir, extEntity, entityClass).appendMethods();
	}

	public void generateGetServlet(String baseDir, ExtEntity extEntity, Class entityClass) throws IOException {
		new GetServletFileWriter(baseDir, extEntity, entityClass).createFile();
	}
	public void generateGetServletMapping(String webXmlPath, ExtEntity extEntity, Class entityClass) throws IOException {
		new GetServletMappingAppender(webXmlPath, extEntity, entityClass).appendServletMapping();
	}

	// create
	public void generateServiceCreateMethod(String baseDir, ExtEntity extEntity, Class entityClass) throws IOException {
		new CreateMethodAppender(baseDir, extEntity, entityClass).appendMethods();
	}
	public void generateServiceBeanCreateMethod(String baseDir, ExtEntity extEntity, Class entityClass) throws IOException {
		new CreateMethodImplAppender(baseDir, extEntity, entityClass).appendMethods();
	}

	public void generateCreateServlet(String baseDir, ExtEntity extEntity, Class entityClass) throws IOException {
		new CreateServletFileWriter(baseDir, extEntity, entityClass).createFile();
	}
	public void generateCreateServletMapping(String webXmlPath, ExtEntity extEntity, Class entityClass) throws IOException {
		new CreateServletMappingAppender(webXmlPath, extEntity, entityClass).appendServletMapping();
	}

	// update
	public void generateServiceUpdateMethod(String baseDir, ExtEntity extEntity, Class entityClass) throws IOException {
		new UpdateMethodAppender(baseDir, extEntity, entityClass).appendMethods();
	}
	public void generateServiceBeanUpdateMethod(String baseDir, ExtEntity extEntity, Class entityClass, Class addressClass) throws IOException {
		new UpdateMethodImplAppender(baseDir, extEntity, entityClass, addressClass).appendMethods();
	}

	public void generateUpdateServlet(String baseDir, ExtEntity extEntity, Class entityClass) throws IOException {
		new UpdateServletFileWriter(baseDir, extEntity, entityClass).createFile();
	}
	public void generateUpdateServletMapping(String webXmlPath, ExtEntity extEntity, Class entityClass) throws IOException {
		new UpdateServletMappingAppender(webXmlPath, extEntity, entityClass).appendServletMapping();
	}

	// delete
	public void generateServiceDeleteMethod(String baseDir, ExtEntity extEntity, Class entityClass) throws IOException {
		new DeleteMethodAppender(baseDir, extEntity, entityClass).appendMethods();
	}
	public void generateServiceBeanDeleteMethod(String baseDir, ExtEntity extEntity, Class entityClass) throws IOException {
		new DeleteMethodImplAppender(baseDir, extEntity, entityClass).appendMethods();
	}

	public void generateDeleteServlet(String baseDir, ExtEntity extEntity, Class entityClass) throws IOException {
		new DeleteServletFileWriter(baseDir, extEntity, entityClass).createFile();
	}
	public void generateDeleteServletMapping(String webXmlPath, ExtEntity extEntity, Class entityClass) throws IOException {
		new DeleteServletMappingAppender(webXmlPath, extEntity, entityClass).appendServletMapping();
	}

	// js methods for CRUD form
	public void generateFormScript(String baseDir, String jsIncludeFilePath, String baseJsPath, ExtEntity extEntity, Class entityClass) throws IOException {
		new FormJsFileWriter(baseDir, extEntity, entityClass).createFile();
		new FormScriptIncludeAppender(jsIncludeFilePath, baseJsPath, extEntity, entityClass).appendJsInclude();

		new FormLayoutJsFileWriter(baseDir, extEntity, entityClass).createFile();
		new FormLayoutScriptIncludeAppender(jsIncludeFilePath, baseJsPath, extEntity, entityClass).appendJsInclude();
	}

	// enum methods
	public void generateEnumHash(String filePath, EnumField enumField, Class enumClass) throws IOException {
		new EnumHashAppender(filePath, enumField, enumClass).appendEnumHash();
	}
	public void generateEnumStore(String filePath, EnumField enumField, Class enumClass) throws IOException {
		new EnumStoreAppender(filePath, enumField, enumClass).appendEnumStore();
	}
	public void generateEnumRenderer(String filePath, EnumField enumField, Class enumClass) throws IOException {
		new EnumRendererAppender(filePath, enumField, enumClass).appendEnumRenderer();
	}
	public void generateEnumCellRenderer(String baseDir, String rendererPropertiesFilePath, String renderersClassFilePath, EnumField enumField, Class enumClass) throws IOException {
		new EnumCellRendererFileWriter(baseDir, enumField, enumClass).createFile();
		new RendererPropertiesAppender(rendererPropertiesFilePath, enumField, enumClass).appendRenderer();
		new RenderersAppender(renderersClassFilePath, enumField, enumClass).appendRendererConstant();
	}

	//selenium tests
	public void generateFormPage(String baseDir, ExtEntity extEntity, Class entityClass) throws IOException {
		new FormPageFileWriter(baseDir, extEntity, entityClass).createFile();
	}
	public void generateMainPage(String baseDir, String mainMenuTitle) throws IOException {
		new MainPageFileWriter(baseDir, mainMenuTitle).createFile();
	}
	public void generateListPage(String baseDir, ExtEntity extEntity, Class entityClass) throws IOException {
		new ListPageFileWriter(baseDir, extEntity, entityClass).createFile();
		new MainPageAppender(baseDir, extEntity, entityClass).appendMainMenuButton();
	}
	public void generateChooseListPage(String baseDir, ExtEntity extEntity, Class entityClass) throws IOException {
		new ChooseListPageFileWriter(baseDir, extEntity, entityClass).createFile();
	}
	public void generateDataProvider(String baseDir, Class entityClass) throws IOException {
		new DataProviderFileWriter(baseDir, entityClass).createFile();
	}
	public void generateTest(String baseDir, Class entityClass) throws IOException {
		new TestFileWriter(baseDir, entityClass).createFile();
	}
	public void generateTestData(String baseDir, Class entityClass) throws IOException {
		new TestDataJsonSimpleFileWriter(baseDir, entityClass).createFile();
	}
}
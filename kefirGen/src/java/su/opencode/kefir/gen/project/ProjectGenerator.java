/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 20.03.2012 18:58:52$
*/
package su.opencode.kefir.gen.project;

import org.xml.sax.SAXException;
import su.opencode.kefir.gen.appender.service.ServiceReferenceAppender;
import su.opencode.kefir.gen.appender.serviceBean.ServiceBeanReferenceAppender;
import su.opencode.kefir.gen.appender.servlet.ServletClassMappingAppender;
import su.opencode.kefir.gen.fileWriter.GeneratorRunnerClassWriter;
import su.opencode.kefir.gen.fileWriter.RenderersFileWriter;
import su.opencode.kefir.gen.fileWriter.css.project.MainCssFileWriter;
import su.opencode.kefir.gen.fileWriter.js.project.*;
import su.opencode.kefir.gen.fileWriter.selenium.AddressFormPageFileWriter;
import su.opencode.kefir.gen.project.address.AddressClassFileWriter;
import su.opencode.kefir.gen.project.address.AddressJsReplacer;
import su.opencode.kefir.gen.project.properties.BuildPropertiesFileWriter;
import su.opencode.kefir.gen.project.properties.GeneratorPropertiesFileWriter;
import su.opencode.kefir.gen.project.properties.RendererPropertiesFileWriter;
import su.opencode.kefir.gen.project.sql.CreateTablesSqlFileWriter;
import su.opencode.kefir.gen.project.sql.DropTablesSqlFileWriter;
import su.opencode.kefir.gen.project.sql.appender.CreateAddressTableAppender;
import su.opencode.kefir.gen.project.sql.appender.DropAddressTableAppender;
import su.opencode.kefir.gen.project.xml.*;
import su.opencode.kefir.gen.project.xml.ant.CommonBuildXmlFileWriter;
import su.opencode.kefir.gen.project.xml.ant.SeleniumBuildXmlFileWriter;
import su.opencode.kefir.gen.project.xml.ant.SrvBuildXmlFileWriter;
import su.opencode.kefir.gen.project.xml.ant.WebBuildXmlFileWriter;
import su.opencode.kefir.gen.project.xml.html.ApplicationJspfFileWriter;
import su.opencode.kefir.gen.project.xml.html.EmptyXmlTagsFormatter;
import su.opencode.kefir.gen.project.xml.html.KefirIncludeJspfFileWriter;
import su.opencode.kefir.gen.project.xml.jsp.IndexJspFileWriter;
import su.opencode.kefir.gen.project.xml.orm.AddressMappingAppender;
import su.opencode.kefir.gen.project.xml.orm.OrmXmlFileWriter;
import su.opencode.kefir.srv.attachment.AttachmentService;
import su.opencode.kefir.srv.attachment.AttachmentServiceBean;
import su.opencode.kefir.srv.dynamicGrid.DynamicGridService;
import su.opencode.kefir.srv.dynamicGrid.DynamicGridServiceBean;
import su.opencode.kefir.web.GetGridParamsServlet;
import su.opencode.kefir.web.SetGridParamServlet;
import su.opencode.kefir.web.fileupload.AttachmentsListServlet;
import su.opencode.kefir.web.fileupload.DownloadServlet;
import su.opencode.kefir.web.fileupload.UploadServlet;

import java.io.IOException;

import static su.opencode.kefir.gen.project.ProjectConfigUtils.*;
import static su.opencode.kefir.util.FileUtils.*;
import static su.opencode.kefir.util.StringUtils.concat;

public class ProjectGenerator
{
	public ProjectGenerator(ProjectConfig config) {
		this.config = config;
	}
	public void generateProject() throws IOException, SAXException {
		createProjectDir();
		createCommonLibDir();
		createApplicationXml();
		createBuildProperties();
		createCommonBuildXml();

		createSrvModule();
		createWebModule();
		createSeleniumModule();
	}
	private void createProjectDir() {
		createDirs( getProjectFullPath(config) );
	}
	private void createCommonLibDir() {
		createDirs( getCommonLibFullPath(config) );
		copyAllFiles(concat(sb, config.libPath, FILE_SEPARATOR, "common"), getCommonLibFullPath(config));
	}
	private void createApplicationXml() throws IOException, SAXException {
		new ApplicationXmlFileWriter(getApplicationXmlFullPath(config), config).writeFile();
	}
	private void createBuildProperties() throws IOException {
		new BuildPropertiesFileWriter(getBuildPropertiesFullPath(config), config).writeFile();
	}
	private void createCommonBuildXml() throws IOException, SAXException {
		new CommonBuildXmlFileWriter(getCommonBuildXmlFullPath(config), config).writeFile();
	}

	private void createSrvModule() throws IOException, SAXException {
		createDirs( getSrvModuleFullPath(config) );
		createSrvLibDir();
		createSrvMetaInfDir();
		createSrvJavaSrcDir();
		createSrvSqlSrcDir();
		createSrvBuildXml();
	}
	private void createSrvBuildXml() throws IOException, SAXException {
		new SrvBuildXmlFileWriter(getSrvBuildXmlFullPath(config), config).writeFile();
	}
	private void createSrvLibDir() {
		createDirs( getSrvLibFullPath(config) );
		copyAllFiles(concat(sb, config.libPath, FILE_SEPARATOR, "srv"), getSrvLibFullPath(config));
	}
	private void createSrvMetaInfDir() throws IOException, SAXException {
		createDirs( getSrvMetaInfFullPath(config) );
		createEjbJarFile();
		createJbossXmlFile();
		createPersistenceXmlFile();

		createOrmXmlFile();
		appendAddressEntityMapping();
	}
	private void createEjbJarFile() throws IOException, SAXException {
		new EjbJarFileWriter( getEjbJarFullPath(config) ).writeFile();
	}
	private void createJbossXmlFile() throws IOException, SAXException {
		new JbossXmlFileWriter( getJbossXmlFullPath(config) ).writeFile();
	}
	private void createPersistenceXmlFile() throws IOException, SAXException {
		new PersistenceXmlFileWriter( getPersistenceXmlFullPath(config), config ).writeFile();
	}
	private void createOrmXmlFile() throws IOException, SAXException {
		new OrmXmlFileWriter( getOrmXmlFullPath(config), config ).writeFile();
	}
	private void appendAddressEntityMapping() throws IOException {
		new AddressMappingAppender( getOrmXmlFullPath(config), config ).appendEntityMapping();
	}

	private void createSrvJavaSrcDir() throws IOException {
		createDirs( getSrvJavaSrcFullPath(config) );
		createSrvBasePackage();
		createGeneratorPropertiesFile();
		createGeneratorRunnerClass();
		createRenderersPackage();

		createAddressClass();
	}
	private void createSrvBasePackage() {
		if ( config.srvBasePackage != null && !config.srvBasePackage.isEmpty() )
			createDirs( getSrvBasePackageFullPath(config) );
	}
	private void createGeneratorPropertiesFile() throws IOException {
		new GeneratorPropertiesFileWriter(getGeneratorPropertiesFullPath(config), config).writeFile();
	}
	private void createGeneratorRunnerClass() throws IOException {
		new GeneratorRunnerClassWriter(getSrvJavaSrcFullPath(config), config.srvBasePackage, GENERATOR_RUNNER_CLASS_NAME, config).createFile();
	}
	private void createRenderersPackage() throws IOException {
		createDirs( getRenderersPackageFullPath(config) );
		createRendererProperties();
		createRenderersClass();
	}
	private void createRendererProperties() throws IOException {
		new RendererPropertiesFileWriter(getRendererPropertiesFullPath(config), config).writeFile();
	}
	private void createRenderersClass() throws IOException {
		new RenderersFileWriter(getSrvJavaSrcFullPath(config), getRenderersPackage(config), RENDERERS_CLASS_NAME).createFile();
	}

	private void createAddressClass() throws IOException {
		new AddressClassFileWriter(getSrvJavaSrcFullPath(config), getAddressClassPackage(config), config).createFile();
		new AddressFormPageFileWriter(getSeleniumSrcPath(config), getAddressClassPackage(config)).createFile();
	}

	private void createSrvSqlSrcDir() throws IOException {
		createDirs( getSrvSqlSrcFullPath(config) );

		createCreateTablesSqlFile();
		appendCreateAddressTable();

		createDropTablesSqlFile();
		appendDropAddressTable();
	}
	private void createCreateTablesSqlFile() throws IOException {
		new CreateTablesSqlFileWriter(getSrvSqlSrcFullPath(config), "", CREATE_TABLES_SQL_FILE_NAME, config).createFile();
	}
	private void appendCreateAddressTable() throws IOException {
		new CreateAddressTableAppender(getCreateTablesSqlFullPath(config), config).appendCreateTable();
	}

	private void createDropTablesSqlFile() throws IOException {
		new DropTablesSqlFileWriter(getSrvSqlSrcFullPath(config), "", DROP_TABLES_SQL_FILE_NAME, config).createFile();
	}
	private void appendDropAddressTable() throws IOException {
		new DropAddressTableAppender(getDropTablesSqlFullPath(config), config).appendDropTable();
	}

	private void createWebModule() throws IOException, SAXException {
		createDirs( getWebModuleFullPath(config) );
		createWebLibDir();
		createWebJavaSrcDir();
		createWebDir();
		createWebBuildXml();
	}
	private void createWebBuildXml() throws IOException, SAXException {
		new WebBuildXmlFileWriter(getWebBuildXmlFullPath(config), config).writeFile();
	}
	private void createWebLibDir() {
		createDirs( getWebLibFullPath(config) );
		copyAllFiles(concat(sb, config.libPath, FILE_SEPARATOR, "web"), getWebLibFullPath(config));
	}
	private void createWebJavaSrcDir() {
		createDirs( getWebJavaSrcFullPath(config) );
		createWebBasePackage();
		// todo: log4j.properties
	}
	private void createWebBasePackage() {
		if ( config.webBasePackage != null && !config.webBasePackage.isEmpty() )
			createDirs( getWebBasePackageFullPath(config) );
	}

	private void createWebDir() throws IOException, SAXException {
		createDirs( getWebDirFullPath(config) );
		createWebInfDir();
		createCssDir();
		createJsDir();

		createApplicationJspfFile();
		createKefirIncludeJspfFile();
		createIndexJspFile();
	}
	private void createWebInfDir() throws IOException, SAXException {
		createDirs( getWebInfDirFullPath(config) );
		createWebXmlFile();
		createJbossWebXmlFile();
	}
	private void createWebXmlFile() throws IOException, SAXException {
		String webXmlPath = getWebXmlFullPath(config);
		new WebXmlFileWriter(webXmlPath, config ).writeFile();

		// servlet mappings for dynamic grid and attachments
		new ServletClassMappingAppender(webXmlPath, GetGridParamsServlet.class).appendServletMapping();
		new ServletClassMappingAppender(webXmlPath, SetGridParamServlet.class).appendServletMapping();
		new ServletClassMappingAppender(webXmlPath, UploadServlet.class).appendServletMapping();
		new ServletClassMappingAppender(webXmlPath, DownloadServlet.class).appendServletMapping();
		new ServletClassMappingAppender(webXmlPath, AttachmentsListServlet.class).appendServletMapping();

		// ejb local references for dynamic grid and attachments
		new ServiceReferenceAppender(webXmlPath, DynamicGridService.class).appendEjbLocalReference();
		new ServiceReferenceAppender(webXmlPath, AttachmentService.class).appendEjbLocalReference();
	}
	private void createJbossWebXmlFile() throws IOException, SAXException {
		String jbossWebXmlPath = getJbossWebXmlFullPath(config);
		new JbossWebXmlFileWriter(jbossWebXmlPath, config).writeFile();

		// ejb local references for dynamic grid and attachments
		new ServiceBeanReferenceAppender(jbossWebXmlPath, DynamicGridService.class, DynamicGridServiceBean.class).appendEjbLocalReference();
		new ServiceBeanReferenceAppender(jbossWebXmlPath, AttachmentService.class, AttachmentServiceBean.class).appendEjbLocalReference();
	}

	private void createCssDir() throws IOException {
		createDirs( getCssDirFullPath(config) );
		createMainCssFile();
	}
	private void createMainCssFile() throws IOException {
		new MainCssFileWriter(getCssDirFullPath(config), "", ProjectConfigUtils.MAIN_CSS_FILE_NAME).createFile();
	}

	private void createJsDir() throws IOException {
		createDirs( getJsDirFullPath(config) );
		createConstantsJsFile();
		createLocalStoresJsFile();
		createRenderersJsFile();
		createViewConfigsJsFile();
		createVtypesJsFile();

		createMainMenuDir();
		createAddressDir();
	}
	private void createConstantsJsFile() throws IOException {
		new ConstantsJsFileWriter( getJsDirFullPath(config), "", ProjectConfigUtils.CONSTANTS_FILE_NAME).createFile();
	}
	private void createLocalStoresJsFile() throws IOException {
		new LocalStoresJsFileWriter( getJsDirFullPath(config), "", ProjectConfigUtils.LOCAL_STORES_FILE_NAME).createFile();
	}
	private void createRenderersJsFile() throws IOException {
		new RenderersJsFileWriter( getJsDirFullPath(config), "", ProjectConfigUtils.RENDERERS_FILE_NAME).createFile();
	}
	private void createViewConfigsJsFile() throws IOException {
		new ViewConfigsJsFileWriter( getJsDirFullPath(config), "", ProjectConfigUtils.VIEW_CONFIGS_FILE_NAME).createFile();
	}
	private void createVtypesJsFile() throws IOException {
		new VtypesJsFileWriter( getJsDirFullPath(config), "", ProjectConfigUtils.VTYPES_FILE_NAME).createFile();
	}

	private void createMainMenuDir() throws IOException {
		createDirs( getMainMenuDirFullPath(config) );
		createMainMenuJsFile();
	}
	private void createMainMenuJsFile() throws IOException {
		new MainMenuJsFileWriter(config, getJsDirFullPath(config), MAIN_MENU_DIR_NAME, MAIN_MENU_JS_FILE_NAME).createFile();
	}

	private void createAddressDir() throws IOException {
		createDirs( getAddressDirFullPath(config) );
		createAddressJsFile();
	}
	private void createAddressJsFile() throws IOException {
		new AddressJsReplacer(getAddressTemplateJsFullPath(config), getAddressJsFullPath(config), config).createFile();
	}

	private void createKefirIncludeJspfFile() throws IOException, SAXException {
		String filePath = getKefirIncludeJspfFullPath(config);
		new KefirIncludeJspfFileWriter(filePath, config).writeFile();
		new EmptyXmlTagsFormatter(filePath).clearSpaces(); // удалить пробелы в <script> </script> тэгах
	}
	private void createApplicationJspfFile() throws IOException, SAXException {
		String filePath = getApplicationJspfFullPath(config);
		new ApplicationJspfFileWriter(filePath, config).writeFile();
		new EmptyXmlTagsFormatter(filePath).clearSpaces(); // удалить пробелы в <script> </script> тэгах
	}

	private void createIndexJspFile() throws IOException, SAXException {
		new IndexJspFileWriter( getWebDirFullPath(config), "", INDEX_JSP_FILE_NAME, config ).createFile();
	}

	private void createSeleniumModule() throws IOException, SAXException {
		createDirs(getSeleniumModuleFullPath(config));
		createSeleniumLibDir();
		createSeleniumJavaSrcTestDir();
		createSeleniumBuildXml();
	}
	private void createSeleniumJavaSrcTestDir() {
		createDirs(getSeleniumJavaSrcFullPath(config));
		createDirs(getSeleniumJavaTestFullPath(config));
	}
	private void createSeleniumLibDir() {
		final String seleniumLibFullPath = getSeleniumLibFullPath(config);
		createDirs(seleniumLibFullPath);
		copyAllFiles(concat(sb, config.libPath, FILE_SEPARATOR, "selenium"), seleniumLibFullPath);
	}
	private void createSeleniumBuildXml() throws IOException, SAXException {
		new SeleniumBuildXmlFileWriter(getSeleniumBuildXmlFullPath(config), config).writeFile();//todo доделать
	}

	private ProjectConfig config;

	protected StringBuffer sb = new StringBuffer();
}
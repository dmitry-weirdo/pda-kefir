/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.helper;

import su.opencode.kefir.gen.ExtEntity;
import su.opencode.kefir.gen.GeneratorService;
import su.opencode.kefir.gen.GeneratorServiceImpl;
import su.opencode.kefir.gen.field.enumField.EnumField;

import java.io.IOException;

import static su.opencode.kefir.gen.ExtEntityUtils.*;
import static su.opencode.kefir.gen.field.ExtEntityFieldsUtils.getEnumFieldAnnotation;
import static su.opencode.kefir.util.StringUtils.concat;

public class GeneratorServiceHelperImpl implements GeneratorServiceHelper
{
	public void generateLocalInterface(HelperConfig config) throws IOException {
		String packageName = getPackageName(config.serviceClassName);
		String serviceClassSimpleName = getSimpleName(config.serviceClassName);
		String serviceBeanClassSimpleName = concat(sb, serviceClassSimpleName, "Bean");
		String serviceReferenceName = serviceClassSimpleName;
		String serviceJndiName = serviceBeanClassSimpleName;

		service.generateLocalService(config.srvSrcDir, packageName, serviceClassSimpleName);
		service.generateLocalServiceBean(config.srvSrcDir, packageName, serviceBeanClassSimpleName, config.serviceClassName);
		service.appendLocalServiceReference(config.webXmlPath, config.serviceClassName, serviceReferenceName);
		service.appendLocalServiceBeanReference(config.jbossWebXmlPath, serviceReferenceName, serviceJndiName);
	}

	public void generateEntityMapping(HelperConfig config) throws IOException {
		ExtEntity extEntity = getExtEntity(config);

		service.appendOrmMapping(config.ormXmlPath, extEntity, config.entityClass);
		service.appendCreateTable(config.createTablesSqlPath, extEntity, config.entityClass);
		service.appendDropTable(config.dropTablesSqlPath, extEntity, config.entityClass);
	}


	public void generateEntityVO(HelperConfig config) throws IOException {
		ExtEntity extEntity = getExtEntity(config);
		service.generateVO(config.srvSrcDir, config.renderersClass, extEntity, config.entityClass);

		service.generateViewConfig(config.viewConfigsJsPath, config.entityClass);
		service.generateViewConfigStyles(config.mainCssPath, config.entityClass);
	}

	public void generateEntityList(HelperConfig config) throws IOException {
		ExtEntity extEntity = getExtEntity(config);

		// server part
		service.generateEntityQueryBuilder(config.srvSrcDir, extEntity, config.entityClass);
		service.generateEntityFilterConfig(config.srvSrcDir, extEntity, config.entityClass);
		service.generateServiceListMethods(config.srvSrcDir, extEntity, config.entityClass);
		service.generateServiceBeanListMethods(config.srvSrcDir, extEntity, config.entityClass);

		// servlet
		service.generateListServlet(config.webSrcDir, extEntity, config.entityClass);
		service.generateListServletMapping(config.webXmlPath, extEntity, config.entityClass);

		// list to excel servlet
		service.generateListToExcelServlet(config.webSrcDir, extEntity, config.entityClass, config.renderersClass);
		service.generateListToExcelServletMapping(config.webXmlPath, extEntity, config.entityClass);

		// js list script
		service.generateListScript(config.jsDir, config.jsIncludeFilePath, config.baseJsPath, extEntity, config.entityClass);

		// js choose script
		service.generateChooseScript(config.jsDir, config.jsIncludeFilePath, config.baseJsPath, extEntity, config.entityClass);

		// add js list button to main menu
		if (extEntity.hasListMainMenuButton())
			service.generateListMainMenuButton(config.mainMenuJsFilePath, extEntity, config.entityClass);
	}

	public void generateEntityServiceCrudMethods(HelperConfig config) throws IOException {
		ExtEntity extEntity = getExtEntity(config);

		// get method of service
		service.generateServiceGetMethod(config.srvSrcDir, extEntity, config.entityClass);
		service.generateServiceBeanGetMethod(config.srvSrcDir, extEntity, config.entityClass);

		// create method of service
		service.generateServiceCreateMethod(config.srvSrcDir, extEntity, config.entityClass);
		service.generateServiceBeanCreateMethod(config.srvSrcDir, extEntity, config.entityClass);

		// update method of service
		service.generateServiceUpdateMethod(config.srvSrcDir, extEntity, config.entityClass);
		service.generateServiceBeanUpdateMethod(config.srvSrcDir, extEntity, config.entityClass, config.addressClass);

		// delete method of service
		service.generateServiceDeleteMethod(config.srvSrcDir, extEntity, config.entityClass);
		service.generateServiceBeanDeleteMethod(config.srvSrcDir, extEntity, config.entityClass);
	}

	public void generateEntityCrud(HelperConfig config) throws IOException {
		ExtEntity extEntity = getExtEntity(config);

		// server part
		generateEntityServiceCrudMethods(config);

		// servlets
		// get servlet
		service.generateGetServlet(config.webSrcDir, extEntity, config.entityClass);
		service.generateGetServletMapping(config.webXmlPath, extEntity, config.entityClass);

		// create servlet
		service.generateCreateServlet(config.webSrcDir, extEntity, config.entityClass);
		service.generateCreateServletMapping(config.webXmlPath, extEntity, config.entityClass);

		// update servlet
		service.generateUpdateServlet(config.webSrcDir, extEntity, config.entityClass);
		service.generateUpdateServletMapping(config.webXmlPath, extEntity, config.entityClass);

		// delete servlet
		service.generateDeleteServlet(config.webSrcDir, extEntity, config.entityClass);
		service.generateDeleteServletMapping(config.webXmlPath, extEntity, config.entityClass);

		// js CRUD form
		service.generateFormScript(config.jsDir, config.jsIncludeFilePath, config.baseJsPath, extEntity, config.entityClass);
	}

	public void generateEnumJs(HelperConfig config) throws IOException {
		EnumField enumField = getEnumFieldAnnotation(config.enumClass);
		if (enumField == null)
			throw new IllegalArgumentException( concat(sb, "EnumField annotation not present for class ", config.enumClass.getName()) );

		service.generateEnumHash(config.constantsJsFilePath, enumField, config.enumClass);
		service.generateEnumStore(config.localStoresJsFilePath, enumField, config.enumClass);
		service.generateEnumRenderer(config.renderersJsFilePath, enumField, config.enumClass);
		service.generateEnumCellRenderer(config.srvSrcDir, config.rendererPropertiesFilePath, config.renderersClassFilePath, enumField, config.enumClass);
	}

	public void generateEntityTests(HelperConfig config) throws IOException {
		ExtEntity extEntity = getExtEntity(config);

		service.generateFormPage(config.seleniumSrcDir, extEntity, config.entityClass);
		service.generateListPage(config.seleniumSrcDir, extEntity, config.entityClass);
		service.generateChooseListPage(config.seleniumSrcDir, extEntity, config.entityClass);
		service.generateDataProvider(config.seleniumTestDir, config.entityClass);
		service.generateTest(config.seleniumTestDir, config.entityClass);
		service.generateTestData(config.seleniumTestDataDir, config.entityClass);
	}
	public void generateMainPage(HelperConfig config, String mainMenuTitle) throws IOException {
		service.generateMainPage(config.seleniumSrcDir, mainMenuTitle);
	}
	private ExtEntity getExtEntity(HelperConfig config) {
		ExtEntity extEntity = getExtEntityAnnotation(config.entityClass);
		if (extEntity == null)
			throw new IllegalArgumentException( concat(sb, "ExtEntity annotation not present for class ", config.entityClass.getName()) );

		return extEntity;
	}

	private StringBuffer sb = new StringBuffer();
	private GeneratorService service = new GeneratorServiceImpl();
}
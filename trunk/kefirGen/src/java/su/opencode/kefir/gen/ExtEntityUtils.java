/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen;

import org.apache.log4j.Logger;
import su.opencode.kefir.gen.field.AddressField;
import su.opencode.kefir.gen.field.ChooseField;
import su.opencode.kefir.gen.field.ComboBoxField;
import su.opencode.kefir.gen.field.SqlColumn;
import su.opencode.kefir.gen.field.button.SecondRowButton;
import su.opencode.kefir.gen.field.enumField.EnumField;
import su.opencode.kefir.gen.field.linkedEntity.PreventDeleteEntity;
import su.opencode.kefir.gen.field.searchField.FilterConfigField;
import su.opencode.kefir.gen.project.xml.orm.EntityMappingAppender;
import su.opencode.kefir.srv.json.LegendField;

import java.lang.reflect.Field;

import static su.opencode.kefir.gen.ExtEntity.*;
import static su.opencode.kefir.gen.field.ExtEntityFieldsUtils.*;
import static su.opencode.kefir.gen.project.xml.orm.EntityMappingAppender.ORM_SEQUENCE_NAME_POSTFIX;
import static su.opencode.kefir.util.StringUtils.*;

/**
 * Класс, достающий сведения из сущности ExtEntity
 * и преобразовывающий их в нужный вид.
 */
public class ExtEntityUtils
{
	@SuppressWarnings(value = "unchecked")
	public static boolean hasExtEntityAnnotation(Class entityClass) {
		return entityClass.getAnnotation(ExtEntity.class) != null;
	}

	/**
	 * @param entityClass класс сущности
	 * @return аннотация ExtEntity на указанном классе или <code>null</code>, если такой аннотации нет
	 */
	@SuppressWarnings(value = "unchecked")
	public static ExtEntity getExtEntityAnnotation(Class entityClass) {
		return (ExtEntity) entityClass.getAnnotation(ExtEntity.class);
	}

	/**
	 * @param varName имя переменной в camel-нотации
	 * @return имя переменной заглавными буквами через подчеркивание
	 */
	public static String getConstantName(String varName) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < varName.length(); i++)
		{
			char ch = varName.charAt(i);

			if ( i != 0 && Character.isUpperCase(ch) ) // перед первым символом в строке подчеркивание не ставится
				sb.append(CONSTANT_NAME_SEPARATOR).append(ch);
			else
				sb.append( Character.toUpperCase(ch) );
		}

		return sb.toString();
	}
	public static String getConstantName(Field field) {
		return getConstantName(field.getName());
	}

	public static Class getClass(String className) {
		try
		{
			return Class.forName(className);
		}
		catch (ClassNotFoundException e)
		{
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * @param entityClass полное имя класса
	 * @return simpleName класса с маленькой буквы
	 */
	public static String getSimpleName(Class entityClass) {
		return decapitalize( entityClass.getSimpleName() );
	}
	/**
	 * @param field поле
	 * @return simpleName класса поля с маленькой буквы
	 */
	public static String getSimpleName(Field field) {
		return getSimpleName(field.getType());
	}


	public static String getServiceClassName(ExtEntity extEntity, Class entityClass) {
		String serviceClassName;

		if ( extEntity.serviceClassName().isEmpty() )
		{
			String packageName = entityClass.getPackage().getName();
			String packageLastName = getSimpleName(packageName); // последняя часть пакета после точки
			serviceClassName = concat(packageName, PACKAGE_SEPARATOR, capitalize(packageLastName), "Service");
		}
		else
		{
			serviceClassName = extEntity.serviceClassName();
		}

		logger.info( concat("service className: ", serviceClassName) );
		return serviceClassName;
	}
	public static String getServiceClassSimpleName(ExtEntity extEntity, Class entityClass) {
		return getSimpleName( getServiceClassName(extEntity, entityClass) );
	}

	public static String getServiceReferenceName(ExtEntity extEntity, Class entityClass) {
		String serviceReferenceName;

		if ( extEntity.serviceReferenceName().isEmpty() )
		{
			serviceReferenceName = getServiceClassSimpleName(extEntity, entityClass);
		}
		else
		{
			serviceReferenceName = extEntity.serviceReferenceName();
		}

		logger.info( concat("service reference name: ", serviceReferenceName) );
		return serviceReferenceName;
	}

	public static String getServiceJndiName(ExtEntity extEntity, Class entityClass) {
		String serviceJndiName;

		if ( extEntity.serviceJndiName().isEmpty() )
		{
			serviceJndiName = getServiceBeanClassSimpleName(extEntity, entityClass);
		}
		else
		{
			serviceJndiName = extEntity.serviceJndiName();
		}

		logger.info( concat("service jndi name: ", serviceJndiName) );
		return serviceJndiName;
	}

	public static String getServiceBeanClassName(ExtEntity extEntity, Class entityClass) {
		String serviceClassName;

		if ( extEntity.serviceBeanClassName().isEmpty() )
		{
			String packageName = entityClass.getPackage().getName();
			String packageLastName = getSimpleName(packageName); // последняя часть пакета после точки
			serviceClassName = concat(packageName, PACKAGE_SEPARATOR, capitalize(packageLastName), "ServiceBean");
		}
		else
		{
			serviceClassName = extEntity.serviceBeanClassName();
		}

		logger.info( concat("service className: ", serviceClassName) );
		return serviceClassName;
	}
	public static String getServiceBeanClassSimpleName(ExtEntity extEntity, Class entityClass) {
		return getSimpleName( getServiceBeanClassName(extEntity, entityClass) );
	}

	public static String getFilterConfigClassName(ExtEntity extEntity, Class entityClass) {
		String filterConfigClassName;

		if ( extEntity.filterConfigClassName().isEmpty() )
			filterConfigClassName = concat(entityClass.getName(), "FilterConfig");
		else
			filterConfigClassName = extEntity.filterConfigClassName();

		logger.info( concat("filterConfig className: ", filterConfigClassName) );
		return filterConfigClassName;
	}
	public static String getFilterConfigClassSimpleName(ExtEntity extEntity, Class entityClass) {
		return getSimpleName( getFilterConfigClassName(extEntity, entityClass) );
	}

	public static String getServletPackageName(ExtEntity extEntity, Class entityClass) {
		String servletPackageName;

		if ( extEntity.servletPackageName().isEmpty() )
			servletPackageName = entityClass.getPackage().getName(); // по умолчанию берется пакет, аналогичный пакету, в котором лежит класс сущности
		else
			servletPackageName = extEntity.servletPackageName();

		logger.info( concat("servlet package name: ", servletPackageName) );
		return servletPackageName;
	}

	public static String getListMethodName(ExtEntity extEntity, Class entityClass) {
		String methodName;

		if ( extEntity.listMethodName().isEmpty() )
			methodName = concat("get", entityClass.getSimpleName(), "s");
		else
			methodName = extEntity.listMethodName();

		logger.info( concat("list method name: ", methodName) );
		return methodName;
	}

	public static String getCountMethodName(ExtEntity extEntity, Class entityClass) {
		String methodName;

		if ( extEntity.countMethodName().isEmpty() )
			methodName = concat("get", entityClass.getSimpleName(), "sCount");
		else
			methodName = extEntity.countMethodName();

		logger.info( concat("count method name: ", methodName) );
		return methodName;
	}

	public static String getGetMethodName(ExtEntity extEntity, Class entityClass) {
		String methodName;

		if ( extEntity.getMethodName().isEmpty() )
			methodName = concat("get", entityClass.getSimpleName());
		else
			methodName = extEntity.getMethodName();

		logger.info( concat("get method name: ", methodName) );
		return methodName;
	}
	public static String getGetVOMethodName(ExtEntity extEntity, Class entityClass) {
		String methodName;

		if ( extEntity.getVOMethodName().isEmpty() )
			methodName = concat("get", entityClass.getSimpleName(), "VO");
		else
			methodName = extEntity.getVOMethodName();

		logger.info( concat("get VO method name: ", methodName) );
		return methodName;
	}

	public static String getCreateMethodName(ExtEntity extEntity, Class entityClass) {
		String methodName;

		if ( extEntity.createMethodName().isEmpty() )
			methodName = concat("create", entityClass.getSimpleName());
		else
			methodName = extEntity.createMethodName();

		logger.info( concat("create method name: ", methodName) );
		return methodName;
	}

	public static String getUpdateMethodName(ExtEntity extEntity, Class entityClass) {
		String methodName;

		if ( extEntity.updateMethodName().isEmpty() )
			methodName = concat("update", entityClass.getSimpleName());
		else
			methodName = extEntity.updateMethodName();

		logger.info( concat("update method name: ", methodName) );
		return methodName;
	}

	public static String getDeleteMethodName(ExtEntity extEntity, Class entityClass) {
		String methodName;

		if ( extEntity.deleteMethodName().isEmpty() )
			methodName = concat("delete", entityClass.getSimpleName());
		else
			methodName = extEntity.deleteMethodName();

		logger.info( concat("delete method name: ", methodName) );
		return methodName;
	}


	public static String getListVOClassName(ExtEntity extEntity, Class entityClass) {
		String voClassName;

		if ( extEntity.listVoClassName().isEmpty() )
			voClassName = concat(entityClass.getName(), "VO");
		else
			voClassName = extEntity.listVoClassName();

		logger.info( concat("list VO className: ", voClassName) );
		return voClassName;
	}
	public static String getListVOClassSimpleName(ExtEntity extEntity, Class entityClass) {
		return getSimpleName( getListVOClassName(extEntity, entityClass) );
	}
	public static String getListVOClassPackageName(ExtEntity extEntity, Class entityClass) {
		return getPackageName( getListVOClassName(extEntity, entityClass) );
	}

	public static String getChooseVOClassName(ExtEntity extEntity, Class entityClass) {
		String voClassName;

		if ( extEntity.chooseVoClassName().isEmpty() )
			voClassName = getListVOClassName(extEntity, entityClass); // по умолчанию используется то же VO, что и для списка
		else
			voClassName = extEntity.chooseVoClassName();

		logger.info( concat("choose VO className: ", voClassName) );
		return voClassName;
	}
	public static String getChooseVOClassSimpleName(ExtEntity extEntity, Class entityClass) {
		return getSimpleName( getChooseVOClassName(extEntity, entityClass) );
	}

	public static String getQueryBuilderClassName(ExtEntity extEntity, Class entityClass) {
		String qbClassName;

		if ( extEntity.queryBuilderClassName().isEmpty() )
			qbClassName = concat(entityClass.getName(), "QueryBuilder");
		else
			qbClassName = extEntity.queryBuilderClassName();

		logger.info( concat("query builder className: ", qbClassName) );
		return qbClassName;
	}
	public static String getQueryBuilderClassSimpleName(ExtEntity extEntity, Class entityClass) {
		return getSimpleName( getQueryBuilderClassName(extEntity, entityClass) );
	}

	public static String getListServletClassName(ExtEntity extEntity, Class entityClass) {
		String servletClassName;

		if ( extEntity.listServletClassName().isEmpty() )
			servletClassName = concat( entityClass.getSimpleName(), "sListServlet" );
		else
			servletClassName = extEntity.listServletClassName();

		logger.info( concat("list servlet className: ", servletClassName) );
		return servletClassName;
	}
	public static String getListServletClassFullName(ExtEntity extEntity, Class entityClass) {
		return concat( getServletPackageName(extEntity, entityClass), PACKAGE_SEPARATOR, getListServletClassName(extEntity, entityClass)  );
	}
	public static String getListServletUrl(ExtEntity extEntity, Class entityClass) {
		String listServletUrl;

		if ( extEntity.listServletUrl().isEmpty() )
			listServletUrl = concat( SERVLET_URL_PREFIX, decapitalize(entityClass.getSimpleName()), "sList" );
		else
			listServletUrl = extEntity.listServletUrl();

		logger.info( concat("list servlet url: ", listServletUrl) );
		return listServletUrl;
	}
	public static String getListServletName(ExtEntity extEntity, Class entityClass) { // имя для web.xml
		return getListServletUrl(extEntity, entityClass).substring(1);
	}

	public static String getListExportToExcelServletClassName(ExtEntity extEntity, Class entityClass) {
		String servletClassName;

		if ( extEntity.listExportToExcelServletClassName().isEmpty() )
			servletClassName = concat( entityClass.getSimpleName(), "sListToExcelServlet" );
		else
			servletClassName = extEntity.listExportToExcelServletClassName();

		logger.info( concat("list export to Excel servlet className: ", servletClassName) );
		return servletClassName;
	}
	public static String getListExportToExcelServletClassFullName(ExtEntity extEntity, Class entityClass) {
		return concat( getServletPackageName(extEntity, entityClass), PACKAGE_SEPARATOR, getListExportToExcelServletClassName(extEntity, entityClass)  );
	}
	public static String getListExportToExcelServletUrl(ExtEntity extEntity, Class entityClass) {
		String url;

		if ( extEntity.listExportToExcelServletUrl().isEmpty() )
			url = concat( SERVLET_URL_PREFIX, decapitalize(entityClass.getSimpleName()), "sListToExcel" );
		else
			url = extEntity.listExportToExcelServletUrl();

		logger.info( concat("list export to Excel servlet url: ", url) );
		return url;
	}
	public static String getListExportToExcelServletName(ExtEntity extEntity, Class entityClass) { // имя для web.xml
		return getListExportToExcelServletUrl(extEntity, entityClass).substring(1);
	}
	public static String getListExportToExcelFileName(ExtEntity extEntity, Class entityClass) {
		String fileName;

		if ( extEntity.listExportToExcelFileName().isEmpty() )
			fileName = concat( getSimpleName(entityClass), "sList" );
		else
			fileName = extEntity.listExportToExcelFileName();

		logger.info( concat("list export to Excel xls file name: ", fileName) );
		return fileName;
	}

	public static String getChooseServletUrl(ExtEntity extEntity, Class entityClass) {
		String chooseServletUrl;

		if ( extEntity.chooseServletUrl().isEmpty() )
			chooseServletUrl = getListServletUrl(extEntity, entityClass); // по умолчанию используется тот же сервлет, что и для списка сущностей
		else
			chooseServletUrl = extEntity.chooseServletUrl();

		logger.info( concat("choose servlet url: ", chooseServletUrl) );
		return chooseServletUrl;
	}

	public static String getGetServletClassName(ExtEntity extEntity, Class entityClass) {
		String servletClassName;

		if ( extEntity.getServletClassName().isEmpty() )
			servletClassName = concat( entityClass.getSimpleName(), "GetServlet" );
		else
			servletClassName = extEntity.getServletClassName();

		logger.info( concat("get servlet className: ", servletClassName) );
		return servletClassName;
	}
	public static String getGetServletClassFullName(ExtEntity extEntity, Class entityClass) {
		return concat( getServletPackageName(extEntity, entityClass), PACKAGE_SEPARATOR, getGetServletClassName(extEntity, entityClass)  );
	}
	public static String getGetServletUrl(ExtEntity extEntity, Class entityClass) {
		String servletUrl;

		if ( extEntity.getServletUrl().isEmpty() )
			servletUrl = concat( SERVLET_URL_PREFIX, decapitalize(entityClass.getSimpleName()), "Get" );
		else
			servletUrl = extEntity.getServletUrl();

		logger.info( concat("get servlet url: ", servletUrl) );
		return servletUrl;
	}
	public static String getGetServletName(ExtEntity extEntity, Class entityClass) { // имя для web.xml
		return getGetServletUrl(extEntity, entityClass).substring(1);
	}

	public static String getCreateServletClassName(ExtEntity extEntity, Class entityClass) {
		String servletClassName;

		if ( extEntity.createServletClassName().isEmpty() )
			servletClassName = concat( entityClass.getSimpleName(), "CreateServlet" );
		else
			servletClassName = extEntity.createServletClassName();

		logger.info( concat("create servlet className: ", servletClassName) );
		return servletClassName;
	}
	public static String getCreateServletClassFullName(ExtEntity extEntity, Class entityClass) {
		return concat( getServletPackageName(extEntity, entityClass), PACKAGE_SEPARATOR, getCreateServletClassName(extEntity, entityClass)  );
	}
	public static String getCreateServletUrl(ExtEntity extEntity, Class entityClass) {
		String servletUrl;

		if ( extEntity.createServletUrl().isEmpty() )
			servletUrl = concat( SERVLET_URL_PREFIX, decapitalize(entityClass.getSimpleName()), "Create" );
		else
			servletUrl = extEntity.createServletUrl();

		logger.info( concat("create servlet url: ", servletUrl) );
		return servletUrl;
	}
	public static String getCreateServletName(ExtEntity extEntity, Class entityClass) { // имя для web.xml
		return getCreateServletUrl(extEntity, entityClass).substring(1);
	}

	public static String getUpdateServletClassName(ExtEntity extEntity, Class entityClass) {
		String servletClassName;

		if ( extEntity.updateServletClassName().isEmpty() )
			servletClassName = concat( entityClass.getSimpleName(), "UpdateServlet" );
		else
			servletClassName = extEntity.updateServletClassName();

		logger.info( concat("update servlet className: ", servletClassName) );
		return servletClassName;
	}
	public static String getUpdateServletClassFullName(ExtEntity extEntity, Class entityClass) {
		return concat( getServletPackageName(extEntity, entityClass), PACKAGE_SEPARATOR, getUpdateServletClassName(extEntity, entityClass)  );
	}
	public static String getUpdateServletUrl(ExtEntity extEntity, Class entityClass) {
		String servletUrl;

		if ( extEntity.updateServletUrl().isEmpty() )
			servletUrl = concat( SERVLET_URL_PREFIX, decapitalize(entityClass.getSimpleName()), "Update" );
		else
			servletUrl = extEntity.updateServletUrl();

		logger.info( concat("update servlet url: ", servletUrl) );
		return servletUrl;
	}
	public static String getUpdateServletName(ExtEntity extEntity, Class entityClass) { // имя для web.xml
		return getUpdateServletUrl(extEntity, entityClass).substring(1);
	}

	public static String getDeleteServletClassName(ExtEntity extEntity, Class entityClass) {
		String servletClassName;

		if ( extEntity.deleteServletClassName().isEmpty() )
			servletClassName = concat( entityClass.getSimpleName(), "DeleteServlet" );
		else
			servletClassName = extEntity.deleteServletClassName();

		logger.info( concat("delete servlet className: ", servletClassName) );
		return servletClassName;
	}
	public static String getDeleteServletClassFullName(ExtEntity extEntity, Class entityClass) {
		return concat( getServletPackageName(extEntity, entityClass), PACKAGE_SEPARATOR, getDeleteServletClassName(extEntity, entityClass)  );
	}
	public static String getDeleteServletUrl(ExtEntity extEntity, Class entityClass) {
		String servletUrl;

		if ( extEntity.deleteServletUrl().isEmpty() )
			servletUrl = concat( SERVLET_URL_PREFIX, decapitalize(entityClass.getSimpleName()), "Delete" );
		else
			servletUrl = extEntity.deleteServletUrl();

		logger.info( concat("create servlet url: ", servletUrl) );
		return servletUrl;
	}
	public static String getDeleteServletName(ExtEntity extEntity, Class entityClass) { // имя для web.xml
		return getDeleteServletUrl(extEntity, entityClass).substring(1);
	}


	public static String getSimpleName(String fullName) {
		return fullName.substring(fullName.lastIndexOf(PACKAGE_SEPARATOR) + 1);
	}
	public static String getFullName(String packageName, String classSimpleName) {
		return concat(packageName, PACKAGE_SEPARATOR, classSimpleName);
	}
	public static String getPackageName(String classFullName) {
		return classFullName.substring(0, classFullName.lastIndexOf(PACKAGE_SEPARATOR));
	}
	public static String getLastPackageLevelName(String classFullName) {
		return getSimpleName(getPackageName(classFullName));
	}

	public static boolean samePackage(String classFullName1, String classFullName2) {
		return getPackageName(classFullName1).equals(getPackageName(classFullName2));
	}
	public static boolean isInPackage(String classFullName, String packageName) {
		return getPackageName(classFullName).equals(packageName);
	}

	// js-files
	public static String getJsDirectory(ExtEntity extEntity, Class entityClass) {
		String jsDirectory;

		if ( extEntity.jsDirectory().isEmpty() )
			jsDirectory = getLastPackageLevelName(entityClass.getName());
		else
			jsDirectory = extEntity.jsDirectory();

		logger.info( concat("js files directory: ", jsDirectory) );
		return jsDirectory;
	}

	public static String getJsNamespace(ExtEntity extEntity, Class entityClass) {
		String jsNamespace;

		if ( extEntity == null || extEntity.jsNamespace().isEmpty() )
			jsNamespace = concat(entityClass.getPackage().getName(), PACKAGE_SEPARATOR, decapitalize(entityClass.getSimpleName()));
		else
			jsNamespace = extEntity.jsNamespace();

		logger.info( concat("js namespace: ", jsNamespace) );
		return jsNamespace;
	}

	public static String getJsFieldPrefix(ExtEntity extEntity, Class entityClass) {
		String jsFieldPrefix;

		if ( extEntity.jsFieldPrefix().isEmpty() )
			jsFieldPrefix = getSimpleName(entityClass);
		else
			jsFieldPrefix = extEntity.jsFieldPrefix();

		logger.info( concat("js prefix: ", jsFieldPrefix) );
		return jsFieldPrefix;
	}

	public static String getListJsFileName(ExtEntity extEntity, Class entityClass) {
		String fileName;

		if ( extEntity.listJsFileName().isEmpty() )
			fileName = concat( decapitalize( entityClass.getSimpleName() ), "sList" );
		else
			fileName = extEntity.listJsFileName();

		logger.info( concat("js list file name: ", fileName) );
		return fileName;
	}

	public static String getListJsNamespace(ExtEntity extEntity, Class entityClass) {
		String listJsNamespace;

		if ( extEntity.listJsNamespace().isEmpty() )
			listJsNamespace = concat(entityClass.getSimpleName(), "sList" );
		else
			listJsNamespace = extEntity.listJsNamespace();

		logger.info( concat("list js namespace: ", listJsNamespace) );
		return listJsNamespace;
	}
	public static String getListJsFullNamespace(ExtEntity extEntity, Class entityClass) {
		return concat( getJsNamespace(extEntity, entityClass), PACKAGE_SEPARATOR, getListJsNamespace(extEntity, entityClass) );
	}
	public static String getListInitFunctionFullName(ExtEntity extEntity, Class entityClass) {
		return concat( getListJsFullNamespace(extEntity, entityClass), PACKAGE_SEPARATOR, extEntity.listInitFunctionName() );
	}

	public static String getListWindowId(ExtEntity extEntity, Class entityClass) {
		String windowId;

		if ( extEntity.listWindowId().isEmpty() )
			windowId = concat( getSimpleName(entityClass), "sWindow" );
		else
			windowId = extEntity.listWindowId();

		logger.info( concat("list window id: ", windowId) );
		return windowId;
	}

	public static String getListGridPanelId(ExtEntity extEntity, Class entityClass) {
		String gridPanelId;

		if ( extEntity.listGridPanelId().isEmpty() )
			gridPanelId = concat( decapitalize(entityClass.getSimpleName()), "sGrid" );
		else
			gridPanelId = extEntity.listGridPanelId();

		logger.info( concat("list gridPanel id: ", gridPanelId) );
		return gridPanelId;
	}

	public static String getListCreateButtonId(ExtEntity extEntity, Class entityClass) {
		String buttonId;

		if ( extEntity.listCreateButtonId().isEmpty() )
			buttonId = concat( decapitalize(entityClass.getSimpleName()), "sList-createButton" );
		else
			buttonId = extEntity.listCreateButtonId();

		logger.info( concat("list create button id: ", buttonId) );
		return buttonId;
	}

	public static String getListShowButtonId(ExtEntity extEntity, Class entityClass) {
		String buttonId;

		if ( extEntity.listShowButtonId().isEmpty() )
			buttonId = concat( decapitalize(entityClass.getSimpleName()), "sList-showButton" );
		else
			buttonId = extEntity.listShowButtonId();

		logger.info( concat("list show button id: ", buttonId) );
		return buttonId;
	}

	public static String getListUpdateButtonId(ExtEntity extEntity, Class entityClass) {
		String buttonId;

		if ( extEntity.listUpdateButtonId().isEmpty() )
			buttonId = concat( decapitalize(entityClass.getSimpleName()), "sList-updateButton" );
		else
			buttonId = extEntity.listUpdateButtonId();

		logger.info( concat("list update button id: ", buttonId) );
		return buttonId;
	}

	public static String getListDeleteButtonId(ExtEntity extEntity, Class entityClass) {
		String buttonId;

		if ( extEntity.listDeleteButtonId().isEmpty() )
			buttonId = concat( decapitalize(entityClass.getSimpleName()), "sList-deleteButton" );
		else
			buttonId = extEntity.listDeleteButtonId();

		logger.info( concat("list delete button id: ", buttonId) );
		return buttonId;
	}

	public static String getListExportToExcelButtonId(ExtEntity extEntity, Class entityClass) {
		String buttonId;

		if ( extEntity.listExportToExcelButtonId().isEmpty() )
			buttonId = concat( decapitalize(entityClass.getSimpleName()), "sList-exportToExcelButton" );
		else
			buttonId = extEntity.listExportToExcelButtonId();

		logger.info( concat("list export to Excel button id: ", buttonId) );
		return buttonId;
	}

	public static String getListCloseButtonId(ExtEntity extEntity, Class entityClass) {
		String buttonId;

		if ( extEntity.listCloseButtonId().isEmpty() )
			buttonId = concat( decapitalize(entityClass.getSimpleName()), "sList-closeButton" );
		else
			buttonId = extEntity.listCloseButtonId();

		logger.info( concat("list close button id: ", buttonId) );
		return buttonId;
	}


	// ------------------------------  форма выбора -------------------------------------------
	public static String getChooseJsFileName(ExtEntity extEntity, Class entityClass) {
		String fileName;

		if ( extEntity == null || extEntity.chooseJsFileName().isEmpty() )
			fileName = concat( decapitalize( entityClass.getSimpleName() ), "Choose" );
		else
			fileName = extEntity.chooseJsFileName();

		logger.info( concat("js choose file name: ", fileName) );
		return fileName;
	}

	public static String getChooseJsNamespace(ExtEntity extEntity, Class entityClass) {
		String chooseJsNamespace;

		if ( extEntity == null || extEntity.chooseJsNamespace().isEmpty() )
			chooseJsNamespace = concat(entityClass.getSimpleName(), "Choose");
		else
			chooseJsNamespace = extEntity.chooseJsNamespace();

		logger.info( concat("choose js namespace: ", chooseJsNamespace) );
		return chooseJsNamespace;
	}

	public static String getChooseJsFullNamespace(ExtEntity extEntity, Class entityClass) {
		return concat( getJsNamespace(extEntity, entityClass), PACKAGE_SEPARATOR, getChooseJsNamespace(extEntity, entityClass) );
	}

	public static String getChooseInitFunctionName(ExtEntity extEntity) {
		return extEntity == null ? DEFAULT_CHOOSE_INIT_FUNCTION_NAME : extEntity.chooseInitFunctionName();
	}
	public static String getChooseInitConfigSuccessHandlerParamName(ExtEntity extEntity) {
		return extEntity == null ? DEFAULT_SUCCESS_HANDLER_PARAM_NAME : extEntity.chooseInitConfigSuccessHandlerParamName();
	}
	public static String getFormShowFunctionName(ExtEntity extEntity) {
		return extEntity == null ? DEFAULT_FORM_SHOW_FUNCTION_NAME : extEntity.formShowFunctionName();
	}
	public static String getFormConfigEntityIdParamName(ExtEntity extEntity) {
		return extEntity == null ? DEFAULT_ENTITY_ID_PARAM_NAME : extEntity.formConfigEntityIdParamName();
	}

	public static String getChooseWindowId(ExtEntity extEntity, Class entityClass) {
		String windowId;

		if ( extEntity.chooseWindowId().isEmpty() )
			windowId = concat( decapitalize(entityClass.getSimpleName()), "ChooseWindow");
		else
			windowId = extEntity.chooseWindowId();

		logger.info( concat("list window id: ", windowId) );
		return windowId;
	}

	public static String getChooseGridPanelId(ExtEntity extEntity, Class entityClass) {
		String gridPanelId;

		if ( extEntity.chooseGridPanelId().isEmpty() )
			gridPanelId = concat( decapitalize(entityClass.getSimpleName()), "ChooseGrid");
		else
			gridPanelId = extEntity.chooseGridPanelId();

		logger.info( concat("choose gridPanel id: ", gridPanelId) );
		return gridPanelId;
	}

	public static String getChooseChooseButtonId(ExtEntity extEntity, Class entityClass) {
		String buttonId;

		if ( extEntity.chooseChooseButtonId().isEmpty() )
			buttonId = concat( decapitalize(entityClass.getSimpleName()), "Choose-chooseButton");
		else
			buttonId = extEntity.chooseChooseButtonId();

		logger.info( concat("choose choose button id: ", buttonId) );
		return buttonId;
	}
	public static String getChooseShowButtonId(ExtEntity extEntity, Class entityClass) {
		String buttonId;

		if ( extEntity.chooseShowButtonId().isEmpty() )
			buttonId = concat( decapitalize(entityClass.getSimpleName()), "Choose-showButton");
		else
			buttonId = extEntity.chooseShowButtonId();

		logger.info( concat("choose show button id: ", buttonId) );
		return buttonId;
	}
	public static String getChooseCreateButtonId(ExtEntity extEntity, Class entityClass) {
		String buttonId;

		if ( extEntity.chooseCreateButtonId().isEmpty() )
			buttonId = concat( decapitalize(entityClass.getSimpleName()), "Choose-createButton");
		else
			buttonId = extEntity.chooseCreateButtonId();

		logger.info( concat("choose create button id: ", buttonId) );
		return buttonId;
	}
	public static String getChooseCancelButtonId(ExtEntity extEntity, Class entityClass) {
		String buttonId;

		if ( extEntity.chooseCancelButtonId().isEmpty() )
			buttonId = concat( decapitalize(entityClass.getSimpleName()), "Choose-cancelButton");
		else
			buttonId = extEntity.chooseCancelButtonId();

		logger.info( concat("choose cancel button id: ", buttonId) );
		return buttonId;
	}

	// ------------------------------  форма CRUD ---------------------------------------------
	public static String getFormLayoutJsFileName(ExtEntity extEntity, Class entityClass) {
		String fileName;

		if ( extEntity.formLayoutJsFileName().isEmpty() )
			fileName = concat( decapitalize( entityClass.getSimpleName() ), "FormLayout" );
		else
			fileName = extEntity.formLayoutJsFileName();

		logger.info( concat("js form file name: ", fileName) );
		return fileName;
	}
	public static String getGetFormItemsLayoutFunctionFullName(ExtEntity extEntity, Class entityClass) {
		return concat( getJsNamespace(extEntity, entityClass), PACKAGE_SEPARATOR, extEntity.getFormItemsLayoutFunctionName() );
	}

	public static String getFormJsFileName(ExtEntity extEntity, Class entityClass) {
		String fileName;

		if ( extEntity.formJsFileName().isEmpty() )
			fileName = concat( decapitalize( entityClass.getSimpleName() ), "Form" );
		else
			fileName = extEntity.formJsFileName();

		logger.info( concat("js form file name: ", fileName) );
		return fileName;
	}

	public static String getFormJsNamespaceFullName(ExtEntity extEntity, Class entityClass) {
		return concat( getJsNamespace(extEntity, entityClass), PACKAGE_SEPARATOR, getFormJsNamespace(extEntity, entityClass) );
	}

	public static String getFormJsNamespace(ExtEntity extEntity, Class entityClass) {
		String formJsNamespace;

		if ( extEntity == null || extEntity.formJsNamespace().isEmpty() )
			formJsNamespace = entityClass.getSimpleName();
		else
			formJsNamespace = extEntity.formJsNamespace();

		logger.info( concat("form js namespace: ", formJsNamespace) );
		return formJsNamespace;
	}

	public static String getFormJsFullNamespace(ExtEntity extEntity, Class entityClass) {
		return concat(getJsNamespace(extEntity, entityClass), PACKAGE_SEPARATOR, getFormJsNamespace(extEntity, entityClass) );
	}


	public static String getFormWindowId(ExtEntity extEntity, Class entityClass) {
		String windowId;

		if ( extEntity.formWindowId().isEmpty() )
			windowId = concat( getSimpleName(entityClass), "FormWindow");
		else
			windowId = extEntity.formWindowId();

		logger.info( concat("form window id: ", windowId) );
		return windowId;
	}

	public static String getFormSaveButtonId(ExtEntity extEntity, Class entityClass) {
		String buttonId;

		if ( extEntity.formSaveButtonId().isEmpty() )
			buttonId = concat( getJsFieldPrefix(extEntity, entityClass), JS_FIELD_NAME_SEPARATOR, "save" );
		else
			buttonId = extEntity.formSaveButtonId();

		logger.info( concat("form save button id: ", buttonId) );
		return buttonId;
	}

	public static String getCreateCancelButtonId(ExtEntity extEntity, Class entityClass) {
		String buttonId;

		if ( extEntity.createCancelButtonId().isEmpty() )
			buttonId = concat( getJsFieldPrefix(extEntity, entityClass), JS_FIELD_NAME_SEPARATOR, "cancel" );
		else
			buttonId = extEntity.createCancelButtonId();

		logger.info( concat("form create cancel button id: ", buttonId) );
		return buttonId;
	}
	public static String getShowCancelButtonId(ExtEntity extEntity, Class entityClass) {
		String buttonId;

		if ( extEntity.showCancelButtonId().isEmpty() )
			buttonId = concat( getJsFieldPrefix(extEntity, entityClass), JS_FIELD_NAME_SEPARATOR, "close" );
		else
			buttonId = extEntity.showCancelButtonId();

		logger.info( concat("form show cancel button id: ", buttonId) );
		return buttonId;
	}
	public static String getUpdateCancelButtonId(ExtEntity extEntity, Class entityClass) {
		String buttonId;

		if ( extEntity.updateCancelButtonId().isEmpty() )
			buttonId = concat( getJsFieldPrefix(extEntity, entityClass), JS_FIELD_NAME_SEPARATOR, "cancel" );
		else
			buttonId = extEntity.updateCancelButtonId();

		logger.info( concat("form update cancel button id: ", buttonId) );
		return buttonId;
	}
	public static String getDeleteCancelButtonId(ExtEntity extEntity, Class entityClass) {
		String buttonId;

		if ( extEntity.deleteCancelButtonId().isEmpty() )
			buttonId = concat( getJsFieldPrefix(extEntity, entityClass), JS_FIELD_NAME_SEPARATOR, "cancel" );
		else
			buttonId = extEntity.deleteCancelButtonId();

		logger.info( concat("form delete cancel button id: ", buttonId) );
		return buttonId;
	}

	public static String getFormEntityVarName(ExtEntity extEntity, Class entityClass) {
		String varName;

		if ( extEntity.formEntityVarName().isEmpty() )
			varName = getSimpleName(entityClass);
		else
			varName = extEntity.formEntityVarName();

		logger.info( concat("form entity var name: ", varName) );
		return varName;
	}

	// --------------------------------------- SecondRowButtons ------------------------------
	public static boolean hasListSecondRowButtons(ExtEntity extEntity) {
		return extEntity.listSecondRowButtons().length > 0;
	}
	public static String getSecondRowButtonName(SecondRowButton secondRowButton) {
		String name;

		if ( !secondRowButton.name().isEmpty() )
		{ // имя задано явно
			name = secondRowButton.name();
		}
		else if ( !secondRowButton.listEntityClassName().isEmpty() )
		{ // задана связанная сущность -> назвать кнопку (краткое имя связанной сущности с маленькой буквы + "Button")
			name = concat( decapitalize( getSimpleName(secondRowButton.listEntityClassName()) ), "sButton");
		}
		else
		{
			throw new IllegalArgumentException("Neither name nor listEntityClassName defined for secondRowButton");
		}

		logger.info( concat("second row button name: ", name) );
		return name;
	}
	public static String getSecondRowButtonId(SecondRowButton secondRowButton, Class entityClass) {
		String buttonId;

		if ( secondRowButton.id().isEmpty() )
			buttonId = concat( decapitalize(entityClass.getSimpleName()), "sList", JS_FIELD_NAME_SEPARATOR, getSecondRowButtonName(secondRowButton) );
		else
			buttonId = secondRowButton.id();

		logger.info( concat("second row button id: ", buttonId) );
		return buttonId;
	}
	public static String getSecondRowButtonListEntityParamName(SecondRowButton secondRowButton, Class entityClass) {
		String paramName;

		if ( secondRowButton.listEntityParamName().isEmpty() )
			paramName = getSimpleName(entityClass); // имя класса основной сущности с маленькой буквы
		else
			paramName = secondRowButton.listEntityParamName();

		logger.info( concat("second row button listEntityParamName: ", paramName) );
		return paramName;
	}
	public static String getListFilterParamName(Field filterField, Class filterFieldClass, Class listEntityClass) {
		String paramName = decapitalize(filterField.getName()); // по умолчанию - имя поля, которое передается в фильтр, с маленькой буквы

		// просмотреть filterConfig в сущности, список которой открывается при переходе
		for (Field field : getFilterConfigFields(listEntityClass))
		{
			if ( field.getType().getName().equals(filterFieldClass.getName()) )
			{ // есть filterConfig по полю того же класса, что и параметр фильтрации — взять имя параметра оттуда
				FilterConfigField filterConfigField = getFilterConfigFieldAnnotation(field);
				paramName = getFilterConfigListInitFunctionParamName(filterConfigField, field);
				break;
			}
		}

		logger.info( concat("filter by ", filterFieldClass.getName(), " in list of ", listEntityClass.getName(), " second row button list filter param name: ", paramName) );
		return paramName;
	}

	// --------------------------------------- PreventDeleteEntities ------------------------------
	public static boolean hasPreventDeleteEntities(ExtEntity extEntity) {
		return extEntity.preventDeleteEntities().length > 0;
	}
	public static Class getPreventDeleteEntityClass(PreventDeleteEntity preventDeleteEntity) {
		return getClass(preventDeleteEntity.className());
	}
	public static ExtEntity getPreventDeleteEntityExtEntityAnnotation(PreventDeleteEntity preventDeleteEntity) {
		Class entityClass = getPreventDeleteEntityClass(preventDeleteEntity);
		if ( !hasExtEntityAnnotation(entityClass) )
			throw new IllegalArgumentException( concat("@ExtEntity annotation not present for @PreventDeleteEntity class ", entityClass ) );

		return getExtEntityAnnotation(entityClass);
	}
	public static String getPreventDeleteEntityFilterConfigFieldName(PreventDeleteEntity preventDeleteEntity, Class entityClass) {
		String paramName;

		if ( preventDeleteEntity.filterConfigFieldName().isEmpty() )
			paramName = concat( getSimpleName(entityClass), "Id" ) ; // имя класса основной сущности с маленькой буквы + "Id"
		else
			paramName = preventDeleteEntity.filterConfigFieldName();

		logger.info( concat("preventDeleteEntity filterConfigFieldName: ", paramName) );
		return paramName;
	}

	// ------------------------------------ main menu list button -----------------------------
	public static String getListMainMenuButtonId(ExtEntity extEntity, Class entityClass) {
		String buttonId = getValue(extEntity.listMainMenuButtonId(), concat(getSimpleName(entityClass), "-list-mainMenu"));

		logger.info( concat("list main menu button id: ", buttonId) );
		return buttonId;
	}
	public static String getListMainMenuButtonText(ExtEntity extEntity, Class entityClass) {
		String buttonText = getValue(extEntity.listMainMenuButtonText(), extEntity.listWindowTitle());

		logger.info( concat("list main menu button text: ", buttonText) );
		return buttonText;
	}
	public static String getListMainMenuButtonToolTip(ExtEntity extEntity, Class entityClass) {
		String buttonText = getValue(extEntity.listMainMenuButtonToolTip(), null); // равный тексту по дефолту подставится в js.

		logger.info( concat("list main menu button tooltip: ", buttonText) );
		return buttonText;
	}

	// ------------------------------------ selenium tests -----------------------------
	public static String getSeleniumPagesPackageName(Class entityClass) {
		final String packageName = concat(entityClass.getPackage().getName(), ".selenium.pages");
		logger.debug( concat("selenium package name: ", packageName) );

		return packageName;
	}
	public static String getSeleniumFormPageClassName(Class entityClass) {
		final String pageClassName = concat(entityClass.getSimpleName(), "FormPage");
		logger.debug( concat("selenium FormPage class name: ", pageClassName) );

		return pageClassName;
	}
	public static String getSeleniumListPageClassName(Class entityClass) {
		final String pageClassName = concat(entityClass.getSimpleName(), "ListPage");
		logger.debug( concat("selenium ListPage class name: ", pageClassName) );

		return pageClassName;
	}
	public static String getSeleniumChooseListPageClassName(Class entityClass) {
		final String pageClassName = concat(entityClass.getSimpleName(), "ChooseListPage");
		logger.debug( concat("selenium ChooseListPage class name: ", pageClassName) );

		return pageClassName;
	}
	public static String getSeleniumDataProviderPackageName(Class entityClass) {
		final String packageName = concat(entityClass.getPackage().getName(), ".selenium.dataprovider");
		logger.debug( concat("selenium package name: ", packageName) );

		return packageName;
	}
	public static String getSeleniumDataProviderClassName(Class entityClass) {
		final String pageClassName = concat(entityClass.getSimpleName(), "DataProvider");
		logger.debug( concat("selenium DataProvider class name: ", pageClassName) );

		return pageClassName;
	}
	public static String getSeleniumTestPackageName(Class entityClass) {
		final String packageName = concat(entityClass.getPackage().getName(), ".selenium.tests");
		logger.debug( concat("selenium package name: ", packageName) );

		return packageName;
	}
	public static String getSeleniumTestClassName(Class entityClass) {
		final String pageClassName = concat(entityClass.getSimpleName(), "Tests");
		logger.debug( concat("selenium Tests class name: ", pageClassName) );

		return pageClassName;
	}
	public static String getSeleniumMainPagePackage() {
		final String className = "su.opencode.kefir.selenium.pages";
		logger.debug( concat("selenium class name: ", className) );

		return className;
	}
	public static String getSeleniumMainPageClassName() {
		final String className = "MainPage";
		logger.debug( concat("selenium class name: ", className) );

		return className;
	}

	// ------------------------------------ orm mapping  -----------------------------
	public static String getSqlTableName(ExtEntity extEntity, Class entityClass) {
		String tableName = getValue(extEntity.sqlTableName(), EntityMappingAppender.getSqlTableName(entityClass));

		logger.info( concat("sql table name: ", tableName) );
		return tableName;
	}
	public static String getSqlSequenceName(ExtEntity extEntity, Class entityClass) {
		String tableName = getValue(extEntity.sqlSequenceName(), EntityMappingAppender.getSqlSequenceName(entityClass));

		logger.info( concat("sql sequence name: ", tableName) );
		return tableName;
	}
	public static String getOrmSequenceName(ExtEntity extEntity, Class entityClass) {
		String name = getValue(extEntity.ormSequenceName(), getSimpleName(entityClass).concat(ORM_SEQUENCE_NAME_POSTFIX));

		logger.info( concat("orm sequence name: ", name) );
		return name;
	}
	public static String getJoinColumnName(ChooseField chooseField, Field field) {
		String columnName = getValue( chooseField.joinColumnName(), concat(EntityMappingAppender.getSqlColumnName(field.getName()), EntityMappingAppender.ID_POSTFIX ) );

		logger.info( concat("@ChooseField join column name: ", columnName) );
		return columnName;
	}
	public static String getJoinColumnName(ComboBoxField chooseField, Field field) {
		String columnName = getValue( chooseField.joinColumnName(), concat( EntityMappingAppender.getSqlColumnName(field.getName()), EntityMappingAppender.ID_POSTFIX ) );

		logger.info( concat("@ComboBoxField join column name: ", columnName) );
		return columnName;
	}
	public static String getJoinColumnName(AddressField addressField, Field field) {
		String columnName = getValue( addressField.joinColumnName(), concat( EntityMappingAppender.getSqlColumnName(field.getName()), EntityMappingAppender.ID_POSTFIX ) );

		logger.info( concat("@AddressField join column name: ", columnName) );
		return columnName;
	}
	public static String getSqlColumnName(EnumField enumField, Field field) {
		String columnName = getValue( enumField.sqlColumnName(), EntityMappingAppender.getSqlColumnName(field.getName()) );

		logger.info( concat("@EnumField sql column name: ", columnName) );
		return columnName;
	}
	public static String getSqlColumnName(SqlColumn sqlColumn, Field field) {
		String columnName = getValue( sqlColumn.columnName(), EntityMappingAppender.getSqlColumnName(field.getName()) );

		logger.info( concat("@SqlColumn sql column name: ", columnName) );
		return columnName;
	}

	// ------------------------------------ view config  -----------------------------
	public static String getRowClassName(LegendField legendField) {
		return getValue(legendField.rowClassName(), legendField.fieldName());
	}
	public static String getLegendClassName(LegendField legendField) {
		return getValue(legendField.legendClassName(), getRowClassName(legendField));
	}

	private static final Logger logger = Logger.getLogger(ExtEntityUtils.class);

	public static final String PACKAGE_SEPARATOR = ".";
	public static final String SERVLET_URL_PREFIX = "/";
	public static final String HTML_URL_SEPARATOR = "/";
	public static final String CONSTANT_NAME_SEPARATOR = "_";
	public static final String JS_FIELD_NAME_SEPARATOR = "-";
}
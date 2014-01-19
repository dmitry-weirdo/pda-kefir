/**
 * Copyright 2011 LLC "Open Code"
 * http://www.o-code.ru
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */
package su.opencode.kefir.gen;

import su.opencode.kefir.gen.field.button.SecondRowButton;
import su.opencode.kefir.gen.field.linkedEntity.PreventDeleteEntity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExtEntity
{
	// ----------------------- ��������� orm-�������� � ��������/����� sql-������ -------------------
	/**
	 * @return �������� sql-�������, �������� ��������
	 * �� ��������� � ����� ����� ������, � ������� ����� ��������� ���������.
	 */
	String sqlTableName() default "";

	/**
	 * @return �����������, ���������� ����� create table �������� � createTables.sql.
	 */
	String sqlTableComment() default "";

	/**
	 * @return �������� sql-���������� ��� �������, �������� ��������.
	 * �� ��������� � ( ��� ������, � ������� ����� ��������� ���������, ���������� ������� + "_gen")
	 */
	String sqlSequenceName() default "";

	/**
	 * @return �������� orm �������� sequence-generator ��� sql-���������� ��� �������, �������� ��������.
	 * �� ��������� � ( ��� ������, � ��������� ����� + "Generator")
	 */
	String ormSequenceName() default "";

	// ----------------------- ��������� ���� ----------------------------------------------
	/**
	 * @return ������������� ����, � ������� ����� �������������� js �����, ���������� � ���������
	 * ���� �� ������, ����� ������� ����������, ������������ ���������� ���������� ������ ������,
	 * � ������� ����� ����� ��������.
	 */
	String jsDirectory() default "";

	/**
	 * @return �������, ������� ����� ������������ ��� id ����� js ����� ���.
	 * ���� �� ������, ����� �������������� ��� ������ �������� � ��������� �����.
	 */
	String jsFieldPrefix() default "";

	/**
	 * @return ������ ��� ������ EJB-�������, ������������ ��� ������ � ������� (��� �� �������, ��� � � CRUD-�������.
	 */
	String serviceClassName() default "";

	/**
	 * @return ��� ejb-�������, ������������ ��� ��������� � web.xml � jboss-web.xml.
	 * ���� �� �������, ����� �������������� ��� ������ �������.
	 */
	String serviceReferenceName() default "";

	/**
	 * @return jndi-��� ejb-������� ��� �������� � jboss-web.xml.
	 * ���� �� �������, ����� �������������� ��� ���������� ������.
	 */
	String serviceJndiName() default "";

	/**
	 * @return ������ ��� ���������� ������ EJB-�������, ������������ ��� ������ � ������� (��� �� �������, ��� � � CRUD-�������.
	 */
	String serviceBeanClassName() default "";

	/**
	 * @return ������ ��� ������ ������� ���������, ������� ������������ ��� ������ � ������.
	 */
	String filterConfigClassName() default "";

	/**
	 * @return ������ ��� ������, � ������� ����� ������������� �������� ������� � CRUD (��� �������� ������������ � ���� �����).
	 */
	String servletPackageName() default "";

	// ----------------------- ������ ----------------------------------------------

	/**
	 * @return �������� ������ �������, ������� ���������� ������ ���������
	 * �� ��������� � ("get" + �������� ������ �������� + "s").
	 */
	String listMethodName() default "";

	/**
	 * @return �������� ������ �������, ������� ���������� ���������� ���������
	 * �� ��������� � ("get" + �������� ������ �������� + "sCount").
	 */
	String countMethodName() default "";

	/**
	 * @return �������� ������ �������, ������� ���������� �������� �� id.
	 * �� ��������� � ("get" + �������� ������ ��������).
	 */
	String getMethodName() default "";

	/**
	 * @return <code>true</code> - ���� get ����� �������� ��������, ��������� ������ "select Entity o where o.id = :id"
	 * <br/>
	 * <code>false</code> - ���� get ����� �������� ��������, ��������� em.find(id)
	 */
	boolean getUsingSelectById() default false;

	/**
	 * @return �������� ������ �������, ������� ���������� VO �������� �� id � ���������� ������ VO.
	 * �� ��������� � ("get" + �������� ������ �������� + "VO").
	 */
	String getVOMethodName() default "";

	/**
	 * @return �������� ������ �������, ������� ������� ��������.
	 * �� ��������� � ("create" + �������� ������ ��������).
	 */
	String createMethodName() default "";

	/**
	 * @return �������� ������ �������, ������� �������� ��������.
	 * �� ��������� � ("update" + �������� ������ ��������).
	 */
	String updateMethodName() default "";

	/**
	 * @return �������� ������ �������, ������� ������� ��������.
	 * �� ��������� � ("delete" + �������� ������ ��������).
	 */
	String deleteMethodName() default "";

	/**
	 * @return ��������� ��������, ������������� ������� ����� ��������� ����� ��������� ��������.
	 */
	PreventDeleteEntity[] preventDeleteEntities() default {};

	/**
	 * @return ������ ��� ������ VO, ������������� ��� ����������� ������ ���������.
	 * ���� ������� ������ ������, �� ����� �������������� (������ ��� ������ �������� + "VO").
	 */
	String listVoClassName() default "";

	/**
	 * @return ������ ��� ������ VO, ������������� ��� ����������� ������ ������ ���������.
	 * ���� �� �������, �� ����� �������������� �� �� VO, ��� � ��� ������ ���������.
	 */
	String chooseVoClassName() default "";

	/**
	 * @return ������ ��� ������ QueryBuilder, ������������� ��� ���������� �������� �� ������ ���������.
	 * ���� ������� ������ ������, �� ����� �������������� (������ ��� �������� + "QueryBuilder")
	 */
	String queryBuilderClassName() default "";

	/**
	 * @return �������� join ��� �������� list � count � QueryBuilder.
	 */
	String queryBuilderJoin() default "";


	/**
	 * @return ������� ��� ������ ��������, ������������� ������ ��������.
	 * �� ��������� � (��� ������ �������� + "sListServlet")
	 */
	String listServletClassName() default "";

	/**
	 * @return URL ��������, ������� ������ ������ ���������.
	 * �� ��������� � ("/" + ��� ������ �������� � ��������� ����� + "sList")
	 */
	String listServletUrl() default "";

	/**
	 * @return ������� ��� ������ ��������, ������������ ������ ��������� � Excel.
	 * �� ��������� � (��� ������ �������� + "sListToExcelServlet")
	 */
	String listExportToExcelServletClassName() default "";

	/**
	 * @return URL ��������, ������������ ������ ��������� � Excel.
	 * �� ��������� � ("/" + ��� ������ �������� � ��������� ����� +  "sListToExcel")
	 */
	String listExportToExcelServletUrl() default "";

	/**
	 * @return ��� xls-�����, � ������� ����������� ������ ���������.
	 * �� ��������� � (��� ������ �������� � ��������� ����� +  "s")
	 */
	String listExportToExcelFileName() default "";

	/**
	 * @return URL ��������, ������� ������ ������ ��������� ��� ������.
	 * ���� �� ������, �� ����� �������������� ��� �� ���, ��� � ��� ������ ���������.
	 */
	String chooseServletUrl() default "";

	/**
	 * @return ������� ��� ������ ��������, ������������� ������ �� id.
	 * �� ��������� � (��� ������ �������� + "GetServlet")
	 */
	String getServletClassName() default "";

	/**
	 * @return URL ��������, ������� �������� �������� �� id.
	 * �� ��������� � ("/" + ��� ������ �������� � ��������� ����� + "Get").
	 */
	String getServletUrl() default "";

	/**
	 * @return ������� ��� ������ ��������, ���������� ��������.
	 * �� ��������� � (��� ������ �������� + "CreateServlet")
	 */
	String createServletClassName() default "";

	/**
	 * @return URL ��������, ������� ������� ��������.
	 * �� ��������� � ("/" + ��� ������ �������� � ��������� ����� + "Create").
	 */
	String createServletUrl() default "";

	/**
	 * @return ������� ��� ������ ��������, ����������� ��������.
	 * �� ��������� � (��� ������ �������� + "UpdateServlet")
	 */
	String updateServletClassName() default "";

	/**
	 * @return URL ��������, ������� �������� ��������.
	 * �� ��������� � ("/" + ��� ������ �������� � ��������� ����� + "Update").
	 */
	String updateServletUrl() default "";

	/**
	 * @return ������� ��� ������ ��������, ���������� ��������.
	 * �� ��������� � (��� ������ �������� + "DeleteServlet")
	 */
	String deleteServletClassName() default "";

	/**
	 * @return URL ��������, ������� ������� ��������.
	 * �� ��������� � ("/" + ��� ������ �������� � ��������� ����� + "Delete").
	 */
	String deleteServletUrl() default "";


	// ------------------------------  Javascript ����� ---------------------------------------------
	/**
	 * @return ���������, � ������� ��������� ��� ����� (������, ������ � CRUD) ��� ������������.
	 * ���� �� ������, �� ����� �������������� (�����, � ������� ��������� ����� �������� + ��� ������ �������� � ��������� �����)
	 */
	String jsNamespace() default "";

	// ------------------------------  ������ �������� ����, ����������� ������ ��������� -----------------------

	/**
	 * @return <code>true</code> - ���� ����� �������� ������ �������� �� ������ ��������� � ������� ����.
	 * <br/>
	 * <code>false</code> - � ��������� ������
	 */
	boolean hasListMainMenuButton() default true;

	/**
	 * @return id ������ � ������� ����.
	 * �� ��������� � (�������� ������ �������� � ��������� ����� + "-list-mainMenu")
	 */
	String listMainMenuButtonId() default "";

	/**
	 * @return ����� ������ � ������� ����.
	 * �� ��������� � ����� {@linkplain #listWindowTitle() �������� ���� �� ������� ���������}
	 */
	String listMainMenuButtonText() default "";

	/**
	 * @return ������ (����������� ���������) �� ������ � ������� ����.
	 * �� ��������� � ����� {@linkplain #listMainMenuButtonText() ������ �� ������}.
	 */
	String listMainMenuButtonToolTip() default "";


	/**
	 * @return �������� js-����� �� ������� ���������.
	 * ���� �� �������, ����� �������������� (��� ������ � ��������� ����� + "sList")
	 */
	String listJsFileName() default "";

	/**
	 * @return ��� ���������� (��� �������) ��� js-������ �������
	 * �� ��������� � (��� ������ �������� + "sList").
	 */
	String listJsNamespace() default "";

	/**
	 * @return �������� ��������� ������� ������ ����� ������ ���������
	 */
	String listInitFunctionName() default DEFAULT_LIST_INIT_FUNCTION_NAME;

	/**
	 * @return id ���� (window) ��� ������ ���������.
	 * �� ��������� � (��� ������ c������� c ��������� ����� + "sWindow")
	 */
	String listWindowId() default "";

	/**
	 * @return �������� ����, ������������� ������ ���������
	 */
	String listWindowTitle();

	/**
	 * @return ���������� �� �� ���� ����� ���� �� ������� ���������
	 */
	boolean listWindowMaximized() default true;

	/**
	 * @return id ������� (gridPanel) ��� ������ ���������.
	 * �� ��������� � (��� ������ �������� � ��������� ����� + "sGrid")
	 */
	String listGridPanelId() default "";

	/**
	 * @return <code>true</code> ���� ������ ��� (� ��������������� ��������) ������������ ����� �������� ����� ������ (CRUD � ��������)
	 * <code>false</code> � � ��������� ������.
	 */
	boolean listSecondRowBeforeFirstRow() default true;

	/**
	 * @return ������, ����������� � �������������� (������) ���� � ������ ���������.
	 */
	SecondRowButton[] listSecondRowButtons() default {};

	/**
	 * @return id ������ �������� �������� �� ������ ���������
	 * �� ��������� � (��� ������ �������� � ��������� ����� + "sList-createButton")
	 */
	String listCreateButtonId() default "";

	/**
	 * @return ����� �� ������ �������� �������� �� ������ ���������
	 */
	String listCreateButtonText() default "�������";

	/**
	 * @return id ������ ��������� �������� �� ������ ���������
	 * �� ��������� � (��� ������ �������� � ��������� ����� + "sList-showButton")
	 */
	String listShowButtonId() default "";

	/**
	 * @return ����� �� ������ ��������� �������� �� ������ ���������
	 */
	String listShowButtonText() default "��������";

	/**
	 * @return id ������ ��������� �������� �� ������ ���������
	 * �� ��������� � (��� ������ �������� � ��������� ����� + "sList-updateButton")
	 */
	String listUpdateButtonId() default "";

	/**
	 * @return ����� �� ������ ��������� �������� �� ������ ���������
	 */
	String listUpdateButtonText() default "��������";

	/**
	 * @return id ������ �������� �������� �� ������ ���������
	 * �� ��������� � (��� ������ �������� � ��������� ����� + "sList-deleteButton")
	 */
	String listDeleteButtonId() default "";

	/**
	 * @return ����� �� ������ �������� �������� �� ������ ���������
	 */
	String listDeleteButtonText() default "�������";

	/**
	 * @return ������������ �� � ������ ��������� ������ �������� � Excel.
	 */
	boolean listExportToExcelButtonPresent() default true;

	/**
	 * @return id ������ �������� � Excel ������ ���������.
	 * �� ��������� � (��� ������ �������� � ��������� ����� + "sList-exportToExcelButton")
	 */
	String listExportToExcelButtonId() default "";

	/**
	 * @return ����� �� ������ �������� � Excel ������ ���������
	 */
	String listExportToExcelButtonText() default "��������� � Excel";

	/**
	 * @return id ������ �������� ������ ���������
	 * �� ��������� � (��� ������ �������� � ��������� ����� + "sList-closeButton")
	 */
	String listCloseButtonId() default "";

	/**
	 * @return ����� �� ������ �������� ������ ���������
	 */
	String listCloseButtonText() default "�����";

	/**
	 * @return ��������� ���������, ���������� � ������, ����� �� ������ ������� ������.
	 */
	String notChosenTitle() default "������";

	/**
	 * @return ���������, ���������� � ������, ����� �� ������ ������� ������.
	 */
	String notChosenMessage();

	// ------------------------------  ����� ������ -------------------------------------------
	/**
	 * @return <code>true</code> - ���� autoLoad �������� � ����� ������ �� ������ ��������� ����.
	 * <code>false</code> � ���� autoLoad �������� � ����� ������ �� ������ ��������� ���.
	 */
	boolean chooseListAutoLoad() default false;

	/**
	 * @return �������� js-����� � ������� �� ������ ���������.
	 * ���� �� �������, ����� �������������� (��� ������ � ��������� ����� + "Choose")
	 */
	String chooseJsFileName() default "";

	/**
	 * @return ��� ���������� (��� �������) ��� js-������ �� ������ ���������
	 * �� ��������� � (��� ������ �������� + "Choose").
	 */
	String chooseJsNamespace() default "";

	/**
	 * @return �������� ��������� ������� ������ ����� ������ �������� �� ������
	 */
	String chooseInitFunctionName() default DEFAULT_CHOOSE_INIT_FUNCTION_NAME;

	/**
	 * @return ��� ��������� ������� ������� ������ ����� ������, � ������� ���������� ���������� ������.
	 */
	String chooseInitConfigSuccessHandlerParamName() default DEFAULT_SUCCESS_HANDLER_PARAM_NAME;

	/**
	 * @return id ���� (window) ��� ������ �� ������ ���������.
	 * �� ��������� � (��� ������ c������� c ��������� ����� + "chooseWindow")
	 */
	String chooseWindowId() default "";

	/**
	 * @return �������� ����, ������������� ����� �� ������ ���������
	 */
	String chooseWindowTitle();

	/**
	 * @return ���������� �� �� ���� ����� ���� � ������� �� ������ ���������
	 */
	boolean chooseWindowMaximized() default false;

	/**
	 * @return ������ ���� � ������� �� ������ ���������
	 */
	int chooseWindowWidth() default 1000;

	/**
	 * @return ������ ���� � ������� �� ������ ���������
	 */
	int chooseWindowHeight() default 600;

	/**
	 * @return id ������� (gridPanel) ��� ������ �� ������ ���������.
	 * �� ��������� � (��� ������ �������� � ��������� ����� + "ChooseGrid")
	 */
	String chooseGridPanelId() default "";

	/**
	 * @return id ������ ������ �������� ��� ������ �� ������ ���������
	 * �� ��������� � (��� ������ �������� � ��������� ����� + "Choose-chooseButton")
	 */
	String chooseChooseButtonId() default "";

	/**
	 * @return ����� �� ������ ������ �������� ��� ������ �� ������ ���������
	 */
	String chooseChooseButtonText() default "�������";

	/**
	 * @return id ������ ��������� �������� ��� ������ �� ������ ���������
	 * �� ��������� � (��� ������ �������� � ��������� ����� + "Choose-showButton")
	 */
	String chooseShowButtonId() default "";

	/**
	 * @return ����� �� ������ ��������� �������� ��� ������ �� ������ ���������
	 */
	String chooseShowButtonText() default "��������";

	/**
	 * @return ������������ �� � ����� ������ �������� ������ �������� ��������.
	 */
	boolean chooseCreateButtonPresent() default true;

	/**
	 * @return id ������ �������� �������� ��� ������ �� ������ ���������
	 * �� ��������� � (��� ������ �������� � ��������� ����� + "Choose-createButton")
	 */
	String chooseCreateButtonId() default "";

	/**
	 * @return ����� �� ������ �������� �������� ��� ������ �� ������ ���������
	 */
	String chooseCreateButtonText() default "�������";

	/**
	 * @return id ������ ������ ������ �� ������ ���������.
	 * �� ��������� � (��� ������ �������� � ��������� ����� + "choose-cancelButton")
	 */
	String chooseCancelButtonId() default "";

	/**
	 * @return ����� �� ������ ������ ������ �� ������ ���������
	 */
	String chooseCancelButtonText() default "������";


	// ------------------------------  ����� CRUD ---------------------------------------------

	/**
	 * @return �������� js-����� � �������� �������� ��������� ����� ��������.
	 * �� ��������� � (��� ������ � ��������� ����� + "FormLayout")
	 */
	String formLayoutJsFileName() default "";

	/**
	 * @return �������� �������, ������������ �������� ��������� ����� ��������.
	 * ������� ��������� � {@linkplain #formLayoutJsFileName() ��������� �����}.
	 */
	String getFormItemsLayoutFunctionName() default "getFormItemsLayout";

	/**
	 * @return �������� js-����� � ������ ��������.
	 * �� ��������� � (��� ������ � ��������� ����� + "Form")
	 */
	String formJsFileName() default "";

	/**
	 * @return ��� ���������� (��� �������) ��� js c������ CRUD �����.
	 * �� ��������� � ��� ������ ��������.
	 */
	String formJsNamespace() default "";

	/**
	 * @return <code>true</code> - ���� ��� ��������� �������� �� id ������������ �� VO,
	 * <code>false</code> - ���� ��� ��������� �������� �� id ������������ ���� ��������.
	 */
	boolean useVoForGetById() default true;

	/**
	 * @return <code>true</code> � ���� ��� ������ ����� ����� ��������� ���� �� ��� ������ ������;<br/>
	 * <code>false</code> � � ��������� ������.
	 */
	boolean formBindMask() default false;

	/**
	 * @return id ����� ���� � CRUD-������ ��������
	 * �� ��������� � (��� ������ � ��������� ����� + "FormWindow")
	 * @see ExtEntityUtils#getFormWindowId(ExtEntity, Class)
	 */
	String formWindowId() default "";

	/**
	 * @return ������ ���� � CRUD-������ ��������.
	 */
	int formWindowWidth() default 650;

	/**
	 * @return ������ ������ � �������� CRUD-�����.
	 */
	int formLabelWidth() default 150;

	/**
	 * @return �������� ��������� ������� ������ ����� �������� ��������
	 */
	String formCreateFunctionName() default DEFAULT_FORM_CREATE_FUNCTION_NAME;

	/**
	 * @return �������� ��������� ������� ������ ����� ��������� ��������
	 */
	String formShowFunctionName() default DEFAULT_FORM_SHOW_FUNCTION_NAME;

	/**
	 * @return �������� ��������� ������� ������ ����� ��������� ��������
	 */
	String formUpdateFunctionName() default DEFAULT_FORM_UPDATE_FUNCTION_NAME;

	/**
	 * @return �������� ��������� ������� ������ ����� �������� ��������
	 */
	String formDeleteFunctionName() default DEFAULT_FORM_DELETE_FUNCTION_NAME;

	/**
	 * @return ��� ��������� ������� ������� ������ ����� �������� (��� ������, ���������, ��������), � ������� ���������� id ��������.
	 */
	String formConfigEntityIdParamName() default DEFAULT_ENTITY_ID_PARAM_NAME;

	/**
	 * @return ��������� ���� �������� ��������
	 */
	String createWindowTitle();

	/**
	 * @return ��������� ���� ��������� ��������
	 */
	String showWindowTitle();

	/**
	 * @return ��������� ���� ��������� ��������
	 */
	String updateWindowTitle();

	/**
	 * @return ��������� ���� �������� ��������
	 */
	String deleteWindowTitle();


	/**
	 * @return ����� �� ������ ���������� � ����� �������� ��������
	 */
	String createSaveButtonText() default "���������";

	/**
	 * @return ����� �� ������ ���������� � ����� ��������� ��������
	 */
	String updateSaveButtonText() default "���������";

	/**
	 * @return ����� �� ������ ���������� � ����� �������� ��������
	 */
	String deleteSaveButtonText() default "�������";


	/**
	 * @return ��������� ��������, ������������ � �������� ���������� ��������
	 */
	String createSaveWaitMessage() default "����������� ����������. ����������, ���������...";

	/**
	 * @return ��������� ��������, ������������ � �������� ��������� ��������
	 */
	String updateSaveWaitMessage() default "����������� ����������. ����������, ���������...";

	/**
	 * @return ��������� ��������, ������������ � �������� �������� ��������
	 */
	String deleteSaveWaitMessage() default "����������� ��������. ����������, ���������...";


	/**
	 * @return ��������� ��������� �� ������ ��� �������� ��������
	 */
	String createSaveErrorMessage() default "������ ��� ��������";

	/**
	 * @return ��������� ��������� �� ������ ��� ��������� ��������
	 */
	String updateSaveErrorMessage() default "������ ��� ���������";

	/**
	 * @return ��������� ��������� �� ������ ��� �������� ��������
	 */
	String deleteSaveErrorMessage() default "������ ��� ��������";


	/**
	 * @return id ������ ������ � ����� �������� ��������
	 * �� ��������� � ( {@linkplain #jsFieldPrefix() ������� js-����} + "-cancel" )
	 */
	String createCancelButtonId() default "";

	/**
	 * @return id ������ ������ (�������� �����) � ����� ��������� ��������
	 * �� ��������� � ( {@linkplain #jsFieldPrefix() ������� js-����} + "-close" )
	 */
	String showCancelButtonId() default "";

	/**
	 * @return id ������ ������ � ����� ��������� ��������
	 * �� ��������� � ( {@linkplain #jsFieldPrefix() ������� js-����} + "-cancel" )
	 */
	String updateCancelButtonId() default "";

	/**
	 * @return id ������ �������� � ����� ��������� ��������
	 * �� ��������� � ( {@linkplain #jsFieldPrefix() ������� js-����} + "-cancel" )
	 */
	String deleteCancelButtonId() default "";


	/**
	 * @return ����� �� ������ ������ � ����� �������� ��������
	 */
	String createCancelButtonText() default "������";

	/**
	 * @return ����� �� ������ ������ � ����� ��������� ��������
	 */
	String showCancelButtonText() default "�������";

	/**
	 * @return ����� �� ������ ������ � ����� ��������� ��������
	 */
	String updateCancelButtonText() default "������";

	/**
	 * @return ����� �� ������ �������� � ����� ��������� ��������
	 */
	String deleteCancelButtonText() default "������";


	/**
	 * @return id ������ ���������� � ������ ��������, ��������� ��������.
	 * �� ��������� � ( {@linkplain #jsFieldPrefix() ������� js-����} + "-save" )
	 */
	String formSaveButtonId() default "";

	/**
	 * @return ��� ���������� ��������, ������������� � ����� ���������\���������\�������� (�� ������� ��� ������-���� ���).
	 * �� ��������� � ��� ������ �������� � ��������� �����
	 */
	String formEntityVarName() default "";


	// todo: ��� �������� default'� ������� � ��������� ���������

	public static final String DEFAULT_LIST_INIT_FUNCTION_NAME = "init";
	public static final String DEFAULT_CHOOSE_INIT_FUNCTION_NAME = "init";

	public static final String DEFAULT_FORM_CREATE_FUNCTION_NAME = "initCreateForm";
	public static final String DEFAULT_FORM_SHOW_FUNCTION_NAME = "initShowForm";
	public static final String DEFAULT_FORM_UPDATE_FUNCTION_NAME = "initUpdateForm";
	public static final String DEFAULT_FORM_DELETE_FUNCTION_NAME = "initDeleteForm";

	public static final String DEFAULT_ENTITY_ID_PARAM_NAME = "id";
	public static final String DEFAULT_SUCCESS_HANDLER_PARAM_NAME = "successHandler";
}
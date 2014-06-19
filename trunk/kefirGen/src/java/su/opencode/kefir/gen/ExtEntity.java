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
	// ----------------------- Генерация orm-маппинга и создания/дропа sql-таблиц -------------------
	/**
	 * @return название sql-таблицы, хранящей сущности
	 * По умолчанию — равно имени класса, в котором слова разделены подчерком.
	 */
	String sqlTableName() default "";

	/**
	 * @return комментарий, помещаемый перед create table сущности в createTables.sql.
	 */
	String sqlTableComment() default "";

	/**
	 * @return название sql-генератора для таблицы, хранящей сущности.
	 * По умолчанию — ( имя класса, в котором слова разделены подчерком, маленькими буквами + "_gen")
	 */
	String sqlSequenceName() default "";

	/**
	 * @return название orm маппинга sequence-generator для sql-генератора для таблицы, хранящей сущности.
	 * По умолчанию — ( имя класса, с маленькой буквы + "Generator")
	 */
	String ormSequenceName() default "";

	// ----------------------- Генерация форм ----------------------------------------------
	/**
	 * @return относительный путь, в котором будут генерироваться js файлы, работающие с сущностью
	 * Если не указан, будет браться директория, называющаяся аналогично последнему уровня пакета,
	 * в котором лежит класс сущности.
	 */
	String jsDirectory() default "";

	/**
	 * @return префикс, который будет использовать для id полей js формы итд.
	 * Если не указан, будет использоваться имя класса сущности с маленькой буквы.
	 */
	String jsFieldPrefix() default "";

	/**
	 * @return Полное имя класса EJB-сервиса, используемый для работы с классом (как со списком, так и с CRUD-формами.
	 */
	String serviceClassName() default "";

	/**
	 * @return имя ejb-сервиса, используемое для референса в web.xml и jboss-web.xml.
	 * Если не указано, будет использоваться имя класса сервиса.
	 */
	String serviceReferenceName() default "";

	/**
	 * @return jndi-имя ejb-сервиса для маппинга в jboss-web.xml.
	 * Если ну указано, будет использоваться имя реализации класса.
	 */
	String serviceJndiName() default "";

	/**
	 * @return Полное имя реализации класса EJB-сервиса, используемый для работы с классом (как со списком, так и с CRUD-формами.
	 */
	String serviceBeanClassName() default "";

	/**
	 * @return полное имя класса фильтра сущностей, который используется для списка и выбора.
	 */
	String filterConfigClassName() default "";

	/**
	 * @return полное имя пакета, в котором будут располагаться сервлеты списков и CRUD (все сервлеты складываются в один пакет).
	 */
	String servletPackageName() default "";

	// ----------------------- список ----------------------------------------------

	/**
	 * @return название метода сервиса, который возвращает список сущностей
	 * По умолчанию — ("get" + название класса сущности + "s").
	 */
	String listMethodName() default "";

	/**
	 * @return название метода сервиса, который возвращает количество сущностей
	 * По умолчанию — ("get" + название класса сущности + "sCount").
	 */
	String countMethodName() default "";

	/**
	 * @return название метода сервиса, который возвращает сущность по id.
	 * По умолчанию — ("get" + название класса сущности).
	 */
	String getMethodName() default "";

	/**
	 * @return <code>true</code> - если get метод получает сущность, используя запрос "select Entity o where o.id = :id"
	 * <br/>
	 * <code>false</code> - если get метод получает сущность, используя em.find(id)
	 */
	boolean getUsingSelectById() default false;

	/**
	 * @return название метода сервиса, который возвращает VO сущности по id и указанному классу VO.
	 * По умолчанию — ("get" + название класса сущности + "VO").
	 */
	String getVOMethodName() default "";

	/**
	 * @return название метода сервиса, который создает сущность.
	 * По умолчанию — ("create" + название класса сущности).
	 */
	String createMethodName() default "";

	/**
	 * @return название метода сервиса, который изменяет сущность.
	 * По умолчанию — ("update" + название класса сущности).
	 */
	String updateMethodName() default "";

	/**
	 * @return название метода сервиса, который удаляет сущность.
	 * По умолчанию — ("delete" + название класса сущности).
	 */
	String deleteMethodName() default "";

	/**
	 * @return связанные сущности, существование которых нужно проверять перед удалением сущности.
	 */
	PreventDeleteEntity[] preventDeleteEntities() default {};

	/**
	 * @return полное имя класса VO, используемого для отображения списка сущностей.
	 * Если указана пустая строка, то будет использоваться (полное имя класса сущности + "VO").
	 */
	String listVoClassName() default "";

	/**
	 * @return полное имя класса VO, используемого для отображения выбора списка сущностей.
	 * Если не указано, то будет использоваться то же VO, что и для списка сущностей.
	 */
	String chooseVoClassName() default "";

	/**
	 * @return полное имя класса QueryBuilder, используемого для построения запросов на списки сущностей.
	 * Если указана пустая строка, то будет использоваться (полное имя сущности + "QueryBuilder")
	 */
	String queryBuilderClassName() default "";

	/**
	 * @return предикат join для запросов list и count в QueryBuilder.
	 */
	String queryBuilderJoin() default "";


	/**
	 * @return краткое имя класса сервлета, возвращающего список объектов.
	 * По умолчанию — (имя класса сущности + "sListServlet")
	 */
	String listServletClassName() default "";

	/**
	 * @return URL сервлета, который выдает список сущностей.
	 * По умолчанию — ("/" + имя класса сущности с маленькой буквы + "sList")
	 */
	String listServletUrl() default "";

	/**
	 * @return краткое имя класса сервлета, выгружающего список сущностей в Excel.
	 * По умолчанию — (имя класса сущности + "sListToExcelServlet")
	 */
	String listExportToExcelServletClassName() default "";

	/**
	 * @return URL сервлета, выгружающего список сущностей в Excel.
	 * По умолчанию — ("/" + имя класса сущности с маленькой буквы +  "sListToExcel")
	 */
	String listExportToExcelServletUrl() default "";

	/**
	 * @return имя xls-файла, в который выгружается список сущностей.
	 * По умолчанию — (имя класса сущности с маленькой буквы +  "s")
	 */
	String listExportToExcelFileName() default "";

	/**
	 * @return URL сервлета, который выдает список сущностей для выбора.
	 * Если не указан, то будет использоваться тот же урл, что и для списка сущностей.
	 */
	String chooseServletUrl() default "";

	/**
	 * @return краткое имя класса сервлета, возвращающего объект по id.
	 * По умолчанию — (имя класса сущности + "GetServlet")
	 */
	String getServletClassName() default "";

	/**
	 * @return URL сервлета, который получает сущность по id.
	 * По умолчанию — ("/" + имя класса сущности с маленькой буквы + "Get").
	 */
	String getServletUrl() default "";

	/**
	 * @return краткое имя класса сервлета, создающего сущность.
	 * По умолчанию — (имя класса сущности + "CreateServlet")
	 */
	String createServletClassName() default "";

	/**
	 * @return URL сервлета, который создает сущность.
	 * По умолчанию — ("/" + имя класса сущности с маленькой буквы + "Create").
	 */
	String createServletUrl() default "";

	/**
	 * @return краткое имя класса сервлета, изменяющего сущность.
	 * По умолчанию — (имя класса сущности + "UpdateServlet")
	 */
	String updateServletClassName() default "";

	/**
	 * @return URL сервлета, который изменяет сущность.
	 * По умолчанию — ("/" + имя класса сущности с маленькой буквы + "Update").
	 */
	String updateServletUrl() default "";

	/**
	 * @return краткое имя класса сервлета, удаляющего сущность.
	 * По умолчанию — (имя класса сущности + "DeleteServlet")
	 */
	String deleteServletClassName() default "";

	/**
	 * @return URL сервлета, который удаляет сущность.
	 * По умолчанию — ("/" + имя класса сущности с маленькой буквы + "Delete").
	 */
	String deleteServletUrl() default "";


	// ------------------------------  Javascript формы ---------------------------------------------
	/**
	 * @return неймспейс, в котором находятся все формы (списка, выбора и CRUD) для джаваскрипта.
	 * Если не указан, то будет использоваться (пакет, в котором находится класс сущности + имя класса сущности с маленькой буквы)
	 */
	String jsNamespace() default "";

	// ------------------------------  Кнопка главного меню, открывающая список сущнсотей -----------------------

	/**
	 * @return <code>true</code> - если нужно добавить кнопку перехода на список сущностей в главное меню.
	 * <br/>
	 * <code>false</code> - в противном случае
	 */
	boolean hasListMainMenuButton() default true;

	/**
	 * @return id кнопки в главном меню.
	 * По умолчанию — (название класса сущности с маленькой буквы + "-list-mainMenu")
	 */
	String listMainMenuButtonId() default "";

	/**
	 * @return текст кнопки в главном меню.
	 * По умолчанию — равно {@linkplain #listWindowTitle() названию окна со списком сущностей}
	 */
	String listMainMenuButtonText() default "";

	/**
	 * @return тултип (всплывающая подсказка) на кнопке в главном меню.
	 * По умолчанию — равно {@linkplain #listMainMenuButtonText() тексту на кнопке}.
	 */
	String listMainMenuButtonToolTip() default "";


	/**
	 * @return название js-файла со списком сущностей.
	 * Если не указано, будет использоваться (имя класса с маленькой буквы + "sList")
	 */
	String listJsFileName() default "";

	/**
	 * @return имя неймспейса (без пакеджа) для js-списка функций
	 * По умолчанию — (имя класса сущности + "sList").
	 */
	String listJsNamespace() default "";

	/**
	 * @return название публичной функции вызова формы списка сущностей
	 */
	String listInitFunctionName() default DEFAULT_LIST_INIT_FUNCTION_NAME;

	/**
	 * @return id окна (window) для списка сущностей.
	 * По умолчанию — (имя класса cущности c маленькой буквы + "sWindow")
	 */
	String listWindowId() default "";

	/**
	 * @return название окна, отображающего список сущностей
	 */
	String listWindowTitle();

	/**
	 * @return развернуто ли на весь экран окно со списком сущностей
	 */
	boolean listWindowMaximized() default true;

	/**
	 * @return id таблицы (gridPanel) для списка сущностей.
	 * По умолчанию — (имя класса сущности с маленькой буквы + "sGrid")
	 */
	String listGridPanelId() default "";

	/**
	 * @return <code>true</code> если второй ряд (с дополнительными кнопками) отображается перед основным рядом кнопок (CRUD и закрытие)
	 * <code>false</code> — в противном случае.
	 */
	boolean listSecondRowBeforeFirstRow() default true;

	/**
	 * @return кнопки, находящиеся в дополнительном (втором) ряде в списке сущностей.
	 */
	SecondRowButton[] listSecondRowButtons() default {};

	/**
	 * @return id кнопки создания сущности из списка сущностей
	 * По умолчанию — (имя класса сущности с маленькой буквы + "sList-createButton")
	 */
	String listCreateButtonId() default "";

	/**
	 * @return текст на кнопке создания сущности из списка сущностей
	 */
	String listCreateButtonText() default "Создать";

	/**
	 * @return id кнопки просмотра сущности из списка сущностей
	 * По умолчанию — (имя класса сущности с маленькой буквы + "sList-showButton")
	 */
	String listShowButtonId() default "";

	/**
	 * @return текст на кнопке просмотра сущности из списка сущностей
	 */
	String listShowButtonText() default "Просмотр";

	/**
	 * @return id кнопки изменения сущности из списка сущностей
	 * По умолчанию — (имя класса сущности с маленькой буквы + "sList-updateButton")
	 */
	String listUpdateButtonId() default "";

	/**
	 * @return текст на кнопке изменения сущности из списка сущностей
	 */
	String listUpdateButtonText() default "Изменить";

	/**
	 * @return id кнопки удаления сущности из списка сущностей
	 * По умолчанию — (имя класса сущности с маленькой буквы + "sList-deleteButton")
	 */
	String listDeleteButtonId() default "";

	/**
	 * @return текст на кнопке удаления сущности из списка сущностей
	 */
	String listDeleteButtonText() default "Удалить";

	/**
	 * @return присутствует ли в списке сущностей кнопка выгрузки в Excel.
	 */
	boolean listExportToExcelButtonPresent() default true;

	/**
	 * @return id кнопки экспорта в Excel списка сущностей.
	 * По умолчанию — (имя класса сущности с маленькой буквы + "sList-exportToExcelButton")
	 */
	String listExportToExcelButtonId() default "";

	/**
	 * @return текст на кнопке экспорта в Excel списка сущностей
	 */
	String listExportToExcelButtonText() default "Выгрузить в Excel";

	/**
	 * @return id кнопки закрытия списка сущностей
	 * По умолчанию — (имя класса сущности с маленькой буквы + "sList-closeButton")
	 */
	String listCloseButtonId() default "";

	/**
	 * @return текст на кнопке закрытия списка сущностей
	 */
	String listCloseButtonText() default "Выход";

	/**
	 * @return Заголовок сообщения, выдаваемое в случае, когда не выбран элемент списка.
	 */
	String notChosenTitle() default "Ошибка";

	/**
	 * @return Сообщение, выдаваемое в случае, когда не выбран элемент списка.
	 */
	String notChosenMessage();

	// ------------------------------  форма выбора -------------------------------------------
	/**
	 * @return <code>true</code> - если autoLoad значений в форме выбора из списка сущностей есть.
	 * <code>false</code> — если autoLoad значений в форме выбора из списка сущностей нет.
	 */
	boolean chooseListAutoLoad() default false;

	/**
	 * @return название js-файла с выбором из списка сущностей.
	 * Если не указано, будет использоваться (имя класса с маленькой буквы + "Choose")
	 */
	String chooseJsFileName() default "";

	/**
	 * @return имя неймспейса (без пакеджа) для js-выбора из списка сущностей
	 * По умолчанию — (имя класса сущности + "Choose").
	 */
	String chooseJsNamespace() default "";

	/**
	 * @return название публичной функции вызова формы выбора сущности из списка
	 */
	String chooseInitFunctionName() default DEFAULT_CHOOSE_INIT_FUNCTION_NAME;

	/**
	 * @return имя параметра конфига функции вызова формы выбора, в который передается обработчик успеха.
	 */
	String chooseInitConfigSuccessHandlerParamName() default DEFAULT_SUCCESS_HANDLER_PARAM_NAME;

	/**
	 * @return id окна (window) для выбора из списка сущностей.
	 * По умолчанию — (имя класса cущности c маленькой буквы + "chooseWindow")
	 */
	String chooseWindowId() default "";

	/**
	 * @return название окна, отображающего выбор из списка сущностей
	 */
	String chooseWindowTitle();

	/**
	 * @return развернуто ли на весь экран окно с выбором из списка сущностей
	 */
	boolean chooseWindowMaximized() default false;

	/**
	 * @return ширина окна с выбором из списка сущностей
	 */
	int chooseWindowWidth() default 1000;

	/**
	 * @return высота окна с выбором из списка сущностей
	 */
	int chooseWindowHeight() default 600;

	/**
	 * @return id таблицы (gridPanel) для выбора из списка сущностей.
	 * По умолчанию — (имя класса сущности с маленькой буквы + "ChooseGrid")
	 */
	String chooseGridPanelId() default "";

	/**
	 * @return id кнопки выбора сущности при выборе из списка сущностей
	 * По умолчанию — (имя класса сущности с маленькой буквы + "Choose-chooseButton")
	 */
	String chooseChooseButtonId() default "";

	/**
	 * @return текст на кнопке выбора сущности при выборе из списка сущностей
	 */
	String chooseChooseButtonText() default "Выбрать";

	/**
	 * @return id кнопки просмотра сущности при выборе из списка сущностей
	 * По умолчанию — (имя класса сущности с маленькой буквы + "Choose-showButton")
	 */
	String chooseShowButtonId() default "";

	/**
	 * @return текст на кнопке просмотра сущности при выборе из списка сущностей
	 */
	String chooseShowButtonText() default "Просмотр";

	/**
	 * @return присутствует ли в форме выбора сущности кнопка создания сущности.
	 */
	boolean chooseCreateButtonPresent() default true;

	/**
	 * @return id кнопки создания сущности при выборе из списка сущностей
	 * По умолчанию — (имя класса сущности с маленькой буквы + "Choose-createButton")
	 */
	String chooseCreateButtonId() default "";

	/**
	 * @return текст на кнопке создания сущности при выборе из списка сущностей
	 */
	String chooseCreateButtonText() default "Создать";

	/**
	 * @return id кнопки отмены выбора из списка сущностей.
	 * По умолчанию — (имя класса сущности с маленькой буквы + "choose-cancelButton")
	 */
	String chooseCancelButtonId() default "";

	/**
	 * @return текст на кнопке отмены выбора из списка сущностей
	 */
	String chooseCancelButtonText() default "Отмена";


	// ------------------------------  форма CRUD ---------------------------------------------

	/**
	 * @return название js-файла с функцией разметки элементов формы сущности.
	 * По умолчанию — (имя класса с маленькой буквы + "FormLayout")
	 */
	String formLayoutJsFileName() default "";

	/**
	 * @return название функции, возвращающей разметку элементов формы сущности.
	 * Функция находится в {@linkplain #formLayoutJsFileName() отдельном файле}.
	 */
	String getFormItemsLayoutFunctionName() default "getFormItemsLayout";

	/**
	 * @return название js-файла с формой сущности.
	 * По умолчанию — (имя класса с маленькой буквы + "Form")
	 */
	String formJsFileName() default "";

	/**
	 * @return имя неймспейса (без пакеджа) для js cкрипта CRUD формы.
	 * По умолчанию — имя класса сущности.
	 */
	String formJsNamespace() default "";

	/**
	 * @return <code>true</code> - если при получении сущности по id используется ее VO,
	 * <code>false</code> - если при получении сущности по id используется сама сущность.
	 */
	boolean useVoForGetById() default true;

	/**
	 * @return <code>true</code> — если при показе формы нужно растянуть шейд на всю высоту экрана;<br/>
	 * <code>false</code> — в противном случае.
	 */
	boolean formBindMask() default false;

	/**
	 * @return id формы окна с CRUD-формой сущности
	 * По умолчанию — (имя класса с маленькой буквы + "FormWindow")
	 * @see ExtEntityUtils#getFormWindowId(ExtEntity, Class)
	 */
	String formWindowId() default "";

	/**
	 * @return ширина окна с CRUD-формой сущности.
	 */
	int formWindowWidth() default 650;

	/**
	 * @return ширина лейбла в основной CRUD-форме.
	 */
	int formLabelWidth() default 150;

	/**
	 * @return название публичной функции вызова формы создания сущности
	 */
	String formCreateFunctionName() default DEFAULT_FORM_CREATE_FUNCTION_NAME;

	/**
	 * @return название публичной функции вызова формы просмотра сущности
	 */
	String formShowFunctionName() default DEFAULT_FORM_SHOW_FUNCTION_NAME;

	/**
	 * @return название публичной функции вызова формы изменения сущности
	 */
	String formUpdateFunctionName() default DEFAULT_FORM_UPDATE_FUNCTION_NAME;

	/**
	 * @return название публичной функции вызова формы удаления сущности
	 */
	String formDeleteFunctionName() default DEFAULT_FORM_DELETE_FUNCTION_NAME;

	/**
	 * @return имя параметра конфига функции вызова формы сущности (для показа, изменения, удаления), в который передается id сущности.
	 */
	String formConfigEntityIdParamName() default DEFAULT_ENTITY_ID_PARAM_NAME;

	/**
	 * @return заголовок окна создания сущности
	 */
	String createWindowTitle();

	/**
	 * @return заголовок окна просмотра сущности
	 */
	String showWindowTitle();

	/**
	 * @return заголовок окна изменения сущности
	 */
	String updateWindowTitle();

	/**
	 * @return заголовок окна удаления сущности
	 */
	String deleteWindowTitle();


	/**
	 * @return текст на кнопке сохранения в форме создания сущности
	 */
	String createSaveButtonText() default "Сохранить";

	/**
	 * @return текст на кнопке сохранения в форме изменения сущности
	 */
	String updateSaveButtonText() default "Сохранить";

	/**
	 * @return текст на кнопке сохранения в форме удаления сущности
	 */
	String deleteSaveButtonText() default "Удалить";


	/**
	 * @return сообщение ожидания, отображаемое в процессе сохранения сущности
	 */
	String createSaveWaitMessage() default "Выполняется сохранение. Пожалуйста, подождите...";

	/**
	 * @return сообщение ожидания, отображаемое в процессе изменения сущности
	 */
	String updateSaveWaitMessage() default "Выполняется сохранение. Пожалуйста, подождите...";

	/**
	 * @return сообщение ожидания, отображаемое в процессе удаления сущности
	 */
	String deleteSaveWaitMessage() default "Выполняется удаление. Пожалуйста, подождите...";


	/**
	 * @return заголовок сообщения об ошибке при создании сущности
	 */
	String createSaveErrorMessage() default "Ошибка при создании";

	/**
	 * @return заголовок сообщения об ошибке при изменении сущности
	 */
	String updateSaveErrorMessage() default "Ошибка при изменении";

	/**
	 * @return заголовок сообщения об ошибке при удалении сущности
	 */
	String deleteSaveErrorMessage() default "Ошибка при удалении";


	/**
	 * @return id кнопки отмены в форме создания сущности
	 * По умолчанию — ( {@linkplain #jsFieldPrefix() префикс js-поля} + "-cancel" )
	 */
	String createCancelButtonId() default "";

	/**
	 * @return id кнопки отмены (закрытия формы) в форме просмотра сущности
	 * По умолчанию — ( {@linkplain #jsFieldPrefix() префикс js-поля} + "-close" )
	 */
	String showCancelButtonId() default "";

	/**
	 * @return id кнопки отмены в форме изменения сущности
	 * По умолчанию — ( {@linkplain #jsFieldPrefix() префикс js-поля} + "-cancel" )
	 */
	String updateCancelButtonId() default "";

	/**
	 * @return id кнопки удаления в форме изменения сущности
	 * По умолчанию — ( {@linkplain #jsFieldPrefix() префикс js-поля} + "-cancel" )
	 */
	String deleteCancelButtonId() default "";


	/**
	 * @return текст на кнопке отмены в форме создания сущности
	 */
	String createCancelButtonText() default "Отмена";

	/**
	 * @return текст на кнопке отмены в форме просмотра сущности
	 */
	String showCancelButtonText() default "Закрыть";

	/**
	 * @return текст на кнопке отмены в форме изменения сущности
	 */
	String updateCancelButtonText() default "Отмена";

	/**
	 * @return текст на кнопке удаления в форме изменения сущности
	 */
	String deleteCancelButtonText() default "Отмена";


	/**
	 * @return id кнопки сохранения в формах создания, изменения удаления.
	 * По умолчанию — ( {@linkplain #jsFieldPrefix() префикс js-поля} + "-save" )
	 */
	String formSaveButtonId() default "";

	/**
	 * @return имя переменной сущности, подставляемой в форму просмотра\изменения\удаления (из таблицы или откуда-либо еще).
	 * По умолчанию — имя класса сущности с маленькой буквы
	 */
	String formEntityVarName() default "";


	// todo: все непустые default'ы вынести в публичные константы

	public static final String DEFAULT_LIST_INIT_FUNCTION_NAME = "init";
	public static final String DEFAULT_CHOOSE_INIT_FUNCTION_NAME = "init";

	public static final String DEFAULT_FORM_CREATE_FUNCTION_NAME = "initCreateForm";
	public static final String DEFAULT_FORM_SHOW_FUNCTION_NAME = "initShowForm";
	public static final String DEFAULT_FORM_UPDATE_FUNCTION_NAME = "initUpdateForm";
	public static final String DEFAULT_FORM_DELETE_FUNCTION_NAME = "initDeleteForm";

	public static final String DEFAULT_ENTITY_ID_PARAM_NAME = "id";
	public static final String DEFAULT_SUCCESS_HANDLER_PARAM_NAME = "successHandler";
}
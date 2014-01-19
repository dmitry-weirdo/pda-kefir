/*
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 16.04.2012 16:46:58$
*/
package su.opencode.kefir.gen.test;

import su.opencode.kefir.gen.ExtEntity;
import su.opencode.kefir.gen.field.*;
import su.opencode.kefir.gen.field.button.SecondRowButton;
import su.opencode.kefir.gen.field.linkedEntity.PreventDeleteEntity;
import su.opencode.kefir.gen.field.searchField.FilterConfigField;
import su.opencode.kefir.gen.field.searchField.SearchField;
import su.opencode.kefir.srv.json.ColumnModel;

@ExtEntity(
	listWindowTitle = "Список связанных сущностей",
	notChosenTitle = "Связанная сущность не выбрана",
	notChosenMessage = "Выберите связанную сущность",

	chooseWindowTitle = "Выбор связанной сущности",
	createWindowTitle = "Ввод связанной сущности",
	showWindowTitle = "Просмотр связанной сущности",
	updateWindowTitle = "Изменение связанной сущности",
	deleteWindowTitle = "Удаление связанной сущности",

	createSaveErrorMessage = "Ошибка при сохранении связанной сущности",
	updateSaveErrorMessage = "Ошибка при изменении связанной сущности",
	deleteSaveErrorMessage = "Ошибка при удалении связанной сущности",

	formWindowWidth = 800,

//	serviceClassName = GeneratorRunner.SERVICE_CLASS_NAME,
//	serviceBeanClassName = GeneratorRunner.SERVICE_BEAN_CLASS_NAME,
//	queryBuilderClassName = "su.opencode.kefir.generated.ChooseEntityQueryBuilder",
//	filterConfigClassName = "su.opencode.kefir.generated.ChooseEntityFilterConfig",

	preventDeleteEntities = {
		@PreventDeleteEntity(className = "su.opencode.kefir.gen.test.TestEntity", message = "Невозможно удалить связанную сущность, т.к. существует тестовая сущность, связанная с ней.")
	},

	listSecondRowButtons = {
		@SecondRowButton(text = "Перейти к списку тестовых сущностей", listEntityClassName = "su.opencode.kefir.gen.test.TestEntity")
	}
)
public class ChooseEntity
{
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getCorrespondentAccount() {
		return correspondentAccount;
	}
	public void setCorrespondentAccount(String correspondentAccount) {
		this.correspondentAccount = correspondentAccount;
	}

	@ColumnModel(header = "Get comboBoxEntity in ChooseEntity"/*, sortParam = "comboBoxEntity.name"*/)
	public ComboBoxEntity getComboBoxEntity() {
		return new ComboBoxEntity();
	}

	@ColumnModel(header = "olol", sortParam = "anotherFamily")
	public String getFamily(){
		return "family";
	}

	@ColumnModel(header = "newField")
	private String field;

	@IdField()
	private Integer id;

	@SearchField(label = "Наименование:", width = 250)
	@TextField(label = "Полное наименование", maxLength = 255, width = 300)
	private String name;

	/**
	 * Сокращенное наименование
	 */
	@TextField(label = "Сокращенное наименование", maxLength = 255, width = 300)
	private String shortName;

	/**
	 * Корреспондентский счет. 20 знаков.<br/>
	 * Последние три знака (18-й, 19-й, 20-й разряды) содержат 3-значный условный номер участника расчётов, соответствующий 7-му, 8-му, 9-му разрядам БИК.
	 */
	@ColumnModel(header = "aaaaaa")
	@TextField(label = "Корреспондентский счет", maxLength = 20, width = 300)
	private String correspondentAccount;

//	@FilterConfigField(name = "comboBoxEntityId", type = "Integer", filterFieldName = "id", sqlParamName = "comboBoxEntity.id", listWindowFilterTitle = "комбо сущность", listWindowTitleParamName = "cadastralNumber")
//	@ComboBoxField(
//		label = "Селект поле", url = "/comboBoxEntitysList", width = 400, maxLength = 25,
//
//		sortBy = "cadastralNumber",
//		queryParam = "cadastralNumber",
//		displayField = "cadastralNumber",
//		fields = {
//			@ComboBoxStoreField(name = "id", type = "int"),
//			@ComboBoxStoreField(name = "cadastralNumber", type = "string")
//		},
//		voFields = {
//			@VOField(name = "cadastralNumber", voFieldName = "comboFieldCadastralNumber") // sortable defaults to true
//		}
//	)
//	private ComboBoxEntity comboBoxEntity; // remote combobox
}
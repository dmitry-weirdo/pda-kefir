/**
 Copyright 2011 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package test;

import su.opencode.kefir.gen.ExtEntity;
import su.opencode.kefir.gen.field.*;
import su.opencode.kefir.gen.field.searchField.SearchField;
import su.opencode.kefir.gen.field.searchField.FilterConfigField;
import su.opencode.minstroy.ejb.leasing.Parcel;
import su.opencode.minstroy.ejb.leasing.Developer;
import su.opencode.minstroy.ejb.attachment.Attachment;
import su.opencode.kefir.srv.json.JsonObject;

import java.util.Date;
import java.util.List;

@ExtEntity(
	listWindowTitle = "Список тестовых сущностей",
	notChosenTitle = "Тестовая сущность не выбрана",
	notChosenMessage = "Выберите тестовую сущность",

	chooseWindowTitle = "Выбор тестовой сущности",
	createWindowTitle = "Ввод тестовой сущности",
	showWindowTitle = "Просмотр тестовой сущности",
	updateWindowTitle = "Изменение тестовой сущности",
	deleteWindowTitle = "Удаление тестовой сущности",

	createSaveErrorMessage = "Ошибка при сохранении тестовой сущности",
	updateSaveErrorMessage = "Ошибка при изменении тестовой сущности",
	deleteSaveErrorMessage = "Ошибка при удалении тестовой сущности"
)
public class TestEntity extends JsonObject
{
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getStrField() {
		return strField;
	}
	public void setStrField(String strField) {
		this.strField = strField;
	}
	public Integer getIntField() {
		return intField;
	}
	public void setIntField(Integer intField) {
		this.intField = intField;
	}
	public Integer getIntSpinnerField() {
		return intSpinnerField;
	}
	public void setIntSpinnerField(Integer intSpinnerField) {
		this.intSpinnerField = intSpinnerField;
	}
	public Double getDoubleField() {
		return doubleField;
	}
	public void setDoubleField(Double doubleField) {
		this.doubleField = doubleField;
	}
	public Date getDateField() {
		return dateField;
	}
	public void setDateField(Date dateField) {
		this.dateField = dateField;
	}
	public Boolean getBooleanField() {
		return booleanField;
	}
	public void setBooleanField(Boolean booleanField) {
		this.booleanField = booleanField;
	}
	public TestEnum getEnumField() {
		return enumField;
	}
	public void setEnumField(TestEnum enumField) {
		this.enumField = enumField;
	}
	public Parcel getComboField() {
		return comboField;
	}
	public void setComboField(Parcel comboField) {
		this.comboField = comboField;
	}
	public Developer getChooseField() {
		return chooseField;
	}
	public void setChooseField(Developer chooseField) {
		this.chooseField = chooseField;
	}
	public String getExcludedField() {
		return excludedField;
	}
	public void setExcludedField(String excludedField) {
		this.excludedField = excludedField;
	}

	@IdField()
	private Integer id;

	@SearchField(label = "Поиск по строковому полю", width = 250)
	@TextField( label = "Строковое поле", width = 200, maxLength = 200/*, uppercase = false, vtype = "mySuperStrVtype",  name = "otherTextFieldName"*/ )
	private String strField; // text field

	@FilterConfigField()
	@IntegerField( label = "Целое поле", width = 100, allowZero = true/*, allowNegative = true, vtype = "mySuperIntegerVtype", minValue = -200, maxValue = 666*/ )
	private Integer intField; // number field (integer)

	@SpinnerField( label = "Целое спиннер поле"/*, minValue = -13, maxValue = 666, vtype = "mySuperSpinnerVtype", allowNegative = true, defaultValue = 13, name = "otherSpinnerFieldFormName", renderer = "mySpinnerFieldRenderer", allowBlank = true, width = 100, maxLength = 3*/)
	private Integer intSpinnerField; // spinner filed

	@NumberField( label = "Дробное поле", width = 100/*, allowNegative = true, vtype = "mySuperIntegerVtype", minValue = -200.22, maxValue = 666.78*/ )
	private Double doubleField; // number field (float)

	@DateField( label = "Датовое поле"/*, minValue = "01.01.2010", maxValue = "02.02.2012", renderer = "myDateRenderer"*/ )
	private Date dateField; // date field

	@CheckboxField( label = "Булево поле", defaultValue = true )
	private Boolean booleanField; // checkbox

	@FilterConfigField()
	@LocalComboBoxField( label = "Энумовое поле", width = 200, maxLength = 100/*, hiddenName = "myLocalComboHiddenName", uppercase = true, editable = true, store = "myStore"*/ )
	private TestEnum enumField; // local combobox

	@ComboBoxField(
		label = "Селект поле", url = "/parcelsList", width = 400, maxLength = 25,

		displayField = "cadastralNumber",
		fields = {
			@ComboBoxStoreField(name = "id", type = "int"),
			@ComboBoxStoreField(name = "cadastralNumber", type = "string")
		},
		sortBy = "cadastralNumber"
	)
	private Parcel comboField; // remote combobox

	@FilterConfigField()
	@SearchField(label = "Поиск по имени связанной сущности", width = 250, chooseFieldFieldName = "name")
	@ChooseField(
		fieldSetName = "Связанная сущность",
		chooseButtonText = "Выбрать застройщика",
		chooseFunctionName = "su.opencode.minstroy.leasing.developer.DeveloperChoose.init",
//		chooseFunctionSuccessHandlerParamName = "chooseFieldChooseSuccessHandler",

		showButtonText = "Просмотреть застройщнка",
//		showFunctionName = "myForm.show",
//		showFunctionIdParamName = "chooseFieldEntityIdConfigParam",

		fields = {
			@ChooseFieldTextField(name = "name", label = "Поле выбора из другой сущности (имя)", width = 200, maxLength = 255, formPanelWidth = 450, labelWidth = 240),
			@ChooseFieldTextField(name = "shortName", label = "Поле выбора из другой сущности (краткое имя)", width = 200, maxLength = 255, formPanelWidth = 440, labelWidth = 150),
			@ChooseFieldTextField(name = "correspondentAccount", label = "Поле выбора из другой сущности (корр. счет)", width = 200, maxLength = 20, formPanelWidth = 450, labelWidth = 240)
		}
	)
	private Developer chooseField; // choose from entity choose form, save id

	
	private List<Attachment> attachmentsField;

	private String excludedField; // поле не содержит аннотации поля формы, и поэтому не будет участвовать в CRUD-форме
}
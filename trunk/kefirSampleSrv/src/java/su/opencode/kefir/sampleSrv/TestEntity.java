/**
 Copyright 2011 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.sampleSrv;

import su.opencode.kefir.gen.ExtEntity;
import su.opencode.kefir.gen.field.*;
import su.opencode.kefir.gen.field.searchField.FilterConfigField;
import su.opencode.kefir.gen.field.searchField.SearchField;
import su.opencode.kefir.gen.project.sql.SqlTableField;
import su.opencode.kefir.srv.attachment.Attachment;
import su.opencode.kefir.srv.json.JsonObject;

import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

import static su.opencode.kefir.sampleSrv.render.Renderers.TEST_ENUM_RENDERER;
import static su.opencode.kefir.srv.renderer.RenderersFactory.*;

@ExtEntity(
	listWindowTitle = "Список тестовых сущностей",
	listMainMenuButtonToolTip = "Тестовая сущность связана с комбо сущностью и со связанной сущностью",
	notChosenTitle = "Тестовая сущность не выбрана",
	notChosenMessage = "Выберите тестовую сущность",

	chooseWindowTitle = "Выбор тестовой сущности",

	createWindowTitle = "Ввод тестовой сущности",
	showWindowTitle = "Просмотр тестовой сущности",
	updateWindowTitle = "Изменение тестовой сущности",
	deleteWindowTitle = "Удаление тестовой сущности",

	formBindMask = true,

	createSaveErrorMessage = "Ошибка при сохранении тестовой сущности",
	updateSaveErrorMessage = "Ошибка при изменении тестовой сущности",
	deleteSaveErrorMessage = "Ошибка при удалении тестовой сущности",

//	serviceClassName = GeneratorRunner.SERVICE_CLASS_NAME,
//	serviceBeanClassName = GeneratorRunner.SERVICE_BEAN_CLASS_NAME,
	queryBuilderClassName = "su.opencode.kefir.generated.TestEntityQueryBuilder",
	filterConfigClassName = "su.opencode.kefir.generated.TestEntityFilterConfig"//,
)
public class TestEntity extends JsonObject {

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
	public ComboBoxEntity getComboBoxEntity() {
		return comboBoxEntity;
	}
	public void setComboBoxEntity(ComboBoxEntity comboBoxEntity) {
		this.comboBoxEntity = comboBoxEntity;
	}
	public ChooseEntity getChooseEntity() {
		return chooseEntity;
	}
	public void setChooseEntity(ChooseEntity chooseEntity) {
		this.chooseEntity = chooseEntity;
	}

	@Transient
	public List<Attachment> getAttachmentsField() {
		return attachmentsField;
	}
	public void setAttachmentsField(List<Attachment> attachmentsField) {
		this.attachmentsField = attachmentsField;
	}
	@Transient
	public List<Attachment> getOtherAttachmentsField() {
		return otherAttachmentsField;
	}
	public void setOtherAttachmentsField(List<Attachment> otherAttachmentsField) {
		this.otherAttachmentsField = otherAttachmentsField;
	}
	public Long getOgrn() {
		return ogrn;
	}
	public void setOgrn(Long ogrn) {
		this.ogrn = ogrn;
	}
	public Long getKpp() {
		return kpp;
	}
	public void setKpp(Long kpp) {
		this.kpp = kpp;
	}
	public Long getInn() {
		return inn;
	}
	public void setInn(Long inn) {
		this.inn = inn;
	}
	public Address getJuridicalAddress() {
		return juridicalAddress;
	}
	public void setJuridicalAddress(Address juridicalAddress) {
		this.juridicalAddress = juridicalAddress;
	}
	public Address getPhysicalAddress() {
		return physicalAddress;
	}
	public void setPhysicalAddress(Address physicalAddress) {
		this.physicalAddress = physicalAddress;
	}
	public String getExcludedField() {
		return excludedField;
	}
	public void setExcludedField(String excludedField) {
		this.excludedField = excludedField;
	}

	@IdField()
	private Integer id;

	@SearchField(label = "Поиск по строковому полю", width = 250/*, sqlParamName = "mySuperSqlName"*/)
	@TextField(label = "Строковое поле", width = 200, maxLength = 200/*, uppercase = false, vtype = "mySuperStrVtype",  name = "otherTextFieldName"*/)
	private String strField; // text field

	@IntegerField(label = "Целое поле", width = 100, allowZero = true/*, allowNegative = true, vtype = "mySuperIntegerVtype", minValue = -200, maxValue = 666*/)
	private Integer intField; // number field (integer)

	@SpinnerField(label = "Целое спиннер поле"/*, minValue = -13, maxValue = 666, vtype = "mySuperSpinnerVtype", allowNegative = true, defaultValue = 13, name = "otherSpinnerFieldFormName", renderer = "mySpinnerFieldRenderer", allowBlank = true, width = 100, maxLength = 3*/)
	private Integer intSpinnerField; // spinner filed

	//	@FilterConfigField(/*name = "mySuperDoubleField", sqlParamName = "mySuperDoubleSqlParam", type = "MySuperDoubleType"*/)
	@NumberField(label = "Дробное поле", renderer = FLOAT_RENDERER, width = 100/*, allowNegative = true, vtype = "mySuperIntegerVtype", minValue = -200.22, maxValue = 666.78*/)
	private Double doubleField; // number field (float)

	@DateField(label = "Датовое поле", renderer = DATE_RENDERER/*, minValue = "01.01.2010", maxValue = "02.02.2012", renderer = "myDateRenderer"*/, minValue = DateField.NULL_DATE_VALUE, maxValue = DateField.NULL_DATE_VALUE)
	private Date dateField; // date field

	@CheckboxField(label = "Булево поле", defaultValue = true, renderer = SEX_RENDERER)
	private Boolean booleanField; // checkbox

	//	@SearchField(label = "Поиск по энумовому полю")
//	@FilterConfigField(listWindowFilterTitle = "энумовое поле")
	@LocalComboBoxField(label = "Энумовое поле", renderer = TEST_ENUM_RENDERER, width = 200, maxLength = 100/*, hiddenName = "myLocalComboHiddenName", uppercase = true, editable = true, store = "myStore", displayField = "myDisplayField", valueField = "myValueField"*/)
	private TestEnum enumField; // local combobox

	@FilterConfigField(name = "comboBoxEntityId", type = "Integer", filterFieldName = "id", sqlParamName = "comboBoxEntity.id", listWindowFilterTitle = "комбо сущность", listWindowTitleParamName = "cadastralNumber")
	@ComboBoxField(
		label = "Селект поле", url = "/comboBoxEntitysList", width = 400, maxLength = 25,

		sortBy = "cadastralNumber",
		queryParam = "cadastralNumber",
		displayField = "cadastralNumber",
		fields = {
			@ComboBoxStoreField(name = "id", type = "int"),
			@ComboBoxStoreField(name = "cadastralNumber", type = "string")
		}
	)
	private ComboBoxEntity comboBoxEntity; // remote combobox

	@FilterConfigField(listWindowFilterTitle = "связанная сущность"/*chooseFieldFieldName = "name", chooseFieldFieldType = "MyGreatType"*//*, name = "mySuperChooseFieldIdFilterConfigField", sqlParamName = "chooseEntity.greatSqlParam"*/)
	@SearchField(label = "Поиск по имени связанной сущности", width = 250, chooseFieldFieldName = "name"/*, sqlParamName = "myChooseField.mySuperChooseFieldSqlFieldName"*/)
	@ChooseField(
		fieldSetName = "Связанная сущность",
//		chooseButtonText = "Выбрать связанную сущность",
//		chooseFunctionName = "myChoose.choose",
//		chooseFunctionSuccessHandlerParamName = "chooseFieldChooseSuccessHandler",

//		showButtonText = "Просмотреть связанную сущность",
//		showFunctionName = "myForm.show",
//		showFunctionIdParamName = "chooseFieldEntityIdConfigParam",

		fields = {
			@ChooseFieldTextField(name = "name", label = "Поле выбора из связанной сущности (имя)", width = 200, maxLength = 255, formPanelWidth = 450, labelWidth = 240),
			@ChooseFieldTextField(name = "shortName", label = "Поле выбора из связанной сущности (краткое имя)", width = 200, maxLength = 255, formPanelWidth = 440, labelWidth = 150),
			@ChooseFieldTextField(name = "correspondentAccount", label = "Поле выбора из связанной сущности (корр. счет)", width = 200, maxLength = 20, formPanelWidth = 450, labelWidth = 240)
		}
	)
	private ChooseEntity chooseEntity; // choose from entity choose form, save id

	@AttachmentsField()
	private List<Attachment> attachmentsField;

	@AttachmentsField(fieldSetTitle = "Другие файловые вложения")
	private List<Attachment> otherAttachmentsField;


	@OgrnTextField(width = 150)
	private Long ogrn;

	@KppTextField(width = 150)
	private Long kpp;

	@InnJuridicalTextField(width = 150)
	private Long inn;

	@AddressField(textFieldLabel = "Юридический адрес", addressWindowTitle = "Юридический адрес тестовой сущности"/*, getAddressStrFunctionName = "getJuridicalAddressStr", initFunctionName = "initJuridicalAddress"*/)
	private Address juridicalAddress;

	@AddressField(textFieldLabel = "Фактический адрес", addressWindowTitle = "Фактический адрес тестовой сущности")
	private Address physicalAddress;

	@SqlColumn(type = SqlTableField.VARCHAR_TYPE, length = 100)
	private String excludedField; // поле не содержит аннотации поля формы, и поэтому не будет участвовать в CRUD-форме
}
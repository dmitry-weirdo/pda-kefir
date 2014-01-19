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
	listWindowTitle = "������ �������� ���������",
	listMainMenuButtonToolTip = "�������� �������� ������� � ����� ��������� � �� ��������� ���������",
	notChosenTitle = "�������� �������� �� �������",
	notChosenMessage = "�������� �������� ��������",

	chooseWindowTitle = "����� �������� ��������",

	createWindowTitle = "���� �������� ��������",
	showWindowTitle = "�������� �������� ��������",
	updateWindowTitle = "��������� �������� ��������",
	deleteWindowTitle = "�������� �������� ��������",

	formBindMask = true,

	createSaveErrorMessage = "������ ��� ���������� �������� ��������",
	updateSaveErrorMessage = "������ ��� ��������� �������� ��������",
	deleteSaveErrorMessage = "������ ��� �������� �������� ��������",

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

	@SearchField(label = "����� �� ���������� ����", width = 250/*, sqlParamName = "mySuperSqlName"*/)
	@TextField(label = "��������� ����", width = 200, maxLength = 200/*, uppercase = false, vtype = "mySuperStrVtype",  name = "otherTextFieldName"*/)
	private String strField; // text field

	@IntegerField(label = "����� ����", width = 100, allowZero = true/*, allowNegative = true, vtype = "mySuperIntegerVtype", minValue = -200, maxValue = 666*/)
	private Integer intField; // number field (integer)

	@SpinnerField(label = "����� ������� ����"/*, minValue = -13, maxValue = 666, vtype = "mySuperSpinnerVtype", allowNegative = true, defaultValue = 13, name = "otherSpinnerFieldFormName", renderer = "mySpinnerFieldRenderer", allowBlank = true, width = 100, maxLength = 3*/)
	private Integer intSpinnerField; // spinner filed

	//	@FilterConfigField(/*name = "mySuperDoubleField", sqlParamName = "mySuperDoubleSqlParam", type = "MySuperDoubleType"*/)
	@NumberField(label = "������� ����", renderer = FLOAT_RENDERER, width = 100/*, allowNegative = true, vtype = "mySuperIntegerVtype", minValue = -200.22, maxValue = 666.78*/)
	private Double doubleField; // number field (float)

	@DateField(label = "������� ����", renderer = DATE_RENDERER/*, minValue = "01.01.2010", maxValue = "02.02.2012", renderer = "myDateRenderer"*/, minValue = DateField.NULL_DATE_VALUE, maxValue = DateField.NULL_DATE_VALUE)
	private Date dateField; // date field

	@CheckboxField(label = "������ ����", defaultValue = true, renderer = SEX_RENDERER)
	private Boolean booleanField; // checkbox

	//	@SearchField(label = "����� �� ��������� ����")
//	@FilterConfigField(listWindowFilterTitle = "�������� ����")
	@LocalComboBoxField(label = "�������� ����", renderer = TEST_ENUM_RENDERER, width = 200, maxLength = 100/*, hiddenName = "myLocalComboHiddenName", uppercase = true, editable = true, store = "myStore", displayField = "myDisplayField", valueField = "myValueField"*/)
	private TestEnum enumField; // local combobox

	@FilterConfigField(name = "comboBoxEntityId", type = "Integer", filterFieldName = "id", sqlParamName = "comboBoxEntity.id", listWindowFilterTitle = "����� ��������", listWindowTitleParamName = "cadastralNumber")
	@ComboBoxField(
		label = "������ ����", url = "/comboBoxEntitysList", width = 400, maxLength = 25,

		sortBy = "cadastralNumber",
		queryParam = "cadastralNumber",
		displayField = "cadastralNumber",
		fields = {
			@ComboBoxStoreField(name = "id", type = "int"),
			@ComboBoxStoreField(name = "cadastralNumber", type = "string")
		}
	)
	private ComboBoxEntity comboBoxEntity; // remote combobox

	@FilterConfigField(listWindowFilterTitle = "��������� ��������"/*chooseFieldFieldName = "name", chooseFieldFieldType = "MyGreatType"*//*, name = "mySuperChooseFieldIdFilterConfigField", sqlParamName = "chooseEntity.greatSqlParam"*/)
	@SearchField(label = "����� �� ����� ��������� ��������", width = 250, chooseFieldFieldName = "name"/*, sqlParamName = "myChooseField.mySuperChooseFieldSqlFieldName"*/)
	@ChooseField(
		fieldSetName = "��������� ��������",
//		chooseButtonText = "������� ��������� ��������",
//		chooseFunctionName = "myChoose.choose",
//		chooseFunctionSuccessHandlerParamName = "chooseFieldChooseSuccessHandler",

//		showButtonText = "����������� ��������� ��������",
//		showFunctionName = "myForm.show",
//		showFunctionIdParamName = "chooseFieldEntityIdConfigParam",

		fields = {
			@ChooseFieldTextField(name = "name", label = "���� ������ �� ��������� �������� (���)", width = 200, maxLength = 255, formPanelWidth = 450, labelWidth = 240),
			@ChooseFieldTextField(name = "shortName", label = "���� ������ �� ��������� �������� (������� ���)", width = 200, maxLength = 255, formPanelWidth = 440, labelWidth = 150),
			@ChooseFieldTextField(name = "correspondentAccount", label = "���� ������ �� ��������� �������� (����. ����)", width = 200, maxLength = 20, formPanelWidth = 450, labelWidth = 240)
		}
	)
	private ChooseEntity chooseEntity; // choose from entity choose form, save id

	@AttachmentsField()
	private List<Attachment> attachmentsField;

	@AttachmentsField(fieldSetTitle = "������ �������� ��������")
	private List<Attachment> otherAttachmentsField;


	@OgrnTextField(width = 150)
	private Long ogrn;

	@KppTextField(width = 150)
	private Long kpp;

	@InnJuridicalTextField(width = 150)
	private Long inn;

	@AddressField(textFieldLabel = "����������� �����", addressWindowTitle = "����������� ����� �������� ��������"/*, getAddressStrFunctionName = "getJuridicalAddressStr", initFunctionName = "initJuridicalAddress"*/)
	private Address juridicalAddress;

	@AddressField(textFieldLabel = "����������� �����", addressWindowTitle = "����������� ����� �������� ��������")
	private Address physicalAddress;

	@SqlColumn(type = SqlTableField.VARCHAR_TYPE, length = 100)
	private String excludedField; // ���� �� �������� ��������� ���� �����, � ������� �� ����� ����������� � CRUD-�����
}
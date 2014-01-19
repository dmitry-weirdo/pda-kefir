/*
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 06.04.2012 17:29:18$
*/
package su.opencode.kefir.gen.test;

import su.opencode.kefir.gen.ExtEntity;
import su.opencode.kefir.gen.field.*;
import su.opencode.kefir.gen.field.searchField.FilterConfigField;
import su.opencode.kefir.gen.field.searchField.SearchField;
import su.opencode.kefir.gen.project.sql.SqlTableField;
import su.opencode.kefir.srv.attachment.Attachment;
import su.opencode.kefir.srv.json.*;
import su.opencode.kefir.srv.renderer.RenderersFactory;

import javax.persistence.Transient;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static su.opencode.kefir.srv.renderer.RenderersFactory.FLOAT_RENDERER;

@ExtEntity(
	sqlTableComment = "Test entity for kefir gen",

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

	listVoClassName = "su.TestEntityOtherVO",

	createSaveErrorMessage = "������ ��� ���������� �������� ��������",
	updateSaveErrorMessage = "������ ��� ��������� �������� ��������",
	deleteSaveErrorMessage = "������ ��� �������� �������� ��������",

//	serviceClassName = GeneratorRunner.SERVICE_CLASS_NAME,
//	serviceBeanClassName = GeneratorRunner.SERVICE_BEAN_CLASS_NAME,
	queryBuilderClassName = "su.opencode.kefir.generated.TestEntityQueryBuilder",
	filterConfigClassName = "su.opencode.kefir.generated.TestEntityFilterConfig"//,

	, hasListMainMenuButton = false
)
@ColumnGroups(groups = {
	@ColumnGroup(id = "permission", header = "���������� �� �������������"),
	@ColumnGroup(id = "holdersCount", header = "���������� ������������"),
	@ColumnGroup(id = "readiness", header = "����������"),
	@ColumnGroup(id = "curators", header = "��������"),
	@ColumnGroup(id = "actionTeamHead", header = "������������ ������������ ������")
})
@FieldSets(fieldSets = {
	@FieldSet(id = "permission", title = "���������� �� �������������")
//	@FieldSet(id = "holdersCount", title = "���������� ������������"),
//	@FieldSet(id = "readiness", title = "����������"),
//	@FieldSet(id = "curators", title = "��������"),
//	@FieldSet(id = "actionTeamHead", title = "������������ ������������ ������")
})
@ViewConfig(
	  viewConfig = "ru.kg.gtn.viewConfig.vehicleViewConfig"
	, legendLabel = "��������� ������: "
	, legendLabelVOField = "carNumber"
  , legendFields = {
		  @LegendField(fieldName = "inTheft", legendLabel = "� �����", rowClassName = "theft666", backgroundColor = "#000000", color = "#FFFFFF", legendClassName = "ffff")
		, @LegendField(fieldName = "hasRestrictions", legendLabel = "�����������", rowClassName = "restricted", backgroundColor = "#00FFFF")
		, @LegendField(fieldName = "pawned", legendLabel = "� ������", backgroundColor = "#A6CAF0")
		, @LegendField(fieldName = "checkupPassed", legendLabel = "��� ��", checkTrue = false, rowClassName = "checkupNotPassed", backgroundColor = "#C0DCC0", legendClassName = "legendCheckupClass")
	}
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
//	public ComboBoxEntity getComboBoxEntity() {
//		return comboBoxEntity;
//	}
//	public void setComboBoxEntity(ComboBoxEntity comboBoxEntity) {
//		this.comboBoxEntity = comboBoxEntity;
//	}
//	public ChooseEntity getChooseEntity() {
//		return chooseEntity;
//	}
//	public void setChooseEntity(ChooseEntity chooseEntity) {
//		this.chooseEntity = chooseEntity;
//	}

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
	public Collection getCollectionField() {
		return collectionField;
	}
	public void setCollectionField(Collection collectionField) {
		this.collectionField = collectionField;
	}

	@ColumnModel(header = "jjjjjj", renderer = RenderersFactory.DONT_SHOW_NIL_RENDERER, groupId = "actionTeamHead", tooltip = "tooltip")
	public String getInfo() {
		return "info";
	}
	@IdField()
	private Integer id;

	@SearchField(label = "����� �� ���������� ����", width = 250/*, sqlParamName = "mySuperSqlName"*/)
	@TextField(label = "��������� ����", width = 200, maxLength = 200/*, uppercase = false, vtype = "mySuperStrVtype",  name = "otherTextFieldName"*/, fieldSetId = "permission")
	private String strField; // text field

	@IntegerField(label = "����� ����", width = 100, allowZero = true/*, allowNegative = true, vtype = "mySuperIntegerVtype", minValue = -200, maxValue = 666*/, fieldSetId = "permission")
	private Integer intField; // number field (integer)

	@SpinnerField(label = "����� ������� ����"/*, minValue = -13, maxValue = 666, vtype = "mySuperSpinnerVtype", allowNegative = true, defaultValue = 13, name = "otherSpinnerFieldFormName", renderer = "mySpinnerFieldRenderer", allowBlank = true, width = 100, maxLength = 3*/)
	private Integer intSpinnerField; // spinner filed

//	@FilterConfigField(/*name = "mySuperDoubleField", sqlParamName = "mySuperDoubleSqlParam", type = "MySuperDoubleType"*/)
	@NumberField(label = "������� ����", width = 100/*, allowNegative = true, vtype = "mySuperIntegerVtype", minValue = -200.22, maxValue = 666.78*/)
	private Double doubleField; // number field (float)

	@DateField(label = "������� ����", renderer = RenderersFactory.FLOAT_RENDERER/*, minValue = "01.01.2010", maxValue = "02.02.2012", renderer = "myDateRenderer"*/, minValue = DateField.NULL_DATE_VALUE, maxValue = DateField.NULL_DATE_VALUE)
	private Date dateField; // date field

	@CheckboxField(label = "������ ����", defaultValue = true)
	private Boolean booleanField; // checkbox

	//	@SearchField(label = "����� �� ��������� ����")
//	@FilterConfigField(listWindowFilterTitle = "�������� ����")
	@LocalComboBoxField(label = "�������� ����", width = 200, maxLength = 100/*, hiddenName = "myLocalComboHiddenName", uppercase = true, editable = true, store = "myStore", displayField = "myDisplayField", valueField = "myValueField"*/)
	private TestEnum enumField; // local combobox

	@ColumnModelExclude
	@FilterConfigField(name = "comboBoxEntityId", type = "Integer", filterFieldName = "id", sqlParamName = "comboBoxEntity.id", listWindowFilterTitle = "����� ��������", listWindowTitleParamName = "cadastralNumber")
	@ComboBoxField(
		label = "������ ����", url = "/comboBoxEntitysList", width = 400, maxLength = 25,

		sortBy = "cadastralNumber",
		queryParam = "cadastralNumber",
		displayField = "cadastralNumber",
		fields = {
			@ComboBoxStoreField(name = "id", type = "int"),
			@ComboBoxStoreField(name = "cadastralNumber", type = "string")
		},
		voFields = {
			@VOField(name = "cadastralNumber", voFieldName = "comboFieldCadastralNumber") // sortable defaults to true
		}
	)
	private ComboBoxEntity comboBoxEntity; // remote combobox

	@ColumnModelExclude
	@FilterConfigField(listWindowFilterTitle = "��������� ��������"/*chooseFieldFieldName = "name", chooseFieldFieldType = "MyGreatType"*//*, name = "mySuperChooseFieldIdFilterConfigField", sqlParamName = "chooseEntity.greatSqlParam"*/)
	@SearchField(label = "����� �� ����� ��������� ��������", width = 250, chooseFieldFieldName = "name"/*, sqlParamName = "myChooseField.mySuperChooseFieldSqlFieldName"*/)
	@ChooseField(
		fieldSetName = "��������� ��������", fieldSetId = "permission",
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
		, voFields = {
			@VOField(name = "comboBoxEntity.otherChooseEntity.shortName", groupId = "actionTeamHead", tooltip = "qwerty"),
			@VOField(name = "name"),
			@VOField(name = "shortName"),
			@VOField(name = "correspondentAccount", sortable = false), // sortable defaults to true
			@VOField(name = "comboBoxEntity.cadastralNumber", sortable = false),
			@VOField(name = "comboBoxEntity.comboName")
		}
	)
	private ChooseEntity chooseEntity; // choose from entity choose form, save id

	@AttachmentsField()
	private List<Attachment> attachmentsField;

	@AttachmentsField(fieldSetTitle = "������ �������� ��������", fieldSetId = "permission")
	private List<Attachment> otherAttachmentsField;

	private TestEntity testEntity;

	@ColumnModel( header = "���", renderer = FLOAT_RENDERER, sortable = false, sortParam = "dudtrsijodtiojdst", groupId = "readiness", tooltip = "hzhz")
	@OgrnTextField(width = 150)
	private Long ogrn;

	@ColumnModelExclude
	@KppTextField(width = 150)
	private Long kpp;

	@InnJuridicalTextField(width = 150)
	private Long inn;

	@ColumnModelExclude
	@AddressField(textFieldLabel = "����������� �����", addressWindowTitle = "����������� ����� �������� ��������", fieldSetId = "permission"/*, getAddressStrFunctionName = "getJuridicalAddressStr", initFunctionName = "initJuridicalAddress"*/)
	private Address juridicalAddress;

	@AddressField(textFieldLabel = "����������� �����", addressWindowTitle = "����������� ����� �������� ��������")
	private Address physicalAddress;

	@ColumnModel(header = "excluded_field", renderer = FLOAT_RENDERER, sortParam = "name")
	@SqlColumn(type = SqlTableField.VARCHAR_TYPE, length = 100)
	private String excludedField; // ���� �� �������� ��������� ���� �����, � ������� �� ����� ����������� � CRUD-�����

	private Collection collectionField;

	@ColumnModelExclude
	private String newField;
}
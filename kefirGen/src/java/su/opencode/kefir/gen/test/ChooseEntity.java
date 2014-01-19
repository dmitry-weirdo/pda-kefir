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
	listWindowTitle = "������ ��������� ���������",
	notChosenTitle = "��������� �������� �� �������",
	notChosenMessage = "�������� ��������� ��������",

	chooseWindowTitle = "����� ��������� ��������",
	createWindowTitle = "���� ��������� ��������",
	showWindowTitle = "�������� ��������� ��������",
	updateWindowTitle = "��������� ��������� ��������",
	deleteWindowTitle = "�������� ��������� ��������",

	createSaveErrorMessage = "������ ��� ���������� ��������� ��������",
	updateSaveErrorMessage = "������ ��� ��������� ��������� ��������",
	deleteSaveErrorMessage = "������ ��� �������� ��������� ��������",

	formWindowWidth = 800,

//	serviceClassName = GeneratorRunner.SERVICE_CLASS_NAME,
//	serviceBeanClassName = GeneratorRunner.SERVICE_BEAN_CLASS_NAME,
//	queryBuilderClassName = "su.opencode.kefir.generated.ChooseEntityQueryBuilder",
//	filterConfigClassName = "su.opencode.kefir.generated.ChooseEntityFilterConfig",

	preventDeleteEntities = {
		@PreventDeleteEntity(className = "su.opencode.kefir.gen.test.TestEntity", message = "���������� ������� ��������� ��������, �.�. ���������� �������� ��������, ��������� � ���.")
	},

	listSecondRowButtons = {
		@SecondRowButton(text = "������� � ������ �������� ���������", listEntityClassName = "su.opencode.kefir.gen.test.TestEntity")
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

	@SearchField(label = "������������:", width = 250)
	@TextField(label = "������ ������������", maxLength = 255, width = 300)
	private String name;

	/**
	 * ����������� ������������
	 */
	@TextField(label = "����������� ������������", maxLength = 255, width = 300)
	private String shortName;

	/**
	 * ����������������� ����. 20 ������.<br/>
	 * ��������� ��� ����� (18-�, 19-�, 20-� �������) �������� 3-������� �������� ����� ��������� ��������, ��������������� 7-��, 8-��, 9-�� �������� ���.
	 */
	@ColumnModel(header = "aaaaaa")
	@TextField(label = "����������������� ����", maxLength = 20, width = 300)
	private String correspondentAccount;

//	@FilterConfigField(name = "comboBoxEntityId", type = "Integer", filterFieldName = "id", sqlParamName = "comboBoxEntity.id", listWindowFilterTitle = "����� ��������", listWindowTitleParamName = "cadastralNumber")
//	@ComboBoxField(
//		label = "������ ����", url = "/comboBoxEntitysList", width = 400, maxLength = 25,
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
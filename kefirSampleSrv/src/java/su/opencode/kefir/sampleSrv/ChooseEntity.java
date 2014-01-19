/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 19.03.2012 17:39:06$
*/
package su.opencode.kefir.sampleSrv;

import su.opencode.kefir.gen.ExtEntity;
import su.opencode.kefir.gen.field.IdField;
import su.opencode.kefir.gen.field.TextField;
import su.opencode.kefir.gen.field.TextAreaField;
import su.opencode.kefir.gen.field.button.SecondRowButton;
import su.opencode.kefir.gen.field.linkedEntity.PreventDeleteEntity;
import su.opencode.kefir.gen.field.searchField.SearchField;
import su.opencode.kefir.srv.json.JsonObject;

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
	queryBuilderClassName = "su.opencode.kefir.generated.ChooseEntityQueryBuilder",
	filterConfigClassName = "su.opencode.kefir.generated.ChooseEntityFilterConfig",

	preventDeleteEntities = {
		@PreventDeleteEntity(className = "su.opencode.kefir.sampleSrv.TestEntity", message = "���������� ������� ��������� ��������, �.�. ���������� �������� ��������, ��������� � ���.")
	},

	listSecondRowButtons = {
		@SecondRowButton(text = "������� � ������ �������� ���������", listEntityClassName = "su.opencode.kefir.sampleSrv.TestEntity")
	}
)
public class ChooseEntity extends JsonObject
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
	public String getInfo(){
		return info;
	}
	public void setInfo(String info){
		this.info = info;
	}

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
	@TextField(label = "����������������� ����", maxLength = 20, width = 300)
	private String correspondentAccount;

	@TextAreaField(label = "����������", maxLength = 100, width = 300, rows = 10, cols = 20)
	private String info;
}
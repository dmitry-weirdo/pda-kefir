/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 19.03.2012 17:42:22$
*/
package su.opencode.kefir.sampleSrv;

import su.opencode.kefir.gen.ExtEntity;
import su.opencode.kefir.gen.field.IdField;
import su.opencode.kefir.gen.field.TextField;
import su.opencode.kefir.gen.field.button.SecondRowButton;
import su.opencode.kefir.gen.field.linkedEntity.PreventDeleteEntity;
import su.opencode.kefir.gen.field.searchField.SearchField;
import su.opencode.kefir.srv.json.JsonObject;

@ExtEntity(
	listWindowTitle = "������ ����� ���������",
	notChosenTitle = "����� �������� �� �������",
	notChosenMessage = "�������� ����� ��������",

	chooseWindowTitle = "����� ����� ��������",
	createWindowTitle = "���� ����� ��������",
	showWindowTitle = "�������� ����� ��������",
	updateWindowTitle = "��������� ����� ��������",
	deleteWindowTitle = "�������� ����� ��������",

	createSaveErrorMessage = "������ ��� ���������� ����� ��������",
	updateSaveErrorMessage = "������ ��� ��������� ����� ��������",
	deleteSaveErrorMessage = "������ ��� �������� ����� ��������",

	formWindowWidth = 800,

//	serviceClassName = GeneratorRunner.SERVICE_CLASS_NAME,
//	serviceBeanClassName = GeneratorRunner.SERVICE_BEAN_CLASS_NAME,
	queryBuilderClassName = "su.opencode.kefir.generated.ComboBoxEntityQueryBuilder",
	filterConfigClassName = "su.opencode.kefir.generated.ComboBoxEntityFilterConfig",

	preventDeleteEntities = {
		@PreventDeleteEntity(className = "su.opencode.kefir.sampleSrv.TestEntity", message = "���������� ������� ����� ��������, �.�. ���������� �������� ��������, ��������� � ���.")
	},

	listSecondRowButtons = {
		@SecondRowButton(text = "������� � ������ �������� ���������", listEntityClassName = "su.opencode.kefir.sampleSrv.TestEntity")
	}
)
public class ComboBoxEntity extends JsonObject
{
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCadastralNumber() {
		return cadastralNumber;
	}
	public void setCadastralNumber(String cadastralNumber) {
		this.cadastralNumber = cadastralNumber;
	}

	@IdField()
	private Integer id;

	/**
	 * ����������� �����. <br/>
	 * ����� ������ ������������×ė��, ��� <br/>
	 * �� � ��� ������� (��� �� ������������� �������),<br/>
	 * �� � ��� ������ ��� ������ (��������, � ������ 01 ����, 02 � ����, 09 � ���, 10 � ����������);<br/>
	 * ����� � ����� �������� ��� ���������� �������;<br/>
	 * ��� � ����� ���������� ������� ��� ������;<br/>
	 * � � ����� (������) ����������;<br/>
	 * �� � ����� ��������� � ����������.
	 */
	@SearchField(label = "����������� �����:", width = 250)
	@TextField(label = "����������� �����", width = 200, maxLength = 25)
	private String cadastralNumber;
}
/*
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 16.04.2012 16:47:13$
*/
package su.opencode.kefir.gen.test;

import su.opencode.kefir.gen.ExtEntity;
import su.opencode.kefir.gen.field.IdField;
import su.opencode.kefir.gen.field.TextField;
import su.opencode.kefir.gen.field.button.SecondRowButton;
import su.opencode.kefir.gen.field.linkedEntity.PreventDeleteEntity;
import su.opencode.kefir.gen.field.searchField.SearchField;
import su.opencode.kefir.srv.json.ColumnModel;
import su.opencode.kefir.srv.json.ColumnModelExclude;

import java.util.List;

import static su.opencode.kefir.srv.renderer.RenderersFactory.FLOAT_RENDERER;

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
//	queryBuilderClassName = "su.opencode.kefir.generated.ComboBoxEntityQueryBuilder",
//	filterConfigClassName = "su.opencode.kefir.generated.ComboBoxEntityFilterConfig",

	preventDeleteEntities = {
		@PreventDeleteEntity(className = "su.opencode.kefir.gen.test.TestEntity", message = "���������� ������� ����� ��������, �.�. ���������� �������� ��������, ��������� � ���.")
	},

	listSecondRowButtons = {
		@SecondRowButton(text = "������� � ������ �������� ���������", listEntityClassName = "su.opencode.kefir.gen.test.TestEntity")
	}
)
public class ComboBoxEntity
{
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public List getCadastralNumber() {
		return cadastralNumber;
	}
	public void setCadastralNumber(List cadastralNumber) {
		this.cadastralNumber = cadastralNumber;
	}
	public String getComboName() {
		return comboName;
	}
	public void setComboName(String comboName) {
		this.comboName = comboName;
	}
	public ChooseEntity getOtherChooseEntity() {
		return otherChooseEntity;
	}
	public void setOtherChooseEntity(ChooseEntity otherChooseEntity) {
		this.otherChooseEntity = otherChooseEntity;
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
	@ColumnModelExclude
	@ColumnModel( header = "���", renderer = FLOAT_RENDERER, sortable = false, sortParam = "dudtrsijodtiojdst" )
	@SearchField(label = "����������� �����:", width = 250)
	@TextField(label = "����������� �����", width = 200, maxLength = 25)
	private List cadastralNumber;

	@ColumnModel(header = "combo box entity combo name")
	private String comboName;

	@ColumnModel(header = "combo box entity other choose entity")
	private ChooseEntity otherChooseEntity;
}
/**
 Copyright 2011 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package test;

import su.opencode.kefir.srv.VO;
import su.opencode.kefir.srv.json.ColumnModel;
import static su.opencode.kefir.srv.json.ColumnModel.DATE_RENDERER;
import static su.opencode.kefir.srv.json.ColumnModel.BOOLEAN_RENDERER;
import static su.opencode.kefir.srv.json.ColumnModel.FLOAT_RENDERER;

import su.opencode.minstroy.ejb.leasing.Developer;
import su.opencode.minstroy.ejb.leasing.Parcel;

import java.util.Date;

public class TestEntityVO extends VO
{
	public TestEntityVO() {
	}
	public TestEntityVO(TestEntity testEntity) {
		super(testEntity);

		Parcel comboField = this.comboField = testEntity.getComboField();
		if (comboField != null)
			this.comboFieldCadastralNumber = comboField.getCadastralNumber();

		Developer chooseField = testEntity.getChooseField();
		if (chooseField != null)
			this.chooseFieldName = chooseField.getName();	   	
	}

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
	public String getComboFieldCadastralNumber() {
		return comboFieldCadastralNumber;
	}
	public void setComboFieldCadastralNumber(String comboFieldCadastralNumber) {
		this.comboFieldCadastralNumber = comboFieldCadastralNumber;
	}
	public Developer getChooseField() {
		return chooseField;
	}
	public void setChooseField(Developer chooseField) {
		this.chooseField = chooseField;
	}
	public String getChooseFieldName() {
		return chooseFieldName;
	}
	public void setChooseFieldName(String chooseFieldName) {
		this.chooseFieldName = chooseFieldName;
	}
	
	private Integer id;

	@ColumnModel(header = "��������� ����")
	private String strField; // text field

	@ColumnModel(header = "������������� ����")
	private Integer intField; // number field (integer)

	@ColumnModel(header = "������������� ������� ����")
	private Integer intSpinnerField; // spinner filed

	@ColumnModel(header = "������� ����", renderer = FLOAT_RENDERER)
	private Double doubleField; // number field (float)

	@ColumnModel(header = "������� ����", renderer = DATE_RENDERER)
	private Date dateField; // date field

	@ColumnModel(header = "������ ����", renderer = BOOLEAN_RENDERER)
	private Boolean booleanField; // checkbox

	@ColumnModel(header = "���� ���� (��������� ���������)", renderer = "ru.kg.gtn.render.TestEnumRenderer")
	private TestEnum enumField; // local combobox

	// combo field
	private Parcel comboField; // remote combobox

	@ColumnModel(header = "������ ���� (������ ���������)")
	private String comboFieldCadastralNumber;

	// choose field
	private Developer chooseField; // choose from entity choose form, save id

	@ColumnModel(header = "���� ������ �� ������ ��������")
	private String chooseFieldName;	
}
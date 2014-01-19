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
import su.opencode.minstroy.ejb.leasing.Parcel;
import su.opencode.minstroy.ejb.leasing.Developer;
import su.opencode.kefir.srv.json.JsonObject;

import java.util.Date;

@ExtEntity(
	jsFieldPrefix = "testEntity",

	listWindowTitle = "Список тестовых сущностей",
	notChosenTitle = "Тестовая сущность не выбрана",
	notChosenMessage = "Выберите тестовую сущность",

	chooseWindowTitle = "Выбор тестовой сущности"
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
	
	private Integer id;

	private String strField; // text field

	private Integer intField; // number field (integer)

	private Integer intSpinnerField; // spinner filed

	private Double doubleField; // number field (float)

	private Date dateField; // date field

	private Boolean booleanField; // checkbox

	private TestEnum enumField; // local combobox

	private Parcel comboField; // remote combobox

	private Developer chooseField; // choose from entity choose form, save id
}
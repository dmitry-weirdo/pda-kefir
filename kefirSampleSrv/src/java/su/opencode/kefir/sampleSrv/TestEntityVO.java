/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.sampleSrv;

import su.opencode.kefir.srv.VO;
import su.opencode.kefir.srv.json.ColumnModel;

import java.util.Date;

import static su.opencode.kefir.sampleSrv.render.Renderers.TEST_ENUM_RENDERER;
import static su.opencode.kefir.srv.renderer.RenderersFactory.*;

public class TestEntityVO extends VO
{
	public TestEntityVO() {
	}

	public TestEntityVO(TestEntity testEntity) {
		super(testEntity);

		this.juridicalAddressStr = Address.getAddress(testEntity.getJuridicalAddress());
		this.physicalAddressStr =  Address.getAddress(testEntity.getPhysicalAddress());

		ComboBoxEntity comboBoxEntity = testEntity.getComboBoxEntity();
		if (comboBoxEntity != null)
		{
			this.comboBoxEntity = new ComboBoxEntityVO(comboBoxEntity);
			this.comboFieldCadastralNumber = comboBoxEntity.getCadastralNumber();
		}

		ChooseEntity chooseEntity = testEntity.getChooseEntity();
		if (chooseEntity != null)
		{
			this.chooseEntity = new ChooseEntityVO(chooseEntity);
			this.chooseEntityName = chooseEntity.getName();
		}
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
	public Long getOgrn() {
		return ogrn;
	}
	public void setOgrn(Long ogrn) {
		this.ogrn = ogrn;
	}
	public Long getInn() {
		return inn;
	}
	public void setInn(Long inn) {
		this.inn = inn;
	}
	public Long getKpp() {
		return kpp;
	}
	public void setKpp(Long kpp) {
		this.kpp = kpp;
	}
	public ComboBoxEntityVO getComboBoxEntity() {
		return comboBoxEntity;
	}
	public void setComboBoxEntity(ComboBoxEntityVO comboBoxEntity) {
		this.comboBoxEntity = comboBoxEntity;
	}
	public String getComboFieldCadastralNumber() {
		return comboFieldCadastralNumber;
	}
	public void setComboFieldCadastralNumber(String comboFieldCadastralNumber) {
		this.comboFieldCadastralNumber = comboFieldCadastralNumber;
	}
	public ChooseEntityVO getChooseEntity() {
		return chooseEntity;
	}
	public void setChooseEntity(ChooseEntityVO chooseEntity) {
		this.chooseEntity = chooseEntity;
	}
	public String getChooseEntityName() {
		return chooseEntityName;
	}
	public void setChooseEntityName(String chooseEntityName) {
		this.chooseEntityName = chooseEntityName;
	}

	public Address getJuridicalAddress() {
		return juridicalAddress;
	}
	public void setJuridicalAddress(Address juridicalAddress) {
		this.juridicalAddress = juridicalAddress;
	}
	public String getJuridicalAddressStr() {
		return juridicalAddressStr;
	}
	public void setJuridicalAddressStr(String juridicalAddressStr) {
		this.juridicalAddressStr = juridicalAddressStr;
	}
	public Address getPhysicalAddress() {
		return physicalAddress;
	}
	public void setPhysicalAddress(Address physicalAddress) {
		this.physicalAddress = physicalAddress;
	}
	public String getPhysicalAddressStr() {
		return physicalAddressStr;
	}
	public void setPhysicalAddressStr(String physicalAddressStr) {
		this.physicalAddressStr = physicalAddressStr;
	}

	private Integer id;

	@ColumnModel(header = "Текстовое поле")
	private String strField; // text field

	@ColumnModel(header = "Целочисленное поле")
	private Integer intField; // number field (integer)

	@ColumnModel(header = "Целочисленное спиннер поле")
	private Integer intSpinnerField; // spinner filed

	@ColumnModel(header = "Дробное поле", renderer = FLOAT_RENDERER)
	private Double doubleField; // number field (float)

	@ColumnModel(header = "Датовое поле", renderer = DATE_RENDERER)
	private Date dateField; // date field

	@ColumnModel(header = "Булево поле", renderer = SEX_RENDERER)
	private Boolean booleanField; // checkbox

	@ColumnModel(header = "Энум поле (локальный комбобокс)", renderer = TEST_ENUM_RENDERER)
	private TestEnum enumField; // local combobox

	/**
	 * ОГРН (основной государственный регистрационный номер), 13 знаков.
	 */
	@ColumnModel(header = "ОГРН", width = 90, renderer = DONT_SHOW_NIL_RENDERER)
	private Long ogrn;

	/**
	 * ИНН (идентификационный номер налогоплательщика).<br/>
	 * Для юридических лиц - 10 знаков.<br/>
	 */
	@ColumnModel(header = "ИНН", width = 85, renderer = DONT_SHOW_NIL_RENDERER)
	private Long inn;

	/**
	 * КПП (код причины постановки на учет).<br/>
	 * Для юридических лиц - 9 знаков.<br/>
	 */
	@ColumnModel(header = "КПП", width = 85, renderer = DONT_SHOW_NIL_RENDERER)
	private Long kpp;


//	// combo field
	private ComboBoxEntityVO comboBoxEntity; // remote combobox

	@ColumnModel(header = "Селект поле (ремоут комбобокс)")
	private String comboFieldCadastralNumber;

//	// choose field
	private ChooseEntityVO chooseEntity;  // choose from entity choose form, save id

	@ColumnModel(header = "Поле связанной сущности (наименование)", sortParam = "chooseEntity.name")
	private String chooseEntityName;

	/**
	 * Юридический адрес
	 */
	private Address juridicalAddress;

	@ColumnModel(header = "Юридический адрес", width = 200, sortable = false)
	private String juridicalAddressStr;

	/**
	 * Фактический адрес
	 */
	private Address physicalAddress;

	@ColumnModel(header = "Фактический адрес", width = 200, sortable = false)
	private String physicalAddressStr;
}
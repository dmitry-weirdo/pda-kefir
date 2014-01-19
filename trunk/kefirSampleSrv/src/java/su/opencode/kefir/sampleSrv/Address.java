/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 19.03.2012 18:14:17$
*/
package su.opencode.kefir.sampleSrv;

import su.opencode.kefir.gen.ExtEntity;
import su.opencode.kefir.srv.json.JsonObject;

import static su.opencode.kefir.util.StringUtils.concat;
import static su.opencode.kefir.util.StringUtils.getNotNullString;

@ExtEntity(
	listWindowTitle = "������ �������",
	notChosenTitle = "����� �� ������",
	notChosenMessage = "�������� �����",

	chooseWindowTitle = "����� ������",

	createWindowTitle = "���� ������",
	showWindowTitle = "�������� ������",
	updateWindowTitle = "��������� ������",
	deleteWindowTitle = "�������� ������"
)
public class Address extends JsonObject
{
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getZipCode() {
		return zipCode;
	}
	public void setZipCode(Integer zipCode) {
		this.zipCode = zipCode;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCityDistrict() {
		return cityDistrict;
	}
	public void setCityDistrict(String cityDistrict) {
		this.cityDistrict = cityDistrict;
	}
	public String getLocality() {
		return locality;
	}
	public void setLocality(String locality) {
		this.locality = locality;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getBlock() {
		return block;
	}
	public void setBlock(String block) {
		this.block = block;
	}
	public String getHouse() {
		return house;
	}
	public void setHouse(String house) {
		this.house = house;
	}
	public String getBuilding() {
		return building;
	}
	public void setBuilding(String building) {
		this.building = building;
	}
	public String getApartment() {
		return apartment;
	}
	public void setApartment(String apartment) {
		this.apartment = apartment;
	}

	public static String getAddress(Address address) {
		if (address == null)
			return null;

		return concat(
			getNotNullString(address.getZipCode()), ADDRESS_SEPARATOR,
			getNotNullString(address.getSubject()), ADDRESS_SEPARATOR,
			getNotNullString(address.getDistrict()), ADDRESS_SEPARATOR,
			getNotNullString(address.getCity()), ADDRESS_SEPARATOR,
			getNotNullString(address.getCityDistrict()), ADDRESS_SEPARATOR,
			getNotNullString(address.getLocality()), ADDRESS_SEPARATOR,
			getNotNullString(address.getStreet()), ADDRESS_SEPARATOR,
			getNotNullString(address.getBlock()), ADDRESS_SEPARATOR,
			getNotNullString(address.getHouse()), ADDRESS_SEPARATOR,
			getNotNullString(address.getBuilding()), ADDRESS_SEPARATOR,
			getNotNullString(address.getApartment())
		);
	}
	public static String getBuildingAddress(Address address) {
		if (address == null)
			return null;

		return concat(
			getNotNullString(address.getSubject()), ADDRESS_SEPARATOR,
			getNotNullString(address.getDistrict()), ADDRESS_SEPARATOR,
			getNotNullString(address.getCity()), ADDRESS_SEPARATOR,
			getNotNullString(address.getCityDistrict()), ADDRESS_SEPARATOR,
			getNotNullString(address.getLocality()), ADDRESS_SEPARATOR,
			getNotNullString(address.getStreet()), ADDRESS_SEPARATOR,
			getNotNullString(address.getBlock()), ADDRESS_SEPARATOR,
			getNotNullString(address.getHouse())
		);
	}


	private Integer id;

	/**
	 * �������� ������. 6 ���� (�������� ������� ����).
	 */
	private Integer zipCode;

	/**
	 * �������� �������� ��.
	 */
	private String subject;

	/**
	 * ����� �������
	 */
	private String district;

	/**
	 * �����
	 */
	private String city;

	/**
	 * ����� ������
	 */
	private String cityDistrict;

	/**
	 * ���������� �����
	 */
	private String locality;

	/**
	 * �����
	 */
	private String street;

	/**
	 * �������
	 */
	private String block;

	/**
	 * ���
	 */
	private String house;

	/**
	 * ������
	 */
	private String building;

	/**
	 * ��������\����
	 */
	private String apartment;

	public static final String ADDRESS_SEPARATOR = ",";
}
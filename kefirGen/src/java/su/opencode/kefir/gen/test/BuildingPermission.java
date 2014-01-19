/**
 Copyright 2011 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.test;

import su.opencode.kefir.srv.json.JsonObject;
import su.opencode.kefir.gen.ExtEntity;
import su.opencode.kefir.gen.field.*;
import su.opencode.kefir.gen.field.linkedEntity.PreventDeleteEntity;
import su.opencode.kefir.gen.field.searchField.SearchField;
import su.opencode.kefir.gen.field.searchField.FilterConfigField;
import static su.opencode.kefir.gen.field.DateField.NULL_DATE_VALUE;

import java.util.Date;

/**
 * ���������� �� ������������� (�������).
 * �������� ��� ���������� �������� ������ �� ������ {@linkplain su.opencode.minstroy.ejb.building.Building ������������ ������},
 * ������� ����� ��������� {@linkplain su.opencode.minstroy.ejb.leasing.Developer ������������} �� {@linkplain su.opencode.minstroy.ejb.leasing.Parcel ��������� �������}.
 */
@ExtEntity(
	queryBuilderJoin = "left join o.building b",

	listWindowTitle = "������ ���������� �� �������������",
	notChosenTitle = "���������� �� ������������� �� �������",
	notChosenMessage = "�������� ���������� �� �������������",

	chooseWindowTitle = "����� ���������� �� �������������",
	createWindowTitle = "���� ���������� �� �������������",
	showWindowTitle = "�������� ���������� �� �������������",
	updateWindowTitle = "��������� ���������� �� �������������",
	deleteWindowTitle = "�������� ���������� �� �������������",

	formWindowWidth = 800,

	jsNamespace = "su.opencode.minstroy.building.buildingPermission", // todo: remove this

	jsDirectory = "buildingPermissionNew",
	serviceClassName = "su.opencode.minstroy.ejb.building.BuildingServiceNew",
	serviceBeanClassName = "su.opencode.minstroy.ejb.building.BuildingServiceNewBean",
	queryBuilderClassName = "su.opencode.minstroy.ejb.building.BuildingPermissionQueryBuilderNew",
	filterConfigClassName = "su.opencode.minstroy.ejb.building.BuildingPermissionFilterConfigNew",

	listServletUrl = "/buildingPermissionsListNew",
	getServletUrl = "/buildingPermissionGetNew",
	createServletUrl = "/buildingPermissionCreateNew",
	updateServletUrl = "/buildingPermissionUpdateNew",
	deleteServletUrl = "/buildingPermissionDeleteNew",

	preventDeleteEntities = {
		@PreventDeleteEntity(className = "su.opencode.minstroy.ejb.building.Building", message = "���������� ������� ���������� �� �������������, �.�. ���������� ������������ ������, ��������� � ���.")
	}
)
public class BuildingPermission extends JsonObject
{
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public LeaseContract getLeaseContract() {
		return leaseContract;
	}
	public void setLeaseContract(LeaseContract leaseContract) {
		this.leaseContract = leaseContract;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Date getProlongationDate() {
		return prolongationDate;
	}
	public void setProlongationDate(Date prolongationDate) {
		this.prolongationDate = prolongationDate;
	}
//	public Building getBuilding() {
//		return building;
//	}
//	public void setBuilding(Building building) {
//		this.building = building;
//	}

	@IdField()
	private Integer id;

	/**
	 * ������� ������. � ������ ������ �������� ������
	 * ����� ���� ��������� ���������� �� ������������� ������ ��� �����
	 * {@linkplain su.opencode.minstroy.ejb.building.Building ������������ ��������}.
	 */
	@FilterConfigField(listWindowFilterTitle = "������� ������", listWindowTitleParamName = "number")
	@ChooseField(
		fieldSetName = "������� ������",

		fields = {
			@ChooseFieldTextField(name = "number", label = "������� ������", maxLength = 100, width = 400, formPanelWidth = 580),
			@ChooseFieldTextField(name = "parcelCadastralNumber", label = "��������� �������", maxLength = 25, width = 400, formPanelWidth = 580),
			@ChooseFieldTextField(name = "developerName", label = "����������", maxLength = 255, width = 400, formPanelWidth = 580)
		}
	)
	private LeaseContract leaseContract;

	@FilterConfigField(name = "parcelId", type = "Integer", filterFieldName = "id", sqlParamName = "leaseContract.parcel.id", listWindowFilterTitle = "��������� �������", listWindowTitleParamName = "cadastralNumber")
	private Parcel parcel; // transient for filterConfig

	@FilterConfigField(name = "developerId", type = "Integer", filterFieldName = "id", sqlParamName = "leaseContract.developer.id", listWindowFilterTitle = "����������")
	private Developer developer; // transient for filterConfig


	/**
	 * ����� ���������� �� �������������.
	 * ����� ������ "RU 630101803"
	 */
	@SearchField(label = "�����:", width = 250)
	@TextField(label = "����� ���������� �� �������������", width = 300, maxLength = 100)
	private String number;

	/**
	 * ���� ������ ����� �������� ��������
	 */
	@DateField(label = "���� ������ ����� ��������", maxValue = NULL_DATE_VALUE)
	private Date beginDate;

	/**
	 * ���� ��������� ����� �������� ��������
	 */
	@DateField(label = "���� ��������� ����� ��������", maxValue = NULL_DATE_VALUE)
	private Date endDate;

	/**
	 * ����, �� ������� ��� ������	� �������.
	 * ������� ���� ������, ��� ���� ��������� ����� �������� ��������.
	 */
	@DateField(label = "���� ���������", maxValue = NULL_DATE_VALUE, allowBlank = true)
	private Date prolongationDate;

	/**
	 * ������������ ������, �� ������� �������� ���������� �� �������������.
	 */
	@FilterConfigField(name = "noBuilding", type = "Boolean", sqlParamName = "b", sqlParamValue = FilterConfigField.NULL_VALUE, addEntityPrefix = false)
	private Integer building;
}
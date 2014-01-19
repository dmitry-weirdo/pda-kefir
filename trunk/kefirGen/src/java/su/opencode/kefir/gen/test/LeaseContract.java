/**
 Copyright 2011 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.test;

import su.opencode.kefir.gen.ExtEntity;
import su.opencode.kefir.gen.field.*;
import su.opencode.kefir.gen.field.searchField.FilterConfigField;
import su.opencode.kefir.gen.field.searchField.SearchField;
import su.opencode.kefir.srv.json.JsonObject;

import java.util.Date;

import static su.opencode.kefir.gen.field.DateField.NULL_DATE_VALUE;
import static su.opencode.kefir.srv.renderer.RenderersFactory.FLOAT_RENDERER;

/**
 * ������� ������.
 * ����������� {@linkplain Developer ������������} ��� ������ ������������� {@linkplain Parcel ���������� �������}.
 */
@ExtEntity(
	listWindowTitle = "������ ��������� ������",
	notChosenTitle = "������� ������ �� ������",
	notChosenMessage = "�������� ������� ������",

	chooseWindowTitle = "����� �������� ������",
	createWindowTitle = "���� �������� ������",
	showWindowTitle = "�������� �������� ������",
	updateWindowTitle = "��������� �������� ������",
	deleteWindowTitle = "�������� �������� ������",

	createSaveErrorMessage = "������ ��� ���������� �������� ������",
	updateSaveErrorMessage = "������ ��� ��������� �������� ������",
	deleteSaveErrorMessage = "������ ��� �������� �������� ������",

	formWindowWidth = 800,

//	jsNamespace = "su.opencode.minstroy.leasing.leaseContract", // todo: remove this

	jsDirectory = "leaseContractNew",
	serviceClassName = "su.opencode.minstroy.ejb.leasing.LeasingServiceNew",
	serviceBeanClassName = "su.opencode.minstroy.ejb.leasing.LeasingServiceNewBean",
	queryBuilderClassName = "su.opencode.minstroy.ejb.leasing.LeaseContractQueryBuilderNew",
	filterConfigClassName = "su.opencode.minstroy.ejb.leasing.LeaseContractFilterConfigNew",

	listServletUrl = "/leaseContractsListNew",
	getServletUrl = "/leaseContractGetNew",
	createServletUrl = "/leaseContractCreateNew",
	updateServletUrl = "/leaseContractUpdateNew",
	deleteServletUrl = "/leaseContractDeleteNew"//,

//	listSecondRowButtons = {
//		@SecondRowButton(text = "����������� ������� �� ������", listEntityClassName = "su.opencode.minstroy.ejb.leasing.RentPayment"),
//		@SecondRowButton(text = "����������� ���������� �� �������������", listEntityClassName = "su.opencode.minstroy.ejb.building.BuildingPermission"),
//		@SecondRowButton(text = "����������� ������������ �������", listEntityClassName = "su.opencode.minstroy.ejb.building.Building")
//	}//,

//	preventDeleteEntities = {
//		@PreventDeleteEntity(className = "su.opencode.minstroy.ejb.leasing.RentPayment", message = "���������� ������� ������� ������, �.�. ���������� ������� �� ������, ��������� � ���."),
//		@PreventDeleteEntity(className = "su.opencode.minstroy.ejb.building.BuildingPermission", message = "���������� ������� ������� ������, �.�. ���������� ���������� �� �������������, ��������� � ���.")
//	}
)
public class LeaseContract extends JsonObject
{
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Parcel getParcel() {
		return parcel;
	}
	public void setParcel(Parcel parcel) {
		this.parcel = parcel;
	}
	public Developer getDeveloper() {
		return developer;
	}
	public void setDeveloper(Developer developer) {
		this.developer = developer;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getRegistationNumber() {
		return registationNumber;
	}
	public void setRegistationNumber(String registationNumber) {
		this.registationNumber = registationNumber;
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

	@IdField()
	private Integer id;

	/**
	 * ��������� �������
	 */
	@FilterConfigField(listWindowFilterTitle = "��������� �������", listWindowTitleParamName = "cadastralNumber")
	@ChooseField(
		fieldSetName = "��������� �������",

		fields = {
			@ChooseFieldTextField(name = "cadastralNumber", label = "����������� �����", formPanelWidth = 580, width = 400, maxLength = 25),
			@ChooseFieldTextField(name = "area", label = "�������", formPanelWidth = 580, width = 100, maxLength = 10, renderer = FLOAT_RENDERER)
		}

	)
	private Parcel parcel;

	/**
	 * ����������
	 */
	@FilterConfigField(listWindowFilterTitle = "����������")
	@ChooseField(
		fieldSetName = "����������",

		fields = {
			@ChooseFieldTextField(name = "name", label = "������������", formPanelWidth = 580, width = 400, maxLength = 255)
		}
	)
	private Developer developer;

	/**
	 * ����� �������� ������
	 */
	@SearchField(label = "�����:", width = 250)
	@TextField(label = "����� ��������", width = 300, maxLength = 100) // todo: vtype
	private String number;

	/**
	 * ��������������� ����� �������� (�� ����������)
	 */
	@SearchField(label = "��������������� �����:", width = 250)
	@TextField(label = "��������������� ����� ��������", width = 300, maxLength = 100) // todo: vtype
	private String registationNumber;


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
	 * ����, �� ������� ��� ������� �������.
	 * ������� ���� ������, ��� ���� ��������� ����� �������� ��������.
	 */
	@DateField(label = "���� ���������", maxValue = NULL_DATE_VALUE, allowBlank = true)
	private Date prolongationDate;
}
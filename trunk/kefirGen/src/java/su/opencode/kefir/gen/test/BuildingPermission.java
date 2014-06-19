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
 * Разрешение на строительство (договор).
 * Выдается при заключении договора аренды на каждый {@linkplain su.opencode.minstroy.ejb.building.Building строительный объект},
 * которое будет строиться {@linkplain su.opencode.minstroy.ejb.leasing.Developer застройщиком} на {@linkplain su.opencode.minstroy.ejb.leasing.Parcel земельном участке}.
 */
@ExtEntity(
	queryBuilderJoin = "left join o.building b",

	listWindowTitle = "Список разрешений на строительство",
	notChosenTitle = "Разрешение на строительство не выбрано",
	notChosenMessage = "Выберите разрешение на строительство",

	chooseWindowTitle = "Выбор разрешения на строительство",
	createWindowTitle = "Ввод разрешения на строительство",
	showWindowTitle = "Просмотр разрешения на строительство",
	updateWindowTitle = "Изменение разрешения на строительство",
	deleteWindowTitle = "Удаление разрешения на строительство",

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
		@PreventDeleteEntity(className = "su.opencode.minstroy.ejb.building.Building", message = "Невозможно удалить разрешение на строительство, т.к. существует строительный объект, связанный с ним.")
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
	 * Договор аренды. В рамках одного договора аренды
	 * могут быть заключены разрешения на строительство одного или более
	 * {@linkplain su.opencode.minstroy.ejb.building.Building строительных объектов}.
	 */
	@FilterConfigField(listWindowFilterTitle = "договор аренды", listWindowTitleParamName = "number")
	@ChooseField(
		fieldSetName = "Договор аренды",

		fields = {
			@ChooseFieldTextField(name = "number", label = "Договор аренды", maxLength = 100, width = 400, formPanelWidth = 580),
			@ChooseFieldTextField(name = "parcelCadastralNumber", label = "Земельный участок", maxLength = 25, width = 400, formPanelWidth = 580),
			@ChooseFieldTextField(name = "developerName", label = "Застройщик", maxLength = 255, width = 400, formPanelWidth = 580)
		}
	)
	private LeaseContract leaseContract;

	@FilterConfigField(name = "parcelId", type = "Integer", filterFieldName = "id", sqlParamName = "leaseContract.parcel.id", listWindowFilterTitle = "земельный участок", listWindowTitleParamName = "cadastralNumber")
	private Parcel parcel; // transient for filterConfig

	@FilterConfigField(name = "developerId", type = "Integer", filterFieldName = "id", sqlParamName = "leaseContract.developer.id", listWindowFilterTitle = "застройщик")
	private Developer developer; // transient for filterConfig


	/**
	 * Номер разрешения на строительство.
	 * Имеет формат "RU 630101803"
	 */
	@SearchField(label = "Номер:", width = 250)
	@TextField(label = "Номер разрешения на строительство", width = 300, maxLength = 100)
	private String number;

	/**
	 * Дата начала срока действия договора
	 */
	@DateField(label = "Дата начала срока действия", maxValue = NULL_DATE_VALUE)
	private Date beginDate;

	/**
	 * Дата окончания срока действия договора
	 */
	@DateField(label = "Дата окончания срока действия", maxValue = NULL_DATE_VALUE)
	private Date endDate;

	/**
	 * Дата, до которой был продле	н договор.
	 * Обязана быть больше, чем дата окончания срока действия договора.
	 */
	@DateField(label = "Дата продления", maxValue = NULL_DATE_VALUE, allowBlank = true)
	private Date prolongationDate;

	/**
	 * Строительный объект, на который выдается разрешение на строительство.
	 */
	@FilterConfigField(name = "noBuilding", type = "Boolean", sqlParamName = "b", sqlParamValue = FilterConfigField.NULL_VALUE, addEntityPrefix = false)
	private Integer building;
}
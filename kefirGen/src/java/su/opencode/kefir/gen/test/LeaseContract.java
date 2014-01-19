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
 * Договор аренды.
 * Заключается {@linkplain Developer застройщиком} для аренды определенного {@linkplain Parcel земельного участка}.
 */
@ExtEntity(
	listWindowTitle = "Список договоров аренды",
	notChosenTitle = "Договор аренды не выбран",
	notChosenMessage = "Выберите договор аренды",

	chooseWindowTitle = "Выбор договора аренды",
	createWindowTitle = "Ввод договора аренды",
	showWindowTitle = "Просмотр договора аренды",
	updateWindowTitle = "Изменение договора аренды",
	deleteWindowTitle = "Удаление договора аренды",

	createSaveErrorMessage = "Ошибка при сохранении договора аренды",
	updateSaveErrorMessage = "Ошибка при изменении договора аренды",
	deleteSaveErrorMessage = "Ошибка при удалении договора аренды",

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
//		@SecondRowButton(text = "Просмотреть платежи за аренду", listEntityClassName = "su.opencode.minstroy.ejb.leasing.RentPayment"),
//		@SecondRowButton(text = "Просмотреть разрешения на строительство", listEntityClassName = "su.opencode.minstroy.ejb.building.BuildingPermission"),
//		@SecondRowButton(text = "Просмотреть строительные объекты", listEntityClassName = "su.opencode.minstroy.ejb.building.Building")
//	}//,

//	preventDeleteEntities = {
//		@PreventDeleteEntity(className = "su.opencode.minstroy.ejb.leasing.RentPayment", message = "Невозможно удалить договор аренды, т.к. существуют платежи за аренду, связанные с ним."),
//		@PreventDeleteEntity(className = "su.opencode.minstroy.ejb.building.BuildingPermission", message = "Невозможно удалить договор аренды, т.к. существуют разрешения на строительство, связанные с ним.")
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
	 * Земельный участок
	 */
	@FilterConfigField(listWindowFilterTitle = "земельный участок", listWindowTitleParamName = "cadastralNumber")
	@ChooseField(
		fieldSetName = "Земельный участок",

		fields = {
			@ChooseFieldTextField(name = "cadastralNumber", label = "Кадастровый номер", formPanelWidth = 580, width = 400, maxLength = 25),
			@ChooseFieldTextField(name = "area", label = "Площадь", formPanelWidth = 580, width = 100, maxLength = 10, renderer = FLOAT_RENDERER)
		}

	)
	private Parcel parcel;

	/**
	 * Застройщик
	 */
	@FilterConfigField(listWindowFilterTitle = "застройщик")
	@ChooseField(
		fieldSetName = "Застройщик",

		fields = {
			@ChooseFieldTextField(name = "name", label = "Наименование", formPanelWidth = 580, width = 400, maxLength = 255)
		}
	)
	private Developer developer;

	/**
	 * Номер договора аренды
	 */
	@SearchField(label = "Номер:", width = 250)
	@TextField(label = "Номер договора", width = 300, maxLength = 100) // todo: vtype
	private String number;

	/**
	 * Регистрационный номер договора (по Росреестру)
	 */
	@SearchField(label = "Регистрационный номер:", width = 250)
	@TextField(label = "Регистрационный номер договора", width = 300, maxLength = 100) // todo: vtype
	private String registationNumber;


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
	 * Дата, до которой был продлен договор.
	 * Обязана быть больше, чем дата окончания срока действия договора.
	 */
	@DateField(label = "Дата продления", maxValue = NULL_DATE_VALUE, allowBlank = true)
	private Date prolongationDate;
}
/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 19.03.2012 17:39:06$
*/
package su.opencode.kefir.sampleSrv;

import su.opencode.kefir.gen.ExtEntity;
import su.opencode.kefir.gen.field.IdField;
import su.opencode.kefir.gen.field.TextField;
import su.opencode.kefir.gen.field.TextAreaField;
import su.opencode.kefir.gen.field.button.SecondRowButton;
import su.opencode.kefir.gen.field.linkedEntity.PreventDeleteEntity;
import su.opencode.kefir.gen.field.searchField.SearchField;
import su.opencode.kefir.srv.json.JsonObject;

@ExtEntity(
	listWindowTitle = "Список связанных сущностей",
	notChosenTitle = "Связанная сущность не выбрана",
	notChosenMessage = "Выберите связанную сущность",

	chooseWindowTitle = "Выбор связанной сущности",
	createWindowTitle = "Ввод связанной сущности",
	showWindowTitle = "Просмотр связанной сущности",
	updateWindowTitle = "Изменение связанной сущности",
	deleteWindowTitle = "Удаление связанной сущности",

	createSaveErrorMessage = "Ошибка при сохранении связанной сущности",
	updateSaveErrorMessage = "Ошибка при изменении связанной сущности",
	deleteSaveErrorMessage = "Ошибка при удалении связанной сущности",

	formWindowWidth = 800,

//	serviceClassName = GeneratorRunner.SERVICE_CLASS_NAME,
//	serviceBeanClassName = GeneratorRunner.SERVICE_BEAN_CLASS_NAME,
	queryBuilderClassName = "su.opencode.kefir.generated.ChooseEntityQueryBuilder",
	filterConfigClassName = "su.opencode.kefir.generated.ChooseEntityFilterConfig",

	preventDeleteEntities = {
		@PreventDeleteEntity(className = "su.opencode.kefir.sampleSrv.TestEntity", message = "Невозможно удалить связанную сущность, т.к. существует тестовая сущность, связанная с ней.")
	},

	listSecondRowButtons = {
		@SecondRowButton(text = "Перейти к списку тестовых сущностей", listEntityClassName = "su.opencode.kefir.sampleSrv.TestEntity")
	}
)
public class ChooseEntity extends JsonObject
{
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getCorrespondentAccount() {
		return correspondentAccount;
	}
	public void setCorrespondentAccount(String correspondentAccount) {
		this.correspondentAccount = correspondentAccount;
	}
	public String getInfo(){
		return info;
	}
	public void setInfo(String info){
		this.info = info;
	}

	@IdField()
	private Integer id;

	@SearchField(label = "Наименование:", width = 250)
	@TextField(label = "Полное наименование", maxLength = 255, width = 300)
	private String name;

	/**
	 * Сокращенное наименование
	 */
	@TextField(label = "Сокращенное наименование", maxLength = 255, width = 300)
	private String shortName;

	/**
	 * Корреспондентский счет. 20 знаков.<br/>
	 * Последние три знака (18-й, 19-й, 20-й разряды) содержат 3-значный условный номер участника расчётов, соответствующий 7-му, 8-му, 9-му разрядам БИК.
	 */
	@TextField(label = "Корреспондентский счет", maxLength = 20, width = 300)
	private String correspondentAccount;

	@TextAreaField(label = "Информация", maxLength = 100, width = 300, rows = 10, cols = 20)
	private String info;
}
/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 19.03.2012 17:42:22$
*/
package su.opencode.kefir.sampleSrv;

import su.opencode.kefir.gen.ExtEntity;
import su.opencode.kefir.gen.field.IdField;
import su.opencode.kefir.gen.field.TextField;
import su.opencode.kefir.gen.field.button.SecondRowButton;
import su.opencode.kefir.gen.field.linkedEntity.PreventDeleteEntity;
import su.opencode.kefir.gen.field.searchField.SearchField;
import su.opencode.kefir.srv.json.JsonObject;

@ExtEntity(
	listWindowTitle = "Список комбо сущностей",
	notChosenTitle = "Комбо сущность не выбрана",
	notChosenMessage = "Выберите комбо сущность",

	chooseWindowTitle = "Выбор комбо сущности",
	createWindowTitle = "Ввод комбо сущности",
	showWindowTitle = "Просмотр комбо сущности",
	updateWindowTitle = "Изменение комбо сущности",
	deleteWindowTitle = "Удаление комбо сущности",

	createSaveErrorMessage = "Ошибка при сохранении комбо сущности",
	updateSaveErrorMessage = "Ошибка при изменении комбо сущности",
	deleteSaveErrorMessage = "Ошибка при удалении комбо сущности",

	formWindowWidth = 800,

//	serviceClassName = GeneratorRunner.SERVICE_CLASS_NAME,
//	serviceBeanClassName = GeneratorRunner.SERVICE_BEAN_CLASS_NAME,
	queryBuilderClassName = "su.opencode.kefir.generated.ComboBoxEntityQueryBuilder",
	filterConfigClassName = "su.opencode.kefir.generated.ComboBoxEntityFilterConfig",

	preventDeleteEntities = {
		@PreventDeleteEntity(className = "su.opencode.kefir.sampleSrv.TestEntity", message = "Невозможно удалить комбо сущность, т.к. существует тестовая сущность, связанная с ней.")
	},

	listSecondRowButtons = {
		@SecondRowButton(text = "Перейти к списку тестовых сущностей", listEntityClassName = "su.opencode.kefir.sampleSrv.TestEntity")
	}
)
public class ComboBoxEntity extends JsonObject
{
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCadastralNumber() {
		return cadastralNumber;
	}
	public void setCadastralNumber(String cadastralNumber) {
		this.cadastralNumber = cadastralNumber;
	}

	@IdField()
	private Integer id;

	/**
	 * Кадастровый номер. <br/>
	 * Имеет формат АА—ББ—ВВВВВ—ГГГ—Д—ЕЕ, где <br/>
	 * АА — код региона (как на автомобильных номерах),<br/>
	 * ББ — код района или округа (например, в Москве 01 —ЦАО, 02 — СВАО, 09 — САО, 10 — Зеленоград);<br/>
	 * ВВВВВ — номер квартала или земельного массива;<br/>
	 * ГГГ — номер земельного участка или здания;<br/>
	 * Д — номер (литера) сооружения;<br/>
	 * ЕЕ — номер помещения в сооружении.
	 */
	@SearchField(label = "Кадастровый номер:", width = 250)
	@TextField(label = "Кадастровый номер", width = 200, maxLength = 25)
	private String cadastralNumber;
}
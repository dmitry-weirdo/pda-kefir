/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.field;

import su.opencode.kefir.gen.fileWriter.js.JsArray;

/**
 * Оболочка для соответствия полей выбираемой сущности полям формы, которые подставляются в форму сущности.
 * Эта используется в js-функции Kefir.form.fillFormFields
 */
public class ChooseFieldField
{
	public ChooseFieldField(String formFieldName, String chooseFieldName, String renderer) {
		this.formFieldName = formFieldName;
		this.chooseFieldName = chooseFieldName;
		this.renderer = renderer;
	}
	public String getFormFieldName() {
		return formFieldName;
	}
	public void setFormFieldName(String formFieldName) {
		this.formFieldName = formFieldName;
	}
	public String getChooseFieldName() {
		return chooseFieldName;
	}
	public void setChooseFieldName(String chooseFieldName) {
		this.chooseFieldName = chooseFieldName;
	}
	public String getRenderer() {
		return renderer;
	}
	public void setRenderer(String renderer) {
		this.renderer = renderer;
	}

	/**
	 * @return представление для элемента массива, который используется в Kefir.form.fillFormFields, без запятой после закрывающей скобки
	 */
	public String getJsArrayElement() {
		if ( renderer == null || renderer.isEmpty() )
			return new JsArray(true, formFieldName, chooseFieldName).toString(); // рендерера нет

		JsArray array = new JsArray();
		array.addString(formFieldName);
		array.addString(chooseFieldName);
		array.add(renderer);
		return array.toString();
	}

	/**
	 * Имя поля формы сущности.
	 */
	private String formFieldName;

	/**
	 * Имя поля выбираемой сущности.
	 */
	private String chooseFieldName;

	/**
	 * Обработчик при подстановке значения поля выбираемой сущности в поле формы.
	 * Может быть не указан.
	 */
	private String renderer;
}
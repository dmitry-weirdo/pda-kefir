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
 * �������� ��� ������������ ����� ���������� �������� ����� �����, ������� ������������� � ����� ��������.
 * ��� ������������ � js-������� Kefir.form.fillFormFields
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
	 * @return ������������� ��� �������� �������, ������� ������������ � Kefir.form.fillFormFields, ��� ������� ����� ����������� ������
	 */
	public String getJsArrayElement() {
		if ( renderer == null || renderer.isEmpty() )
			return new JsArray(true, formFieldName, chooseFieldName).toString(); // ��������� ���

		JsArray array = new JsArray();
		array.addString(formFieldName);
		array.addString(chooseFieldName);
		array.add(renderer);
		return array.toString();
	}

	/**
	 * ��� ���� ����� ��������.
	 */
	private String formFieldName;

	/**
	 * ��� ���� ���������� ��������.
	 */
	private String chooseFieldName;

	/**
	 * ���������� ��� ����������� �������� ���� ���������� �������� � ���� �����.
	 * ����� ���� �� ������.
	 */
	private String renderer;
}
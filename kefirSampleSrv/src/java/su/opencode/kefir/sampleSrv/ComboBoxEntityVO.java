/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 19.03.2012 18:46:07$
*/
package su.opencode.kefir.sampleSrv;

import su.opencode.kefir.srv.VO;
import su.opencode.kefir.srv.json.ColumnModel;

public class ComboBoxEntityVO extends VO
{
	public ComboBoxEntityVO() {
	}
	public ComboBoxEntityVO(ComboBoxEntity comboBoxEntity) {
		super(comboBoxEntity);
	}

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

	private Integer id;

	@ColumnModel(header = "Кадастровый номер", width = 200)
	private String cadastralNumber;
}
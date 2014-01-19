/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 19.03.2012 18:36:09$
*/
package su.opencode.kefir.sampleSrv;

import su.opencode.kefir.srv.VO;
import su.opencode.kefir.srv.json.ColumnModel;

public class ChooseEntityVO extends VO
{
	public ChooseEntityVO() {
	}
	public ChooseEntityVO(ChooseEntity chooseEntity) {
		super(chooseEntity);
	}

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

	private Integer id;
	/**
	 * ������ ������������
	 */
	@ColumnModel(header = "������ ������������", width = 250)
	private String name;

	/**
	 * ����������� ������������
	 */
	@ColumnModel(header = "����������� ������������", width = 250)
	private String shortName;

	/**
	 * ����������������� ����. 20 ������.<br/>
	 * ��������� ��� ����� (18-�, 19-�, 20-� �������) �������� 3-������� �������� ����� ��������� ��������, ��������������� 7-��, 8-��, 9-�� �������� ���.
	 */
	@ColumnModel(header = "����������������� ����", width = 150)
	private String correspondentAccount;

	@ColumnModel(header = "����������", width = 250)
	private  String info;
}
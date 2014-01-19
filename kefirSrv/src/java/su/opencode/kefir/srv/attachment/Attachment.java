/**
 Copyright 2011 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.srv.attachment;

import su.opencode.kefir.srv.json.JsonObject;
//import su.opencode.extgen.ExtEntity;
//import su.opencode.kefir.gen.field.searchField.FilterConfigField;

//@ExtEntity(
//	serviceClassName = "su.opencode.kefir.srv.attachment.AttachmentService",
//	serviceBeanClassName = "su.opencode.kefir.srv.attachment.AttachmentServiceBean",
//
//	listWindowTitle = "������ �������� ��������",
//	notChosenTitle = "�������� �������� �� �������",
//	notChosenMessage = "�������� �������� ��������",
//
//	chooseWindowTitle = "����� ��������� ��������",
//
//	createWindowTitle = "���� ��������� ��������",
//	showWindowTitle = "�������� ��������� ��������",
//	updateWindowTitle = "��������� ��������� ��������",
//	deleteWindowTitle = "�������� ��������� ��������"
//)
/**
 * �������� ����������.
 * ������ �������� ���������� �������� � ������������� ���� ��������.
 */
public class Attachment extends JsonObject
{
	public Attachment() {
	}
	public Attachment(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public String getEntityFieldName() {
		return entityFieldName;
	}
	public void setEntityFieldName(String entityFieldName) {
		this.entityFieldName = entityFieldName;
	}
	public Integer getEntityId() {
		return entityId;
	}
	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Integer getFileSize() {
		return fileSize;
	}
	public void setFileSize(Integer fileSize) {
		this.fileSize = fileSize;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}

	private Integer id;

	/**
	 * ��� ��������, � ������� ������� ���������� (�������� ExtEntity ������).
	 */
//	@FilterConfigField(uppercase = false)
	private String entityName;

	/**
	 * ���� ��������, � ������� ������� ����������.
	 */
//	@FilterConfigField(uppercase = false)
	private String entityFieldName;

	/**
	 * id ��������, � ������� ��������� ����������
	 */
//	@FilterConfigField()
	private Integer entityId;

	/**
	 * ��� ������������ �����
	 */
	private String fileName;

	/**
	 * ������ ����� � ������
	 */
	private Integer fileSize;

	/**
	 * �������-���� ������������ �����
	 */
	private String contentType;

	/**
	 * blob-������ ������������ �����
	 */
	private byte[] data;
}
/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.srv.attachment;

import su.opencode.kefir.srv.VO;

/**
 * Оболочка для отображения файлового приложения в FileUploadPanel.
 */
public class AttachmentVO extends VO
{
	public AttachmentVO() {
	}
	public AttachmentVO(Attachment attachment) {
		super(attachment);

		this.shortName = attachment.getFileName();
		this.filePath = "";
		this.fileCls = "file-xls"; // todo: if possible, switch id depending on content type
		this.state = "done";
		this.bytesTotal = attachment.getFileSize();
		this.input = false;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
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
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFileCls() {
		return fileCls;
	}
	public void setFileCls(String fileCls) {
		this.fileCls = fileCls;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Integer getFileSize() {
		return fileSize;
	}
	public void setFileSize(Integer fileSize) {
		this.fileSize = fileSize;
	}
	public int getBytesTotal() {
		return bytesTotal;
	}
	public void setBytesTotal(Integer bytesTotal) {
		this.bytesTotal = bytesTotal;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public Boolean getInput() {
		return input;
	}
	public void setInput(Boolean input) {
		this.input = input;
	}

	private Integer id;

	private String entityName;

	private Integer entityId;

	private String fileName;

	private String shortName;

	private String filePath;

	private String fileCls;

	private String state;

	private Integer fileSize;

	private Integer bytesTotal;

	private String contentType;

	private Boolean input;
}
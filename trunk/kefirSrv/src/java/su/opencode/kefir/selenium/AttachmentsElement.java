/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.selenium;

import su.opencode.kefir.srv.attachment.Attachment;

import java.util.List;

import static su.opencode.kefir.util.StringUtils.concat;

public class AttachmentsElement extends AbstractPage
{
	public AttachmentsElement(String id) {
		super(id);

		final String bodyXpath = concat(sb, "//div[@id='", id, "']/div/div/div/table/tbody/tr");

		removeXpath = concat(sb, "//div[@id='", id, "']/div/div[2]/div");
		addXpath = concat(sb, bodyXpath, "/td/table/tbody/tr/td/div/input");
		uploadAllXpath = concat(sb, bodyXpath, "/td/table/tbody/tr/td[2]/table");
		removeAllXpath = concat(sb, bodyXpath, "/td[2]/table");
	}

	public AttachmentsElement addAttachments(List<Attachment> attachmentList) {
		if (attachmentList != null && !attachmentList.isEmpty())
		{
			for (Attachment att : attachmentList)
				setUploadValueByXpath(concat(sb, addXpath, "[", (++attachmentsCount + 1), "]"), att.getFileName());
		}

		return this;
	}

	public AttachmentsElement uploadAllAttachments() {
		clickByXpath(uploadAllXpath);
		attachmentsCount = DEFAULT_ATTACHMENTS_COUNT;

		return this;
	}

	public AttachmentsElement removeAllAttachments() {
		clickByXpath(removeAllXpath);
		attachmentsCount = DEFAULT_ATTACHMENTS_COUNT;

		return this;
	}

	public AttachmentsElement removeAttachment(int rowIndex) {
		clickByXpath(concat(sb, removeXpath, "/div[", rowIndex, "]/div[3]"));
		if (attachmentsCount != DEFAULT_ATTACHMENTS_COUNT)
			attachmentsCount--;

		return this;
	}

	private static final int DEFAULT_ATTACHMENTS_COUNT = 0;

	private String addXpath;
	private String removeXpath;
	private String uploadAllXpath;
	private String removeAllXpath;

	private int attachmentsCount = DEFAULT_ATTACHMENTS_COUNT;
}

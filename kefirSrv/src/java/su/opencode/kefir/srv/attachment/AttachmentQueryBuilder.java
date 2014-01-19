/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.srv.attachment;

import su.opencode.kefir.srv.EntityQueryBuilder;

public class AttachmentQueryBuilder extends EntityQueryBuilder
{
	public Class getEntityClass() {
		return Attachment.class;
	}
	public String getSqlEntityName() {
		return "Attachment";
	}
}
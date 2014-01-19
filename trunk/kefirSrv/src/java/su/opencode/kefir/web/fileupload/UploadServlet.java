/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.web.fileupload;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONException;
import org.json.JSONObject;
import static su.opencode.kefir.util.FileUtils.BUF_SIZE;
import static su.opencode.kefir.util.FileUtils.TEMP_DIRECTORY;
import su.opencode.kefir.web.Action;
import su.opencode.kefir.web.InitiableAction;
import su.opencode.kefir.srv.attachment.Attachment;
import su.opencode.kefir.srv.attachment.AttachmentService;
import su.opencode.kefir.util.StringUtils;
import su.opencode.kefir.web.JsonServlet;

import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UploadServlet extends JsonServlet
{
	protected Action getAction() {
		return new InitiableAction()
		{
			public void doAction() throws Exception {
				if (ServletFileUpload.isMultipartContent(request))
					processMultipartContent();
				else
					processSimpleContent();
			}

			@SuppressWarnings(value = "unchecked")
			private void processMultipartContent() throws FileUploadException, IOException, ServletException, JSONException {
				logger.info("UploadServlet: processing multipart content");

				AttachmentService service = getService(AttachmentService.class);

				DiskFileItemFactory factory = new DiskFileItemFactory();
				factory.setSizeThreshold(BUF_SIZE);
				factory.setRepository(new File(TEMP_DIRECTORY));

				ServletFileUpload upload = new ServletFileUpload(factory);
				List<FileItem> items = upload.parseRequest(request);
				Attachment attachment = new Attachment();

				Map<String, String> params = fillFormFields(items);
				for (FileItem item : items)
				{
					if ( !item.isFormField() )
					{ // file field
						attachment.setContentType(item.getContentType());
						attachment.setFileName(item.getName());
						attachment.setFileSize((int) item.getSize());
						attachment.setData(item.get());
						attachment.setEntityName( params.get(ATTACHMENT_ENTITY_NAME_PARAM_NAME) );
						attachment.setEntityFieldName( params.get(ATTACHMENT_ENTITY_FIELD_NAME_PARAM_NAME) );
						attachment.setEntityId( getIntParam(params, ATTACHMENT_ENTITY_ID_PARAM_NAME) );

						Integer attachmentId = service.createAttachment(attachment);

						// todo: possible multiple attachments in one request -> need to return ids list
						response.setContentType("text/html"); // prevent <pre></pre> adding around json in responseText (in FF)

						JSONObject jsonObject = new JSONObject();
						jsonObject.put("id", attachmentId);
						jsonObject.put("index", Integer.parseInt(params.get("index"))); // index in store
						writeJson(jsonObject);

						return;
					}
				}
			}
			private Map<String, String> fillFormFields(List<FileItem> items) { // заполняет нефайловые элементы формы
				Map<String, String> params = new HashMap<String, String>();

				for (FileItem item : items)
				{
					if (item.isFormField())
					{ // simple field
						logger.info(StringUtils.concat(sb, "item field name: ", item.getFieldName(), ", value: ", item.getString()));
						params.put(item.getFieldName(), item.getString());
					}
				}

				return params;
			}

			private void processSimpleContent() throws IOException, ServletException {
				String method = getStringParam(METHOD_PARAM_NAME);
				if (method == null)
				{
					writeSuccess();
					return;
				}

				AttachmentService service = getService(AttachmentService.class);

				if (method.equalsIgnoreCase(REMOVE_METHOD_VALUE))
				{
					service.deleteAttachment(getIntegerParam("id"));
				}

				if (method.equalsIgnoreCase(REMOVE_ALL_METHOD_VALUE))
				{
					List<Integer> ids = getCheckGridIds(IDS_PARAM_NAME);
					if (ids != null)
					{ // исключить падение в случае, когда ни один файл на сервер не загружен, но "Удалить все" нажали
						for (Integer id : ids)
							service.deleteAttachment(id);
					}
				}

				writeSuccess();
			}
		};
	}

	protected static Integer getIntParam(Map<String, String> map, String key) {
		String value = map.get(key);
		return ( value == null || value.isEmpty() ) ? null : Integer.parseInt(value);
	}

	public static final String ATTACHMENT_TYPE_PARAM_NAME = "type";
	public static final String ATTACHMENT_ENTITY_NAME_PARAM_NAME = "entityName";
	public static final String ATTACHMENT_ENTITY_FIELD_NAME_PARAM_NAME = "entityFieldName";
	public static final String ATTACHMENT_ENTITY_ID_PARAM_NAME = "entityId";
	public static final String IDS_PARAM_NAME = "ids";
	public static final String METHOD_PARAM_NAME = "method";
	public static final String REMOVE_METHOD_VALUE = "remove";
	public static final String REMOVE_ALL_METHOD_VALUE = "removeAll";
}
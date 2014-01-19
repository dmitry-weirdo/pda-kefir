/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.web.fileupload;

import static su.opencode.kefir.util.StringUtils.concat;
import su.opencode.kefir.web.JsonServlet;
import su.opencode.kefir.srv.attachment.Attachment;
import su.opencode.kefir.srv.attachment.AttachmentService;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class DownloadServlet extends JsonServlet
{
	public void init() throws ServletException {
		final String buffer = getInitParameter("buffer");
		if (buffer != null)
			bufferSize = Integer.parseInt(buffer);
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO: check roles to download the attachment
		// todo: inspect the downloading of a real big file
		Integer id = getIntegerParam(request, "id");
		logger.info( concat(sb, "downloading attachment with id = ", id) );

		if (id == null)
		{
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "Document id not specified");
			return;
		}

		AttachmentService service = getService(AttachmentService.class);
		Attachment attachment = service.getAttachment(id);
		if (attachment == null)
		{
			response.sendError(HttpServletResponse.SC_NOT_FOUND, concat(sb, "Attachment with such id not found"));
			return;
		}

//		File file = new File(attachmentsDir, attachment.getFileName());
//		if (!file.isFile() || file.length() > Integer.MAX_VALUE)
//		{
//			log("Attachment '" + attachment.getFileName() + "' registered in database but not found on disk");
//			response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid file on disk");
//			return;
//		}

		String contentType = attachment.getContentType();
		if (contentType == null)
			contentType = "application/octet-stream";

		response.setCharacterEncoding("UTF-8");
		response.setContentType(contentType);
		response.setContentLength(attachment.getFileSize());
		response.setHeader("Cache-Control", "public, max-age=0");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + getEncodedFilename(request, attachment.getFileName()) + "\"");

		InputStream input = new ByteArrayInputStream(attachment.getData(), 0, attachment.getData().length);
//		FileInputStream input = new FileInputStream(file);
		ServletOutputStream output = response.getOutputStream();

		try
		{
			byte[] buffer = new byte[bufferSize];
			response.setBufferSize(bufferSize);

			int length;
			while ((length = input.read(buffer)) >= 0)
				output.write(buffer, 0, length);

			output.flush();
		}
		finally
		{
			input.close();
			output.close();
		}
	}
	private String getEncodedFilename(HttpServletRequest request, String filename) throws UnsupportedEncodingException {
		StringBuffer sb = new StringBuffer();
		if (request.getHeader("User-Agent").contains("Opera") || !request.getHeader("User-Agent").contains("MSIE"))
		{ // mozilla
			sb.append(new String(filename.getBytes("UTF-8"), "iso-8859-1"));
		}
		else
		{ // MSIE
			// todo: если возможно решить проблему с именами длиннее 30 символов в IE
			byte[] filenameBytes = filename.getBytes("UTF-8");
			for (byte filenameByte : filenameBytes)
			{
				if ((filenameByte | 0x7F) == 0xFFFFFFFF)
				{ // starts with 1
					sb.append('%');
					sb.append(HEXDIGITS[(filenameByte & (15 * 16)) / 16]); // first 4 digits
					sb.append(HEXDIGITS[filenameByte & 15]); // last 4 digits
				}
				else
				{ // starts with 0
					sb.append((char) filenameByte);
				}
			}
		}

		return sb.toString();
	}

	private int bufferSize = 2048;
	private static final char[] HEXDIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
}
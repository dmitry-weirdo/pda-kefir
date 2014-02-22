/*
 Copyright 2011 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 18.07.2011 20:14:43$
*/
package su.opencode.kefir.web;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import su.opencode.kefir.srv.SortConfig;
import su.opencode.kefir.srv.VO;
import su.opencode.kefir.srv.json.JsonEntity;
import su.opencode.kefir.srv.json.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public abstract class Action
{
	public abstract void doAction() throws Exception;
	public abstract boolean needsInit();

	public void init(HttpServletRequest request, HttpServletResponse response) {
		if (!needsInit())
			return;

		this.request = request;
		this.response = response;
		this.parameters = request == null ? null : JsonServlet.getParameterMap(request);
	}
	public void destroy() {
		this.request = null;
		this.response = null;
		this.parameters = null;
	}

	protected SortConfig fillSortConfig() {
		return JsonServlet.fillSortConfig(request);
	}

	protected String getEntityName() throws UnsupportedEncodingException {
		return JsonServlet.getEntityName(request);
	}
	protected <T extends VO> Class<T> getVOClass(String className) throws ClassNotFoundException {
		return JsonServlet.getVOClass(className);
	}

	protected Integer getDutyActionId() {
		return JsonServlet.getDutyActionId(request);
	}

	protected <T extends JsonObject> T fromJson(Class<T> aClass) {
		return JsonObject.fromJson(parameters, aClass);
	}

	protected void writeJson(JsonEntity entity) throws IOException, ServletException {
		JsonServlet.writeJson(response, entity);
	}
	protected void writeJson(JSONObject jsonObject) throws IOException, ServletException {
		JsonServlet.writeJson(response, jsonObject);
	}
	protected void writeJson(JSONObject jsonObject, boolean escapeHtml) throws IOException, ServletException {
		JsonServlet.writeJson(response, jsonObject, escapeHtml);
	}
	protected void writeJson(List entities, int count) throws IOException, ServletException {
		JsonServlet.writeJson(response, entities, count);
	}
	protected void writeJson(List entities, int count, boolean escapeHtml) throws IOException, ServletException {
		JsonServlet.writeJson(response, entities, count, escapeHtml);
	}
	protected void writeJson(List entities) throws IOException, ServletException {
		JsonServlet.writeJson(response, entities, entities.size());
	}
	protected void writeJson(List entities, boolean escapeHtml) throws IOException, ServletException {
		JsonServlet.writeJson(response, entities, escapeHtml);
	}

	protected void writeSuccess() throws IOException, ServletException {
		JsonServlet.writeSuccess(response);
	}
	protected void writeSuccess(JsonEntity jsonEntity) throws IOException, ServletException {
		JsonServlet.writeSuccess(response, jsonEntity);
	}
	protected void writeSuccess(List entities) throws IOException, ServletException {
		JsonServlet.writeSuccess(response, entities);
	}
	protected void writeSuccess(JSONObject jsonObject) throws IOException, ServletException {
		JsonServlet.writeSuccess(response, jsonObject);
	}

	protected Integer getIntegerParam(String paramName) {
		return JsonServlet.getIntegerParam(request, paramName);
	}
	protected Integer getId() {
		return JsonServlet.getId(request);
	}

	protected Long getLongParam(String paramName) {
		return JsonServlet.getLongParam(request, paramName);
	}
	protected Double getDoubleParam(String paramName) throws ServletException {
		return JsonServlet.getDoubleParam(request, paramName);
	}
	protected String getStringParam(String paramName) throws UnsupportedEncodingException {
		return JsonServlet.getStringParam(request, paramName);
	}
	protected String getStringParam(String paramName, boolean upperCase) throws UnsupportedEncodingException {
		return JsonServlet.getStringParam(request, paramName, upperCase);
	}
	protected Date getDateParam(String paramName) throws ServletException {
		return JsonServlet.getDateParam(request, paramName);
	}
	protected Boolean getBooleanParam(String paramName) throws ServletException {
		return JsonServlet.getBooleanParam(request, paramName);
	}
	protected Boolean getBooleanCheckBoxParam(String paramName) {
		return JsonServlet.getBooleanCheckBoxParam(request, paramName);
	}

	protected List<Integer> getCheckGridIds(String paramName) throws UnsupportedEncodingException {
		return JsonServlet.getCheckGridIds(request, paramName);
	}

	protected void writeToExcel(String fileName, XSSFWorkbook workbook) throws IOException {
		JsonServlet.writeToExcel(response, fileName, workbook);
	}
	protected void writeToExcel(List<? extends VO> list, String entityName, String fileName, InputStream renderersInputStream) throws IOException {
		JsonServlet.writeToExcel(response, list, entityName, fileName, renderersInputStream);
	}
	protected <T extends JsonObject> void writeSummary(T object) throws IOException, ServletException {
		JsonServlet.writeSummary(request, response, object);
	}

	protected <T extends Enum> T getEnumValue(Class<T> aClass) throws UnsupportedEncodingException {
		return JsonServlet.getEnumValue(request, aClass);
	}

	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected Map<String, String[]> parameters;
}
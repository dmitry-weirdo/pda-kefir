/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 30.09.2010 11:20:52$
*/
package su.opencode.kefir.web;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import su.opencode.kefir.srv.ClientException;
import su.opencode.kefir.srv.SortConfig;
import su.opencode.kefir.srv.VO;
import su.opencode.kefir.srv.json.JsonEntity;
import su.opencode.kefir.srv.json.JsonObject;
import su.opencode.kefir.util.JsonUtils;
import su.opencode.kefir.util.StringUtils;
import su.opencode.kefir.web.report.DynamicGridReportHelper;
import su.opencode.kefir.web.util.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.*;

import static su.opencode.kefir.util.DateUtils.getDayMonthYearFormat;
import static su.opencode.kefir.util.DateUtils.getJsDateFormat;
import static su.opencode.kefir.util.JsonUtils.putToJson;
import static su.opencode.kefir.util.StringUtils.*;

@SuppressWarnings(value = "unchecked")
public class JsonServlet extends HttpServlet
{
	protected void handleException(HttpServletResponse response, Exception e) throws IOException {
		writeFailure(response, e);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Action action = getAction();
		action.init(request, response);

		try
		{
			action.doAction();
		}
		catch (Exception e)
		{
			handleException(response, e);
		}
		finally
		{
			action.destroy();
		}
	}

	public static <T> T getService(Class<T> serviceClass) {
		return ServiceFactory.get(serviceClass);
	}
	public static <T> T  getService(String className) {
		try
		{
			return getService((Class<T>) Class.forName(className));
		}
		catch (ClassNotFoundException e)
		{
			throw new RuntimeException(e);
		}
	}
	public static Map<String, String[]> getParameterMap(HttpServletRequest request) {
		return (Map<String, String[]>) request.getParameterMap();
	}

	protected Action getAction() {
		return new NonInitiableAction();
	}


	protected static SortConfig fillSortConfig(HttpServletRequest request) {
		StringBuffer sb = new StringBuffer();

		logger.info(LOG_SEPARATOR);
		logger.info(concat(sb, "entity name: ", request.getParameter(ENTITY_NAME_PARAM_NAME)));
		logger.info(concat(sb, "start: ", request.getParameter(START_PARAM_NAME)));
		logger.info(concat(sb, "limit: ", request.getParameter(LIMIT_PARAM_NAME)));
		logger.info(concat(sb, "sort by: ", request.getParameter(SORT_BY_PARAM_NAME)));
		logger.info(concat(sb, "sort dir: ", request.getParameter(SORT_DIR_PARAM_NAME)));

		SortConfig sortConfig = fromJson(request.getParameterMap(), SortConfig.class);

		// исключить падение в случае неуказанных старта и лимита (выставить их по умолчанию)
		if (sortConfig.getStart() == null)
		{
			sortConfig.setStart(SortConfig.DEFAULT_START);
		}

		if (sortConfig.getLimit() == null)
		{
			sortConfig.setLimit(SortConfig.DEFAULT_LIMIT);
		}

		return sortConfig;
	}
	protected static SortConfig fillSortByConfig(HttpServletRequest request) {
		StringBuffer sb = new StringBuffer();

		logger.info(LOG_SEPARATOR);
		logger.info(concat(sb, "entity name: ", request.getParameter(ENTITY_NAME_PARAM_NAME)));
		logger.info(concat(sb, "sort by: ", request.getParameter(SORT_BY_PARAM_NAME)));
		logger.info(concat(sb, "sort dir: ", request.getParameter(SORT_DIR_PARAM_NAME)));

		return fromJson(request.getParameterMap(), SortConfig.class);
	}

	protected static String getEntityName(HttpServletRequest request) throws UnsupportedEncodingException {
		logger.info(concat("entity name: ", request.getParameter(ENTITY_NAME_PARAM_NAME)));
		return getStringParam(request, ENTITY_NAME_PARAM_NAME, false);
	}
	protected static <T extends VO> Class<T> getVOClass(String className) throws ClassNotFoundException {
		Class<?> voClass = Class.forName(className);
		if (!VO.class.isAssignableFrom(voClass))
		{
			throw new IllegalArgumentException(concat("Class ", className, " does not extend ", VO.class.getName()));
		}

		return (Class<T>) voClass;
	}

	protected static Integer getDutyActionId(HttpServletRequest request) {
		return getIntegerParam(request, DUTY_ACTION_ID_PARAM_NAME);
	}

	public static void writeJson(HttpServletResponse response, JsonEntity entity) throws IOException, ServletException {
		try
		{
			JSONObject jsonObject = entity.toJson();
			jsonObject.put(JSON_SUCCESS_PROPERTY, true);
			response.getOutputStream().write(jsonObject.toString().getBytes(RESPONSE_ENCODING));
		}
		catch (JSONException e)
		{
			throw new ServletException(e); // todo: appropriate handling
		}
	}
	public static void writeJson(HttpServletResponse response, List entities) throws IOException, ServletException {
		writeJson(response, entities, entities.size());
	}
	public static void writeJson(HttpServletResponse response, List entities, int count) throws IOException, ServletException {
		try
		{
			JSONArray jsonArray = new JSONArray();
			for (Object entity : entities)
				jsonArray.put(((JsonEntity) entity).toJson());

			JSONObject json = new JSONObject();
			json.put(JSON_SUCCESS_PROPERTY, true);
			json.put(JSON_TOTAL_PROPERTY, count);
			json.put(JSON_RESULTS_PROPERTY, jsonArray);

			response.setContentType(JSON_RESPONSE_CONTENT_TYPE);
			response.getOutputStream().write(escapeHtml(json.toString()).getBytes(RESPONSE_ENCODING)); // todo: refactor to separate method of servlet subclass
		}
		catch (JSONException e)
		{
			throw new ServletException(e); // todo: appropriate handling
		}
	}
	public static void writeJson(HttpServletResponse response, List entities, int count, boolean escapeHtml) throws IOException, ServletException {
		try
		{
			JSONArray jsonArray = new JSONArray();
			for (Object entity : entities)
				jsonArray.put(((JsonEntity) entity).toJson());

			JSONObject json = new JSONObject();
			json.put(JSON_SUCCESS_PROPERTY, true);
			json.put(JSON_TOTAL_PROPERTY, count);
			json.put(JSON_RESULTS_PROPERTY, jsonArray);

			String value = escapeHtml ? escapeHtml(json.toString()) : json.toString();
			response.setContentType(JSON_RESPONSE_CONTENT_TYPE);
			response.getOutputStream().write(value.getBytes(RESPONSE_ENCODING)); // todo: refactor to separate method of servlet subclass
		}
		catch (JSONException e)
		{
			throw new ServletException(e); // todo: appropriate handling
		}
	}
	public static void writeJson(HttpServletResponse response, List entities, boolean escapeHtml) throws IOException, ServletException {
		int count = entities.size();

		try
		{
			JSONArray jsonArray = new JSONArray();
			for (Object entity : entities)
				jsonArray.put(((JsonEntity) entity).toJson());

			JSONObject json = new JSONObject();
			json.put(JSON_SUCCESS_PROPERTY, true);
			json.put(JSON_TOTAL_PROPERTY, count);
			json.put(JSON_RESULTS_PROPERTY, jsonArray);

			String value = escapeHtml ? escapeHtml(json.toString()) : json.toString();
			response.getOutputStream().write(value.getBytes(RESPONSE_ENCODING)); // todo: refactor to separate method of servlet subclass
		}
		catch (JSONException e)
		{
			throw new ServletException(e); // todo: appropriate handling
		}
	}

	public static void writeJson(HttpServletResponse response, JSONArray jsonArray) throws IOException, ServletException {
		try
		{
			JSONObject json = new JSONObject();
			json.put(JSON_TOTAL_PROPERTY, jsonArray.length());
			json.put(JSON_RESULTS_PROPERTY, jsonArray);

			response.getOutputStream().write(escapeHtml(json.toString()).getBytes(RESPONSE_ENCODING));
		}
		catch (JSONException e)
		{
			throw new ServletException(e);
		}
	}
	public static void writeJson(HttpServletResponse response, JSONObject json) throws IOException, ServletException {
		try
		{
			json.put(JSON_SUCCESS_PROPERTY, true);
		}
		catch (JSONException e)
		{
			throw new RuntimeException(e);
		}

		writeJson(response, json, true);
	}
	public static void writeJson(HttpServletResponse response, JSONObject json, boolean isEscapeHtml)	throws IOException, ServletException {
		String value = isEscapeHtml ? escapeHtml(json.toString()) : json.toString();
		response.setContentType(JSON_RESPONSE_CONTENT_TYPE);
		response.getOutputStream().write(value.getBytes(RESPONSE_ENCODING));
	}

	public static void writeSuccess(HttpServletResponse response) throws IOException, ServletException {
		writeSuccess(response, true);
	}
	public static void writeSuccess(HttpServletResponse response, boolean isStream) throws IOException, ServletException {
		try
		{
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(JSON_SUCCESS_PROPERTY, true);
			jsonObject.put(JSON_TOTAL_PROPERTY, 0);
			jsonObject.put(JSON_RESULTS_PROPERTY, new JSONArray());

			response.setContentType(JSON_RESPONSE_CONTENT_TYPE);

			if (isStream)
			{
				response.getOutputStream().write(jsonObject.toString().getBytes(RESPONSE_ENCODING));
			}
			else
			{
				final PrintWriter writer = response.getWriter();
				writer.print(jsonObject.toString());
				writer.close();
			}
		}
		catch (JSONException e)
		{
			throw new ServletException(e); // todo: appropriate handling
		}
	}
	public static void writeSuccess(HttpServletResponse response, JSONObject jsonObject) throws IOException, ServletException {
		try
		{
			jsonObject.put(JSON_SUCCESS_PROPERTY, true);
			response.setContentType(JSON_RESPONSE_CONTENT_TYPE);
			response.getOutputStream().write(jsonObject.toString().getBytes(RESPONSE_ENCODING));
		}
		catch (JSONException e)
		{
			throw new ServletException(e); // todo: appropriate handling
		}
	}
	public static void writeSuccess(HttpServletResponse response, JsonEntity entity)	throws IOException, ServletException {
		try
		{
			JSONObject jsonObject = entity == null ? new JSONObject() : entity.toJson();
			jsonObject.put(JSON_SUCCESS_PROPERTY, true);
			response.setContentType(JSON_RESPONSE_CONTENT_TYPE);
			response.getOutputStream().write(jsonObject.toString().getBytes(RESPONSE_ENCODING));
		}
		catch (JSONException e)
		{
			throw new ServletException(e); // todo: appropriate handling
		}
	}
	public static void writeSuccess(HttpServletResponse response, List entities) throws IOException, ServletException {
		try
		{
			JSONArray jsonArray = new JSONArray();
			for (Object entity : entities)
				jsonArray.put(((JsonEntity) entity).toJson());

			JSONObject json = new JSONObject();
			json.put(JSON_SUCCESS_PROPERTY, true);
			json.put(JSON_TOTAL_PROPERTY, entities.size());
			json.put(JSON_RESULTS_PROPERTY, jsonArray);

			response.setContentType(JSON_RESPONSE_CONTENT_TYPE);
			response.getOutputStream().write(escapeHtml(json.toString()).getBytes(RESPONSE_ENCODING)); // todo: refactor to separate method of servlet subclass
		}
		catch (JSONException e)
		{
			throw new ServletException(e); // todo: appropriate handling
		}
	}

	protected void writeFailure(HttpServletResponse response) throws IOException, ServletException {
		response.getOutputStream().write(concat(sb, "{\"", JSON_SUCCESS_PROPERTY, "\": false}").getBytes(RESPONSE_ENCODING));
	}
	protected void writeFailure(HttpServletResponse response, Exception e)  {
		writeFailure(response, e, false);
	}
	protected void writeFailure(HttpServletResponse response, Exception e, boolean isStream) {
		if (e instanceof ClientException)
		{
			writeFailure(response, e.getMessage(), isStream);
			return;
		}

		final Throwable throwable = e.getCause();
		final StringWriter sw = new StringWriter();

		if (throwable != null)
		{
			if (throwable instanceof ClientException)
			{
				writeFailure(response, throwable.getMessage(), isStream);
				return;
			}
			else
			{
				throwable.printStackTrace(new PrintWriter(sw));
			}
		}
		else
		{
			e.printStackTrace(new PrintWriter(sw));
		}

		writeFailure(response, sw.toString(), isStream);
	}
	protected void writeExtError(HttpServletResponse response, Exception e) throws IOException {
		e.printStackTrace(); // also write error to Jboss console (на JasperReportServlet, когда падает из-за шаблона, получается повторная запись  getOutputStream() has already been called for this response)

		response.setContentType(JSON_RESPONSE_CONTENT_TYPE);
		final PrintWriter writer = response.getWriter();
		final String html = concat(sb,
			"<html>\n",
			"<head>\n",
			"<title>Гостехнадзор</title>\n",
			"</head>\n",
			"<body>\n",
			"<link rel=\"stylesheet\" type=\"text/css\" href=\"/ext/resources/css/ext-all.css\">\n",
			"<script type=\"text/javascript\" src=\"/ext/adapter/ext/ext-base.js\"></script>\n",
			"<script type=\"text/javascript\" src=\"/ext/ext-all-debug.js\"></script>\n",
			"<script type=\"text/javascript\">Ext.MessageBox.alert('Ошибка', '", getErrorMessage(e), "');</script>\n",
			"</body>\n",
			"</html>");
		writer.print(html);
		writer.close();
	}
	protected String getErrorMessage(Exception e) {
		final StringBuffer sb = new StringBuffer();
		if (e instanceof ClientException)
			sb.append(e.getMessage());
		else
		{
			sb.append(e.toString()).append(":<br/>");
			for (StackTraceElement el : e.getStackTrace())
				sb.append(StringUtils.escapeHtml(el.toString())).append("<br/>");
		}

		return sb.toString();
	}
	protected void writeFailure(HttpServletResponse response, String str) {
		writeFailure(response, str, true);
	}
	protected void writeFailure(HttpServletResponse response, String str, boolean isStream) {
		try
		{
			final JSONObject jsonObject = new JSONObject();
			jsonObject.put(JSON_SUCCESS_PROPERTY, false);
			jsonObject.put("msg", str);

			if (isStream)
			{
				response.getOutputStream().write(jsonObject.toString().getBytes(RESPONSE_ENCODING));
			}
			else
			{
				response.setContentType(JSON_RESPONSE_CONTENT_TYPE);
				final PrintWriter writer = response.getWriter();
				writer.print(jsonObject.toString());
				writer.close();
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	protected void writeFailureStackTrace(HttpServletResponse response, Exception e) {
		final StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));

		writeFailure(response, sw.toString());
	}
	protected JSONObject getEmptyJsonObject() {
		return new JSONObject();
	}

	public static Integer getIntegerParam(HttpServletRequest request, String paramName) {
		String param = request.getParameter(paramName);
		return param == null || param.isEmpty() ? null : Integer.parseInt(param);
	}
	public static Integer getIntegerParam(Map<String, String[]> map, String paramName) {
		return JsonUtils.getIntegerParam(map, paramName);
	}

	public static Integer getId(HttpServletRequest request) {
		return getIntegerParam(request, ID_PARAM_VALUE);
	}
	public static Long getLongParam(HttpServletRequest request, String paramName) {
		String param = request.getParameter(paramName);
		return param == null || param.isEmpty() ? null : Long.parseLong(param);
	}
	protected Integer getIntegerValue(String param) {
		return param == null ? null : Integer.parseInt(param);
	}
	public static String getStringParam(HttpServletRequest request, String paramName) throws UnsupportedEncodingException {
		String param = getEncodedUppercaseString(request.getParameter(paramName));
		return param == null || param.trim().isEmpty() ? null : param.trim();
	}
	public static String getStringParam(HttpServletRequest request, String paramName, boolean upperCase)
		throws UnsupportedEncodingException
	{
		String paramValue = request.getParameter(paramName);
		String param = upperCase ? getEncodedUppercaseString(paramValue) : getEncodedString(paramValue);
		return param == null || param.trim().isEmpty() ? null : param.trim();
	}
	public static String getStringParam(Map<String, String[]> map, String paramName, boolean upperCase) {
		return JsonUtils.getStringParam(map, paramName, upperCase);
	}

	public static Boolean getBooleanParam(Map<String, String[]> map, String paramName) {
		return JsonUtils.getBooleanParam(map, paramName);
	}

	public static Boolean getBooleanParam(HttpServletRequest request, String paramName) {
		String value = request.getParameter(paramName);
		if (value == null || value.isEmpty())
		{
			return null;
		}

		return !value.equals("0") && (value.equals("1") || Boolean.parseBoolean(value));
	}
	public static Boolean getBooleanCheckBoxParam(HttpServletRequest request, String paramName) {
		return request.getParameter(paramName) != null && (
			request.getParameter(paramName).equals(CHECKBOX_ON_PARAM_VALUE) ||
				request.getParameter(paramName).equalsIgnoreCase("true"));
	}
	public static Double getDoubleParam(HttpServletRequest request, String paramName) throws ServletException {
		try
		{
			String param = request.getParameter(paramName);
			return (param == null || param.trim().isEmpty()) ? null : getNumberFormat().parse(param).doubleValue();
		}
		catch (ParseException e)
		{
			throw new ServletException(e); // todo: appropriate handling
		}
	}
	public static Date getDateParam(HttpServletRequest request, String paramName) throws ServletException {
		String param = request.getParameter(paramName);

		try
		{
			return (param == null || param.trim().isEmpty()) ? null : getDayMonthYearFormat().parse(param);
		}
		catch (ParseException e)
		{
			try
			{
				return getJsDateFormat().parse(param);
			}
			catch (ParseException e1)
			{
				throw new ServletException(e); // todo: perform appropriate exception handling
			}
		}
	}
	protected static List<Integer> getCheckGridIds(HttpServletRequest request, String paramName) throws UnsupportedEncodingException {
		List<Integer> ids = new ArrayList<Integer>();
		String idsParam = getStringParam(request, paramName);
		if (idsParam == null || idsParam.isEmpty())
		{
			return Collections.emptyList();
		}

		String[] idsStr = idsParam.split(", ");
		for (String idStr : idsStr)
			ids.add(Integer.parseInt(idStr));

		return ids;
	}
	protected List<String> getCheckGridIdsString(HttpServletRequest request, String paramName) throws UnsupportedEncodingException {
		List<String> ids = new ArrayList<String>();
		String idsParam = getStringParam(request, paramName, false);
		if (idsParam == null || idsParam.isEmpty())
		{
			return null;
		}

		String[] idsStr = idsParam.split(", ");
		ids.addAll(Arrays.asList(idsStr));
		return ids;
	}
	protected List<String> getCheckGridIdsString(HttpServletRequest request, String paramName, String separator) throws UnsupportedEncodingException {
		List<String> ids = new ArrayList<String>();
		String idsParam = getStringParam(request, paramName, false);
		if (idsParam == null || idsParam.isEmpty())
		{
			return null;
		}

		String[] idsStr = idsParam.split(separator);
		ids.addAll(Arrays.asList(idsStr));
		return ids;
	}

	public static <T extends JsonObject> T fromJson(Map map, Class<T> aClass) {
		T object = newInstance(aClass);
		object.fromJson(map);

		return object;
	}
	private static <T extends JsonObject> T newInstance(Class<T> aClass) {
		try
		{
			return (T) Class.forName(aClass.getName()).newInstance();
		}
		catch (InstantiationException e)
		{
			throw new RuntimeException(e);
		}
		catch (IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
		catch (ClassNotFoundException e)
		{
			throw new RuntimeException(e);
		}
	}
	protected boolean isTestServer() throws UnknownHostException {
		final byte[] address = InetAddress.getLocalHost().getAddress();
		boolean value = false;
		int i = 0;
		while (i < LOCAL_IPS.length && !value)
		{
			byte[] ip = LOCAL_IPS[i++];
			value = Arrays.equals(address, ip);
		}

		return value;
	}

	protected static void writeToExcel(HttpServletResponse response, List<? extends VO> list, String entityName, String fileName, InputStream renderersInputStream) throws IOException {
		final DynamicGridReportHelper helper = new DynamicGridReportHelper();
		final HSSFWorkbook book = helper.getReport(list, entityName, renderersInputStream);

		response.setContentType("application/vnd.ms-excel"); // MIME-Type for Microsoft Excel
		response.setHeader("Cache-Control", "private, max-age=0");
		if (fileName != null)
		{
			response.setHeader("Content-Disposition", concat("attachment;filename=", fileName, ".xls"));
		}

		book.write(response.getOutputStream());
	}
	protected static <T extends JsonObject> void writeSummary(HttpServletRequest request, HttpServletResponse response, T object) throws IOException, ServletException {
		final JSONObject json = new JSONObject();

		putToJson(json, "json", object.toJson());
		putToJson(json, "gridId", getStringParam(request, "gridId", false));
		putToJson(json, "liveSummaryId", getStringParam(request, "liveSummaryId", false));
		putToJson(json, "success", true);

		writeJson(response, json);
	}
	public static <T extends Enum> T getEnumValue(HttpServletRequest request, Class<T> aClass) throws UnsupportedEncodingException {
		for (T rd : aClass.getEnumConstants())
		{
			final String stringParam = getStringParam(request, rd.name());
			if (stringParam != null && (stringParam.equals("on") || stringParam.equals("true")))
			{
				return rd;
			}
		}

		return null;
	}

	protected StringBuffer sb = new StringBuffer();

	private static final byte[][] LOCAL_IPS = new byte[][] {{10, 10, 1, 13}, {10, 10, 1, 41}, {10, 10, 1, 29}, /*{10, 10, 1, 16}*/};

	private static final String SORT_BY_PARAM_NAME = "sort";
	private static final String SORT_DIR_PARAM_NAME = "dir";
	private static final String START_PARAM_NAME = "start";
	private static final String LIMIT_PARAM_NAME = "limit";
	public static final String ENTITY_NAME_PARAM_NAME = "entityName";

	private static final String CHECKBOX_ON_PARAM_VALUE = "on";

	private static final String ID_PARAM_VALUE = "id";

	public static final String JSON_RESPONSE_CONTENT_TYPE = "text/html";

	public static final String JSON_TOTAL_PROPERTY = "total";
	public static final String JSON_RESULTS_PROPERTY = "results";
	public static final String JSON_SUCCESS_PROPERTY = "success";

	public static final String RESPONSE_ENCODING = "UTF-8";

	protected static final String NAME_QUERY_PARAM_NAME = "nameQuery";

	protected static final String DUTY_ACTION_ID_PARAM_NAME = "dutyActionId";

	protected static final String LOG_SEPARATOR = "===============";

	protected static final Logger logger = Logger.getLogger(JsonServlet.class);
}
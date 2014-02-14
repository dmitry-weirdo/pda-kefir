/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ rybakin
 $Revision$
 $Date: 13.11.2010 12:00:15$
*/
package su.opencode.kefir.srv.json;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import su.opencode.kefir.util.DateUtils;
import su.opencode.kefir.util.JsonUtils;
import su.opencode.kefir.util.ObjectUtils;

import javax.persistence.Transient;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.util.*;

import static su.opencode.kefir.util.JsonUtils.*;
import static su.opencode.kefir.util.ObjectUtils.*;
import static su.opencode.kefir.util.StringUtils.concat;
import static su.opencode.kefir.util.StringUtils.getConcatenation;

public abstract class JsonObject implements JsonEntity, Serializable
{
	public static JSONObject toJson(List<? extends JsonObject> entities, int count) {
		final JSONArray jsonArray = new JSONArray();
		for (JsonObject entity : entities)
			jsonArray.put(entity.toJson());

		final JSONObject json = new JSONObject();
		putToJson(json, JSON_TOTAL_PROPERTY, count);
		putToJson(json, JSON_RESULTS_PROPERTY, jsonArray);

		return json;
	}

	/**
	 * Создает и возвращает Json объект из текущего объекта.
	 * Заполняются все get и is методы для Json объекта, кроме аннотированных @Json(exclude = true).
	 * Имя параметра берется из метода.
	 *
	 * @return JSONObject
	 */
	@Transient
	public JSONObject toJson() {
		final JSONObject json = new JSONObject();
		final Class thisClass = this.getClass();
		for (Method m : thisClass.getDeclaredMethods())
		{
			if (!isGetter(m.getName()) || isJsonExclude(m) || m.getParameterTypes().length != 0)
				continue;

			final Object value = invokeMethod(this, m);
			if (isJsonObject(json, value))
				continue;

			if ( isArray(json, value) )
				continue;

			if ( isCollection(json, value) )
				continue;

			putToJson(json, jsonName, value);
		}

		return json;
	}

	private boolean isArray(JSONObject json, Object value) {
		if (value == null)
			return false;

		if ( !value.getClass().isArray() )
			return false;

		JSONArray jsonArray = new JSONArray();

		int length = Array.getLength(value);
		for (int i = 0; i < length; i++)
		{
			Object o = Array.get(value, i);

			if (o instanceof JsonObject)
				jsonArray.put(((JsonObject) o).toJson());
			else
				jsonArray.put(o);
		}

		putToJson(json, jsonName, jsonArray);

		return true;
	}
	private boolean isCollection(JSONObject json, Object value) {
		if (value == null || !(value instanceof Collection))
			return false;

		final JSONArray jsonArray = new JSONArray();
		for (Object o : (Collection) value)
		{
			if (o instanceof JsonObject)
				jsonArray.put(((JsonObject)o).toJson());
			else
				jsonArray.put(o);
		}

		putToJson(json, jsonName, jsonArray);

		return true;
	}
	private boolean isGetter(String methodName) {
		if (methodName.startsWith("get"))
		{
			jsonName = concat(methodName.substring(3, 4).toLowerCase(), methodName.substring(4));
			return true;
		}

		if (methodName.startsWith("is"))
		{
			jsonName = concat(methodName.substring(2, 3).toLowerCase(), methodName.substring(3));
			return true;
		}

		return false;
	}
	private boolean isJsonExclude(Method m) {
		for (Annotation a : m.getDeclaredAnnotations())
		{
			if (a instanceof Json)
				return ((Json) a).exclude();
		}

		return false;
	}
	@Deprecated
	private void toJson(JSONObject json, Class thisClass) {
		for (Field f : thisClass.getDeclaredFields())
		{
			final Method getMethod = returnGetMethod(thisClass, f);
			if (getMethod == null)
				continue;

			if (isJsonExclude(f))
				continue;

			final Object value = invokeMethod(this, getMethod);
			if (isJsonObject(json, value))
				continue;

			if (isJsonArrayObject(json, value))
				continue;

			putToJson(json, jsonName, value);
		}
	}

	@Transient
	public void fromJson(Map<String, String[]> map) {
		if (map == null)
			return;

		for (String jsonFieldName : map.keySet())
		{
			final Class thisClass = this.getClass();
			final Field field = returnFieldByAnnotationName(thisClass, jsonFieldName);
			final Method getMethod = returnGetMethod(thisClass, field);
			if (getMethod == null)
				continue;

			final Class type = field.getType();
			final Method setMethod = returnSetMethod(thisClass, field, type);
			if (setMethod == null)
				continue;

			if (type.equals(Integer.class) || type.equals(int.class))
			{
				invokeMethod(this, setMethod, getIntegerParam(map, jsonFieldName));
				continue;
			}

			if (type.equals(Date.class))
			{
				invokeMethod(this, setMethod, getDateParam(map, jsonFieldName));
				continue;
			}

			if (type.equals(Double.class) || type.equals(double.class))
			{
				invokeMethod(this, setMethod, getDoubleParam(map, jsonFieldName));
				continue;
			}

			if (type.equals(BigDecimal.class))
			{
				invokeMethod(this, setMethod, new BigDecimal(getStringParam(map, jsonFieldName)));
				continue;
			}

			if (type.equals(String.class))
			{
				invokeMethod(this, setMethod, getStringParam(map, jsonFieldName, isUpperCase(field)));
				continue;
			}

			if (type.equals(Boolean.class) || type.equals(boolean.class))
			{
				invokeMethod(this, setMethod, getBooleanParam(map, jsonFieldName));
				continue;
			}

			if (type.equals(Long.class) || type.equals(long.class))
			{
				invokeMethod(this, setMethod, getLongParam(map, jsonFieldName));
				continue;
			}

			if (type.equals(Short.class) || type.equals(short.class))
			{
				invokeMethod(this, setMethod, getShortParam(map, jsonFieldName));
				continue;
			}

			if (type.equals(Float.class) || type.equals(float.class))
			{
				invokeMethod(this, setMethod, getFloatParam(map, jsonFieldName));
				continue;
			}

			if (setIfEnumeration(map, jsonFieldName, setMethod, type))
				continue;

			if (setIfListOfJsonObject(map, jsonFieldName, field, type, setMethod))
				continue;

			if (setIfJsonObject(map, jsonFieldName, setMethod, type))
				continue;

			throw new RuntimeException(concat("Incorrect type ", type));
		}
	}

	public <T> T fromJson(String json, Class<T> thisClass) {
		JSONObject jsonObject = new JSONObject(json);
		return fromJson(jsonObject, thisClass);
	}

	public static <T> T fromJson(JSONObject json, Class<T> thisClass) {
		if (json == null)
			return null;

		logger.info( concat("fromJson: class ", thisClass.getName()) );

		try
		{
			T instance = thisClass.newInstance();

			Iterator iterator = json.keys();
			while (iterator.hasNext())
			{
				String fieldName = (String) iterator.next();
				logger.info( concat("fromJson: field: \"", fieldName, "\""));
				if ( !hasField(json, fieldName) )
				{ // field is null or is not present in jsonObject
					// todo: think about setting null to instance field
					logger.info( concat("Field \"", fieldName, "\" in JSONObject is null") );
					continue;
				}

				Field field = ObjectUtils.getFieldOrNull(thisClass, fieldName);
				if (field == null)
				{
					logger.info( concat("Field \"", fieldName, "\" is present in JSONObject, but is not present in class ", thisClass.getName()) );
					continue;
				}

				Class<?> fieldType = field.getType();
				Method setter = ObjectUtils.returnSetterMethod(thisClass, field);
				if (setter == null)
				{
					logger.info( concat("Field \"", fieldName, "\" is present in JSONObject and in class ", thisClass.getName(), ", but no setter method exist for this field. Do not set this field to \"", thisClass.getName(), " instance.") );
					continue;
				}

				// todo: check @Json(exclude = true) on setter method

				if ( fieldType.isEnum() )
				{
//					String enumJsonValue = json.getString(fieldName); // fails
					String enumJsonValue = json.get(fieldName).toString(); // somewhy debug shows it's an instance of the enum, but toString() for insurance
					Enum<?> enumValue = Enum.valueOf((Class<Enum>) fieldType, enumJsonValue);
					ObjectUtils.executeSetter(instance, field, enumValue);
					continue;
				}

				if (fieldType.isArray())
				{
					// todo: check that field is JSONArray and throw if it is not
					JSONArray jsonArray = json.getJSONArray(fieldName);
					if ( JsonUtils.isEmpty(jsonArray) ) // todo: refactor to JsonUtils method
					{ // empty array -> set null to field
						ObjectUtils.executeSetter(instance, field, null);
						continue;
					}

					Class<?> componentType = fieldType.getComponentType();

					int length = jsonArray.length();
					Object array = Array.newInstance(componentType, length);
					for (int i = 0; i < length; i++)
					{
						Object value;

						Object jsonArrayElement = jsonArray.get(i);
						if (jsonArrayElement instanceof JSONObject)
						{ // object
							value = fromJson( (JSONObject) jsonArrayElement, componentType);
						}
						else
						{ // scalar type
							// todo: solve inner values
							value = ObjectUtils.cast(componentType, jsonArrayElement);
						}

						Array.set(array, i, value);
					}

					ObjectUtils.executeSetter(instance, field, array); // set array to instance

					continue;
				}

				if ( ObjectUtils.isCollection(fieldType) )
				{
					// todo: check that field is JSONArray and throw if it is not
					JSONArray jsonArray = json.getJSONArray(fieldName);
					if ( JsonUtils.isEmpty(jsonArray) ) // todo: refactor to JsonUtils method
					{ // empty array -> set null to field
						ObjectUtils.executeSetter(instance, field, null);
						continue;
					}

					Collection collection;
					if ( ObjectUtils.isList(fieldType) )
					{
						collection = new ArrayList();
					}
					else if ( ObjectUtils.isSet(fieldType) )
					{
						collection = new TreeSet(); // sorted set
					}
					else
					{
						throw new IllegalArgumentException( concat( JsonObject.class.getName(), "#fromJson does not support collections fields with type = \"", fieldType, "\". Only lists and sets are supported,"));
					}

					int length = jsonArray.length();
					for (int i = 0; i < length; i++)
					{
						Object value;

						Object jsonArrayElement = jsonArray.get(i);
						if (jsonArrayElement instanceof JSONObject)
						{ // object
							Class collectionElementType = getParametrizedTypeParameter(field);
							value = fromJson( (JSONObject) jsonArrayElement, collectionElementType); // todo: use correct value instead of object
						}
						else
						{ // scalar type
							// todo: solve inner values
							value = jsonArrayElement;
						}

						collection.add(value);
					}

					ObjectUtils.executeSetter(instance, field, collection); // set array to instance
					continue;
				}

				if ( json.get(fieldName) instanceof JSONObject )
				{ // inner object is object too
					JSONObject fieldJson = json.getJSONObject(fieldName);
					Object fieldObject = fromJson(fieldJson, fieldType);

					ObjectUtils.executeSetter(instance, field, fieldObject);
					continue;
				}

				if ( !fieldType.isPrimitive() && ( ObjectUtils.areSameClasses(fieldType, Date.class) || fieldType.isInstance(Date.class) ) )
				{
					String dateStr = json.getString(fieldName);
					Date date = DateUtils.getUtcDateFormat().parse(dateStr);

					ObjectUtils.executeSetter(instance, field, date);
					continue;
				}

				// scalar type
				Object value = JsonUtils.getFieldValue(json, fieldType, fieldName);
				ObjectUtils.executeSetter(instance, field, value);
			}

			return instance;
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}


	private boolean setIfListOfJsonObject(Map<String, String[]> map, String jsonFieldName, Field field, Class type, Method setMethod) {
		if (!type.isInterface() || !type.equals(List.class))
			return false;

		final String stringParam = getStringParam(map, jsonFieldName, false);
		if (stringParam == null)
			return true;

		final Type genericType = field.getGenericType();
		if (genericType instanceof ParameterizedType)
		{
			final ParameterizedType pt = (ParameterizedType) genericType;
			final Type[] typeArguments = pt.getActualTypeArguments();
			if (typeArguments.length != 1)
				throw new RuntimeException("Too many types");

			final Class listArgument = (Class) typeArguments[0];
			final List rv = new ArrayList();

			try
			{
				final JSONArray jsonArray = new JSONArray(stringParam);
				final int length = jsonArray.length();

				for (int i = 0; i < length; i++)
				{
					final JsonObject jsonObject = getNewInstance(listArgument);
					final JSONObject ob = jsonArray.getJSONObject(i);
					jsonObject.fromJson(getMap(ob));

					rv.add(jsonObject);
				}
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
			invokeMethod(this, setMethod, rv);
		}

		return true;
	}

	private boolean isUpperCase(Field field) {
		for (Annotation a : field.getDeclaredAnnotations())
		{
			if (a instanceof Json)
				return ((Json) a).uppercase();
		}

		return false;
	}

	@SuppressWarnings(value = "unchecked")
	private boolean isJsonArrayObject(JSONObject json, Object value) {
		if (!(value instanceof Collection))
			return false;

		final Collection<JsonObject> collection = (Collection<JsonObject>) value;
		final JSONArray jsonArray = new JSONArray();
		for (JsonObject o : collection)
		{
			if (jsonId)
			{
				final JSONObject ob = new JSONObject();
				putId(ob, o);
				jsonArray.put(ob);
			}
			else
				jsonArray.put(o.toJson());
		}

		putToJson(json, jsonName, jsonArray);

		return true;
	}

	private boolean isJsonObject(JSONObject json, Object value) {
		if (!(value instanceof JsonObject))
			return false;

		final JsonObject jsonObject = (JsonObject) value;
		if (jsonId)
		{
			putId(json, jsonObject);
			return true;
		}

		putToJson(json, jsonName, jsonObject.toJson());

		return true;
	}

	private void putId(JSONObject json, JsonObject jsonObject) {
		final Method getMethod = returnGetIdMethod(jsonObject.getClass());
		if (getMethod == null)
			return;

		putToJson(json, jsonName, invokeMethod(jsonObject, getMethod));
	}

	/**
	 * Копирует значения всех полей объекта object (кроме id), в текущий объект
	 *
	 * @param object объект, с которого делается копия
	 */
	@Transient
	public void getCopy(JsonObject object) {
		final Class objectClass = object.getClass();
		if (!this.getClass().equals(objectClass))
			throw new RuntimeException(getConcatenation("This class '", this.getClass().getName(),
				"' not equals object class '", objectClass.getName(), "'"));

		for (Field f : objectClass.getDeclaredFields())
		{
			final String fieldName = f.getName();
			if (fieldName.equalsIgnoreCase("id"))
				continue;

			final Method getMethod = returnGetMethod(objectClass, f);
			final Method setMethod = returnSetMethod(objectClass, f, f.getType());
			if (getMethod == null || setMethod == null)
				continue;

			invokeMethod(this, setMethod, invokeMethod(object, getMethod));
		}
	}

	private boolean isExcludeField(String[] excludeFields, String fieldName) {
		if (excludeFields == null)
			return false;

		for (String excludeField : excludeFields)
			if (fieldName.equalsIgnoreCase(excludeField))
				return true;

		return false;
	}

	/**
	 * @param object							класс
	 * @param excludedFieldsNames имена полей класса, разделеные точкой с запятой "id;name;comment"
	 */
	@Transient
	public void getCopy(JsonObject object, String excludedFieldsNames) {
		final Class newObjectClass = object.getClass();
		if (!this.getClass().equals(newObjectClass))
			throw new RuntimeException(getConcatenation("This class '", this.getClass().getName(),
				"' not equals object class '", newObjectClass.getName(), "'"));

		final String[] nullableFields = excludedFieldsNames == null ? null : excludedFieldsNames.split(";");
		for (Field f : newObjectClass.getDeclaredFields())
		{
			final String fieldName = f.getName();
			if (isExcludeField(nullableFields, fieldName))
				continue;

			final Method getMethod = returnGetMethod(newObjectClass, f);
			final Method setMethod = returnSetMethod(newObjectClass, f, f.getType());
			if (getMethod == null || setMethod == null)
				continue;

			invokeMethod(this, setMethod, invokeMethod(object, getMethod));
		}
	}

	private boolean setIfEnumeration(Map<String, String[]> map, String paramName, Method setMethod, Class methodType) {
		final Object[] enumConstants = methodType.getEnumConstants();
		if (enumConstants == null)
			return false;

		final String enumName = getStringParam(map, paramName, false);
		Integer enumId;
		try
		{
			enumId = Integer.parseInt(enumName);
		}
		catch (NumberFormatException e)
		{
			enumId = null;
		}

		if (enumId != null)
		{
			for (Object object : enumConstants)
			{
				final Enum anEnum = (Enum) object;
				if (anEnum.ordinal() == enumId)
				{
					invokeMethod(this, setMethod, anEnum);
					break;
				}
			}
		}
		else
		{
			for (Object object : enumConstants)
			{
				final Enum anEnum = (Enum) object;
				if (anEnum.name().equals(enumName))
				{
					invokeMethod(this, setMethod, anEnum);
					break;
				}
			}
		}

		return true;
	}

	private boolean setIfJsonObject(Map<String, String[]> map, String paramName, Method setMethod, Class methodType) {
		try
		{
			final Object o = methodType.newInstance();
			if (!(o instanceof JsonObject))
				return false;

			final JsonObject jsonObject = (JsonObject) o;

			try
			{
				final Integer id = getIntegerParam(map, paramName);
				final Method setId = returnSetIdMethod(jsonObject.getClass());
				if (setId == null || id == null)
					return true;

				invokeMethod(jsonObject, setId, id);
				invokeMethod(this, setMethod, jsonObject);
			}
			catch (RuntimeException e)
			{ // распарсить параметр как json-строку, содержащую поля объекта
				try
				{
					JSONObject paramJsonObject = new JSONObject(getStringParam(map, paramName, false));
					jsonObject.fromJson(getMap(paramJsonObject));

					invokeMethod(this, setMethod, jsonObject);
				}
				catch (JSONException e1)
				{
					throw new RuntimeException(e);
				}
			}
		}
		catch (InstantiationException | IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}

		return true;
	}

	private Map<String, String[]> getMap(JSONObject jsonObject) throws JSONException {
		Map<String, String[]> map = new HashMap<String, String[]>();

		Iterator iterator = JsonUtils.sortedKeys(jsonObject);
		while (iterator.hasNext())
		{
			Object o = iterator.next();
			map.put(o.toString(), new String[] { jsonObject.get(o.toString()).toString() });
		}

		return map;
	}

	protected Method returnGetIdMethod(Class aClass) {
		try
		{
			return aClass.getMethod("getId");
		}
		catch (Exception e)
		{
			return null;//todo: если нет, то брать из аннотаций
		}
	}

	protected Method returnSetIdMethod(Class aClass) {
		try
		{
			return aClass.getMethod("setId", Integer.class);
		}
		catch (Exception e)
		{
			return null;//todo: если нет, то брать из аннотаций
		}
	}

	private boolean isJsonExclude(Field f) {
		for (Annotation annotation : f.getDeclaredAnnotations())
		{
			if (annotation instanceof Json)
			{
				Json an = (Json) annotation;
				jsonExclude = an.exclude();
				jsonId = an.returnId();
				final String jsonName = an.name();
				if (jsonName.isEmpty())
					this.jsonName = f.getName();
				else
					this.jsonName = jsonName;

				return jsonExclude;
			}
		}

		jsonName = f.getName();
		jsonExclude = false;
		jsonId = false;

		return jsonExclude;
	}

	private void fillFromAnnotation(Field field) {
		for (Annotation annotation : field.getDeclaredAnnotations())
		{
			if (annotation instanceof Json)
			{
				Json an = (Json) annotation;
				final String name = an.name();
				jsonName = name.isEmpty() ? field.getName() : name;
				jsonExclude = an.exclude();
				jsonId = an.returnId();

				return;
			}
		}

		jsonExclude = false;
		jsonId = false;
		jsonName = field.getName();
	}

	private Field returnFieldByAnnotationName(Class thisClass, String jsonFieldName) {
		for (Field field : thisClass.getDeclaredFields())
		{
			fillFromAnnotation(field);
			if (jsonFieldName.equals(jsonName))
				return field;
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T extends JsonObject> T fromJson(Map<String, String[]> map, Class<T> voClass) {
		final JsonObject vo = getNewInstance(voClass);
		vo.fromJson(map);

		return (T) vo;
	}

	private String jsonName;
	private boolean jsonExclude;
	private boolean jsonId;

	public static final String JSON_TOTAL_PROPERTY = "total";
	public static final String JSON_RESULTS_PROPERTY = "results";
	public static final String JSON_SUCCESS_PROPERTY = "success";

	private static final Logger logger = Logger.getLogger(JsonObject.class);
}
/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.selenium;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static su.opencode.kefir.util.FileUtils.FILE_SEPARATOR;
import static su.opencode.kefir.util.FileUtils.close;
import static su.opencode.kefir.util.JsonUtils.getMapFromJson;
import static su.opencode.kefir.util.StringUtils.concat;

public abstract class AbstractDataProvider
{
	protected abstract Object[] getEntityFromJson(HashMap<String, String[]> createEntityData, HashMap<String, String[]> updateEntityData);

	public Iterator<Object[]> loadDataFromJson(String dataFileName) {
		return getIterator(getJsonFileString(dataFileName));
	}

	private Iterator<Object[]> getIterator(String jsonFileString) {
		final List<Object[]> rv = new ArrayList<>();
		try
		{
			final JSONArray jsonArray = new JSONArray(jsonFileString);
			for (int i = 0; i < jsonArray.length(); i++)
			{
				final JSONObject result = jsonArray.getJSONObject(i);
				final JSONObject createEntityData = result.getJSONObject("createEntityData");
				final JSONObject updateEntityData = result.getJSONObject("updateEntityData");
				final HashMap<String, String[]> mapFromJson = getMapFromJson(createEntityData);

				rv.add(getEntityFromJson(mapFromJson, getMapFromJson(updateEntityData)));
			}
		}
		catch (JSONException e)
		{
			throw new RuntimeException(e);
		}

		return rv.listIterator();
	}

	private String getJsonFileString(String dataFileName) {
		BufferedReader br = null;
		try
		{
			br = new BufferedReader(new FileReader(concat(sb, DEFAULT_TEST_DATA_FOLDER, FILE_SEPARATOR, dataFileName)));
			sb.delete(0, sb.length());
			String line;
			while ((line = br.readLine()) != null)
				sb.append(line);
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			close(br);
		}

		return sb.toString();
	}

	public static final String DEFAULT_TEST_DATA_FOLDER = System.getProperty("data_folder");

	private static final StringBuffer sb = new StringBuffer();
}

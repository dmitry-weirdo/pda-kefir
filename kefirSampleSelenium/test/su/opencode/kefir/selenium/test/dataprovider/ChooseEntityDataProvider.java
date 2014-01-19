/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.selenium.test.dataprovider;

import org.testng.annotations.DataProvider;
import su.opencode.kefir.sampleSrv.ChooseEntity;
import su.opencode.kefir.selenium.AbstractDataProvider;
import su.opencode.kefir.srv.json.JsonObject;

import java.util.HashMap;
import java.util.Iterator;

import static su.opencode.kefir.srv.json.JsonObject.fromJson;

public class ChooseEntityDataProvider extends AbstractDataProvider
{
	@DataProvider(name = DATA_PROVIDER_NAME)
	public static Iterator<Object[]> loadData() {
		return new ChooseEntityDataProvider().loadDataFromJson(VO_DATA_FILE);
	}

	@Override
	protected Object[] getEntityFromJson(HashMap<String, String[]> createEntityData, HashMap<String, String[]> updateEntityData) {
		return new Object[] { fromJson(createEntityData, ENTITY_CLASS), fromJson(updateEntityData, ENTITY_CLASS) };
	}

	private static final Class<? extends JsonObject> ENTITY_CLASS = ChooseEntity.class;
	private static final String VO_DATA_FILE = "ChooseEntity.json";

	public static final String DATA_PROVIDER_NAME = "ChooseEntity";
}

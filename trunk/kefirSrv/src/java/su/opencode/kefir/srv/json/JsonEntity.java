/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 30.09.2010 11:23:20$
*/
package su.opencode.kefir.srv.json;

import org.json.JSONException;
import org.json.JSONObject;

public interface JsonEntity
{
	JSONObject toJson() throws JSONException;

	// String getDefaultSortField();
}
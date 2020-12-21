package org.xbf.addons.core_http.models;

import org.xbf.addons.core_http.KeyLevel;
import org.xbf.core.Data.SmartTable;
import org.xbf.core.Data.SmartTableObjectNoKey;
import org.xbf.core.Data.Annotations.Include;
import org.xbf.core.Data.Annotations.Key;

public class APIKey extends SmartTableObjectNoKey {

	public APIKey() {
		super("http_server_keys");
	}
	
	@Include
	@Key
	public String key;
	
	@Include
	public int uid;
	
	@Include
	public int keyLevelInt;
	
	@Include
	public boolean active;
	
	public KeyLevel level;
	
	@Override
	public void objectLoaded() {
		level = KeyLevel.of(keyLevelInt);
	}
	
	public static SmartTable<APIKey> getSmartTable() {
		return new SmartTable<APIKey>("http_server_keys", APIKey.class);
	}

	
	
}

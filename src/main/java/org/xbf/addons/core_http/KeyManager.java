package org.xbf.addons.core_http;

import java.util.Random;

import org.xbf.addons.core_http.models.APIKey;
import org.xbf.core.Utils.Map.FastMap;

public class KeyManager {

	public static String generateKey(long id) {
		int leftLimit = 'a'; // letter 'a'
	    int rightLimit = 'z'; // letter 'z'
	    int targetStringLength = 256;
	    Random random = new Random();
	    StringBuilder buffer = new StringBuilder(targetStringLength);
	    for (int i = 0; i < targetStringLength; i++) {
	        int randomLimitedInt = leftLimit + (int) 
	          (random.nextFloat() * (rightLimit - leftLimit + 1));
	        buffer.append((char) randomLimitedInt);
	    }
	    String generatedString = buffer.toString();
	    return generatedString;
	}

	public static APIKey getKey(String apiKey) {
		return APIKey.getSmartTable().get(new FastMap<String, String>().add("apikey", apiKey));
	}

	public static boolean validateKey(String apiKey) {
		APIKey key = getKey(apiKey);
		if(key == null) return false;
		if(!key.active) return false;
		return true;
	}
	
}

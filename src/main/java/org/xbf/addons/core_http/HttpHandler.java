package org.xbf.addons.core_http;

import java.io.IOException;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.ResponseException;

public class HttpHandler {

	public String url = "/api/" + this.getClass().getName().toLowerCase();

	public KeyLevel level = KeyLevel.USER;
	
	public Logger l = LoggerFactory.getLogger(this.getClass());

	public HttpHandler() {

	}

	public String getBody(IHTTPSession session) {
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			session.parseBody(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
        final String json = map.get("postData");
        return json;
//		InputStreamReader isReader = new InputStreamReader(session.getInputStream());
//		BufferedReader reader = new BufferedReader(isReader);
//		StringBuffer sb = new StringBuffer();
//		String str;
//		try {
//			while ((str = reader.readLine()) != null) {
//				sb.append(str);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return sb.toString();
	}

}

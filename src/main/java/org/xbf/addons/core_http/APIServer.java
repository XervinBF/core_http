package org.xbf.addons.core_http;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Set;

import org.reflections.Reflections;
import org.slf4j.LoggerFactory;
import org.xbf.addons.core_http.models.APIKey;

import com.google.gson.Gson;

import ch.qos.logback.classic.Logger;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response.Status;

public class APIServer extends NanoHTTPD {

	Logger l = (Logger) LoggerFactory.getLogger(APIServer.class);
	
	public static ArrayList<HttpHandler> handlers = new ArrayList<HttpHandler>();
	
	public APIServer() {		
		super(HttpServerPlugin.config.port);
		l.info("Starting API server");
        try {
			start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
		} catch (IOException e) {
			e.printStackTrace();
		}
        l.info("API Server running on port " + HttpServerPlugin.config.port);
	}
	

	public static CookieHandler getCookieHandler(IHTTPSession sess) {
		return sess.getCookies();
	}
	

	@Override
	public Response serve(IHTTPSession session) {
		long start = System.currentTimeMillis();
		String url = session.getUri();
		if(url.equals("/api/test")) {
			return newFixedLengthResponse("OK");
		}
		l.info(url + " - " + session.getMethod().name());
		
//		for (String h : session.getHeaders().keySet()) {
//			System.out.println(h + ": " + session.getHeaders().get(h));
//		}
		String apiKey = null;
		if(session.getParms().containsKey("apikey"))
			apiKey = session.getParms().get("apikey");
		else if(session.getHeaders().containsKey("xkey"))
			apiKey = session.getHeaders().get("xkey");
		
		
		
		
		for (HttpHandler icls : handlers) {
			Class<? extends HttpHandler> cls = icls.getClass();
			if(url.startsWith(icls.url)) {
			
				if(icls.level != KeyLevel.NONE) {
					if(apiKey == null) {
						Response res = newFixedLengthResponse(Status.UNAUTHORIZED, "text", "Supply api key.\nURL Parameters: apikey\n\tExample: /api/user/getusers?apikey=xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx\n\nHeaders: xkey\n\tExample xkey: xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
						res.addHeader("Cache-Control", "no-cache");
						return res;
					}
						
					if(!KeyManager.validateKey(apiKey)) {
						Response res = newFixedLengthResponse(Status.UNAUTHORIZED, "text", "Invalid Key");
						res.addHeader("Cache-Control", "no-cache");
						return res;
					}
					
					APIKey key = KeyManager.getKey(apiKey);
					
					if(icls.level.level < key.level.level) {
						Response res = newFixedLengthResponse(Status.FORBIDDEN, "text", "Not enough permissions");
						res.addHeader("Cache-Control", "no-cache");
						return res;
					}
				}
				
				boolean anyMatches = false;
				
				for (java.lang.reflect.Method m : cls.getMethods()) {
					String aurl = icls.url + "/" + m.getName().toLowerCase();
					if(aurl.equals(url)) {
						anyMatches = true;
						break;
					}
				}

				for (java.lang.reflect.Method m : cls.getMethods()) {
					String aurl = icls.url + "/" + m.getName().toLowerCase();
					System.out.println(aurl);
					if(url.equalsIgnoreCase(aurl) || (m.getName().equalsIgnoreCase("index") && !anyMatches)) {
						String idPart = url.replace(icls.url + "/", "");
						Object[] margs = new Object[m.getParameterCount()];
						Parameter[] ps = m.getParameters();
						String[] query = new String[0];
						if(session.getQueryParameterString() != null)
							query = session.getQueryParameterString().split("&");
						for (int i = 0; i < ps.length; i++) {
							Parameter p = ps[i];
							if(p.getType().getSimpleName().equalsIgnoreCase("IHTTPSession")) {
								margs[i] = session;
							} else if (p.getType().getSimpleName().equalsIgnoreCase("APIKey")) {
								margs[i] = KeyManager.getKey(apiKey);
							} else {
								String n = p.getName();
								if((n.equalsIgnoreCase("id")) && !anyMatches) {
									margs[i] = idPart;
								}
								for (String s : query) {
									System.out.println(s + " - " + n);
									if(s.startsWith(n)) {
										try {
											margs[i] = s.split("=")[1];
										} catch (Exception e) {
											return newFixedLengthResponse("ArgumentParsingError: Failed to parse arguments in url. Please check your url again. ("+ s + ")");
										}
									}
								}
								
								if(p.getType().getSimpleName().equals("int")) {
									margs[i] = Integer.parseInt((String) margs[i]);
								}
							}
						}
						try {
							Object o = m.invoke(icls, margs);
							if(o instanceof HttpResult) {
								l.info("Returned HttpResult (" + (System.currentTimeMillis() - start) + " ms)");
								HttpResult result = ((HttpResult)o);
								Response res = newFixedLengthResponse(result.status, "text", result.content);
								res.addHeader("Cache-Control", "no-cache");
								return res;
							}
							
							if(o instanceof Response) {
								l.info("Returned response (" + (System.currentTimeMillis() - start) + " ms)");
								((Response)o).addHeader("Cache-Control", "no-cache");
								return (Response) o;
							}
							
							if(o instanceof String) {
								l.info("Returned String (" + (System.currentTimeMillis() - start) + " ms)");
								Response res = newFixedLengthResponse((String) o);
								res.addHeader("Cache-Control", "no-cache");
								return res;
							}
							
							l.info("Returned object (" + (System.currentTimeMillis() - start) + " ms)");
							Response res = newFixedLengthResponse(Status.OK, "application/json", new Gson().toJson(o));
							res.addHeader("Cache-Control", "no-cache");
							return res;
						} catch (Exception e) {
							e.printStackTrace();
							l.info("Returned 500 (" + (System.currentTimeMillis() - start) + " ms)");
							Response res = newFixedLengthResponse(Status.INTERNAL_ERROR, "text", "An internal error occured\n" + e.getClass().getSimpleName());
							res.addHeader("Cache-Control", "no-cache");
							return res;
						}
						
					}
				}
				
			}
			
		}
		l.info("Returned 404 (" + (System.currentTimeMillis() - start) + " ms)");
		Response res = newFixedLengthResponse(Status.NOT_FOUND, "text", "Could not find endpoint");
		res.addHeader("Cache-Control", "no-cache");
		return res;
	}
	
}

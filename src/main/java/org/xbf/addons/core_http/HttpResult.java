package org.xbf.addons.core_http;

import com.google.gson.Gson;

import fi.iki.elonen.NanoHTTPD.Response.Status;

public class HttpResult {

	Status status;
	String content;
	public HttpResult(String content, Status status) {
		this.content = content;
		this.status = status;
	}
	
	public HttpResult(Object content, Status status) {
		this(new Gson().toJson(content), status);
	}
	
	public HttpResult(Status status) {
		this(null, status);
	}
	
	public HttpResult(String content) {
		this(content, Status.OK);
	}
	
	public HttpResult(Object content) {
		this(content, Status.OK);
	}
	
}

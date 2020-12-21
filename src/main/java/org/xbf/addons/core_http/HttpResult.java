package org.xbf.addons.core_http;

import com.google.gson.Gson;

import fi.iki.elonen.NanoHTTPD.Response.Status;

public class HttpResult {

	HttpStatus status;
	String content;
	public HttpResult(String content, HttpStatus status) {
		this.content = content;
		this.status = status;
	}
	
	public HttpResult(Object content, HttpStatus status) {
		this(new Gson().toJson(content), status);
	}
	
	public HttpResult(HttpStatus status) {
		this(null, status);
	}
	
	public HttpResult(String content) {
		this(content, HttpStatus.OK);
	}
	
	public HttpResult(Object content) {
		this(content, HttpStatus.OK);
	}
	
}

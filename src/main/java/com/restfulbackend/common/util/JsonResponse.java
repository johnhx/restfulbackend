package com.restfulbackend.common.util;

public class JsonResponse {
	public static String SUCCESS = "sucess";
	public static String ERROR = "error";

	private Boolean error;
	
	private String message;
	
	private Object data;

	public JsonResponse() {
		this.error = true;
		this.message = "";
		this.data = null;
	}

	public Boolean getError() {
		return error;
	}
	
	public void setError(Boolean error) {
		this.error = error;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}

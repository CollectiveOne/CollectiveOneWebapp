package org.collectiveone.web.dto;

public class PostResult {
	String result;
	String message;
	
	public PostResult(String _result, String _message) {
		result = _result;
		message = _message;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}

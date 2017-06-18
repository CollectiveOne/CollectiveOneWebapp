package org.collectiveone.common.dto;

public class PostResult {
	String result;
	String message;
	String elementId;
	
	public PostResult(String _result, String _message, String _elementId) {
		result = _result;
		message = _message;
		elementId = _elementId;
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

	public String getElementId() {
		return elementId;
	}

	public void setElementId(String elementId) {
		this.elementId = elementId;
	}
	
	
}

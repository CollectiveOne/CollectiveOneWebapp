package org.collectiveone.common.dto;

import java.util.ArrayList;
import java.util.List;

public class PostResult {
	String result;
	String message;
	List<String> elementIds = new ArrayList<String>();
	
	public PostResult(String _result, String _message, List<String> _elementIds) {
		result = _result;
		message = _message;
		elementIds = _elementIds;
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

	public List<String> getElementIds() {
		return elementIds;
	}

	public void setElementIds(List<String> elementIds) {
		this.elementIds = elementIds;
	}
	
}

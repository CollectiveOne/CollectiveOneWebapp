package org.collectiveone.web.dto;

public class PostResult {
	boolean result;
	String message;
	
	public PostResult(boolean _result, String _message) {
		result = _result;
		message = _message;
	}
}

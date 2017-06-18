package org.collectiveone.common.dto;

public class GetResult<T> {
	String result;
	String message;
	T data;
	
	public GetResult(String _result, String _message, T _data) {
		result = _result;
		message = _message;
		data = _data;
	}
	
	public String isResult() {
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
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
}

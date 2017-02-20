package org.collectiveone.web.dto;

import java.util.List;

public class ObjectListRes<T> {
	private List<T> objects;
	private int[] resSet = {0,0,0};
	
	public List<T> getObjects() {
		return objects;
	}
	public void setObjects(List<T> objects) {
		this.objects = objects;
	}
	public int[] getResSet() {
		return resSet;
	}
	public void setResSet(int[] resSet) {
		this.resSet = resSet;
	}
	
}

package com.ibm.kdd.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SparseEvent extends HashMap<String,String> implements Event  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public SparseEvent() {
		
	}
	
	public SparseEvent(SparseEvent another) {
		Set<String> attrNames = another.getAttrNames();
		for (String attrName: attrNames) {
			this.setValue(attrName, another.getStr(attrName));
		}
	}
	
	public Set<String> getAttrNames() {
		return this.keySet();
	}

	@Override
	public double getDouble(String attrName) {
		// TODO Auto-generated method stub
		return Double.parseDouble(getStr(attrName));
	}

	@Override
	public int getInt(String attrName) {
		// TODO Auto-generated method stub
		return Integer.parseInt(getStr(attrName));
	}

	@Override
	public String getStr(String attrName) {
		// TODO Auto-generated method stub
		return this.get(attrName);
	}

	@Override
	public String getValue(String attrName) {
		// TODO Auto-generated method stub
		return this.get(attrName);
	}

	@Override
	public void setValue(String attrName, double value) {
		// TODO Auto-generated method stub
		this.put(attrName, value+"");
	}

	@Override
	public void setValue(String attrName, int value) {
		// TODO Auto-generated method stub
		this.put(attrName, value+"");
	}

	@Override
	public void setValue(String attrName, String value) {
		// TODO Auto-generated method stub
		this.put(attrName, value);
	}
	
	@Override
	public Event clone() {
		return new SparseEvent(this);
	}

	@Override
	public Event createNew() {
		// TODO Auto-generated method stub
		return new SparseEvent();
	}

	@Override
	public boolean hasAttribute(String attrName) {
		// TODO Auto-generated method stub
		return this.getAttrNames().contains(attrName);
	}

}

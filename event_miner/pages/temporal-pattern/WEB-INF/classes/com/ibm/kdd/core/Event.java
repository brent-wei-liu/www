package com.ibm.kdd.core;

import java.io.Serializable;
import java.util.Set;

public interface Event extends Serializable {
	
	double getDouble(String attrName);
	
	int getInt(String attrName);
		
	String getStr(String attrName);
		
	String getValue(String attrName);
	
	void setValue(String attrName, double value);
	
	void setValue(String attrName, int value);
	
	void setValue(String attrName, String value);
	
	Set<String> getAttrNames();
	
	boolean hasAttribute(String attrName);
	
	int hashCode();
	
	boolean equals(Object obj);
	
	Event clone();
	
	Event createNew();
	
}

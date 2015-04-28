package com.ibm.kdd.core;

import java.io.Serializable;
import java.util.Set;

public interface Events extends Serializable, Iterable<Event>{
	
	Set<String> getAttrNames();
	
	DataType getAttrDataType(String attrName);
	
	Event createEvent();
	
	Event getEvent(int index);
	
	int getNumEvents();
		
}

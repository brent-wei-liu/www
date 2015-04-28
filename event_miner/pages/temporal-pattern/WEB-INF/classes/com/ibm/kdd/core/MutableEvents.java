package com.ibm.kdd.core;

public interface MutableEvents extends Events{
	
	void addEvent(Event event);
	
	void insertEvent(int index, Event event);
	
	void setEvent(int index, Event event);
	
	void removeEvent(int index);
	
	void shuffle();
	
	void clear();

}

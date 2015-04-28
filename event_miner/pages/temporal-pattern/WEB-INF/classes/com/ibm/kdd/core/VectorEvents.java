package com.ibm.kdd.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class VectorEvents implements MutableEvents {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Map<String, Integer> attrIndexes;
	
	private String[] attrNames;
	
	private DataType[] dataTypes;
	
	private int numNumericAttrs;
	
	private List<VectorEvent> eventList;
	
	public VectorEvents(String[] attrNames, DataType[] dataTypes) {
		this.eventList = new ArrayList<VectorEvent>();
		// Count the number of numeric attributes
		numNumericAttrs = 0;
		for (int i=0; i<dataTypes.length; i++) {
			DataType dataType = dataTypes[i];
			if (DataType.isNumeric(dataType)) {
				numNumericAttrs++;
			}
		}
		// Rearrange the attributes, put the numeric attributes ahead string attributes
		this.attrNames = new String[attrNames.length];
		this.dataTypes = new DataType[dataTypes.length];
		this.attrIndexes = new HashMap<String, Integer>();
		int numericAttrIndex = 0;
		int strAttrIndex = numNumericAttrs;
		for (int i=0; i<dataTypes.length; i++) {
			DataType dataType = dataTypes[i];
			if (DataType.isNumeric(dataType)) {
				this.attrNames[numericAttrIndex] = attrNames[i];
				this.dataTypes[numericAttrIndex] = dataTypes[i];
				attrIndexes.put(attrNames[i], numericAttrIndex);
				numericAttrIndex++;				
			}
			else {
				this.attrNames[strAttrIndex] = attrNames[i];
				this.dataTypes[strAttrIndex] = dataTypes[i];
				attrIndexes.put(attrNames[i], strAttrIndex);
				strAttrIndex++;
			}
		}
	}
	
	public int getAttrIndex(String attrName) {
		Integer index = attrIndexes.get(attrName);
		if (index == null) {
			return -1;
		}
		else {
			return index;
		}
	}
	
	public int getNumericAttrIndex(String attrName) {
		// TODO Auto-generated method stub
		return getAttrIndex(attrName);
	}

	public int getStrAttrIndex(String attrName) {
		// TODO Auto-generated method stub
		int index = getAttrIndex(attrName);
		if (index < numNumericAttrs) {
			return -1;
		}
		else {
			return index - numNumericAttrs;
		}
	}

	public int getNumNumericAttrs() {
		return numNumericAttrs;
	}
	
	public int getNumAttrs() {
		return attrNames.length;
	}

	@Override
	public Set<String> getAttrNames() {
		// TODO Auto-generated method stub
		return attrIndexes.keySet();
	}

	@Override
	public DataType getAttrDataType(String attrName) {
		// TODO Auto-generated method stub
		int index = getAttrIndex(attrName);
		if (index == -1) {
			return null;
		}
		else {
			return dataTypes[index];
		}
	}

	@Override
	public Event createEvent() {
		// TODO Auto-generated method stub
		return new VectorEvent(this);
	}

	@Override
	public Event getEvent(int index) {
		// TODO Auto-generated method stub
		return eventList.get(index);
	}

	@Override
	public int getNumEvents() {
		// TODO Auto-generated method stub
		return eventList.size();
	}

	@Override
	public void addEvent(Event event) {
		// TODO Auto-generated method stub
		eventList.add((VectorEvent)event);
	}
	
	@Override
	public void insertEvent(int index, Event event) {
		// TODO Auto-generated method stub
		eventList.add(index, (VectorEvent)event);
	}	

	@Override
	public void setEvent(int index, Event event) {
		// TODO Auto-generated method stub
		eventList.set(index, (VectorEvent)event);
	}

	@Override
	public void removeEvent(int index) {
		// TODO Auto-generated method stub
		eventList.remove(index);
	}

	@Override
	public Iterator<Event> iterator() {
		// TODO Auto-generated method stub
		return new EventIterator(this);
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		eventList.clear();
	}
	
	@Override
	public void shuffle() {
		// TODO Auto-generated method stub
		Collections.shuffle(eventList);
	}	
	
	@Override
	public String toString() {
		return eventList.toString();
	}


	

}

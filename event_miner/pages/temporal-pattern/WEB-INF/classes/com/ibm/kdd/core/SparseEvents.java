package com.ibm.kdd.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SparseEvents implements MutableEvents {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4642438970222454651L;
	
	private Set<String> attrNames = new HashSet<String>();
	
	private Map<String,DataType> dataTypes = new HashMap<String, DataType>();
	
	private List<SparseEvent> eventList = new ArrayList<SparseEvent>();
	
	public SparseEvents() {
		
	}
	
	public SparseEvents(int capacity) {
		eventList = new ArrayList<SparseEvent>(capacity);
	}
	
	public SparseEvents(Map<String,DataType> dataTypes) {
		this.dataTypes = dataTypes;
	}
	
	public SparseEvents(List<String> attrNames, List<DataType> dataTypes) {
		this.dataTypes = new HashMap<String, DataType>();
		for (int i=0; i<attrNames.size(); i++) {
			this.dataTypes.put(attrNames.get(i), dataTypes.get(i));
		}
	}

	@Override
	public Set<String> getAttrNames() {
		// TODO Auto-generated method stub
		return attrNames;
	}

	@Override
	public DataType getAttrDataType(String attrName) {
		// TODO Auto-generated method stub
		return dataTypes.get(attrName);
	}

	@Override
	public Event createEvent() {
		// TODO Auto-generated method stub
		return new SparseEvent();
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
	public Iterator<Event> iterator() {
		// TODO Auto-generated method stub
		return new EventIterator(this);
	}

	@Override
	public void addEvent(Event event) {
		// TODO Auto-generated method stub
		SparseEvent sparseEvent = (SparseEvent)event;
		eventList.add(sparseEvent);
		attrNames.addAll(sparseEvent.getAttrNames());
	}
	
	public void addAll(Event[] events) {
		for (Event event : events) {
			this.addEvent(event);
		}
	}

	@Override
	public void insertEvent(int index, Event event) {
		// TODO Auto-generated method stub
		SparseEvent sparseEvent = (SparseEvent)event;
		eventList.add(index, sparseEvent);
		attrNames.addAll(sparseEvent.getAttrNames());
	}

	@Override
	public void setEvent(int index, Event event) {
		// TODO Auto-generated method stub
		SparseEvent sparseEvent = (SparseEvent)event;
		eventList.set(index, sparseEvent);
		attrNames.addAll(sparseEvent.getAttrNames());
	}

	@Override
	public void removeEvent(int index) {
		// TODO Auto-generated method stub
		eventList.remove(index);
	}

	@Override
	public void shuffle() {
		// TODO Auto-generated method stub
		Collections.shuffle(eventList);
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		eventList.clear();
		attrNames.clear();
	}
	
	
}

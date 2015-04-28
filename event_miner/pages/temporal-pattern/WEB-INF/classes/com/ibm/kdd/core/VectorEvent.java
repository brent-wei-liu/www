package com.ibm.kdd.core;

import java.util.Arrays;
import java.util.Set;

public class VectorEvent implements Event {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private transient VectorEvents events = null;
	
	private double[] numValues = null;
	
	private String[] strValues = null;
	
	public VectorEvent(VectorEvents events) {
		this.events = events;
		int numNumericAttrs = events.getNumNumericAttrs();
		int numStrAttrs = events.getNumAttrs() - numNumericAttrs;
		if (numNumericAttrs > 0) {
			this.numValues = new double[numNumericAttrs];
		}
		if (numStrAttrs > 0) {
			this.strValues = new String[numStrAttrs]; 
		}
	}

	@Override
	public double getDouble(String attrName) {
		// TODO Auto-generated method stub		
		int index = events.getNumericAttrIndex(attrName);
		return numValues[index];
	}
	
	public double getDouble(int index) {
		// TODO Auto-generated method stub		
		return numValues[index];
	}

	@Override
	public int getInt(String attrName) {
		// TODO Auto-generated method stub
		int index = events.getNumericAttrIndex(attrName);
		return (int)(numValues[index]);
	}
	
	public int getInt(int index) {
		// TODO Auto-generated method stub
		return (int)numValues[index];
	}

	@Override
	public String getStr(String attrName) {
		// TODO Auto-generated method stub
		int index = events.getStrAttrIndex(attrName);
		return strValues[index];
	}
	

	public String getStr(int index) {
		// TODO Auto-generated method stub
		return strValues[index];
	}

	@Override
	public String getValue(String attrName) {
		// TODO Auto-generated method stub
		int index = events.getAttrIndex(attrName);
		if (index == -1) {
			return null;
		}
		if (index < events.getNumNumericAttrs()) {
			return numValues[index]+"";
		}
		else {
			return strValues[index - events.getNumNumericAttrs()];
		}
	}

	@Override
	public void setValue(String attrName, double value) {
		// TODO Auto-generated method stub
		int index = events.getAttrIndex(attrName);
		numValues[index] = value;
	}

	@Override
	public void setValue(String attrName, int value) {
		// TODO Auto-generated method stub
		int index = events.getAttrIndex(attrName);
		numValues[index] = value;
	}

	@Override
	public void setValue(String attrName, String value) {
		// TODO Auto-generated method stub
		int index = events.getAttrIndex(attrName);
		strValues[index - events.getNumNumericAttrs()] = value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(numValues);
		result = prime * result + Arrays.hashCode(strValues);
		return result;
	}

	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VectorEvent other = (VectorEvent) obj;
		if (!Arrays.equals(numValues, other.numValues))
			return false;
		if (!Arrays.equals(strValues, other.strValues))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Event(" + Arrays.toString(numValues)
				+ "," + Arrays.toString(strValues) + ")";
	}
	
	@Override
	public Event clone() {
		VectorEvent cloneEvent = new VectorEvent(events);
		if (numValues != null) {
			cloneEvent.numValues = Arrays.copyOf(numValues, numValues.length);
		}
		if (strValues != null) {
			cloneEvent.strValues = Arrays.copyOf(strValues, strValues.length);
		}
		return cloneEvent;
	}

	@Override
	public Set<String> getAttrNames() {
		// TODO Auto-generated method stub
		return events.getAttrNames();
	}

	@Override
	public Event createNew() {
		// TODO Auto-generated method stub
		return events.createEvent();
	}

	@Override
	public boolean hasAttribute(String attrName) {
		// TODO Auto-generated method stub
		return this.getAttrNames().contains(attrName);
	}

	
}

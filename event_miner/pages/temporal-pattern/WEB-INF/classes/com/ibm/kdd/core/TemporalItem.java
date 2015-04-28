package com.ibm.kdd.core;

import java.io.Serializable;

public class TemporalItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public int type;

	public long timestamp;

	public double weight;

	public String account = "all";

	public TemporalItem() {
	}
	
	public TemporalItem(String account) {
		this.account = account;
	}

	public TemporalItem(int type,String account) {
		this(type, 0,account);
	}
	
	public TemporalItem(int type) {
		this(type, 0);
	}
	
	public TemporalItem(int type, long timestamp) {
		this.type = type;
		this.timestamp = timestamp;
	}

	public TemporalItem(int type, long timestamp, String account) {
		this.type = type;
		this.timestamp = timestamp;
		this.account = account;
	}

	public TemporalItem(int type, long timestamp, double weight) {
		this(type, timestamp);
		this.weight = weight;
	}

	public TemporalItem(int type, long timestamp, double weight, String account) {
		this(type, timestamp);
		this.weight = weight;
		this.account = account;
	}

	@Override
	public String toString() {
		return "[type=" + type + ", time=" + timestamp + ", w=" + weight + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
		result = prime * result + type;
		long temp;
		temp = Double.doubleToLongBits(weight);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		TemporalItem other = (TemporalItem) obj;
		if (timestamp != other.timestamp)
			return false;
		if (type != other.type)
			return false;
		if (Double.doubleToLongBits(weight) != Double.doubleToLongBits(other.weight))
			return false;
		return true;
	}

}

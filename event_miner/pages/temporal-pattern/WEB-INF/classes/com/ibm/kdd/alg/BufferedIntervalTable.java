package com.ibm.kdd.alg;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

public class BufferedIntervalTable {
	
	LinkedList<IntervalIndice> buf = null;
		
	Iterator<IntervalIndice> it;
	
	IntervalTable table;
	
	boolean itEnds = false;
	
	public BufferedIntervalTable(IntervalTable table) {
		this.table = table;
		buf = new LinkedList<IntervalIndice>();
		it = table.iterator();		
		itEnds = false;
	}
	
	public long getNextInterval(int index) {
		if (index >= buf.size()) {
			fetch(index-buf.size()+1);
		}
		return buf.get(index).interval;
	}
	
	public Set<Integer> getNextAIndice(int index) {
		if (index >= buf.size()) {
			fetch(index-buf.size()+1);
		}
		return buf.get(index).AIndice;
	}
	
	public Set<Integer> getNextBIndice(int index) {
		if (index >= buf.size()) {
			fetch(index-buf.size()+1);
		}
		return buf.get(index).BIndice;
	}
	
	public boolean hasNext(int index) {
		if (buf.size() > index) {
			return true;
		}
		else {
			fetch(index-buf.size()+1);
			if (buf.size() <= index) {
				return false;
			}
			else {
				return true;
			}
		}
	}
	
	public void moreForward() {
		buf.removeFirst();
	}
	
	public long getMemoryCost() {
		return buf.size() + table.getNumMemElements(it);
	}
	
	
	private int fetch(int n) {
		for (int i=0; i<n; i++) {
			if (it.hasNext() == false) {
				return i;
			}
			buf.addLast(it.next());
		}
		return n;
	}



}

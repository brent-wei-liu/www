package com.ibm.kdd.alg;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * BSD License
 * 
 * @author Liang TANG
 *
 */
public class IntervalIndice {
	
	public Set<Integer> AIndice;
	public Set<Integer> BIndice;
	public long interval;
	
	public IntervalIndice(long interval, Set<Integer> AIndice, Set<Integer> BIndice) {
		this.interval = interval;
		this.AIndice = new HashSet<Integer>(AIndice);
		this.BIndice = new HashSet<Integer>(BIndice);
	}

	@Override
	public String toString() {
		return "[interval=" + interval+", AIndice=" + AIndice + ", BIndice=" + BIndice+"]";
	}
	
}

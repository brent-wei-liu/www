package com.ibm.kdd.alg;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 
 * BSD License
 * 
 * @author Liang TANG
 *
 */
public abstract class IntervalTable implements Iterable<IntervalIndice> {
		
	public IntervalTable() {
		
	}
	
	abstract public long getNumMemElements(Iterator<IntervalIndice> it);
		
	protected static SortedMap<Long, Set<Integer>> mergeByTimestamp(long[] timestamps) {
		SortedMap<Long, Set<Integer>> timestampMap = new TreeMap<Long, Set<Integer>>();
		for (int i = 0; i < timestamps.length; i++) {
			long t = timestamps[i];
			Set<Integer> indice = timestampMap.get(t);
			if (indice == null) {
				indice = new HashSet<Integer>();
				timestampMap.put(t, indice);
			}
			indice.add(i);
		}
		return timestampMap;
	}

	
}

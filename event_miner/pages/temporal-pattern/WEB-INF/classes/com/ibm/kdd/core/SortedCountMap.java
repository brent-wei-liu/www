package com.ibm.kdd.core;

import java.util.Collection;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;

public class SortedCountMap<K> {
	
	private SortedMap<K, Integer> map = new TreeMap<K, Integer>();
	
	public SortedCountMap() {
		
	}
	
	public void addAll(Collection<K> c) {
		for (K key: c) {
			add(key);
		}
	}
	
	public void add(K key) {
		add(key, 1);
	}
	
	public void add(K key, int countToAdd) {
		Integer count = map.get(key);
		if (count == null) {
			count = 0;
		}
		count += countToAdd;
		map.put(key, count);
	}
	
	public void remove(K key) {
		remove(key, 1);
	}
	
	public void remove(K key, int countToRemove) {
		Integer count = map.get(key);
		if (count == null) {
			throw new Error("Not contain this key : "+key);
		}
		count -=countToRemove;
		if (count == 0) {
			map.remove(key);
		}
		else {
			map.put(key, count);
		}
	}
		
	public int getCount(K key) {
		Integer count = map.get(key);
		if (count == null) {
			return 0;
		}
		else {
			return count;
		}
	}
	
	public int size() {
		return map.size();
	}
	
	public SortedSet<K> keySet() {
		return (SortedSet<K>) map.keySet();
	}

	@Override
	public String toString() {
		return map.toString();
	}
	
	

}

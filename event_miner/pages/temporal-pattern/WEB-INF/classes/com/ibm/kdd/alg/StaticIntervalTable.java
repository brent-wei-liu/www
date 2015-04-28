package com.ibm.kdd.alg;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * 
 * BSD License
 * 
 * @author Liang TANG
 * 
 */
public class StaticIntervalTable extends IntervalTable {

	Map<Long, Set<Integer>> AIndiceMap = new HashMap<Long, Set<Integer>>();

	Map<Long, Set<Integer>> BIndiceMap = new HashMap<Long, Set<Integer>>();

	long[] intervals;

	long maxInterval;

	long totalElemSize;

	public StaticIntervalTable(long[] A_timestamps, long[] B_timestamps,
			long maxInterval) throws InterruptedException {
		// TODO Auto-generated constructor stub
		this.maxInterval = maxInterval;
		SortedMap<Long, Set<Integer>> A_timestampMap = mergeByTimestamp(A_timestamps);
		SortedMap<Long, Set<Integer>> B_timestampMap = mergeByTimestamp(B_timestamps);
		totalElemSize = 0;
		if (maxInterval > 0) {
			for (long A_t : A_timestampMap.keySet()) {
				Set<Integer> AIndice = A_timestampMap.get(A_t);
				for (long B_t : B_timestampMap.keySet()) {
					long interval = B_t - A_t;
					if (interval >= 0) {
						if (interval > maxInterval) {
							break;
						}
						Set<Integer> BIndice = B_timestampMap.get(B_t);
						addIntervals(interval, AIndice, BIndice);
						totalElemSize++;
					}

				}
				if (Thread.interrupted()) {
					throw new InterruptedException();
				} else {
					Thread.yield();
				}
			}
		} else {
			for (long A_t : A_timestampMap.keySet()) {
				Set<Integer> AIndice = A_timestampMap.get(A_t);
				for (long B_t : B_timestampMap.keySet()) {
					long interval = B_t - A_t;
					if (interval >= 0) {
						Set<Integer> BIndice = B_timestampMap.get(B_t);
						addIntervals(interval, AIndice, BIndice);
						totalElemSize++;
					}
				}
				if (Thread.interrupted()) {
					throw new InterruptedException();
				} else {
					Thread.yield();
				}
			}
		}
		this.intervals = getSortedIntervalArray();
	}

	private void addIntervals(long interval, Collection<Integer> AIndice,
			Collection<Integer> BIndice) {
		Set<Integer> AIndices = AIndiceMap.get(interval);
		Set<Integer> BIndices = BIndiceMap.get(interval);
		if (AIndices == null) {
			AIndices = new HashSet<Integer>();
			AIndiceMap.put(interval, AIndices);
			BIndices = new HashSet<Integer>();
			BIndiceMap.put(interval, BIndices);
		}
		AIndices.addAll(AIndice);
		BIndices.addAll(BIndice);
	}

	private long[] getSortedIntervalArray() {
		SortedSet<Long> allIntervals = new TreeSet<Long>();
		allIntervals.addAll(AIndiceMap.keySet());
		long[] array = new long[allIntervals.size()];
		int i = 0;
		for (Long interval : allIntervals) {
			array[i] = interval;
			i++;
		}
		return array;
	}

	@Override
	public Iterator<IntervalIndice> iterator() {
		// TODO Auto-generated method stub
		return new IntervalIterator();
	}

	@Override
	public long getNumMemElements(Iterator<IntervalIndice> it) {
		// TODO Auto-generated method stub
		return totalElemSize + 1;
	}

	public class IntervalIterator implements Iterator<IntervalIndice> {

		int currentIndex = 0;

		public IntervalIterator() {
			currentIndex = 0;
		}

		@Override
		public boolean hasNext() {
			// TODO Auto-generated method stub
			return currentIndex < intervals.length;
		}

		@Override
		public IntervalIndice next() {
			// TODO Auto-generated method stub
			if (currentIndex >= intervals.length) {
				return null;
			}
			long interval = intervals[currentIndex];
			currentIndex++;
			Set<Integer> AIndices = AIndiceMap.get(interval);
			Set<Integer> BIndices = BIndiceMap.get(interval);
			if (AIndices == null) {
				return new IntervalIndice(interval, new HashSet<Integer>(),
						new HashSet<Integer>());
			} else {
				return new IntervalIndice(interval, AIndices, BIndices);
			}

		}

		@Override
		public void remove() {
			// TODO Auto-generated method stub
			throw new Error("Not support remove");
		}

	}

}

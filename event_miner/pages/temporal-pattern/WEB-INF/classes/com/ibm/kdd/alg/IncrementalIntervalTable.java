package com.ibm.kdd.alg;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;


public class IncrementalIntervalTable extends IntervalTable {
	
	long[] A_dts;
	long[] B_dts;
	Set<Integer>[] A_dtIndices = null;
	Set<Integer>[] B_dtIndices = null;
	long[] A_timestamps;
	long[] B_timestamps;
	long maxInterval;
	
	public IncrementalIntervalTable(long[] A_timestamps, long[] B_timestamps, long maxInterval) throws InterruptedException {
		// TODO Auto-generated constructor stub
		this.A_timestamps = A_timestamps;
		this.B_timestamps = B_timestamps;
		this.maxInterval = maxInterval;
		Map<Long, Set<Integer>> A_timestampMap = mergeByTimestamp(A_timestamps);
		Map<Long, Set<Integer>> B_timestampMap = mergeByTimestamp(B_timestamps);
		A_dts = new long[A_timestampMap.keySet().size()];
		B_dts = new long[B_timestampMap.keySet().size()];
		A_dtIndices = new Set[A_dts.length];
		B_dtIndices = new Set[B_dts.length];
		int i = 0;
		for (Long t : A_timestampMap.keySet()) {
			A_dts[i] = t;
			A_dtIndices[i] = A_timestampMap.get(t);
			i++;
		}
		i=0;
		for (Long t: B_timestampMap.keySet()) {
			B_dts[i] = t;
			B_dtIndices[i] = B_timestampMap.get(t);
			i++;
		}
	}

	@Override
	public Iterator<IntervalIndice> iterator() {
		// TODO Auto-generated method stub		
		return new IntervalIterator();
	}
	
	static class IntervalPair implements Comparable<IntervalPair> {
		
		public long interval;
		
		public int  index;
		
		public IntervalPair(long interval, int index) {
			this.index = index;
			this.interval = interval;
		}

		@Override
		public int compareTo(IntervalPair o) {
			// TODO Auto-generated method stub
			if (this.interval < o.interval) {
				return -1;
			}
			else if (this.interval > o.interval) {
				return 1;
			}
			else {
				return 0;
			}
		}

		@Override
		public String toString() {
			return "[" + interval + ", " + index + "]";
		}
		
	}
	
	@Override
	public long getNumMemElements(Iterator<IntervalIndice> it) {
		// TODO Auto-generated method stub		
		return ((IntervalIterator)it).getNumMemElements()
				+ this.A_timestamps.length
				+ this.B_timestamps.length;
	}

	

	
	public class IntervalIterator implements Iterator<IntervalIndice> {
		
		int[]  curBIndice = null;
		PriorityQueue<IntervalPair> intervalHeap;
		
		public IntervalIterator() {
			this.curBIndice = new int[A_dts.length];
			Arrays.fill(this.curBIndice, 0);
			
			// find the next B index for each A
			intervalHeap = new PriorityQueue<IntervalPair>(A_dts.length);
			for (int AIndex=0; AIndex<curBIndice.length; AIndex++) {
				int BIndex = Arrays.binarySearch(B_dts, A_dts[AIndex]);
				if (BIndex < 0) {
					BIndex = -BIndex-1;
				}
				curBIndice[AIndex] = BIndex;
				if (BIndex < B_dts.length) {
					long interval = B_dts[BIndex] - A_dts[AIndex];
					if (maxInterval <= 0 || interval <= maxInterval) {
						intervalHeap.add(new IntervalPair(interval, AIndex));
					}
				}
			}
		}

		@Override
		public boolean hasNext() {
			// TODO Auto-generated method stub
			return intervalHeap.size() > 0;
		}

		@Override
		public IntervalIndice next() {
			// TODO Auto-generated method stub
			long interval;
			IntervalPair minInterval = intervalHeap.poll();
			int AIndex = minInterval.index;
			int BIndex = curBIndice[AIndex];
			IntervalIndice ret = new IntervalIndice(minInterval.interval, A_dtIndices[AIndex], B_dtIndices[BIndex]);
			curBIndice[AIndex]++;
			if (curBIndice[AIndex] < B_dts.length) {
				interval = B_dts[curBIndice[AIndex]] - A_dts[AIndex];
				if (maxInterval <= 0 || interval <= maxInterval) {
					intervalHeap.add(new IntervalPair(interval, AIndex));
				}
			}
			
			while(!intervalHeap.isEmpty()) {
				IntervalPair nextInterval = intervalHeap.peek();
				if (nextInterval.interval > minInterval.interval) {
					break;
				}
				intervalHeap.poll();

				AIndex = nextInterval.index;
				BIndex = curBIndice[AIndex];
				ret.AIndice.addAll(A_dtIndices[AIndex]);
				ret.BIndice.addAll(B_dtIndices[BIndex]);
				curBIndice[AIndex]++;
				if (curBIndice[AIndex] < B_dts.length) {
					interval = B_dts[curBIndice[AIndex]] - A_dts[AIndex];
					if (maxInterval <= 0 || interval <= maxInterval) {
						intervalHeap.add(new IntervalPair(interval, AIndex));
					}
				}
			}
			// System.out.println(intervalHeap.toString());
			return ret;
		}

		@Override
		public void remove() {
			// TODO Auto-generated method stub
			throw new Error("Not support remove!");
		}
		
		public long getNumMemElements() {
			return curBIndice.length+intervalHeap.size();
		}
		
	}

	

}

package com.ibm.kdd.alg;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.ibm.kdd.core.Event;
import com.ibm.kdd.core.Events;
import com.ibm.kdd.core.TemporalItem;
import com.ibm.kdd.core.PatternConstraint;
import com.ibm.kdd.core.TemporalDependency;

public abstract class TemporalDependencyMiner implements TemporalDependencyMineable, Serializable{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected TemporalItem[] events;
	
	protected int numEventTypes;
	
	protected long[][] timestamps;
	
	protected Set<Integer> eventTypes;
	
	protected long[] minSamplePeriods = null;
		
	public TemporalDependencyMiner(TemporalItem[] events) {		
		this.events = new TemporalItem[events.length];
		this.eventTypes = new HashSet<Integer>();
		
		// Normalize the timestamps to make the first event's timestamp be 0
		for (int i = 0; i < this.events.length; i++) {
			this.events[i] = new TemporalItem(events[i].type,events[i].timestamp);
			this.events[i].timestamp -= events[0].timestamp;
			this.eventTypes.add(this.events[i].type);
		}
		
		this.numEventTypes = getNumEventTypes();
		
		this.timestamps = new long[numEventTypes][];
		for (int eventType=0; eventType < numEventTypes; eventType++) {
			timestamps[eventType] = createEventTimestamps(eventType);
		}
		
		this.minSamplePeriods = new long[numEventTypes];
		for (int i=0; i<this.timestamps.length; i++) {
			long[] ts = timestamps[i];			
			if (ts != null && ts.length > 0) {
				long minPeriod = Long.MAX_VALUE;
				for (int j=1; j<ts.length; j++) {
					long period = ts[j] - ts[j-1];
					if (period > 0 && period < minPeriod) {
						minPeriod = period;
					}
				}
				this.minSamplePeriods[i] = minPeriod;
			}
		}
	}
	
	public long[][] getTimestamps() {
		return timestamps;
	}
	
	public List<TemporalDependency> find() throws InterruptedException {
		return find(0, -1);
	}
	
	@Override
	public List<TemporalDependency> find(int topK, long maxInterval) throws InterruptedException {
		int numEventTypes = getNumEventTypes();
		List<TemporalDependency> patterns = new ArrayList<TemporalDependency>();
		PriorityQueue<TemporalDependency> topPatterns = new PriorityQueue<TemporalDependency>(topK+1, 
				new TemporalPatternComparator());
		for (int A=0; A<numEventTypes; A++) {
			for (int B=0; B<numEventTypes; B++) {
				Collection<TemporalDependency> subResults = find(A,B, topK, maxInterval);
				for (TemporalDependency p: subResults) {
					if (p != null) {
//						patterns.add(p);
						topPatterns.add(p);
						if(topK>0&&topPatterns.size()>topK)
							topPatterns.poll();
					}
				}
			}
			Thread.yield();
		}
		
		Iterator<TemporalDependency> iter = topPatterns.iterator();
		while(iter.hasNext()){
			patterns.add(iter.next());
		}
		return patterns;
	}
	
	public TemporalItem[] getEvents() {
		return events;
	}
	
	protected int getNumEventTypes() {
		return eventTypes.size();
	}
	
	protected int getNumEvent(int eventType) {
		int num = 0;
		for (TemporalItem e : events) {
			if (e.type == eventType) {
				num++;
			}
		}
		return num;
	}
	
	public static double getPhi(int a, int b, int c, int d) {
		double d1 = Math.sqrt(a+b);
		double d2 = Math.sqrt(a+c);
		double d3 = Math.sqrt(b+d);
		double d4 = Math.sqrt(c+d);
		if (d1 < Double.MIN_VALUE || d2 < Double.MIN_VALUE || d3 < Double.MIN_VALUE || d4 <Double.MIN_VALUE) {
			return 0;
		}
		double phi = ((a/d1/d2) * (d/d3/d4)) - ((b/d1/d2) * (c/d3/d4));
		return phi;
	}
	
	protected long getMaxTimeIntervalFromTo(int A, int B) {
		long maxInterval = 0;
		int lastAPos = -1;
		for (int i=0; i<events.length; i++) {
			int event = events[i].type;
			if (event == B && lastAPos >= 0) {
				long interval = events[i].timestamp - events[lastAPos].timestamp;
				maxInterval = maxInterval < interval ? interval: maxInterval;
				lastAPos = -1;
			}
			
			if (event == A && lastAPos == -1) {
				lastAPos = i;
			}			
		}
		return maxInterval;
	}
	
	protected long getMaxTimeIntervalBetween(int A, int B) {
		return Math.max(getMaxTimeIntervalFromTo(A,B), getMaxTimeIntervalFromTo(B, A));
	}
	
	protected long getMaxIntervalBetween(int targetEventType) {
		long maxInterval = 0;
		long lastTargetEventTimestamp = -1;
		for (int i=0; i<events.length; i++) {
			TemporalItem event = events[i];
			if (event.type == targetEventType) {
				if (lastTargetEventTimestamp == -1) {
					lastTargetEventTimestamp = event.timestamp;
				}
				else {
					long interval = event.timestamp - lastTargetEventTimestamp;
					maxInterval = maxInterval < interval ? interval : maxInterval;
					lastTargetEventTimestamp = event.timestamp;
				}
			}
		}
		return maxInterval;
	}
	
	/**
	 * Return the timestamp array for target event type
	 * 	nextArr[i]
	 * @param targetEventType
	 * @return
	 */
	protected long[] createEventTimestamps(int targetEventType) {
		int N = events.length;
		int numTargetEvents = getNumEvent(targetEventType);
		long[] timestamps = new long[numTargetEvents];
		int index = 0;		
		for (int i=0; i<N; i++) {
			if (events[i].type == targetEventType) {
				timestamps[index] = events[i].timestamp;
				index++;
			}
		}
		return timestamps;
	}
	
	
	
	
	/**
	 * 
	 * @param A
	 * @param B
	 * @return
	 */
	public abstract Collection<TemporalDependency> find(int A, int B, int topK, long maxInterval) throws InterruptedException;
	
	
	/**
	 * TemporalPatternComparator
	 * @author Liang
	 *
	 */
	public static class TemporalPatternComparator implements Comparator<TemporalDependency> {

		@Override
		public int compare(TemporalDependency o1, TemporalDependency o2) {
			// TODO Auto-generated method stub
			if (o1.getScore() > o2.getScore())  {
				return 1;
			}
			else if (o1.getScore() < o2.getScore()) {
				return -1;
			}
			else {
				return 0;
			}
		}
		
	}

}

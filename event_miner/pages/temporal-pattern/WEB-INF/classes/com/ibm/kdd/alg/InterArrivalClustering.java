package com.ibm.kdd.alg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.PriorityQueue;

import com.ibm.kdd.core.TemporalItem;
import com.ibm.kdd.core.TemporalDependency;
import com.ibm.kdd.core.SortedCountMap;

public class InterArrivalClustering extends TemporalDependencyMiner {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	long[] deltas = null;
	
	double minChisquare;
	
	double minSupport;
	
	long peakMemCost = 0;

	public InterArrivalClustering(TemporalItem[] events, double minChisquare, double minSupport, long[] deltas) {
		super(events);
		// TODO Auto-generated constructor stub
		this.deltas = deltas;
		this.minChisquare = minChisquare;
		this.minSupport = minSupport;
	}

	@Override
	public long getPeakMemCost() {
		// TODO Auto-generated method stub
		return peakMemCost;
	}

	@Override
	public Collection<TemporalDependency> find(int A, int B, int topK, long maxInterval) throws InterruptedException {
		// TODO Auto-generated method stub
		long[] A_timestamps = timestamps[A];
		long[] B_timestamps = timestamps[B];
		int N = events.length;
		int N_A = A_timestamps.length;
		int N_B = B_timestamps.length;
		int minSupCount = (int) (N * minSupport);
		if (N_A < minSupCount || N_B < minSupCount) {
			return new ArrayList<TemporalDependency>(1);
		}
		double T_B = B_timestamps[N_B - 1] - B_timestamps[0];
		double uP_B = ((double)N_B) / T_B;
		double w_B = this.minSamplePeriods[B];
		
		PriorityQueue<TemporalDependency> topPatterns = new PriorityQueue<TemporalDependency>(topK,  new TemporalPatternComparator());
		for (long delta : deltas) {
			SortedCountMap<Long> intervals = new SortedCountMap<Long>();
			long lastAt = -1;
			for (int i=0; i<events.length; i++) {
				TemporalItem event = events[i];
				if (event.type == A) {
					lastAt = event.timestamp;
				}
				else if (event.type == B && lastAt >= 0) {
					long interval = event.timestamp - lastAt;
					boolean bAbsorbed = false;
					for (long t : intervals.keySet()) {
						if (Math.abs(interval - t) <= delta) {
							intervals.add(t);
							bAbsorbed = true;
							break;
						}
					}
					if (!bAbsorbed) {
						intervals.add(interval);
						this.peakMemCost = Math.max(intervals.size(), this.peakMemCost);
						if (Thread.interrupted()) {
							throw new InterruptedException();
						} else {
							Thread.yield();
						}
					}
					lastAt = -1;
				}
			}
			for (long t : intervals.keySet()) {
				int count = intervals.getCount(t);
				if (count < minSupCount*0.5) {
					continue;
				}
				long t1 = t-delta;
				long t2 = t+delta;
				if (t1 < 0) {
					t1 = 0;
				}
				double intervalLen = t2 - t1 + w_B;
				double P_B = Math.min(uP_B * intervalLen, 1.0);
				double diff = count - N_A * P_B;
				double chiSquare = diff * diff / (N_A * P_B * (1 - P_B));
				if (diff > 0) {
					TemporalDependency p = new TemporalDependency(A, B, t1, t2);
					p.setSupportCountA(count);
					p.setSupportCountB(count);
					p.setScore(chiSquare);
					if (p.getT1() > 0 || p.getA() != p.getB()) {
						boolean hasOverlap = false;
						for (TemporalDependency ip: topPatterns) {
							if (p.hasOverlap(ip)) {
								if (p.getScore() > ip.getScore()) {
									topPatterns.remove(ip);
									topPatterns.add(p);
								}
								hasOverlap = true;
								break;
							}
						}
						if (!hasOverlap) {
							topPatterns.add(p);
							if (topK > 0 && topPatterns.size() > topK) {
								topPatterns.poll();
							}
						}
					}
				}
				
				if (Thread.interrupted()) {
					throw new InterruptedException();
				} else {
					Thread.yield();
				}
			}
		}
		
		return topPatterns;
	}

}

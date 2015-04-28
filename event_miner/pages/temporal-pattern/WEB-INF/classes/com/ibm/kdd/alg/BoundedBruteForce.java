package com.ibm.kdd.alg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.SortedSet;
import java.util.TreeSet;

import com.ibm.kdd.core.TemporalItem;
import com.ibm.kdd.core.TemporalDependency;
//import emkit.temporal.item.dependency.unsupervised.miner.TemporalDependencyMiner;

public class BoundedBruteForce extends TemporalDependencyMiner {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	double minChisquare = 0;
	
	double minSupport = 0;
	
	long peakMemCost;

	public BoundedBruteForce(TemporalItem[] events, double minChisquare, double minSupport) {
		super(events);
		// TODO Auto-generated constructor stub
		this.minChisquare = minChisquare;
		this.minSupport = minSupport;
		this.peakMemCost = 0;
	}
	
	@Override
	public long getPeakMemCost() {
		// TODO Auto-generated method stub
		return peakMemCost;
	}

	@Override
	public Collection<TemporalDependency> find(int A, int B, int topK, long maxInterval)
			throws InterruptedException {
		// TODO Auto-generated method stub
		SortedSet<Long> tSet = new TreeSet<Long>();
		long[] A_timestamps = timestamps[A];
		long[] B_timestamps = timestamps[B];
		int N_A = A_timestamps.length;
		int N_B = B_timestamps.length;
		int N = events.length;
		int minSupCount = (int)(N*minSupport);
		double w_B = this.minSamplePeriods[B];
		double T_B = B_timestamps[N_B - 1] - B_timestamps[0];
		double intervalLen_max = T_B / N_B * ((double) N_A) / (minChisquare + N_A);
		if (N_A < minSupCount || N_B < minSupCount) {
			return new ArrayList<TemporalDependency>(1);
		}
		
		if (maxInterval > 0) {
			for (long A_t: A_timestamps) {
				for (long B_t: B_timestamps) {
					if (B_t >= A_t) {
						if (B_t > A_t + maxInterval) {
							break;
						}
						tSet.add(B_t - A_t);
					}
				}
				if (Thread.interrupted()) {
					throw new InterruptedException();
				}
				else {
					Thread.yield();
				}
			}			
		}
		else {
			for (long A_t: A_timestamps) {
				for (long B_t: B_timestamps) {
					if (B_t >= A_t) {
						tSet.add(B_t - A_t);
					}
				}
				if (Thread.interrupted()) {
					throw new InterruptedException();
				}
				else {
					Thread.yield();
				}
			}
		}
		long[] tCandidates = new long[tSet.size()];
		int tIndex=0;
		for (Long t: tSet) {
			tCandidates[tIndex] = t;
			tIndex++;
		}
		peakMemCost = Math.max(peakMemCost, tCandidates.length);
		
		PriorityQueue<TemporalDependency> topPatterns = new PriorityQueue<TemporalDependency>(topK+1, new TemporalPatternComparator());
		for (int i=0; i<tCandidates.length; i++) {
			for (int j=i+1; j<tCandidates.length; j++) {
				long t1 = tCandidates[i];
				long t2 = tCandidates[j];
				if (t2 - t1 >= intervalLen_max) {
					break;
				}
				TemporalDependency p = new TemporalDependency(A, B, t1, t2);
				BruteForce.count(p, timestamps, w_B, events);
				if (p.getScore() > minChisquare && p.getSupportCountA() > minSupCount
						&& p.getSupportCountB() > minSupCount) {
					if (t1 > 0 || A != B) {
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
				}else {
					Thread.yield();
				}
			}
		}
		
		return topPatterns;
	}

}

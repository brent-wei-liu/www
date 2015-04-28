package com.ibm.kdd.alg;

import java.util.Collection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.ibm.kdd.core.TemporalItem;
import com.ibm.kdd.core.PatternConstraint;
import com.ibm.kdd.core.TemporalDependency;
import com.ibm.kdd.alg.TemporalDependencyMiner.TemporalPatternComparator;

/**
 * 
 * BSD License
 * 
 * @author Liang TANG
 *
 */
public class BruteForce extends TemporalDependencyMiner {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	double minChisquare = 0;
	
	double minSupport = 0;
	
	long peakMemCost;

	public BruteForce(TemporalItem[] events, double minChisquare, double minSupport) {
		super(events);
		// TODO Auto-generated constructor stub
		this.minChisquare = minChisquare;
		this.minSupport = minSupport;
		this.peakMemCost = 0;
	}

	@Override
	public Collection<TemporalDependency> find(int A, int B, int topK, long maxInterval) throws InterruptedException {
		// TODO Auto-generated method stub
		SortedSet<Long> tSet = new TreeSet<Long>();
		long[] A_timestamps = timestamps[A];
		long[] B_timestamps = timestamps[B];
		int N_A = A_timestamps.length;
		int N_B = B_timestamps.length;
		int N = events.length;
		int minSupCount = (int)(N*minSupport);
		double w_B = this.minSamplePeriods[B];
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
				TemporalDependency p = new TemporalDependency(A, B, t1, t2);
				count(p, timestamps, w_B, events);
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
	
	public static void count(TemporalDependency p, long[][] timestamps, double w_B, TemporalItem[] events) {
		long[] A_timestamps = timestamps[p.getA()];
		long[] B_timestamps = timestamps[p.getB()];
		long t1 = p.getT1();
		long t2 = p.getT2();
		int N_AB = 0;
		Set<Integer> coveredBSet = new HashSet<Integer>();
		for (long A_t : A_timestamps) {
			boolean hasB = false;
			for (int BIndex = 0; BIndex< B_timestamps.length; BIndex++) {
				long B_t = B_timestamps[BIndex];
				if (B_t >= A_t+t1 && B_t < A_t+t2) {
					hasB = true;
					coveredBSet.add(BIndex);
					break;
				}
			}
			if (hasB) {
				N_AB++;
			}
		}
		p.setSupportCountA(N_AB);
		p.setSupportCountB(coveredBSet.size());
		int N_A = A_timestamps.length;
		int N_B = B_timestamps.length;
		int N = events.length;
		double T_B = events[N-1].timestamp - events[0].timestamp;
		double uP_B = N_B / T_B;
		double P_B = uP_B * (t2-t1+w_B);
		if (P_B > 1.0) {
			P_B = 1.0;
		}
		double diff = N_AB - N_A*P_B;
		double chiSquare = diff*diff/(N_A*P_B*(1-P_B));
		if (diff < 0) {
			p.setScore(0);
		}
		else {
			p.setScore(chiSquare);
		}
	}

	@Override
	public long getPeakMemCost() {
		// TODO Auto-generated method stub
		return peakMemCost;
	}

}

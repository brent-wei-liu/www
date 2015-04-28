package com.ibm.kdd.alg;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.ibm.kdd.core.TemporalItem;
import com.ibm.kdd.core.TemporalDependency;

/**
 * 
 * @author Liang TANG
 *
 */
public class DependencyInfoCounter {
	
	protected static int getNumEvent(TemporalItem[] events, int eventType) {
		int num = 0;
		for (TemporalItem e : events) {
			if (e.type == eventType) {
				num++;
			}
		}
		return num;
	}
	
	protected static int hasEvent(TemporalItem[] events, long start, long end,
			int eventType) {
		for (int i = 0; i < events.length; i++) {
			TemporalItem event = events[i];
			if (event.type == eventType && event.timestamp >= start
					&& event.timestamp <= end) {
				return i;
			}
		}
		return -1;
	}
	
	protected static int hasEvent(TemporalItem[] events, long start, long end, int eventType, 
				boolean[] visitMarkers) {
		for (int i=0; i<events.length; i++) {
			TemporalItem event = events[i];
			if (event.type == eventType && event.timestamp >= start && event.timestamp <= end 
					&& visitMarkers[i] == false ) {
				return i;
			}
		}
		return -1;
	}
	
	public static void count(TemporalDependency p, TemporalItem[] events) {
		int A = p.getA();
		int B = p.getB();
		int N = events.length;
		int N_A = getNumEvent(events, A);
		int N_B = getNumEvent(events, B);
		int N_nA = N - N_A;
		int N_AB = 0;
		int N_nAB = 0;
		long t1 = p.getT1();
		long t2 = p.getT2();
		Set<Integer> coveredBSet = new HashSet<Integer>();
		for (int i=0; i<N; i++) {
			if (events[i].type == A) {
				int B_index = hasEvent(events, events[i].timestamp+t1, events[i].timestamp+t2, B);
				if (B_index >= 0) {
					N_AB++;
					coveredBSet.add(B_index);
				}
			}
			else {
				int B_index = hasEvent(events, events[i].timestamp+t1, events[i].timestamp+t2, B);
				if (B_index >= 0) {
					N_nAB++;
				}
			}
		}
		double phi = TemporalDependencyMiner.getPhi(N_AB, N_A-N_AB, N_nAB, N_nA-N_nAB);
		p.setPhi(phi);
		p.setN_AB(N_AB);
		p.setN_AnB(N_A-N_AB);
		p.setN_nAB(N_nAB);
		p.setN_nAnB(N_nA-N_nAB);
		p.setSupportCountB(coveredBSet.size());
		double T_B = events[N-1].timestamp - events[0].timestamp;
		double uP_B = N_B / T_B;
		double P_B = uP_B * (t2-t1);
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
	
	public static double computeCoverage(TemporalDependency[] testPatterns, TemporalDependency[] truePatterns, TemporalItem[] events) {
		int N = events.length;
		int nTotalDependentPairs = 0;
		int nCoveredDependentPairs = 0;
		for (int i=0; i<N; i++) {
			TemporalItem event = events[i];
			for (int pIndex=0; pIndex<truePatterns.length; pIndex++) {
				TemporalDependency p = truePatterns[pIndex];
				int A = p.getA();
				int B = p.getB();
				long t1 = p.getT1();
				long t2 = p.getT2();
				if (event.type == A) {
					int B_index = hasEvent(events, events[i].timestamp+t1, events[i].timestamp+t2, B);
					if (B_index >= 0) {
						nTotalDependentPairs++;
						for (TemporalDependency testP: testPatterns) {
							if (cover(testP, event, events[B_index])) {
								nCoveredDependentPairs++;
								break;
							}
						}
					}
				}
			}
		}
		return ((double)nCoveredDependentPairs)/ ((double)nTotalDependentPairs);
	}
	
	private static boolean cover(TemporalDependency p, TemporalItem A, TemporalItem B) {
		if (p.getA() == A.type && p.getB() == B.type && 
				A.timestamp+p.getT1() <= B.timestamp &&
				A.timestamp+p.getT2() >= B.timestamp ) {
			return true;
		}
		else {
			return false;
		}
	}
	
	

}

package com.ibm.kdd.alg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import com.ibm.kdd.alg.TemporalDependencyMiner.TemporalPatternComparator;
import com.ibm.kdd.core.EventAccount;
import com.ibm.kdd.core.TemporalDependency;
import com.ibm.kdd.core.TemporalItem;

/**
 * This algorithm consider the account information to caculate the support.
 * */

public class NewSTScan extends STScan {

	private static final long serialVersionUID = 1L;

	private EventAccount eventAccount = null;

	public NewSTScan(TemporalItem[] events, EventAccount ea, double minChisquare, double minSupport) {
		super(events, minChisquare, minSupport);
		eventAccount = ea;
	}

	public NewSTScan(TemporalItem[] events, EventAccount ea, double minChisquare, double minSupport, int filter,
			boolean bUseIncrInvTable) {
		super(events, minChisquare, minSupport, filter, bUseIncrInvTable);
		eventAccount = ea;
	}

	public NewSTScan(TemporalItem[] events, EventAccount ea, double minChisquare, double minSupport,
			boolean bUseIncrInvTable) {
		super(events, minChisquare, minSupport, bUseIncrInvTable);
		eventAccount = ea;
	}

	@Override
	public Collection<TemporalDependency> find(int A, int B, int topK, long maxInterval) throws InterruptedException {
		// TODO Auto-generated method stub
		long[] A_timestamps = timestamps[A];
		long[] B_timestamps = timestamps[B];
		
		IntervalTable intervalTable =  null;
		if (bUseIncrInvTable) {
			intervalTable = new IncrementalIntervalTable(A_timestamps, B_timestamps, maxInterval);
		}
		else {
			intervalTable = new StaticIntervalTable(A_timestamps, B_timestamps, maxInterval);
		}
		
		int N = events.length;
		int N_A = A_timestamps.length;
		int N_B = B_timestamps.length;
		int minSupCount = (int) (N * minSupport);
		int totalAccountA = eventAccount.getNumEventOfAccount(A);
		int minSupCountA = (int)(totalAccountA*minSupport);
		int totalAccountB = eventAccount.getNumEventOfAccount(B);
		int minSupCountB = (int)(totalAccountB*minSupport);
		if (N_A < minSupCountA || N_B < minSupCountB) {
			return new ArrayList<TemporalDependency>(1);
		}

		// Create interval table
		// DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		// System.out.println(dateFormat.format(new Date()) + " creating interval table for " + A + " -> " + B);

		// Compute threshold C
		double T_B = B_timestamps[N_B - 1] - B_timestamps[0];
		double uP_B = N_B / T_B;
		double w_B = this.minSamplePeriods[B];
		double intervalLen_max = T_B / N_B * ((double) N_A) / (minChisquare + N_A);
	
		// Scan intervals
		// System.out.println(dateFormat.format(new Date()) + " Scan intervals "+ A + " -> " + B);
		PriorityQueue<TemporalDependency> topPatterns = new PriorityQueue<TemporalDependency>(topK+1, 
				new TemporalPatternComparator());
		if(filter==1 && A==B)
			return topPatterns;
		long t1 = 0;
		
		// 0,1,1,0,0,1,1,0,1,0
		BufferedIntervalTable bufTab = new BufferedIntervalTable(intervalTable);
		
		while(bufTab.hasNext(0)) {
			t1 = bufTab.getNextInterval(0);
			Set<Integer> cumAIndice = new HashSet<Integer>();
			Set<Integer> cumBIndice = new HashSet<Integer>();
			for (int iEndIdx = 0; bufTab.hasNext(iEndIdx); iEndIdx++) {	
				long t2 = bufTab.getNextInterval(iEndIdx);
				double intervalLen = ((double)(t2 - t1)) + w_B;
				if (intervalLen > intervalLen_max) {
					break;
				}
				cumAIndice.addAll(bufTab.getNextAIndice(iEndIdx));
				cumBIndice.addAll(bufTab.getNextBIndice(iEndIdx));
				int supportA = cumAIndice.size();
				int supportB = cumBIndice.size();
				// Check support
				if (supportA <= minSupCountA || supportB <= minSupCountB) {
					continue;
				}
				// Compute actual chi-square
				double P_B = Math.min(uP_B * intervalLen, 1.0);
				double diff = supportA - N_A * P_B;
				double chiSquare = diff * diff / (N_A * P_B * (1 - P_B));
				if (Double.isInfinite(chiSquare) || Double.isNaN(chiSquare)) {
					System.out.println("break");
				}
				if (diff > 0 && chiSquare > minChisquare) {
					TemporalDependency p = new TemporalDependency(A, B, t1, t2);
					p.setSupportCountA(supportA);
					p.setSupportCountB(supportB);
					p.setN_totalEvent(events.length);
					p.setN_AAccountEvent(totalAccountA);
					p.setN_BAccountEvent(totalAccountB);
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
			}
			peakMemCost = Math.max(peakMemCost, bufTab.getMemoryCost());
			//move to next one
			bufTab.moreForward();

			if (Thread.interrupted()) {
				throw new InterruptedException();
			} else {
				Thread.yield();
			}
		}
//		System.out.println("topK="+topK);
		return topPatterns;

	}
}

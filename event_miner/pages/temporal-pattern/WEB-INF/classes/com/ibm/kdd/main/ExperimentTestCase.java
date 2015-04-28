package com.ibm.kdd.main;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ibm.kdd.core.EventAccount;
import com.ibm.kdd.core.TemporalItem;
import com.ibm.kdd.core.TemporalItemSimpleFileTool;
import com.ibm.kdd.core.PatternConstraint;
import com.ibm.kdd.core.TemporalDependency;
import com.ibm.kdd.alg.NewSTScan;
import com.ibm.kdd.alg.TemporalDependencyMineable;
import com.ibm.kdd.alg.BoundedBruteForce;
import com.ibm.kdd.alg.BruteForce;
import com.ibm.kdd.alg.InterArrivalClustering;
import com.ibm.kdd.alg.STScan;

/**
 * 
 * BSD License
 * 
 * @author Liang TANG
 * 
 */
public class ExperimentTestCase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String algorithmName;

	private String eventFileName;

	private int topK = 10;

	private double minChisquare = 10.83;

	private double minSupport = 0.1;

	private long maxInterval = 0;

	public static long maxTimeInSec = 60 * 60 * 4; // 4 hours for each case
	
	

	public ExperimentTestCase(String algorithmName, String eventFileName,
			int topK, double minChisquare, double minSupport, long maxInterval) {
		this.topK = topK;
		this.algorithmName = algorithmName;
		this.eventFileName = eventFileName;
		this.minChisquare = minChisquare;
		this.minSupport = minSupport;
		this.maxInterval = maxInterval;
	}
	
	public List<TemporalDependency> run(int filter,TemporalItem[] events, EventAccount ea) {
		// TODO Auto-generated method stub
		try {
			TemporalDependencyMineable miner = null;
			if (algorithmName.equalsIgnoreCase("STScan")) {
				miner = new STScan(events, minChisquare, minSupport,filter, false);
			} else if (algorithmName.equalsIgnoreCase("BoundedBruteForce")) {
				miner = new BoundedBruteForce(events, minChisquare, minSupport);
			} else if (algorithmName.equalsIgnoreCase("STScan_IncrTable")) {
				miner = new STScan(events, minChisquare, minSupport, true);
			} else if (algorithmName.equalsIgnoreCase("BruteForce")) {
				miner = new BruteForce(events, minChisquare, minSupport);
			} else if (algorithmName.equalsIgnoreCase("InterArrivalClustering")) {
				miner = new InterArrivalClustering(events, minChisquare,
						minSupport, new long[] { 1, 2, 4, 8, 16, 30, 50, 80,
								100, 200, 400, 800, 1200, 1600, 2000 });
			} else if (algorithmName.equalsIgnoreCase("NewSTScan")) {
				miner = new NewSTScan(events,ea, minChisquare, minSupport,filter, false);;
			}else {
				throw new Error("Unknown algorithm name : " + algorithmName);
			}
			
			List<TemporalDependency> tResult = miner.find(topK, maxInterval);
			return tResult;
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public List<TemporalDependency> run(Map<String, Object> paramPool) {
		// TODO Auto-generated method stub
		try {
			TemporalItem[] events = TemporalItemSimpleFileTool
					.load(eventFileName);
			TemporalDependencyMineable miner = null;
			if (algorithmName.equalsIgnoreCase("STScan")) {
				miner = new STScan(events, minChisquare, minSupport, false);
			} else if (algorithmName.equalsIgnoreCase("BoundedBruteForce")) {
				miner = new BoundedBruteForce(events, minChisquare, minSupport);
			} else if (algorithmName.equalsIgnoreCase("STScan_IncrTable")) {
				miner = new STScan(events, minChisquare, minSupport, true);
			} else if (algorithmName.equalsIgnoreCase("BruteForce")) {
				miner = new BruteForce(events, minChisquare, minSupport);
			} else if (algorithmName.equalsIgnoreCase("InterArrivalClustering")) {
				miner = new InterArrivalClustering(events, minChisquare,
						minSupport, new long[] { 1, 2, 4, 8, 16, 30, 50, 80,
								100, 200, 400, 800, 1200, 1600, 2000 });
			} 
			else {
				throw new Error("Unknown algorithm name : " + algorithmName);
			}
			
			List<TemporalDependency> tResult = miner.find(topK, maxInterval);
			return tResult;
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return eventFileName + "_" + algorithmName;
	}

	public int getTimes() {
		// TODO Auto-generated method stub
		return 1;
	}

	public Object clone() {
		return new ExperimentTestCase(algorithmName, eventFileName, topK,
				minChisquare, minSupport, maxInterval);
	}

}

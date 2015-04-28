package com.ibm.kdd.alg;

import java.util.Collection;

import java.util.List;

import com.ibm.kdd.core.PatternConstraint;
import com.ibm.kdd.core.TemporalDependency;

public interface TemporalDependencyMineable {
	
	 List<TemporalDependency> find(int topK, long maxInterval) throws InterruptedException;
	 
	 long getPeakMemCost();
	 
}

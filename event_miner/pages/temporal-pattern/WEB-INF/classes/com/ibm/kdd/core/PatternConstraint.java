package com.ibm.kdd.core;

import java.io.Serializable;

import com.ibm.kdd.alg.TemporalDependencyMiner;

public interface PatternConstraint extends Serializable{
	
	boolean satisfy(TemporalDependency p, TemporalDependencyMiner miner);

}

package com.ibm.kdd.core;

import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * 
 * BSD License
 * 
 * @author Liang TANG
 *
 */
public class TemporalDependency implements Serializable{
	


	/**
	 *  pattern : A => B in [t1, t2]
	 */
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected int A;
	
	protected int B;
	
	protected long t1;
	
	protected long t2;
	
	protected double score;
	
	private double phi;
	
	private int N_AB;
	
	private int N_AnB;
	
	private int N_nAB;
	
	private int N_nAnB;
	
	private int supportB;
	
	/**caculate the support based on account infomation*/
	private int N_totalEvent;
	
	private int N_AAccountEvent;
	
	private int N_BAccountEvent;
	
	public TemporalDependency(int A, int B) {
		this.A = A;
		this.B = B;
	}
	
	public TemporalDependency(int A, int B, long t1, long t2) {
		this(A,B);
		this.t1 = t1;
		this.t2 = t2;
	}
	
	public TemporalDependency(TemporalDependency copy) {
		this(copy.A, copy.B, copy.t1, copy.t2);
		this.score = copy.score;
		this.phi = copy.phi;
		this.N_AB = copy.N_AB;
		this.N_AnB = copy.N_AnB;
		this.N_nAB = copy.N_nAB;
		this.N_nAnB = copy.N_nAnB;
		this.supportB = copy.supportB;
	}
	

	public int getA() {
		return A;
	}

	public void setA(int a) {
		A = a;
	}

	public int getB() {
		return B;
	}

	public void setB(int b) {
		B = b;
	}

	public long getT1() {
		return t1;
	}

	public void setT1(long t1) {
		this.t1 = t1;
	}

	public long getT2() {
		return t2;
	}

	public void setT2(long t2) {
		this.t2 = t2;
	}
	
	public void setScore(double score) {
		this.score = score;
	}
	
	public double getScore() {
		return this.score;
	}
	
	
	public void setSupportCountA(int supCount) {
		setN_AB(supCount);
	}
	
	public int getSupportCountA() {
		return getN_AB();
	}
	
	public void setSupportCountB(int supCount) {
		this.supportB = supCount;
	}
	
	public int getSupportCountB() {
		return this.supportB;
	}

	public double getPhi() {
		return phi;
	}

	public void setPhi(double phi) {
		this.phi = phi;
	}
	
	public int getN_AB() {
		return N_AB;
	}

	public int getN_AnB() {
		return N_AnB;
	}
	
	public void setN_AB(int n_AB) {
		N_AB = n_AB;
	}

	public void setN_AnB(int n_AnB) {
		N_AnB = n_AnB;
	}

	public int getN_nAB() {
		return N_nAB;
	}

	public void setN_nAB(int n_nAB) {
		N_nAB = n_nAB;
	}

	public int getN_nAnB() {
		return N_nAnB;
	}

	public void setN_nAnB(int n_nAnB) {
		N_nAnB = n_nAnB;
	}
	
	
	/**
	 * @return the n_totalEvent
	 */
	public int getN_totalEvent() {
		return N_totalEvent;
	}

	/**
	 * @param n_totalEvent the n_totalEvent to set
	 */
	public void setN_totalEvent(int n_totalEvent) {
		N_totalEvent = n_totalEvent;
	}

	/**
	 * @return the n_AAccountEvent
	 */
	public int getN_AAccountEvent() {
		return N_AAccountEvent;
	}

	/**
	 * @param n_AAccountEvent the n_AAccountEvent to set
	 */
	public void setN_AAccountEvent(int n_AAccountEvent) {
		N_AAccountEvent = n_AAccountEvent;
	}

	/**
	 * @return the n_BAccountEvent
	 */
	public int getN_BAccountEvent() {
		return N_BAccountEvent;
	}

	/**
	 * @param n_BAccountEvent the n_BAccountEvent to set
	 */
	public void setN_BAccountEvent(int n_BAccountEvent) {
		N_BAccountEvent = n_BAccountEvent;
	}

	public boolean hasOverlap(TemporalDependency other) {
		if (t1 >= other.t1 && t1 <= other.t2) {
			return true;
		}
		if (t2 >= other.t1 && t2 <= other.t2) {
			return true;
		}
		return false;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + A;
		result = prime * result + B;
		result = prime * result + (int) (t1 ^ (t1 >>> 32));
		result = prime * result + (int) (t2 ^ (t2 >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TemporalDependency other = (TemporalDependency) obj;
		if (A != other.A)
			return false;
		if (B != other.B)
			return false;
		if (t1 != other.t1)
			return false;
		if (t2 != other.t2)
			return false;
		return true;
	}

	@Override
	public String toString() {
		DecimalFormat df = new DecimalFormat("0.000");
		String phiStr = df.format(phi);
		String scoreStr = df.format(score);
		return A + " -> " + B + " [" + t1 + ", " + t2 + "],\tchi="+scoreStr+",phi="+phiStr
				+"; supA="+N_AB+","+N_AnB+","+N_nAB+","+N_nAnB+", supB="+supportB;
	}
	
	public String toString(String[] itemLabels) {
		DecimalFormat df = new DecimalFormat("0.000");
		String phiStr = df.format(phi);
		String scoreStr = df.format(score);
		return itemLabels[A] + " -> " + itemLabels[B] + " [" + t1 + ", " + t2 + "],\tchi="+scoreStr+",phi="+phiStr
				+"; supA="+N_AB+","+N_AnB+","+N_nAB+","+N_nAnB+", supB="+supportB;
	}



}

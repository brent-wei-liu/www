package com.ibm.kdd.conf;

import java.io.File;
import java.util.Map;

public class Environment {

	private static String KDD_DATA_HOME = null;

	private static String DATA_FILE = null;

	private static String MAP_FILE = null;
	
	private static String ACC_FILE = null;

	private static String dataFile = null;

	private static String typeMap = null;
	
	private static String accFile = null;

	static {
		KDD_DATA_HOME = System.getenv("KDD_DATA_HOME");
		DATA_FILE = System.getenv("DATA_FILE");
		MAP_FILE = System.getenv("MAP_FILE");
		ACC_FILE= System.getenv("ACC_FILE");
		if (KDD_DATA_HOME == null) {
			KDD_DATA_HOME = "Ddata";
		}
		if (DATA_FILE == null)
			dataFile = KDD_DATA_HOME + File.separator + "event_may.txt";
		else
			dataFile = DATA_FILE;
		if (MAP_FILE == null)
			typeMap = KDD_DATA_HOME + File.separator + "map_may.txt";
		else
			typeMap = MAP_FILE;
		
		if (ACC_FILE == null)
			accFile = KDD_DATA_HOME + File.separator + "acc_may.txt";
		else
			accFile = ACC_FILE;
		
	}

	/**
	 * @return the kDD_DATA_HOME
	 */
	public static String getKDD_DATA_HOME() {
		return KDD_DATA_HOME;
	}

	/**
	 * @param kDD_DATA_HOME
	 *            the kDD_DATA_HOME to set
	 */
	public static void setKDD_DATA_HOME(String kDD_DATA_HOME) {
		KDD_DATA_HOME = kDD_DATA_HOME;
	}

	/**
	 * @return the dataFile
	 */
	public static String getDataFile() {
		return dataFile;
	}

	/**
	 * @param dataFile
	 *            the dataFile to set
	 */
	public static void setDataFile(String dataFile) {
		Environment.dataFile = dataFile;
	}

	/**
	 * @return the typeMap
	 */
	public static String getTypeMap() {
		return typeMap;
	}

	/**
	 * @param typeMap
	 *            the typeMap to set
	 */
	public static void setTypeMap(String typeMap) {
		Environment.typeMap = typeMap;
	}
	

	/**
	 * @return the accFile
	 */
	public static String getAccFile() {
		return accFile;
	}

	/**
	 * @param accFile the accFile to set
	 */
	public static void setAccFile(String accFile) {
		Environment.accFile = accFile;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		System.out.println(dataFile);
		System.out.println(typeMap);
	}

}

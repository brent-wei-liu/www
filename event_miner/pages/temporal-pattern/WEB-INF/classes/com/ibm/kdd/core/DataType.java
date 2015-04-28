package com.ibm.kdd.core;

public enum DataType {
	STR,
	INT,
	FLOAT,
	DATE,
	TIME,
	TIMESTAMP,
	UNKNOWN;
	
	public static DataType getDataType(String typeName) {
		if (typeName.equalsIgnoreCase("string") || typeName.equalsIgnoreCase("str")) {
			return STR;
		}
		else if (typeName.equalsIgnoreCase("integer") || typeName.equalsIgnoreCase("int")) {
			return INT;
		}
		else if (typeName.equalsIgnoreCase("float")) {
			return FLOAT;
		}
		else if (typeName.equalsIgnoreCase("date")) {
			return DATE;
		}
		else if (typeName.equalsIgnoreCase("time")) {
			return TIME;
		}
		else if (typeName.equalsIgnoreCase("timestamp")) {
			return TIMESTAMP;
		}
		else {
			return UNKNOWN;
		}
	}
	
	public static boolean isNumeric(DataType type) {
		if (type == INT || type == FLOAT) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public static boolean isCategorical(DataType type) {
		return !isNumeric(type);
	}
}

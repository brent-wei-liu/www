package com.ibm.kdd.util;

import java.io.FileReader;

import java.io.IOException;


import au.com.bytecode.opencsv.CSVReader;

import com.ibm.kdd.core.Event;
import com.ibm.kdd.core.SparseEvent;


public class CSVEventInputStreamReader implements EventInputStreamReader{
	
	CSVReader csvReader;
	String[] attrNames;
	
	public CSVEventInputStreamReader(String fileName) throws IOException {
		csvReader = new CSVReader(new FileReader(fileName));
		attrNames = csvReader.readNext();
	}
	
	@Override
	public Event readNext() throws IOException {
		String[] row = csvReader.readNext();
		if (row == null) {
			return null;
		}
		SparseEvent event = new SparseEvent();
		for (int i=0; i<attrNames.length; i++) {
			event.setValue(attrNames[i], row[i]);
		}
		return event;
	}
	
	public void close() throws IOException {
		csvReader.close();
	}

}

package com.ibm.kdd.util;

import java.util.Collection;

import com.ibm.kdd.core.Event;

import com.ibm.kdd.core.Events;

public class XMLEventWriter {
	
	public static void write(Events events, String fileName) throws Exception {
		XMLEventOutputStreamWriter output = new XMLEventOutputStreamWriter(fileName);
		for (Event event : events) {
			output.write(event);
		}
		output.close();
	}
	
	public static void write(Collection<Events> eventSets, String fileName) throws Exception {
		XMLEventOutputStreamWriter output = new XMLEventOutputStreamWriter(fileName);
		for (Events events : eventSets) {
			output.write(events);
		}
		output.close();
	}

}

package com.ibm.kdd.util;

import com.ibm.kdd.core.Event;

public interface EventOutputStreamWriter {
	
	public void write(Event event) throws Exception;
}

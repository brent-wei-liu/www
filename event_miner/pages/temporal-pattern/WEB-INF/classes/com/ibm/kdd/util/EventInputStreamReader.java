package com.ibm.kdd.util;


import com.ibm.kdd.core.Event;

public interface EventInputStreamReader {
	
	Event readNext() throws Exception;
}

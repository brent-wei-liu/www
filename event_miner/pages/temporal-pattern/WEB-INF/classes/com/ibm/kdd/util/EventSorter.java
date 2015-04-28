package com.ibm.kdd.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.ibm.kdd.core.Event;
import com.ibm.kdd.core.Events;

public class EventSorter {
	
	public static List<Event> sortAsList(Events events, final String timestampAttrName) {
		List<Event> eventList = new ArrayList<Event>(events.getNumEvents());
		for (Event event : events) {
			eventList.add(event);
		}
		return sortAsList(eventList, timestampAttrName);
	}
	
	public static List<Event> sortAsList(List<Event> eventList, final String timestampAttrName) {
		Collections.sort(eventList, new Comparator<Event>() {
			@Override
			public int compare(Event o1, Event o2) {
				// TODO Auto-generated method stub
				long t1 = Long.parseLong(o1.getValue(timestampAttrName));
				long t2 = Long.parseLong(o2.getValue(timestampAttrName));
				if (t1 > t2) {
					return 1;
				}
				else if (t2 > t1) {
					return -1;
				}
				else {
					return 0;
				}
			}			
		});
		return eventList;
	}

}

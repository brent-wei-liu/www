package com.ibm.kdd.core;

import java.io.BufferedWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import com.ibm.kdd.core.Event;
import com.ibm.kdd.core.Events;
import com.ibm.kdd.util.EventInputStreamReader;

public class TemporalItemConverter {
	
	Events events = null;
	Iterator<Event> eventIterator = null;
	String[] attrNames = null;
	String weightAttrName = null;
	String timestampAttrName = null;
	SimpleDateFormat timestampFormat = null;
	List<EventTypeInfo> eventTypeInfos = null;
	EventInputStreamReader streamReader = null;
	int maxConvertSize = -1;
	
	public final static int OPTION_DEFAULT = 0x0;
	
	public final static int OPTION_SORT = 0x1;
	
	public final static int OPTION_IGNORE_NOATTR_EVENT = 0x2;
	
	public TemporalItemConverter(Events events, String attrName, String timestampAttrName) {
		this(events, new String[]{attrName}, timestampAttrName,  null);
	}
	
	public TemporalItemConverter(Events events, String attrName, String timestampAttrName, String weightAttrName) {
		this(events, new String[]{attrName}, timestampAttrName, weightAttrName);
	}
	
	public TemporalItemConverter(Events events, String[] attrNames, String timestampAttrName, String weightAttrName) {
		this.events = events;
		this.attrNames = attrNames;
		this.timestampAttrName = timestampAttrName;
		this.weightAttrName = weightAttrName;
	}
	
	public TemporalItemConverter(EventInputStreamReader streamReader, String attrName, String timestampAttrName) {
		this(streamReader, new String[]{attrName}, timestampAttrName, null);
	}
	
	public TemporalItemConverter(EventInputStreamReader streamReader, String attrName, String timestampAttrName, String weightAttrName) {
		this(streamReader, new String[]{attrName}, timestampAttrName, weightAttrName);
	}
	
	public TemporalItemConverter(EventInputStreamReader streamReader, String[] attrNames, String timestampAttrName,  String weightAttrName) {
		this.streamReader = streamReader;
		this.attrNames = attrNames;
		this.timestampAttrName = timestampAttrName;
		this.weightAttrName = weightAttrName;
	}
	
	public void setTimestampFormat(String timestampFormat) {
		this.timestampFormat = new SimpleDateFormat(timestampFormat);
	}
	
	public void setMaxNumEvents(int maxSize) {
		this.maxConvertSize = maxSize;
	}
	
	public TemporalItem[] convert(int option) throws Exception {
		boolean bIgnoreBadEvent = (option & OPTION_IGNORE_NOATTR_EVENT) > 0;
		List<TemporalItem> tEventList = new ArrayList<TemporalItem>();
		Map[] attrValueMaps = new Map[attrNames.length];
		eventTypeInfos = new ArrayList<EventTypeInfo>();
		int eventTypeCounter = 0;
		for (int attrIndex=0; attrIndex<attrNames.length; attrIndex++) {
			attrValueMaps[attrIndex] = new HashMap<String, Integer>();
		}
		
		if (this.events != null) {
			this.eventIterator = this.events.iterator();
		}
		
		Event event = null;
		int numEvents = 0;
		while ((event = getNextEvent()) != null) {
			// Get timestamp value
			long timestamp = -1;
			double weight = 0;
			if (event.getValue(timestampAttrName) == null) {
				continue;
			}
			if (timestampFormat != null) {
				timestamp = timestampFormat.parse(event.getValue(timestampAttrName)).getTime()/1000;
			}
			else {
				try{
					timestamp  = Long.parseLong(event.getValue(timestampAttrName));
				}catch(NumberFormatException e) {
					if (bIgnoreBadEvent) {
						continue;
					}
					else {
						e.printStackTrace();
					}
				}
			}
			// Get event weight
			if (weightAttrName != null) {
				String value = event.getValue(weightAttrName);
				if (value == null) {
					if (bIgnoreBadEvent) {
						break;
					} else {
						throw new Error("event : " + event.toString() + " has no attribute : " + weightAttrName);
					}
				}
				weight = Double.parseDouble(value);
			}
			// Get each event type
			for (int attrIndex=0; attrIndex<attrNames.length; attrIndex++) {
				String attrName = attrNames[attrIndex];
				Map<String, Integer> valueMap = attrValueMaps[attrIndex];
				String value = event.getValue(attrName);
				if (value == null) {
					if (bIgnoreBadEvent) {
						break;
					}
					else {
						throw new Error("event : "+event.toString()+ " has no attribute : "+attrName);
					}
				}
				Integer eventType = valueMap.get(value);
				if (eventType == null) {
					eventType = eventTypeCounter;
					eventTypeCounter++;
					valueMap.put(value, eventType);
					eventTypeInfos.add(new EventTypeInfo(attrName, value));
				}
				tEventList.add(new TemporalItem(eventType, timestamp, weight));
			}
			
			numEvents++;
			if (this.maxConvertSize > 0 && numEvents >= this.maxConvertSize) {
				break;
			}
		}
		TemporalItem[] tEvents = new TemporalItem[tEventList.size()];
		tEventList.toArray(tEvents);
		
		if ((option & OPTION_SORT) > 0) {
			Arrays.sort(tEvents, new TemporalEventComparator());
		}
		return tEvents;
	}
	
	private Event getNextEvent() throws Exception {
		if (this.eventIterator != null) {
			return this.eventIterator.next();
		}
		else if (streamReader != null) {
			return streamReader.readNext();
		}
		else {
			return null;
		}
	}
	
	public EventTypeInfo lookupEventType(int eventType) {
		if (eventType >= eventTypeInfos.size() || eventType < 0) {
			return null;
		}
		else {
			return eventTypeInfos.get(eventType);
		}
	}
	
	public void saveEventTypeInfos(String fileName) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
		for (int i=0; i<eventTypeInfos.size(); i++) {
			writer.write(i+" : "+eventTypeInfos.get(i).toString());
			writer.write("\n");
		}
		writer.close();
	}
	

	
	/**
	 * 
	 * @author ltang002
	 *
	 */
	public static class EventTypeInfo {
		public String attrName;
		public String attrValue;
		
		public EventTypeInfo(String attrName, String attrValue) {
			this.attrName = attrName;
			this.attrValue = attrValue;
		}

		@Override
		public String toString() {
			return attrName + " = "+ attrValue;
		}
				
	}
	
	/**
	 * 
	 * @author ltang002
	 *
	 */
	private static class TemporalEventComparator implements Comparator<TemporalItem> {

		@Override
		public int compare(TemporalItem o1, TemporalItem o2) {
			// TODO Auto-generated method stub
			if (o1.timestamp > o2.timestamp) {
				return 1;
			}
			else if (o1.timestamp < o2.timestamp) {
				return -1;
			}
			else {
				return 0;
			}
		}
		
	}
	
	
	
}

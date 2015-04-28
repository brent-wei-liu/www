package com.ibm.kdd.core;

import java.util.Iterator;

public class EventIterator implements Iterator<Event>  {
	
	int eventIndex = 0;
	
	Events events = null;
	
	public EventIterator(Events events) {
		this.eventIndex = 0;
		this.events = events;
	}

	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return eventIndex < events.getNumEvents();
	}

	@Override
	public Event next() {
		// TODO Auto-generated method stub
		if (eventIndex >= events.getNumEvents()) {
			return null;
		}
		Event event = events.getEvent(eventIndex);
		eventIndex++;
		return event;
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub
		throw new Error("Remve is not supported!");
	}

}

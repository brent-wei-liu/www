package com.ibm.kdd.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.stream.XMLStreamException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;
import org.dom4j.io.SAXReader;

import com.ibm.kdd.core.DataType;
import com.ibm.kdd.core.Event;
import com.ibm.kdd.core.MutableEvents;
import com.ibm.kdd.core.SparseEvents;
import com.ibm.kdd.core.VectorEvents;

public class XMLEventLoader {
	
	public static VectorEvents loadVectorEvents(String xmlFileName) throws DocumentException {
		
		// Read all attribute names in the first pass
		SAXReader reader = new SAXReader();
		EventAttributeReaderHandler attrHandler = new EventAttributeReaderHandler();
		reader.addHandler("/data/event", attrHandler);
		reader.read(xmlFileName);
		Set<String> attrSet = attrHandler.attrSet;
		String[] attrNames = new String[attrSet.size()];
		attrSet.toArray(attrNames);
		DataType[] attrTypes = new DataType[attrNames.length];
		Arrays.fill(attrTypes, DataType.STR);
		
		// Read all events in the second pass
		VectorEvents events = new VectorEvents(attrNames, attrTypes);
		reader = new SAXReader();
		EventReaderHandler eventHandler = new EventReaderHandler(events);
		reader.addHandler("/data/event", eventHandler);
		reader.read(xmlFileName);
		
		return events;
	}
	
	public static SparseEvents loadSparseEvents(String xmlFileName) throws IOException, XMLStreamException, DocumentException  {
		return loadSparseEvents(xmlFileName, 0);
	}
	
	public static SparseEvents loadSparseEvents(String xmlFileName, int numEvents) throws IOException, XMLStreamException, DocumentException  {
		SparseEvents events = new SparseEvents();
		XMLEventInputStreamReader reader = new XMLEventInputStreamReader(xmlFileName);
		Event event = null;
		while((event = reader.readNext()) != null ) {			
			events.addEvent(event);
			if (numEvents > 0 && events.getNumEvents() >= numEvents) {
				break;
			}
		}
		reader.close();
		return events;
	}
	
	static class EventAttributeReaderHandler implements ElementHandler {
		public Set<String> attrSet;

		public EventAttributeReaderHandler() {
			this.attrSet = new HashSet<String>();
		}

		@Override
		public void onEnd(ElementPath path) {
			// TODO Auto-generated method stub
			// process a ROW element
			Element row = path.getCurrent();
			List<Element> attrElems = row.elements();
			for (Element attrElem : attrElems) {
				this.attrSet.add(attrElem.getName());
			}
			row.detach();
		}
		
		@Override
		public void onStart(ElementPath path) {
			// TODO Auto-generated method stub
		}
	}
	
	static class EventReaderHandler implements ElementHandler {
	    public VectorEvents events;

		public EventReaderHandler(VectorEvents events) {
			this.events = events;
		}

		@Override
		public void onEnd(ElementPath path) {
			// TODO Auto-generated method stub
			// process a ROW element
			Element row = path.getCurrent();
			List<Element> attrElems = row.elements();
			Event event = events.createEvent();
			for (Element attrElem : attrElems) {
				event.setValue(attrElem.getName(), attrElem.getTextTrim());
			}
			this.events.addEvent(event);
			row.detach();
		}
		
		@Override
		public void onStart(ElementPath path) {
			// TODO Auto-generated method stub
		}
	}
}

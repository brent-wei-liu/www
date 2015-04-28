package com.ibm.kdd.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TemporalItemSimpleFileTool {

	public static TemporalItem[] load(String fileName) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		String line;
		line = reader.readLine();
		int numEvents = Integer.parseInt(line);
		TemporalItem[] events = new TemporalItem[numEvents];
		for (int i = 0; i < numEvents; i++) {
			line = reader.readLine();
			String[] tokens = line.split(",");
			if (tokens.length >= 3) {
				events[i] = new TemporalItem(Integer.parseInt(tokens[0].trim()), Long.parseLong(tokens[1].trim()),
						Double.parseDouble(tokens[2].trim()));
			} else {
				events[i] = new TemporalItem(Integer.parseInt(tokens[0].trim()), Long.parseLong(tokens[1].trim()));
			}
		}
		reader.close();
		return events;
	}

	public static void save(TemporalItem[] events, String fileName) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
		writer.write(events.length + "\n");
		for (TemporalItem event : events) {
			writer.write(event.type + "," + event.timestamp + "," + event.weight);
			writer.write("\n");
		}
		writer.close();
	}

	public static String[] loadLabels(String fileName) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		String line;
		List<String> labels = new ArrayList<String>();
		while ((line = reader.readLine()) != null) {
			String[] tokens = line.split("\\s");
			String lastToken = tokens[tokens.length - 1].trim();
			labels.add(lastToken);
		}
		reader.close();
		String[] labelArray = new String[labels.size()];
		labels.toArray(labelArray);
		return labelArray;
	}

	public static EventAccount loadAccounts(String fileName) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		String line;
		EventAccount ea = new EventAccount();

		int eventType;
		String account = "";
		while ((line = reader.readLine()) != null) {
			line = line.trim();
			if (line == "")
				continue;
			String[] tokens = line.split("\\s");
			eventType = Integer.parseInt(tokens[0].trim());
			account = tokens[1].trim();
			ea.setAccount(eventType, account);
			ea.setNumEventOfAccount(eventType, 0);
		}
		reader.close();

		return ea;
	}

	public static EventAccount MountAccount(EventAccount ea, TemporalItem[] events) throws IOException {

		for (TemporalItem e : events) {
			ea.incrNumEventOfAccount(e.type);
		}

		return ea;
	}
	

}

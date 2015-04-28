package com.ibm.kdd.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EventAccount {

	private Map<Integer, String> eventAccount = new HashMap<Integer, String>();

	private Map<String, Integer> totalEventByAccount = new HashMap<String, Integer>();

	public EventAccount() {
	}

	public String getAccount(int eventType) {
		return eventAccount.get(eventType);
	}

	public void setAccount(int eventType, String account) {
		eventAccount.put(eventType, account);
	}

	public int getNumEventOfAccount(String account) {
		return totalEventByAccount.get(account);
	}

	public int getNumEventOfAccount(int eventType) {
		String account = eventAccount.get(eventType);
		if (account == null)
			return (Integer) null;
		else
			return getNumEventOfAccount(account);
	}

	public void setNumEventOfAccount(String account, int num) {
		totalEventByAccount.put(account, num);
	}
	
	public void setNumEventOfAccount(int eventType,int num) throws IOException{
		String acc = eventAccount.get(eventType);
		if (acc == null)
			throw new IOException("no account information exists for " + eventType);
		setNumEventOfAccount(acc,num);
	}
	
	public void incrNumEventOfAccount(int eventType) throws IOException {
		String acc = eventAccount.get(eventType);
		if (acc == null)
			throw new IOException("no account information exists for " + eventType);
		incrNumEventOfAccount(acc);

	}

	public void incrNumEventOfAccount(String account) {
		if (!totalEventByAccount.containsKey(account)) {
			totalEventByAccount.put(account, 1);
			return;
		}
		int cnt = totalEventByAccount.get(account);
		totalEventByAccount.put(account, cnt + 1);

	}
}

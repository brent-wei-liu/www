package com.ibm.kdd.core;

import java.util.Arrays;
import java.util.Comparator;

/**
 * 
 * @author Liang TANG
 *
 */
public class TemporalItemSorter {
	
	public static void sortByTimestamp(TemporalItem[] events) {
		Arrays.sort(events, new TimestampComparator());
	}
	
	private static class TimestampComparator implements Comparator<TemporalItem> {

		@Override
		public int compare(TemporalItem o1, TemporalItem o2) {
			// TODO Auto-generated method stub
			if (o1.timestamp > o2.timestamp) {
				return 1;
			}
			else if (o1.timestamp < o2.timestamp) {
				return -1;
			}
			else  {
				return 0;
			}
		}
		
	}

}

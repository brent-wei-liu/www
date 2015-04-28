package com.ibm.kdd.main;



import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;





public class Test {
	
	public static String convertTime(long time){
	    Date date = new Date(time);
	    Format format = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
	    return format.format(date).toString();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long millis = 1287234750000l;
		
		System.out.println(convertTime(millis));
	}

}

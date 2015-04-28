package com.ibm.kdd.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class RemoveEventClass {
	
	public static void remove(String p, String src,String dst) throws IOException{
		BufferedReader srcBR = new BufferedReader(new FileReader(src));
		PrintWriter pw = new PrintWriter(new File(dst));
		String line =null;
		while((line = srcBR.readLine())!=null){
			String tokens[] = line.split(",");
			if(tokens.length<2)
				pw.println(line.trim());
			else if(!tokens[0].trim().equals(p))
				pw.println(line.trim());
		}
		
		srcBR.close();
		pw.close();
	}

	public static void main(String args[]) throws IOException{
		remove("49","data/remove01_may.txt","data/remove0149_may.txt");
	}
}

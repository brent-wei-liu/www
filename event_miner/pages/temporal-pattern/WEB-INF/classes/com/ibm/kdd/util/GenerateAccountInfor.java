package com.ibm.kdd.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import com.ibm.kdd.conf.Environment;
import com.ibm.kdd.core.TemporalItemSimpleFileTool;

public class GenerateAccountInfor {

	public static void generate(String mapFile, String accFile) throws IOException {
		PrintWriter pw = new PrintWriter(new File(accFile));
		String[] labels = TemporalItemSimpleFileTool.loadLabels(mapFile);
		String acc = "";
		for (int i = 0; i < labels.length; i++) {
			pw.print(i);
			pw.print(" ");
			acc = labels[i].split("_")[0].trim();
//			System.out.println(acc);
			if ("mul".equalsIgnoreCase(acc))
				pw.print("mul");
			else
				pw.print("other");
			pw.println();
		}

		pw.close();

	}

	public static void main(String[] args) throws IOException {
		generate(Environment.getTypeMap(), Environment.getAccFile());
	}

}

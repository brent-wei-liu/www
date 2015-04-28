package com.ibm.kdd.main;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.ibm.kdd.alg.STScan;
import com.ibm.kdd.core.TemporalDependency;
import com.ibm.kdd.core.TemporalItem;
import com.ibm.kdd.core.TemporalItemConverter;
import com.ibm.kdd.core.TemporalItemSimpleFileTool;

public class Main {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		if(args.length <6){
			System.out.println("event_file topk minChiSquare minSupport maxInterval algorithm");
			System.exit(1);
		}
		TemporalItemSimpleFileTool tisft = new TemporalItemSimpleFileTool();
		String temporalItemFile =  args[0].trim();
		TemporalItem[] temporalItems = tisft.load(temporalItemFile);
		System.out.println(temporalItems.length);
		int topK = Integer.parseInt(args[1].trim());

		double minChisquare = Double.parseDouble(args[2].trim());

		double minSupport = Double.parseDouble(args[3].trim());

		long maxInterval = Integer.parseInt(args[4].trim());
		
		String alg = args[5].trim();
		
		ExperimentTestCase etc = new ExperimentTestCase(alg,temporalItemFile,topK,minChisquare,minSupport,maxInterval);
		
		List<TemporalDependency> rst = etc.run((Map<String, Object>)null);
		
		for(TemporalDependency t:rst){
			System.out.println(t);
		}

	}

}

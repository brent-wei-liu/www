package com.ibm.kdd.severlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.TreeMap;
import java.util.WeakHashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ibm.kdd.conf.Environment;
import com.ibm.kdd.core.EventAccount;
import com.ibm.kdd.core.TemporalDependency;
import com.ibm.kdd.core.TemporalItem;
import com.ibm.kdd.core.TemporalItemSimpleFileTool;
import com.ibm.kdd.main.ExperimentTestCase;

/**
 * Servlet implementation class MineDependence
 */
@WebServlet("/MineDependence")
public class MineDependence extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MineDependence() {
		super();
		// TODO Auto-generated constructor stub
	}

	public List<TemporalDependency> mine(double minSupp, double minChiS, int topK, int filter, TemporalItem[] data,EventAccount ea) {

		ExperimentTestCase etc = new ExperimentTestCase("NewSTScan", "", topK, minChiS, minSupp, 0);
		List<TemporalDependency> rst = etc.run(filter, data,ea);

		return rst;

	}

	public JsonArray sortedPaged(int pageSize, int startIndex, String sortStr, List<TemporalDependency> result) {
		JsonArray array = new JsonArray();
		String[] arr = sortStr.split("\\s+");
		String field = arr[0].trim().toLowerCase();
		String order = arr[1].trim();
		double help = order.equalsIgnoreCase("asc") ? 1 : -1;
		int size = (result.size() == 0 ? 1 : result.size());

		Comparator<TemporalDependency> cmp = new TemporalDepComp(field, help);
		PriorityQueue<TemporalDependency> queue = new PriorityQueue<TemporalDependency>(size, cmp);

		for (TemporalDependency td : result) {
			queue.add(td);
		}

		int index = 0;
		TemporalDependency r = null;
		while ((r = queue.poll()) != null) {
			if (startIndex <= index && index < (startIndex + pageSize)) {
				JsonObject jObj = new JsonObject();
				jObj.addProperty("Antecedent", r.getA());
				jObj.addProperty("Consequent", r.getB());
				jObj.addProperty("MinInterval", r.getT1());
				jObj.addProperty("MaxInterval", r.getT2());
				jObj.addProperty("ChiSquare", Math.round(r.getScore()*1000.0)/1000.0);
				jObj.addProperty("SuppAntecedent", r.getSupportCountA());
				jObj.addProperty("SuppConsequent", r.getSupportCountB());
				jObj.addProperty("ATotal", r.getN_AAccountEvent());
				jObj.addProperty("BTotal", r.getN_BAccountEvent());
				jObj.addProperty("Total", r.getN_totalEvent());
				array.add(jObj);
			}
			index++;
		}

		return array;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		int jtPageSize = Integer.parseInt(request.getParameter("jtPageSize"));
		int jtStartIndex = Integer.parseInt(request.getParameter("jtStartIndex"));
		String jtSorting = request.getParameter("jtSorting");
		JsonObject json = new JsonObject();
		json.addProperty("Result", "OK");
		JsonArray retArray = new JsonArray();
		double minSupp = 0.1, minChiS = 10.1;
		int topK = 10;
		int filter = 1;
		try {
			minSupp = Double.parseDouble(request.getParameter("minSupp").trim());
			minChiS = Double.parseDouble(request.getParameter("minChiS").trim());
			topK = Integer.parseInt(request.getParameter("topK").trim());
			filter = Integer.parseInt(request.getParameter("filter").trim());
		} catch (Exception ex) {
			json.add("Records", retArray);
			json.addProperty("TotalRecordCount", 0);
			PrintWriter writer = response.getWriter();
			System.out.println(json.toString());
			writer.write(json.toString());
			return;
			// json.addProperty("Message", "Invalid Parameter Detected");
		}
		String key = "" + minSupp + "_" + minChiS + "_" + topK + "_" + filter;
		System.out.println(key);
		String cachedKey = "ResultCached";
		WeakHashMap<String, List<TemporalDependency>> cached = null;
		HttpSession session = request.getSession();
		cached = (WeakHashMap<String, List<TemporalDependency>>) session.getAttribute(cachedKey);
		if (cached == null) {
			cached = new WeakHashMap<String, List<TemporalDependency>>();
			session.setAttribute(cachedKey, cached);
		}
		List<TemporalDependency> result = null;
		if ((result = cached.get(key)) == null) {
			System.out.println("not cached! rerun the algorithm");
			ServletContext context = getServletContext();
			TemporalItem[] temporalItems = (TemporalItem[]) context.getAttribute("temporalItems");
			
			if (temporalItems == null) {
				String temporalItemFile = Environment.getDataFile();
				
				temporalItems = TemporalItemSimpleFileTool.load(temporalItemFile);
				context.setAttribute("temporalItems", temporalItems);
				
			}
			EventAccount eventAccount = (EventAccount) context.getAttribute("eventAccount");
			if(eventAccount == null)
			{
				String accfile = Environment.getAccFile();
				eventAccount = TemporalItemSimpleFileTool.loadAccounts(accfile);
				eventAccount = TemporalItemSimpleFileTool.MountAccount(eventAccount, temporalItems);
				context.setAttribute("eventAccount", eventAccount);
			}

			result = mine(minSupp, minChiS, topK, filter, temporalItems,eventAccount);
			cached.put(key, result);
		}

		retArray = sortedPaged(jtPageSize, jtStartIndex, jtSorting, result);

		json.add("Records", retArray);
		json.addProperty("Result", "OK");
		json.addProperty("TotalRecordCount", result.size());

		PrintWriter writer = response.getWriter();
//		System.out.println(json.toString());
		writer.write(json.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

class TemporalDepComp implements Comparator<TemporalDependency> {

	private String field = "";

	private double order = 1;

	public TemporalDepComp(String f, double o) {
		field = f;
		order = o;
	}

	@Override
	public int compare(TemporalDependency o1, TemporalDependency o2) {
		// TODO Auto-generated method stub
		double v1 = 0.0, v2 = 0.0;
		switch (field) {
		case "antecedent":
			v1 = o1.getA() * order;
			v2 = o2.getA() * order;
			break;
		case "consequent":
			v1 = o1.getB() * order;
			v2 = o2.getB() * order;
			break;
		case "mininterval":
			v1 = o1.getT1() * order;
			v2 = o2.getT1() * order;
			break;
		case "maxinterval":
			v1 = o1.getT2() * order;
			v2 = o2.getT2() * order;
			break;
		case "chisquare":
			v1 = o1.getScore() * order;
			v2 = o2.getScore() * order;
			break;
		case "suppantecedent":
			v1 = o1.getSupportCountA() * order;
			v2 = o2.getSupportCountA() * order;
			break;
		case "suppconsequent":
			v1 = o1.getSupportCountB() * order;
			v2 = o2.getSupportCountB() * order;
			break;
		}
		if (v1 == v2)
			return 0;
		else if (v1 > v2)
			return 1;
		else
			return -1;
	}
}

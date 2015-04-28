package com.ibm.kdd.severlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ibm.kdd.core.TemporalItem;

/**
 * Servlet implementation class MarkDependence
 */
@WebServlet("/MarkDependence")
public class MarkDependence extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MarkDependence() {
		super();
		// TODO Auto-generated constructor stub
	}

	private long[][] mark(int typeA, int typeB, int min, int max, TemporalItem[] temporalItems) {
		TreeSet<Long> setA = new TreeSet<Long>();
		TreeSet<Long> setB = new TreeSet<Long>();
		ArrayList<Long> rstA = new ArrayList<Long>();
		ArrayList<Long> rstB = new ArrayList<Long>();
		for (TemporalItem item : temporalItems) {
			if (item.type == typeA)
				setA.add(item.timestamp);
			if (item.type == typeB)
				setB.add(item.timestamp);
		}
		Iterator<Long> iterA = setA.iterator();
		Iterator<Long> iterB = setB.iterator();
		long a = -1, b = -1;
		while (iterA.hasNext() && iterB.hasNext()) {
			if (a == -1 && b == -1) {
				a = iterA.next();
				b = iterB.next();
			}
			if (b - a < min) {
				b = iterB.next();

			} else if (b - a > max) {
				a = iterA.next();
			} else {
				rstA.add(a);
				rstB.add(b);
				a = iterA.next();
				b = iterB.next();
			}
		}

		int size = rstA.size();
		long[][] rst = new long[size][2];
		for (int i = 0; i < size; i++) {
			rst[i][0] = rstA.get(i);
			rst[i][1] = rstB.get(i);
		}

		return rst;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		int typeA, typeB, min, max;
		typeA = Integer.parseInt(request.getParameter("Antecedent"));
		typeB = Integer.parseInt(request.getParameter("Consequent"));
		min = Integer.parseInt(request.getParameter("MinInterval"));
		max = Integer.parseInt(request.getParameter("MaxInterval"));
		ServletContext context = getServletContext();
		TemporalItem[] temporalItems = (TemporalItem[]) context.getAttribute("temporalItems");
		System.out.println("typeA=" + typeA + ",typeB=" + typeB + ",min=" + min + ",max=" + max);
		long[][] result = mark(typeA, typeB, min, max, temporalItems);
		JsonObject json = new JsonObject();
		JsonArray array = new JsonArray();
		for (int i = 0; i < result.length; i++) {
			JsonObject jso = new JsonObject();
			jso.addProperty("A", result[i][0]);
			jso.addProperty("B", result[i][1]);
			array.add(jso);
		}

		json.addProperty("Result", "OK");
		json.add("Records", array);
		json.addProperty("Num", result.length);
		json.addProperty("A", typeA);
		json.addProperty("B", typeB);

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

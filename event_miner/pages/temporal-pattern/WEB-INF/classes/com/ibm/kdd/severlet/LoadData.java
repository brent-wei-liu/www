package com.ibm.kdd.severlet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ibm.kdd.conf.Environment;
import com.ibm.kdd.core.TemporalItem;
import com.ibm.kdd.core.TemporalItemSimpleFileTool;

/**
 * Servlet implementation class LoadData
 */
@WebServlet("/LoadData")
public class LoadData extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoadData() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		ServletContext context = getServletContext();
		
		TemporalItem[] temporalItems = (TemporalItem[])context.getAttribute("temporalItems");
		if(temporalItems == null){
			String temporalItemFile= Environment.getDataFile();
			temporalItems = TemporalItemSimpleFileTool.load(temporalItemFile);
			context.setAttribute("temporalItems", temporalItems);
		}
		
		JsonArray retArray = new JsonArray();
		Set<Integer> types = new HashSet<Integer>();
		int tp;
		long tm;
		for(TemporalItem item:temporalItems){
			tp=item.type;
			tm = item.timestamp;
			JsonObject itemObj =new JsonObject();
			itemObj.addProperty("tp", tp);
			itemObj.addProperty("tm", tm);
			
			retArray.add(itemObj);
			types.add(item.type);
		}
		
		JsonObject json = new JsonObject();
		
		json.add("Records", retArray);
		json.addProperty("Result", "OK");
		json.addProperty("TypeNum", types.size());
		
		PrintWriter writer = response.getWriter();
//		System.out.println(json.toString());
		writer.write(json.toString());

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request,response);
	}

}

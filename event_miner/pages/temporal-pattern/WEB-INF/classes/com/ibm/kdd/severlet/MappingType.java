package com.ibm.kdd.severlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.ibm.kdd.conf.Environment;
import com.ibm.kdd.core.TemporalItemSimpleFileTool;

/**
 * Servlet implementation class MappingType
 */
@WebServlet("/MappingType")
public class MappingType extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MappingType() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		int uIndex = Integer.parseInt(request.getParameter("uIndex"));

		ServletContext context = getServletContext();
		String[] map = null;
		map = (String []) context.getAttribute("MappingTypes");
		if(map == null){
			String mapfile= Environment.getTypeMap();
			map = TemporalItemSimpleFileTool.loadLabels(mapfile);
			context.setAttribute("MappingTypes", map);
		}
//		JsonObject json = new JsonObject();
//		json.addProperty("Result", "OK");
//		json.addProperty("Type", map[uIndex]);
		
		PrintWriter writer = response.getWriter();
//		System.out.println(map[uIndex]);
		writer.write(map[uIndex]);

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

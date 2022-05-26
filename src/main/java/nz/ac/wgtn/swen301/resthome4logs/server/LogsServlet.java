package nz.ac.wgtn.swen301.resthome4logs.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

public class LogsServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public LogsServlet() {
		
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONArray jsonArray = new JSONArray();
		PrintWriter printWriter = response.getWriter();
		String stringLimit = request.getParameter("limit");
		String stringLevel = request.getParameter("level");
		int limit = Persistency.getDataBase().size();
		if (stringLimit != null) {
			limit = Integer.parseInt(stringLimit);
		}
		int count = 0;
		response.setContentType("application/json");
		for (JSONObject json : Persistency.getDataBase()) {
			if (limit > count) {
				jsonArray.put(json);
			}
			count++;
		}
		printWriter.println(jsonArray.toString(2));
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		BufferedReader reader = request.getReader();
		StringBuilder builder = new StringBuilder();
	    String line;
	    while((line = reader.readLine()) != null){
	      builder.append(line);
	    }
	    JSONObject json = new JSONObject(builder.toString());
	    String id = json.getString("id");
	    String message = json.getString("message");
	    String timestamp = json.getString("timestamp");
	    String thread = json.getString("thread");
	    String logger = json.getString("logger");
	    String level = json.getString("level");
	    String errorDetails = json.getString("errorDetails");
	    LogEvent le = new LogEvent(id, message, timestamp, thread, logger, level, errorDetails);
	    Persistency.addLog(le);
	}
	
	@Override
	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Persistency.removeAll();
	}
	
	
}

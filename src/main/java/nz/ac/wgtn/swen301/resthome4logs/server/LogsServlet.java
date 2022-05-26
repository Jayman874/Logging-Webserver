package nz.ac.wgtn.swen301.resthome4logs.server;

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
	private Persistency per = new Persistency();

	public LogsServlet() {
		
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONArray jsonArray = new JSONArray();
		PrintWriter printWriter = response.getWriter();
		String limit = request.getParameter("limit");
		String level = request.getParameter("level");
		response.setContentType("application/json");
		for (JSONObject json : per.getDataBase()) {
			jsonArray.put(json);
		}
		printWriter.println(jsonArray.toString(2));
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
	
	@Override
	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
	
	
}

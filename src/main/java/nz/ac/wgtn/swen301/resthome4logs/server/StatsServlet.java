package nz.ac.wgtn.swen301.resthome4logs.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

public class StatsServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public StatsServlet() {}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<String> loggers = new ArrayList<String>();
		PrintWriter printWriter = response.getWriter();
		for (JSONObject obj : Persistency.getDatabase()) {
			String logger = obj.getString("logger");
			if (!(loggers.contains(logger))) {
				loggers.add(logger);
			}
		}
		printWriter.println("<table>");
		printWriter.println("<tr>");
		printWriter.println("<th>logger</th>");
		printWriter.println("<th>ALL</th>");
		printWriter.println("<th>TRACE</th>");
		printWriter.println("<th>DEBUG</th>");
		printWriter.println("<th>INFO</th>");
		printWriter.println("<th>WARN</th>");
		printWriter.println("<th>ERROR</th>");
		printWriter.println("<th>FATAL</th>");
		printWriter.println("<th>OFF</th>");
		printWriter.println("</tr>");
		printWriter.println("</table>");
	}
}

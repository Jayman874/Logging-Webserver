package nz.ac.wgtn.swen301.resthome4logs.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import nz.ac.wgtn.swen301.resthome4logs.server.Persistency.Level;

public class StatsServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public StatsServlet() {}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter printWriter = response.getWriter();
		HashMap<String, HashMap<Level, Integer>> table = new LinkedHashMap<>();
		for (JSONObject json : Persistency.getDatabase()) {
			String loggerName = json.getString("logger");
			String level = json.getString("level");
			HashMap<Level, Integer> levels;
			if (table.containsKey(loggerName)) {
				levels = table.get(loggerName);
			} else {
				levels = new LinkedHashMap<>();
				for (Level l : Level.values()) {
					levels.put(l, 0);
				}
			}
			for (Entry<Level, Integer> entry : levels.entrySet()) {
			    Level key = entry.getKey();
			    if (level.equals(key.name())) {
			    	levels.put(key, levels.get(key) + 1);
			    }
			}
			table.put(loggerName, levels);
		}
		printWriter.println("<table border=1px black solid; style=border-collapse:collapse; table-layout:fixed; width:100%;>");
		printWriter.println("<tr>");
		printWriter.println("<th>logger</th>");
		for (Level level : Level.values()) {
			printWriter.println("<th>" + level.name() + "</th>");
		}
		printWriter.println("</tr>");
		for (Entry<String, HashMap<Level, Integer>> entry : table.entrySet()) {
			printWriter.println("<tr>");
			printWriter.println("<td style=width:10%;>" + entry.getKey() + "</td>");
			for (Level level : Level.values()) {
				if (entry.getValue().containsKey(level)) {
					printWriter.println("<td style=width:10%;>" + entry.getValue().get(level) + "</td>");
				}
			} 
			printWriter.println("</tr>");
		}
		printWriter.println("</table>");
		response.setStatus(HttpServletResponse.SC_OK);
	}
}

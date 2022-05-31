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

public class StatsServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public StatsServlet() {}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter printWriter = response.getWriter();
		HashMap<String, HashMap<String, Integer>> table = new LinkedHashMap<>();
		for (JSONObject json : Persistency.getDatabase()) {
			String loggerName = json.getString("logger");
			String level = json.getString("level");
			HashMap<String, Integer> levels;
			if (table.containsKey(loggerName)) {
				levels = table.get(loggerName);
			} else {
				levels = new LinkedHashMap<>();
				for (Persistency.Level l : Persistency.Level.values()) {
					levels.put(l.name(), 0);
				}
			}
			for (Entry<String, Integer> entry : levels.entrySet()) {
			    String key = entry.getKey();
			    if (level.equals(key)) {
			    	levels.put(key, levels.get(key) + 1);
			    }
			}
			table.put(loggerName, levels);
		}
		printWriter.println("<table border=1px black solid; style=border-collapse:collapse; table-layout:fixed; width:100%;>");
		printWriter.println("<th>logger</th>");
		for (Persistency.Level level : Persistency.Level.values()) {
			printWriter.println("<th>" + level.name() + "</th>");
		}
		for (Entry<String, HashMap<String, Integer>> entry : table.entrySet()) {
			printWriter.println("<tr>");
		    printWriter.println("<td style=width:10%;>" + entry.getKey() + "</td>");
		    for (Persistency.Level level : Persistency.Level.values()) {
				if (entry.getValue().containsKey(level.name())) {
					printWriter.println("<td style=width:10%;>" + entry.getValue().get(level.name()) + "</td>");
				}
			} 
			printWriter.println("</tr>");
		}
		printWriter.println("</table>");
	}
}

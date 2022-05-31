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

public class StatsCSVServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	public StatsCSVServlet() {}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/csv");
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
		printWriter.print("logger\t");
		for (Persistency.Level level : Persistency.Level.values()) {
			printWriter.print(level.name()+"\t");
		}
		printWriter.print("\n");
		for (Entry<String, HashMap<String, Integer>> entry : table.entrySet()) {
			printWriter.print(entry.getKey()+"\t");
			for (Persistency.Level level : Persistency.Level.values()) {
				if (entry.getValue().containsKey(level.name())) {
					printWriter.print(entry.getValue().get(level.name())+"\t");
				}
			}
			printWriter.print("\n");
		}
		response.setStatus(HttpServletResponse.SC_OK);
	}
}
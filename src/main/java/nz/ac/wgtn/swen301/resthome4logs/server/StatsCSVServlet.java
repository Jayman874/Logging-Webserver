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

public class StatsCSVServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	public StatsCSVServlet() {}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/csv");
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
		printWriter.print("logger\t");
		for (Level level : Level.values()) {
			printWriter.print(level.name()+"\t");
		}
		printWriter.print("\n");
		for (Entry<String, HashMap<Level, Integer>> entry : table.entrySet()) {
			printWriter.print(entry.getKey()+"\t");
			for (Level level : Level.values()) {
				if (entry.getValue().containsKey(level)) {
					printWriter.print(entry.getValue().get(level)+"\t");
				}
			}
			printWriter.print("\n");
		}
		response.setStatus(HttpServletResponse.SC_OK);
	}
}
package nz.ac.wgtn.swen301.resthome4logs.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import nz.ac.wgtn.swen301.resthome4logs.server.Persistency.Level;

public class LogsServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	public LogsServlet() {}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONArray jsonArray = new JSONArray();
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		PrintWriter printWriter = response.getWriter();
		String stringLimit = request.getParameter("limit");
		String stringLevel = request.getParameter("level");
		int limit = Persistency.getDatabaseSize();
		int logLevel = -1;
		int numLevels = Level.values().length;
		if (stringLimit == null || stringLevel == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		try {
			limit = Integer.parseInt(stringLimit);
		} catch (NumberFormatException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		if ((!Persistency.contains(stringLevel)) || limit <= 0 || limit > 2147483647) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		for (Level level : Level.values()){
			String string = level.name();
			if (string.equals(stringLevel)) {
				logLevel = level.ordinal();
				break;
			}
		}
		while (logLevel < numLevels) {
			for (JSONObject json : Persistency.getDatabase()) {
				if (json.getString("level").equals(Level.values()[logLevel].name())) {
					jsonList.add(json);
				}
			}
			logLevel++;
		}
		int count = 0;
		response.setContentType("application/json");
		DateFormat date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        jsonList.sort((d1, d2) -> {
            try {
                return date.parse((d2.getString("timestamp"))).compareTo(date.parse(d1.getString("timestamp")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return 0;
        });
		for (JSONObject json : jsonList) {
			if (limit > count) {
				jsonArray.put(json);
			}
			count++;
		}
		response.setStatus(HttpServletResponse.SC_OK);
		printWriter.println(jsonArray.toString(2));
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
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
			DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		    UUID.fromString(id);
			format.setLenient(false);
			format.parse(timestamp);
			if (!(Persistency.contains(level))) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}
			for (JSONObject obj : Persistency.getDatabase()) {
				if (obj.getString("id").equals(id)) {
					response.sendError(HttpServletResponse.SC_CONFLICT);
					return;
				}
			}
			LogEvent le = new LogEvent(id, message, timestamp, thread, logger, level, errorDetails);
			Persistency.addLog(le);
		} catch (JSONException | ParseException | IllegalArgumentException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		response.setStatus(HttpServletResponse.SC_CREATED);
	}
	
	@Override
	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Persistency.removeAll();
		response.setStatus(HttpServletResponse.SC_OK);
	}
	
}

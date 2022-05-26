package nz.ac.wgtn.swen301.resthome4logs.server;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import org.json.JSONObject;

public class Persistency {

	public static List<JSONObject> DB = new ArrayList<JSONObject>();
	
	public static List<JSONObject> getDataBase() {
		return DB;
	}
	
	public static void addLog(LogEvent e) {
		DB.add(format(e));
	}
	
	public static void removeAll() {
		DB.clear();
	}
	
	public static JSONObject format(LogEvent event) {
		JSONObject logMap = new JSONObject();
		// uses reflection to set map to a linked hash map
		// this is so map can be ordered when it is being parsed
		try {
		  Field changeMap = logMap.getClass().getDeclaredField("map");
		  changeMap.setAccessible(true);
		  changeMap.set(logMap, new LinkedHashMap<>());
		  changeMap.setAccessible(false);
		} catch (IllegalAccessException | NoSuchFieldException e) {
			e.printStackTrace();
		}
		logMap.put("id" , event.getId());
		logMap.put("message" , event.getMessage());
		logMap.put("timestamp" , event.getTimestamp());
		logMap.put("thread" , event.getThread());
		logMap.put("logger" , event.getLogger());
		logMap.put("level" , event.getLevel());
		logMap.put("errorDetails" , event.getErrorDetails());
		return logMap;
	}

}

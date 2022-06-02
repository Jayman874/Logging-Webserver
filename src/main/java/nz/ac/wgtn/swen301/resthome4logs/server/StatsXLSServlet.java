package nz.ac.wgtn.swen301.resthome4logs.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;

import nz.ac.wgtn.swen301.resthome4logs.server.Persistency.Level;

public class StatsXLSServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public StatsXLSServlet() {}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/vnd.ms-excel");
		ServletOutputStream outputStream = response.getOutputStream();
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
		XSSFWorkbook xlsBook = new XSSFWorkbook();
		XSSFSheet sheet = xlsBook.createSheet("stats");
		XSSFRow row = sheet.createRow(0);
		XSSFCell cell = row.createCell(0);
		int cellNumber = 1;
		int rowNumber = 1;
		cell.setCellValue("logger");
		for (Level level : Level.values()) {
			cell = row.createCell(cellNumber++);
			cell.setCellValue(level.name());
		}
		for (Entry<String, HashMap<Level, Integer>> entry : table.entrySet()) {
			cellNumber = 0;
			row = sheet.createRow(rowNumber);
			cell = row.createCell(cellNumber++);
			cell.setCellValue(entry.getKey());
			for (Level level : Level.values()) {
				if (entry.getValue().containsKey(level)) {
					cell = row.createCell(cellNumber++);
					cell.setCellValue(entry.getValue().get(level));
				}
			}
			rowNumber++;
		}
		response.setStatus(HttpServletResponse.SC_OK);
		xlsBook.write(outputStream);
		xlsBook.close();
	}
}

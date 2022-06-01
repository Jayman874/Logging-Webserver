package nz.ac.wgtn.swen301.resthome4logs.server;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import nz.ac.wgtn.swen301.resthome4logs.server.Persistency.Level;

public class TestStatsXLS {
	
	@Test
	public void testXLSServletResponseValid() throws ServletException, IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		StatsXLSServlet xlsServlet = new StatsXLSServlet();
		xlsServlet.doGet(request, response);
		assertEquals(response.getStatus(), HttpServletResponse.SC_OK);
	}
	
	@Test
	public void testXLSServletContentType() throws ServletException, IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		StatsXLSServlet xlsServlet = new StatsXLSServlet();
		xlsServlet.doGet(request, response);
		assertEquals(response.getContentType(), "application/vnd.ms-excel");
	}
	
	@Test
	public void testXLSServletCorrectSheetName() throws ServletException, IOException {
		String jsonString = "  {\n" +
	            "    \"id\": \"d290f1ee-6c54-4b01-90e6-d701748f0851\",\n" +
	            "    \"message\": \"application started\",\n" +
	            "    \"timestamp\": \"04-05-2021 10:12:00\",\n" +
	            "    \"thread\": \"main\",\n" +
	            "    \"logger\": \"com.example.Foo\",\n" +
	            "    \"level\": \"DEBUG\",\n" +
	            "    \"errorDetails\": \"string\"\n" +
	            "  }";
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		LogsServlet servlet = new LogsServlet();
		StatsXLSServlet xlsServlet = new StatsXLSServlet();
		JSONObject json = new JSONObject(jsonString);
		request.setContent(json.toString().getBytes());
		servlet.doPost(request, response);
		assertEquals(response.getStatus(), HttpServletResponse.SC_CREATED);
		xlsServlet.doGet(request, response);
		assertEquals(response.getStatus(), HttpServletResponse.SC_OK);
		InputStream xls = new ByteArrayInputStream(response.getContentAsByteArray());
		XSSFWorkbook xlsBook = new XSSFWorkbook(xls);
		assertEquals(xlsBook.getSheetName(0), "stats");
		xlsBook.close();
		Persistency.getDatabase().clear();
	}
	
	@Test
	public void testXLSServletValid() throws ServletException, IOException {
		String jsonString = "  {\n" +
	            "    \"id\": \"d290f1ee-6c54-4b01-90e6-d701748f0851\",\n" +
	            "    \"message\": \"application started\",\n" +
	            "    \"timestamp\": \"04-05-2021 10:12:00\",\n" +
	            "    \"thread\": \"main\",\n" +
	            "    \"logger\": \"com.example.Foo\",\n" +
	            "    \"level\": \"DEBUG\",\n" +
	            "    \"errorDetails\": \"string\"\n" +
	            "  }";
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		LogsServlet servlet = new LogsServlet();
		StatsXLSServlet xlsServlet = new StatsXLSServlet();
		JSONObject json = new JSONObject(jsonString);
		request.setContent(json.toString().getBytes());
		servlet.doPost(request, response);
		assertEquals(response.getStatus(), HttpServletResponse.SC_CREATED);
		xlsServlet.doGet(request, response);
		assertEquals(response.getStatus(), HttpServletResponse.SC_OK);
		InputStream xls = new ByteArrayInputStream(response.getContentAsByteArray());
		XSSFWorkbook xlsBook = new XSSFWorkbook(xls);
		XSSFSheet sheet = xlsBook.getSheet("stats");
		int row = 0;
		assertEquals(sheet.getRow(row).getCell(0).toString(), "logger");
		Level[] level = Persistency.Level.values();
		for (int i = 1; i < sheet.getRow(row).getLastCellNum(); i++) {
			assertEquals(sheet.getRow(row).getCell(i).toString(), level[i-1].name());
		}
		row++;
		assertEquals(sheet.getRow(row).getCell(0).toString(), json.getString("logger"));
		for (int i = 1; i < sheet.getRow(row).getLastCellNum(); i++) {
			int value = (int) sheet.getRow(row).getCell(i).getNumericCellValue();
			if (i != Persistency.Level.valueOf(json.getString("level")).ordinal()+1) {
				assertEquals(value, 0);
			} else {
				assertEquals(value, 1);
			}
		}
		assertEquals(response.getStatus(), HttpServletResponse.SC_OK);
		xlsBook.close();
		Persistency.getDatabase().clear();
	}
	
	@Test
	public void testXLSServletValid2() throws ServletException, IOException {
		String jsonString = "  {\n" +
	            "    \"id\": \"d290f1ee-6c54-4b01-90e6-d701748f0851\",\n" +
	            "    \"message\": \"application started\",\n" +
	            "    \"timestamp\": \"04-05-2021 10:12:00\",\n" +
	            "    \"thread\": \"main\",\n" +
	            "    \"logger\": \"com.example.Foo\",\n" +
	            "    \"level\": \"DEBUG\",\n" +
	            "    \"errorDetails\": \"string\"\n" +
	            "  }";
		String jsonString2 = "  {\n" +
	            "    \"id\": \"d290f1ee-6c54-4b01-90e6-d701748f0852\",\n" +
	            "    \"message\": \"application started\",\n" +
	            "    \"timestamp\": \"04-05-2021 10:12:00\",\n" +
	            "    \"thread\": \"main\",\n" +
	            "    \"logger\": \"com.example.Foo\",\n" +
	            "    \"level\": \"DEBUG\",\n" +
	            "    \"errorDetails\": \"string\"\n" +
	            "  }";
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		LogsServlet servlet = new LogsServlet();
		StatsXLSServlet xlsServlet = new StatsXLSServlet();
		JSONObject json = new JSONObject(jsonString);
		JSONObject json2 = new JSONObject(jsonString2);
		request.setContent(json.toString().getBytes());
		servlet.doPost(request, response);
		assertEquals(response.getStatus(), HttpServletResponse.SC_CREATED);
		request.setContent(json2.toString().getBytes());
		servlet.doPost(request, response);
		assertEquals(response.getStatus(), HttpServletResponse.SC_CREATED);
		xlsServlet.doGet(request, response);
		assertEquals(response.getStatus(), HttpServletResponse.SC_OK);
		InputStream xls = new ByteArrayInputStream(response.getContentAsByteArray());
		XSSFWorkbook xlsBook = new XSSFWorkbook(xls);
		XSSFSheet sheet = xlsBook.getSheet("stats");
		int row = 0;
		assertEquals(sheet.getRow(row).getCell(0).toString(), "logger");
		Level[] level = Persistency.Level.values();
		for (int i = 1; i < sheet.getRow(row).getLastCellNum(); i++) {
			assertEquals(sheet.getRow(row).getCell(i).toString(), level[i-1].name());
		}
		row++;
		assertEquals(sheet.getRow(row).getCell(0).toString(), json.getString("logger"));
		for (int i = 1; i < sheet.getRow(row).getLastCellNum(); i++) {
			int value = (int) sheet.getRow(row).getCell(i).getNumericCellValue();
			if (i != Persistency.Level.valueOf(json.getString("level")).ordinal()+1) {
				assertEquals(value, 0);
			} else {
				assertEquals(value, 2);
			}
		}
		assertEquals(response.getStatus(), HttpServletResponse.SC_OK);
		xlsBook.close();
		Persistency.getDatabase().clear();
	}
	
	@Test
	public void testXLSServletValid3() throws ServletException, IOException {
		String jsonString = "  {\n" +
	            "    \"id\": \"d290f1ee-6c54-4b01-90e6-d701748f0851\",\n" +
	            "    \"message\": \"application started\",\n" +
	            "    \"timestamp\": \"04-05-2021 10:12:00\",\n" +
	            "    \"thread\": \"main\",\n" +
	            "    \"logger\": \"com.example.Foo\",\n" +
	            "    \"level\": \"DEBUG\",\n" +
	            "    \"errorDetails\": \"string\"\n" +
	            "  }";
		String jsonString2 = "  {\n" +
	            "    \"id\": \"d290f1ee-6c54-4b01-90e6-d701748f0951\",\n" +
	            "    \"message\": \"application started\",\n" +
	            "    \"timestamp\": \"04-05-2021 10:12:00\",\n" +
	            "    \"thread\": \"main\",\n" +
	            "    \"logger\": \"com.example.Test\",\n" +
	            "    \"level\": \"WARN\",\n" +
	            "    \"errorDetails\": \"string\"\n" +
	            "  }";
		String jsonString3 = "  {\n" +
	            "    \"id\": \"d290f1ee-6c54-4b01-90e6-d701748f0952\",\n" +
	            "    \"message\": \"application started\",\n" +
	            "    \"timestamp\": \"04-05-2021 10:12:00\",\n" +
	            "    \"thread\": \"main\",\n" +
	            "    \"logger\": \"com.example.Test\",\n" +
	            "    \"level\": \"INFO\",\n" +
	            "    \"errorDetails\": \"string\"\n" +
	            "  }";
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		LogsServlet servlet = new LogsServlet();
		StatsXLSServlet xlsServlet = new StatsXLSServlet();
		JSONObject json = new JSONObject(jsonString);
		JSONObject json2 = new JSONObject(jsonString2);
		JSONObject json3 = new JSONObject(jsonString3);
		request.setContent(json.toString().getBytes());
		servlet.doPost(request, response);
		assertEquals(response.getStatus(), HttpServletResponse.SC_CREATED);
		request.setContent(json2.toString().getBytes());
		servlet.doPost(request, response);
		assertEquals(response.getStatus(), HttpServletResponse.SC_CREATED);
		request.setContent(json3.toString().getBytes());
		servlet.doPost(request, response);
		assertEquals(response.getStatus(), HttpServletResponse.SC_CREATED);
		xlsServlet.doGet(request, response);
		assertEquals(response.getStatus(), HttpServletResponse.SC_OK);
		InputStream xls = new ByteArrayInputStream(response.getContentAsByteArray());
		XSSFWorkbook xlsBook = new XSSFWorkbook(xls);
		XSSFSheet sheet = xlsBook.getSheet("stats");
		int row = 0;
		assertEquals(sheet.getRow(row).getCell(0).toString(), "logger");
		Level[] level = Persistency.Level.values();
		for (int i = 1; i < sheet.getRow(row).getLastCellNum(); i++) {
			assertEquals(sheet.getRow(row).getCell(i).toString(), level[i-1].name());
		}
		row++;
		assertEquals(sheet.getRow(row).getCell(0).toString(), json.getString("logger"));
		for (int i = 1; i < sheet.getRow(row).getLastCellNum(); i++) {
			int value = (int) sheet.getRow(row).getCell(i).getNumericCellValue();
			if (i != Persistency.Level.valueOf(json.getString("level")).ordinal()+1) {
				assertEquals(value, 0);
			} else {
				assertEquals(value, 1);
			}
		}
		row++;
		assertEquals(sheet.getRow(row).getCell(0).toString(), json2.getString("logger"));
		for (int i = 1; i < sheet.getRow(row).getLastCellNum(); i++) {
			int value = (int) sheet.getRow(row).getCell(i).getNumericCellValue();
			if (i == Persistency.Level.valueOf(json2.getString("level")).ordinal()+1 
					|| i == Persistency.Level.valueOf(json3.getString("level")).ordinal()+1) {
				assertEquals(value, 1);
			} else {
				assertEquals(value, 0);
			}
		}
		assertEquals(response.getStatus(), HttpServletResponse.SC_OK);
		xlsBook.close();
		Persistency.getDatabase().clear();
	}
	
}

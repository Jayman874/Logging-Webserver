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
		assertEquals(sheet.getRow(0).getCell(0).toString(), "logger");
		Level[] level = Persistency.Level.values();
		for (int i = 1; i < sheet.getRow(0).getLastCellNum(); i++) {
			assertEquals(sheet.getRow(0).getCell(i).toString(), level[i-1].name());
		}
		xlsBook.close();
		Persistency.getDatabase().clear();
	}
	
}

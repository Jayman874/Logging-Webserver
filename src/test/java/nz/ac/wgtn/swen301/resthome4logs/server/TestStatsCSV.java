package nz.ac.wgtn.swen301.resthome4logs.server;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import nz.ac.wgtn.swen301.resthome4logs.server.Persistency.Level;

public class TestStatsCSV {
	
	private String jsonString = "  {\n" +
            "    \"id\": \"d290f1ee-6c54-4b01-90e6-d701748f0851\",\n" +
            "    \"message\": \"application started\",\n" +
            "    \"timestamp\": \"04-05-2021 10:12:00\",\n" +
            "    \"thread\": \"main\",\n" +
            "    \"logger\": \"com.example.Foo\",\n" +
            "    \"level\": \"DEBUG\",\n" +
            "    \"errorDetails\": \"string\"\n" +
            "  }";
	
	private String jsonString2 = "  {\n" +
            "    \"id\": \"d290f1ee-6c54-4b01-90e6-d701748f0851\",\n" +
            "    \"message\": \"application started\",\n" +
            "    \"timestamp\": \"04-05-2021 10:12:00\",\n" +
            "    \"thread\": \"main\",\n" +
            "    \"logger\": \"com.example.Foo\",\n" +
            "    \"level\": \"TRACE\",\n" +
            "    \"errorDetails\": \"string\"\n" +
            "  }";
	
	private String jsonString3 = "  {\n" +
            "    \"id\": \"d290f1ee-6c54-4b01-90e6-d701748f0951\",\n" +
            "    \"message\": \"application started\",\n" +
            "    \"timestamp\": \"04-05-2021 10:12:00\",\n" +
            "    \"thread\": \"main\",\n" +
            "    \"logger\": \"com.example.Foo\",\n" +
            "    \"level\": \"TRACE\",\n" +
            "    \"errorDetails\": \"string\"\n" +
            "  }";
	
	private String jsonString4 = "  {\n" +
            "    \"id\": \"d290f1ee-6c54-4b01-90e6-d701748f0951\",\n" +
            "    \"message\": \"application started\",\n" +
            "    \"timestamp\": \"04-05-2021 10:12:00\",\n" +
            "    \"thread\": \"main\",\n" +
            "    \"logger\": \"com.example.Test\",\n" +
            "    \"level\": \"WARN\",\n" +
            "    \"errorDetails\": \"string\"\n" +
            "  }";
	
	private String jsonString5 = "  {\n" +
            "    \"id\": \"d290f1ee-6c54-4b01-90e6-d701748f0952\",\n" +
            "    \"message\": \"application started\",\n" +
            "    \"timestamp\": \"04-05-2021 10:12:00\",\n" +
            "    \"thread\": \"main\",\n" +
            "    \"logger\": \"com.example.Test\",\n" +
            "    \"level\": \"INFO\",\n" +
            "    \"errorDetails\": \"string\"\n" +
            "  }";
	
	@Test
	public void testCSVServletResponseValid() throws ServletException, IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		StatsCSVServlet csvServlet = new StatsCSVServlet();
		csvServlet.doGet(request, response);
		assertEquals(response.getStatus(), HttpServletResponse.SC_OK);
	}
	
	@Test
	public void testCSVServletContentType() throws ServletException, IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		StatsCSVServlet csvServlet = new StatsCSVServlet();
		csvServlet.doGet(request, response);
		assertEquals(response.getContentType(), "text/csv");
	}
	
	@Test
	public void testCSVServletValid() throws ServletException, IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		LogsServlet servlet = new LogsServlet();
		StatsCSVServlet csvServlet = new StatsCSVServlet();
		JSONObject json = new JSONObject(jsonString);
		request.setContent(json.toString().getBytes());
		servlet.doPost(request, response);
		assertEquals(response.getStatus(), HttpServletResponse.SC_CREATED);
		csvServlet.doGet(request, response);
		assertEquals(response.getStatus(), HttpServletResponse.SC_OK);
		Level[] level = Persistency.Level.values();
		String[] info = response.getContentAsString().split("\n");
		String[] loggers = info[0].split("\t");
		String[] loggerNum = info[1].split("\t");
		assertEquals(loggers[0], "logger");
		for (int i = 1; i < Persistency.Level.values().length+1; i++) {
			assertEquals(loggers[i], level[i-1].name());
		}
		assertEquals(loggerNum[0], json.getString("logger"));
		for (int i = 1; i < Persistency.Level.values().length+1; i++) {
			if (i != Persistency.Level.valueOf(json.getString("level")).ordinal()+1) {
				assertEquals(loggerNum[i], "0");
			} else {
				assertEquals(loggerNum[i], "1");
			}
		}
		assertEquals(response.getStatus(), HttpServletResponse.SC_OK);
		Persistency.getDatabase().clear();
	}
	
	@Test
	public void testCSVServletValid2() throws ServletException, IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		LogsServlet servlet = new LogsServlet();
		StatsCSVServlet csvServlet = new StatsCSVServlet();
		JSONObject json = new JSONObject(jsonString2);
		JSONObject json2 = new JSONObject(jsonString3);
		request.setContent(json.toString().getBytes());
		servlet.doPost(request, response);
		assertEquals(response.getStatus(), HttpServletResponse.SC_CREATED);
		request.setContent(json2.toString().getBytes());
		servlet.doPost(request, response);
		assertEquals(response.getStatus(), HttpServletResponse.SC_CREATED);
		csvServlet.doGet(request, response);
		Level[] level = Persistency.Level.values();
		String[] info = response.getContentAsString().split("\n");
		String[] loggers = info[0].split("\t");
		String[] loggerNum = info[1].split("\t");
		assertEquals(loggers[0], "logger");
		for (int i = 1; i < Persistency.Level.values().length+1; i++) {
			assertEquals(loggers[i], level[i-1].name());
		}
		assertEquals(loggerNum[0], json.getString("logger"));
		for (int i = 1; i < Persistency.Level.values().length+1; i++) {
			if (i != Persistency.Level.valueOf(json.getString("level")).ordinal()+1) {
				assertEquals(loggerNum[i], "0");
			} else {
				assertEquals(loggerNum[i], "2");
			}
		}
		assertEquals(response.getStatus(), HttpServletResponse.SC_OK);
		Persistency.getDatabase().clear();
	}
	
	@Test
	public void testCSVServletValid3() throws ServletException, IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		LogsServlet servlet = new LogsServlet();
		StatsCSVServlet csvServlet = new StatsCSVServlet();
		JSONObject json = new JSONObject(jsonString);
		JSONObject json2 = new JSONObject(jsonString4);
		JSONObject json3 = new JSONObject(jsonString5);
		request.setContent(json.toString().getBytes());
		servlet.doPost(request, response);
		assertEquals(response.getStatus(), HttpServletResponse.SC_CREATED);
		request.setContent(json2.toString().getBytes());
		servlet.doPost(request, response);
		assertEquals(response.getStatus(), HttpServletResponse.SC_CREATED);
		request.setContent(json3.toString().getBytes());
		servlet.doPost(request, response);
		assertEquals(response.getStatus(), HttpServletResponse.SC_CREATED);
		csvServlet.doGet(request, response);
		Level[] level = Persistency.Level.values();
		String[] info = response.getContentAsString().split("\n");
		String[] loggers = info[0].split("\t");
		String[] loggerNum = info[1].split("\t");
		String[] loggerNum2 = info[2].split("\t");
		assertEquals(loggers[0], "logger");
		for (int i = 1; i < Persistency.Level.values().length+1; i++) {
			assertEquals(loggers[i], level[i-1].name());
		}
		assertEquals(loggerNum[0], json.getString("logger"));
		for (int i = 1; i < Persistency.Level.values().length+1; i++) {
			if (i != Persistency.Level.valueOf(json.getString("level")).ordinal()+1) {
				assertEquals(loggerNum[i], "0");
			} else {
				assertEquals(loggerNum[i], "1");
			}
		}
		assertEquals(loggerNum2[0], json2.getString("logger"));
		for (int i = 1; i < Persistency.Level.values().length+1; i++) {
			if (i == Persistency.Level.valueOf(json2.getString("level")).ordinal()+1 
					|| i == Persistency.Level.valueOf(json3.getString("level")).ordinal()+1) {
				assertEquals(loggerNum2[i], "1");
			} else {
				assertEquals(loggerNum2[i], "0");
			}
		}
		assertEquals(response.getStatus(), HttpServletResponse.SC_OK);
		Persistency.getDatabase().clear();
	}
}

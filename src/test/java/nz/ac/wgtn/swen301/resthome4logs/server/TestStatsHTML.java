package nz.ac.wgtn.swen301.resthome4logs.server;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import nz.ac.wgtn.swen301.resthome4logs.server.Persistency.Level;

public class TestStatsHTML {

	@Test
	public void testHTMLServletResponseValid() throws ServletException, IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		StatsServlet htmlServlet = new StatsServlet();
		htmlServlet.doGet(request, response);
		assertEquals(response.getStatus(), HttpServletResponse.SC_OK);
	}
	
	@Test
	public void testHTMLServletContentType() throws ServletException, IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		StatsServlet htmlServlet = new StatsServlet();
		htmlServlet.doGet(request, response);
		assertEquals(response.getContentType(), "text/html");
	}
	
	@Test
	public void testHTMLServletValid() throws ServletException, IOException {
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
		StatsServlet htmlServlet = new StatsServlet();
		JSONObject json = new JSONObject(jsonString);
		request.setContent(json.toString().getBytes());
		servlet.doPost(request, response);
		assertEquals(response.getStatus(), HttpServletResponse.SC_CREATED);
		htmlServlet.doGet(request, response);
		Document document = Jsoup.parse(response.getContentAsString(), Parser.htmlParser());
		String body = document.body().wholeText();
		String[] info = body.split("\n");
		assertEquals(info[2], "logger\r");
		Level[] level = Persistency.Level.values();
		for (int i = 3; i < 11; i++) {
			assertEquals(info[i], level[i-3].name()+"\r");
		}
		assertEquals(info[13], json.getString("logger")+"\r");
		for (int i = 14; i < 21; i++) {
			if (i != 16) {
				assertEquals(info[i], "0\r");
			} else {
				assertEquals(info[i], "1\r");
			}
		}
		assertEquals(response.getStatus(), HttpServletResponse.SC_OK);
		Persistency.getDatabase().clear();
	}
	
	@Test
	public void testHTMLServletValid2() throws ServletException, IOException {
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
	            "    \"logger\": \"com.example.Foo\",\n" +
	            "    \"level\": \"DEBUG\",\n" +
	            "    \"errorDetails\": \"string\"\n" +
	            "  }";
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		LogsServlet servlet = new LogsServlet();
		StatsServlet htmlServlet = new StatsServlet();
		JSONObject json = new JSONObject(jsonString);
		JSONObject json2 = new JSONObject(jsonString2);
		request.setContent(json.toString().getBytes());
		servlet.doPost(request, response);
		assertEquals(response.getStatus(), HttpServletResponse.SC_CREATED);
		request.setContent(json2.toString().getBytes());
		servlet.doPost(request, response);
		assertEquals(response.getStatus(), HttpServletResponse.SC_CREATED);
		htmlServlet.doGet(request, response);
		Document document = Jsoup.parse(response.getContentAsString(), Parser.htmlParser());
		String body = document.body().wholeText();
		String[] info = body.split("\n");
		assertEquals(info[2], "logger\r");
		Level[] level = Persistency.Level.values();
		for (int i = 3; i < 11; i++) {
			assertEquals(info[i], level[i-3].name()+"\r");
		}
		assertEquals(info[13], json.getString("logger")+"\r");
		for (int i = 14; i < 21; i++) {
			if (i != 16) {
				assertEquals(info[i], "0\r");
			} else {
				assertEquals(info[i], "2\r");
			}
		}
		assertEquals(response.getStatus(), HttpServletResponse.SC_OK);
		Persistency.getDatabase().clear();
	}
	
	@Test
	public void testHTMLServletValid3() throws ServletException, IOException {
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
		StatsServlet htmlServlet = new StatsServlet();
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
		htmlServlet.doGet(request, response);
		Document document = Jsoup.parse(response.getContentAsString(), Parser.htmlParser());
		String body = document.body().wholeText();
		String[] info = body.split("\n");
		assertEquals(info[2], "logger\r");
		Level[] level = Persistency.Level.values();
		for (int i = 3; i < 11; i++) {
			assertEquals(info[i], level[i-3].name()+"\r");
		}
		assertEquals(info[13], json.getString("logger")+"\r");
		for (int i = 14; i < 21; i++) {
			if (i != 16) {
				assertEquals(info[i], "0\r");
			} else {
				assertEquals(info[i], "1\r");
			}
		}
		assertEquals(info[24], json2.getString("logger")+"\r");
		for (int i = 25; i < 33; i++) {
			if (i == 29 || i == 28) {
				assertEquals(info[i], "1\r");
			} else {
				assertEquals(info[i], "0\r");
			}
		}
		assertEquals(response.getStatus(), HttpServletResponse.SC_OK);
		Persistency.getDatabase().clear();
	}
}

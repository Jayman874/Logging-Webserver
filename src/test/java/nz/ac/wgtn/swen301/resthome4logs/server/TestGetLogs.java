package nz.ac.wgtn.swen301.resthome4logs.server;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class TestGetLogs {
	
	@Test
	public void testGetLogNoParameters() throws ServletException, IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		LogsServlet servlet = new LogsServlet();
		servlet.doGet(request, response);
		assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
	}
	
	@Test
	public void testGetLogOneParameterLimit() throws ServletException, IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		LogsServlet servlet = new LogsServlet();
		request.setParameter("limit", "5");
		servlet.doGet(request, response);
		assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
	}
	
	@Test
	public void testGetLogOneParameterLevel() throws ServletException, IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		LogsServlet servlet = new LogsServlet();
		request.setParameter("level", "WARN");
		servlet.doGet(request, response);
		assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
	}

	@Test
	public void testGetLogContentType() throws ServletException, IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		LogsServlet servlet = new LogsServlet();
		request.setParameter("limit", "5");
		request.setParameter("level", "WARN");
		servlet.doGet(request, response);
		assertEquals("application/json", response.getContentType());
	}
	
	@Test
	public void testGetLogParameters() throws ServletException, IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		LogsServlet servlet = new LogsServlet();
		request.setParameter("limit", "5");
		request.setParameter("level", "WARN");
		servlet.doGet(request, response);
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
	}
	
	@Test
	public void testGetInvalidLimitParameter() throws ServletException, IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		LogsServlet servlet = new LogsServlet();
		request.setParameter("limit", "-10");
		request.setParameter("level", "WARN");
		servlet.doGet(request, response);
		assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
	}
	
	@Test
	public void testGetInvalidLimitParameter2() throws ServletException, IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		LogsServlet servlet = new LogsServlet();
		request.setParameter("limit", "number");
		request.setParameter("level", "DEBUG");
		servlet.doGet(request, response);
		assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
	}
	
	@Test
	public void testGetInvalidLevelParameter() throws ServletException, IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		LogsServlet servlet = new LogsServlet();
		request.setParameter("limit", "3");
		request.setParameter("level", "bad");
		servlet.doGet(request, response);
		assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
	}
	
	@Test
	public void testGetInvalidParameters() throws ServletException, IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		LogsServlet servlet = new LogsServlet();
		request.setParameter("limit", "-10");
		request.setParameter("level", "DEBU");
		servlet.doGet(request, response);
		assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
	}
	
	@Test
	public void testGetLogValid() throws ServletException, IOException {
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
		JSONObject json = new JSONObject(jsonString);
		request.setContent(json.toString().getBytes());
		servlet.doPost(request, response);
		request.setParameter("limit", "5");
		request.setParameter("level", "ALL");
		servlet.doGet(request, response);
		String content = response.getContentAsString();
		JSONArray jsonArray = new JSONArray(content);
		JSONObject obj = jsonArray.getJSONObject(0);
		assertEquals(obj.get("id"), "d290f1ee-6c54-4b01-90e6-d701748f0851");
		assertEquals(obj.get("message"), "application started");
		assertEquals(obj.get("timestamp"), "04-05-2021 10:12:00");
		assertEquals(obj.get("thread"), "main");
		assertEquals(obj.get("logger"), "com.example.Foo");
		assertEquals(obj.get("level"), "DEBUG");
		assertEquals(obj.get("errorDetails"), "string");
		Persistency.getDatabase().clear();
	}
}

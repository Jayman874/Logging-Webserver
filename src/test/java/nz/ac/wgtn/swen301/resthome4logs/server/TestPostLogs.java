package nz.ac.wgtn.swen301.resthome4logs.server;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class TestPostLogs {
	
	@Test
	public void testPostLogValid() throws ServletException, IOException {
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
		assertEquals(response.getStatus(), HttpServletResponse.SC_CREATED);
		Persistency.getDatabase().clear();
	}
	
	@Test
	public void testPostLogValid2() throws ServletException, IOException {
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
		JSONObject json = new JSONObject(jsonString);
		JSONObject json2 = new JSONObject(jsonString2);
		request.setContent(json.toString().getBytes());
		servlet.doPost(request, response);
		assertEquals(response.getStatus(), HttpServletResponse.SC_CREATED);
		request.setContent(json2.toString().getBytes());
		servlet.doPost(request, response);
		assertEquals(response.getStatus(), HttpServletResponse.SC_CREATED);
		request.setParameter("limit", "1");
		request.setParameter("level", "ALL");
		servlet.doGet(request, response);
		assertEquals(response.getStatus(), HttpServletResponse.SC_OK);
		Persistency.getDatabase().clear();
	}
	
	@Test
	public void testPostLogDuplicateIdInvalid() throws ServletException, IOException {
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
		JSONObject json2 = new JSONObject(jsonString);
		request.setContent(json.toString().getBytes());
		servlet.doPost(request, response);
		assertEquals(response.getStatus(), HttpServletResponse.SC_CREATED);
		request.setContent(json2.toString().getBytes());
		servlet.doPost(request, response);
		assertEquals(response.getStatus(), HttpServletResponse.SC_CONFLICT);
		Persistency.getDatabase().clear();
	}
	
	@Test
	public void testPostLogInvalidJson() throws ServletException, IOException {
		String jsonString = "  {\n" +
	            "    \"id\": \"d290f1ee-6c54-4b01-90e6-d701748f0851\",\n" +
	            "    \"message\": \"application started\",\n" +
	            "    \"time\": \"04-05-2021 10:12:00\",\n" +
	            "    \"thread\": \"main\",\n" +
	            "    \"logger\": \"com.example.Foo\",\n" +
	            "    \"level\": \"DEBUG\",\n" +
	            "    \"error\": \"string\"\n" +
	            "  }";
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		LogsServlet servlet = new LogsServlet();
		JSONObject json = new JSONObject(jsonString);
		request.setContent(json.toString().getBytes());
		servlet.doPost(request, response);
		assertEquals(response.getStatus(), HttpServletResponse.SC_BAD_REQUEST);
		Persistency.getDatabase().clear();
	}
	
	@Test
	public void testPostLogInvalidDate() throws ServletException, IOException {
		String jsonString = "  {\n" +
	            "    \"id\": \"d290f1ee-6c54-4b01-90e6-d701748f0851\",\n" +
	            "    \"message\": \"application started\",\n" +
	            "    \"timestamp\": \"2022-04-05 10:12:00\",\n" +
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
		assertEquals(response.getStatus(), HttpServletResponse.SC_BAD_REQUEST);
		Persistency.getDatabase().clear();
	}
	
	@Test
	public void testPostLogInvalidUUID() throws ServletException, IOException {
		String jsonString = "  {\n" +
	            "    \"id\": \"d290f1ee-6c5fnjdecbjbjbffedjwl c\",\n" +
	            "    \"message\": \"application started\",\n" +
	            "    \"timestamp\": \"04-05-2022 10:12:00\",\n" +
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
		assertEquals(response.getStatus(), HttpServletResponse.SC_BAD_REQUEST);
		Persistency.getDatabase().clear();
	}
	
	@Test
	public void testPostLogInvalidLogLevel() throws ServletException, IOException {
		String jsonString = "  {\n" +
	            "    \"id\": \"d290f1ee-6c54-4b01-90e6-d701748f0851\",\n" +
	            "    \"message\": \"application started\",\n" +
	            "    \"timestamp\": \"04-05-2021 10:12:00\",\n" +
	            "    \"thread\": \"main\",\n" +
	            "    \"logger\": \"com.example.Foo\",\n" +
	            "    \"level\": \"HELLO\",\n" +
	            "    \"errorDetails\": \"string\"\n" +
	            "  }";
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		LogsServlet servlet = new LogsServlet();
		JSONObject json = new JSONObject(jsonString);
		request.setContent(json.toString().getBytes());
		servlet.doPost(request, response);
		assertEquals(response.getStatus(), HttpServletResponse.SC_BAD_REQUEST);
		Persistency.getDatabase().clear();
	}
}

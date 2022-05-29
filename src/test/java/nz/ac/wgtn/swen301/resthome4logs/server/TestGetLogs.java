package nz.ac.wgtn.swen301.resthome4logs.server;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class TestGetLogs {

	@Test
	public void testLogContentType() throws ServletException, IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		LogsServlet servlet = new LogsServlet();
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
}

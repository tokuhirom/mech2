package me.geso.mech2;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Test;

public class Mech2ResultTest {

	@Test
	public void testStatusCode() throws Exception {
		JettyServletTester.runServlet((req, resp) -> {
			resp.setContentType("text/html; charset=utf-8");
			resp.getWriter().print("HOGE");
		}, (baseURL) -> {
			Mech2 mech2 = Mech2.builder().build();
			Mech2Result res = mech2.get(baseURL).execute();
			assertThat(res.getStatusCode(), is(200));
		});
	}

	@Test
	public void testContentType() throws Exception {
		JettyServletTester.runServlet((req, resp) -> {
			resp.setContentType("text/html; charset=utf-8");
			resp.getWriter().print("HOGE");
		}, (baseURL) -> {
			Mech2 mech2 = Mech2.builder().build();
			Mech2Result res = mech2.get(baseURL).execute();
			assertThat(res.getContentType().getCharset().displayName(),
				is("UTF-8"));
			assertThat(res.getContentType().getMimeType(), is("text/html"));
		});
	}

	@Test
	public void testContentType2() throws Exception {
		JettyServletTester.runServlet((req, resp) -> {
			resp.setContentType("text/html; charset=utf-8");
			resp.getWriter().print("まるちばいと");
		}, (baseURL) -> {
			Mech2 mech2 = Mech2.builder().build();
			Mech2Result res = mech2.get(baseURL).execute();
			assertThat(res.getContentType().getCharset().displayName(),
				is("UTF-8"));
			assertThat(res.getContentType().getMimeType(), is("text/html"));
			assertThat(res.getResponseBodyAsString(), is("まるちばいと"));
		});
	}

	@Test
	public void isJSONResponseShouldReturnTrueIfContentTypeIsJSON() throws Exception {
		JettyServletTester.runServlet((req, resp) -> {
			resp.setContentType("application/json; charset=utf-8");
		}, (baseURL) -> {
			Mech2 mech2 = Mech2.builder().build();
			Mech2Result res = mech2.get(baseURL).execute();
			assertTrue(res.isJSONResponse());
		});
	}

	@Test
	public void isJSONResponseShouldReturnTrueWithCaseInsensitive() throws Exception {
		JettyServletTester.runServlet((req, resp) -> {
			resp.setContentType("Application/Json; charset=utf-8");
		}, (baseURL) -> {
			Mech2 mech2 = Mech2.builder().build();
			Mech2Result res = mech2.get(baseURL).execute();
			assertTrue(res.isJSONResponse());
		});
	}

	@Test
	public void isJSONResponseShouldReturnFalseIfContentTypeIsNotJSON() throws
			Exception {
		JettyServletTester.runServlet((req, resp) -> {
			resp.setContentType("text/html; charset=utf-8");
		}, (baseURL) -> {
			Mech2 mech2 = Mech2.builder().build();
			Mech2Result res = mech2.get(baseURL).execute();
			assertFalse(res.isJSONResponse());
		});
	}

	@Test
	public void isJSONResponseShouldReturnFalseIfContentTypeIsNull() throws Exception {
		JettyServletTester.runServlet((req, resp) -> {
			// Content-Type is empty
		}, (baseURL) -> {
			Mech2 mech2 = Mech2.builder().build();
			Mech2Result res = mech2.get(baseURL).execute();
			assertFalse(res.isJSONResponse());
		});
	}
}

package me.geso.mech2;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;

public class Mech2ResultTest {

	@Test
	public void testStatusCode() throws IOException, URISyntaxException,
			Exception {
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
	public void testContentType() throws IOException, URISyntaxException,
			Exception {
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
	public void isJSONResponseShouldReturnTrueIfContentTypeIsJSON() throws IOException, URISyntaxException, Exception {
		JettyServletTester.runServlet((req, resp) -> {
			resp.setContentType("application/json; charset=utf-8");
		}, (baseURL) -> {
			Mech2 mech2 = Mech2.builder().build();
			Mech2Result res = mech2.get(baseURL).execute();
			assertTrue(res.isJSONResponse());
		});
	}

	@Test
	public void isJSONResponseShouldReturnFalseIfContentTypeIsntJSON() throws URISyntaxException, IOException,
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
	public void isJSONResponseShouldReturnFalseIfContentTypeIsNull() throws URISyntaxException, IOException, Exception {
		JettyServletTester.runServlet((req, resp) -> {
			// Content-Type is empty
		}, (baseURL) -> {
			Mech2 mech2 = Mech2.builder().build();
			Mech2Result res = mech2.get(baseURL).execute();
			assertFalse(res.isJSONResponse());
		});
	}
}

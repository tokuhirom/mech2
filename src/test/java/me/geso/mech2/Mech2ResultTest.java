package me.geso.mech2;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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
			assertThat(res.getContentType().getCharset(), is("utf-8"));
			assertThat(res.getContentType().getMimeType(), is("text/html"));
		});
	}

}

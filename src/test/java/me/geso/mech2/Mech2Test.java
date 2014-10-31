package me.geso.mech2;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.nio.charset.StandardCharsets;

import org.apache.http.util.EntityUtils;
import org.junit.Test;

import lombok.Data;

public class Mech2Test {
	@Test
	public void testGet() throws Exception {
		JettyServletTester.runServlet((req, resp) -> {
			resp.getWriter().print("HOGE");
		}, (baseURL) -> {
			Mech2 mech2 = Mech2.builder().build();
			Mech2Result res = mech2.get(baseURL).execute();
			assertThat(res.getResponse().getStatusLine().getStatusCode(), is(200));
			assertThat(EntityUtils.toString(res.getResponse().getEntity(), StandardCharsets.UTF_8), is("HOGE"));
		});
	}

	@Test
	public void testGetJSON() throws Exception {
		JettyServletTester.runServlet((req, resp) -> {
			resp.getWriter().print("{\"foo\":\"bar\"}");
		}, (baseURL) -> {
			Mech2 mech2 = Mech2.builder().build();
			Mech2Result res = mech2.get(baseURL).execute();
			assertThat(res.getResponse().getStatusLine().getStatusCode(), is(200));
			Mech2JSONResult<Foo> jsonResult = res.toJSONResult(Foo.class);
			assertThat(jsonResult.parseJSON().getFoo(), is("bar"));
		});
	}

	@Test
	public void testPost() throws Exception {
		JettyServletTester.runServlet((req, resp) -> {
			assertThat(req.getMethod(), is("POST"));
			resp.getWriter().print("{\"foo\":\"bar\"}");
		}, (baseURL) -> {
			Mech2 mech2 = Mech2.builder().build();
			Mech2Result res = mech2.post(baseURL).execute();
			assertThat(res.getResponse().getStatusLine().getStatusCode(), is(200));
		});
	}

	@Data
	public static class Foo {
		private String foo;
	}
}

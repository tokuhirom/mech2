package me.geso.mech2;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.nio.charset.StandardCharsets;

import org.apache.http.util.EntityUtils;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

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

	@Test
	public void testPostJSON() throws Exception {
		JettyServletTester.runServlet((req, resp) -> {
			assertThat(req.getMethod(), is("POST"));
			assertEquals("application/json; charset=UTF-8", req.getHeader("content-type"));
			resp.getWriter().print("{\"foo\":\"bar\"}");
		}, (baseURL) -> {
			Mech2 mech2 = Mech2.builder().build();
			Foo foo = new Foo();
			foo.setFoo("bar");
			Mech2Result res = mech2.post(baseURL).setBodyJSON(foo).execute();
			assertThat(res.getResponse().getStatusLine().getStatusCode(), is(200));
		});
	}

	@Test
	public void testHead() throws Exception {
		JettyServletTester.runServlet((req, resp) -> {
			assertThat(req.getMethod(), is("HEAD"));
			resp.setHeader("X-Oreore", "oreore");
		}, (baseURL) -> {
			Mech2 mech2 = Mech2.builder().build();
			Mech2Result res = mech2.head(baseURL).execute();
			assertThat(res.getResponse().getStatusLine().getStatusCode(), is(200));
			assertThat(res.getResponse().getHeaders("x-Oreore")[0].getValue(), is("oreore"));
			assertEquals("Response body should me empty", res.getResponse().getEntity(), null);
		});
	}

	@Test
	public void testPut() throws Exception {
		JettyServletTester.runServlet((req, resp) -> {
			assertThat(req.getMethod(), is("PUT"));
			resp.getWriter().print("{\"foo\":\"bar\"}");
		}, (baseURL) -> {
			Mech2 mech2 = Mech2.builder().build();
			Mech2Result res = mech2.put(baseURL).execute();
			assertThat(res.getResponse().getStatusLine().getStatusCode(), is(200));
		});
	}

	@Test
	public void testPutJSON() throws Exception {
		JettyServletTester.runServlet((req, resp) -> {
			assertThat(req.getMethod(), is("PUT"));
			assertEquals("application/json; charset=UTF-8", req.getHeader("content-type"));
			resp.getWriter().print("{\"foo\":\"bar\"}");
		}, (baseURL) -> {
			Mech2 mech2 = Mech2.builder().build();
			Foo foo = new Foo();
			foo.setFoo("bar");
			Mech2Result res = mech2.put(baseURL).setBodyJSON(foo).execute();
			assertThat(res.getResponse().getStatusLine().getStatusCode(), is(200));
		});
	}

	@Test(expected = JsonProcessingException.class)
	public void testPutMalformedJSON() throws Exception {
		JettyServletTester.runServlet((req, resp) -> {
			assertThat(req.getMethod(), is("PUT"));
		}, (baseURL) -> {
			Mech2 mech2 = Mech2.builder().build();
			mech2.put(baseURL).setBodyJSON(new Object() {
			}).execute(); // <= malformed!
		});
	}

	@Test
	public void testDelete() throws Exception {
		JettyServletTester.runServlet((req, resp) -> {
			assertThat(req.getMethod(), is("DELETE"));
			resp.getWriter().print("HOGE");
		}, (baseURL) -> {
			Mech2 mech2 = Mech2.builder().build();
			Mech2Result res = mech2.delete(baseURL).execute();
			assertThat(res.getResponse().getStatusLine().getStatusCode(), is(200));
		});
	}

	@Data
	public static class Foo {
		private String foo;
	}
}

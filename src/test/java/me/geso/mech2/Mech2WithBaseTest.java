package me.geso.mech2;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import lombok.AllArgsConstructor;
import lombok.Data;

public class Mech2WithBaseTest {

	@Test
	public void testGet() throws Exception {
		JettyServletTester.runServlet((req, resp) -> {
			resp.getWriter().write("HAHAHA");
		}, (baseURI) -> {
			Mech2 mech2 = Mech2.builder().build();
			Mech2WithBase wb = new Mech2WithBase(mech2, baseURI);
			assertThat(wb.get("/").execute().getResponse().getStatusLine().getStatusCode(), is(200));
			assertThat(wb.get("/").execute().getResponseBodyAsString(), is("HAHAHA"));
		});

	}

	@Test
	public void testPostJSON() throws Exception {
		JettyServletTester.runServlet((req, resp) -> {
			assertEquals("application/json; charset=UTF-8", req.getHeader("Content-Type"));
			resp.getWriter().write("HAHAHA");
		}, (baseURI) -> {
			Mech2 mech2 = Mech2.builder().build();
			Mech2WithBase wb = new Mech2WithBase(mech2, baseURI);
			Mech2Result result = wb.postJSON("/", new Foo("ooo")).execute();
			assertEquals(200, result.getResponse().getStatusLine().getStatusCode());
		});
	}

	@Data
	@AllArgsConstructor
	public static class Foo {
		private String bar;
	}

}

package me.geso.mech2;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
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

	@Test
	public void testPostMultipart() throws Exception {
		JettyServletTester.runServlet((req, resp) -> {
			try {
				assertTrue(req.getHeader("Content-Type").startsWith("multipart/form-data"));
				assertTrue(ServletFileUpload.isMultipartContent(req));
				final FileItemFactory fileItemFactory = new DiskFileItemFactory();
				ServletFileUpload servletFileUpload = new ServletFileUpload(fileItemFactory);
				FileItemIterator itemIterator = servletFileUpload.getItemIterator(req);
				while (itemIterator.hasNext()) {
					FileItemStream next = itemIterator.next();
					System.out.println(next.getContentType());
				}
				resp.getWriter().write("HAHAHA");
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}, (baseURI) -> {
			Mech2 mech2 = Mech2.builder().build();
			Mech2WithBase wb = new Mech2WithBase(mech2, baseURI);
			Mech2Result result = wb.postMultipart("/")
				.addBinaryBody("hoge", new File("README.md"))
				.addTextBody("fuga", "iyan")
				.execute();
			assertEquals(200, result.getResponse().getStatusLine().getStatusCode());
		});
	}

	@Data
	@AllArgsConstructor
	public static class Foo {
		private String bar;
	}

}

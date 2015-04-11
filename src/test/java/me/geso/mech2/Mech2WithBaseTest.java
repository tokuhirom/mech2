package me.geso.mech2;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.servlet.ServletHolder;
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
				long count = 0;
				while (itemIterator.hasNext()) {
					count++;
					itemIterator.next();
				}
				assertEquals(2L, count);
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

		final ServletHolder servletHolder = new ServletHolder(new MultiPartServlet());
		String tempDir = System.getProperty("java.io.tmpdir");
		servletHolder.getRegistration().setMultipartConfig(new MultipartConfigElement(tempDir));
		JettyServletTester.runServlet(servletHolder, (baseURI) -> {
			Mech2 mech2 = Mech2.builder().build();
			Mech2WithBase wb = new Mech2WithBase(mech2, baseURI);
			Mech2Result result = wb.postMultipart("/")
				.addBinaryBody("hoge", new File("README.md"))
				.addTextBody("fuga", "iyan")
				.execute();
			assertEquals(200, result.getResponse().getStatusLine().getStatusCode());
		});
	}

	public static class MultiPartServlet extends HttpServlet {
		private static final long serialVersionUID = 1L;
		protected void doPost(HttpServletRequest req, HttpServletResponse resp)
				throws ServletException, IOException
		{
			assertTrue(req.getHeader("Content-Type").startsWith("multipart/form-data"));
			assertTrue(ServletFileUpload.isMultipartContent(req));
			final List<Part> parts = new ArrayList<>(req.getParts());
			assertEquals(2, parts.size());
			assertEquals("application/octet-stream", parts.get(0).getContentType());
			assertEquals("hoge", parts.get(0).getName());
			assertTrue(IOUtils.toString(parts.get(0).getInputStream(), StandardCharsets.UTF_8.displayName()).contains("SYNOPSIS"));
			assertEquals("text/plain; charset=UTF-8", parts.get(1).getContentType());
			assertEquals("fuga", parts.get(1).getName());
			assertEquals("iyan", IOUtils.toString(parts.get(1).getInputStream(), StandardCharsets.UTF_8.displayName()));
			resp.setStatus(200);
			resp.getWriter().write("OK");
		}
	}

	@Data
	@AllArgsConstructor
	public static class Foo {
		private String bar;
	}

}

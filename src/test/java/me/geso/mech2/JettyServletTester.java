package me.geso.mech2;

import java.io.IOException;
import java.net.URI;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.Ignore;

@Ignore
public class JettyServletTester {
	@FunctionalInterface
	public interface ServletCallback {
		void service(HttpServletRequest req, HttpServletResponse resp)
				throws ServletException, IOException;
	}

	@FunctionalInterface
	public interface URIConsumer {
		void accept(URI body)
				throws Exception;
	}

	public static void runServlet(Class<? extends Servlet> servletClass, URIConsumer body) throws Exception {
		runServlet(new ServletHolder(servletClass), body);
	}

	public static void runServlet(Servlet servlet, URIConsumer body) throws Exception {
		runServlet(new ServletHolder(servlet), body);
	}

	public static void runServlet(ServletHolder servletHolder, URIConsumer body) throws Exception {
		try (JettyServletRunner jettyServletRunner = new JettyServletRunner(servletHolder)) {
			body.accept(jettyServletRunner.getBaseURI());
		}
	}

	public static void runServlet(ServletCallback callback, URIConsumer body) throws Exception {
		ServletHolder servletHolder = new ServletHolder(new HttpServlet() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void service(HttpServletRequest req, HttpServletResponse resp)
					throws ServletException, IOException {
				callback.service(req, resp);
			}
		});
		runServlet(servletHolder, body);
	}

}

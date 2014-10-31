package me.geso.mech2;

import java.net.URI;

import javax.servlet.Servlet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class JettyServletRunner implements AutoCloseable {
	private final URI baseURI;
	private final Server server;

	public JettyServletRunner(Class<? extends Servlet> servletClass) throws Exception {
		this(new ServletHolder(servletClass));
	}

	public JettyServletRunner(Servlet servlet) throws Exception {
		this(new ServletHolder(servlet));
	}

	public JettyServletRunner(ServletHolder servletHolder) throws Exception {
		this.server = new Server(0);
		ServletContextHandler context = new ServletContextHandler(
			server,
			"/",
			ServletContextHandler.SESSIONS
				);
		context.addServlet(servletHolder, "/*");
		server.setStopAtShutdown(true);
		server.start();
		ServerConnector connector = (ServerConnector)server
			.getConnectors()[0];
		int port = connector.getLocalPort();
		this.baseURI = new URI("http://127.0.0.1:" + port);
	}

	@Override
	public void close() throws Exception {
		if (this.server != null) {
			this.server.stop();
		}
	}

	public URI getBaseURI() {
		return this.baseURI;
	}
}

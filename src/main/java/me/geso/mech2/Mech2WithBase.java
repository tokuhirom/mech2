package me.geso.mech2;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Mech2 object with base URI.
 * <p>
 * This class is useful when
 * <ul>
 * <li>to write Restful HTTP API client.</li>
 * <li>testing with embedded jetty</li>
 * </ul>
 * 
 * @author tokuhirom
 *
 */
public class Mech2WithBase {
	private final Mech2 mech2;
	private final URI baseURI;

	public Mech2WithBase(Mech2 mech2, URI baseURI) {
		this.mech2 = mech2;
		this.baseURI = baseURI;
	}

	/**
	 * Get a mech2 instance.
	 * 
	 * @return mech2 instance
	 */
	public Mech2 getMech2() {
		return this.mech2;
	}

	/**
	 * Get base URI
	 * 
	 * @return base URI object
	 */
	public URI getBaseURI() {
		return this.baseURI;
	}

	/**
	 * Create new GET request.
	 *
	 * @param path request path
	 * @return Generated request object.
	 * @throws URISyntaxException
	 */
	public Mech2Request get(String path) throws URISyntaxException {
		URIBuilder uriBuilder = new URIBuilder(this.baseURI)
			.setPath(path);
		return new Mech2Request(this.getMech2(), uriBuilder, new HttpGet());
	}

	/**
	 * Create new GET request using {@link String#format(String, Object...)}.
	 * <p>
	 * It's same as
	 * <pre><code>mech.get(String.format("/member/%s", member.getId())</code></pre>
	 * . But readable.
	 * 
	 * @param pathFormat path format
	 * @param args parameters.
	 * @return request object
	 * @throws URISyntaxException
	 */
	public Mech2Request getf(String pathFormat, Object... args)
			throws URISyntaxException {
		String path = String.format(pathFormat, args);
		return this.get(path);
	}

	/**
	 * Disable redirect handling.
	 *
	 * @return fluent
	 */
	public Mech2WithBase disableRedirectHandling() {
		this.mech2.disableRedirectHandling();
		return this;
	}

	/**
	 * Create new POST request.
	 * 
	 * @param path path
	 * @return request object
	 * @throws URISyntaxException
	 */
	public Mech2Request post(String path)
			throws URISyntaxException {
		URIBuilder uriBuilder = new URIBuilder(this.baseURI)
			.setPath(path);
		return new Mech2Request(this.getMech2(), uriBuilder, new HttpPost());
	}

	/**
	 * Create new POST request contains JSON.
	 * 
	 * @param path path to request
	 * @param data
	 *            source of JSON. It'll serialize by jackson.
	 * @return request object
	 * @throws URISyntaxException
	 * @throws JsonProcessingException
	 */
	public Mech2Request postJSON(String path, Object data)
			throws URISyntaxException, JsonProcessingException {
		URIBuilder uriBuilder = new URIBuilder(this.baseURI)
			.setPath(path);
		Mech2Request mech2Request = new Mech2Request(this.getMech2(),
			uriBuilder, new HttpPost());
		mech2Request.setBodyJSON(data);
		return mech2Request;
	}

	/**
	 * Create multi-part POST request(Charset is UTF-8).
	 * 
	 * @param path path to request
	 * @return request object
	 */
	public Mech2RequestMultipart postMultipart(String path) {
		return this.postMultipart(path, StandardCharsets.UTF_8);
	}

	/**
	 * Create new multi-part POST request.
	 * 
	 * @param path path to request
	 * @param charset charset
	 * @return request object
	 */
	public Mech2RequestMultipart postMultipart(String path, Charset charset) {
		URIBuilder uriBuilder = new URIBuilder(this.baseURI)
			.setPath(path);
		HttpPost httpPost = new HttpPost();
		return new Mech2RequestMultipart(this.getMech2(), uriBuilder, httpPost,
			charset);
	}

}

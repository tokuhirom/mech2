package me.geso.mech2;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;

public class Mech2WithBase {
	private final Mech2 mech2;
	private final URI baseURI;

	public Mech2WithBase(Mech2 mech2, URI baseURI) {
		this.mech2 = mech2;
		this.baseURI = baseURI;
	}

	public Mech2 getMech2() {
		return this.mech2;
	}

	public URI getBaseURI() {
		return this.baseURI;
	}

	public Mech2Request get(String path) throws URISyntaxException {
		URIBuilder uriBuilder = new URIBuilder(this.baseURI)
			.setPath(path);
		return new Mech2Request(this.getMech2(), uriBuilder, new HttpGet());
	}

	public Mech2Request getf(String pathFormat, Object... args)
			throws URISyntaxException {
		String path = String.format(pathFormat, args);
		URIBuilder uriBuilder = new URIBuilder(this.baseURI)
			.setPath(path);
		return new Mech2Request(this.getMech2(),
			uriBuilder, new HttpGet());
	}

	/**
	 * Disable redirect handling.
	 *
	 * @return
	 */
	public Mech2WithBase disableRedirectHandling() {
		this.mech2.disableRedirectHandling();
		return this;
	}

	public Mech2Request post(String path)
			throws URISyntaxException {
		URIBuilder uriBuilder = new URIBuilder(this.baseURI)
			.setPath(path);
		return new Mech2Request(this.getMech2(), uriBuilder, new HttpPost());
	}

	public Mech2Request postJSON(String path, Object data)
			throws URISyntaxException, JsonProcessingException {
		URIBuilder uriBuilder = new URIBuilder(this.baseURI)
			.setPath(path);
		Mech2Request mech2Request = new Mech2Request(this.getMech2(), uriBuilder, new HttpPost());
		mech2Request.setBodyJSON(data);
		return mech2Request;
	}

}

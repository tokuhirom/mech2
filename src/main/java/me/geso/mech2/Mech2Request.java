package me.geso.mech2;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;

import com.fasterxml.jackson.core.JsonProcessingException;

public class Mech2Request {
	private final Mech2 mech2;
	private final URIBuilder uriBuilder;
	private final HttpRequestBase request;

	public Mech2Request(Mech2 mech2, URIBuilder uriBuilder, HttpRequestBase request) {
		this.mech2 = mech2;
		this.uriBuilder = uriBuilder;
		this.request = request;
	}

	public Mech2Result execute() throws URISyntaxException, IOException {
		this.request.setURI(this.uriBuilder.build());
		return this.mech2.request(request);
	}

	public Mech2Request addParameter(String param, String value) {
		this.uriBuilder.addParameter(param, value);
		return this;
	}

	public Mech2Request addHeader(String name, String value) {
		this.request.addHeader(name, value);
		return this;
	}

	public Mech2Request setHeader(String name, String value) {
		this.request.setHeader(name, value);
		return this;
	}

	public Mech2Request setBody(HttpEntity entity) {
		if (this.request instanceof HttpEntityEnclosingRequest) {
			((HttpEntityEnclosingRequest)this.request).setEntity(entity);
			return this;
		} else {
			throw new IllegalStateException(this.request.getMethod()
				+ " request cannot enclose an entity");
		}
	}

	public Mech2Request setBodyJSON(Object form) throws JsonProcessingException {
		byte[] body = this.mech2.getObjectMapper().writeValueAsBytes(form);
		this.setBody(new ByteArrayEntity(body));
		return this;
	}
}

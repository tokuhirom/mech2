package me.geso.mech2;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * The class represents HttpRequest.
 *
 */
public class Mech2Request {
	private final Mech2 mech2;
	private final URIBuilder uriBuilder;
	private final HttpRequestBase request;

	/**
	 * Normally, you shouldn't call this directly. Use
	 * {@link Mech2#get(java.net.URI)}, etc instead.
	 * 
	 * @param mech2 mech2 instance.
	 * @param uriBuilder URI builder
	 * @param request request object
	 */
	public Mech2Request(Mech2 mech2, URIBuilder uriBuilder, HttpRequestBase request) {
		this.mech2 = mech2;
		this.uriBuilder = uriBuilder;
		this.request = request;
	}

	/**
	 * Execute request.
	 * 
	 * @return executed result
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public Mech2Result execute() throws URISyntaxException, IOException {
		this.request.setURI(this.uriBuilder.build());
		return this.mech2.request(request);
	}

	@Deprecated
	public Mech2Request addParameter(String param, String value) {
		return this.addQueryParameter(param, value);
	}

	/**
	 * Add query parameter to the URI
	 * 
	 * @param param parameter key
	 * @param value parameter value
	 * @return request object
	 */
	public Mech2Request addQueryParameter(String param, String value) {
		this.uriBuilder.addParameter(param, value);
		return this;
	}

	/**
	 * Set query parameter to the URI
	 * 
	 * @param param parameter name
	 * @param value parameter value
	 * @return request object
	 */
	public Mech2Request setQueryParameter(String param, String value) {
		this.uriBuilder.setParameter(param, value);
		return this;
	}

	/**
	 * Add header to the request.
	 * 
	 * @param name header name
	 * @param value header value
	 * @return fluent
	 */
	public Mech2Request addHeader(String name, String value) {
		this.request.addHeader(name, value);
		return this;
	}

	/**
	 * Set header to the request.
	 * 
	 * @param name header name
	 * @param value header value
	 * @return fluent
	 */
	public Mech2Request setHeader(String name, String value) {
		this.request.setHeader(name, value);
		return this;
	}

	/**
	 * Set HttpEntity object to the request.
	 * 
	 * @param entity entity object
	 * @return fluent
	 */
	public Mech2Request setBody(HttpEntity entity) {
		if (this.request instanceof HttpEntityEnclosingRequest) {
			((HttpEntityEnclosingRequest)this.request).setEntity(entity);
			return this;
		} else {
			throw new IllegalStateException(this.request.getMethod()
				+ " request cannot enclose an entity");
		}
	}

	/**
	 * Set JSON value as the entity body.<br>
	 * This method uses jackson for serializing.
	 *
	 * @param form json object. It will serialize by Jackson.
	 * @return request object
	 * @throws JsonProcessingException
	 */
	public Mech2Request setBodyJSON(Object form) throws JsonProcessingException {
		byte[] body = this.mech2.getObjectMapper().writeValueAsBytes(form);
		ByteArrayEntity entity = new ByteArrayEntity(body);
		entity.setContentType("application/json; charset=utf-8");
		this.setBody(entity);
		return this;
	}
}

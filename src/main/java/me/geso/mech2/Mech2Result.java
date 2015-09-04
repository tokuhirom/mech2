package me.geso.mech2;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * HTTP request result.<br>
 * <p>
 * It contains HttpRequest object and HttpResponse object.
 *
 * @author tokuhirom
 *
 */
public class Mech2Result {
	private final HttpRequest request;
	private final HttpResponse response;
	private final Mech2 mech2;

	Mech2Result(HttpRequest request, HttpResponse response, Mech2 mech2) {
		this.request = request;
		this.response = response;
		this.mech2 = mech2;

		if (request instanceof HttpHead) {
			// Response of "HEAD" method doesn't have any body
			return;
		}

		try {
			byte[] body = EntityUtils.toByteArray(response.getEntity());
			ByteArrayEntity entity = new ByteArrayEntity(body);
			entity.setContentType(response.getFirstHeader("Content-Type"));
			response.setEntity(entity);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	/**
	 * Get HttpRequest object.
	 * 
	 * @return request
	 */
	public HttpRequest getRequest() {
		return this.request;
	}

	/**
	 * Get HttpResponse object.
	 * 
	 * @return response
	 */
	public HttpResponse getResponse() {
		return this.response;
	}

	/**
	 * Returns true if the HttResponse has 2xx status code. False otherwise.
	 * 
	 * @return true if response code is 2XX, false otherwise.
	 */
	public boolean isSuccess() {
		int statusCode = this.response.getStatusLine().getStatusCode();
		return 200 <= statusCode && statusCode < 300;
	}

	/**
	 * Returns true if Content-Type of response is "application/json". Otherwise, returns false.
	 * 
	 * @return true if the response's content-type is json-ish, false otherwise.
	 */
	public boolean isJSONResponse() {
		Header contentTypeHeader = response.getFirstHeader("Content-Type");
		if (contentTypeHeader == null) {
			return false;
		}

		String responseMime = ContentType.parse(contentTypeHeader.getValue()).getMimeType();
		return ContentType.APPLICATION_JSON.getMimeType().equalsIgnoreCase(responseMime);
	}

	/**
	 * Convert the result to Mech2JSONResult object. It contains this object and
	 * JSON type information.
	 * 
	 * @param valueType type of JSON value
	 * @return result object with JSON type information.
	 */
	public <T> Mech2JSONResult<T> toJSONResult(Class<T> valueType) {
		return Mech2JSONResult.of(this, valueType);
	}

	/**
	 * Convert the result to Mech2JSONResult object. It contains this object and
	 * JSON type information.
	 * 
	 * @param klass type of JSON value
	 * @return result object with JSON type information
	 */
	public <T> Mech2JSONResult<T> toJSONResult(TypeReference<T> klass) {
		return Mech2JSONResult.of(this, klass);
	}

	/**
	 * Parse JSON from content-body using jackson.
	 * 
	 * @param valueType type of json value
	 * @return parsed result
	 * @throws IOException
	 */
	public <T> T parseJSON(TypeReference<T> valueType) throws IOException {
		return this.mech2.getObjectMapper().readValue(
			this.getResponse().getEntity().getContent(), valueType);
	}

	/**
	 * Parse JSON from content-body using jackson.
	 * 
	 * @param valueType type of json value
	 * @return parsed result
	 * @throws IOException
	 */
	public <T> T parseJSON(Class<T> valueType) throws IOException {
		return this.mech2.getObjectMapper().readValue(
			this.getResponse().getEntity().getContent(), valueType);
	}

	/**
	 * throw exception if the response doesn't have 2xx response.
	 *
	 * @throws Mech2FailException
	 */
	public Mech2Result orDie() throws Mech2FailException {
		if (this.isSuccess()) {
			return this;
		} else {
			throw new Mech2FailException(this);
		}
	}

	/**
	 * Get the HTTP response as String.
	 * 
	 * @return string representation of content body
	 * @throws ParseException
	 * @throws IOException
	 */
	public String getResponseBodyAsString() throws ParseException, IOException {
		ContentType contentType = ContentType.getOrDefault(this.response
			.getEntity());
		Charset charset = contentType.getCharset() == null ? StandardCharsets.ISO_8859_1
			: contentType.getCharset();
		return EntityUtils.toString(this.response.getEntity(), charset);
	}

	/**
	 * Shorthand for {@code mech.getResponse().getStatusLine().getStatusCode()}
	 * 
	 * @return status code.
	 */
	public int getStatusCode() {
		return this.getResponse().getStatusLine().getStatusCode();
	}

	/**
	 * Get ContentType object from the response.
	 * 
	 * @return content type object.
	 */
	public ContentType getContentType() {
		Header header = this.getResponse()
			.getFirstHeader("Content-Type");
		return ContentType.parse(header != null ? header.getValue() : "");
	}
}

package me.geso.mech2;

import java.io.IOException;
import java.net.URI;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.experimental.Accessors;

/**
 * Main class of the mech2 package. Yet another HTTP client library based on
 * Apache HttpClient.
 * 
 * <pre>
 * 	<code>
 * 	Mech2 mech = Mech2.builder().build();
 *  Mech2Result res = mech.get("/").execute();
 *  System.out.println(res.getResponseBodyAsString());
 * </code>
 * </pre>
 */
public class Mech2 {
	private static final Logger LOGGER = LoggerFactory.getLogger(Mech2.class);

	private final HttpClientBuilder httpClientBuilder;
	private final ObjectMapper objectMapper;

	private Mech2(HttpClientBuilder clientBuilder, ObjectMapper objectMapper) {
		this.httpClientBuilder = clientBuilder;
		this.objectMapper = objectMapper;
	}

	/**
	 * Get the builder object.
	 *
	 * @return Builder object
	 */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Create new GET request object.
	 *
	 * @param uri URI to request
	 * @return Request object
	 */
	public Mech2Request get(URI uri) {
		return new Mech2Request(this, new URIBuilder(uri), new HttpGet());
	}

	/**
	 * Create new POST request object.
	 *
	 * @param uri URI to request
	 * @return request object
	 */
	public Mech2Request post(URI uri) {
		return new Mech2Request(this, new URIBuilder(uri), new HttpPost());
	}

	/**
	 * Create new PUT request object.
	 *
	 * @param uri URI to request
	 * @return request object
	 */
	public Mech2Request put(URI uri) {
		return new Mech2Request(this, new URIBuilder(uri), new HttpPut());
	}

	/**
	 * Create new DELETE request object.
	 *
	 * @param uri URI object
	 * @return created request object.
	 */
	public Mech2Request delete(URI uri) {
		return new Mech2Request(this, new URIBuilder(uri), new HttpDelete());
	}

	/**
	 * Create new HEAD request object.
	 *
	 * @param uri uri
	 * @return created request object.
	 */
	public Mech2Request head(URI uri) {
		return new Mech2Request(this, new URIBuilder(uri), new HttpHead());
	}

	/**
	 * Disable redirect handling.
	 *
	 * @return Fluent
	 */
	public Mech2 disableRedirectHandling() {
		this.httpClientBuilder.disableRedirectHandling();
		return this;
	}

	/**
	 * Send HTTP request by HttpUriRequest.
	 *
	 * @param request instance of HttpUriRequest
	 * @return result object.
	 * @throws IOException
	 */
	public Mech2Result request(HttpUriRequest request) throws IOException {
		long startedOn = System.currentTimeMillis();
		try (CloseableHttpClient client = this.httpClientBuilder.build()) {
			try (CloseableHttpResponse resp = client.execute(request)) {
				LOGGER.info("{}: {}, {} secs", request.getURI(),
					resp.getStatusLine().toString(),
					(System.currentTimeMillis() - startedOn) / 1000.0);
				return new Mech2Result(request, resp, this);
			}
		}
	}

	/**
	 * Get the current HttpClientBuilder object.<br>
	 * You can set parameter for this object.
	 *
	 * @return HttpClientBuilder object.
	 */
	public HttpClientBuilder getHttpClientBuilder() {
		return this.httpClientBuilder;
	}

	/**
	 * Get the jackson's ObjectMapper object.<br>
	 * You can configure the parameters.
	 *
	 * @return Jackson's ObjectMapper object
	 */
	public ObjectMapper getObjectMapper() {
		return this.objectMapper;
	}

	@Accessors(fluent = true)
	public static class Builder {
		private HttpClientBuilder httpClientBuilder;
		private ObjectMapper objectMapper;

		private Builder() {
			this.httpClientBuilder = HttpClientBuilder.create();
			this.objectMapper = new ObjectMapper();
			this.objectMapper.configure(
				DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		}

		public Builder setObjectMapper(final ObjectMapper objectMapper) {
			this.objectMapper = objectMapper;
			return this;
		}

		public Builder setHttpClientBuilder(final HttpClientBuilder httpClientBuilder) {
			this.httpClientBuilder = httpClientBuilder;
			return this;
		}

		public Mech2 build() {
			return new Mech2(this.httpClientBuilder, this.objectMapper);
		}
	}

}

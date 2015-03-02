package me.geso.mech2;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;

/**
 * This is a HTTP request class, has a {@code multipart/form-data} content-type.
 */
public class Mech2RequestMultipart extends Mech2Request {
	private final MultipartEntityBuilder builder;
	private final Charset charset;

	/**
	 * Do not use this directly.
	 * 
	 * @param mech2 mech2 object
	 * @param uriBuilder uri builder
	 * @param request request object
	 * @param charset character set
	 */
	public Mech2RequestMultipart(Mech2 mech2, URIBuilder uriBuilder, HttpRequestBase request, Charset charset) {
		super(mech2, uriBuilder, request);
		this.charset = charset;
		this.builder = MultipartEntityBuilder.create().setCharset(charset);
	}

	/**
	 * Add binary body from byte array.
	 * 
	 * @param name parameter name
	 * @param file parameter value
	 * @return fluent
	 */
	public Mech2RequestMultipart addBinaryBody(String name, byte[] file) {
		this.builder.addBinaryBody(name, file);
		return this;
	}

	/**
	 * Add binary body from the file.
	 * 
	 * @param name parameter name
	 * @param file file object
	 * @return fluent
	 */
	public Mech2RequestMultipart addBinaryBody(String name, File file) {
		this.builder.addBinaryBody(name, file);
		return this;
	}

	/**
	 * Add binary body from the file using specific content-type.
	 * 
	 * @param name parameter ame
	 * @param file file object
	 * @param contentType content-type
	 * @param filename file name
	 * @return fluent.
	 */
	public Mech2RequestMultipart addBinaryBody(String name, File file,
			ContentType contentType, String filename) {
		this.builder.addBinaryBody(name, file, contentType, filename);
		return this;
	}

	/**
	 * Add text body
	 *
	 * @param name parameter name
	 * @param text value
	 * @return fluent.
	 */
	public Mech2RequestMultipart addTextBody(String name, String text) {
		this.builder.addTextBody(name, text, ContentType.create("text/plain", charset));
		return this;
	}

	/**
	 * Execute HTTP request.
	 */
	@Override
	public Mech2Result execute() throws URISyntaxException, IOException {
		this.setBody(this.builder.build());
		return super.execute();
	}

}

package me.geso.mech2;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;

public class Mech2RequestMultipart extends Mech2Request {
	private final MultipartEntityBuilder builder;
	private final Charset charset;

	public Mech2RequestMultipart(Mech2 mech2, URIBuilder uriBuilder, HttpRequestBase request, Charset charset) {
		super(mech2, uriBuilder, request);
		this.charset = charset;
		this.builder = MultipartEntityBuilder.create().setCharset(charset);
	}

	public Mech2RequestMultipart addBinaryBody(String name, byte[] file) {
		this.builder.addBinaryBody(name, file);
		return this;
	}

	public Mech2RequestMultipart addBinaryBody(String name, File file) {
		this.builder.addBinaryBody(name, file);
		return this;
	}

	public Mech2RequestMultipart addTextBody(String name, String text) {
		this.builder.addTextBody(name, text, ContentType.create("text/plain", charset));
		return this;
	}

	@Override
	public Mech2Result execute() throws URISyntaxException, IOException {
		this.setBody(this.builder.build());
		return super.execute();
	}

}

package com.elekscamp.messenger_javafx_client.DAL;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HttpRequest {

	public enum HttpMethod {
		GET, PUT, POST, DELETE
	};

	private HttpMethod method;
	private URL url;
	private HttpURLConnection conn;
	private int responseCode;
	private String responseMessage;
	private String response;
	private String body;
	private Map<String, String> header;

	public HttpRequest() { }

	public String execute(String urlStr, HttpMethod method, Map<String, String> header, String body)
			throws IOException {

		this.method = method;
		this.header = header;
		this.body = body;
		System.out.println(urlStr + " body: " + body);
		responseCode = -1;

		url = new URL(urlStr);
		conn = (HttpURLConnection) url.openConnection();

		configureConnection();

		if (conn.getDoOutput())
			writeOutput();

		readInput();

		conn.disconnect();
		System.out.println("response: " + response);
		return new String(response.getBytes(), "UTF-8");
	}

	private void configureConnection() throws ProtocolException {

		conn.setRequestMethod(method.toString());

		if (header != null) {
			for (Map.Entry<String, String> each : header.entrySet()) {
				conn.addRequestProperty(each.getKey(), each.getValue());
			}
		}

		if (method == HttpMethod.POST || method == HttpMethod.PUT) {
			conn.setDoOutput(true);
		} else {
			conn.setDoOutput(false);
		}
	}

	private void writeOutput() throws IOException {

		if (body != null && !body.isEmpty() && conn.getDoOutput()) {
			try (DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream())) {
				outputStream.write(body.getBytes(StandardCharsets.UTF_8));
			}
		}
	}

	private void readInput() throws IOException {

		responseCode = conn.getResponseCode();
		responseMessage = conn.getResponseMessage();
		response = "";

		InputStream inputStream = responseCode == 400 ? conn.getErrorStream() : conn.getInputStream();

		try (BufferedReader bufReader = new BufferedReader(new InputStreamReader(inputStream))) {
			String readerLine;
			while ((readerLine = bufReader.readLine()) != null) {
				response += readerLine;
			}
		}
	}

	public HttpMethod getMethod() {
		return method;
	}

	public String getUrl() {
		return url.toString();
	}

	public int getResponceCode() {
		return responseCode;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public String getResponse() {
		return response;
	}

	public String getBody() {
		return body;
	}

	public Map<String, String> getHeader() {
		return header;
	}
}
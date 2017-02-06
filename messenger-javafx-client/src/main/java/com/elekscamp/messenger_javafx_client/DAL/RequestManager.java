package com.elekscamp.messenger_javafx_client.DAL;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.ObjectMapper;

import com.elekscamp.messenger_javafx_client.DAL.HttpRequest.HttpMethod;
import com.elekscamp.messenger_javafx_client.Entities.Credential;
import com.elekscamp.messenger_javafx_client.Entities.Token;
import com.elekscamp.messenger_javafx_client.Entities.User;
import com.elekscamp.messenger_javafx_client.Exceptions.ExceptionResolver;
import com.elekscamp.messenger_javafx_client.Exceptions.HttpErrorCodeException;

public class RequestManager <T> implements RequestService<T> {
	
	private static String requestApi;
	private static Map<String, String> header;
	private static Token token;
	
	private final Class<T> type;
	
	private HttpRequest request;
	private ObjectMapper objectMapper;
	private String response;
	private JsonFactory jsonFactory;
	private JsonParser jsonParser;
	private ExceptionResolver exceptionResolver;
	private T item;
	
	public RequestManager(Class<T> type) {
		
		this.type = type;
		request = new HttpRequest();
		objectMapper = new ObjectMapper();
		jsonFactory = new JsonFactory();
		exceptionResolver = new ExceptionResolver();
		
		if (header == null) header = new HashMap<String, String>();
	}
	
	public RequestManager(String requestApi, Class<T> type) {
		
		this(type);
		RequestManager.requestApi = requestApi;
	}
	
	public static void setRequestApi(String requestApi) {
		RequestManager.requestApi = requestApi;
	}
	
	public static String getRequestApi() {
		return RequestManager.requestApi;
	}
	
	public static User authenticateUser(Credential credential) throws HttpErrorCodeException, IOException {
		
		if (header == null) header = new HashMap<String, String>();
		HttpRequest request = new HttpRequest();
		ObjectMapper objectMapper = new ObjectMapper();
		User user;
		
		header.put("Content-Type", "application/x-www-form-urlencoded");
		
		String response;
		try {
			response = request.execute(requestApi.concat("/token"), HttpMethod.POST, header, credential.toString());
			token = objectMapper.readValue(response, Token.class);
			header.put("Authorization", "Bearer ".concat(token.getAccessToken()));
			response = request.execute(requestApi.concat("/users/identifyUser"), HttpMethod.GET, header, "");
			user = objectMapper.readValue(response, User.class);
		} catch (IOException e) {
			int responseCode = request.getResponceCode();
			if (responseCode == 400) throw new HttpErrorCodeException("Incorrect Login or Password.");
			
			throw e;
		}
		
		return user;
	}
	
	public List<T> get(String path) throws HttpErrorCodeException, IOException {

		List<T> entitiesList = new ArrayList<T>();
		
		try {
			response = request.execute(requestApi.concat(path), HttpMethod.GET, header, "");
	
			jsonParser = jsonFactory.createJsonParser(response);
			jsonParser.nextToken();
			while (jsonParser.nextToken() == JsonToken.START_OBJECT) {
				T item = objectMapper.readValue(jsonParser, type);
				entitiesList.add(item);
			}
		} catch (IOException e) {
			exceptionResolver.checkForErrorCodeException(request.getResponceCode(), request.getResponseMessage());
			throw e;
		}
		
		return entitiesList;
	}
	
	public List<T> get(String path, Integer page, Integer pageSize) throws HttpErrorCodeException, IOException {
		
		String pagination = "Pagination";
		List<T> entitiesList;
		
		header.put(pagination, page + "," + pageSize);
		entitiesList = get(path);
		header.remove(pagination);
		
		return entitiesList;
	}
	
	public T get(String path, Integer id) throws HttpErrorCodeException, IOException {
		
		try {
			response = request.execute(requestApi + path + "/" + id.toString(), HttpMethod.GET, header, "");
			item = objectMapper.readValue(response, type);
		} catch (IOException e) {
			exceptionResolver.checkForErrorCodeException(request.getResponceCode(), request.getResponseMessage());
			throw e;
		}	
		
		return item;
	}
	
	public void getFile(String path, Integer id) throws HttpErrorCodeException, IOException {
		
		try {
			response = request.execute(requestApi + path + "/" + id.toString(), HttpMethod.GET, header, "");
			System.out.println("file responce: " + response);
			item = objectMapper.readValue(response, type);
		} catch (IOException e) {
			exceptionResolver.checkForErrorCodeException(request.getResponceCode(), request.getResponseMessage());
			throw e;
		}	
	}
	
	public String post(String path, T item) throws HttpErrorCodeException, IOException {
		
		header.put("Content-Type", "application/json");
		String itemJson = objectMapper.writeValueAsString(item);
		
		try {
			response = request.execute(requestApi.concat(path), HttpMethod.POST, header, itemJson);
			if (request.getResponceCode() == 400) 
				throw new HttpErrorCodeException(response);
		} catch (IOException e) {
			exceptionResolver.checkForErrorCodeException(request.getResponceCode(), request.getResponseMessage());
			throw e;
		}	
		
		return response;
	}
	
	public String post(String path, File data) throws IOException {
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost(requestApi + path);

            FileBody file = new FileBody(data);
            StringBody comment = new StringBody("A binary file of some kind", ContentType.TEXT_PLAIN);

            HttpEntity httpEntity = MultipartEntityBuilder.create()
            		.setMode(HttpMultipartMode.RFC6532)
                    .addPart("files", file)
                    .addPart("comment", comment)
                    .build();
            httpPost.addHeader("Authorization", "Bearer ".concat(token.getAccessToken()));
            httpPost.addHeader("enctype", "multipart/form-data");
            
            httpPost.setEntity(httpEntity);
            
            System.out.println("executing request " + httpPost.getRequestLine());
            CloseableHttpResponse response = httpClient.execute(httpPost);
            try {
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    System.out.println("Response content length: " + resEntity.getContentLength());
                }
                EntityUtils.consume(resEntity);
            } finally {
                response.close();
            }
        } finally {
            httpClient.close();
        }
		
		
	/*	String boundary = Long.toHexString(System.currentTimeMillis()); 
		header.put("enctype", "multipart/form-data");
		header.put("Content-Type", "multipart/form-data; boundary=----WebKitFormBoundary" + boundary);
		header.put("Content-Disposition", "form-data; name=\"files\"; filename=\"screen.JPG\"");
		
		try {
			response = request.execute(requestApi.concat(path), HttpMethod.POST, header, data);
			if (request.getResponceCode() == 400) 
				throw new HttpErrorCodeException(response);
		} catch (IOException e) {
			exceptionResolver.checkForErrorCodeException(request.getResponceCode(), request.getResponseMessage());
			throw e;
		}	
		*/
		return "";//response;
	}
	
	public void put(String path, Integer id, T item) throws HttpErrorCodeException, IOException {

		header.put("Content-Type", "application/json");
		String itemJson = objectMapper.writeValueAsString(item); 

		try {
			response = request.execute(requestApi + path + "/" + id.toString(), HttpMethod.PUT, header, itemJson);
		} catch (IOException e) {
			exceptionResolver.checkForErrorCodeException(request.getResponceCode(), request.getResponseMessage());
			throw e;
		}	
	}
	
	public void delete(String path, Integer id) throws HttpErrorCodeException, IOException {
		
		try {
			response = request.execute(requestApi + path + "/" + id.toString(), HttpMethod.DELETE, header, "");
		} catch (IOException e) {
			exceptionResolver.checkForErrorCodeException(request.getResponceCode(), request.getResponseMessage());
			throw e;
		}	
	}
}
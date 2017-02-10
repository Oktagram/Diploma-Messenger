package com.elekscamp.messenger_javafx_client.DAL;

import java.io.File;
import java.io.IOException;
import com.elekscamp.messenger_javafx_client.Exceptions.HttpErrorCodeException;

public class FileProvider {

	private RequestManager<Object> service;
	private String path;

	public FileProvider() {
		service = new RequestManager<Object>(Object.class);
		path = "/files";
	}

	public String uploadAttachment(File data, Integer messageId) throws IOException {
		return service.post(path + "/uploadAttachment/" + messageId.toString(), data);
	}

	public String uploadProfilePicture(File data, Integer profileId) throws IOException, HttpErrorCodeException {

		return service.post(path + "/uploadPicture/" + profileId.toString(), data);

		/*
		 * url = new URL(RequestManager.getRequestApi()); conn =
		 * (HttpURLConnection) url.openConnection();
		 * 
		 * //add reuqest header conn.setRequestMethod("POST"); //
		 * conn.setRequestProperty("User-Agent", USER_AGENT);
		 * conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		 * 
		 * String urlParameters =
		 * "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";
		 * 
		 * // Send post request conn.setDoOutput(true); DataOutputStream wr =
		 * new DataOutputStream(conn.getOutputStream());
		 * wr.writeBytes(urlParameters); wr.flush(); wr.close();
		 * 
		 * int responseCode = conn.getResponseCode();
		 * System.out.println("\nSending 'POST' request to URL : " + url);
		 * System.out.println("Post parameters : " + urlParameters);
		 * System.out.println("Response Code : " + responseCode);
		 * 
		 * BufferedReader in = new BufferedReader(new
		 * InputStreamReader(conn.getInputStream())); String inputLine;
		 * StringBuffer response = new StringBuffer();
		 * 
		 * while ((inputLine = in.readLine()) != null) {
		 * response.append(inputLine); } in.close();
		 */
	}
}

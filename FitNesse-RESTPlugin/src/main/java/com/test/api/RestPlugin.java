/*
 * FitNesse RestPlugin
 * A simple REST fixture that gives the
 * ability to make rest calls and search
 * through JSON objects which allows for
 * greater business cases
 * 
 * Author: Stuart Sullivan
 * Date: 10, June 2013
 */
package com.test.api;

import com.jayway.jsonpath.JsonPath;
import com.test.api.Config;

import java.io.*;
import java.util.*;
import java.util.Map.*;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;

public class RestPlugin {
	private int code;
	private String cookie, call, input = "";
	private String method, output = "";
	private String url, header = "";
	private Config config;
	private List<Data> headers = new ArrayList<Data>();

	/**
	 * Constructors
	 */
	public RestPlugin() {
		config = Config.getConfig(Config.DEFAULT_CONFIG_NAME);
	}

	public RestPlugin(String u) {
		this.url = u;
		config = Config.getConfig(Config.DEFAULT_CONFIG_NAME);
	}

	/**
	 * Important setters that help configure each call
	 */
	public void method(String method) {
		this.method = method;
	}

	public void url(String url) {
		this.url = url;
	}

	public void header(String h) {
		headers.add(new Data(h.split(":")[0].trim(), h.split(":")[1].trim()));
	}

	public void body(String input) {
		this.input = input;
	}

	// Taking input from a file
	public String readFile(String file) {
		input = "";
		try {
			// create a reader
			BufferedReader br = new BufferedReader(new FileReader(file));

			// grab the first line
			String in = br.readLine();

			// Proceed to read each line of the file until file is empty
			while (in != null) {
				input += in.trim();
				in = br.readLine();
			}

			// Close reader
			br.close();

			return input;
		} catch (Exception e) {
			return "error : " + e.toString();
		}
	}

	/**
	 * Important search functions used for advance testing of the APIs it allows
	 * the tester to grab specific data from the after the call
	 */
	public String header() {
		return header;
	}

	public String response() {
		return output;
	}

	public String code() {
		return "" + code;
	}

	// Search Given JSON for value associated with the key
	public String find(String key, String entry) {
		// Temporary string holder
		String str = "";
		// Holds the array list that is produced by the JSON Path
		List<String> strList = new ArrayList<String>();
		try {
			// Creates a new path object
			JsonPath path = null;
			// if the key and the entry are not empty...
			if (key != null && entry != null) {
				// Instantiate the path
				path = JsonPath.compile(key);
				// Search in entry
				strList.add("" + path.read(entry));
			} else
				return "The response is empty";

			// Remove the '[' and ']' in the list
			str = ""
					+ strList.toString().replaceAll("\\[", "")
							.replaceAll("\\]", "");

			return str;
		} catch (Exception e) {
			return "error : " + e.toString();
		}
	}

	// Search response JSON for value associated with the key
	public String find(String key) {
		String str = "";
		List<String> strList = new ArrayList<String>();
		try {
			JsonPath path = JsonPath.compile(key);
			if (output != null)
				strList.add("" + path.read(output));
			else
				return "The response is empty";

			str = ""
					+ strList.toString().replaceAll("\\[", "")
							.replaceAll("\\]", "");
			return str;
		} catch (Exception e) {
			return "error : " + e.toString();
		}
	}

	// To list all values under the key
	public String findAll(String key) {
		List<String> str;
		String strList = "";

		try {
			JsonPath path = JsonPath.compile(key);
			if (output != null) {
				str = path.read(output);
			}

			else
				return "The response is non existant or it is not in array form";

			for (int i = 0; i < str.size(); i++) {
				strList += "" + str.toArray()[i] + "\n";
			}

			return strList;
		} catch (Exception e) {
			return "error : " + e.toString();
		}
	}

	/**
	 * The function grabs the specific call and does that API call
	 * 
	 * @param call
	 *            - The API call GetResponse does the API call
	 */
	public void call(String call) {
		this.call = call;
		header = "";
		output = "";
		DoCall();
	}

	// Do the actual call
	private String DoCall() {
		// If parameters are empty drop out
		if (url == null)
			return "No domain found";
		if (call == null)
			return "No call found";

		// Set request
		String request = url + call;

		// Based on the method make a call
		try {
			switch (this.method.toUpperCase()) {
			case "GET":
				GeneralCall(new GetMethod(request));
				return output;
			case "POST":
				GeneralCall(new PostMethod(request));
				return output;
			case "PUT":
				GeneralCall(new PutMethod(request));
				return output;
			case "DELETE":
				GeneralCall(new DeleteMethod(request));
				return output;
			default:
				return "No method found";
			}
		} catch (Exception e) {
			return "error : " + e.toString();
		}
	}

	/**
	 * All the specific rest call functions. HTTPClient is not as generic as the
	 * lower level HttpURLConnections
	 */
	private void GeneralCall(HttpMethod callMethod) throws HttpException,
			IOException {
		// Create the client and a input for the call
		HttpClient client = new HttpClient();
		StringRequestEntity requestEntity = new StringRequestEntity(input,
				"application/json", "UTF-8");

		// Add the header elements
		for (int i = 0; i < headers.size(); i++) {
			callMethod.addRequestHeader(headers.get(i).key,
					headers.get(i).value);
		}

		// Depending on the method make a call, don't save the cookie on
		if (!this.method.toUpperCase().equals("GET")) {
			callMethod.addRequestHeader("Cookie", config.get("Cookie"));
			((EntityEnclosingMethod) callMethod)
					.setRequestEntity(requestEntity);
			code = client.executeMethod(callMethod);
			config.replace("Cookie",
					(cookie = client.getState().getCookies()[0].toString()));
		} else {
			callMethod.addRequestHeader("Cookie", config.get("Cookie"));
			code = client.executeMethod(callMethod);
		}

		// Save all the header elements for the call
		for (int i = 0; i < headers.size(); i++) {
			header += callMethod.getRequestHeaders()[i].getName() + ":"
					+ callMethod.getRequestHeaders()[i].getValue() + "\n";
		}

		// Save the output response
		readResponse(callMethod);
	}

	// Save the output response
	private void readResponse(HttpMethod callMethod) throws IOException {
		InputStream rstream = callMethod.getResponseBodyAsStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(rstream,
				"UTF-8"));
		String line;

		while ((line = br.readLine()) != null) {
			output += line;
		}
		br.close();
	}
}
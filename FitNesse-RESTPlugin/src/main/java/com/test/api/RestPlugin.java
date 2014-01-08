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
import com.test.api.Headers;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.*;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;

public class RestPlugin {
	private int code;
	private String cookie, call, input = "";
	private String method, output = "";
	private String url, query, header = "";
	private Headers headers;

	/**
	 * Constructors
	 */
	public RestPlugin() {
		url = "";
		headers = Headers.getHeaders(Headers.DEFAULT_HEADERS_NAME);
	}

	public RestPlugin(String u) {
		this.url = u;
		headers = Headers.getHeaders(Headers.DEFAULT_HEADERS_NAME);
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
		headers.add(h.split(":")[0].trim(), h.split(":")[1].trim());
	}

	public void body(String input) {
		this.input = input;
	}
	
	public void query(String query) {
		this.query = query;
		if(!this.query.contains("q=")) 
			this.query = this.query.replaceAll("\\s", "");
		else
			this.query = this.query.replaceAll("\\s", "%20");
	}
	
	public String request() {
		String request;
		if(query == null)
			request = url + call;
		else 
			request = url + call + "?" + query;
		return  request;
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
		return find(key, output);
	}

	// To list all values under the key
	public String findAll(String key) {
		List<String> str;
		String strList = "";

		try {
			JsonPath path = JsonPath.compile(key);
			if (output != null)
				str = path.read(output);
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
	
	public String date() {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		return format.format(date);
	}
	
	
	public int randomTo(int min, int max) {
		int r = min + (int)Math.random() * ((max - min) + 1);
		return r;
	}

	/**
	 * The function grabs the specific call and does that API call
	 * 
	 * @param call
	 *            - The API call GetResponse does the API call
	 */
	public String call(String call) {
		this.call = call;
		header = "";
		output = "";
		return doCall();
	}
	
	public String execute() {
		call = "";
		header = "";
		output = "";
		return doCall();
	}

	// Do the actual call
	private String doCall() {
		String debug = null;
		// If parameters are empty drop out
		if (url == null) {
			debug += "No domain found\n";
			return debug;
		}
		if (call == null) {
			debug += "No call found\n";
			return debug;
		}

		// Set request
		String request;
		request = url + call;
		debug += "Call URL - " + request + "\n";
		
		// Based on the method make a call
		try {
			if(this.method.toUpperCase().equals("GET")) {
				debug += "GET METHOD\n";
				debug += generalCall(new GetMethod(request));
				return debug;
            } else if(this.method.toUpperCase().equals("PUT")) {
				debug += "PUT METHOD\n";
				debug += generalCall(new PutMethod(request));
				return debug;
            } else if(this.method.toUpperCase().equals("POST")) {
				debug += "POST METHOD\n";
				debug += generalCall(new PostMethod(request));
				return debug;
            } else if(this.method.toUpperCase().equals("DELETE")) {
				debug += "DELETE METHOD\n";
				debug += generalCall(new DeleteMethod(request));
				return debug;
            } else {
				debug += "No method found\n";
				return debug;
			}
		} catch (Exception e) {
			return "error : " + e.toString();
		}
	}

	/**
	 * All the specific rest call functions. HTTPClient is not as generic as the
	 * lower level HttpURLConnections
	 */
	private String generalCall(HttpMethod callMethod) throws Exception,
			IOException {
		String debug = null;
		
		// Create the client and a input for the call
		HttpClient client = new HttpClient();
		StringRequestEntity requestEntity = new StringRequestEntity(input,
				"application/json", "UTF-8");
		
		// Add a query string if needed
		if(query != null) {
			NameValuePair[] qString = new NameValuePair[query.split("&").length];
			for(int i = 0; i < query.split("&").length; i++) {
				qString[i] = new NameValuePair(query.split("&")[i].split("=")[0], query.split("&")[i].split("=")[1]);
				debug += "query > " + qString[i].toString() + "\n";
			}
			callMethod.setQueryString(qString);
		}
		
		// Add the header elements
		Iterator<Entry<String, String>> it = headers.data.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> tmp = it.next();
			callMethod.addRequestHeader(tmp.getKey(), tmp.getValue());
		}

		// Depending on the method make a call, don't save the cookie on
		if (callMethod instanceof EntityEnclosingMethod) {
			((EntityEnclosingMethod) callMethod)
					.setRequestEntity(requestEntity);
			code = client.executeMethod(callMethod);
			getCookie(client);
		} else {
			code = client.executeMethod(callMethod);
		}
		
		debug += "method used > " + callMethod.hasBeenUsed() + "\n";

		// Save all the header elements for the call
		it = headers.data.entrySet().iterator();
		while (it.hasNext()) {
			header += it.next().toString() + "\n";
		}
		
		// Save the output response
		readResponse(callMethod);
		
		return debug;
	}

	// Save the output response
	private void readResponse(HttpMethod callMethod) throws IOException {
		InputStream rstream = callMethod.getResponseBodyAsStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(rstream,
				"UTF-8"));
		String line, tmp = "";

		while ((line = br.readLine()) != null) {
			tmp += line;
		}
		br.close();
		
		output = tmp;
	}

	private void getCookie(HttpClient client) {
		Cookie[] cookies = client.getState().getCookies();
		for (int i = 0; i < cookies.length; i++)
			if (!cookies[i].toString().equals(cookie)) {
				cookie = cookies[i].toString();
				headers.replace("Cookie", cookie);
				return;
			}
		return;
	}
}
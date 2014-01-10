package com.test.api;

import java.util.HashMap;
import java.util.Map;


public class Headers {
	/**
	 * A Hashmap to help deal with header options
	 */
	public static final String DEFAULT_HEADERS_NAME = "default";

	private static final Map<String, Headers> HEADERS = new HashMap<String, Headers>();

	public static Headers getHeaders() {
		return getHeaders(DEFAULT_HEADERS_NAME);
	}

	public static Headers getHeaders(String name) {
		if (name == null) {
			name = DEFAULT_HEADERS_NAME;
		}
		Headers namedHeaders = HEADERS.get(name);
		if (namedHeaders == null) {
			namedHeaders = new Headers(name);
			HEADERS.put(name, namedHeaders);
		}
		return namedHeaders;
	}

	private final String name;

	public Map<String, String> data;

	private Headers(final String name) {
		this.name = name;
		this.data = new HashMap<String, String>();
	}

	public String getName() {
		return name;
	}

	public void add(String key, String value) {
		data.put(key, value);
	}
	
	public void replace(String key, String value) {
		data.remove(key);
		data.put(key, value);
	}

	public String get(String key) {
		return data.get(key);
	}

	public String get(String key, String def) {
		String v = get(key);
		if (v == null) {
			v = def;
		}
		return v;
	}

	public Long getAsLong(String key, Long def) {
		String val = get(key);
		try {
			return Long.parseLong(val);
		} catch (NumberFormatException e) {
			return def;
		}
	}

	public Boolean getAsBoolean(String key, Boolean def) {
		String val = get(key);
		if (val == null) {
			return def;
		}
		return Boolean.parseBoolean(val);
	}

	public Integer getAsInteger(String key, Integer def) {
		String val = get(key);
		try {
			return Integer.parseInt(val);
		} catch (NumberFormatException e) {
			return def;
		}
	}

	public void clear() {
		data.clear();
	}

	@Override
	public String toString() {
		return "[name=" + getName() + "] " + data.toString();
	}
}

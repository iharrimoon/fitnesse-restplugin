package com.test.api;

import java.util.HashMap;
import java.util.Map;

public class Config {
	/**
	 * An attempt to do a config page that will make the easy to configure test.
	 * Mostly taken for REST Fixture
	 */
	public static final String DEFAULT_CONFIG_NAME = "default";

	private static final Map<String, Config> CONFIGURATIONS = new HashMap<String, Config>();

	public static Config getConfig() {
		return getConfig(DEFAULT_CONFIG_NAME);
	}

	public static Config getConfig(String name) {
		if (name == null) {
			name = DEFAULT_CONFIG_NAME;
		}
		Config namedConfig = CONFIGURATIONS.get(name);
		if (namedConfig == null) {
			namedConfig = new Config(name);
			CONFIGURATIONS.put(name, namedConfig);
		}
		return namedConfig;
	}

	private final String name;

	public Map<String, String> data;

	private Config(final String name) {
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

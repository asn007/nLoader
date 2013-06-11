package eu.q_b.asn007.nloader.helpers;

import java.io.File;
import java.util.HashMap;
import java.util.Map.Entry;

import eu.q_b.asn007.nloader.*;

public class NLoaderConfiguration {

	private HashMap<String, String> properties;
	private File config;
	
	public NLoaderConfiguration(File configFile) {
		BaseProcedures.log("Loading NLoader configuration file...", this.getClass());
		this.config = configFile;
		properties = new HashMap<String, String>();
		String string = BaseProcedures.readFileAsString(configFile.toString(), "");
		if(!string.equals(""))
			populateMapWithPairs(string.split("\n"));
	}

	private void populateMapWithPairs(String[] split) {
		for(String keyValuePair: split) properties.put(keyValuePair.split(":", 2)[0], keyValuePair.split(":", 2)[1]);
	}
	
	private void addToConfig(String key, String value) {
		if(properties.get(key) != null) properties.remove(key);
		properties.put(key, value);
		saveConfig();
	}

	public void saveConfig() {
		StringBuilder sb = new StringBuilder();
		for(Entry<String, String> entry: properties.entrySet())
			sb.append(entry.getKey() + ":" + entry.getValue() + "\n");
		BaseProcedures.writeString(sb.toString().substring(0, sb.toString().length() - 1), config);
	}
	
	public String getString(String key) {
		return getString(key, "");
	}

	public String getString(String key, String def) {
		if(properties.get(key) == null) return def;
		return properties.get(key);
	}
	
	public void setString(String key, String value) {
		addToConfig(key, value);
	}
	
	public Boolean getBoolean(String key) {
		return Boolean.parseBoolean(getString(key));
	}
	
	public void setBoolean(String key, Boolean value) {
		addToConfig(key, value.toString());
	}
	
	public Integer getInteger(String key) {
		return getInteger(key, 0);
	}
	
	public Integer getInteger(String key, Integer def) {
		try {
			return Integer.parseInt(getString(key));
		} catch(NumberFormatException ex) {
			return def;
		}
	}
	
	public void setInteger(String key, Integer value) {
		addToConfig(key, value.toString());
	}
}

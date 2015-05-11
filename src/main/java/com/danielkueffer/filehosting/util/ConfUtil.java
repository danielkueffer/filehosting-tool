package com.danielkueffer.filehosting.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Configuration utilities
 * 
 * @author dkueffer
 * 
 */
public class ConfUtil {

	/**
	 * Get Map from a Configuration value
	 * 
	 * @param confArray
	 * @return
	 */
	public static Map<String, String> getMapFromString(String confArray) {
		Map<String, String> confMap = new LinkedHashMap<String, String>();

		String re = confArray.substring(0, confArray.length() - 1).substring(1,
				confArray.length() - 1);

		String[] pairs = re.split(",");

		for (String pair : pairs) {
			String[] entry = pair.trim().split("=");

			String key = entry[0];

			String value = "";

			if (entry.length > 1) {
				value = entry[1];
			}

			confMap.put(key, value);
		}

		return confMap;
	}
}

package ead.converter;

import java.util.HashMap;
import java.util.Map;

import ead.common.model.params.text.EAdString;

public class StringsConverter {

	private static final String PREFIX = "converter.string";

	private Map<EAdString, String> strings;
	private Map<String, EAdString> reverse;

	public StringsConverter() {
		strings = new HashMap<EAdString, String>();
		reverse = new HashMap<String, EAdString>();
	}

	/**
	 * Converts a string to an EAdString
	 * 
	 * @param text
	 * @return
	 */
	public EAdString convert(String text) {
		EAdString string = reverse.get(text);
		if (string == null) {
			string = new EAdString(PREFIX + strings.size());
			strings.put(string, text);
			reverse.put(text, string);
		}
		return string;
	}

}

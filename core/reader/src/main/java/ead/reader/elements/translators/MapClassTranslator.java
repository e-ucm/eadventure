package ead.reader.elements.translators;

import java.util.Map;

public class MapClassTranslator implements ClassTranslator {

	private Map<String, String> translations;

	public MapClassTranslator(Map<String, String> translations) {
		this.translations = translations;
	}

	@Override
	public String translate(String clazz) {
		return translations.get(clazz);
	}

}

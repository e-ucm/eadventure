package ead.converter;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Singleton;

import ead.common.model.elements.EAdElement;

@Singleton
public class EAdElementsCache {

	public Map<String, EAdElement> elements;

	public EAdElementsCache() {
		elements = new HashMap<String, EAdElement>();
	}

	public void put(EAdElement element) {
		elements.put(element.getId(), element);
	}

	public EAdElement get(String id) {
		return elements.get(id);
	}

}

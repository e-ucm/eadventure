package es.eucm.eadventure.engine.core.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.inject.Singleton;

import es.eucm.eadventure.common.resources.EAdString;
import es.eucm.eadventure.common.resources.StringHandler;

@Singleton
public class DefaultStringHandler implements StringHandler {

	private int idGenerator = 0;

	private Map<EAdString, String> strings;

	public DefaultStringHandler() {
		strings = new HashMap<EAdString, String>();
	}

	@Override
	public String getString(EAdString string) {
		String value = strings.get(string);
		return value == null ? string.toString() : value;
	}

	@Override
	public void setString(EAdString eAdString, String string) {
		strings.put(eAdString, string);
	}

	@Override
	public EAdString addString(String string) {
		EAdString eAdString = null;
		do {
			eAdString = new EAdString("string_" + idGenerator + "_"
					+ Math.round(Math.random() * 100));
			idGenerator++;
		} while (strings.containsKey(eAdString));

		strings.put(eAdString, string);
		return eAdString;
	}

	@Override
	public void setStrings(Map<EAdString, String> strings) {
		this.strings = strings;

	}

	@Override
	public void addStrings(Map<EAdString, String> strings) {
		for (Entry<EAdString, String> entry : strings.entrySet()) {
			this.strings.put(entry.getKey(), entry.getValue());
		}

	}

	@Override
	public Map<EAdString, String> getStrings() {
		return strings;
	}

}

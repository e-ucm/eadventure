package es.eucm.eadventure.engine.core.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import com.google.inject.Singleton;

import es.eucm.eadventure.common.params.EAdString;
import es.eucm.eadventure.common.resources.StringHandler;

@Singleton
public class DefaultStringHandler implements StringHandler {

	private Logger logger = Logger.getLogger("DefaultStringHandler");
	
	private Map<EAdString, String> strings;

	public DefaultStringHandler() {
		strings = new HashMap<EAdString, String>();
	}

	@Override
	public String getString(EAdString string) {
		if (string.toString().startsWith(StringHandler.TEXTUAL_STRING_PREFIX)) {
			return string.toString().substring(
					StringHandler.TEXTUAL_STRING_PREFIX.length());
		}
		String value = strings.get(string);
		return value == null ? string.toString() : value;
	}

	@Override
	public void setString(EAdString eAdString, String string) {
		logger.finest("Add string " + eAdString.toString() + " " + string);
		strings.put(eAdString, string);
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

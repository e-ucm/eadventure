package ead.engine.core.game.enginefilters;

import java.util.Map;

import ead.common.model.elements.variables.VarDef;
import ead.engine.core.game.GameState;

public class EngineStringFilter extends
		AbstractEngineFilter<Map<String, String>> {

	/**
	 * Default strings file. Loaded during initialization
	 */
	private static final String ENGINE_DEFAULT_STRINGS = "strings.xml";

	private static final String DEFAULT_STRINGS = "engine_strings.xml";

	public EngineStringFilter() {
		super(0);
	}

	@Override
	public Map<String, String> filter(Map<String, String> o, Object[] params) {
		GameState gameState = (GameState) params[0];
		o.put("@" + DEFAULT_STRINGS, "");
		o.put("@" + ENGINE_DEFAULT_STRINGS, "");

		String languagesProperty = gameState.getValueMap().getValue(null,
				new VarDef<String>("languages", String.class, null));
		if (languagesProperty != null) {
			String[] languages = languagesProperty.split(",");
			for (String language : languages) {
				o.put("@" + language + "/" + DEFAULT_STRINGS, language);
				o.put("@" + language + "/" + ENGINE_DEFAULT_STRINGS, language);
			}
		}
		return o;
	}

}

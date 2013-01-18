/**
 * eAdventure (formerly <e-Adventure> and <e-Game>) is a research project of the
 *    <e-UCM> research group.
 *
 *    Copyright 2005-2010 <e-UCM> research group.
 *
 *    You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *
 *    <e-UCM> is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *
 *          C Profesor Jose Garcia Santesmases sn,
 *          28040 Madrid (Madrid), Spain.
 *
 *          For more info please visit:  <http://e-adventure.e-ucm.es> or
 *          <http://www.e-ucm.es>
 *
 * ****************************************************************************
 *
 *  This file is part of eAdventure, version 2.0
 *
 *      eAdventure is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      eAdventure is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with eAdventure.  If not, see <http://www.gnu.org/licenses/>.
 */

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

		String languagesProperty = gameState.getValue(null, new VarDef<String>(
				"languages", String.class, null));
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

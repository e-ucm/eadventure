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

package ead.engine.core.game;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.model.elements.EAdAdventureModel;
import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.extra.EAdList;
import ead.common.model.elements.extra.EAdListImpl;
import ead.common.params.text.EAdString;
import ead.engine.core.platform.assets.AssetHandler;
import ead.reader.adventure.AdventureReader;
import ead.reader.adventure.ObjectFactory;
import ead.reader.strings.StringsReader;
import ead.tools.GenericInjector;
import ead.tools.StringHandler;
import ead.tools.reflection.ReflectionClassLoader;
import ead.tools.reflection.ReflectionProvider;
import ead.tools.xml.XMLParser;

@Singleton
public class GameLoaderImpl implements GameLoader {

	private XMLParser parser;

	private AdventureReader adventureReader;

	private StringsReader stringsReader;

	private Game game;

	private StringHandler stringHandler;

	private ReflectionProvider reflectionProvider;

	private ReflectionClassLoader reflectionClassLoader;

	private AssetHandler assetHandler;

	// Data to load

	private String data;

	private String strings;

	private String properties;

	// Loaded data
	private EAdAdventureModel model;

	private Map<EAdString, String> stringsMap;

	private Map<String, String> propertiesMap;

	private int listeners;

	private EAdList<EAdEffect> initialEffects;

	@Inject
	public GameLoaderImpl(GenericInjector injector) {
		parser = injector.getInstance(XMLParser.class);
		adventureReader = new AdventureReader(parser);
		stringsReader = new StringsReader(parser);
		stringHandler = injector.getInstance(StringHandler.class);
		game = injector.getInstance(Game.class);
		reflectionProvider = injector.getInstance(ReflectionProvider.class);
		reflectionClassLoader = injector
				.getInstance(ReflectionClassLoader.class);
		assetHandler = injector.getInstance(AssetHandler.class);
		ObjectFactory.init(reflectionProvider);
		ReflectionClassLoader.init(reflectionClassLoader);
		initialEffects = new EAdListImpl<EAdEffect>(EAdEffect.class);
	}

	@Override
	public void loadGameFromFiles(String dataFile, String stringsFile,
			String propertiesFile) {
		listeners = 0;
	}

	private void checkListeners() {
		listeners++;
		if (listeners == 3) {
			loadGame(data, strings, properties);
		}
	}

	@Override
	public void loadGame(String data, String strings, String properties) {
		step = 0;
		this.data = data;
		this.strings = strings;
		this.properties = properties;
	}

	private int step = -1;

	@Override
	public void step() {
		if (step >= 0) {
			if (step == 0) {
				model = adventureReader.readXML(data);
			} else if (step == 1) {
				stringsMap = stringsReader.readStrings(strings);
			} else if (step == 2) {

			} else if (step == 3) {
				loadGame(model, stringsMap, propertiesMap);
			}
			if (step < 4) {
				step++;
			}
		}
	}

	@Override
	public void loadGame(EAdAdventureModel model,
			Map<EAdString, String> stringsMap, Map<String, String> propertiesMap) {
		if (stringsMap == null) {
			stringsMap = new HashMap<EAdString, String>();
		}
		stringHandler.setStrings(stringsMap);

		if (propertiesMap == null) {
			propertiesMap = new HashMap<String, String>();
		}
		for (Entry<String, String> entry : propertiesMap.entrySet()) {
			model.setProperty(entry.getKey(), entry.getValue());
		}
		initialEffects.clear();
	}

	@Override
	public EAdList<EAdEffect> getInitialEffects() {
		return initialEffects;
	}
}

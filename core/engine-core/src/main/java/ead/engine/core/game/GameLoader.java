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

import java.util.Map;
import java.util.Map.Entry;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.model.elements.EAdAdventureModel;
import ead.common.params.text.EAdString;
import ead.reader.adventure.AdventureReader;
import ead.reader.adventure.ObjectFactory;
import ead.reader.properties.PropertiesReader;
import ead.reader.strings.StringsReader;
import ead.tools.GenericInjector;
import ead.tools.StringHandler;
import ead.tools.reflection.ReflectionClassLoader;
import ead.tools.reflection.ReflectionProvider;
import ead.tools.xml.XMLParser;

@Singleton
public class GameLoader {

	private XMLParser parser;

	private AdventureReader adventureReader;

	private StringsReader stringsReader;

	private PropertiesReader propertiesReader;

	private StringHandler stringHandler;

	private Game game;

	private ReflectionProvider reflectionProvider;

	private ReflectionClassLoader reflectionClassLoader;

	// Data to load

	private String data;

	private String strings;

	private String properties;

	// Loaded data
	private EAdAdventureModel model;

	private Map<EAdString, String> stringsMap;

	private Map<String, String> propertiesMap;

	@Inject
	public GameLoader(GenericInjector injector) {
		parser = injector.getInstance(XMLParser.class);
		adventureReader = new AdventureReader(parser);
		stringsReader = new StringsReader(parser);
		propertiesReader = new PropertiesReader();
		stringHandler = injector.getInstance(StringHandler.class);
		game = injector.getInstance(Game.class);
		reflectionProvider = injector.getInstance(ReflectionProvider.class);
		reflectionClassLoader = injector
				.getInstance(ReflectionClassLoader.class);
		ObjectFactory.init(reflectionProvider);
		ReflectionClassLoader.init(reflectionClassLoader);
	}

	public void loadGame(String data, String strings, String properties) {
		step = 0;
		this.data = data;
		this.strings = strings;
		this.properties = properties;
	}

	int step = -1;

	public void step() {
		if (step >= 0) {
			if (step == 0) {
				model = adventureReader.readXML(data);
			} else if (step == 1) {
				stringsMap = stringsReader.readStrings(strings);
			} else if (step == 2) {
				propertiesMap = propertiesReader.readProperties(properties);
			} else if (step == 3) {
				stringHandler.setStrings(stringsMap);

				for (Entry<String, String> entry : propertiesMap.entrySet()) {
					model.setProperty(entry.getKey(), entry.getValue());
				}
				game.setGame(model, model.getChapters().get(0));
			}
			if (step < 4) {
				step++;
			}
		}
	}

}

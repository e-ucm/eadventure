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

package ead.engine.playn.html;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import playn.core.PlayN;
import playn.html.HtmlAssets;
import playn.html.HtmlPlatform;
import playn.html.HtmlPlatform.Mode;

import com.google.gwt.core.client.GWT;

import ead.common.model.elements.EAdAdventureModel;
import ead.common.util.EAdURI;
import ead.engine.core.game.Game;
import ead.engine.playn.core.EAdEngine;
import ead.engine.playn.core.platform.EngineCallback;
import ead.engine.playn.core.platform.PlayNGinInjector;
import ead.engine.playn.reader.GWTPropertiesReader;
import ead.engine.playn.reader.GWTReader;
import ead.engine.playn.reader.GWTStringReader;

/**
 * Reads game data directly from resources
 * 
 * @author anserran
 * 
 */
public class GWTGame implements EngineCallback {

	private static final Logger logger = LoggerFactory.getLogger("GWTGame");

	private PlayNGinInjector injector;

	private EAdAdventureModel model;

	private int filesRead;

	private String moduleName;

	private Map<String, String> propertiesMap;

	private EAdAventureModelProxy modelProxy;

	public GWTGame(String moduleName) {
		injector = GWT.create(PlayNGinInjector.class);
		injector.getPlayNInjector().setInjector(injector);
		filesRead = 0;
		propertiesMap = new HashMap<String, String>();
		modelProxy = new EAdAventureModelProxy();
		this.moduleName = moduleName;
	}

	public void launch() {
		HtmlAssets assets = HtmlPlatform.register(Mode.AUTODETECT).assets();
		assets.setPathPrefix("");
		// Read strings
		GWTStringReader stringsReader = new GWTStringReader();
		stringsReader.readXML(moduleName + "/strings.xml",
				injector.getStringHandler(), this);

		// Read properties
		GWTPropertiesReader propertiesReader = new GWTPropertiesReader();
		propertiesReader.readProperties(moduleName + "/ead.properties",
				propertiesMap, this);

		// Read model
		GWTReader modelReader = new GWTReader();
		modelReader.readXML(moduleName + "/data.xml", modelProxy, this);

	}

	@Override
	public void done() {
		filesRead++;
		logger.info("Files read: {}", filesRead);
		if (filesRead == 3) {
			injector.getAssetHandler().setResourcesLocation(
					new EAdURI(moduleName + "/"));
			Game game = injector.getGame();
			model = modelProxy.getModel();
			for (Entry<String, String> e : propertiesMap.entrySet()) {
				model.setProperty(e.getKey(), e.getValue());
			}
			game.setGame(model, model.getChapters().get(0));
			game.loadGame();
			PlayN.run(new EAdEngine(game, injector.getGUI(), injector
					.getAssetHandler(), injector.getMouseState(), injector
					.getPlatformConfiguration()));
		}
	}

	@Override
	public void error() {
		logger.error("Some error reading the game files.");
	}

	public static class EAdAventureModelProxy {

		private EAdAdventureModel model;

		public void setModel(EAdAdventureModel model) {
			this.model = model;
		}

		public EAdAdventureModel getModel() {
			return model;
		}

	}

}

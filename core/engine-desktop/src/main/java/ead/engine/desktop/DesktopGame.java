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

package ead.engine.desktop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.google.inject.Guice;
import com.google.inject.Injector;

import ead.common.model.elements.BasicAdventureModel;
import ead.common.model.elements.BasicChapter;
import ead.common.model.elements.EAdAdventureModel;
import ead.common.model.elements.scene.EAdScene;
import ead.common.params.text.EAdString;
import ead.common.reader.EAdAdventureDOMModelReader;
import ead.common.util.EAdURI;
import ead.common.util.StringHandler;
import ead.common.writer.EAdAdventureModelWriter;
import ead.elementfactories.EAdElementsFactory;
import ead.engine.core.debuggers.Debugger;
import ead.engine.core.debuggers.DebuggerHandler;
import ead.engine.core.game.Game;
import ead.engine.core.game.GameController;
import ead.engine.core.game.GameLoop;
import ead.engine.core.platform.EngineConfiguration;
import ead.engine.core.platform.assets.AssetHandler;
import ead.engine.core.tracking.selection.TrackerSelector;
import ead.engine.desktop.core.platform.module.DesktopAssetHandlerModule;
import ead.engine.desktop.core.platform.module.DesktopModule;
import ead.engine.java.core.platform.modules.JavaBasicGameModule;

public class DesktopGame {

	private Game game;

	private Injector injector;

	private String file;

	private boolean writeAndRead = false;

	public DesktopGame() {
		injector = Guice.createInjector(new DesktopAssetHandlerModule(),
				new DesktopModule(), new JavaBasicGameModule());
		game = injector.getInstance(Game.class);
	}

	public DesktopGame(EAdScene scene) {
		this(scene, null);
	}

	public DesktopGame(EAdAdventureModel model, Map<EAdString, String> strings) {
		this(model, null, strings, null);
	}

	public DesktopGame(EAdScene scene, List<Class<? extends Debugger>> debuggers) {
		this();
		EAdAdventureModel model = new BasicAdventureModel();
		BasicChapter chapter = new BasicChapter();
		chapter.setId("chapter1");
		chapter.getScenes().add(scene);
		chapter.setInitialScene(scene);
		model.getChapters().add(chapter);
		setGame(model, EAdElementsFactory.getInstance().getStringFactory()
				.getStrings(), debuggers);
	}

	public DesktopGame(EAdAdventureModel adventureModel, String file,
			Map<EAdString, String> strings,
			List<Class<? extends Debugger>> debuggers) {
		this();
		setGame(adventureModel, strings, debuggers);
		this.file = file;
	}

	public void setGame(EAdAdventureModel model,
			Map<EAdString, String> strings,
			List<Class<? extends Debugger>> debuggers) {

		if (writeAndRead) {
			model = writeAndRead(model);
		}

		if (debuggers != null && debuggers.size() > 0) {
			DebuggerHandler debuggerHandler = injector
					.getInstance(DebuggerHandler.class);
			debuggerHandler.init(debuggers);
		}

		StringHandler stringHandler = injector.getInstance(StringHandler.class);
		stringHandler.addStrings(strings);

		game.setGame(model, model.getChapters().get(0));

	}

	public void launch(int ticksPerSecond, boolean fullscreen) {
		GameLoop gameLoop = injector.getInstance(GameLoop.class);
		gameLoop.setTicksPerSecond(ticksPerSecond);

		AssetHandler assetHandler = injector.getInstance(AssetHandler.class);
		List<String> text = assetHandler.getTextFile("@select.track");

		if (text != null) {
			TrackerSelector trackerSelector = injector
					.getInstance(TrackerSelector.class);
			trackerSelector.setSelection(text);
		}

		game = injector.getInstance(Game.class);

		final GameController launcher = injector
				.getInstance(GameController.class);
		final EAdURI uri = (file == null) ? null : new EAdURI(file);

		EngineConfiguration conf = injector
				.getInstance(EngineConfiguration.class);

		conf.setFullscreen(fullscreen);
		conf.setSize(800, 600);

		new Thread("DesktopGame") {
			public void run() {
				launcher.start(uri);
			}
		}.start();
	}

	public EAdAdventureModel getModel() {
		return game.getAdventureModel();
	}

	public EAdAdventureModel writeAndRead(EAdAdventureModel model) {
		File resultFile = null;
		EAdAdventureModelWriter writer = null;
		EAdAdventureDOMModelReader reader = null;

		try {
			resultFile = File.createTempFile("data", ".xml");
			resultFile.deleteOnExit();
			writer = new EAdAdventureModelWriter();
			reader = new EAdAdventureDOMModelReader();
		} catch (IOException e) {

		}

		FileOutputStream out = null;
		try {
			out = new FileOutputStream(resultFile);
			writer.write(model, out);
		} catch (FileNotFoundException e) {

		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {

				}
			}
		}

		FileInputStream in = null;
		try {
			in = new FileInputStream(resultFile);
			EAdAdventureModel model2 = reader.read(in);
			return model2;
		} catch (FileNotFoundException e) {

		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {

				}
			}
		}
		return null;
	}

}

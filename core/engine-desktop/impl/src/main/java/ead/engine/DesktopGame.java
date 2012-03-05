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

package ead.engine;

import java.util.Map;

import com.google.inject.Guice;
import com.google.inject.Injector;

import ead.common.model.elements.BasicAdventureModel;
import ead.common.model.elements.BasicChapter;
import ead.common.model.elements.EAdAdventureModel;
import ead.common.model.elements.scene.EAdScene;
import ead.common.params.text.EAdString;
import ead.common.util.EAdURI;
import ead.common.util.StringHandler;
import ead.elementfactories.EAdElementsFactory;
import ead.engine.core.game.Game;
import ead.engine.core.game.GameLoop;
import ead.engine.core.platform.EngineConfiguration;
import ead.engine.core.platform.PlatformLauncher;
import ead.engine.core.platform.module.DesktopAssetHandlerModule;
import ead.engine.core.platform.module.DesktopModule;
import ead.engine.core.platform.modules.BasicGameModule;

public class DesktopGame {

	private Injector injector;

	private String file;

	public DesktopGame(EAdScene scene) {
		EAdAdventureModel model = new BasicAdventureModel();
		BasicChapter chapter = new BasicChapter();
		chapter.setId("chapter1");
		chapter.getScenes().add(scene);
		chapter.setInitialScene(scene);
		model.getChapters().add(chapter);
		init(model, EAdElementsFactory.getInstance().getStringFactory().getStrings());
	}

	public DesktopGame(EAdAdventureModel adventureModel, String file, Map<EAdString, String> strings) {
		init(adventureModel, strings);
		this.file = file;
	}

	public void init(EAdAdventureModel model, Map<EAdString, String> strings) {
		
		injector = Guice.createInjector(new DesktopAssetHandlerModule(),
				new DesktopModule(), new BasicGameModule());

		Game game = injector.getInstance(Game.class);
		game.setGame(model, model.getChapters().get(0));

		StringHandler stringHandler = injector.getInstance(StringHandler.class);
		stringHandler.addStrings(strings);
	}

	public void launch(int ticksPerSecond) {
		final PlatformLauncher launcher = injector
				.getInstance(PlatformLauncher.class);
		final EAdURI uri = ( file == null ) ? null : new EAdURI(file);

		EngineConfiguration conf = injector
				.getInstance(EngineConfiguration.class);
		GameLoop gameLoop = injector.getInstance(GameLoop.class);
		gameLoop.setTicksPerSecond(ticksPerSecond);
		conf.setSize(800, 600);

		new Thread() {
			public void run() {
				launcher.launch(uri);
			}
		}.start();
	}

}

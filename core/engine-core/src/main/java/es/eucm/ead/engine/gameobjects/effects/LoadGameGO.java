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

package es.eucm.ead.engine.gameobjects.effects;

import com.badlogic.gdx.Gdx;
import com.google.inject.Inject;
import es.eucm.ead.engine.EAdEngine;
import es.eucm.ead.engine.assets.AssetHandler;
import es.eucm.ead.engine.factories.SceneElementFactory;
import es.eucm.ead.engine.game.Game;
import es.eucm.ead.engine.game.GameLoader;
import es.eucm.ead.engine.game.interfaces.GUI;
import es.eucm.ead.model.assets.AssetDescriptor;
import es.eucm.ead.model.elements.conditions.EmptyCond;
import es.eucm.ead.model.elements.effects.LoadGameEf;
import es.eucm.ead.model.elements.effects.variables.ChangeFieldEf;
import es.eucm.ead.model.elements.huds.MouseHud;
import es.eucm.ead.model.elements.operations.ElementField;
import es.eucm.ead.model.elements.scenes.SceneElement;
import es.eucm.ead.reader.AdventureReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Stack;

public class LoadGameGO extends AbstractEffectGO<LoadGameEf> {

	static private Logger logger = LoggerFactory.getLogger(LoadGameGO.class);
	private GameLoader gameLoader;

	private SceneElementFactory sceneElementFactory;

	private GUI gui;

	private AssetHandler assetHandler;

	private AdventureReader reader;

	private Stack<AssetDescriptor> assetsToLoad;

	private boolean readingXML;

	private boolean readingAssets;

	private int assetsToLoadNumber;

	private boolean done;

	// Aux vars
	private static final ChangeFieldEf SHOW_CURSOR;

	private static final ChangeFieldEf HIDE_CURSOR;

	static {
		ElementField field = new ElementField(MouseHud.MOUSE_REF,
				SceneElement.VAR_VISIBLE);
		SHOW_CURSOR = new ChangeFieldEf(field, EmptyCond.TRUE);
		HIDE_CURSOR = new ChangeFieldEf(field, EmptyCond.FALSE);
	}

	@Inject
	public LoadGameGO(GameLoader gameLoader, Game game) {
		super(game);
		this.gameLoader = gameLoader;
	}

	public void initialize() {
		gameLoader.loadGame((EAdEngine) Gdx.app.getApplicationListener());
	}

}

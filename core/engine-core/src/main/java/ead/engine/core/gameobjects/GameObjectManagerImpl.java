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

package ead.engine.core.gameobjects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

import ead.common.model.elements.scenes.EAdSceneElement;
import ead.engine.core.gameobjects.go.DrawableGO;
import ead.engine.core.gameobjects.huds.HudGO;
import ead.engine.core.util.EAdTransformation;

@Singleton
public class GameObjectManagerImpl implements GameObjectManager,
		Comparator<HudGO> {

	private static final Logger logger = LoggerFactory
			.getLogger("GameObjectManagerImpl");

	private ArrayList<DrawableGO<?>> gameObjects;

	private ArrayList<HudGO> huds;

	public GameObjectManagerImpl() {
		this.gameObjects = new ArrayList<DrawableGO<?>>();
		this.huds = new ArrayList<HudGO>();
		logger.info("New instance");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.engine.core.gameobjects.GameObjectManager#add(es.eucm
	 * .eadventure.engine.core.gameobjects.GameObject)
	 */
	@Override
	public void add(DrawableGO<?> element,
			EAdTransformation transformation) {
		gameObjects.add(element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.engine.core.gameobjects.GameObjectManager#getGameObjects
	 * ()
	 */
	@Override
	public List<DrawableGO<?>> getGameObjects() {
		return this.gameObjects;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.engine.core.gameobjects.GameObjectManager#addHUD(es
	 * .eucm.eadventure.engine.core.gameobjects.huds.HudGO)
	 */
	@Override
	public void addHUD(HudGO hud) {
		if (!huds.contains(hud))
			huds.add(hud);

		Collections.sort(huds, this);
		logger.info("Added HUD. size: " + huds.size() + "; huds: "
				+ huds);
	}

	@Override
	public List<HudGO> getHUDs() {
		return huds;
	}

	@Override
	public void removeHud(HudGO hud) {
		huds.remove(hud);
	}

	@Override
	public int compare(HudGO arg0, HudGO arg1) {
		return arg0.getPriority() - arg1.getPriority();
	}

	@Override
	public DrawableGO<?> getGameObject(EAdSceneElement element) {
		if (element == null) {
			return null;
		}
		for (DrawableGO<?> go : gameObjects) {
			if (go.getElement() == element) {
				return go;
			}
		}
		return null;
	}

}

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

package es.eucm.eadventure.engine.core.gameobjects.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.inject.Singleton;

import es.eucm.eadventure.engine.core.gameobjects.DrawableGO;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectManager;
import es.eucm.eadventure.engine.core.gameobjects.huds.BasicHUD;
import es.eucm.eadventure.engine.core.gameobjects.huds.HudGO;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.util.EAdTransformation;

@Singleton
public class GameObjectManagerImpl implements GameObjectManager {

	private ArrayList<DrawableGO<?>>[] gameObjects;

	private ArrayList<EAdTransformation>[] transformations;

	private ArrayList<HudGO> huds;

	private int pointer;

	private int bufferPointer;
	
	private BasicHUD basicHud;

	private static final Logger logger = Logger
			.getLogger("GameObjectManagerImpl");

	@SuppressWarnings("unchecked")
	public GameObjectManagerImpl() {
		this.gameObjects = new ArrayList[2];
		this.gameObjects[0] = new ArrayList<DrawableGO<?>>();
		this.gameObjects[1] = new ArrayList<DrawableGO<?>>();
		this.transformations = new ArrayList[2];
		this.transformations[0] = new ArrayList<EAdTransformation>();
		this.transformations[1] = new ArrayList<EAdTransformation>();
		this.pointer = 0;
		this.bufferPointer = 1;
		this.huds = new ArrayList<HudGO>();
		logger.info("New instance");
	}
	
	public void setBasicHUD( BasicHUD basicHUD ){
		this.basicHud = basicHUD;
		basicHUD.setGameObjectManager( this );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.engine.core.gameobjects.GameObjectManager#add(es.eucm
	 * .eadventure.engine.core.gameobjects.GameObject)
	 */
	@Override
	public void add(DrawableGO<?> element, EAdTransformation transformation) {
		synchronized (GameObjectManager.lock) {
			this.gameObjects[bufferPointer].add(element);
			this.transformations[bufferPointer].add(transformation);
		}
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
		return this.gameObjects[pointer];
	}

	@Override
	public List<EAdTransformation> getTransformations() {
		return this.transformations[pointer];
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
		logger.info("Added HUD. size: " + huds.size() + "; huds: " + huds);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.engine.core.gameobjects.GameObjectManager#getHUD()
	 */
	@Override
	public void addHUDs(GUI gui, EAdTransformation t) {
		if (!huds.isEmpty()) {
			for (HudGO hud : huds) {
				hud.update();
				gui.addElement(hud, t);
			}
		}
		basicHud.update();
		gui.addElement(basicHud, t);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.engine.core.gameobjects.GameObjectManager#removeHUD()
	 */
	@Override
	public void removeHUD(HudGO hud) {
		huds.remove(hud);
		logger.info("Removed HUD. size: " + huds.size() + "; huds: " + huds);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.engine.core.gameobjects.GameObjectManager#swap()
	 */
	@Override
	public void swap() {
		synchronized (lock) {
			pointer = bufferPointer;
			bufferPointer = (bufferPointer + 1) % 2;
			gameObjects[bufferPointer].clear();
			transformations[bufferPointer].clear();
		}
	}
	
	public List<HudGO> getHUDs(){
		return huds;
	}

}

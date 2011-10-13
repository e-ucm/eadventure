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

package es.eucm.eadventure.engine.core.gameobjects.huds.impl;

import java.util.List;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.model.actions.EAdAction;
import es.eucm.eadventure.common.params.geom.EAdPosition;
import es.eucm.eadventure.engine.core.MouseState;
import es.eucm.eadventure.engine.core.gameobjects.GameObject;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectManager;
import es.eucm.eadventure.engine.core.gameobjects.SceneElementGO;
import es.eucm.eadventure.engine.core.gameobjects.huds.ActionsHUD;
import es.eucm.eadventure.engine.core.guiactions.GUIAction;
import es.eucm.eadventure.engine.core.guiactions.MouseAction;
import es.eucm.eadventure.engine.core.platform.EAdCanvas;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.RuntimeAsset;
import es.eucm.eadventure.engine.core.util.EAdTransformation;
import es.eucm.eadventure.engine.core.util.impl.EAdTransformationImpl;

/**
 * <p>
 * Default generic implementation of the {@link ActionsHUD}
 * </p>
 * 
 */
@Singleton
public class ActionsHUDImpl implements ActionsHUD {

	/**
	 * The logger
	 */
	private static final Logger logger = Logger
			.getLogger("ActionsHUDImpl");

	/**
	 * The games {@link GUI}
	 */
	protected GUI gui;

	/**
	 * List of the {@link EAdAction}s
	 */
	protected List<EAdAction> actions;

	/**
	 * The position in the x coordinates
	 */
	private int x;

	/**
	 * The position in the y coordinates
	 */
	private int y;

	/**
	 * The radius of the actions HUD
	 */
	protected int radius;

	protected SceneElementGO<?> sceneElement;

	private GameObjectManager gameObjectManager;
	
	@Inject
	public ActionsHUDImpl(GUI gui, GameObjectManager gameObjectManager) {
		this.gui = gui;
		this.gameObjectManager = gameObjectManager;
	}

	/* (non-Javadoc)
	 * @see es.eucm.eadventure.engine.core.gameobjects.GameObject#processAction(es.eucm.eadventure.engine.core.guiactions.GUIAction)
	 */
	@Override
	public boolean processAction(GUIAction action) {
		if (action instanceof MouseAction) {
			MouseAction temp = (MouseAction) action;

			switch (temp.getType()) {
			case CLICK:
				logger.info("Remove actions HUD");
				gameObjectManager.removeHUD(this);
				temp.consume();
			default:
			}

		}
		action.consume();
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.engine.core.gameobjects.GameObject#setElement(java
	 * .lang.Object)
	 */
	@Override
	public void setElement(SceneElementGO<?> ref) {
		sceneElement = ref;
		int x = ref.getCenterX();
		int y = ref.getCenterY();
		radius = (int) (( ref.getWidth() > ref.getHeight() ? ref.getWidth() : ref.getHeight() ) * ref.getScale()) / 2;
		radius = Math.min(250, radius);
		setElementProperties(x, y, radius);
		actions = ref.getValidActions();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.engine.core.gameobjects.GameObject#getDraggableElement
	 * (es.eucm.eadventure.engine.core.MouseState)
	 */
	@Override
	public GameObject<?> getDraggableElement(MouseState mouseState) {
		return null;
	}

	@Override
	public List<EAdAction> getActions() {
		return actions;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getRadius() {
		return radius;
	}

	public void setElementProperties(int x, int y, int radius) {
		this.x = x;
		this.y = y;
		this.radius = radius;
		logger.info("properties: " + x + " " + y + " " + radius);
	}

	@Override
	public SceneElementGO<?> getElement() {
		return sceneElement;
	}
	
	@Override
	public List<RuntimeAsset<?>> getAssets(List<RuntimeAsset<?>> assetList,
			boolean allAssets) {
		//TODO
		return assetList;
	}

	@Override
	public void setPosition(EAdPosition p) {
		
	}

	@Override
	public void doLayout(EAdTransformation transformation) {
		
	}

	@Override
	public void update() {
		
	}

	@Override
	public EAdPosition getPosition() {
		return null;
	}
	
	@Override
	public EAdTransformation getTransformation() {
		return EAdTransformationImpl.INITIAL_TRANSFORMATION;
	}

	@Override
	public boolean isEnable() {
		return true;
	}

	@Override
	public void render(EAdCanvas<?> c) {
		// TODO Auto-generated method stub
		
	}

}

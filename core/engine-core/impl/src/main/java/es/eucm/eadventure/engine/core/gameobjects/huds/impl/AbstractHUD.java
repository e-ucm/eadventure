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

import java.util.ArrayList;
import java.util.List;

import es.eucm.eadventure.common.params.geom.EAdPosition;
import es.eucm.eadventure.common.util.EAdTransformation;
import es.eucm.eadventure.common.util.impl.EAdTransformationImpl;
import es.eucm.eadventure.engine.core.gameobjects.go.DrawableGO;
import es.eucm.eadventure.engine.core.gameobjects.go.SceneElementGO;
import es.eucm.eadventure.engine.core.gameobjects.huds.HudGO;
import es.eucm.eadventure.engine.core.guiactions.GUIAction;
import es.eucm.eadventure.engine.core.input.MouseState;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.RuntimeAsset;
import es.eucm.eadventure.engine.core.platform.rendering.EAdCanvas;

public abstract class AbstractHUD implements HudGO {

	protected GUI gui;

	private List<DrawableGO<?>> hudGameObjects;

	public AbstractHUD(GUI gui) {
		this.gui = gui;
		hudGameObjects = new ArrayList<DrawableGO<?>>();
	}

	public void addElement(DrawableGO<?> drawable) {
		hudGameObjects.add(drawable);
	}

	public void render(EAdCanvas<?> c) {

	}

	public void doLayout(EAdTransformation t) {
		for (DrawableGO<?> go : hudGameObjects) {
			gui.addElement(go, t);
		}
	}

	public void update() {
		for (DrawableGO<?> go : hudGameObjects) {
			go.update();
		}
	}

	public boolean contains(int x, int y) {
		return false;
	}
	
	public boolean processAction(GUIAction action){
		return false;
	}
	
	@Override
	public void setElement(Void element) {
		// Do nothing
	}

	@Override
	public Void getElement() {
		// Return nothing
		return null;
	}
	
	@Override
	public EAdTransformation getTransformation() {
		return EAdTransformationImpl.INITIAL_TRANSFORMATION;
	}
	
	@Override
	public SceneElementGO<?> getDraggableElement(MouseState mouseState) {
		return null;
	}

	@Override
	public boolean isEnable() {
		return true;
	}

	@Override
	public List<RuntimeAsset<?>> getAssets(List<RuntimeAsset<?>> assetList,
			boolean allAssets) {
		return assetList;
	}

	@Override
	public EAdPosition getPosition() {
		return null;
	}

	@Override
	public void setPosition(EAdPosition p) {
		
	}
	

}

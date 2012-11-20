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

package ead.engine.core.gameobjects.huds;

import java.util.ArrayList;
import java.util.List;

import ead.common.resources.assets.AssetDescriptor;
import ead.common.util.EAdPosition;
import ead.engine.core.gameobjects.go.DrawableGO;
import ead.engine.core.gameobjects.go.SceneElementGO;
import ead.engine.core.gameobjects.huds.HudGO;
import ead.engine.core.input.InputAction;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.assets.RuntimeDrawable;
import ead.engine.core.platform.rendering.GenericCanvas;
import ead.engine.core.util.EAdTransformation;
import ead.engine.core.util.EAdTransformationImpl;

public abstract class AbstractHUD implements HudGO {

	protected GUI gui;

	private List<DrawableGO<?>> hudGameObjects;

	protected EAdTransformation transformation;

	private boolean first = true;

	public AbstractHUD(GUI gui) {
		this.gui = gui;
		hudGameObjects = new ArrayList<DrawableGO<?>>();
		transformation = new EAdTransformationImpl();
	}

	public List<DrawableGO<?>> getContaintedGOs() {
		return hudGameObjects;
	}

	public void addElement(DrawableGO<?> drawable) {
		hudGameObjects.add(drawable);
	}

	public void render(GenericCanvas<?> c) {

	}

	public void doLayout(EAdTransformation t) {
		for (DrawableGO<?> go : hudGameObjects) {
			gui.addElement(go, t);
		}
	}

	public void update() {
		if (first) {
			first = false;
			init();
		}
		for (DrawableGO<?> go : hudGameObjects) {
			go.update();
		}
	}

	public boolean contains(int x, int y) {
		return false;
	}

	public boolean processAction(InputAction<?> action) {
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
		return transformation;
	}

	@Override
	public SceneElementGO<?> getDraggableElement() {
		return null;
	}

	@Override
	public boolean isEnable() {
		return true;
	}

	@Override
	public List<AssetDescriptor> getAssets(List<AssetDescriptor> assetList,
			boolean allAssets) {
		return assetList;
	}

	@Override
	public EAdPosition getPosition() {
		return null;
	}

	public void resetTransfromation() {
		transformation.getMatrix().setIdentity();
	}

	public RuntimeDrawable<?, ?> getRuntimeDrawable() {
		return null;
	}

	public int getWidth() {
		return 1;
	}

	public int getHeight() {
		return 1;
	}

	@Override
	public void init() {

	}

}

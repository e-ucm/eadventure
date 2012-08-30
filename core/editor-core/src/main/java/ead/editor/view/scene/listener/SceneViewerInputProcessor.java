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

package ead.editor.view.scene.listener;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import ead.common.model.elements.scene.EAdSceneElement;
import ead.editor.view.scene.SimpleSceneViewer;
import ead.editor.view.scene.go.EditableGameObject;

public class SceneViewerInputProcessor implements InputProcessor {

	private SimpleSceneViewer viewer;

	private SceneListener sceneListener;

	private List<EAdSceneElement> selection;

	private boolean[] keyboard = new boolean[256];

	public SceneViewerInputProcessor(SimpleSceneViewer viewer,
			SceneListener sceneListener) {
		this.viewer = viewer;
		this.selection = new ArrayList<EAdSceneElement>();
		this.sceneListener = sceneListener;
	}

	@Override
	public boolean keyDown(int keycode) {
		keyboard[keycode] = true;
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		keyboard[keycode] = false;
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		EditableGameObject go = viewer.get(x, y);
		if (!viewer.getSelection().contains(go)
				&& !keyboard[Input.Keys.CONTROL_LEFT]
				&& !keyboard[Input.Keys.CONTROL_RIGHT]) {
			viewer.getSelection().clear();
		}

		viewer.getSelection().add(go);		
		selection.add(go.getSceneElement());
		sceneListener.updateSelection(selection);
		viewer.updateSelectionBounds();

		return true;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		if ( dragging ){
			dragging = false;
			viewer.setDelta(0, 0);
			for (EditableGameObject go : viewer.getSelection()) {
				go.addDelta();
			}
			viewer.updateSelectionBounds();
		}
		return true;
	}

	private boolean dragging = false;
	private int dragX = -1;
	private int dragY = -1;

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		if (!dragging) {
			EditableGameObject go = viewer.get(x, y);
			if (viewer.getSelection().contains(go)) {
				dragging = true;
				dragX = x;
				dragY = y;
			}
		} else {
			int deltaX = x - dragX;
			int deltaY = y - dragY;
			viewer.setDelta(deltaX, deltaY);
			for (EditableGameObject go : viewer.getSelection()) {
				go.setDelta(deltaX, deltaY);
			}

		}
		return true;
	}

	@Override
	public boolean mouseMoved(int x, int y) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}

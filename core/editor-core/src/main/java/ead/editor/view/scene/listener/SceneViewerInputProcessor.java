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
	public boolean touchMoved(int x, int y) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}

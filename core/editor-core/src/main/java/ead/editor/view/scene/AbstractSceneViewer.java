package ead.editor.view.scene;

import java.util.ArrayList;
import java.util.List;

import ead.common.model.elements.scene.EAdScene;
import ead.common.model.elements.scene.EAdSceneElement;
import ead.common.model.elements.variables.VarDef;
import ead.editor.view.scene.listener.SceneListener;

public abstract class AbstractSceneViewer implements SceneViewer {

	protected EAdScene scene;

	protected List<SceneListener> listeners;

	@Override
	public void setScene(EAdScene scene) {
		this.scene = scene;
		this.listeners = new ArrayList<SceneListener>();
	}

	@Override
	public void addFieldChangeListener(SceneListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeFieldChangeListener(SceneListener listener) {
		listeners.remove(listener);
	}

	public <T> void notify(VarDef<T> varDef, EAdSceneElement element, T value) {
		for (SceneListener l : listeners) {
			l.updateInitialValue(varDef, element, value);
		}
	}

}

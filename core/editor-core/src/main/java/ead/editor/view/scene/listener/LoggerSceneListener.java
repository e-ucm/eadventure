package ead.editor.view.scene.listener;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ead.common.model.elements.scene.EAdSceneElement;
import ead.common.model.elements.variables.VarDef;

public class LoggerSceneListener implements SceneListener {

	private final static Logger logger = LoggerFactory.getLogger("SceneLoader");

	@Override
	public <T> void updateInitialValue(VarDef<T> var, EAdSceneElement element,
			T value) {
		logger.debug(
				"Varirable {} change its value to {} in element {}",
				new String[] { var.toString(), value.toString(),
						element.getId() });

	}

	@Override
	public void updateSelection(List<EAdSceneElement> sceneElements) {
		logger.debug("Selection udpated. {} elements selected",
				sceneElements == null ? 0 : sceneElements.size());

	}
}

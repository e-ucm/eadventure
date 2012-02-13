package ead.editor.view.generics.scene.impl;

import ead.common.model.elements.scene.EAdSceneElementDef;
import ead.common.model.elements.scenes.SceneElementDef;
import ead.common.resources.EAdBundleId;

public class SceneElementDefProxy extends SceneElementDef {

	public SceneElementDefProxy(EAdSceneElementDef definition) {
		setId(definition.getId() + "_proxy");
		getResources().addAsset(getResources().getInitialBundle(), 
				SceneElementDef.appearance, 
				definition.getResources().getAsset(definition.getResources().getInitialBundle(), SceneElementDef.appearance));
		for (EAdBundleId bundle : definition.getResources().getBundles()) {
			if (bundle != definition.getResources().getInitialBundle()) {
				getResources().addBundle(bundle);
				getResources().addAsset(bundle, SceneElementDef.appearance, definition.getResources().getAsset(bundle, SceneElementDef.appearance));
			}
		}
	}
}

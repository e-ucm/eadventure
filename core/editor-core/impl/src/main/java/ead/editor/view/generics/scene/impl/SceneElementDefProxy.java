package ead.editor.view.generics.scene.impl;

import ead.common.model.elements.scene.EAdSceneElementDef;
import ead.common.model.elements.scenes.SceneElementDefImpl;
import ead.common.resources.EAdBundleId;

public class SceneElementDefProxy extends SceneElementDefImpl {

	public SceneElementDefProxy(EAdSceneElementDef definition) {
		setId(definition.getId() + "_proxy");
		getResources().addAsset(getResources().getInitialBundle(), 
				SceneElementDefImpl.appearance, 
				definition.getResources().getAsset(definition.getResources().getInitialBundle(), SceneElementDefImpl.appearance));
		for (EAdBundleId bundle : definition.getResources().getBundles()) {
			if (bundle != definition.getResources().getInitialBundle()) {
				getResources().addBundle(bundle);
				getResources().addAsset(bundle, SceneElementDefImpl.appearance, definition.getResources().getAsset(bundle, SceneElementDefImpl.appearance));
			}
		}
	}
}

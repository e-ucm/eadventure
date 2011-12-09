package es.eucm.eadventure.editor.view.generics.scene.impl;

import es.eucm.eadventure.common.model.elements.EAdSceneElementDef;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneElementDefImpl;
import es.eucm.eadventure.common.resources.EAdBundleId;

public class SceneElementDefProxy extends EAdSceneElementDefImpl {

	public SceneElementDefProxy(EAdSceneElementDef definition) {
		setId(definition.getId() + "_proxy");
		getResources().addAsset(getResources().getInitialBundle(), 
				EAdSceneElementDefImpl.appearance, 
				definition.getResources().getAsset(definition.getResources().getInitialBundle(), EAdSceneElementDefImpl.appearance));
		for (EAdBundleId bundle : definition.getResources().getBundles()) {
			if (bundle != definition.getResources().getInitialBundle()) {
				getResources().addBundle(bundle);
				getResources().addAsset(bundle, EAdSceneElementDefImpl.appearance, definition.getResources().getAsset(bundle, EAdSceneElementDefImpl.appearance));
			}
		}
	}
}

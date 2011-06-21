package es.eucm.eadventure.common.model.elmentfactories.scenedemos;

import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneImpl;
import es.eucm.eadventure.common.resources.assets.drawable.impl.ImageImpl;

/**
 * An empty scene
 * 
 */
public class EmptyScene extends EAdSceneImpl {

	public EmptyScene() {
		super("EmptyScene");
		getBackground().getResources().addAsset(
				getBackground().getInitialBundle(),
				EAdBasicSceneElement.appearance,
				new ImageImpl("@drawable/Loading.png"));
	}

}

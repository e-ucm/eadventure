package es.eucm.eadventure.common.model.elements.test;

import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneImpl;
import es.eucm.eadventure.common.resources.assets.drawable.impl.ImageImpl;

public class BasicSceneFactoryTest {
	
	public static EAdScene getBasicScene() {
		EAdSceneImpl sceneImpl = new EAdSceneImpl("scene");
		
		sceneImpl.getBackground().getResources().addAsset(sceneImpl.getBackground().getInitialBundle(), EAdBasicSceneElement.appearance, new ImageImpl("@drawable/background1.png"));
		
		return sceneImpl;
	}

}

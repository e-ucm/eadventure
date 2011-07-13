package es.eucm.eadventure.common.elmentfactories.scenedemos;

import es.eucm.eadventure.common.elmentfactories.EAdElementsFactory;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.resources.assets.drawable.OrientedDrawable;

public class CharacterScene extends EmptyScene {

	private String standUris[] = new String[] { "@drawable/stand_up.png",
			"@drawable/red_stand_right.png", "@drawable/stand_down.png",
			"@drawable/red_stand_left.png" };

	public CharacterScene() {
		OrientedDrawable stand = EAdElementsFactory.getInstance()
				.getDrawableFactory().getOrientedDrawable(standUris);
		EAdBasicSceneElement element = EAdElementsFactory.getInstance()
				.getSceneElementFactory().createSceneElement(stand, 100, 10);
		
		element.setScale(5.0f);
		
		this.getSceneElements().add(element);

	}

}

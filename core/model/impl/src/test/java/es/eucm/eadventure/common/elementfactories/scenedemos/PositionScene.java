package es.eucm.eadventure.common.elementfactories.scenedemos;

import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl.Corner;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.ImageImpl;

public class PositionScene extends EmptyScene {

	public PositionScene() {
		getBackground().getResources().addAsset(
				getBackground().getInitialBundle(),
				EAdBasicSceneElement.appearance,
				new ImageImpl("@drawable/centerbackground.png"));
		
		EAdBasicSceneElement e = new EAdBasicSceneElement( "e", CharacterScene.getStateDrawable());
		e.setVarInitialValue(EAdBasicSceneElement.VAR_SCALE, 3.0f);
		e.setPosition(new EAdPositionImpl(Corner.BOTTOM_CENTER, 400, 300));
		
		this.getElements().add(e);
	}

	@Override
	public String getSceneDescription() {
		return "A scene to tests coners in EAdPositionImpl";
	}

	public String getDemoName() {
		return "Positions Scene";
	}
}

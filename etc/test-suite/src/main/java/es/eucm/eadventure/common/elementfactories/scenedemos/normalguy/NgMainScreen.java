package es.eucm.eadventure.common.elementfactories.scenedemos.normalguy;

import es.eucm.eadventure.common.elementfactories.scenedemos.EmptyScene;
import es.eucm.eadventure.common.model.effects.impl.EAdChangeScene;
import es.eucm.eadventure.common.model.effects.impl.EAdInterpolationEffect;
import es.eucm.eadventure.common.model.effects.impl.enums.InterpolationType;
import es.eucm.eadventure.common.model.effects.impl.enums.InterpolationLoopType;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.events.EAdSceneElementEvent;
import es.eucm.eadventure.common.model.events.enums.SceneElementEventType;
import es.eucm.eadventure.common.model.events.impl.EAdSceneElementEventImpl;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl.Corner;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.ImageImpl;

public class NgMainScreen extends EmptyScene {

	public NgMainScreen() {
		setBackground(new EAdBasicSceneElement( new ImageImpl(
				"@drawable/ng_mainscreen_bg.png")));
		getBackground().setId("background");
		EAdBasicSceneElement spiral = new EAdBasicSceneElement(
				new ImageImpl("@drawable/ng_spiral.png"));
		spiral.setId("spiral");
		spiral.setPosition(Corner.CENTER, 400, 300);
		getComponents().add(spiral);
		EAdBasicSceneElement logo = new EAdBasicSceneElement(
				new ImageImpl("@drawable/ng_logo.png"));
		logo.setId("spiral");
		logo.setPosition(Corner.CENTER, 400, 300);
		getComponents().add(logo);
		logo.setVarInitialValue(EAdBasicSceneElement.VAR_SCALE, 0.0f);

		// Animations
		EAdSceneElementEvent e = new EAdSceneElementEventImpl();
		EAdInterpolationEffect rotate = new EAdInterpolationEffect(spiral,
				EAdBasicSceneElement.VAR_ROTATION, 0, 2 * Math.PI, 50000, 0,
				InterpolationLoopType.RESTART, -1, InterpolationType.DESACCELERATE);
		e.addEffect(SceneElementEventType.ADDED_TO_SCENE, rotate);
		spiral.getEvents().add(e);
		
		e = new EAdSceneElementEventImpl();
		EAdInterpolationEffect bounce = new EAdInterpolationEffect(logo,
				EAdBasicSceneElement.VAR_SCALE, 0.0f, 1.0f, 1000, 1000,
				InterpolationLoopType.NO_LOOP, 1, InterpolationType.LINEAR);
		e.addEffect(SceneElementEventType.ADDED_TO_SCENE, bounce);
		
		EAdChangeScene changeScene = new EAdChangeScene( );
		changeScene.setId("changeScene");
		changeScene.setNextScene(new NgRoom1());
		getBackground().addBehavior(EAdMouseEventImpl.MOUSE_LEFT_PRESSED, changeScene);
		
		logo.getEvents().add(e);
		

	}

	@Override
	public String getSceneDescription() {
		return "A game showing the eAdventure 2.0 features";
	}

	public String getDemoName() {
		return "Normal Guy";
	}

}

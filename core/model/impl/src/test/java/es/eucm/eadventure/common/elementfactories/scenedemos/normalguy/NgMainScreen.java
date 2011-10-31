package es.eucm.eadventure.common.elementfactories.scenedemos.normalguy;

import es.eucm.eadventure.common.elementfactories.scenedemos.EmptyScene;
import es.eucm.eadventure.common.model.effects.impl.EAdChangeScene;
import es.eucm.eadventure.common.model.effects.impl.EAdInterpolationEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdInterpolationEffect.InterpolationType;
import es.eucm.eadventure.common.model.effects.impl.EAdInterpolationEffect.LoopType;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.events.EAdSceneElementEvent;
import es.eucm.eadventure.common.model.events.EAdSceneElementEvent.SceneElementEvent;
import es.eucm.eadventure.common.model.events.impl.EAdSceneElementEventImpl;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl.Corner;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.ImageImpl;

public class NgMainScreen extends EmptyScene {

	public NgMainScreen() {
		setBackground(new EAdBasicSceneElement("background", new ImageImpl(
				"@drawable/ng_mainscreen_bg.png")));
		EAdBasicSceneElement spiral = new EAdBasicSceneElement("spiral",
				new ImageImpl("@drawable/ng_spiral.png"));
		spiral.setPosition(Corner.CENTER, 400, 300);
		getElements().add(spiral);
		EAdBasicSceneElement logo = new EAdBasicSceneElement("spiral",
				new ImageImpl("@drawable/ng_logo.png"));
		logo.setPosition(Corner.CENTER, 400, 300);
		getElements().add(logo);
		logo.setVarInitialValue(EAdBasicSceneElement.VAR_SCALE, 0.0f);

		// Animations
		EAdSceneElementEvent e = new EAdSceneElementEventImpl();
		EAdInterpolationEffect rotate = new EAdInterpolationEffect(spiral,
				EAdBasicSceneElement.VAR_ROTATION, 0, 2 * Math.PI, 50000, 0,
				LoopType.RESTART, -1, InterpolationType.DESACCELERATE);
		e.addEffect(SceneElementEvent.ADDED_TO_SCENE, rotate);
		spiral.getEvents().add(e);
		
		e = new EAdSceneElementEventImpl();
		EAdInterpolationEffect bounce = new EAdInterpolationEffect(logo,
				EAdBasicSceneElement.VAR_SCALE, 0.0f, 1.0f, 1000, 1000,
				LoopType.NO_LOOP, 1, InterpolationType.LINEAR);
		e.addEffect(SceneElementEvent.ADDED_TO_SCENE, bounce);
		
		EAdChangeScene changeScene = new EAdChangeScene("changeScene" );
		changeScene.setNextScene(new NgRoom1());
		getBackground().addBehavior(EAdMouseEventImpl.MOUSE_LEFT_PRESSED, changeScene);
		
		logo.getEvents().add(e);
		

	}

	@Override
	public String getSceneDescription() {
		return "An game showing the eAdventure 2.0 features";
	}

	public String getDemoName() {
		return "Normal Guy";
	}

}

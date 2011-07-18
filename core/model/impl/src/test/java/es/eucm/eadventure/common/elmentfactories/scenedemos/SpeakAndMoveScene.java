package es.eucm.eadventure.common.elmentfactories.scenedemos;

import es.eucm.eadventure.common.elmentfactories.EAdElementsFactory;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdMoveActiveElement;
import es.eucm.eadventure.common.model.effects.impl.sceneelements.EAdMakeActiveElementEffect;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.events.EAdSceneElementEvent;
import es.eucm.eadventure.common.model.events.EAdSceneElementEvent.SceneElementEvent;
import es.eucm.eadventure.common.model.events.impl.EAdSceneElementEventImpl;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.model.params.EAdPosition;
import es.eucm.eadventure.common.model.trajectories.impl.SimpleTrajectoryGenerator;

public class SpeakAndMoveScene extends EmptyScene {

	public SpeakAndMoveScene() {
		EAdBasicSceneElement character = EAdElementsFactory
				.getInstance()
				.getSceneElementFactory()
				.createSceneElement(CharacterScene.getStateDrawable(), 100, 300);

		character.stateVar().setInitialValue(CharacterScene.STATE_STAND);

		character.setScale(3.0f);
		character.setPosition(new EAdPosition(400, 300));

		EAdEffect effect = EAdElementsFactory
				.getInstance()
				.getEffectFactory()
				.getSpeakEffect("Hey! Don't touch me, sir!",
						character.positionXVar(), character.positionYVar());

		character.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, effect);

		this.getSceneElements().add(character);

		EAdMakeActiveElementEffect makeActive = new EAdMakeActiveElementEffect(
				"makeActiveCharacter");
		makeActive.setSceneElement(character);

		EAdSceneElementEvent event = new EAdSceneElementEventImpl(
				"makeAcitveCharacter");
		event.addEffect(SceneElementEvent.ADDED_TO_SCENE, makeActive);
		character.getEvents().add(event);

		setTrajectoryGenerator(new SimpleTrajectoryGenerator(false));

		getBackground().addBehavior(EAdMouseEventImpl.MOUSE_RIGHT_CLICK,
				new EAdMoveActiveElement("moveCharacter"));
	}

}

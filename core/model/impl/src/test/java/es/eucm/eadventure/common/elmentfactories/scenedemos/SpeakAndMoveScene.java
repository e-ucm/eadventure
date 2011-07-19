package es.eucm.eadventure.common.elmentfactories.scenedemos;

import es.eucm.eadventure.common.elmentfactories.EAdElementsFactory;
import es.eucm.eadventure.common.model.effects.impl.EAdMoveActiveElement;
import es.eucm.eadventure.common.model.effects.impl.sceneelements.EAdMakeActiveElementEffect;
import es.eucm.eadventure.common.model.effects.impl.text.EAdSpeakEffect;
import es.eucm.eadventure.common.model.elements.EAdSceneElement.CommonStates;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.events.EAdSceneElementEvent;
import es.eucm.eadventure.common.model.events.EAdSceneElementEvent.SceneElementEvent;
import es.eucm.eadventure.common.model.events.impl.EAdSceneElementEventImpl;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.model.params.EAdPosition;
import es.eucm.eadventure.common.model.trajectories.impl.SimpleTrajectoryGenerator;
import es.eucm.eadventure.common.resources.assets.drawable.impl.BallonShape.BalloonType;

public class SpeakAndMoveScene extends EmptyScene {

	public SpeakAndMoveScene() {
		EAdBasicSceneElement character = EAdElementsFactory
				.getInstance()
				.getSceneElementFactory()
				.createSceneElement(CharacterScene.getStateDrawable(), 100, 300);

		character.stateVar().setInitialValue(CommonStates.EAD_STATE_DEFAULT.toString());

		character.setScale(3.0f);
		character.setPosition(new EAdPosition(400, 300));

		EAdSpeakEffect effect = EAdElementsFactory
				.getInstance()
				.getEffectFactory()
				.getSpeakEffect("Hey! Don't touch me, sir! Somebody said, it could be here. I can't count the reasons I should stay. I'm tired of wait and see... There's another way. Give me some love, somebody said, it could be here, but... I should stay. I'm tired of wait and see... There's another way. Give me some love, somebody said, it could be here, but... I should stay. I'm tired of wait and see... There's another way. Give me some love, somebody said, it could be here, but...",
						character);
		
		effect.setBalloonType(BalloonType.ROUNDED_RECTANGLE);

		character.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, effect);
		
		effect = EAdElementsFactory
				.getInstance()
				.getEffectFactory()
				.getSpeakEffect("WHAT!",
						character);
		
		effect.setBalloonType(BalloonType.ELECTRIC);

		character.addBehavior(EAdMouseEventImpl.MOUSE_DRAG, effect);
		
		effect = EAdElementsFactory
				.getInstance()
				.getEffectFactory()
				.getSpeakEffect("...Shhh. Silence...",
						character);
		
		effect.setBalloonType(BalloonType.CLOUD);

		character.addBehavior(EAdMouseEventImpl.MOUSE_RIGHT_CLICK, effect);

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

package es.eucm.eadventure.common.elementfactories.scenedemos;

import es.eucm.eadventure.common.elementfactories.EAdElementsFactory;
import es.eucm.eadventure.common.elementfactories.scenedemos.normalguy.NgCommon;
import es.eucm.eadventure.common.model.actions.impl.EAdBasicAction;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdActorActionsEffect;
import es.eucm.eadventure.common.model.effects.impl.sceneelements.EAdMoveSceneElement;
import es.eucm.eadventure.common.model.effects.impl.text.EAdSpeakEffect;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.events.EAdSceneElementEvent;
import es.eucm.eadventure.common.model.events.enums.SceneElementEventType;
import es.eucm.eadventure.common.model.events.impl.EAdSceneElementEventImpl;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.model.trajectories.impl.SimpleTrajectoryDefinition;
import es.eucm.eadventure.common.model.variables.impl.SystemFields;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl.Corner;
import es.eucm.eadventure.common.predef.model.effects.EAdMakeActiveElementEffect;
import es.eucm.eadventure.common.predef.model.effects.EAdMoveActiveElement;
import es.eucm.eadventure.common.predef.model.effects.EAdSpeakSceneElement;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.ImageImpl;

public class SpeakAndMoveScene extends EmptyScene {

	public SpeakAndMoveScene() {
		// EAdBasicSceneElement character = EAdElementsFactory
		// .getInstance()
		// .getSceneElementFactory()
		// .createSceneElement(CharacterScene.getStateDrawable(), 100, 300);

		NgCommon.init();
		EAdBasicSceneElement character = new EAdBasicSceneElement(
				NgCommon.getMainCharacter());

		character.setPosition(new EAdPositionImpl(Corner.BOTTOM_CENTER, 400,
				400));

		EAdSpeakEffect effect = new EAdSpeakSceneElement(character);
		EAdElementsFactory
				.getInstance()
				.getStringFactory()
				.setString(
						effect.getString(),
						"Hello, my friend. I have a loooooooooooooooooooooooooooooot of things to say. Will I be able to tell all in one only bubble? Yeah, I didn't think so. So let's move on to the next topic, shall we?Hello, my friend. I have a loooooooooooooooooooooooooooooot of things to say. Will I be able to tell all in one only bubble? Yeah, I didn't think so. So let's move on to the next topic, shall we?Hello, my friend. I have a loooooooooooooooooooooooooooooot of things to say. Will I be able to tell all in one only bubble? Yeah, I didn't think so. So let's move on to the next topic, shall we?Hello, my friend. I have a loooooooooooooooooooooooooooooot of things to say. Will I be able to tell all in one only bubble? Yeah, I didn't think so. So let's move on to the next topic, shall we?Hello, my friend. I have a loooooooooooooooooooooooooooooot of things to say. Will I be able to tell all in one only bubble? Yeah, I didn't think so. So let's move on to the next topic, shall we? Hello, my friend. I have a loooooooooooooooooooooooooooooot of things to say. Will I be able to tell all in one only bubble? Yeah, I didn't think so. So let's move on to the next topic, shall we?");
		// effect.setBalloonType(BalloonType.RECTANGLE);
		// effect.setFont(new EAdFontImpl(18));

		// effect.seta
		character.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_PRESSED, effect);

		this.getComponents().add(character);

		EAdMakeActiveElementEffect makeActive = new EAdMakeActiveElementEffect(
				character);

		EAdSceneElementEvent event = new EAdSceneElementEventImpl();
		event.setId("makeAcitveCharacter");
		event.addEffect(SceneElementEventType.ADDED_TO_SCENE, makeActive);
		character.getEvents().add(event);

		SimpleTrajectoryDefinition d = new SimpleTrajectoryDefinition(false);
		d.setLimits(0, 0, 800, 600);
		setTrajectoryDefinition(d);

		EAdMoveSceneElement move = new EAdMoveSceneElement();
		move.setId("moveCharacter");
		move.setTargetCoordiantes(SystemFields.MOUSE_X, SystemFields.MOUSE_Y);

		getBackground().addBehavior(EAdMouseEventImpl.MOUSE_LEFT_PRESSED,
				new EAdMoveActiveElement());

		EAdBasicSceneElement actionsObject = new EAdBasicSceneElement(
				new ImageImpl("@drawable/infobutton.png"));
		actionsObject.setPosition(100, 100);
		EAdBasicAction action = new EAdBasicAction();
		action.getResources().addAsset(action.getInitialBundle(),
				EAdBasicAction.appearance,
				new ImageImpl("@drawable/examine-normal.png"));
		action.getResources().addAsset(action.getHighlightBundle(),
				EAdBasicAction.appearance,
				new ImageImpl("@drawable/examine-pressed.png"));

		EAdSpeakEffect speak = new EAdSpeakEffect();

		EAdElementsFactory.getInstance().getStringFactory()
				.setString(speak.getString(), "The action was triggered!");

		
		action.getEffects().add(speak);
		actionsObject.getActions().add(action);

		EAdEffect showActions = new EAdActorActionsEffect(actionsObject);
		actionsObject.addBehavior(EAdMouseEventImpl.MOUSE_RIGHT_CLICK,
				showActions);
		getComponents().add(actionsObject);
	}

	@Override
	public String getSceneDescription() {
		return "A scene with a character moving and talking. Press anywhere in the scene to move the character there. Press on the character to make him talk.";
	}

	public String getDemoName() {
		return "Speak and Move Scene";
	}

}

package es.eucm.eadventure.common.elementfactories.scenedemos;

import es.eucm.eadventure.common.elementfactories.EAdElementsFactory;
import es.eucm.eadventure.common.model.conditions.impl.OperationCondition;
import es.eucm.eadventure.common.model.conditions.impl.OperationCondition.Comparator;
import es.eucm.eadventure.common.model.effects.impl.EAdMoveActiveElement;
import es.eucm.eadventure.common.model.effects.impl.sceneelements.EAdMoveSceneElement;
import es.eucm.eadventure.common.model.effects.impl.text.EAdSpeakEffect;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.events.EAdSceneElementEvent;
import es.eucm.eadventure.common.model.events.EAdSceneElementEvent.SceneElementEvent;
import es.eucm.eadventure.common.model.events.impl.EAdSceneElementEventImpl;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.model.trajectories.impl.SimpleTrajectoryDefinition;
import es.eucm.eadventure.common.model.variables.EAdField;
import es.eucm.eadventure.common.model.variables.EAdOperation;
import es.eucm.eadventure.common.model.variables.impl.EAdFieldImpl;
import es.eucm.eadventure.common.model.variables.impl.SystemFields;
import es.eucm.eadventure.common.model.variables.impl.operations.ConditionedOperation;
import es.eucm.eadventure.common.model.variables.impl.operations.MathOperation;
import es.eucm.eadventure.common.params.EAdFontImpl;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl.Corner;
import es.eucm.eadventure.common.predef.model.effects.EAdMakeActiveElementEffect;

public class SpeakAndMoveScene extends EmptyScene {

	public SpeakAndMoveScene() {
		super.setBackgroundFill(EAdColor.GREEN);
		EAdBasicSceneElement character = EAdElementsFactory
				.getInstance()
				.getSceneElementFactory()
				.createSceneElement(CharacterScene.getStateDrawable(), 100, 300);

		character.setScale(3.0f);
		character.setPosition(new EAdPositionImpl(Corner.BOTTOM_CENTER, 400,
				400));

		EAdField<Integer> topField = new EAdFieldImpl<Integer>(character,
				EAdBasicSceneElement.VAR_TOP);
		EAdField<Integer> bottomField = new EAdFieldImpl<Integer>(character,
				EAdBasicSceneElement.VAR_BOTTOM);
		EAdField<Integer> leftField = new EAdFieldImpl<Integer>(character,
				EAdBasicSceneElement.VAR_LEFT);
		EAdField<Integer> rightField = new EAdFieldImpl<Integer>(character,
				EAdBasicSceneElement.VAR_RIGHT);

		EAdCondition c = new OperationCondition(new MathOperation("[0] / 2",
				SystemFields.GUI_HEIGHT), new MathOperation("[0] ", topField),
				Comparator.GREATER);

		ConditionedOperation opY = new ConditionedOperation(c, topField,
				bottomField);
		EAdOperation opX = new MathOperation("([0] + [1] ) / 2", rightField,
				leftField);

		EAdSpeakEffect effect = new EAdSpeakEffect("speak");
		effect.setPosition(opX, opY);
		EAdElementsFactory
				.getInstance()
				.getStringFactory()
				.setString(
						effect.getString(),
						"Hello, my friend. I have a loooooooooooooooooooooooooooooot of things to say. Will I be able to tell all in one only bubble? Yeah, I didn't think so. So let's move on to the next topic, shall we?Hello, my friend. I have a loooooooooooooooooooooooooooooot of things to say. Will I be able to tell all in one only bubble? Yeah, I didn't think so. So let's move on to the next topic, shall we?Hello, my friend. I have a loooooooooooooooooooooooooooooot of things to say. Will I be able to tell all in one only bubble? Yeah, I didn't think so. So let's move on to the next topic, shall we?Hello, my friend. I have a loooooooooooooooooooooooooooooot of things to say. Will I be able to tell all in one only bubble? Yeah, I didn't think so. So let's move on to the next topic, shall we?Hello, my friend. I have a loooooooooooooooooooooooooooooot of things to say. Will I be able to tell all in one only bubble? Yeah, I didn't think so. So let's move on to the next topic, shall we? Hello, my friend. I have a loooooooooooooooooooooooooooooot of things to say. Will I be able to tell all in one only bubble? Yeah, I didn't think so. So let's move on to the next topic, shall we?");
//		effect.setBalloonType(BalloonType.RECTANGLE);
		effect.setFont(new EAdFontImpl(18));

		character.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_PRESSED, effect);

		this.getElements().add(character);

		EAdMakeActiveElementEffect makeActive = new EAdMakeActiveElementEffect(
				character);

		EAdSceneElementEvent event = new EAdSceneElementEventImpl(
				"makeAcitveCharacter");
		event.addEffect(SceneElementEvent.ADDED_TO_SCENE, makeActive);
		character.getEvents().add(event);

		
		SimpleTrajectoryDefinition d = new SimpleTrajectoryDefinition(false);
		d.setLimits(0, 300, 800, 600);
		setTrajectoryDefinition(d);

		EAdMoveSceneElement move = new EAdMoveSceneElement("moveCharacter");
		move.setTargetCoordiantes(SystemFields.MOUSE_X, SystemFields.MOUSE_Y);

		getBackground().addBehavior(EAdMouseEventImpl.MOUSE_LEFT_PRESSED,
				new EAdMoveActiveElement("moveCharacter"));
	}

	@Override
	public String getSceneDescription() {
		return "A scene with a character moving and talking";
	}

	public String getDemoName() {
		return "Speak and Move Scene";
	}

}

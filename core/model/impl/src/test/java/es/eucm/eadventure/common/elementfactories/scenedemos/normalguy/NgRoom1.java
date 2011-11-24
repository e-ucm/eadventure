package es.eucm.eadventure.common.elementfactories.scenedemos.normalguy;

import es.eucm.eadventure.common.elementfactories.EAdElementsFactory;
import es.eucm.eadventure.common.elementfactories.StringFactory;
import es.eucm.eadventure.common.elementfactories.scenedemos.EmptyScene;
import es.eucm.eadventure.common.model.conditions.impl.ANDCondition;
import es.eucm.eadventure.common.model.conditions.impl.NOTCondition;
import es.eucm.eadventure.common.model.conditions.impl.OperationCondition;
import es.eucm.eadventure.common.model.conditions.impl.enums.Comparator;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.effects.impl.sceneelements.EAdMoveSceneElement;
import es.eucm.eadventure.common.model.effects.impl.text.EAdSpeakEffect;
import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeFieldValueEffect;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.events.EAdSceneElementEvent;
import es.eucm.eadventure.common.model.events.enums.SceneElementEventType;
import es.eucm.eadventure.common.model.events.impl.EAdSceneElementEventImpl;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.model.trajectories.impl.SimpleTrajectoryDefinition;
import es.eucm.eadventure.common.model.variables.EAdField;
import es.eucm.eadventure.common.model.variables.EAdVarDef;
import es.eucm.eadventure.common.model.variables.impl.EAdFieldImpl;
import es.eucm.eadventure.common.model.variables.impl.EAdVarDefImpl;
import es.eucm.eadventure.common.model.variables.impl.SystemFields;
import es.eucm.eadventure.common.model.variables.impl.operations.BooleanOperation;
import es.eucm.eadventure.common.model.variables.impl.operations.MathOperation;
import es.eucm.eadventure.common.model.variables.impl.operations.ValueOperation;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl.Corner;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.ImageImpl;

public class NgRoom1 extends EmptyScene {

	private EAdBasicSceneElement ng;
	private EAdBasicSceneElement darkness;
	private OperationCondition isDark;
	private NOTCondition isNotDark;
	private EAdFieldImpl<Boolean> darknessVisible;
	private EAdBasicSceneElement table;
	private EAdBasicSceneElement lamp;
	private EAdBasicSceneElement carpet;
	private EAdBasicSceneElement door;
	private EAdBasicSceneElement portrait;
	private EAdBasicSceneElement key;

	public NgRoom1() {
		NgCommon.init();
		initConditions();
		setBackground(new EAdBasicSceneElement( new ImageImpl(
				"@drawable/ng_room1_bg.png")));
		getBackground().setId("background");

		ng = new EAdBasicSceneElement(NgCommon.getMainCharacter());

		ng.setPosition(Corner.BOTTOM_CENTER, 200, 400);
		ng.setScale(0.8f);

		SimpleTrajectoryDefinition d = new SimpleTrajectoryDefinition(false);
		d.setLimits(150, 380, 800, 600);
		setTrajectoryDefinition(d);

		EAdMoveSceneElement move = new EAdMoveSceneElement();
		move.setId("moveCharacter");
		move.setTargetCoordiantes(SystemFields.MOUSE_X, SystemFields.MOUSE_Y);
		move.setSceneElement(ng);
		move.setUseTrajectory(true);

		getBackground().addBehavior(EAdMouseEventImpl.MOUSE_LEFT_PRESSED, move);
		createElements();
		initConditions();
		addElementsInOrder();

		setDarkness(ng);
		setLamp();
		setPortrait();
		setDoor();
		setKey();
		this.getElements().add(darkness);
	}

	private void initConditions() {
		darknessVisible = new EAdFieldImpl<Boolean>(darkness,
				EAdBasicSceneElement.VAR_VISIBLE);

		isDark = new OperationCondition(darknessVisible);
		isNotDark = new NOTCondition(isDark);
	}

	private void createElements() {
		darkness = new EAdBasicSceneElement( new ImageImpl(
				"@drawable/ng_lights_off.png"));
		darkness.setId("darkness");
		darkness.setPosition(Corner.CENTER, 0, 0);
		table = new EAdBasicSceneElement( new ImageImpl(
				"@drawable/ng_table.png"));
		table.setId("table");
		table.setPosition(Corner.CENTER, 576, 550);
		lamp = new EAdBasicSceneElement( new ImageImpl(
				"@drawable/ng_lamp.png"));
		lamp.setId("lamp");
		lamp.setPosition(Corner.CENTER, 617, 470);

		carpet = new EAdBasicSceneElement( new ImageImpl(
				"@drawable/ng_carpet.png"));
		carpet.setId("table");
		carpet.setPosition(Corner.CENTER, 350, 470);
		door = new EAdBasicSceneElement(new ImageImpl(
				"@drawable/ng_door.png"));
		door.setId("door");
		door.setPosition(Corner.CENTER, 662, 235);
		portrait = new EAdBasicSceneElement( new ImageImpl(
				"@drawable/ng_portrait.png"));
		portrait.setId("portrait");
		portrait.setPosition(Corner.CENTER, 430, 230);

		key = new EAdBasicSceneElement( new ImageImpl(
				"@drawable/ng_key.png"));
		key.setId("key");
		key.setPosition(Corner.CENTER, 430, 230);

	}

	private void addElementsInOrder() {
		getElements().add(door);
		getElements().add(key);
		getElements().add(portrait);
		getElements().add(carpet);
		getElements().add(ng);
		getElements().add(table);
		getElements().add(lamp);
		getElements().add(darkness);
	}

	private void setDarkness(EAdBasicSceneElement ng) {
		EAdSceneElementEvent event = new EAdSceneElementEventImpl();
		EAdChangeFieldValueEffect changeX = new EAdChangeFieldValueEffect( new EAdFieldImpl<Integer>(darkness,
						EAdBasicSceneElement.VAR_X), new EAdFieldImpl<Integer>(
						ng, EAdBasicSceneElement.VAR_CENTER_X));
		changeX.setId("changeX");
		EAdChangeFieldValueEffect changeY = new EAdChangeFieldValueEffect(
				new EAdFieldImpl<Integer>(darkness,
						EAdBasicSceneElement.VAR_Y), new EAdFieldImpl<Integer>(
						ng, EAdBasicSceneElement.VAR_CENTER_Y));
		changeY.setId("changeY");
		event.addEffect(SceneElementEventType.ALWAYS, changeX);
		event.addEffect(SceneElementEventType.ALWAYS, changeY);

		darkness.getEvents().add(event);
	}

	private void setLamp() {
		EAdChangeFieldValueEffect switchLights = new EAdChangeFieldValueEffect(darknessVisible, new BooleanOperation(isNotDark));
		switchLights.setId("switch");
		EAdMoveSceneElement move = moveNg(617, 510);
		move.getFinalEffects().add(switchLights);
		lamp.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_PRESSED, move);
	}

	private void setDoor() {

	}

	private void setPortrait() {

		addText(portrait);
	}

	private void addText(EAdBasicSceneElement portrait) {
		EAdVarDef<Integer> timesClicked = new EAdVarDefImpl<Integer>(
				"timesClicked", Integer.class, 0);
		EAdField<Integer> timesField = new EAdFieldImpl<Integer>(portrait,
				timesClicked);

		EAdChangeFieldValueEffect addTimes = new EAdChangeFieldValueEffect(
				timesField, new MathOperation("[0] + 1 ", timesField));

		
//		ORCondition orConditon = new ORCondition( getTextCondition(0), getTextCondition())
		EAdCondition moveCondition = new ANDCondition(isNotDark, null);
		EAdMoveSceneElement move = moveNg(430, 260);
		move.setCondition(moveCondition);

		portrait.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, move);

		move.getFinalEffects().add(NgCommon.getLookNorthEffect());
		move.getFinalEffects().add(getSpeakEffect(0));
		move.getFinalEffects().add(addTimes);

	}

	private void setKey() {

	}
	
	protected EAdCondition getTextCondition( EAdField<Integer> timesField, int value ){
		OperationCondition op = new OperationCondition(timesField,
				new ValueOperation(value), Comparator.EQUAL);
		return op;
	}

	private String strings[] = new String[] {
			"Hmm. That's me. It's kind of weird. This is not my house. It's a nice photo, anyway.",
			"Yeah, I know. My photo. We talked about it just a moment ago.",
			"Yeah...", "You know, I'm not coming back...",
			"OK. You know what, YOU KNOW WHAT?",
			"There you go. Are you happy now?" };

	private EAdEffect getSpeakEffect(int i) {
		StringFactory sf = EAdElementsFactory.getInstance().getStringFactory();
		EAdSpeakEffect speak = new EAdSpeakEffect();
		speak.setId("effect1");
		sf.setString(speak.getString(), strings[i]);
		return speak;
	}

	private EAdMoveSceneElement moveNg(int x, int y) {
		EAdMoveSceneElement move = new EAdMoveSceneElement();
		move.setId("move");
		move.setSceneElement(ng);
		move.setTargetCoordiantes(x, y);
		return move;
	}

}

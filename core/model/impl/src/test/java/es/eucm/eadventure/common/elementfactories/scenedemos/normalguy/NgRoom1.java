package es.eucm.eadventure.common.elementfactories.scenedemos.normalguy;

import es.eucm.eadventure.common.elementfactories.EAdElementsFactory;
import es.eucm.eadventure.common.elementfactories.StringFactory;
import es.eucm.eadventure.common.elementfactories.scenedemos.EmptyScene;
import es.eucm.eadventure.common.model.conditions.impl.FlagCondition;
import es.eucm.eadventure.common.model.conditions.impl.NOTCondition;
import es.eucm.eadventure.common.model.effects.impl.sceneelements.EAdMoveSceneElement;
import es.eucm.eadventure.common.model.effects.impl.text.EAdSpeakEffect;
import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeFieldValueEffect;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.events.EAdSceneElementEvent;
import es.eucm.eadventure.common.model.events.EAdSceneElementEvent.SceneElementEvent;
import es.eucm.eadventure.common.model.events.impl.EAdSceneElementEventImpl;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.model.trajectories.impl.SimpleTrajectoryDefinition;
import es.eucm.eadventure.common.model.variables.EAdField;
import es.eucm.eadventure.common.model.variables.EAdVarDef;
import es.eucm.eadventure.common.model.variables.impl.EAdFieldImpl;
import es.eucm.eadventure.common.model.variables.impl.EAdVarDefImpl;
import es.eucm.eadventure.common.model.variables.impl.SystemFields;
import es.eucm.eadventure.common.model.variables.impl.operations.BooleanOperation;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl.Corner;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.ImageImpl;

public class NgRoom1 extends EmptyScene {

	private EAdBasicSceneElement ng;
	private EAdBasicSceneElement darkness;
	private FlagCondition isDark;
	private NOTCondition isNotDark;

	public NgRoom1() {
		NgCommon.init();
		setBackground(new EAdBasicSceneElement("background", new ImageImpl(
				"@drawable/ng_room1_bg.png")));

		ng = new EAdBasicSceneElement(NgCommon.getMainCharacter());

		ng.setPosition(Corner.BOTTOM_CENTER, 200, 400);
		ng.setScale(0.8f);

		SimpleTrajectoryDefinition d = new SimpleTrajectoryDefinition(false);
		d.setLimits(150, 380, 800, 600);
		setTrajectoryDefinition(d);

		EAdMoveSceneElement move = new EAdMoveSceneElement("moveCharacter");
		move.setTargetCoordiantes(SystemFields.MOUSE_X, SystemFields.MOUSE_Y);
		move.setSceneElement(ng);
		move.setUseTrajectory(true);

		getBackground().addBehavior(EAdMouseEventImpl.MOUSE_LEFT_PRESSED, move);

		addDarkness(ng);
		addDoor();
		addKey();
		addPortrait();
		addCarpet();
		getElements().add(ng);
		addTable();
		this.getElements().add(darkness);
	}

	private void addDarkness(EAdBasicSceneElement ng) {
		darkness = new EAdBasicSceneElement("darkness", new ImageImpl(
				"@drawable/ng_lights_off.png"));
		darkness.setPosition(Corner.CENTER, 0, 0);

		EAdSceneElementEvent event = new EAdSceneElementEventImpl();
		EAdChangeFieldValueEffect changeX = new EAdChangeFieldValueEffect(
				"changeX", new EAdFieldImpl<Integer>(darkness,
						EAdBasicSceneElement.VAR_X), new EAdFieldImpl<Integer>(
						ng, EAdBasicSceneElement.VAR_CENTER_X));
		EAdChangeFieldValueEffect changeY = new EAdChangeFieldValueEffect(
				"changeY", new EAdFieldImpl<Integer>(darkness,
						EAdBasicSceneElement.VAR_Y), new EAdFieldImpl<Integer>(
						ng, EAdBasicSceneElement.VAR_CENTER_Y));
		event.addEffect(SceneElementEvent.ALWAYS, changeX);
		event.addEffect(SceneElementEvent.ALWAYS, changeY);

		darkness.getEvents().add(event);

	}

	private void addTable() {
		EAdBasicSceneElement table = new EAdBasicSceneElement("table",
				new ImageImpl("@drawable/ng_table.png"));
		table.setPosition(Corner.CENTER, 576, 550);

		getElements().add(table);

		EAdBasicSceneElement lamp = new EAdBasicSceneElement("lamp",
				new ImageImpl("@drawable/ng_lamp.png"));
		lamp.setPosition(Corner.CENTER, 617, 470);

		getElements().add(lamp);

		EAdField<Boolean> darknessVisible = new EAdFieldImpl<Boolean>(darkness,
				EAdBasicSceneElement.VAR_VISIBLE);

		isDark = new FlagCondition(darknessVisible);
		isNotDark = new NOTCondition(isDark);
		EAdChangeFieldValueEffect switchLights = new EAdChangeFieldValueEffect(
				"switch", darknessVisible, new BooleanOperation(isNotDark));
		EAdMoveSceneElement move = moveNg(617, 510);
		move.getFinalEffects().add(switchLights);
		lamp.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_PRESSED, move);
	}

	private void addCarpet() {
		EAdBasicSceneElement carpet = new EAdBasicSceneElement("table",
				new ImageImpl("@drawable/ng_carpet.png"));
		carpet.setPosition(Corner.CENTER, 350, 470);

		getElements().add(carpet);
	}

	private void addDoor() {
		EAdBasicSceneElement door = new EAdBasicSceneElement("table",
				new ImageImpl("@drawable/ng_door.png"));
		door.setPosition(Corner.CENTER, 662, 235);

		getElements().add(door);

	}

	private void addPortrait() {
		EAdBasicSceneElement portrait = new EAdBasicSceneElement("table",
				new ImageImpl("@drawable/ng_portrait.png"));
		portrait.setPosition(Corner.CENTER, 430, 230);

		addText(portrait);

		getElements().add(portrait);
	}

	private void addText(EAdBasicSceneElement portrait) {
		EAdVarDef<Integer> timesClicked = new EAdVarDefImpl<Integer>(
				"timesClicked", Integer.class, 0);
		EAdField<Integer> timesField = new EAdFieldImpl<Integer>(portrait,
				timesClicked);
		EAdMoveSceneElement move = moveNg(430, 260);
		move.setCondition(isNotDark);

		portrait.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, move);

		StringFactory sf = EAdElementsFactory.getInstance().getStringFactory();
		EAdSpeakEffect effect = new EAdSpeakEffect("effect1");

		sf.setString(effect.getString(), "Hmm. That's me.");

	}

	private void addKey() {
		EAdBasicSceneElement key = new EAdBasicSceneElement("table",
				new ImageImpl("@drawable/ng_key.png"));
		key.setPosition(Corner.CENTER, 430, 230);

		getElements().add(key);
	}

	private EAdMoveSceneElement moveNg(int x, int y) {
		EAdMoveSceneElement move = new EAdMoveSceneElement("move");
		move.setSceneElement(ng);
		move.setTargetCoordiantes(x, y);
		return move;
	}

}

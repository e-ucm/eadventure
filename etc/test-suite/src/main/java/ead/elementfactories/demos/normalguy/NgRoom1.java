/**
 * eAdventure (formerly <e-Adventure> and <e-Game>) is a research project of the
 *    <e-UCM> research group.
 *
 *    Copyright 2005-2010 <e-UCM> research group.
 *
 *    You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *
 *    <e-UCM> is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *
 *          C Profesor Jose Garcia Santesmases sn,
 *          28040 Madrid (Madrid), Spain.
 *
 *          For more info please visit:  <http://e-adventure.e-ucm.es> or
 *          <http://www.e-ucm.es>
 *
 * ****************************************************************************
 *
 *  This file is part of eAdventure, version 2.0
 *
 *      eAdventure is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      eAdventure is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with eAdventure.  If not, see <http://www.gnu.org/licenses/>.
 */

package ead.elementfactories.demos.normalguy;

import ead.common.model.elements.EAdCondition;
import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.conditions.ANDCond;
import ead.common.model.elements.conditions.NOTCond;
import ead.common.model.elements.conditions.OperationCond;
import ead.common.model.elements.conditions.enums.Comparator;
import ead.common.model.elements.effects.sceneelements.MoveSceneElementEf;
import ead.common.model.elements.effects.text.SpeakEf;
import ead.common.model.elements.effects.variables.ChangeFieldEf;
import ead.common.model.elements.events.SceneElementEv;
import ead.common.model.elements.events.enums.SceneElementEventType;
import ead.common.model.elements.guievents.EAdMouseEvent;
import ead.common.model.elements.scenes.SceneElementImpl;
import ead.common.model.elements.trajectories.SimpleTrajectoryDefinition;
import ead.common.model.elements.variables.EAdField;
import ead.common.model.elements.variables.EAdVarDef;
import ead.common.model.elements.variables.FieldImpl;
import ead.common.model.elements.variables.SystemFields;
import ead.common.model.elements.variables.VarDefImpl;
import ead.common.model.elements.variables.operations.BooleanOp;
import ead.common.model.elements.variables.operations.MathOp;
import ead.common.model.elements.variables.operations.ValueOp;
import ead.common.resources.assets.drawable.basics.ImageImpl;
import ead.common.util.EAdPosition.Corner;
import ead.elementfactories.EAdElementsFactory;
import ead.elementfactories.StringFactory;
import ead.elementfactories.demos.scenes.EmptyScene;

public class NgRoom1 extends EmptyScene {

	private SceneElementImpl ng;
	private SceneElementImpl darkness;
	private OperationCond isDark;
	private NOTCond isNotDark;
	private FieldImpl<Boolean> darknessVisible;
	private SceneElementImpl table;
	private SceneElementImpl lamp;
	private SceneElementImpl carpet;
	private SceneElementImpl door;
	private SceneElementImpl portrait;
	private SceneElementImpl key;

	public NgRoom1() {
		NgCommon.init();
		initConditions();
		setBackground(new SceneElementImpl( new ImageImpl(
				"@drawable/ng_room1_bg.png")));
		getBackground().setId("background");

		ng = new SceneElementImpl(NgCommon.getMainCharacter());

		ng.setPosition(Corner.BOTTOM_CENTER, 200, 400);
		ng.setScale(0.8f);

		SimpleTrajectoryDefinition d = new SimpleTrajectoryDefinition(false);
		d.setLimits(150, 380, 800, 600);
		setTrajectoryDefinition(d);

		MoveSceneElementEf move = new MoveSceneElementEf();
		move.setId("moveCharacter");
		move.setTargetCoordiantes(SystemFields.MOUSE_SCENE_X, SystemFields.MOUSE_SCENE_Y);
		move.setSceneElement(ng);
		move.setUseTrajectory(true);

		getBackground().addBehavior(EAdMouseEvent.MOUSE_LEFT_PRESSED, move);
		createElements();
		initConditions();
		addElementsInOrder();

		setDarkness(ng);
		setLamp();
		setPortrait();
		setDoor();
		setKey();
		this.getComponents().add(darkness);
	}

	private void initConditions() {
		darknessVisible = new FieldImpl<Boolean>(darkness,
				SceneElementImpl.VAR_VISIBLE);

		isDark = new OperationCond(darknessVisible);
		isNotDark = new NOTCond(isDark);
	}

	private void createElements() {
		darkness = new SceneElementImpl( new ImageImpl(
				"@drawable/ng_lights_off.png"));
		darkness.setId("darkness");
		darkness.setPosition(Corner.CENTER, 0, 0);
		table = new SceneElementImpl( new ImageImpl(
				"@drawable/ng_table.png"));
		table.setId("table");
		table.setPosition(Corner.CENTER, 576, 550);
		lamp = new SceneElementImpl( new ImageImpl(
				"@drawable/ng_lamp.png"));
		lamp.setId("lamp");
		lamp.setPosition(Corner.CENTER, 617, 470);

		carpet = new SceneElementImpl( new ImageImpl(
				"@drawable/ng_carpet.png"));
		carpet.setId("table");
		carpet.setPosition(Corner.CENTER, 350, 470);
		door = new SceneElementImpl(new ImageImpl(
				"@drawable/ng_door.png"));
		door.setId("door");
		door.setPosition(Corner.CENTER, 662, 235);
		portrait = new SceneElementImpl( new ImageImpl(
				"@drawable/ng_portrait.png"));
		portrait.setId("portrait");
		portrait.setPosition(Corner.CENTER, 430, 230);

		key = new SceneElementImpl( new ImageImpl(
				"@drawable/ng_key.png"));
		key.setId("key");
		key.setPosition(Corner.CENTER, 430, 230);

	}

	private void addElementsInOrder() {
		getComponents().add(door);
		getComponents().add(key);
		getComponents().add(portrait);
		getComponents().add(carpet);
		getComponents().add(ng);
		getComponents().add(table);
		getComponents().add(lamp);
		getComponents().add(darkness);
	}

	private void setDarkness(SceneElementImpl ng) {
		SceneElementEv event = new SceneElementEv();
		ChangeFieldEf changeX = new ChangeFieldEf( new FieldImpl<Integer>(darkness,
						SceneElementImpl.VAR_X), new FieldImpl<Integer>(
						ng, SceneElementImpl.VAR_CENTER_X));
		changeX.setId("changeX");
		ChangeFieldEf changeY = new ChangeFieldEf(
				new FieldImpl<Integer>(darkness,
						SceneElementImpl.VAR_Y), new FieldImpl<Integer>(
						ng, SceneElementImpl.VAR_CENTER_Y));
		changeY.setId("changeY");
		event.addEffect(SceneElementEventType.ALWAYS, changeX);
		event.addEffect(SceneElementEventType.ALWAYS, changeY);

		darkness.getEvents().add(event);
	}

	private void setLamp() {
		ChangeFieldEf switchLights = new ChangeFieldEf(darknessVisible, new BooleanOp(isNotDark));
		switchLights.setId("switch");
		MoveSceneElementEf move = moveNg(617, 510);
		move.getNextEffects().add(switchLights);
		lamp.addBehavior(EAdMouseEvent.MOUSE_LEFT_PRESSED, move);
	}

	private void setDoor() {

	}

	private void setPortrait() {

		addText(portrait);
	}

	private void addText(SceneElementImpl portrait) {
		EAdVarDef<Integer> timesClicked = new VarDefImpl<Integer>(
				"timesClicked", Integer.class, 0);
		EAdField<Integer> timesField = new FieldImpl<Integer>(portrait,
				timesClicked);

		ChangeFieldEf addTimes = new ChangeFieldEf(
				timesField, new MathOp("[0] + 1 ", timesField));

		
//		ORCondition orConditon = new ORCondition( getTextCondition(0), getTextCondition())
		EAdCondition moveCondition = new ANDCond(isNotDark, null);
		MoveSceneElementEf move = moveNg(430, 260);
		move.setCondition(moveCondition);

		portrait.addBehavior(EAdMouseEvent.MOUSE_LEFT_CLICK, move);

		move.getNextEffects().add(NgCommon.getLookNorthEffect());
		move.getNextEffects().add(getSpeakEffect(0));
		move.getNextEffects().add(addTimes);

	}

	private void setKey() {

	}
	
	protected EAdCondition getTextCondition( EAdField<Integer> timesField, int value ){
		OperationCond op = new OperationCond(timesField,
				new ValueOp(value), Comparator.EQUAL);
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
		SpeakEf speak = new SpeakEf();
		speak.setId("effect1");
		sf.setString(speak.getString(), strings[i]);
		return speak;
	}

	private MoveSceneElementEf moveNg(int x, int y) {
		MoveSceneElementEf move = new MoveSceneElementEf();
		move.setId("move");
		move.setSceneElement(ng);
		move.setTargetCoordiantes(x, y);
		return move;
	}

}

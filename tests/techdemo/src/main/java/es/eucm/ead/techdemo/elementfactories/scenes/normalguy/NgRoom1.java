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

package es.eucm.ead.techdemo.elementfactories.scenes.normalguy;

import es.eucm.ead.model.assets.drawable.basics.Image;
import es.eucm.ead.model.elements.conditions.ANDCond;
import es.eucm.ead.model.elements.conditions.Condition;
import es.eucm.ead.model.elements.conditions.NOTCond;
import es.eucm.ead.model.elements.conditions.OperationCond;
import es.eucm.ead.model.elements.conditions.enums.Comparator;
import es.eucm.ead.model.elements.effects.*;
import es.eucm.ead.model.elements.effects.enums.InterpolationLoopType;
import es.eucm.ead.model.elements.effects.enums.InterpolationType;
import es.eucm.ead.model.elements.effects.sceneelements.MoveSceneElementEf;
import es.eucm.ead.model.elements.effects.timedevents.WaitEf;
import es.eucm.ead.model.elements.effects.variables.ChangeFieldEf;
import es.eucm.ead.model.elements.events.SceneElementEv;
import es.eucm.ead.model.elements.events.TimedEv;
import es.eucm.ead.model.elements.events.enums.SceneElementEvType;
import es.eucm.ead.model.elements.events.enums.TimedEvType;
import es.eucm.ead.model.elements.extra.EAdList;
import es.eucm.ead.model.elements.operations.ElementField;
import es.eucm.ead.model.elements.operations.MathOp;
import es.eucm.ead.model.elements.operations.SystemFields;
import es.eucm.ead.model.elements.operations.ValueOp;
import es.eucm.ead.model.elements.predef.effects.SpeakSceneElementEf;
import es.eucm.ead.model.elements.scenes.Scene;
import es.eucm.ead.model.elements.scenes.SceneElement;
import es.eucm.ead.model.elements.trajectories.SimpleTrajectory;
import es.eucm.ead.model.params.guievents.DragGEv;
import es.eucm.ead.model.params.guievents.MouseGEv;
import es.eucm.ead.model.params.guievents.enums.DragGEvType;
import es.eucm.ead.model.params.text.EAdString;
import es.eucm.ead.model.params.util.Position.Corner;
import es.eucm.ead.techdemo.elementfactories.EAdElementsFactory;
import es.eucm.ead.techdemo.elementfactories.StringFactory;
import es.eucm.ead.techdemo.elementfactories.scenes.scenes.EmptyScene;

/**
 * First scene. Main character does not know where he is. Everything is dark in
 * here. Where will be the light?
 */
public class NgRoom1 extends EmptyScene {

	private SceneElement ng;
	private SceneElement darkness;
	private OperationCond isDark;
	private NOTCond isNotDark;
	private ElementField darknessAlpha;
	private SceneElement table;
	private SceneElement lamp;
	private SceneElement carpet;
	private SceneElement door;
	private SceneElement portrait;
	private SceneElement key;
	private ElementField timesField;

	// private Scene initScene;

	public NgRoom1() {
		// this.initScene = initScene;
		// NgCommon.init();
		initConditions();
		setBackground(new SceneElement(new Image("@drawable/ng_room1_bg.png")));

		ng = new SceneElement(NgCommon.getMainCharacter());

		ng.setPosition(Corner.BOTTOM_CENTER, 200, 400);
		ng.setScale(0.8f);

		SimpleTrajectory d = new SimpleTrajectory(false);
		d.setLimits(150, 380, 800, 600);
		setTrajectoryDefinition(d);

		MoveSceneElementEf move = new MoveSceneElementEf();
		move.setTargetCoordiantes(SystemFields.MOUSE_SCENE_X,
				SystemFields.MOUSE_SCENE_Y);
		move.setSceneElement(ng);
		move.setUseTrajectory(true);

		getBackground().addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, move);
		createElements();
		initConditions();
		addElementsInOrder();

	}

	private void initConditions() {
		darknessAlpha = new ElementField(darkness, SceneElement.VAR_ALPHA);

		isDark = new OperationCond(darknessAlpha, new ValueOp(0.9f),
				Comparator.GREATER_EQUAL);
		isNotDark = new NOTCond(isDark);
	}

	private void createElements() {
		darkness = new SceneElement(new Image("@drawable/ng_lights_off.png"));
		darkness.setPosition(Corner.CENTER, 0, 0);
		darkness.setEnable(false);

		table = new SceneElement(new Image("@drawable/ng_table.png"));
		table.setPosition(Corner.CENTER, 576, 550);

		lamp = new SceneElement(new Image("@drawable/ng_lamp.png"));
		lamp.setPosition(Corner.CENTER, 617, 470);

		carpet = new SceneElement(new Image("@drawable/ng_carpet.png"));
		carpet.setPosition(Corner.CENTER, 350, 470);

		door = new SceneElement(new Image("@drawable/ng_door.png"));
		door.setPosition(Corner.CENTER, 662, 235);

		portrait = new SceneElement(new Image("@drawable/ng_portrait.png"));
		portrait.setPosition(Corner.CENTER, 430, 230);

		key = new SceneElement(new Image("@drawable/ng_key.png"));
		key.setPosition(Corner.CENTER, 430, 230);

	}

	private void addElementsInOrder() {
		getSceneElements().add(door);
		getSceneElements().add(key);
		getSceneElements().add(portrait);
		getSceneElements().add(carpet);
		getSceneElements().add(ng);
		getSceneElements().add(table);
		getSceneElements().add(lamp);
		getSceneElements().add(darkness);
	}

	public void setUpSceneElements(Scene corridor) {
		setDarkness(ng);
		setMainCharactersSpeech();
		setLamp();
		setPortrait();
		setDoor(corridor);
		setKey();
		addLightBlink();
		setCarpet();
		setTable();
		this.getSceneElements().add(darkness);
	}

	/**
	 * Lets main character's move over the carpet
	 */
	private void setCarpet() {
		MoveSceneElementEf move = new MoveSceneElementEf();
		move.setTargetCoordiantes(SystemFields.MOUSE_SCENE_X,
				SystemFields.MOUSE_SCENE_Y);
		move.setSceneElement(ng);
		carpet.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, move);
	}

	/**
	 * Lets main character's move to the door
	 */
	private void setTable() {
		MoveSceneElementEf move = new MoveSceneElementEf();
		move.setTargetCoordiantes(SystemFields.MOUSE_SCENE_X,
				SystemFields.MOUSE_SCENE_Y);
		move.setSceneElement(ng);
		table.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, move);
	}

	/**
	 * When the user clicks on the main character, it looks to the user and a
	 * text appears
	 */
	private void setMainCharactersSpeech() {
		SpeakSceneElementEf speech = new SpeakSceneElementEf(ng, new EAdString(
				"n.1"));

		speech.getNextEffects().add(NgCommon.getLookSouthEffect());

		speech.setElement(ng);
		ng.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, speech);

	}

	/**
	 * Puts the darkness in the room
	 * 
	 * @param ng
	 */
	private void setDarkness(SceneElement ng) {
		SceneElementEv event = new SceneElementEv();
		ChangeFieldEf changeX = new ChangeFieldEf(new ElementField(darkness,
				SceneElement.VAR_X), new ElementField(ng,
				SceneElement.VAR_CENTER_X));
		ChangeFieldEf changeY = new ChangeFieldEf(new ElementField(darkness,
				SceneElement.VAR_Y), new ElementField(ng,
				SceneElement.VAR_CENTER_Y));
		event.addEffect(SceneElementEvType.ALWAYS, changeX);
		event.addEffect(SceneElementEvType.ALWAYS, changeY);

		darkness.addEvent(event);
	}

	private void setLamp() {
		ChangeFieldEf switchLights = new ChangeFieldEf(darknessAlpha,
				new ValueOp(0.0f));
		MoveSceneElementEf move = moveNg(617, 510);
		move.getNextEffects().add(switchLights);

		lamp.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, move);
	}

	private void setDoor(Scene corridor) {
		// Principal character moving to the door
		MoveSceneElementEf move = moveNg(662, 235);
		door.addBehavior(new DragGEv(key.getDefinition().getId(),
				DragGEvType.DROP), move);

		// Define next scene, add next behavior
		ChangeSceneEf corridorScene = new ChangeSceneEf();
		corridorScene.setNextScene(corridor);
		move.getNextEffects().add(corridorScene);
	}

	private void setPortrait() {

		addText(portrait);
	}

	private void addText(SceneElement portrait) {
		timesField = new ElementField(portrait, "timesClicked");

		ChangeFieldEf addTimes = new ChangeFieldEf(timesField, new MathOp(
				"[0] + 1 ", timesField));

		// ORCondition orConditon = new ORCondition( getTextCondition(0),
		// getTextCondition())
		Condition moveCondition = new ANDCond(isNotDark, null);
		MoveSceneElementEf move = moveNg(430, 260);
		move.setCondition(moveCondition);

		portrait.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, move);

		move.getNextEffects().add(NgCommon.getLookNorthEffect());

		TriggerMacroEf triggerMacro = new TriggerMacroEf();

		for (int i = 0; i < 4; i++) {
			EAdList<Effect> macro = new EAdList<Effect>();
			macro.add(getSpeakEffect(i));
			OperationCond cond1 = new OperationCond(timesField, i,
					Comparator.EQUAL);
			triggerMacro.putEffects(cond1, macro);
		}

		OperationCond cond = new OperationCond(timesField, 4, Comparator.EQUAL);
		InterpolationEf portraitGoDown = new InterpolationEf(new ElementField(
				portrait, SceneElement.VAR_Y), 0, 80, 200,
				InterpolationLoopType.NO_LOOP, InterpolationType.BOUNCE_END);

		Effect e1 = getSpeakEffect(4);
		Effect e3 = getSpeakEffect(5);
		e1.getNextEffects().add(portraitGoDown);
		portraitGoDown.getNextEffects().add(e3);

		EAdList<Effect> macro2 = new EAdList<Effect>();
		macro2.add(e1);
		triggerMacro.putEffects(cond, macro2);

		move.getNextEffects().add(triggerMacro);
		triggerMacro.getNextEffects().add(addTimes);

	}

	private void setKey() {
		DragEf e = new DragEf();
		e.setCondition(new OperationCond(timesField, 4, Comparator.GREATER));
		key.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, e);
	}

	protected Condition getTextCondition(ElementField timesField, int value) {
		OperationCond op = new OperationCond(timesField, new ValueOp(value),
				Comparator.EQUAL);
		return op;
	}

	private String strings[] = new String[] {
			"Hmm. That's me. It's kind of weird. This is not my house. It's a nice photo, anyway.",
			"Yeah, I know. My photo. We talked about it just a moment ago.",
			"Yeah...", "You know, I'm not coming back...",
			"OK. You know what, YOU KNOW WHAT?",
			"There you go. Are you happy now?" };

	private Effect getSpeakEffect(int i) {
		StringFactory sf = EAdElementsFactory.getInstance().getStringFactory();
		SpeakSceneElementEf speak = new SpeakSceneElementEf(ng, new EAdString(
				"n.23"));
		sf.setString(speak.getString(), strings[i]);
		return speak;
	}

	private MoveSceneElementEf moveNg(int x, int y) {
		MoveSceneElementEf move = new MoveSceneElementEf();
		move.setSceneElement(ng);
		move.setTargetCoordiantes(x, y);
		return move;
	}

	private void addLightBlink() {
		float blink = 0.02f;
		TimedEv blinkEvent = new TimedEv();
		blinkEvent.setTime(500);
		blinkEvent.setRepeats(-1);

		MathOp op1 = new MathOp("[0] + " + blink, darknessAlpha);
		MathOp op2 = new MathOp("[0] - " + blink, darknessAlpha);

		ChangeFieldEf up = new ChangeFieldEf(darknessAlpha, op1);
		ChangeFieldEf down = new ChangeFieldEf(darknessAlpha, op2);
		WaitEf wait = new WaitEf();
		wait.setTime(50);

		up.addNextEffect(wait);
		wait.addNextEffect(down);
		up.setCondition(isNotDark);

		blinkEvent.addEffect(TimedEvType.END_TIME, up);
		lamp.addEvent(blinkEvent);
	}

}

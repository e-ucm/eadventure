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

package ead.demos.elementfactories.scenes.scenes;

import ead.common.model.assets.drawable.basics.Caption;
import ead.common.model.assets.drawable.basics.Image;
import ead.common.model.assets.text.BasicFont;
import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.effects.ActorActionsEf;
import ead.common.model.elements.effects.InterpolationEf;
import ead.common.model.elements.effects.text.SpeakEf;
import ead.common.model.elements.effects.variables.ChangeFieldEf;
import ead.common.model.elements.events.SceneElementEv;
import ead.common.model.elements.events.enums.SceneElementEvType;
import ead.common.model.elements.extra.EAdList;
import ead.common.model.elements.operations.BasicField;
import ead.common.model.elements.operations.ValueOp;
import ead.common.model.elements.predef.effects.MakeActiveElementEf;
import ead.common.model.elements.predef.effects.MoveActiveElementToMouseEf;
import ead.common.model.elements.predef.effects.SpeakSceneElementEf;
import ead.common.model.elements.scenes.BasicScene;
import ead.common.model.elements.scenes.EAdSceneElement;
import ead.common.model.elements.scenes.EAdSceneElementDef;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.scenes.SceneElementDef;
import ead.common.model.elements.trajectories.EAdTrajectory;
import ead.common.model.elements.trajectories.SimpleTrajectory;
import ead.common.model.params.fills.Paint;
import ead.common.model.params.guievents.MouseGEv;
import ead.common.model.params.text.EAdString;
import ead.common.model.params.util.Position;
import ead.common.model.params.util.Position.Corner;
import ead.demos.elementfactories.EAdElementsFactory;
import ead.demos.elementfactories.scenes.normalguy.NgCommon;

public class SpeakAndMoveScene extends EmptyScene {

	private int dispY = 0;
	private BasicField<EAdTrajectory> trajectoryField;

	public SpeakAndMoveScene() {
		this.setId("SpeakAndMoveScene");
		// EAdBasicSceneElement character = EAdElementsFactory
		// .getInstance()
		// .getSceneElementFactory()
		// .createSceneElement(CharacterScene.getStateDrawable(), 100, 300);

		NgCommon.init();
		SceneElement character = new SceneElement(NgCommon.getMainCharacter());
		character.setInitialAlpha(0.5f);
		character.setPosition(new Position(Corner.BOTTOM_CENTER, 400, 400));

		SpeakEf effect = new SpeakSceneElementEf(character, new EAdString(
				"techDemo.SpeakAndMoveScene.longText"));
		effect.setFont(new BasicFont(20.0f));
		// effect.setBalloonType(BalloonType.RECTANGLE);
		// effect.setFont(new EAdFontImpl(18));

		// effect.seta
		character.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, effect);

		this.getSceneElements().add(character);

		MakeActiveElementEf makeActive = new MakeActiveElementEf(character);

		SceneElementEv event = new SceneElementEv();
		event.addEffect(SceneElementEvType.INIT, makeActive);
		character.getEvents().add(event);

		addBehavior(MouseGEv.MOUSE_LEFT_PRESSED,
				new MoveActiveElementToMouseEf());

		SceneElement actionsObject = new SceneElement(new Image(
				"@drawable/infobutton.png"));
		actionsObject.setPosition(100, 100);
		SceneElementDef action = new SceneElementDef();
		action.addAsset(SceneElementDef.appearance, new Image(
				"@drawable/examine-normal.png"));
		action.addAsset(SceneElementDef.overAppearance, new Image(
				"@drawable/examine-pressed.png"));

		SpeakEf speak = new SpeakEf("ead.speakandmoveScene");

		InterpolationEf move = new InterpolationEf();
		move.setInterpolationTime(1000);
		move.addField(character.getField(SceneElement.VAR_X), actionsObject
				.getField(SceneElement.VAR_CENTER_X));
		move.addField(character.getField(SceneElement.VAR_Y), actionsObject
				.getField(SceneElement.VAR_CENTER_Y));
		move.getNextEffects().add(speak);
		move.setRelative(false);

		EAdElementsFactory.getInstance().getStringFactory().setString(
				speak.getString(), "The action was triggered!");

		action.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, move);

		EAdList<EAdSceneElementDef> actions = new EAdList<EAdSceneElementDef>();

		actions.add(action);

		actionsObject.setId("ActionsObject");

		actionsObject.getDefinition().setVarInitialValue(
				ActorActionsEf.VAR_ACTIONS, actions);

		EAdEffect showActions = new ActorActionsEf(actionsObject
				.getDefinition());
		actionsObject.addBehavior(MouseGEv.MOUSE_RIGHT_PRESSED, showActions);
		getSceneElements().add(actionsObject);

		// Trajectories

		trajectoryField = new BasicField<EAdTrajectory>(this,
				BasicScene.VAR_TRAJECTORY_DEFINITION);

		SimpleTrajectory freeWalk = new SimpleTrajectory();
		setTrajectoryDefinition(freeWalk);

		getSceneElements().add(
				getChangeTrajectory(freeWalk,
						"techDemo.SpeakAndMoveScene.freewalk"));

		SimpleTrajectory onlyHorizontal = new SimpleTrajectory();
		onlyHorizontal.setOnlyHorizontal(true);

		getSceneElements().add(
				getChangeTrajectory(onlyHorizontal,
						"techDemo.SpeakAndMoveScene.onlyhorizontal"));

		SimpleTrajectory onlyHorizontalWithLimits = new SimpleTrajectory();
		onlyHorizontalWithLimits.setOnlyHorizontal(true);
		onlyHorizontalWithLimits.setFreeWalk(false);
		onlyHorizontalWithLimits.setLeft(40);
		onlyHorizontalWithLimits.setRight(400);

		getSceneElements().add(
				getChangeTrajectory(onlyHorizontalWithLimits,
						"techDemo.SpeakAndMoveScene.onlyhorizontallimits"));

		SimpleTrajectory rectangleTrajectory = new SimpleTrajectory();
		rectangleTrajectory.setOnlyHorizontal(false);
		rectangleTrajectory.setFreeWalk(false);
		rectangleTrajectory.setTop(50);
		rectangleTrajectory.setBottom(400);
		rectangleTrajectory.setLeft(40);
		rectangleTrajectory.setRight(500);

		getSceneElements().add(
				getChangeTrajectory(rectangleTrajectory,
						"techDemo.SpeakAndMoveScene.rectangle"));
	}

	private EAdSceneElement getChangeTrajectory(SimpleTrajectory freeWalk,
			String string) {
		dispY += 50;
		Caption c = new Caption(string);
		c.setBubblePaint(Paint.BLACK_ON_WHITE);
		c.setPadding(5);
		SceneElement e = new SceneElement(c);
		e.setPosition(10, dispY);
		e.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, new ChangeFieldEf(
				trajectoryField, new ValueOp(freeWalk)));
		return e;
	}

}

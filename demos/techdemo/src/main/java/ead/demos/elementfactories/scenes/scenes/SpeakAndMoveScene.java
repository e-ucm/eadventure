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

import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.effects.ActorActionsEf;
import ead.common.model.elements.effects.InterpolationEf;
import ead.common.model.elements.effects.text.SpeakEf;
import ead.common.model.elements.events.SceneElementEv;
import ead.common.model.elements.events.enums.SceneElementEvType;
import ead.common.model.elements.extra.EAdList;
import ead.common.model.elements.extra.EAdListImpl;
import ead.common.model.elements.guievents.MouseGEv;
import ead.common.model.elements.scenes.EAdSceneElementDef;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.scenes.SceneElementDef;
import ead.common.model.elements.trajectories.SimpleTrajectoryDefinition;
import ead.common.model.predef.effects.MakeActiveElementEf;
import ead.common.model.predef.effects.MoveActiveElementToMouseEf;
import ead.common.model.predef.effects.SpeakSceneElementEf;
import ead.common.resources.assets.drawable.basics.Image;
import ead.common.resources.assets.text.BasicFont;
import ead.common.util.EAdPosition;
import ead.common.util.EAdPosition.Corner;
import ead.demos.elementfactories.EAdElementsFactory;
import ead.demos.elementfactories.scenes.normalguy.NgCommon;

public class SpeakAndMoveScene extends EmptyScene {

	public SpeakAndMoveScene() {
		this.setId("SpeakAndMoveScene");
		// EAdBasicSceneElement character = EAdElementsFactory
		// .getInstance()
		// .getSceneElementFactory()
		// .createSceneElement(CharacterScene.getStateDrawable(), 100, 300);

		NgCommon.init();
		SceneElement character = new SceneElement(NgCommon.getMainCharacter());
		character.setInitialAlpha(0.5f);
		character.setPosition(new EAdPosition(Corner.BOTTOM_CENTER, 400, 400));

		SpeakEf effect = new SpeakSceneElementEf(character);
		effect.setFont(new BasicFont(20.0f));
		EAdElementsFactory
				.getInstance()
				.getStringFactory()
				.setString(
						effect.getString(),
						"Hello, my friend. I have a loooooooooooooooooooooooooooooot of things to say. Will I be able to tell all in one only bubble? Yeah, I didn't think so. So let's move on to the next topic, shall we?Hello, my friend. I have a loooooooooooooooooooooooooooooot of things to say. Will I be able to tell all in one only bubble? Yeah, I didn't think so. So let's move on to the next topic, shall we?Hello, my friend. I have a loooooooooooooooooooooooooooooot of things to say. Will I be able to tell all in one only bubble? Yeah, I didn't think so. So let's move on to the next topic, shall we?Hello, my friend. I have a loooooooooooooooooooooooooooooot of things to say. Will I be able to tell all in one only bubble? Yeah, I didn't think so. So let's move on to the next topic, shall we?Hello, my friend. I have a loooooooooooooooooooooooooooooot of things to say. Will I be able to tell all in one only bubble? Yeah, I didn't think so. So let's move on to the next topic, shall we? Hello, my friend. I have a loooooooooooooooooooooooooooooot of things to say. Will I be able to tell all in one only bubble? Yeah, I didn't think so. So let's move on to the next topic, shall we?");
		// effect.setBalloonType(BalloonType.RECTANGLE);
		// effect.setFont(new EAdFontImpl(18));

		// effect.seta
		character.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, effect);

		this.getSceneElements().add(character);

		MakeActiveElementEf makeActive = new MakeActiveElementEf(character);

		SceneElementEv event = new SceneElementEv();
		event.addEffect(SceneElementEvType.FIRST_UPDATE, makeActive);
		character.getEvents().add(event);

		SimpleTrajectoryDefinition d = new SimpleTrajectoryDefinition(false);
		d.setLimits(0, 0, 800, 600);
		setTrajectoryDefinition(d);

		getBackground().addBehavior(MouseGEv.MOUSE_LEFT_PRESSED,
				new MoveActiveElementToMouseEf());

		SceneElement actionsObject = new SceneElement(new Image(
				"@drawable/infobutton.png"));
		actionsObject.setPosition(100, 100);
		SceneElementDef action = new SceneElementDef();
		action.getResources().addAsset(action.getInitialBundle(),
				SceneElementDef.appearance,
				new Image("@drawable/examine-normal.png"));

		SpeakEf speak = new SpeakEf();

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

		EAdList<EAdSceneElementDef> actions = new EAdListImpl<EAdSceneElementDef>(
				EAdSceneElementDef.class);

		actions.add(action);

		actionsObject.setId("ActionsObject");

		actionsObject.getDefinition().setVarInitialValue(
				ActorActionsEf.VAR_ACTIONS, actions);

		EAdEffect showActions = new ActorActionsEf(actionsObject
				.getDefinition());
		actionsObject.addBehavior(MouseGEv.MOUSE_RIGHT_CLICK, showActions);
		getSceneElements().add(actionsObject);
	}

}

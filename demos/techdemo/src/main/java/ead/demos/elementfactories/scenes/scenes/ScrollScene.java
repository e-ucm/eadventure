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

import ead.common.model.elements.effects.sceneelements.MoveSceneElementEf;
import ead.common.model.elements.events.SceneElementEv;
import ead.common.model.elements.events.enums.SceneElementEvType;
import ead.common.model.elements.guievents.MouseGEv;
import ead.common.model.elements.scene.EAdSceneElementDef;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.trajectories.SimpleTrajectoryDefinition;
import ead.common.model.elements.variables.SystemFields;
import ead.common.model.predef.effects.MakeActiveElementEf;
import ead.common.model.predef.effects.MoveActiveElementToMouseEf;
import ead.common.model.predef.effects.SpeakSceneElementEf;
import ead.common.model.predef.events.ScrollWithSceneElementEv;
import ead.common.resources.assets.drawable.basics.Image;
import ead.common.util.EAdPosition.Corner;
import ead.demos.elementfactories.EAdElementsFactory;
import ead.demos.elementfactories.scenes.normalguy.NgCommon;

public class ScrollScene extends EmptyScene {

	public ScrollScene() {
		setId("ScrollScene");
		setBounds(1000, 1213);
		setBackground(new SceneElement(new Image(
				"@drawable/scrollbg.png")));
		
		NgCommon.init();
		SceneElement character = new SceneElement(NgCommon.getMainCharacter());
		character.setPosition(Corner.BOTTOM_CENTER, 1000 / 2, 1213 / 2);
		
		SpeakSceneElementEf effect = new SpeakSceneElementEf( character );
		EAdElementsFactory.getInstance().getStringFactory().setString(effect.getCaption().getText(), "Sometimes I don't speak right");
		character.addBehavior(MouseGEv.MOUSE_RIGHT_CLICK, effect);
		
		
		this.getSceneElements().add(character);

		MakeActiveElementEf makeActive = new MakeActiveElementEf(
				character);

		SceneElementEv event = new SceneElementEv();
		event.setId("makeAcitveCharacter");
		event.addEffect(SceneElementEvType.FIRST_UPDATE, makeActive);
		character.getEvents().add(event);
		
		this.getEvents().add(new ScrollWithSceneElementEv(this, character));

		SimpleTrajectoryDefinition trajectory = new SimpleTrajectoryDefinition(false);
		trajectory.setLimits(0, 0, 1000, 1213);
		setTrajectoryDefinition(trajectory);

		// Sets up character's movement
		MoveSceneElementEf move = new MoveSceneElementEf();
		move.setId("moveCharacter");
		move.setTargetCoordiantes(SystemFields.MOUSE_SCENE_X,
				SystemFields.MOUSE_SCENE_Y);
		move.setSceneElement(character);
		move.setUseTrajectory(true);
		getBackground().addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, move);
	}

	@Override
	public String getSceneDescription() {
		return "A scene scrolling with the character";
	}

	public String getDemoName() {
		return "Scroll Scene";
	}

}

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

import es.eucm.ead.model.assets.drawable.basics.Image;
import es.eucm.ead.model.elements.effects.sceneelements.MoveSceneElementEf;
import es.eucm.ead.model.elements.events.SceneElementEv;
import es.eucm.ead.model.elements.events.enums.SceneElementEvType;
import es.eucm.ead.model.elements.operations.SystemFields;
import es.eucm.ead.model.elements.predef.effects.MakeActiveElementEf;
import es.eucm.ead.model.elements.predef.effects.SpeakSceneElementEf;
import es.eucm.ead.model.elements.predef.events.ScrollWithSceneElementEv;
import es.eucm.ead.model.elements.scenes.SceneElement;
import es.eucm.ead.model.elements.trajectories.SimpleTrajectory;
import es.eucm.ead.model.params.guievents.MouseGEv;
import es.eucm.ead.model.params.text.EAdString;
import es.eucm.ead.model.params.util.Position.Corner;
import ead.demos.elementfactories.EAdElementsFactory;
import ead.demos.elementfactories.scenes.normalguy.NgCommon;

public class ScrollScene extends EmptyScene {

	public ScrollScene() {
		this.setId("ScrollScene");
		setBounds(1000, 1213);
		setBackground(new SceneElement(new Image("@drawable/scrollbg.png")));

		NgCommon.init();
		SceneElement character = new SceneElement(NgCommon.getMainCharacter());
		character.setPosition(Corner.BOTTOM_CENTER, 1000 / 2, 1213 / 2);

		SpeakSceneElementEf effect = new SpeakSceneElementEf(character,
				new EAdString("n.1234"));
		EAdElementsFactory.getInstance().getStringFactory().setString(
				effect.getCaption().getText(), "Sometimes I don't speak right");
		character.addBehavior(MouseGEv.MOUSE_RIGHT_PRESSED, effect);

		this.getSceneElements().add(character);

		MakeActiveElementEf makeActive = new MakeActiveElementEf(character);

		SceneElementEv event = new SceneElementEv();
		event.addEffect(SceneElementEvType.INIT, makeActive);
		character.getEvents().add(event);

		this.getEvents().add(new ScrollWithSceneElementEv(this, character));

		SimpleTrajectory trajectory = new SimpleTrajectory(false);
		trajectory.setLimits(0, 0, 1000, 1213);
		setTrajectoryDefinition(trajectory);

		// Sets up character's movement
		MoveSceneElementEf move = new MoveSceneElementEf();
		move.setTargetCoordiantes(SystemFields.MOUSE_SCENE_X,
				SystemFields.MOUSE_SCENE_Y);
		move.setSceneElement(character);
		move.setUseTrajectory(true);
		getBackground().addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, move);
	}

}

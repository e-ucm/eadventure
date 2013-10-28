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

package es.eucm.ead.techdemo.elementfactories.scenes.scenes;

import es.eucm.ead.model.assets.drawable.basics.Image;
import es.eucm.ead.model.elements.events.SceneElementEv;
import es.eucm.ead.model.elements.events.enums.SceneElementEvType;
import es.eucm.ead.model.elements.predef.effects.MakeActiveElementEf;
import es.eucm.ead.model.elements.predef.effects.MoveActiveElementToMouseEf;
import es.eucm.ead.model.elements.scenes.SceneElement;
import es.eucm.ead.model.elements.trajectories.PolygonTrajectory;
import es.eucm.ead.model.params.guievents.MouseGEv;
import es.eucm.ead.model.params.util.Position;
import es.eucm.ead.model.params.util.Position.Corner;
import es.eucm.ead.techdemo.elementfactories.scenes.normalguy.NgCommon;

public class PolygonTrajectoryScene extends EmptyScene {

	public PolygonTrajectoryScene() {
		this.setId("PolygonTrajectoryScene");
		NgCommon.init();

		getBackground().getDefinition().setAppearance(
				new Image("@drawable/polygon.png"));
		SceneElement element = new SceneElement(NgCommon.getMainCharacter());

		element.setPosition(new Position(Corner.BOTTOM_CENTER, 520, 300));
		element.setScale(0.5f, 0.5f);

		MakeActiveElementEf effect = new MakeActiveElementEf(element);

		SceneElementEv event = new SceneElementEv();
		event.addEffect(SceneElementEvType.INIT, effect);
		element.addEvent(event);
		getSceneElements().add(element);
		addBehavior(MouseGEv.MOUSE_LEFT_PRESSED,
				new MoveActiveElementToMouseEf());

		// Create trajectory
		PolygonTrajectory polygon = new PolygonTrajectory();
		polygon.addPoint(242, 9);
		polygon.addPoint(726, 7);
		polygon.addPoint(729, 87);
		polygon.addPoint(750, 87);
		polygon.addPoint(752, 184);
		polygon.addPoint(680, 183);
		polygon.addPoint(680, 275);
		polygon.addPoint(621, 278);
		polygon.addPoint(621, 340);
		polygon.addPoint(590, 343);
		polygon.addPoint(589, 397);
		polygon.addPoint(700, 398);
		polygon.addPoint(700, 294);
		polygon.addPoint(778, 293);
		polygon.addPoint(771, 456);
		polygon.addPoint(588, 458);
		polygon.addPoint(589, 493);
		polygon.addPoint(770, 495);
		polygon.addPoint(770, 581);
		polygon.addPoint(579, 582);
		polygon.addPoint(575, 538);
		polygon.addPoint(534, 535);
		polygon.addPoint(533, 582);
		polygon.addPoint(111, 584);
		polygon.addPoint(109, 499);
		polygon.addPoint(191, 499);
		polygon.addPoint(190, 394);
		polygon.addPoint(26, 391);
		polygon.addPoint(26, 85);
		polygon.addPoint(640, 87);
		polygon.addPoint(640, 60);
		polygon.addPoint(238, 61);

		this.setTrajectoryDefinition(polygon);
	}

}

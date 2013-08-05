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

package ead.demos.elementfactories.scenes.normalguy;

import es.eucm.ead.model.assets.drawable.basics.Image;
import es.eucm.ead.model.elements.effects.ChangeSceneEf;
import es.eucm.ead.model.elements.effects.InterpolationEf;
import es.eucm.ead.model.elements.effects.enums.InterpolationLoopType;
import es.eucm.ead.model.elements.effects.enums.InterpolationType;
import es.eucm.ead.model.elements.events.SceneElementEv;
import es.eucm.ead.model.elements.events.enums.SceneElementEvType;
import es.eucm.ead.model.elements.scenes.EAdScene;
import es.eucm.ead.model.elements.scenes.SceneElement;
import es.eucm.ead.model.params.guievents.MouseGEv;
import es.eucm.ead.model.params.util.Position.Corner;
import ead.demos.elementfactories.scenes.scenes.EmptyScene;

public class NgCreditsScreen extends EmptyScene {

	public NgCreditsScreen(EAdScene initScene) {
		setBackground(new SceneElement(new Image(
				"@drawable/ng_mainscreen_bg.png")));
		SceneElement spiral = new SceneElement(new Image(
				"@drawable/ng_spiral.png"));
		spiral.setPosition(Corner.CENTER, 400, 300);
		getSceneElements().add(spiral);
		SceneElement logo = new SceneElement(new Image("@drawable/ng_logo.png"));
		logo.setPosition(Corner.CENTER, 400, 300);
		getSceneElements().add(logo);
		logo.setInitialScale(0.0f);

		// Animations
		SceneElementEv e = new SceneElementEv();
		InterpolationEf rotate = new InterpolationEf(spiral,
				SceneElement.VAR_ROTATION, 0, 2 * Math.PI, 20000, 0,
				InterpolationLoopType.RESTART, -1, InterpolationType.LINEAR);
		e.addEffect(SceneElementEvType.INIT, rotate);
		spiral.getEvents().add(e);

		e = new SceneElementEv();
		InterpolationEf bounce = new InterpolationEf(logo,
				SceneElement.VAR_SCALE, 0.0f, 1.0f, 1000, 1000,
				InterpolationLoopType.NO_LOOP, 1, InterpolationType.LINEAR);
		e.addEffect(SceneElementEvType.INIT, bounce);

		ChangeSceneEf changeScene = new ChangeSceneEf();
		changeScene.setNextScene(initScene);
		getBackground().addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, changeScene);

		logo.getEvents().add(e);

	}

	@Override
	public String getSceneDescription() {
		return "A game showing the eAdventure 2.0 features";
	}

	public String getDemoName() {
		return "Normal Guy";
	}

}

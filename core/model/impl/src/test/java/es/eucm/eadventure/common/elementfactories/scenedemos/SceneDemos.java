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

package es.eucm.eadventure.common.elementfactories.scenedemos;

import java.util.ArrayList;
import java.util.List;

import es.eucm.eadventure.common.elementfactories.scenedemos.normalguy.NgMainScreen;
import es.eucm.eadventure.common.elementfactories.scenedemos.normalguy.NgRoom1;

/**
 * A class holding all scenes that can be tested
 * 
 */

public class SceneDemos {

	private static SceneDemos instance;

	private List<SceneDemo> sceneDemos;

	private SceneDemos() {
		sceneDemos = new ArrayList<SceneDemo>();
		sceneDemos.add(new EmptyScene());
		sceneDemos.add(new ShapeScene());
//		sceneDemos.add(new TextsScene());
		sceneDemos.add(new CharacterScene());
		sceneDemos.add(new SpeakAndMoveScene());
		sceneDemos.add(new ComplexElementScene());
//		sceneDemos.add(new SoundScene());
		sceneDemos.add(new DrawablesScene());
//		sceneDemos.add(new MoleGame());
//		sceneDemos.add(new ShowQuestionScene());
//		sceneDemos.add(new TrajectoriesScene());
//		sceneDemos.add(new PhysicsScene());
		sceneDemos.add(new PhysicsScene2());
		sceneDemos.add(new DragDropScene());
		sceneDemos.add(new PositionScene());
		sceneDemos.add(new DepthZScene());
		sceneDemos.add(new SharingEffectsScene());
//		sceneDemos.add(new VideoScene());
		sceneDemos.add(new NgMainScreen());
//		sceneDemos.add(new NgRoom1());
		
	}

	public static SceneDemos getInstance() {
		if (instance == null)
			instance = new SceneDemos();
		return instance;
	}

	public List<String> getSceneDemosDescriptions() {
		List<String> strings = new ArrayList<String>();
		for (SceneDemo scene : sceneDemos) {
			strings.add(scene.getDemoName() + " - " + scene.getSceneDescription());
		}
		return strings;
	}

	public List<SceneDemo> getScenes() {
		return sceneDemos;
	}

}

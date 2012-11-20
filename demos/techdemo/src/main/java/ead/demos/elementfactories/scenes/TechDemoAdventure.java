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

package ead.demos.elementfactories.scenes;

import java.util.ArrayList;
import java.util.List;

import ead.common.model.elements.BasicAdventureModel;
import ead.common.model.elements.BasicChapter;
import ead.common.model.elements.scenes.EAdScene;
import ead.demos.elementfactories.scenes.normalguy.NgMainScreen;
import ead.demos.elementfactories.scenes.scenes.BVSScene;
import ead.demos.elementfactories.scenes.scenes.CharacterScene;
import ead.demos.elementfactories.scenes.scenes.ComplexElementScene;
import ead.demos.elementfactories.scenes.scenes.DepthZScene;
import ead.demos.elementfactories.scenes.scenes.DragDropScene;
import ead.demos.elementfactories.scenes.scenes.DrawablesScene;
import ead.demos.elementfactories.scenes.scenes.EmptyScene;
import ead.demos.elementfactories.scenes.scenes.FiltersDemo;
import ead.demos.elementfactories.scenes.scenes.InitScene;
import ead.demos.elementfactories.scenes.scenes.InventoryScene;
import ead.demos.elementfactories.scenes.scenes.PhysicsScene2;
import ead.demos.elementfactories.scenes.scenes.PositionScene;
import ead.demos.elementfactories.scenes.scenes.ScrollScene;
import ead.demos.elementfactories.scenes.scenes.ShapeScene;
import ead.demos.elementfactories.scenes.scenes.SharingEffectsScene;
import ead.demos.elementfactories.scenes.scenes.ShowQuestionScene;
import ead.demos.elementfactories.scenes.scenes.SpeakAndMoveScene;
import ead.demos.elementfactories.scenes.scenes.TrajectoriesScene;

public class TechDemoAdventure extends BasicAdventureModel {

	private List<SceneDemo> sceneDemos;

	private BasicChapter chapter;

	public TechDemoAdventure() {
		chapter = new BasicChapter();
		this.getChapters().add(chapter);

		sceneDemos = new ArrayList<SceneDemo>();
		sceneDemos = new ArrayList<SceneDemo>();
		sceneDemos.add(new InitScene());
		sceneDemos.add(new EmptyScene());
		sceneDemos.add(new ShapeScene());
		//		sceneDemos.add(new TextsScene());
		sceneDemos.add(new CharacterScene());
		sceneDemos.add(new SpeakAndMoveScene());
		sceneDemos.add(new ComplexElementScene());
		//		sceneDemos.add(new SoundScene());
		sceneDemos.add(new DrawablesScene());
		//		sceneDemos.add(new MoleGame());
		sceneDemos.add(new ShowQuestionScene());
		sceneDemos.add(new TrajectoriesScene());
		//		sceneDemos.add(new PhysicsScene());
		sceneDemos.add(new PhysicsScene2());
		sceneDemos.add(new DragDropScene());
		sceneDemos.add(new PositionScene());
		sceneDemos.add(new DepthZScene());
		sceneDemos.add(new SharingEffectsScene());
		sceneDemos.add(new InventoryScene());
		sceneDemos.add(new ScrollScene());
		sceneDemos.add(new FiltersDemo());
		//		sceneDemos.add(new VideoScene());
		sceneDemos.add(new NgMainScreen(null));
		//		sceneDemos.add(new NgRoom1());
		sceneDemos.add(new BVSScene());
	}

	public List<SceneDemo> getScenes() {
		return sceneDemos;
	}

	public List<String> getSceneDemosDescriptions() {
		ArrayList<String> strings = new ArrayList<String>();
		for (SceneDemo s : sceneDemos) {
			strings.add(s.toString());
		}
		return strings;
	}

	public void setInitialScene(EAdScene scene) {
		chapter.setInitialScene(scene);
	}

	private static TechDemoAdventure instance;

	public static TechDemoAdventure getInstance() {
		if (instance == null) {
			instance = new TechDemoAdventure();
		}
		return instance;
	}

}

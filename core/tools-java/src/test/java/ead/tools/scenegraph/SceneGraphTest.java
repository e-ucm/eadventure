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

package ead.tools.scenegraph;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import ead.common.model.elements.effects.ChangeSceneEf;
import ead.common.model.elements.scenes.BasicScene;
import ead.common.params.guievents.MouseGEv;
import ead.tools.BasicSceneGraph;
import ead.tools.SceneGraph;
import ead.tools.java.reflection.JavaReflectionProvider;

public class SceneGraphTest {

	private SceneGraph sceneGraph;

	@Before
	public void setUp() {
		sceneGraph = new BasicSceneGraph(new JavaReflectionProvider());
	}

	@Test
	public void testBasic() {
		BasicScene initScene = new BasicScene();
		sceneGraph.generateGraph(initScene);
		assertTrue(sceneGraph.getScenes().size() == 1);
		assertTrue(sceneGraph.getEffectsVisited().size() == 0);
		assertTrue(sceneGraph.getGraph().get(initScene).size() == 0);

		BasicScene scene2 = new BasicScene();
		ChangeSceneEf changeScene2 = new ChangeSceneEf(scene2);
		initScene.getBackground().addBehavior(MouseGEv.MOUSE_LEFT_PRESSED,
				changeScene2);
		sceneGraph.generateGraph(initScene);
		assertTrue(sceneGraph.getScenes().size() == 2);
		assertTrue(sceneGraph.getEffectsVisited().size() == 1);
		assertTrue(sceneGraph.getGraph().get(initScene).size() == 1);
		assertTrue(sceneGraph.getGraph().get(scene2).size() == 0);

		ChangeSceneEf changeInitScene = new ChangeSceneEf(initScene);
		scene2.getBackground().addBehavior(MouseGEv.MOUSE_LEFT_PRESSED,
				changeInitScene);
		sceneGraph.generateGraph(initScene);
		assertTrue(sceneGraph.getScenes().size() == 2);
		assertTrue(sceneGraph.getEffectsVisited().size() == 2);
		assertTrue(sceneGraph.getGraph().get(initScene).size() == 1);
		assertTrue(sceneGraph.getGraph().get(scene2).size() == 1);

		ChangeSceneEf previousScene = new ChangeSceneEf();
		BasicScene scene3 = new BasicScene();
		ChangeSceneEf changeScene3 = new ChangeSceneEf(scene3);
		scene2.getBackground().addBehavior(MouseGEv.MOUSE_EXITED, changeScene3);
		initScene.getBackground().addBehavior(MouseGEv.MOUSE_EXITED,
				changeScene3);
		scene3.getBackground().addBehavior(MouseGEv.MOUSE_ENTERED,
				previousScene);
		sceneGraph.generateGraph(initScene);
		assertTrue(sceneGraph.getScenes().size() == 3);
		assertTrue(sceneGraph.getEffectsVisited().size() == 4);
		assertTrue(sceneGraph.getGraph().get(initScene).size() == 2);
		assertTrue(sceneGraph.getGraph().get(initScene).contains(scene2));
		assertTrue(sceneGraph.getGraph().get(initScene).contains(scene3));
		assertTrue(sceneGraph.getGraph().get(scene2).size() == 2);
		assertTrue(sceneGraph.getGraph().get(scene2).contains(initScene));
		assertTrue(sceneGraph.getGraph().get(scene2).contains(scene3));
		assertTrue(sceneGraph.getGraph().get(scene3).size() == 2);
		assertTrue(sceneGraph.getGraph().get(scene3).contains(scene2));
		assertTrue(sceneGraph.getGraph().get(scene3).contains(initScene));
	}

	@Test
	public void testEvents() {

	}

}

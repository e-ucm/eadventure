package ead.tools.scenegraph;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import ead.common.model.elements.effects.ChangeSceneEf;
import ead.common.model.elements.guievents.MouseGEv;
import ead.common.model.elements.scenes.BasicScene;
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
		initScene.setId("initScene");
		sceneGraph.generateGraph(initScene);
		assertTrue(sceneGraph.getScenes().size() == 1 );
		assertTrue(sceneGraph.getEffectsVisited().size() == 0 );
		assertTrue(sceneGraph.getGraph().get(initScene).size() == 0);
		
		BasicScene scene2 = new BasicScene( );
		scene2.setId("scene2");
		ChangeSceneEf changeScene2 = new ChangeSceneEf(scene2);
		initScene.getBackground().addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, changeScene2);
		sceneGraph.generateGraph(initScene);
		assertTrue(sceneGraph.getScenes().size() == 2 );
		assertTrue(sceneGraph.getEffectsVisited().size() == 1 );
		assertTrue(sceneGraph.getGraph().get(initScene).size() == 1);
		assertTrue(sceneGraph.getGraph().get(scene2).size() == 0);
		
		ChangeSceneEf changeInitScene = new ChangeSceneEf( initScene );
		scene2.getBackground().addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, changeInitScene);
		sceneGraph.generateGraph(initScene);
		assertTrue(sceneGraph.getScenes().size() == 2 );
		assertTrue(sceneGraph.getEffectsVisited().size() == 2 );
		assertTrue(sceneGraph.getGraph().get(initScene).size() == 1);
		assertTrue(sceneGraph.getGraph().get(scene2).size() == 1);
		
		ChangeSceneEf previousScene = new ChangeSceneEf( );
		BasicScene scene3 = new BasicScene( );
		scene3.setId("scene3");
		ChangeSceneEf changeScene3 = new ChangeSceneEf( scene3 );
		scene2.getBackground().addBehavior(MouseGEv.MOUSE_EXITED, changeScene3);
		initScene.getBackground().addBehavior(MouseGEv.MOUSE_EXITED, changeScene3);
		scene3.getBackground().addBehavior(MouseGEv.MOUSE_ENTERED, previousScene);
		sceneGraph.generateGraph(initScene);
		assertTrue(sceneGraph.getScenes().size() == 3 );
		assertTrue(sceneGraph.getEffectsVisited().size() == 4 );
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
	public void testEvents( ){
		BasicScene scene1 = new BasicScene();
		scene1.setId("scene1");
		
		
	}

}

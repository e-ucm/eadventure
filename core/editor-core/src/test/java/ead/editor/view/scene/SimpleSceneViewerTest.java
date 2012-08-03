package ead.editor.view.scene;

import javax.swing.JFrame;

import ead.common.model.elements.scene.EAdScene;
import ead.common.model.elements.scenes.BasicScene;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.params.fills.ColorFill;
import ead.common.resources.assets.drawable.basics.shapes.RectangleShape;
import ead.common.util.EAdPosition.Corner;
import ead.elementfactories.demos.scenes.InitScene;

public class SimpleSceneViewerTest {
	
	public static void main( String args[] ){
		
		JFrame frame = new JFrame("SceneViewer");
		frame.setSize(800, 600);
		
		SimpleSceneViewer viewer = new SimpleSceneViewer( );
//		viewer.setScene(getSimpleScene());
		viewer.setScene(new InitScene());
		
		viewer.getCanvas().setSize(800, 600);
		frame.getContentPane().add(viewer.getCanvas());
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static EAdScene getSimpleScene( ){
		BasicScene scene = new BasicScene( new RectangleShape( 800, 600, ColorFill.RED));
		
		SceneElement button = new SceneElement( new RectangleShape( 20, 20, ColorFill.BLACK));
		button.setPosition(Corner.CENTER, 400, 300);
		
		scene.getSceneElements().add(button);
		
		return scene;
	}

}

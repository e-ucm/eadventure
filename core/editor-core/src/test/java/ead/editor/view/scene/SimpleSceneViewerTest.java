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

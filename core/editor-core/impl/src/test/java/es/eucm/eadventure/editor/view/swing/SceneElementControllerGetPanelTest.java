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

package es.eucm.eadventure.editor.view.swing;

import java.awt.FlowLayout;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import static org.mockito.Mockito.*;

import es.eucm.eadventure.common.elementfactories.demos.scenes.EmptyScene;
import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneElementDefImpl;
import es.eucm.eadventure.common.model.extra.impl.EAdListImpl;
import es.eucm.eadventure.common.params.EAdString;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.editor.control.CommandManager;
import es.eucm.eadventure.editor.control.ElementController;
import es.eucm.eadventure.editor.control.elements.impl.EAdSceneController;
import es.eucm.eadventure.editor.impl.EditorStringHandler;
import es.eucm.eadventure.editor.view.ComponentProvider;
import es.eucm.eadventure.editor.view.generics.Panel;
import es.eucm.eadventure.gui.EAdFrame;
import es.eucm.eadventure.gui.EAdGUILookAndFeel;

public class SceneElementControllerGetPanelTest extends EAdFrame {

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(EAdGUILookAndFeel.getInstance());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		new SceneElementControllerGetPanelTest();
	}
	
    public SceneElementControllerGetPanelTest() {
        setSize( 600,400 );
        
        setLayout(new FlowLayout());
        
        EAdScene scene = new EmptyScene();
        
        CommandManager commandManager = mock(CommandManager.class);
        
        EAdSceneController sceneController = new EAdSceneController();
        sceneController.setElement(scene);
        Panel panel = sceneController.getPanel(ElementController.View.EXPERT);
        
        StringHandler stringHandler = new EditorStringHandler();

        SwingProviderFactory swingProviderFactory = new SwingProviderFactory(stringHandler, commandManager);
        ComponentProvider<Panel, JComponent> componentProvider = swingProviderFactory.getProvider(panel);
        add(componentProvider.getComponent(panel));
        
        setVisible( true );
        setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
        //pack();
    }
	
}

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

package ead.editor.view.swing;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import static org.mockito.Mockito.*;

import ead.common.model.elements.scene.EAdScene;
import ead.editor.control.CommandManager;
import ead.editor.control.FieldValueReader;
import ead.editor.view.generics.FieldDescriptor;
import ead.editor.view.generics.impl.FieldDescriptorImpl;
import ead.editor.view.generics.scene.PreviewPanel;
import ead.editor.view.generics.scene.impl.PreviewPanelImpl;
import ead.editor.view.swing.scene.PreviewPanelComponentProvider;
import ead.elementfactories.demos.scenes.EmptyScene;
import ead.gui.EAdFrame;
import ead.gui.EAdGUILookAndFeel;

public class PreviewPanelComponentProviderTest extends EAdFrame {

	private static final long serialVersionUID = 1L;
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(EAdGUILookAndFeel.getInstance());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		new PreviewPanelComponentProviderTest();
	}
	
    public PreviewPanelComponentProviderTest() {
        setSize( 400, 300 );

        FieldDescriptor<String> fieldDescriptor = new FieldDescriptorImpl<String>(null, "name");
        FieldValueReader fieldValueReader = mock(FieldValueReader.class);
        when(fieldValueReader.readValue(fieldDescriptor)).thenReturn("value");

        CommandManager commandManager = mock(CommandManager.class);
        
        EAdScene scene = new EmptyScene();
        
        PreviewPanel previewPanel = new PreviewPanelImpl(scene);
        previewPanel.setScene(scene);
        PreviewPanelComponentProvider previewPanelProvider = new PreviewPanelComponentProvider(commandManager);
        JComponent component = previewPanelProvider.getComponent(previewPanel);
        add(component);  
        
        setVisible( true );
        setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
    }
	
}

package es.eucm.eadventure.editor.view.swing;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import static org.mockito.Mockito.*;

import es.eucm.eadventure.common.model.elements.EAdSceneElementDef;
import es.eucm.eadventure.common.params.EAdString;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.editor.control.CommandManager;
import es.eucm.eadventure.editor.control.ElementController;
import es.eucm.eadventure.editor.control.elements.impl.EAdSceneElementDefController;
import es.eucm.eadventure.editor.impl.EditorStringHandler;
import es.eucm.eadventure.editor.view.ComponentProvider;
import es.eucm.eadventure.editor.view.generics.Panel;
import es.eucm.eadventure.gui.EAdFrame;
import es.eucm.eadventure.gui.EAdGUILookAndFeel;

public class ElementControllerGetPanelTest2 extends EAdFrame {

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(EAdGUILookAndFeel.getInstance());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		new ElementControllerGetPanelTest2();
	}
	
    public ElementControllerGetPanelTest2() {
        this.setSize( 600,400 );
        
        //this.setLayout(new BorderLayout());
        
        EAdSceneElementDef sceneElementDef = mock(EAdSceneElementDef.class);
        when(sceneElementDef.getName()).thenReturn(EAdString.newEAdString("testName"));
        when(sceneElementDef.getDoc()).thenReturn(EAdString.newEAdString("testDocumentation"));
        when(sceneElementDef.getDesc()).thenReturn(EAdString.newEAdString("testDescription"));
        when(sceneElementDef.getDetailDesc()).thenReturn(EAdString.newEAdString("testDetailedDescription"));
        
        CommandManager commandManager = mock(CommandManager.class);
        
        EAdSceneElementDefController sceneController = new EAdSceneElementDefController();
        sceneController.setElement(sceneElementDef);
        Panel panel = sceneController.getPanel(ElementController.View.EXPERT);
        
        StringHandler stringHandler = new EditorStringHandler();

        SwingProviderFactory swingProviderFactory = new SwingProviderFactory(stringHandler, commandManager);
        ComponentProvider<Panel, JComponent> componentProvider = swingProviderFactory.getProvider(panel);
        this.add(componentProvider.getComponent(panel), BorderLayout.CENTER);
        
        this.setVisible( true );
        this.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
    }
	
}

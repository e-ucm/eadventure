package es.eucm.eadventure.editor.view.swing;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import static org.mockito.Mockito.*;

import es.eucm.eadventure.common.elementfactories.scenedemos.EmptyScene;
import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.editor.control.CommandManager;
import es.eucm.eadventure.editor.control.FieldValueReader;
import es.eucm.eadventure.editor.view.generics.FieldDescriptor;
import es.eucm.eadventure.editor.view.generics.impl.FieldDescriptorImpl;
import es.eucm.eadventure.editor.view.generics.impl.TextOption;
import es.eucm.eadventure.editor.view.generics.scene.PreviewPanel;
import es.eucm.eadventure.editor.view.generics.scene.impl.PreviewPanelImpl;
import es.eucm.eadventure.editor.view.swing.componentproviders.TextComponentProvider;
import es.eucm.eadventure.editor.view.swing.scene.PreviewPanelComponentProvider;
import es.eucm.eadventure.gui.EAdFrame;
import es.eucm.eadventure.gui.EAdGUILookAndFeel;

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
        setSize( 800, 600 );

        FieldDescriptor<String> fieldDescriptor = new FieldDescriptorImpl<String>(null, "name");
        FieldValueReader fieldValueReader = mock(FieldValueReader.class);
        when(fieldValueReader.readValue(fieldDescriptor)).thenReturn("value");

        CommandManager commandManager = mock(CommandManager.class);
        
        setLayout(new BorderLayout());
        
        EAdScene scene = new EmptyScene();
        
        PreviewPanel previewPanel = new PreviewPanelImpl();
        previewPanel.setScene(scene);
        PreviewPanelComponentProvider previewPanelProvider = new PreviewPanelComponentProvider(commandManager);
        JComponent component = previewPanelProvider.getComponent(previewPanel);
        add(component, BorderLayout.CENTER);
                
        setVisible( true );
        setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
    }
	
}

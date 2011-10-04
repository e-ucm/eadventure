package es.eucm.eadventure.editor.view.swing;

import java.awt.FlowLayout;

import javax.swing.JComponent;
import javax.swing.WindowConstants;

import static org.mockito.Mockito.*;

import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.editor.control.ElementController;
import es.eucm.eadventure.editor.control.FieldValueReader;
import es.eucm.eadventure.editor.control.elements.impl.EAdSceneController;
import es.eucm.eadventure.editor.view.ComponentProvider;
import es.eucm.eadventure.editor.view.generics.FieldDescriptor;
import es.eucm.eadventure.editor.view.generics.Panel;
import es.eucm.eadventure.editor.view.generics.impl.FieldDescriptorImpl;
import es.eucm.eadventure.gui.EAdFrame;

public class ElementControllerGetPanelTest extends EAdFrame {

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		new ElementControllerGetPanelTest();
	}
	
    public ElementControllerGetPanelTest() {
        setSize( 400,400 );
        
        setLayout(new FlowLayout());
        
        FieldDescriptor<String> fieldDescriptor = new FieldDescriptorImpl<String>(null, "name");
        FieldValueReader fieldValueReader = mock(FieldValueReader.class);
        when(fieldValueReader.readValue(fieldDescriptor)).thenReturn("value");
        
        EAdScene scene = mock(EAdScene.class);
        
        EAdSceneController sceneController = new EAdSceneController();
        sceneController.setElement(scene);
        Panel panel = sceneController.getPanel(ElementController.View.EXPERT);

        SwingProviderFactory swingProviderFactory = new SwingProviderFactory(fieldValueReader);
        ComponentProvider<Panel, JComponent> componentProvider = swingProviderFactory.getProvider(panel);
        add(componentProvider.getComponent(panel));
        
        setVisible( true );
        setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
        pack();
    }
	
}

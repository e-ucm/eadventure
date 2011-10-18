package es.eucm.eadventure.editor.view.swing;

import java.awt.FlowLayout;

import javax.swing.JComponent;
import javax.swing.WindowConstants;

import static org.mockito.Mockito.*;

import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.editor.control.CommandManager;
import es.eucm.eadventure.editor.control.FieldValueReader;
import es.eucm.eadventure.editor.impl.EditorStringHandler;
import es.eucm.eadventure.editor.view.ComponentProvider;
import es.eucm.eadventure.editor.view.generics.FieldDescriptor;
import es.eucm.eadventure.editor.view.generics.Panel;
import es.eucm.eadventure.editor.view.generics.impl.FieldDescriptorImpl;
import es.eucm.eadventure.editor.view.generics.impl.PanelImpl;
import es.eucm.eadventure.editor.view.generics.impl.TextOption;
import es.eucm.eadventure.gui.EAdFrame;



public class FactoryProviderTest extends EAdFrame {

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		new FactoryProviderTest();
	}
	
    public FactoryProviderTest() {
        setSize( 400,400 );
        
        setLayout(new FlowLayout());
        
        FieldDescriptor<String> fieldDescriptor = new FieldDescriptorImpl<String>(null, "name");
        FieldValueReader fieldValueReader = mock(FieldValueReader.class);
        when(fieldValueReader.readValue(fieldDescriptor)).thenReturn("value");
        
        CommandManager commandManager = mock(CommandManager.class);
        
        TextOption option = new TextOption("name", "toolTip", fieldDescriptor);
        TextOption option2 = new TextOption("name2", "toolTip", fieldDescriptor, TextOption.ExpectedLength.LONG);
        Panel panel = new PanelImpl("title", Panel.LayoutPolicy.VERTICAL);
        panel.getElements().add(option);
        panel.getElements().add(option2);

        StringHandler stringHandler = new EditorStringHandler();

        SwingProviderFactory swingProviderFactory = new SwingProviderFactory(stringHandler, commandManager);
        ComponentProvider<Panel, JComponent> componentProvider = swingProviderFactory.getProvider(panel);
        add(componentProvider.getComponent(panel));
        
        setVisible( true );
        setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
        pack();
    }
	
}

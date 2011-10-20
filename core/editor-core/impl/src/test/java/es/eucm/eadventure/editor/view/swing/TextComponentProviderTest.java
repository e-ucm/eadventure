package es.eucm.eadventure.editor.view.swing;

import java.awt.FlowLayout;

import javax.swing.WindowConstants;

import static org.mockito.Mockito.*;

import es.eucm.eadventure.editor.control.FieldValueReader;
import es.eucm.eadventure.editor.view.generics.FieldDescriptor;
import es.eucm.eadventure.editor.view.generics.impl.FieldDescriptorImpl;
import es.eucm.eadventure.editor.view.generics.impl.TextOption;
import es.eucm.eadventure.editor.view.swing.componentproviders.TextComponentProvider;
import es.eucm.eadventure.gui.EAdFrame;

public class TextComponentProviderTest extends EAdFrame {

	private static final long serialVersionUID = 1L;
	
	public static void main(String[] args) {
		new TextComponentProviderTest();
	}
	
    public TextComponentProviderTest() {
        setSize( 400,400 );

        FieldDescriptor<String> fieldDescriptor = new FieldDescriptorImpl<String>(null, "name");
        FieldValueReader fieldValueReader = mock(FieldValueReader.class);
        when(fieldValueReader.readValue(fieldDescriptor)).thenReturn("value");

        setLayout(new FlowLayout());
        TextOption option = new TextOption("name", "toolTip", fieldDescriptor);
        TextComponentProvider textComponentProvider = new TextComponentProvider(fieldValueReader);
        add(textComponentProvider.getComponent(option));
        
        setVisible( true );
        setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
    }
	
}

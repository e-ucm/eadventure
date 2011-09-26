package es.eucm.eadventure.editor.view.swing;

import java.awt.FlowLayout;

import javax.swing.WindowConstants;

import static org.mockito.Mockito.*;

import es.eucm.eadventure.editor.view.generics.FieldDescriptor;
import es.eucm.eadventure.editor.view.generics.impl.TextOption;
import es.eucm.eadventure.gui.EAdFrame;

public class TextComponentProviderTest extends EAdFrame {

	private static final long serialVersionUID = 1L;
	
	public static void main(String[] args) {
		new TextComponentProviderTest();
	}
	
    public TextComponentProviderTest() {
        setSize( 400,400 );

        @SuppressWarnings("unchecked")
        FieldDescriptor<String> fieldDescriptor = (FieldDescriptor<String>) mock(FieldDescriptor.class);
        when(fieldDescriptor.readValue()).thenReturn("value");

        setLayout(new FlowLayout());
        TextOption option = new TextOption("name", "toolTip", fieldDescriptor);
        TextComponentProvider textComponentProvider = new TextComponentProvider();
        textComponentProvider.setElement(option);
        add(textComponentProvider.getComponent());
        
        setVisible( true );
        setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
    }
	
}

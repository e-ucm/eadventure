package es.eucm.eadventure.editor.view.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import es.eucm.eadventure.editor.view.ComponentProvider;
import es.eucm.eadventure.editor.view.generics.impl.ElementOption;

public class ElementComponentProvider implements ComponentProvider<ElementOption<?>, JButton> {

	private ElementOption<?> element;
	
	private JButton button;
	
	public ElementComponentProvider() {
		
	}
	
	@Override
	public void setElement(ElementOption<?> element2) {
		this.element = element2;
		button = new JButton(element.getTitle());
		button.setToolTipText(element.getToolTipText());
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				//TODO change element in window
			}
			
		});
	}

	@Override
	public JButton getComponent() {
		return button;
	}

}

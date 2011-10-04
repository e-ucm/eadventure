package es.eucm.eadventure.editor.view.swing;

import es.eucm.eadventure.editor.view.ComponentProvider;
import es.eucm.eadventure.editor.view.generics.impl.EAdStringOption;
import es.eucm.eadventure.gui.EAdTextField;

public class EAdStringComponentProvider implements ComponentProvider<EAdStringOption, EAdTextField> {

	private EAdStringOption element;
	
	private EAdTextField textField;
	
	public EAdStringComponentProvider() {
		
	}
	
	@Override
	public EAdTextField getComponent(EAdStringOption element2) {
		this.element = element2;
		textField = new EAdTextField(element.getTitle(), 20);
		textField.setToolTipText(element.getToolTipText());
		//TODO String handler in the editor?
		//textField.setText(element.getFieldDescriptor().readValue());

		return textField;
	}

}

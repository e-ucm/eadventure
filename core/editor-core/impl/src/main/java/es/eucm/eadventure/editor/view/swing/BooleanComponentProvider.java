package es.eucm.eadventure.editor.view.swing;

import javax.swing.JCheckBox;

import es.eucm.eadventure.editor.view.ComponentProvider;
import es.eucm.eadventure.editor.view.generics.impl.BooleanOption;

public class BooleanComponentProvider implements ComponentProvider<BooleanOption, JCheckBox> {

	private BooleanOption element;
	
	private JCheckBox checkBox;
	
	public BooleanComponentProvider() {
		
	}
	
	@Override
	public void setElement(BooleanOption element2) {
		this.element = element2;
		checkBox = new JCheckBox(element.getTitle());
		checkBox.setToolTipText(element.getToolTipText());
		checkBox.setSelected(element.getFieldDescriptor().readValue());
	}

	@Override
	public JCheckBox getComponent() {
		return checkBox;
	}

}

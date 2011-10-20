package es.eucm.eadventure.editor.view.swing.componentproviders;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.editor.control.FieldValueReader;
import es.eucm.eadventure.editor.view.ComponentProvider;
import es.eucm.eadventure.editor.view.generics.impl.EAdConditionOption;
import es.eucm.eadventure.gui.EAdBorderedPanel;
import es.eucm.eadventure.gui.EAdButton;
import es.eucm.eadventure.gui.EAdTextField;

public class EAdConditionComponentProvider implements ComponentProvider<EAdConditionOption, JComponent> {

	private FieldValueReader fieldValueReader;
	
	public EAdConditionComponentProvider(FieldValueReader fieldValueReader) {
		this.fieldValueReader = fieldValueReader;
	}

	@Override
	public JComponent getComponent(EAdConditionOption element) {
		if (element.getView() == EAdConditionOption.View.DETAILED) {
			JPanel panel = new EAdBorderedPanel(element.getTitle());
			panel.setLayout(new BorderLayout());
	
			EAdTextField textField = new EAdTextField();
			panel.add(textField, BorderLayout.CENTER);
			EAdCondition condition = fieldValueReader.readValue(element.getFieldDescriptor());
			textField.setText(condition.toString());
			textField.setEnabled(false);
			//TODO should update field after condition edition
			
			EAdButton button = new EAdButton("Edit");
			button.setToolTipText(element.getToolTipText());
			panel.add(button, BorderLayout.EAST);
			//TODO should allow for the edition of conditions
			
			return panel;
		} else {
			EAdButton button = new EAdButton(element.getTitle());
			//TODO should allow for the edition of conditions
			//TODO should show the icons
			

			return button;
		}
	}

}

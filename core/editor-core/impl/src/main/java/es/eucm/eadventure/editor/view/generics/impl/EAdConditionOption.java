package es.eucm.eadventure.editor.view.generics.impl;

import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.editor.view.generics.FieldDescriptor;

public class EAdConditionOption extends AbstractOption<EAdCondition> {

	public static enum View { DETAILED, BASIC }
	
	private View view;
	
	public EAdConditionOption(String title, String toolTipText, FieldDescriptor<EAdCondition> fieldDescriptor, View view) {
		super(title, toolTipText, fieldDescriptor);
		this.view = view;
	}
	
	public View getView() {
		return view;
	}

}

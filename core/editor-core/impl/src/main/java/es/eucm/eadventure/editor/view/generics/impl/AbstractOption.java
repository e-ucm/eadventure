package es.eucm.eadventure.editor.view.generics.impl;

import es.eucm.eadventure.editor.view.generics.FieldDescriptor;
import es.eucm.eadventure.editor.view.generics.Option;

public class AbstractOption implements Option {

	/**
	 * Label on the component
	 */
	private String label;
	
	/**
	 * Tool tip text explanation
	 */
	private String toolTipText;
	
	/**
	 * Descriptor of the field represented by this option
	 */
	private FieldDescriptor fieldDescriptor;
	
	public AbstractOption(String label, String toolTipText, FieldDescriptor fieldDescriptor) {
		this.label = label;
		this.toolTipText = toolTipText;
		this.fieldDescriptor = fieldDescriptor;
	}
	
	@Override
	public String getTitle() {
		return label;
	}

	@Override
	public String getToolTipText() {
		return toolTipText;
	}
	
	@Override
	public FieldDescriptor getFieldDescriptor() {
		return fieldDescriptor;
	}

}

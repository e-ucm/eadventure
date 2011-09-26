package es.eucm.eadventure.editor.view.generics.impl;

import es.eucm.eadventure.editor.view.generics.FieldDescriptor;
import es.eucm.eadventure.editor.view.generics.Option;

public class AbstractOption<S> implements Option<S> {

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
	private FieldDescriptor<S> fieldDescriptor;
	
	public AbstractOption(String label, String toolTipText, FieldDescriptor<S> fieldDescriptor) {
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
	public FieldDescriptor<S> getFieldDescriptor() {
		return fieldDescriptor;
	}

}

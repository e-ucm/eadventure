package es.eucm.eadventure.editor.view.generics.impl;

import es.eucm.eadventure.editor.view.generics.FieldDescriptor;
import es.eucm.eadventure.editor.view.generics.Option;

/**
 * Abstract implementation for {@link Option}s
 * 
 * @param <S>
 *            The type of the option element
 */
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

	/**
	 * @param label
	 *            The label in the option (can be null)
	 * @param toolTipText
	 *            The toolTipText in the option (cannot be null)
	 * @param fieldDescriptor
	 *            The {@link FieldDescriptor} that describes the field in the
	 *            element to be displayed/edited
	 */
	public AbstractOption(String label, String toolTipText,
			FieldDescriptor<S> fieldDescriptor) {
		this.label = label;
		this.toolTipText = toolTipText;
		if (toolTipText == null || toolTipText.equals(""))
			throw new RuntimeException(
					"BALTAEXCEPTION: ToolTipTexts must be provided for all interface elements!");
		this.fieldDescriptor = fieldDescriptor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.editor.view.generics.Option#getTitle()
	 */
	@Override
	public String getTitle() {
		return label;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.editor.view.generics.Option#getToolTipText()
	 */
	@Override
	public String getToolTipText() {
		return toolTipText;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.editor.view.generics.Option#getFieldDescriptor()
	 */
	@Override
	public FieldDescriptor<S> getFieldDescriptor() {
		return fieldDescriptor;
	}

}

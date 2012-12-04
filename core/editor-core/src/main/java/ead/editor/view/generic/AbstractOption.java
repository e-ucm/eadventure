/**
 * eAdventure (formerly <e-Adventure> and <e-Game>) is a research project of the
 *    <e-UCM> research group.
 *
 *    Copyright 2005-2010 <e-UCM> research group.
 *
 *    You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *
 *    <e-UCM> is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *
 *          C Profesor Jose Garcia Santesmases sn,
 *          28040 Madrid (Madrid), Spain.
 *
 *          For more info please visit:  <http://e-adventure.e-ucm.es> or
 *          <http://www.e-ucm.es>
 *
 * ****************************************************************************
 *
 *  This file is part of eAdventure, version 2.0
 *
 *      eAdventure is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      eAdventure is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with eAdventure.  If not, see <http://www.gnu.org/licenses/>.
 */

package ead.editor.view.generic;

import ead.editor.view.generic.FieldDescriptor;
import ead.editor.view.generic.Option;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

/**
 * Abstract implementation for {@link Option}s
 *
 * @param <S>
 *            The type of the option element
 */
public abstract class AbstractOption<S> implements Option<S> {

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
	protected FieldDescriptor<S> fieldDescriptor;

	/**
	 * While updating, external updates will be ignored
	 */
	protected boolean isUpdating = false;

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
		if (toolTipText == null || toolTipText.isEmpty())
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

	/**
	 * reads the value of the model object that is being configured
	 *
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <S> S read(FieldDescriptor<S> fieldDescriptor) {
		try {
			PropertyDescriptor pd = getPropertyDescriptor(fieldDescriptor
					.getElement().getClass(), fieldDescriptor.getFieldName());
			S value = (S) pd.getReadMethod().invoke(
					fieldDescriptor.getElement());
			return value;
		} catch (Exception e) {
			throw new RuntimeException("Error reading field '"
					+ fieldDescriptor.getFieldName() + "' in "
					+ fieldDescriptor.getElement().getClass(), e);
		}
	}

	/**
	 * Utility method to find a property descriptor for a single property
	 *
	 * @param c
	 * @param fieldName
	 * @return
	 */
	private static PropertyDescriptor getPropertyDescriptor(Class<?> c,
			String fieldName) {
		try {
			for (PropertyDescriptor pd : Introspector.getBeanInfo(c)
					.getPropertyDescriptors()) {
				if (pd.getName().equals(fieldName)) {
					return pd;
				}
			}
		} catch (IntrospectionException e) {
			throw new IllegalArgumentException(
					"Could not find getters or setters for field " + fieldName
							+ " in class " + c.getCanonicalName());
		}
		return null;
	}
}

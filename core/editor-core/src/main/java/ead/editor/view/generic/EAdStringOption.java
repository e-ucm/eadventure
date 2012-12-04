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

import ead.common.params.text.EAdString;
import ead.editor.control.CommandManager;
import ead.editor.control.commands.ChangeEAdStringValueCommand;
import ead.tools.StringHandler;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import ead.editor.control.change.ChangeEvent;
import ead.editor.control.change.ChangeListener;

/**
 * This provider uses a {@link StringHandler} to establish the value of a string
 * and where the new value is stored. The {@link EAdString} is only a key and
 * must not be modified.
 *
 */
public class EAdStringOption extends AbstractOption<EAdString> implements
		ChangeListener<ChangeEvent> {

	private JTextComponent textField;

	@Override
	public void cleanup(CommandManager manager) {
		manager.removeChangeListener(this);
	}

	@Override
	public void processChange(ChangeEvent event) {
		if (event.hasChanged(fieldDescriptor)) {
			textField.setText(stringHandler
					.getString(read(getFieldDescriptor())));
		}
	}

	public static enum ExpectedLength {
		SHORT, NORMAL, LONG
	}

	private ExpectedLength expectedLength;

	/**
	 * The current {@link StringHandler} that maps {@link EAdString}s with their
	 * actual values
	 */
	private StringHandler stringHandler;

	public EAdStringOption(String title, String toolTipText,
			FieldDescriptor<EAdString> fieldDescriptor,
			ExpectedLength expectedLength) {
		super(title, toolTipText, fieldDescriptor);
		this.expectedLength = expectedLength;
	}

	public EAdStringOption(String title, String toolTipText,
			FieldDescriptor<EAdString> fieldDescriptor) {
		this(title, toolTipText, fieldDescriptor, ExpectedLength.NORMAL);
	}

	public ExpectedLength getExpectedLength() {
		return expectedLength;
	}

	public void setExpectedLength(ExpectedLength expectedLength) {
		this.expectedLength = expectedLength;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * es.eucm.eadventure.editor.view.ComponentProvider#getComponent(es.eucm
	 * .eadventure.editor.view.generics.InterfaceElement)
	 */
	@Override
	public JComponent getComponent(CommandManager manager) {
		JComponent component = null;
		switch (getExpectedLength()) {
		case LONG:
			textField = new JTextArea(getTitle());
			component = textField;
			break;
		case SHORT:
		case NORMAL:
			textField = new JTextField(getTitle());
			component = textField;
			break;
		default:
			throw new IllegalArgumentException("Bad expected length");
		}
		textField.setToolTipText(getToolTipText());

		textField.setText(stringHandler.getString(read(getFieldDescriptor())));

		textField.getDocument().addDocumentListener(
				new TextFieldDocumentListener(manager,
						read(getFieldDescriptor()), textField));

		manager.addChangeListener(this);

		return component;
	}

	/**
	 * Document listener for the field, used to detect changes and modify the
	 * value of the string using {@link ChangeEAdStringValueCommand}s.
	 */
	private class TextFieldDocumentListener implements DocumentListener {

		private CommandManager manager;

		/**
		 * The key {@link EAdString}
		 */
		private EAdString key;

		/**
		 * The actual JTextComponent, used to get the current value
		 */
		private JTextComponent textField;

		/**
		 * @param key
		 *            The {@link EAdString}
		 * @param textField
		 *            The JTextComponent
		 */
		public TextFieldDocumentListener(CommandManager manager, EAdString key,
				JTextComponent textField) {
			this.manager = manager;
			this.key = key;
			this.textField = textField;
		}

		@Override
		public void changedUpdate(DocumentEvent arg0) {
		}

		@Override
		public void insertUpdate(DocumentEvent arg0) {
			manager.performCommand(new ChangeEAdStringValueCommand(key,
					textField.getText(), stringHandler));
		}

		@Override
		public void removeUpdate(DocumentEvent arg0) {
			manager.performCommand(new ChangeEAdStringValueCommand(key,
					textField.getText(), stringHandler));
		}

	}
}

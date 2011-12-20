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

package es.eucm.eadventure.editor.view.swing.componentproviders;

import javax.swing.JComponent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import es.eucm.eadventure.common.params.EAdString;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.editor.control.CommandManager;
import es.eucm.eadventure.editor.control.FieldValueReader;
import es.eucm.eadventure.editor.control.commands.impl.ChangeEAdStringValueCommand;
import es.eucm.eadventure.editor.view.ComponentProvider;
import es.eucm.eadventure.editor.view.generics.impl.EAdStringOption;
import es.eucm.eadventure.gui.EAdTextArea;
import es.eucm.eadventure.gui.EAdTextField;

/**
 * Swing {@link ComponentProvider} for fields with {@link EAdString} elements.
 * <p>
 * This provider uses a {@link StringHandler} to establish the value of a string
 * and where the new value is stored. The {@link EAdString} is only a key and
 * must not be modified.
 * 
 */
public class EAdStringComponentProvider implements
		ComponentProvider<EAdStringOption, JComponent> {

	/**
	 * The current {@link StringHandler} that maps {@link EAdString}s with their
	 * actual values
	 */
	private StringHandler stringHandler;

	/**
	 * The system dependent {@link FieldValueReader}
	 */
	private FieldValueReader fieldValueReader;

	/**
	 * The {@link CommandManager} to allow for undo/redo operations
	 */
	private CommandManager commandManger;

	/**
	 * Constructor.
	 * 
	 * @param stringHandler
	 *            The {@link StringHandler}
	 * @param fieldValueReader
	 *            The {@link FieldValueReader}
	 * @param commandManager
	 *            The {@link CommandManager}
	 */
	public EAdStringComponentProvider(StringHandler stringHandler,
			FieldValueReader fieldValueReader, CommandManager commandManager) {
		this.stringHandler = stringHandler;
		this.fieldValueReader = fieldValueReader;
		this.commandManger = commandManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.editor.view.ComponentProvider#getComponent(es.eucm
	 * .eadventure.editor.view.generics.InterfaceElement)
	 */
	@Override
	public JComponent getComponent(EAdStringOption element) {
		JComponent component = null;
		JTextComponent textField;
		switch (element.getExpectedLength()) {
		case LONG:
			component = new EAdTextArea(element.getTitle());
			textField = ((EAdTextArea) component).getTextArea();
			break;
		case SHORT:
			textField = new EAdTextField(element.getTitle());
			component = textField;
			break;
		case NORMAL:
		default:
			textField = new EAdTextField(element.getTitle());
			component = textField;
		}
		textField.setToolTipText(element.getToolTipText());

		textField.setText(stringHandler.getString(fieldValueReader
				.readValue(element.getFieldDescriptor())));

		textField.getDocument().addDocumentListener(
				new TextFieldDocumentListener(fieldValueReader
						.readValue(element.getFieldDescriptor()), textField));

		return component;
	}

	/**
	 * Document listener for the field, used to detect changes and modify the
	 * value of the string using {@link ChangeEAdStringValueCommand}s.
	 */
	private class TextFieldDocumentListener implements DocumentListener {

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
		public TextFieldDocumentListener(EAdString key, JTextComponent textField) {
			this.key = key;
			this.textField = textField;
		}

		@Override
		public void changedUpdate(DocumentEvent arg0) {
		}

		@Override
		public void insertUpdate(DocumentEvent arg0) {
			commandManger.performCommand(new ChangeEAdStringValueCommand(key,
					textField.getText(), stringHandler));
		}

		@Override
		public void removeUpdate(DocumentEvent arg0) {
			commandManger.performCommand(new ChangeEAdStringValueCommand(key,
					textField.getText(), stringHandler));
		}

	}

}

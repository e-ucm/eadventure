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

import javax.swing.JComponent;
import ead.editor.control.CommandManager;
import ead.editor.control.commands.ChangeFieldValueCommand;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import ead.editor.control.change.ChangeEvent;
import ead.editor.control.change.ChangeListener;

public class TextOption extends AbstractOption<String> implements
		ChangeListener<ChangeEvent>, DocumentListener {

	private JTextComponent textField;
	private String oldValue = "";

	private CommandManager manager;

	@Override
	public void cleanup(CommandManager manager) {
		manager.removeChangeListener(this);
	}

	@Override
	public void processChange(ChangeEvent event) {
		if (event != null && event.hasChanged(fieldDescriptor)) {
			if (!isUpdating) {
				isUpdating = true;
				textField.setText(read(getFieldDescriptor()));
				isUpdating = false;
			}
			oldValue = textField.getText();
		}
	}

	public static enum ExpectedLength {
		SHORT, NORMAL, LONG
	}

	private ExpectedLength expectedLength;

	public TextOption(String title, String toolTipText,
			FieldDescriptor<String> fieldDescriptor,
			ExpectedLength expectedLength) {
		super(title, toolTipText, fieldDescriptor);
		this.expectedLength = expectedLength;
	}

	public TextOption(String title, String toolTipText,
			FieldDescriptor<String> fieldDescriptor) {
		this(title, toolTipText, fieldDescriptor, ExpectedLength.NORMAL);
	}

	public ExpectedLength getExpectedLength() {
		return expectedLength;
	}

	public void setExpectedLength(ExpectedLength expectedLength) {
		this.expectedLength = expectedLength;
	}

	@Override
	public JComponent getComponent(CommandManager manager) {
		textField = new JTextField(getTitle(), 20);
		textField.setToolTipText(getToolTipText());
		textField.setText(read(getFieldDescriptor()));
		oldValue = textField.getText();

		textField.getDocument().addDocumentListener(this);

		this.manager = manager;
		manager.addChangeListener(this);

		return textField;
	}

	@Override
	public void changedUpdate(DocumentEvent arg0) {
		update();
	}

	@Override
	public void insertUpdate(DocumentEvent arg0) {
		update();
	}

	@Override
	public void removeUpdate(DocumentEvent arg0) {
		update();
	}

	private void update() {
		if (isUpdating) {
			return;
		}

		isUpdating = true;
		manager.performCommand(new ChangeFieldValueCommand<String>(oldValue,
				textField.getText(), getFieldDescriptor()));
		isUpdating = false;
	}
}

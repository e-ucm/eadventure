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

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import ead.editor.control.Command;
import ead.editor.control.commands.ChangeFieldCommand;
import ead.editor.model.nodes.DependencyNode;
import ead.editor.view.generic.accessors.Accessor;

public class TextOption extends DefaultAbstractOption<String> {

	public static enum ExpectedLength {
		SHORT, NORMAL, LONG
	}

	private ExpectedLength expectedLength;

	protected JTextComponent textField;

	public TextOption(String title, String toolTipText, Object object,
			String fieldName, ExpectedLength expectedLength, DependencyNode node) {
		super(title, toolTipText, object, fieldName, node);
		this.expectedLength = expectedLength;
	}

	public TextOption(String title, String toolTipText,
			Accessor<String> fieldDescriptor, ExpectedLength expectedLength,
			DependencyNode... changed) {
		super(title, toolTipText, fieldDescriptor, changed);
		this.expectedLength = expectedLength;
	}

	public TextOption(String title, String toolTipText,
			Accessor<String> fieldDescriptor, DependencyNode... changed) {
		this(title, toolTipText, fieldDescriptor, ExpectedLength.NORMAL,
				changed);
	}

	@Override
	public String getControlValue() {
		return textField.getText();
	}

	@Override
	public void setControlValue(String newValue) {
		textField.setText(newValue);
	}

	public ExpectedLength getExpectedLength() {
		return expectedLength;
	}

	public void setExpectedLength(ExpectedLength expectedLength) {
		this.expectedLength = expectedLength;
	}

	@Override
	public JComponent createControl() {
		switch (expectedLength) {
		case SHORT:
			textField = new JTextField(getTitle(), 10);
			break;
		case NORMAL:
			textField = new JTextField(getTitle(), 20);
			break;
		case LONG:
			textField = new JTextArea(getTitle(), 3, 20);
			((JTextArea) textField).setLineWrap(true);
			((JTextArea) textField).setWrapStyleWord(true);
			break;
		default:
			throw new IllegalArgumentException("Not a valid length: "
					+ expectedLength);
		}
		textField.setToolTipText(getToolTipText());
		textField.setText(fieldDescriptor.read());
		textField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent de) {
				update();
			}

			@Override
			public void removeUpdate(DocumentEvent de) {
				update();
			}

			@Override
			public void changedUpdate(DocumentEvent de) {
				update();
			}
		});
		if (expectedLength.equals(ExpectedLength.LONG)) {
			JScrollPane jsp = new JScrollPane(textField);
			jsp.setMinimumSize(new Dimension(0, 40));
			return jsp;
		} else {
			return textField;
		}
	}

	@Override
	protected Command createUpdateCommand() {
		// Users expect to undo/redo entire words, rather than character-by-character
		return new ChangeFieldCommand<String>(getControlValue(),
				getFieldDescriptor(), changed) {
			@Override
			public boolean likesToCombine(String nextValue) {
				return nextValue.startsWith(newValue)
						&& nextValue.length() == newValue.length() + 1
						&& !Character.isWhitespace(nextValue.charAt(nextValue
								.length() - 1));
			}
		};
	}
}

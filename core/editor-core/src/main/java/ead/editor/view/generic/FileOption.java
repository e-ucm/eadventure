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

import ead.editor.view.generic.accessors.Accessor;
import ead.editor.control.Command;
import ead.editor.control.commands.ChangeFileCommand;
import ead.editor.control.commands.FileCache;
import ead.editor.model.nodes.DependencyNode;
import ead.utils.i18n.I18N;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class FileOption extends AbstractOption<File> {

	private JTextField textField;
	private JButton chooserButton;
	private FileCache fileCache;
	private String buttonText;

	public FileOption(String title, String toolTipText, String buttonText,
			Accessor<File> fieldDescriptor, FileCache fileCache,
			DependencyNode node) {
		super(title, toolTipText, fieldDescriptor, node);
		this.fileCache = fileCache;
		this.buttonText = buttonText;
	}

	@Override
	protected File getControlValue() {
		return new File(textField.getText());
	}

	@Override
	protected void setControlValue(File newValue) {
		textField.setText(newValue.getPath());
	}

	@Override
	protected JComponent createControl() {
		final JPanel controls = new JPanel(new BorderLayout(4, 0));
		textField = new JTextField(getTitle(), 20);
		textField.setToolTipText(getToolTipText());
		textField.setText(fieldDescriptor.read().getPath());
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
		controls.add(textField, BorderLayout.CENTER);

		chooserButton = new JButton(buttonText);
		chooserButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				File f = chooseFile(controls, getToolTipText(), true,
						JFileChooser.FILES_ONLY, "Error", "Could not open XYZ");
				if (f != null) {
					setControlValue(f);
				}
			}
		});
		controls.add(chooserButton, BorderLayout.EAST);
		return controls;
	}

	/**
	 * Should return whether a value is valid or not. Invalid values will
	 * not generate updates, and will therefore not affect either model or other
	 * views.
	 * @param value
	 * @return whether it is valid or not; default is "always-true"
	 */
	@Override
	protected boolean isValid(File value) {
		return value.isFile() && value.canRead();
	}

	@Override
	protected Command createUpdateCommand() {
		return new ChangeFileCommand(getControlValue(), getFieldDescriptor(),
				fileCache, node);
	}

	/**
	 * A simple file-chooser - launched when the button is pressed.
	 * @param p
	 * @param message
	 * @param toOpen
	 * @param fileType
	 * @param errorTitle
	 * @param errorTemplate
	 * @return 
	 */
	public static File chooseFile(Component p, String message, boolean toOpen,
			int fileType, String errorTitle, String errorTemplate) {
		JFileChooser jfc = new JFileChooser();
		jfc.setDialogTitle(message);
		jfc.setFileSelectionMode(fileType);
		File f = null;
		while (f == null) {
			int rc = (toOpen ? jfc.showOpenDialog(p) : jfc.showSaveDialog(p));
			if (rc == JFileChooser.CANCEL_OPTION) {
				f = null;
				break;
			}

			f = jfc.getSelectedFile();
			if (f == null || (!f.exists() && toOpen)
					|| (fileType == JFileChooser.FILES_ONLY && f.isDirectory())) {
				JOptionPane.showMessageDialog(null,
						I18N.bind(errorTemplate, f), errorTitle,
						JOptionPane.ERROR_MESSAGE);
				f = null;
				continue;
			}
		}
		return f;
	}
}

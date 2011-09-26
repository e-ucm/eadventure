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

package es.eucm.eadventure.gui.listpanel.columntypes;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.Border;

import es.eucm.eadventure.gui.R;

/**
 * 
 */
public class ConditionsCellRendererEditor extends AbstractCellEditor implements
		CellRenderEditor {

	private static final long serialVersionUID = 1L;

	private boolean useText;

	// TODO conditions controller
	private Object value;

	public ConditionsCellRendererEditor() {
		this.useText = false;
	}

	public ConditionsCellRendererEditor(boolean useText) {
		this.useText = useText;
	}

	public Object getCellEditorValue() {

		return value;
	}

	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int col) {
		if (value == null)
			// TODO return null;
			this.value = (Object) value;
		return createButton(isSelected, table);
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		if (value == null)
			// TODO return null;
			this.value = (Object) value;
		return createButton(isSelected, table);
	}

	private Icon createIcon() throws IOException {

		// Create icon
		InputStream is;
		Icon icon = null;

		boolean hasConditions = false;

		if (hasConditions) {
			is = ClassLoader
					.getSystemResourceAsStream(R.Drawable.conditions16x16_png);
			icon = new ImageIcon(ImageIO.read(is));
		} else {
			is = ClassLoader
					.getSystemResourceAsStream(R.Drawable.no_conditions16x16_png);
			icon = new ImageIcon(ImageIO.read(is));
		}

		return icon;
	}

	private Component createButton(boolean isSelected, JTable table) {

		JPanel temp = new JPanel();
		Border border = BorderFactory.createEmptyBorder(2, 2, 2, 2);
		if (isSelected) {
			border = BorderFactory.createCompoundBorder(
					BorderFactory.createMatteBorder(2, 0, 2, 0,
							table.getSelectionBackground()), border);
		}
		temp.setBorder(border);
		JButton button = null;

		// Create text (if applicable)
		String text = null;
		if (useText) {
			// TODO Internationalization
			text = ("GeneralText.EditConditions");
		}

		// Create icon (if applicable)
		Icon icon = null;
		try {
			icon = createIcon();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Create button
		if (text != null && icon != null) {
			button = new JButton(text, icon);
			button.setToolTipText(text);
		} else if (text != null) {
			button = new JButton(text);
			button.setToolTipText(text);
		} else if (icon != null) {
			button = new JButton(icon);
			button.setContentAreaFilled(false);
			button.setOpaque(false);
		}

		button.setFocusable(false);
		button.setEnabled(isSelected);

		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				// TODO
				// new ConditionsDialog( ConditionsCellRendererEditor.this.value
				// );
				// Update icon
				try {
					((JButton) (arg0.getSource())).setIcon(createIcon());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		temp.setLayout(new BorderLayout());
		temp.add(button, BorderLayout.CENTER);
		// temp.add(button);

		// button.requestFocus();
		return temp;

	}

	@Override
	public boolean isEditable() {
		return true;
	}

}

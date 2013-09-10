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

package es.eucm.ead.editor.view.generic;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

import es.eucm.ead.editor.model.nodes.DependencyNode;
import es.eucm.ead.editor.view.generic.accessors.Accessor;

public class ColorOption extends DefaultAbstractOption<Color> {

	private static JColorChooser jcc = new JColorChooser();
	private Color controlValue = null;

	private JButton colorButton;
	private BufferedImage colorImage = new BufferedImage(32, 32,
			BufferedImage.TYPE_INT_ARGB);

	private void paintIcon(Color color) {
		Graphics2D g = (Graphics2D) colorImage.getGraphics();
		for (int j = 0; j < colorImage.getHeight() / 8 + 1; j++) {
			for (int i = 0; i < colorImage.getWidth() / 8 + 1; i++) {
				g.setColor(((i + j) % 2) == 0 ? Color.BLACK : Color.WHITE);
				g.fillRect(i * 8, j * 8, 8, 8);
				g.setColor(color);
				g.fillRect(i * 8, j * 8, 8, 8);
			}
		}
	}

	public ColorOption(String title, String toolTipText, Object object,
			String fieldName, DependencyNode... changed) {
		super(title, toolTipText, object, fieldName, changed);
	}

	public ColorOption(String title, String toolTipText,
			Accessor<Color> fieldDescriptor, DependencyNode... changed) {
		super(title, toolTipText, fieldDescriptor, changed);
	}

	@Override
	protected JComponent createControl() {
		colorButton = new JButton();
		oldValue = readModelValue();
		setControlValue(oldValue);
		colorButton.setToolTipText(getToolTipText());

		colorButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				setControlValue(readModelValue());
				jcc.setColor(controlValue);
				JOptionPane.showMessageDialog(colorButton.getParent(), jcc,
						"Select a color", JOptionPane.QUESTION_MESSAGE);
				if (!controlValue.equals(jcc.getColor())
						&& jcc.getColor() != null) {
					setControlValue(jcc.getColor());
					update();
				}
			}
		});
		return colorButton;
	}

	@Override
	public Color getControlValue() {
		return controlValue;
	}

	@Override
	protected void setControlValue(Color newValue) {
		paintIcon(newValue);
		colorButton.setIcon(new ImageIcon(colorImage));
		controlValue = newValue;
	}
}

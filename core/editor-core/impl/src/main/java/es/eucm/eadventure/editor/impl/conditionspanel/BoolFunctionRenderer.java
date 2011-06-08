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

package es.eucm.eadventure.editor.impl.conditionspanel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.eucm.eadventure.common.model.conditions.impl.ListedCondition.Operator;
import es.eucm.eadventure.editor.impl.Messages;

/**
 * The renderer class for the evaluation function of a listed condition (AND, OR)
 * @author Roberto Tornero
 * 
 */
public class BoolFunctionRenderer extends JPanel{

	private static final long serialVersionUID = -5275581739883291548L;

	protected static final Logger logger = LoggerFactory.getLogger("BoolFunctionRenderer");
	/**
	 * The label with the value of the operator
	 */
	private JLabel valueLabel;
	/**
	 * The combo-box for selecting values for the operator
	 */
	private JComboBox valueCombo;
	/**
	 * The button to edit the properties of the evaluation function
	 */
	private JButton editButton;
	/**
	 * The controller for the appearance and customization of the conditions
	 */
	private ConditionsController controller;
	/**
	 * The current operator for the evaluation function 
	 */
	private Operator operator;

	/**
	 * Constructor for the renderer of the evaluation function
	 */
	public BoolFunctionRenderer(ConditionsController contr, Operator op){

		super();
		setBackground(Color.white);
		setMinimumSize(new Dimension(150, 60));
		setPreferredSize(new Dimension(150, 60));
		setMaximumSize(new Dimension(150, 60));

		controller = contr;
		operator = op;

		valueLabel = new JLabel(operator.toString());

		valueCombo = new JComboBox(new String[] {ConditionMessages.and_message, ConditionMessages.or_message});

		valueCombo.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {

				valueLabel.setText(valueCombo.getSelectedItem().toString());

				if (valueCombo.getSelectedItem().equals(ConditionMessages.and_message))
					operator = Operator.AND;
				else operator = Operator.OR;

				remove(valueCombo);
				add(valueLabel);
				add(editButton);
				BoolFunctionRenderer.this.repaint();
				BoolFunctionRenderer.this.revalidate();

				controller.updateOperator(operator);
			}

		});

		try {
			editButton = new JButton(new ImageIcon(ImageIO.read(getResourceAsStream("@drawable/edit.png"))));

		} catch (IOException e) {
			logger.error(Messages.input_output_exception);
		}		

		editButton.setContentAreaFilled(false);
		editButton.setBorderPainted(false);
		editButton.setFocusable(false);

		editButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {

				remove(valueLabel);
				remove(editButton);
				add(valueCombo);
				BoolFunctionRenderer.this.repaint();
				BoolFunctionRenderer.this.revalidate();
			}

		});

		add(valueLabel);
		add(editButton);		
	}

	/**
	 * Overrided method for the customized painting of the components
	 */
	@Override
	protected void paintComponent(Graphics g) {

		int strokeSize = 1;
		Color shadowColor = Color.black;
		boolean shady = true;
		boolean highQuality = true;
		Dimension arcs = new Dimension(20, 20);
		int shadowGap = 5;
		int shadowOffset = 4;
		int shadowAlpha = 150;

		super.paintComponent(g);
		int width = getWidth();
		int height = getHeight();

		Color shadowColorA = new Color(shadowColor.getRed(), 
				shadowColor.getGreen(), shadowColor.getBlue(), shadowAlpha);
		Graphics2D graphics = (Graphics2D) g;

		if (highQuality) {
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
					RenderingHints.VALUE_ANTIALIAS_ON);
		}

		if (shady) {
			graphics.setColor(shadowColorA);
			graphics.fillRoundRect(shadowOffset, shadowOffset, width - strokeSize - shadowOffset, height - strokeSize - shadowOffset, arcs.width, arcs.height);
		} else {
			shadowGap = 1;
		}

		graphics.setColor(getBackground());
		graphics.fillRoundRect(0, 0, width - shadowGap,	height - shadowGap, arcs.width, arcs.height);
		graphics.setColor(getForeground());
		graphics.setStroke(new BasicStroke(strokeSize));
		graphics.drawRoundRect(0, 0, width - shadowGap,	height - shadowGap, arcs.width, arcs.height);

		graphics.setStroke(new BasicStroke());
	}

	/**
	 * +++Temporal method for loading resources, future AssetHandler instance expected+++  
	 */
	protected InputStream getResourceAsStream(String path) {

		String location = path.replaceAll("@", "");
		return ClassLoader.getSystemResourceAsStream(location);
	}

}

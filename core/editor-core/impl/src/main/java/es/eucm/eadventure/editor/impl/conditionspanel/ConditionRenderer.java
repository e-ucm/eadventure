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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.editor.impl.Messages;

/**
 * Generic class for the condition renderers
 * @author Roberto Tornero
 *
 */
public class ConditionRenderer extends JPanel {

	private static final long serialVersionUID = -273206588159875790L;

	protected static final Logger logger = LoggerFactory.getLogger("ConditionRenderer");

	/*
	 * Fields for displaying. The structure is as follows: ICON Subject(id) Verb(Action) directComplement(Value)
	 */

	/**
	 * Label with the icon of the condition type
	 */
	protected JLabel iconLabel;
	/**
	 * List with the components to display on the renderer
	 */
	protected List<Component> renderComponents;
	/**
	 * Panel with buttons for removing, duplicating and editing each condition 
	 */
	protected ConditionButtonsPanel buttonsPanel;
	/**
	 * Condition with the values to display on the renderer
	 */
	protected EAdCondition condition;
	/**
	 * Controller for the changes on the conditions
	 */
	protected ConditionsController controller;
	/**
	 * Button to create the NOTCondition of the current condition
	 */
	protected JButton notButton;
	/**
	 * Boolean to detect if the condition is or is not a NOTCondition 
	 */
	protected boolean not;

	/**
	 * Constructor for the generic conditions renderer
	 * @param contr
	 * @param cond
	 * @param no
	 */
	public ConditionRenderer(ConditionsController contr, EAdCondition cond, boolean no){

		super();
		setBackground(Color.white);
		setMinimumSize(new Dimension(200, 105));
		setPreferredSize(new Dimension(200, 105));
		setMaximumSize(new Dimension(200, 105));
		condition = cond;
		iconLabel = new JLabel();
		renderComponents = new ArrayList<Component>(); 
		not = no;

		add(createNotButton());		
		buttonsPanel = new ConditionButtonsPanel();
		controller = contr;

	}

	/**
	 * Returns the notButton with its proper appearance (red if NOTCondition, white if plain condition) 
	 * @return
	 */
	private Component createNotButton() {
		notButton = null;

		if (not)
			try {
				notButton = new JButton(new ImageIcon(ImageIO.read(getResourceAsStream("@drawable/conditions/notOn.png"))));
			} catch (IOException e) {			
				logger.error(Messages.input_output_exception);
			}
			else try {
				notButton = new JButton(new ImageIcon(ImageIO.read(getResourceAsStream("@drawable/conditions/notOff.png"))));
			} catch (IOException e) {				
				logger.error(Messages.input_output_exception);
			}			

			notButton.setBorderPainted(false);
			notButton.setContentAreaFilled(false);

			notButton.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {				

					if (ConditionRenderer.this.getParent() instanceof NOTConditionRenderer)
						condition = controller.setNotCondition(((NOTConditionRenderer)ConditionRenderer.this.getParent()).getNotCondition(), not);					
					else condition = controller.setNotCondition(condition, not);

					notButton.repaint();
					notButton.revalidate();
				}

			});

			return notButton;
	}

	/**
	 * Overrided method for customized painting of the components
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
	 * Returns the condition of the renderer
	 * @return
	 */
	protected EAdCondition getCondition() {
		return condition;		
	}

	/**
	 * Sets the condition of the renderer
	 * @param cond
	 */
	protected void setCondition(EAdCondition cond) {
		condition = cond;		
	}


	/**
	 * +++Temporal method for loading resources, future AssetHandler instance expected+++  
	 */
	protected InputStream getResourceAsStream(String path) {

		String location = path.replaceAll("@", "");
		return ClassLoader.getSystemResourceAsStream(location);
	}

	/**
	 * Class that represents the buttons panel for each condition renderer
	 * @author Roberto Tornero
	 *
	 */
	public class ConditionButtonsPanel extends JPanel {


		private static final long serialVersionUID = 6621666519868576066L;
		/**
		 * The button for deleting conditions
		 */
		private JButton deleteButton;
		/**
		 * The button for duplicating conditions
		 */
		private JButton duplicateButton;
		/**
		 * The button for editing conditions
		 */
		private JButton editButton;

		/**
		 * Constructor for the buttons panel class
		 */
		public ConditionButtonsPanel() {

			super();
			setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
			setBackground(Color.white);

			try {
				deleteButton = new JButton(new ImageIcon(ImageIO.read(getResourceAsStream("@drawable/deleteNode.png"))));
				duplicateButton = new JButton(new ImageIcon(ImageIO.read(getResourceAsStream("@drawable/duplicateNode.png"))));
				editButton = new JButton(new ImageIcon(ImageIO.read(getResourceAsStream("@drawable/edit.png"))));

			} catch (IOException e) {
				logger.error(Messages.input_output_exception);
			}		

			deleteButton.setContentAreaFilled(false);        
			duplicateButton.setContentAreaFilled(false);      
			editButton.setContentAreaFilled(false); 

			deleteButton.setBorderPainted(false);
			duplicateButton.setBorderPainted(false);
			editButton.setBorderPainted(false);

			deleteButton.setFocusable(false);
			duplicateButton.setFocusable(false);
			editButton.setFocusable(false);

			deleteButton.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {

					controller.deleteConditionFrom(condition);
				}

			});

			duplicateButton.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {

					controller.duplicateConditionFrom(condition);
				}

			});

			editButton.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {

					controller.editCondition(condition);
				}

			});

			add(deleteButton);
			add(duplicateButton);
			add(editButton);

		}

	}

}

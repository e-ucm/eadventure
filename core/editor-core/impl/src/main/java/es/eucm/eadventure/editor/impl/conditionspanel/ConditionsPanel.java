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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.eucm.eadventure.common.model.conditions.impl.ListedCondition;
import es.eucm.eadventure.common.model.variables.impl.IntegerVar;
import es.eucm.eadventure.editor.impl.VarFlagSummary;
import es.eucm.eadventure.utils.i18n.CommonMessages;

/**
 * The panel to display and edit, remove or add new conditions
 * @author Roberto Tornero
 *
 */
public class ConditionsPanel extends JDialog {


	private static final long serialVersionUID = 8030288094853405175L;

	protected static final Logger logger = LoggerFactory.getLogger("ConditionsPanel");

	/**
	 * The panel in top of the dialog
	 */
	private JPanel topPanel;
	/**
	 * The editor pane to display the description of the condition state
	 */
	private JEditorPane textPane;
	/**
	 * The panel with the add, ok and info buttons
	 */
	private JPanel buttonsPanel;
	/**
	 * The button for adding new conditions
	 */
	private JButton addConditionButton;
	/**
	 * The button to accept the changes
	 */
	private JButton okButton;
	/**
	 * The renderer for displaying the conditions
	 */
	private ListedConditionRenderer conditionRenderer;
	/**
	 * The controller for conditions
	 */
	private ConditionsController controller;

	/**
	 * Constructor for the conditions panel
	 */
	public ConditionsPanel(VarFlagSummary summary){

		setLayout(new BorderLayout());
		setSize(640,480);
		this.setResizable(false);
		topPanel = createTopPanel();
		add(topPanel, BorderLayout.NORTH);

		createButtonsPanel();
		add(buttonsPanel, BorderLayout.SOUTH);

		conditionRenderer = new ListedConditionRenderer();
		controller = new ConditionsController(this, summary);

		createCentralPanel();
	}

	/**
	 * Method for creating the properties of the topPanel
	 * @return
	 */
	private JPanel createTopPanel(){

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		topPanel.setBorder(BorderFactory.createLineBorder(Color.black));

		textPane = new JEditorPane();
		textPane.setBackground(Color.white);
		textPane.setText("Description for condition state will be painted here");
		textPane.setEditable(false);

		topPanel.add(textPane, BorderLayout.CENTER);
		return topPanel;
	}

	/**
	 * Method for creating the central panel, which includes the renderer
	 */
	private void createCentralPanel(){

		JScrollPane scroll = new JScrollPane(conditionRenderer, ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(scroll, BorderLayout.CENTER);
	}

	/**
	 * Method for creating the buttons panel
	 */
	private void createButtonsPanel(){

		buttonsPanel = new JPanel();

		JButton helpButton = null;

		try {
			addConditionButton = new JButton(new ImageIcon(ImageIO.read(getResourceAsStream("@drawable/addNode.png"))));
			helpButton = new JButton(new ImageIcon(ImageIO.read(getResourceAsStream("@drawable/information.png"))));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		addConditionButton.setText(ConditionMessages.add_condition);
		addConditionButton.addActionListener(new ActionListener(){

			public void actionPerformed( ActionEvent e ) {

				controller.addConditionFrom();            	
			}
		} );

		buttonsPanel.add(addConditionButton);

		okButton = new JButton(CommonMessages.ok);
		okButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				ConditionsPanel.this.dispose();				
			}

		});
		buttonsPanel.add(okButton); 

		helpButton.setContentAreaFilled(false);
		helpButton.setMargin(new Insets(0,0,0,0));
		helpButton.setFocusable(false);
		buttonsPanel.add(helpButton);     		

		buttonsPanel.setBorder(BorderFactory.createLineBorder(Color.black));		
	}

	/**
	 * A setter for an existing listed condition
	 * @param list
	 */
	public void setListedCondition(ListedCondition list){

		controller.setListedCondition(list);
	}

	/**
	 * Returns the renderer
	 * @return
	 */
	public ListedConditionRenderer getRenderer() {
		return conditionRenderer;
	}

	/**
	 * +++Temporal method for loading resources, future AssetHandler instance expected+++  
	 */
	private InputStream getResourceAsStream(String path) {

		String location = path.replaceAll("@", "");
		return ClassLoader.getSystemResourceAsStream(location);
	}


	/**
	 * Main method to test the functionality
	 * @param args
	 */
	public static void main(String args[]){

		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {

				VarFlagSummary summary = new VarFlagSummary();

				summary.addVar(new IntegerVar("Var1"));
				summary.addVar(new IntegerVar("Var2"));
				summary.addFlag("Flag1");
				summary.addFlag("Flag2");

				ConditionsPanel p = new ConditionsPanel(summary);		
				p.setVisible(true);				
			}

		});

	}	

}

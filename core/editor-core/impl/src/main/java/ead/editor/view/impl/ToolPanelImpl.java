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

package ead.editor.view.impl;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import com.google.inject.Inject;

import ead.editor.control.CommandManager;
import ead.editor.control.NavigationController;
import ead.editor.control.change.ChangeListener;
import ead.editor.view.ToolPanel;
import ead.gui.EAdBorderedPanel;
import ead.gui.EAdSimpleButton;
import ead.gui.EAdTextField;
import ead.utils.swing.SwingUtilities;

/**
 * Default implementation of the tool panel
 */
public class ToolPanelImpl implements ToolPanel, ChangeListener {

	/**
	 * The pane where the tools are drawn
	 */
	private JPanel toolPanel;
	
	/**
	 * Redo button
	 */
	private EAdSimpleButton redoButton;
	
	/**
	 * Undo button
	 */
	private EAdSimpleButton undoButton;
	
	/**
	 * Search button
	 */
	private EAdSimpleButton searchButton;
	
	/**
	 * Navigate forward button
	 */
	private EAdSimpleButton forwardButton;
	
	/**
	 * Navigate backward button
	 */
	private EAdSimpleButton backwardButton;
	
	/**
	 * Search text field
	 */
	private EAdTextField searchField;
	
	/**
	 * The action manager
	 */
	private CommandManager actionManager;
	
	/**
	 * The navigation controller
	 */
	private NavigationController navigationController;
	
	@Inject
	public ToolPanelImpl(CommandManager actionManager,
			NavigationController navigationController) {
		this.actionManager = actionManager;
		this.actionManager.addChangeListener(this);
		this.navigationController = navigationController;
		this.navigationController.addChangeListener(this);

		toolPanel = new EAdBorderedPanel(null);
		SwingUtilities.doInEDTNow(new Runnable() {
			public void run() {
				addRedoButton();
				addUndoButton();
				addSeparator();
				addBackwardButton();
				addForwardButton();
				addSeparator();
				addSearchFieldAndButton();
			}
		});
		
		processChange();
	}

	@Override
	public JPanel getPanel() {
		return toolPanel;
	}
	
	@Override
	public void processChange() {
		SwingUtilities.doInEDT(new Runnable() {
			public void run() {
				redoButton.setEnabled(actionManager.canRedo());
				undoButton.setEnabled(actionManager.canUndo());
				forwardButton.setEnabled(navigationController.canGoForward());
				backwardButton.setEnabled(navigationController.canGoBackward());
				toolPanel.repaint();
			}
		});
	}

	/**
	 * Add a separator in the tool panel
	 */
	private void addSeparator() {
        JSeparator separator = new JSeparator( SwingConstants.VERTICAL );
        separator.setPreferredSize( new Dimension( 2, 24 ) );
        toolPanel.add(separator);
	}
	
	/**
	 * Add the redo action button
	 */
	private void addRedoButton() {
		redoButton = new EAdSimpleButton(EAdSimpleButton.SimpleButton.REDO);
		toolPanel.add(redoButton);
		redoButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				actionManager.redoCommand();
			}
			
		});
	}

	/**
	 * Add the undo action button
	 */
	private void addUndoButton() {
		undoButton = new EAdSimpleButton(EAdSimpleButton.SimpleButton.UNDO);
		toolPanel.add(undoButton);
		redoButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				actionManager.undoCommand();
			}
			
		});
	}

	/**
	 * Add the search field and button
	 */
	private void addSearchFieldAndButton() {
		searchButton = new EAdSimpleButton(EAdSimpleButton.SimpleButton.SEARCH);
		searchField = new EAdTextField(20);
		searchField.setToolTipText("Search...");
		toolPanel.add(searchField);
		toolPanel.add(searchButton);
		searchButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				//TODO search interface
			}
			
		});
	}

	/**
	 * Add the forward button to the tool panel
	 */
	private void addForwardButton() {
		forwardButton = new EAdSimpleButton(EAdSimpleButton.SimpleButton.FORWARD);
		toolPanel.add(forwardButton);
		forwardButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				navigationController.goForward();
			}
			
		});
	}

	/**
	 * Add backward button to the tool panel
	 */
	private void addBackwardButton() {
		backwardButton = new EAdSimpleButton(EAdSimpleButton.SimpleButton.BACKWARD);
		toolPanel.add(backwardButton);
		backwardButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				navigationController.goBackward();
			}
			
		});
	}

}

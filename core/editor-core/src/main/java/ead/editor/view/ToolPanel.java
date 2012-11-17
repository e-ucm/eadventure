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

package ead.editor.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import com.google.inject.Inject;

import ead.editor.control.CommandManager;
import ead.editor.control.Controller;
import ead.editor.control.NavigationController;
import ead.editor.control.ProjectController;
import ead.editor.control.ViewController;
import ead.editor.control.change.ChangeListener;
import ead.editor.model.nodes.DependencyNode;
import ead.editor.model.nodes.QueryNode;
import ead.editor.view.EditorWindow;
import ead.editor.view.ToolPanel;
import ead.gui.EAdSimpleButton;
import ead.gui.EAdTextField;
import ead.utils.swing.SwingUtilities;
import java.awt.FlowLayout;
import javax.swing.*;

/**
 * Default implementation of the tool panel
 */
public class ToolPanel implements ChangeListener {

	/**
	 * The pane where the tools are drawn
	 */
	private JPanel toolPanel;

	/**
	 * Redo button
	 */
	private JButton redoButton;

	/**
	 * Undo button
	 */
	private JButton undoButton;

	/**
	 * Search button
	 */
	private JButton searchButton;

	/**
	 * Navigate forward button
	 */
	private JButton forwardButton;

	/**
	 * Navigate backward button
	 */
	private JButton backwardButton;

	/**
	 * Search text field
	 */
	private JTextField searchField;

    /**
     * Current controller
     */
    private Controller controller;

    /**
     *
     * @param actionManager
     * @param navigationController
     */
	@Inject
	public ToolPanel(Controller controller) {
		this.controller = controller;

        toolPanel = new JPanel(new FlowLayout());
		SwingUtilities.doInEDTNow(new Runnable() {
			@Override
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
	}

	public JPanel getPanel() {
		controller.getCommandManager().addChangeListener(this);
		controller.getNavigationController().addChangeListener(this);
        controller.getProjectController().addChangeListener(this);
		return toolPanel;
	}

	@Override
	public final void processChange(Object o) {
		SwingUtilities.doInEDT(new Runnable() {
			@Override
			public void run() {
                CommandManager cm = controller.getCommandManager();
				redoButton.setEnabled(cm.canRedo());
				undoButton.setEnabled(cm.canUndo());
                NavigationController nm = controller.getNavigationController();
				forwardButton.setEnabled(nm.canGoForward());
				backwardButton.setEnabled(nm.canGoBackward());
                searchField.setEnabled(controller.getModel().getEngineModel() != null);
                searchButton.setEnabled(controller.getModel().getEngineModel() != null);
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
        redoButton.setEnabled(false);
		toolPanel.add(redoButton);
		redoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.getCommandManager().redoCommand();
			}

		});
	}

	/**
	 * Add the undo action button
	 */
	private void addUndoButton() {
		undoButton = new EAdSimpleButton(EAdSimpleButton.SimpleButton.UNDO);
        undoButton.setEnabled(false);
		toolPanel.add(undoButton);
		redoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.getCommandManager().undoCommand();
			}

		});
	}

	/**
	 * Add the search field and button
	 */
	private void addSearchFieldAndButton() {
		searchButton = new EAdSimpleButton(EAdSimpleButton.SimpleButton.SEARCH);
		searchButton.setEnabled(false);
        searchField = new JTextField(20);
        searchField.setEnabled(false);
		searchField.setToolTipText("Search...");
		toolPanel.add(searchField);
		toolPanel.add(searchButton);

        ActionListener queryListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String query = searchField.getText();
                controller.getViewController().addView("query", "q"+query, false);
			}
		};

        searchField.addActionListener(queryListener);
		searchButton.addActionListener(queryListener);
    }

	/**
	 * Add the forward button to the tool panel
	 */
	private void addForwardButton() {
		forwardButton = new EAdSimpleButton(EAdSimpleButton.SimpleButton.FORWARD);
		forwardButton.setEnabled(false);
        toolPanel.add(forwardButton);
		forwardButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.getNavigationController().goForward();
			}
		});
	}

	/**
	 * Add backward button to the tool panel
	 */
	private void addBackwardButton() {
		backwardButton = new EAdSimpleButton(EAdSimpleButton.SimpleButton.BACKWARD);
		backwardButton.setEnabled(false);
        toolPanel.add(backwardButton);
		backwardButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.getNavigationController().goBackward();
			}
		});
	}
}

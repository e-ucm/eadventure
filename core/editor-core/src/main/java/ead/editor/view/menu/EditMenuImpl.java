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

package ead.editor.view.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.google.inject.Inject;

import ead.editor.control.CommandManager;
import ead.editor.control.change.ChangeListener;
import ead.editor.view.menu.EditMenu;
import ead.gui.EAdMenuItem;
import ead.utils.swing.SwingUtilities;

/**
 * Default implementation of the editor menu.
 */
public class EditMenuImpl extends MenuImpl implements EditMenu, ChangeListener {
	
	/**
	 * Undo menu item
	 */
	private EAdMenuItem undo;
	
	/**
	 * Undo menu item
	 */
	private EAdMenuItem redo;
	
	/**
	 * Instance of the action manager
	 */
	private CommandManager actionManager;
	
	@Inject
	public EditMenuImpl(CommandManager actionManager) {
		super(Messages.edit_menu);
		
		this.actionManager = actionManager;
		
		initialize();
	}
	
	/**
	 * Initialize the editor menu
	 */
	private void initialize() {
		undo = new EAdMenuItem(Messages.edit_menu_undo);
		undo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				actionManager.undoCommand();
			}
			
		});
		addMenuItem(undo);
		
		redo = new EAdMenuItem(Messages.edit_menu_redo);
		redo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				actionManager.redoCommand();
			}
			
		});
		addMenuItem(redo);
	
		actionManager.addChangeListener(this);
		
		processChange();
	}

	@Override
	public void processChange() {
		SwingUtilities.doInEDT(new Runnable() {

			@Override
			public void run() {
				undo.setEnabled(actionManager.canUndo());
				redo.setEnabled(actionManager.canRedo());
			}
			
		});
	}

}

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

import ead.editor.control.Controller;
import javax.swing.JPanel;

import ead.editor.view.menu.EditorMenuBar;

/**
 * Interface for the eAdventure editor window.
 */
public interface EditorWindow {
	
	/**
	 * Initialize the editor window
	 */
	void initialize();

	/**
	 * Show the editor window
	 */
	void showWindow();

	/**
	 * Creates a new modal pane.
	 * @param modalPanel Add a new modal panel
	 */
	void addModalPanel(JPanel modalPanel);

	/**
	 * Creates a new view.
	 * @param type used for grouping purposes; views with the same type
	 *    can be grouped together. 
	 * @param view actual view to place in editor
	 * @param elementId of element being edited; null indicates no particular element
	 * @param reuseExisting - if specified, will try to reuse existing views
	 *    of the same element.
	 */
	void addView(String type, String elementId, JPanel view, boolean reuseExisting);
	
	/**
	 * Remove the top modal panel
	 */
	void removeModalPanel();
	
	/**
	 * Return the controller used for this window.
	 * @return 
	 */
	public Controller getController();
	
	public JPanel getLeftPanel();

	public JPanel getMainPanel();

	public JPanel getTitlePanel();

	public ToolPanel getToolPanel();

	public EditorMenuBar getEditorMenuBar();
	
}

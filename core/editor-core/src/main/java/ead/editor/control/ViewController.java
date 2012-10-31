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

package ead.editor.control;

import javax.swing.JPanel;

/**
 * Interface for the view controller.
 */
public interface ViewController {
	/**
	 * Show the eAdventure editor window
	 */
	void showWindow();

	/**
	 * Add a modal pane to the main editor window
	 *
	 * @param modelPanel The modal pane
	 */
	void addModalPanel(JPanel modalPane);

	/**
	 * Remove a modal panel, possibly canceling any changes.
	 *
	 * @param cancelChanges Cancel the changes of the actions in
	 * the modal pane
	 */
	void removeModalPanel(boolean cancelChanges);

    /**
	 * Creates a new view.
	 * @param type used for grouping purposes; views with the same type
	 *    can be grouped together.
	 * @param elementId of element being edited; null indicates no particular element
	 * @param reuseExisting - if specified, will try to reuse existing views
	 *    of the same element.
	 */
	void addView(String type, String elementId, boolean reuseExisting);

	/**
	 * Restores windows to a previously saved state
	 */
	void restoreViews();

	/**
	 * Saves view state
	 */
	void saveViews();

	/**
	 * Saves view state
	 */
	void clearViews();

	/**
	 * Sets a title qualifier, used to display (say) currently-edited file.
	 * @param titleQualifier 
	 */
	public void setTitleQualifier(String titleQualifier);	
	
	/**
	 * Set the actual super-controller.
	 * @param controller the main controller, providing access to model, views,
	 * and more
	 */
	public void setController(Controller controller);
}

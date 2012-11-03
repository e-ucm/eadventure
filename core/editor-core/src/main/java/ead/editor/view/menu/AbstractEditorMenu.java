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

import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.KeyStroke;

import ead.editor.control.Controller;
import ead.editor.control.change.ChangeListener;
import ead.gui.EAdMenuItem;

/**
 * An abstract editor menu. All editor menus should descend from this one
 * @author mfreire
 */
public abstract class AbstractEditorMenu extends JMenu {

	protected Controller controller;

	private static int platformMnemonic 
			= Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
	
	/**
	 * Constructor. Should not do any real work (leave that to the initialization)
	 * @param controller
	 * @param name 
	 */
    public AbstractEditorMenu(Controller controller, String name) {
        super(removeMenuKeyIndicator(name));
        this.controller = controller;
		setMnemonic(getMenuKeyFromName(name));
    }
	
	/**
	 * Finishes menu setup. 
	 * Should only be called once all controllers have been correctly set up.
	 */
	public abstract void initialize();
	
	/**
	 * Returns the keycode for the char after the first '_' in the text.
	 * @param text
	 * @return 
	 */
	private static int getMenuKeyFromName(String text) {
		int pos = text.indexOf('_');
		if (pos == -1 || pos+1 == text.length()) {
			throw new IllegalArgumentException(
				"The string '" + text + "' must contain a '_' before its end");
		}
		char c = text.charAt(pos+1);
		return KeyEvent.getExtendedKeyCodeForChar(c);
	}		
	
	/**
	 * Removes all underscores from the text
	 * @param text
	 * @return 
	 */
	private static String removeMenuKeyIndicator(String text) {
		return text.replaceAll("_", "");
	}
	
	/** 
	 * An abstract editor action.
	 * Registers a 'ctrl
	 */
    public abstract class AbstractEditorAction extends AbstractAction 
		implements ChangeListener {

		/**
		 * Creates an action without an accelerator
		 * @param nameAndMnemonic a string like "_This example", where the
		 * 't' would be chosen as the mnemonic key, and the underscore would not
		 * be included in the actual menuitem name
		 */
	    public AbstractEditorAction(String name) {
            putValue(NAME, removeMenuKeyIndicator(name));			            
            putValue(MNEMONIC_KEY, getMenuKeyFromName(name));
		}
		
		/**
		 * Creates an action with an accelerator
		 * @param nameAndMnemonic a string like "_This example", where the
		 * 't' would be chosen as the mnemonic key, and the underscore would not
		 * be included in the actual menuitem name
		 * @param globalKey 
		 */
	    public AbstractEditorAction(String name,
				int globalKey, int globalModifiers) {
            putValue(NAME, removeMenuKeyIndicator(name));
            putValue(MNEMONIC_KEY, getMenuKeyFromName(name));
            putValue(ACCELERATOR_KEY, 
					KeyStroke.getKeyStroke(globalKey, platformMnemonic | globalModifiers));
        }
		
        @Override
        public void processChange(Object event) {
            // default is to do nothing
        }		 
	}
	
	protected void registerAction(AbstractEditorAction a) {
		String name = "" + a.getValue(AbstractAction.NAME);
		String desc = "" + a.getValue(AbstractAction.SHORT_DESCRIPTION);

		// build menu item
		EAdMenuItem item = new EAdMenuItem(desc);
		item.setAction(a);
		add(item);

		// register upstream
		controller.putAction(name, a);		
	}
}

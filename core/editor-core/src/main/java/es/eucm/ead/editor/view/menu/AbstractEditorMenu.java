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

package es.eucm.ead.editor.view.menu;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.KeyStroke;

import es.eucm.ead.editor.control.Controller;
import es.eucm.ead.editor.control.change.ChangeListener;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;

/**
 * An abstract editor menu. All editor menus should descend from this one
 * @author mfreire
 */
public abstract class AbstractEditorMenu extends JMenu {

	protected Controller controller;

	private static int platformMnemonic = Toolkit.getDefaultToolkit()
			.getMenuShortcutKeyMask();

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
		if (pos == -1 || pos + 1 == text.length()) {
			throw new IllegalArgumentException("The string '" + text
					+ "' must contain a '_' before its end");
		}
		char c = text.toLowerCase().charAt(pos + 1);
		return getKeyCodeForChar(c);
	}

	/**
	 * Java 1.6 does not support 1.7's KeyEvent.getExtendedKeyCodeForChar.
	 * This is a temporary replacement.
	 * @param c a char (ideally non-accented)
	 * @return the corresponding key-code
	 */
	private static int getKeyCodeForChar(char c) {
		char lc = Character.toLowerCase(c);
		char uc = Character.toUpperCase(c);
		switch (lc) {
		case '0':
			return KeyEvent.VK_0;
		case '1':
			return KeyEvent.VK_1;
		case '2':
			return KeyEvent.VK_2;
		case '3':
			return KeyEvent.VK_3;
		case '4':
			return KeyEvent.VK_4;
		case '5':
			return KeyEvent.VK_5;
		case '6':
			return KeyEvent.VK_6;
		case '7':
			return KeyEvent.VK_7;
		case '8':
			return KeyEvent.VK_8;
		case '9':
			return KeyEvent.VK_9;
		case 'a':
			return KeyEvent.VK_A;
		case 'b':
			return KeyEvent.VK_B;
		case 'c':
			return KeyEvent.VK_C;
		case 'd':
			return KeyEvent.VK_D;
		case 'e':
			return KeyEvent.VK_E;
		case 'f':
			return KeyEvent.VK_F;
		case 'g':
			return KeyEvent.VK_G;
		case 'h':
			return KeyEvent.VK_H;
		case 'i':
			return KeyEvent.VK_I;
		case 'j':
			return KeyEvent.VK_J;
		case 'k':
			return KeyEvent.VK_K;
		case 'l':
			return KeyEvent.VK_L;
		case 'm':
			return KeyEvent.VK_M;
		case 'n':
			return KeyEvent.VK_N;
		case 'o':
			return KeyEvent.VK_O;
		case 'p':
			return KeyEvent.VK_P;
		case 'q':
			return KeyEvent.VK_Q;
		case 'r':
			return KeyEvent.VK_R;
		case 's':
			return KeyEvent.VK_S;
		case 't':
			return KeyEvent.VK_T;
		case 'u':
			return KeyEvent.VK_U;
		case 'v':
			return KeyEvent.VK_V;
		case 'w':
			return KeyEvent.VK_W;
		case 'x':
			return KeyEvent.VK_X;
		case 'y':
			return KeyEvent.VK_Y;
		case 'z':
			return KeyEvent.VK_Z;

			// seems to work for most characters in 1.7's ExtendedKeyCodes
		default:
			return 0x01000000 + uc;
		}
	}

	/**
	 * Removes all underscores from the text
	 * @param text
	 * @return
	 */
	public static String removeMenuKeyIndicator(String text) {
		return text.replaceAll("_", "");
	}

	/**
	 * An abstract editor action.
	 * Registers shortcuts, mnemonics and icons
	 */
	public static abstract class AbstractEditorAction<E> extends AbstractAction
			implements ChangeListener<E> {

		/**
		 * Creates an action without an accelerator
		 * @param name a string like "_This example", where the
		 * 't' would be chosen as the mnemonic key, and the underscore would not
		 * be included in the actual menuitem name
		 */
		public AbstractEditorAction(String name) {
			putValue(NAME, removeMenuKeyIndicator(name));
			putValue(MNEMONIC_KEY, getMenuKeyFromName(name));
		}

		/**
		 * Creates an action with an accelerator
		 * @param name a string like "_This example", where the
		 * 't' would be chosen as the mnemonic key, and the underscore would not
		 * be included in the actual menuitem name
		 * @param globalKey
		 * @param globalModifiers
		 */
		public AbstractEditorAction(String name, int globalKey,
				int globalModifiers) {
			putValue(NAME, removeMenuKeyIndicator(name));
			putValue(MNEMONIC_KEY, getMenuKeyFromName(name));
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(globalKey,
					platformMnemonic | globalModifiers));
		}

		/**
		 * Creates an action with an accelerator and icon
		 */

		/**
		 * Detailed constructor, with accelerator, mnemonic and icon.
		 * @param name a string like "_This example", where the
		 * 't' would be chosen as the mnemonic key, and the underscore would not
		 * be included in the actual menuitem name
		 * @param globalKey
		 * @param globalModifiers
		 * @param iconUrl 
		 */
		public AbstractEditorAction(String name, int globalKey,
				int globalModifiers, String iconUrl) {
			putValue(NAME, removeMenuKeyIndicator(name));
			putValue(MNEMONIC_KEY, getMenuKeyFromName(name));
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(globalKey,
					platformMnemonic | globalModifiers));
			putValue(Action.SMALL_ICON, new ImageIcon(ClassLoader
					.getSystemClassLoader().getResource(iconUrl)));
		}

		@Override
		public void processChange(E event) {
			// default is to do nothing
		}
	}

	/**
	 * Registers an action with the menu. This creates a suitable entry,
	 * with key-bindings, shortcuts and everything.
	 * @param a The action to register.
	 */
	protected void registerAction(AbstractEditorAction a) {
		String name = "" + a.getValue(AbstractAction.NAME);
		String desc = "" + a.getValue(AbstractAction.SHORT_DESCRIPTION);

		// build menu item
		JMenuItem item = new JMenuItem(desc);
		item.setAction(a);
		add(item);

		// register upstream
		controller.putAction(name, a);
	}
}

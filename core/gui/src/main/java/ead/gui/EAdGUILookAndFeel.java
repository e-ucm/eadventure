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

/**
 * <e-Adventure> is an <e-UCM> research project.
 * <e-UCM>, Department of Software Engineering and Artificial Intelligence.
 * Faculty of Informatics, Complutense University of Madrid (Spain).
 * @author Del Blanco, A., Marchiori, E., Torrente, F.J.
 * @author Moreno-Ger, P. & Fern‡ndez-Manj—n, B. (directors)
 * @year 2009
 * Web-site: http://e-adventure.e-ucm.es
 */

/*
 Copyright (C) 2004-2009 <e-UCM> research group

 This file is part of <e-Adventure> project, an educational game & game-like 
 simulation authoring tool, availabe at http://e-adventure.e-ucm.es. 

 <e-Adventure> is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 <e-Adventure> is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with <e-Adventure>; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

 */
package ead.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

import javax.swing.JPopupMenu;
import javax.swing.LookAndFeel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.basic.BasicLookAndFeel;

public class EAdGUILookAndFeel extends BasicLookAndFeel {

	private static final long serialVersionUID = 5492387410718621734L;

	private static EAdGUILookAndFeel instance;

	private Font font;

	private Font boldFont;

	private Color backgroundColor = Color.WHITE;

	private Color forgroundColor = Color.BLACK;

	private Color disabledColor = new Color(240, 240, 240);

	private ColorUIResource focusColor = new ColorUIResource(Color.BLUE);

	static {
		instance = new EAdGUILookAndFeel();

		try {
			InputStream is = ClassLoader
					//.getSystemResourceAsStream("DroidSans.ttf");
					//.getSystemResourceAsStream("KunKhmer.ttf");
					//.getSystemResourceAsStream("WireWyrm.otf");
					.getSystemResourceAsStream("roboto/Roboto-Regular.ttf");
			instance.font = Font.createFont(Font.TRUETYPE_FONT, is);
			instance.font = instance.font.deriveFont(12.0f);

			is = ClassLoader
					//.getSystemResourceAsStream("DroidSans-Bold.ttf");
					.getSystemResourceAsStream("roboto/Roboto-Bold.ttf");
			instance.boldFont = Font.createFont(Font.TRUETYPE_FONT, is);
			instance.boldFont = instance.boldFont.deriveFont(12.0f);
			
			UIManager.put("javax.swing.plaf.FontUIResource",
					new FontUIResource(instance.font));

			System.setProperty("awt.useSystemAAFontSettings", "on");
			
			UIManager
					.setLookAndFeel(instance);
			instance.setConstants();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public UIDefaults getDefaults() {
		UIDefaults table = super.getDefaults();

		FontUIResource fontResource = new FontUIResource(instance.font);
		Enumeration<Object> newKeys = table.keys();

		while (newKeys.hasMoreElements()) {
			Object key = newKeys.nextElement();
			if (table.get(key) instanceof Font
					|| table.get(key) instanceof FontUIResource) {
				table.put(key, fontResource);
			}
		}

		return table;
	}

	private void setConstants() {
		System.setProperty("com.apple.macos.useScreenMenuBar", "true");
		System.setProperty("apple.laf.useScreenMenuBar", "true");

		JPopupMenu.setDefaultLightWeightPopupEnabled(true);

		try {
			UIManager.put("TabbedPane.contentAreaColor", backgroundColor);
			UIManager.put("TabbedPane.light", backgroundColor);
			UIManager.put("TabbedPane.highlight", backgroundColor);
			UIManager.put("TabbedPane.shadow", backgroundColor);
			UIManager.put("TabbedPane.darkShadow", backgroundColor);
			UIManager.put("TabbedPane.focus", backgroundColor);
			UIManager.put("ComboBox.disabledBackground", backgroundColor);

			UIManager.put("Panel.background", backgroundColor);
			UIManager.put("MenuBar.background", backgroundColor);
			UIManager.put("Menu.background", backgroundColor);
			UIManager.put("MenuItem.background", backgroundColor);
			UIManager.put("CheckBoxMenuItem.background", backgroundColor);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void initClassDefaults(UIDefaults table) {
		super.initClassDefaults(table);
		final String basicPackageName = "javax.swing.plaf.basic.";
		final String eadPackageName = "ead.gui.extra.";

		Object[] uiDefaults = { "ButtonUI", eadPackageName + "EAdButtonUI",
				"CheckBoxUI", eadPackageName + "EAdCheckBoxUI", "ComboBoxUI",
				eadPackageName + "EAdComboBoxUI", "DesktopIconUI",
				basicPackageName + "BasicDesktopIconUI", "FileChooserUI",
				basicPackageName + "BasicFileChooserUI", "InternalFrameUI",
				basicPackageName + "BasicInternalFrameUI", "LabelUI",
				basicPackageName + "BasicLabelUI", "PopupMenuSeparatorUI",
				basicPackageName + "BasicPopupMenuSeparatorUI",
				"ProgressBarUI", basicPackageName + "BasicProgressBarUI",
				"RadioButtonUI", eadPackageName + "EAdRadioButtonUI",
				"ScrollBarUI", eadPackageName + "EAdScrollBarUI",
				"ScrollPaneUI", basicPackageName + "BasicScrollPaneUI",
				"SeparatorUI", basicPackageName + "BasicSeparatorUI",
				"SliderUI", basicPackageName + "BasicSliderUI", "SplitPaneUI",
				eadPackageName + "EAdSplitPaneUI", "TabbedPaneUI",
				eadPackageName + "EAdTabbedPaneUI", "TextFieldUI",
				basicPackageName + "BasicTextFieldUI", "ToggleButtonUI",
				eadPackageName + "EAdToggleButtonUI", "ToolBarUI",
				basicPackageName + "BasicToolBarUI", "ToolTipUI",
				basicPackageName + "BasicToolTipUI", "TreeUI",
				basicPackageName + "BasicTreeUI", "RootPaneUI",
				basicPackageName + "BasicRootPaneUI", "SpinnerUI", eadPackageName + "EAdSpinnerUI"};

		table.putDefaults(uiDefaults);
	}

	public static Font getFont() {
		return instance.font;
	}

	public static Font getBoldFont() {
		return instance.boldFont;
	}

	/**
	 * @return the backgroundColor
	 */
	public static Color getBackgroundColor() {
		return instance.backgroundColor;
	}

	/**
	 * @param backgroundColor
	 *            the backgroundColor to set
	 */
	public static void setBackgroundColor(Color backgroundColor) {
		instance.backgroundColor = backgroundColor;
		instance.setConstants();
	}

	/**
	 * @return the forgroundColor
	 */
	public static Color getForegroundColor() {
		return instance.forgroundColor;
	}

	/**
	 * @return the disabledColor
	 */
	public static Color getDisabledColor() {
		return instance.disabledColor;
	}

	public static ColorUIResource getFocusColor() {
		return instance.focusColor;
	}

	@Override
	public String getDescription() {
		return "EAd GUI Look and Feel";
	}

	@Override
	public String getID() {
		return "EAdGUILookAndFeel";
	}

	@Override
	public String getName() {
		return "EAdGUILookAndFeel";
	}

	@Override
	public boolean isNativeLookAndFeel() {
		return false;
	}

	@Override
	public boolean isSupportedLookAndFeel() {
		return true;
	}

	public static LookAndFeel getInstance() {
		return instance;
	}

}

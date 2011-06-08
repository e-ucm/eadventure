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

package es.eucm.eadventure.editor.view.impl;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.editor.R;
import es.eucm.eadventure.editor.view.EditorWindow;
import es.eucm.eadventure.editor.view.ToolPanel;
import es.eucm.eadventure.editor.view.menu.EditorMenuBar;
import es.eucm.eadventure.gui.EAdFrame;
import es.eucm.eadventure.gui.EAdHideingSplitPane;
import es.eucm.eadventure.gui.EAdPanel;
import es.eucm.eadventure.utils.swing.SwingUtilities;

/**
 * Default implementation of the main editor window
 */
@Singleton
public class EditorWindowImpl implements EditorWindow {

	/**
	 * Logger
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(EditorWindowImpl.class);

	/**
	 * Main right panel in the editor
	 */
	private JPanel rightPanel;

	/**
	 * Main left panel in the editor
	 */
	private EAdPanel leftPanel;

	/**
	 * The main panel where elements are edited
	 */
	private JPanel mainPanel;

	/**
	 * The top right panel, where the title of the element being edited and the
	 * tools are displayed
	 */
	private JPanel titlePanel;

	/**
	 * Tool panel of the editor
	 */
	private ToolPanel toolPanel;

	/**
	 * The menu bar for the editor
	 */
	private EditorMenuBar editorMenuBar;

	/**
	 * Editor window
	 */
	protected EAdFrame editorWindow;

	@Inject
	public EditorWindowImpl(ToolPanel toolPanel, EditorMenuBar editorMenuBar) {
		this.toolPanel = toolPanel;
		this.editorMenuBar = editorMenuBar;
	}

	@Override
	public void initialize() {
		
		setIcons();
		createRightPanel();
		createLeftPanel();
		createMainWindow();
	}

	@Override
	public void showWindow() {
		setSizeAndPosition();
		SwingUtilities.doInEDT(new Runnable() {
			@Override
			public void run() {
				editorWindow.setVisible(true);
			}
		});
	}

	@Override
	public void addModalPanel(JPanel modalPanel) {
		editorWindow.addModalPanel(modalPanel);
	}

	@Override
	public void removeModalPanel() {
		editorWindow.removeModalPanel();
	}

	/**
	 * Create the right editor panel
	 */
	private void createRightPanel() {
		rightPanel = new JPanel();
		rightPanel.setLayout(new BorderLayout());

		mainPanel = new JPanel();
		rightPanel.add(mainPanel, BorderLayout.CENTER);

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		titlePanel = new JPanel();
		topPanel.add(titlePanel, BorderLayout.CENTER);
		topPanel.add(toolPanel.getPanel(), BorderLayout.EAST);

		rightPanel.add(topPanel, BorderLayout.NORTH);
	}

	/**
	 * Create the left editor panel
	 */
	private void createLeftPanel() {
		leftPanel = new EAdPanel();

		leftPanel.setLayout(new BorderLayout());
	}

	/**
	 * Create the main window of the editor
	 */
	private void createMainWindow() {
		editorWindow = new EAdFrame();
		editorWindow.setTitle("eAdventure");

		EAdHideingSplitPane splitPane = new EAdHideingSplitPane(leftPanel,
				rightPanel);
		editorWindow.add(splitPane);

		JMenuBar menuBar = editorMenuBar.getMenuBar();
		editorWindow.setJMenuBar(menuBar);

		SwingUtilities.doInEDTNow(new Runnable() {

			@Override
			public void run() {
				editorWindow.setVisible(false);
			}

		});
	}

	/**
	 * Set the icons of the applications.
	 * 
	 * NOTE: This method does not work in Mac, where applications can no change
	 * the icon programmatically. An application bundle must be used, see:
	 * http://developer.apple.com/library/mac/#documentation/Java/Conceptual/
	 * Java14Development/03-JavaDeployment/JavaDeployment.html
	 */
	private void setIcons() {
		SwingUtilities.doInEDTNow(new Runnable() {
			@Override
			public void run() {
				List<Image> icons = new ArrayList<Image>();

				try {
					icons.add(ImageIO.read(ClassLoader
							.getSystemResourceAsStream(R.Drawable.EditorIcon16x16_png)));
					icons.add(ImageIO.read(ClassLoader
							.getSystemResourceAsStream(R.Drawable.EditorIcon32x32_png)));
					icons.add(ImageIO.read(ClassLoader
							.getSystemResourceAsStream(R.Drawable.EditorIcon64x64_png)));
					icons.add(ImageIO.read(ClassLoader
							.getSystemResourceAsStream(R.Drawable.EditorIcon128x128_png)));
					editorWindow.setIconImages(icons);
				} catch (Exception e) {
					logger.error("Could not load icons correctly");
				}

			}
		});
	}

	/**
	 * Set the size and position of the editor window
	 */
	private void setSizeAndPosition() {
		SwingUtilities.doInEDTNow(new Runnable() {
			@Override
			public void run() {
				editorWindow.setMinimumSize(new Dimension(640, 400));
				Dimension screenSize = Toolkit.getDefaultToolkit()
						.getScreenSize();
				int width = (int) screenSize.getWidth();
				int height = (int) screenSize.getHeight();
				editorWindow.setSize(width, height);
				editorWindow.setLocation((screenSize.width - width) / 2,
						(screenSize.height - height) / 2);
			}
		});
	}

	@Override
	public JPanel getRightPanel() {
		return rightPanel;
	}

	@Override
	public EAdPanel getLeftPanel() {
		return leftPanel;
	}

	@Override
	public JPanel getMainPanel() {
		return mainPanel;
	}

	@Override
	public JPanel getTitlePanel() {
		return titlePanel;
	}

	@Override
	public ToolPanel getToolPanel() {
		return toolPanel;
	}

	@Override
	public EditorMenuBar getEditorMenuBar() {
		return editorMenuBar;
	}

}

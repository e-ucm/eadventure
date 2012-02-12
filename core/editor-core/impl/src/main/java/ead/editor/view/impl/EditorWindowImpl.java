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

import bibliothek.gui.DockController;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import ead.editor.R;
import ead.editor.view.EditorWindow;
import ead.editor.view.ToolPanel;
import ead.editor.view.menu.EditorMenuBar;
import ead.gui.EAdFrame;
import ead.gui.EAdHideingSplitPane;
import ead.utils.i18n.Resource;
import ead.utils.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of the main editor window
 */
@Singleton
public class EditorWindowImpl implements EditorWindow {

	/**
	 * Logger
	 */
	private static final Logger logger = LoggerFactory
			.getLogger("EditorWindowImpl");

	/**
	 * Main right panel in the editor
	 */
	private JPanel rightPanel;

	/**
	 * Main left panel in the editor
	 */
	private JPanel leftPanel;

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

    /**
     * Dock controller
     */
    protected DockController controller;

	@Inject
	public EditorWindowImpl(ToolPanel toolPanel, EditorMenuBar editorMenuBar) {
		this.toolPanel = toolPanel;
		this.editorMenuBar = editorMenuBar;
	}

	@Override
	public void initialize() {

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

        leftPanel = new JPanel();
		leftPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		leftPanel.setLayout(new BorderLayout());

        // requires left and right panel; builds a splitPane
		createMainWindow();
        // requires main window
		setIcons();

        controller = new DockController();
        controller.setRootWindow(editorWindow);
        // FIXME: may want to call setup auto-calling of controller.kill() on close
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
	 * Create the main window of the editor
	 */
	private void createMainWindow() {
		editorWindow = new EAdFrame();
		editorWindow.setTitle("eAdventure Editor");

		EAdHideingSplitPane splitPane =
                new EAdHideingSplitPane(leftPanel, rightPanel);
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
                icons.add(Resource.loadImage(R.Drawable.EditorIcon16x16_png));
                icons.add(Resource.loadImage(R.Drawable.EditorIcon32x32_png));
                icons.add(Resource.loadImage(R.Drawable.EditorIcon64x64_png));
                icons.add(Resource.loadImage(R.Drawable.EditorIcon128x128_png));
                editorWindow.setIconImages(icons);
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
				int width = (int) (screenSize.getWidth() * .8f);
				int height = (int) (screenSize.getHeight() * .8f);
                logger.info("Setting size to {}x{}", new Object[]{width, height});
				editorWindow.setSize(width, height);
				editorWindow.setLocation((screenSize.width - width) / 2,
						(screenSize.height - height) / 2);
                editorWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
		});
	}

	@Override
	public JPanel getRightPanel() {
		return rightPanel;
	}

	@Override
	public JPanel getLeftPanel() {
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

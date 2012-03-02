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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bibliothek.gui.DockController;
import bibliothek.gui.DockStation;
import bibliothek.gui.Dockable;
import bibliothek.gui.dock.DefaultDockable;
import bibliothek.gui.dock.SplitDockStation;
import bibliothek.gui.dock.StackDockStation;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.editor.R;
import ead.editor.view.EditorWindow;
import ead.editor.view.ToolPanel;
import ead.editor.view.menu.EditorMenuBar;
import ead.gui.EAdFrame;
import ead.gui.EAdHidingSplitPane;
import ead.utils.i18n.Resource;
import ead.utils.swing.SwingUtilities;

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
	 * Main right panel in the editor; contains main editing area.
	 */
	private SplitDockStation rightPanel;

	/**
	 * Main left panel in the editor; contains list-of-tools
	 */
	private JPanel leftPanel;

	/**
	 * The main panel where elements are edited; split into right&left
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
	 * Root views
	 */
	private TreeMap<String, DockStation> rootViews
		= new TreeMap<String, DockStation>();
	
	/**
	 * The menu bar for the editor
	 */
	private EditorMenuBar editorMenuBar;

	/**
	 * Editor window where everything will be placed; has support for blurryness
	 * effect to denote dialog-modality.
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

		rightPanel = new SplitDockStation();
		titlePanel = new JPanel();
		editorMenuBar.getMenuBar().add(new JSeparator());
		editorMenuBar.getMenuBar().add(toolPanel.getPanel());

        leftPanel = new JPanel();
		leftPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		leftPanel.setLayout(new BorderLayout());
		leftPanel.setOpaque(true);
		leftPanel.setBackground(leftPanel.getBackground().darker());

        // requires left and right panel; builds an animated splitPane
		createMainWindow();
        // requires main window
		setIcons();

        controller = new DockController();
        controller.setRootWindow(editorWindow);
        controller.add(rightPanel);
        // FIXME: may want to call setup auto-calling of controller.kill() on close
        
        addView("red", "1", coloredPanel(Color.red), true);
        addView("red", "2", coloredPanel(Color.orange), true);
        addView("blue", "3", coloredPanel(Color.blue), true);
        addView("blue", "4", coloredPanel(Color.cyan), true);
    }

	private static JPanel coloredPanel(Color color){
		JPanel panel = new JPanel();
		panel.setOpaque( true );
		panel.setBackground( color );
		return panel;
	}
	
	/**
	 * Creates a new view of a given category.
	 */
	private Dockable createNewView(String type) {
        logger.info("Creating new stack for type {}", type);
        StackDockStation dockStation = new StackDockStation();
		JPanel panel = new JPanel();
		panel.setOpaque( true );
		panel.setBackground(Color.white);
		panel.add(new JLabel("This would be a list of " + type), BorderLayout.NORTH);
		//panel.add(dockStation, BorderLayout.CENTER);
		DefaultDockable dockable = new DefaultDockable(panel);
		dockable.setTitleText(type);
		rightPanel.drop(dockable);
		// TODO: i18n, & also use an icon
		return dockStation;
	}

	@Override
	public void addView(String type, String elementId, JPanel view, boolean reuseExisting) {
		Dockable destination = null;		
//		if ( ! rootViews.containsKey(type)) {
//			rootViews.put(type, createNewView(type));
//		} else {
//			destination = rootViews.get(type);
//			// FIXME: need to check for existence in a more robust way
//			if (! destination.isDockableVisible()) {
//				rootViews.put(type, createNewView(type));
//			}
//		}
		//destination = rootViews.get(type);
		DefaultDockable dockable = new DefaultDockable(view);
		dockable.setTitleText(elementId);
//
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

		EAdHidingSplitPane splitPane =
                new EAdHidingSplitPane(leftPanel, rightPanel);
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

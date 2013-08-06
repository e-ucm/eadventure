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

package es.eucm.ead.editor.view;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;

import es.eucm.ead.editor.R;
import es.eucm.ead.editor.control.Controller;
import es.eucm.ead.editor.control.EditorConfig;
import es.eucm.ead.editor.model.nodes.CharacterNode;
import es.eucm.ead.editor.model.nodes.DependencyNode;
import es.eucm.ead.editor.model.nodes.asset.AssetsNode;
import es.eucm.ead.editor.model.nodes.asset.CaptionAssetNode;
import es.eucm.ead.editor.model.nodes.asset.ImageAssetNode;
import es.eucm.ead.editor.view.dock.ClassDockableFactory;
import es.eucm.ead.editor.view.dock.ElementPanel;
import es.eucm.ead.editor.view.menu.EditMenu;
import es.eucm.ead.editor.view.menu.FileMenu;
import es.eucm.ead.editor.view.menu.RunMenu;
import es.eucm.ead.editor.view.menu.WindowMenu;
import es.eucm.ead.editor.view.panel.ActorPanel;
import es.eucm.ead.editor.view.panel.RawElementPanel;
import es.eucm.ead.editor.view.panel.asset.AssetsPanel;
import es.eucm.ead.editor.view.panel.asset.CaptionAssetPanel;
import es.eucm.ead.editor.view.panel.asset.ImageAssetPanel;
import es.eucm.ead.editor.view.structure.StructureElement;
import es.eucm.ead.editor.view.structure.StructurePanel;
import es.eucm.ead.editor.view.toolbar.ToolPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

import bibliothek.gui.DockStation;
import bibliothek.gui.Dockable;
import bibliothek.gui.dock.common.CControl;
import bibliothek.gui.dock.common.DefaultMultipleCDockable;
import bibliothek.gui.dock.common.MultipleCDockable;
import bibliothek.gui.dock.common.MultipleCDockableFactory;
import bibliothek.gui.dock.common.intern.CDockable;
import bibliothek.gui.dock.common.intern.CommonDockable;
import bibliothek.gui.dock.common.mode.ExtendedMode;

import es.eucm.ead.editor.control.ViewController;
import es.eucm.ead.editor.view.menu.AbstractEditorMenu;
import es.eucm.ead.tools.java.utils.i18n.Resource;
import es.eucm.ead.tools.java.utils.swing.SwingUtilities;

/**
 * Default implementation of the main editor window
 */
@Singleton
public class EditorWindow implements ViewController {

	/**
	 * Logger
	 */
	private static final Logger logger = LoggerFactory.getLogger("EWindowImpl");
	/**
	 * Main left panel in the editor; contains list-of-tools
	 */
	private StructurePanel leftPanel;

	/**
	 * edit menu
	 */
	private ToolPanel toolPanel;
	/**
	 * The menu bar for the editor
	 */
	private JMenuBar editorMenuBar;
	/**
	 * Editor window where everything will be placed
	 */
	protected JFrame editorWindow;
	/**
	 * Dock controller for content views; takes up rightPanel
	 */
	protected CControl dockController;
	/**
	 * Model controller
	 */
	protected Controller controller;

	private void initializeStructurePanel() {
		for (Action a : controller.getActions()) {
			if (a instanceof EditMenu.EditorAction) {
				leftPanel.addElement(new StructureElement(a));
			}
		}
		leftPanel.commit();

		// register panel to listen to changes in the project
		leftPanel.setController(controller);
		controller.getProjectController().addChangeListener(leftPanel);
		leftPanel.processChange(null);
	}

	/**
	 * Registers a panel factory associated to a node-type
	 * @param nodeType
	 * @param panelType
	 */
	public void registerElementPanelFactory(
			Class<? extends DependencyNode> nodeType,
			Class<? extends ElementPanel> panelType) {
		dockController.addMultipleDockableFactory(nodeType.getName(),
				new ClassDockableFactory(panelType, nodeType, controller));
	}

	@Override
	public void restoreViews() {
		File f = controller.getModel().getLoader().relativeFile("views.xml");
		if (f.exists() && f.canRead() && f.length() > 0) {
			try {
				dockController.readXML(f);
			} catch (Exception ex) {
				logger.error("Could not restore views from {}", f, ex);
				clearViews();
			}
		}
	}

	@Override
	public void clearViews() {
		while (dockController.getCDockableCount() > 0) {
			CDockable c = dockController.getCDockable(0);
			// We need to access the Core API to find out which other Dockables exist.
			DockStation parent = c.intern().getDockParent();

			// Closing a Dockable may remove the parent DockStation;
			// first save a list, then delete it
			Dockable[] children = new Dockable[parent.getDockableCount()];
			for (int i = 0; i < children.length; i++) {
				children[i] = parent.getDockable(i);
			}
			for (Dockable child : children) {
				// Avoids closing stacks
				if (child instanceof CommonDockable) {
					CDockable cChild = ((CommonDockable) child).getDockable();
					cChild.setVisible(false);
				}
			}
			logger.debug("Cleared one round");
		}
		File f = controller.getModel().getLoader().relativeFile("views.xml");
		f.delete();
		logger.info("Cleared views");
	}

	@Override
	public void saveViews() {
		File f = controller.getModel().getLoader().relativeFile("views.xml");
		try {
			dockController.writeXML(f);
		} catch (IOException ex) {
			logger.error("Could not save views into {}", f, ex);
		}
	}

	/**
	 * Sets a title qualifier, used to display (say) currently-edited file.
	 * @param title
	 */
	@Override
	public void setTitleQualifier(String title) {
		String nextTitle = title + " - " + Messages.main_window_title;
		editorWindow.setTitle(nextTitle);
	}

	/**
	 * Creates a new view of a given category.
	 */
	private void createNewView(String id) {
		logger.info("opening view for #{}...", id);
		if (dockController.getMultipleDockable(id) != null) {
			MultipleCDockable d = dockController.getMultipleDockable(id);
			d.setVisible(true);
			d.setExtendedMode(ExtendedMode.MAXIMIZED);
			logger.info("Expect dockable {} to be visible now!", id);
		} else {
			DependencyNode node = controller.getModel().getElement(id);

			ClassDockableFactory f = null;
			Class parentClass = node.getClass();
			while (f == null) {
				f = (ClassDockableFactory) dockController
						.getMultipleDockableFactory(parentClass.getName());
				parentClass = parentClass.getSuperclass();
			}
			DefaultMultipleCDockable d = f.createDockable(id);
			dockController.addDockable(id, d);
			setLocationToRelativesIfPossible(d);
			d.setVisible(true);
		}
	}

	@Override
	public void addView(String type, String elementId, boolean reuseExisting) {
		createNewView(elementId);
	}

	@Override
	public void showWindow() {
		SwingUtilities.doInEDT(new Runnable() {

			@Override
			public void run() {
				editorWindow.setVisible(true);
				setSizeAndPosition();
			}
		});
	}

	/**
	 * Current modal dialog, if any
	 */
	private JFrame currentModal = null;

	@Override
	public void addModalPanel(JPanel modalPanel) {
		if (currentModal != null) {
			removeModalPanel(true);
		}
		logger
				.info(
						"addModal: received a panel with {} components, and size {}x{}; isEDT = {}",
						new Object[] {
								modalPanel.getComponentCount(),
								modalPanel.getPreferredSize().width,
								modalPanel.getPreferredSize().height,
								javax.swing.SwingUtilities
										.isEventDispatchThread() });
		currentModal = new JFrame();
		currentModal.setUndecorated(true);
		currentModal.add(modalPanel);
		currentModal.setLocationRelativeTo(editorWindow);
		currentModal.setAlwaysOnTop(true);
		currentModal.pack();
		currentModal.setVisible(true);
	}

	@Override
	public void removeModalPanel(boolean cancelChanges) {
		currentModal.setVisible(false);
		currentModal.dispose();
		currentModal = null;
	}

	/**
	 * Create the main window of the editor
	 */
	private void createMainWindow() {
		editorWindow.setTitle(Messages.main_window_title);
		editorWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				leftPanel, dockController.getContentArea());
		editorWindow.add(splitPane);

		// initialize the menus
		editorMenuBar = new JMenuBar();
		AbstractEditorMenu menus[] = new AbstractEditorMenu[] {
				new FileMenu(controller), new EditMenu(controller),
				new RunMenu(controller), new WindowMenu(controller), };
		for (AbstractEditorMenu m : menus) {
			m.initialize();
			editorMenuBar.add(m);
		}
		editorWindow.setJMenuBar(editorMenuBar);

		toolPanel = new ToolPanel(controller);
		editorMenuBar.add(new JSeparator());
		editorMenuBar.add(toolPanel.getPanel());

		SwingUtilities.doInEDTNow(new Runnable() {

			@Override
			public void run() {
				editorWindow.setVisible(false);
			}
		});
	}

	private void setLocationToRelativesIfPossible(MultipleCDockable d) {
		MultipleCDockableFactory<?, ?> f = d.getFactory();
		for (int i = 0; i < dockController.getCDockableCount(); i++) {
			CDockable c = dockController.getCDockable(i);
			if ((c instanceof MultipleCDockable)
					&& ((MultipleCDockable) c).getFactory() == f) {
				d.setLocation(c.getBaseLocation());
				break;
			}
		}
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
		EditorConfig ec = controller.getConfig();
		final int width = ec
				.containsKey(EditorConfig.EditorConf.WindowSizeWidth) ? ec
				.getInt(EditorConfig.EditorConf.WindowSizeWidth) : -1;
		final int height = ec
				.containsKey(EditorConfig.EditorConf.WindowSizeHeight) ? ec
				.getInt(EditorConfig.EditorConf.WindowSizeHeight) : -1;
		final int xPos = ec.containsKey(EditorConfig.EditorConf.WindowPosX) ? ec
				.getInt(EditorConfig.EditorConf.WindowPosX)
				: -1;
		final int yPos = ec.containsKey(EditorConfig.EditorConf.WindowPosY) ? ec
				.getInt(EditorConfig.EditorConf.WindowPosY)
				: -1;

		SwingUtilities.doInEDTNow(new Runnable() {

			@Override
			public void run() {
				editorWindow.setMinimumSize(new Dimension(640, 400));
				Dimension screenSize = Toolkit.getDefaultToolkit()
						.getScreenSize();
				int w = width < 0 ? (int) (screenSize.width * .8f) : width;
				int h = height < 0 ? (int) (screenSize.height * .8f) : height;
				int x = xPos < 0 ? (screenSize.width - w) / 2 : xPos;
				int y = yPos < 0 ? (screenSize.height - h) / 2 : yPos;

				editorWindow.setSize(w, h);
				editorWindow.setLocation(x, y);

				logger.info("Saved window settings {},{} {}x{}", new Object[] {
						x, y, w, h });

				editorWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				editorWindow.addComponentListener(new ComponentAdapter() {
					@Override
					public void componentResized(ComponentEvent e) {
						saveWindowPreferences();
					}

					@Override
					public void componentMoved(ComponentEvent e) {
						saveWindowPreferences();
					}
				});
			}
		});
	}

	private void saveWindowPreferences() {
		EditorConfig ec = controller.getConfig();
		ec.put(EditorConfig.EditorConf.WindowSizeWidth, ""
				+ editorWindow.getWidth());
		ec.put(EditorConfig.EditorConf.WindowSizeHeight, ""
				+ editorWindow.getHeight());
		ec.put(EditorConfig.EditorConf.WindowPosX, ""
				+ editorWindow.getLocationOnScreen().x);
		ec.put(EditorConfig.EditorConf.WindowPosY, ""
				+ editorWindow.getLocationOnScreen().y);
		logger.debug("Saved window settings {},{} {}x{}", new Object[] {
				ec.getInt(EditorConfig.EditorConf.WindowSizeWidth),
				ec.getInt(EditorConfig.EditorConf.WindowSizeHeight),
				ec.getInt(EditorConfig.EditorConf.WindowPosX),
				ec.getInt(EditorConfig.EditorConf.WindowPosY) });
		ec.save(null);
	}

	/**
	 * Set the actual super-controller.
	 * This will finish building the interface, and then launch it.
	 *
	 * @param controller the main controller, providing access to model, views,
	 * and more
	 */
	@Override
	public void setController(Controller controller) {
		this.controller = controller;

		// left panel, right panel, and the splitPane
		leftPanel = new StructurePanel();
		editorWindow = new JFrame();
		dockController = new CControl(editorWindow);
		createMainWindow();
		initializeStructurePanel();

		// requires main window
		setIcons();

		// Add your panel factories here
		registerElementPanelFactory(CharacterNode.class, ActorPanel.class);
		registerElementPanelFactory(DependencyNode.class, RawElementPanel.class);
		registerElementPanelFactory(ImageAssetNode.class, ImageAssetPanel.class);
		registerElementPanelFactory(CaptionAssetNode.class,
				CaptionAssetPanel.class);
		registerElementPanelFactory(AssetsNode.class, AssetsPanel.class);
	}
}

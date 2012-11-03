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

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bibliothek.gui.dock.common.CControl;
import bibliothek.gui.dock.common.DefaultMultipleCDockable;
import bibliothek.gui.dock.common.MultipleCDockable;
import bibliothek.gui.dock.common.MultipleCDockableFactory;
import bibliothek.gui.dock.common.intern.CDockable;

import com.google.inject.Singleton;

import ead.editor.R;
import ead.editor.control.Controller;
import ead.editor.control.EditorConfig;
import ead.editor.control.EditorConfig.EditorConf;
import ead.editor.control.ViewController;
import ead.editor.model.nodes.ActorNode;
import ead.editor.model.nodes.DependencyNode;
import ead.editor.view.dock.ClassDockableFactory;
import ead.editor.view.dock.ElementPanel;
import ead.editor.view.menu.FileMenu;
import ead.editor.view.panel.ActorPanel;
import ead.editor.view.panel.RawElementPanel;
import ead.gui.structurepanel.StructureElement;
import ead.gui.structurepanel.StructureElementProvider;
import ead.gui.structurepanel.StructurePanel;
import ead.utils.i18n.Resource;
import ead.utils.swing.SwingUtilities;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.*;

import ead.editor.view.menu.AbstractEditorMenu;
import ead.editor.view.menu.EditMenu;
import ead.editor.view.menu.RunMenu;

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
        leftPanel = new StructurePanel();

        leftPanel.addElement(new StructureElement(
				new SimpleStructureElement("Scenes",
                R.Drawable.sidePanel__scenes_png)));
		leftPanel.addElement(new StructureElement(
				new SimpleStructureElement("Player",
                R.Drawable.sidePanel__player_png)));
		leftPanel.addElement(new StructureElement(
				new SimpleStructureElement("NPCs",
                R.Drawable.sidePanel__npcs_png)));
		leftPanel.addElement(new StructureElement(
				new SimpleStructureElement("Conversations",
                R.Drawable.sidePanel__conversations_png)));
		leftPanel.addElement(new StructureElement(
				new SimpleStructureElement("Items",
                R.Drawable.sidePanel__items_png)));
		leftPanel.addElement(new StructureElement(
				new SimpleStructureElement("Atrezzo",
                R.Drawable.sidePanel__atrezzo_png)));
		leftPanel.addElement(new StructureElement(
				new SimpleStructureElement("Books",
                R.Drawable.sidePanel__books_png)));
		leftPanel.addElement(new StructureElement(
				new SimpleStructureElement("Advanced",
                R.Drawable.sidePanel__advanced_png)));
		leftPanel.addElement(new StructureElement(
				new SimpleStructureElement("Cutscenes",
                R.Drawable.sidePanel__cutscenes_png)));
		leftPanel.addElement(new StructureElement(
				new SimpleStructureElement("Adaptation Profiles",
                R.Drawable.sidePanel__adaptationProfiles_png)));
		leftPanel.addElement(new StructureElement(
				new SimpleStructureElement("Assessment Profiles",
                R.Drawable.sidePanel__assessmentProfiles_png)));
		leftPanel.createElements();
    }

    /**
     * Registers a panel factory associated to a node-type
     * @param nodeType
     * @param panelType
     */
    public void registerElementPanelFactory(Class<? extends DependencyNode> nodeType,
            Class<? extends ElementPanel> panelType) {
        dockController.addMultipleDockableFactory(nodeType.getName(),
            new ClassDockableFactory(panelType, nodeType, controller));
    }

	@Override
	public void restoreViews() {
		File f = controller.getModel().relativeFile("views.xml");
		if (f.exists() && f.canRead()) {
			try {
				dockController.readXML(f);
			} catch (IOException ex) {
				logger.error("Could not restore views from {}", f, ex);
			}
		}
	}

	@Override
	public void clearViews() {
		for (int i=0; i<dockController.getCDockableCount(); i++) {
			CDockable c = dockController.getCDockable(i);
			dockController.remove((MultipleCDockable)c);
		}
	}

	@Override
	public void saveViews() {
		File f = controller.getModel().relativeFile("views.xml");
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
            logger.info("Learn how to make visible here!");
        } else {
            DependencyNode node = controller.getModel().getElement(id);

            ClassDockableFactory f = null;
            Class parentClass = node.getClass();
            while (f == null) {
                f = (ClassDockableFactory)
                    dockController.getMultipleDockableFactory(parentClass.getName());
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
        setSizeAndPosition();
        SwingUtilities.doInEDT(new Runnable() {

            @Override
            public void run() {
                editorWindow.setVisible(true);
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
        logger.info("addModal: received a panel with {} components, and size {}x{}; isEDT = {}",
                new Object[]{modalPanel.getComponentCount(),
                    modalPanel.getPreferredSize().width, modalPanel.getPreferredSize().height,
                    javax.swing.SwingUtilities.isEventDispatchThread()});
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
        editorWindow = new JFrame();
        editorWindow.setTitle(Messages.main_window_title);
        editorWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                leftPanel, dockController.getContentArea());
        editorWindow.add(splitPane);

		// initialize the menus
        editorMenuBar = new JMenuBar();
		AbstractEditorMenu menus[] = new AbstractEditorMenu[] {
			new FileMenu(controller), 
			new EditMenu(controller),
			new RunMenu(controller)
		};
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
        final int width = ec.containsKey(EditorConf.WindowSizeWidth) ?
                ec.getInt(EditorConf.WindowSizeWidth) : -1;
        final int height = ec.containsKey(EditorConf.WindowSizeHeight) ?
                ec.getInt(EditorConf.WindowSizeHeight) : -1;
        final int xPos = ec.containsKey(EditorConf.WindowPosX) ?
                ec.getInt(EditorConf.WindowPosX) : -1;
        final int yPos = ec.containsKey(EditorConf.WindowPosY) ?
                ec.getInt(EditorConf.WindowPosY) : -1;

        SwingUtilities.doInEDTNow(new Runnable() {

            @Override
            public void run() {
                editorWindow.setMinimumSize(new Dimension(640, 400));
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                int w = width<0 ? (int) (screenSize.width * .8f) : width;
                int h = height<0 ? (int) (screenSize.height * .8f) : height;
                int x = xPos<0 ? (screenSize.width - w) / 2 : xPos;
                int y = yPos<0 ? (screenSize.height - h) / 2 : yPos;

                editorWindow.setSize(w, h);
                editorWindow.setLocation(x, y);

                logger.info("Saved window settings {},{} {}x{}", new Object[] {
                    x, y, w, h
                });

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
        ec.put(EditorConf.WindowSizeWidth, ""+editorWindow.getWidth());
        ec.put(EditorConf.WindowSizeHeight, ""+editorWindow.getHeight());
        ec.put(EditorConf.WindowPosX, ""+editorWindow.getLocationOnScreen().x);
        ec.put(EditorConf.WindowPosY, ""+editorWindow.getLocationOnScreen().y);
        logger.debug("Saved window settings {},{} {}x{}", new Object[] {
            ec.getInt(EditorConf.WindowSizeWidth),
            ec.getInt(EditorConf.WindowSizeHeight),
            ec.getInt(EditorConf.WindowPosX),
            ec.getInt(EditorConf.WindowPosY)
        });
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
        initializeStructurePanel();
        dockController = new CControl();
        createMainWindow();

        // requires main window
        setIcons();

        // Add your panel factories here
        registerElementPanelFactory(ActorNode.class, ActorPanel.class);
        registerElementPanelFactory(DependencyNode.class, RawElementPanel.class);		
	}

	/**
	 * Simplistic structure provider
	 */
	public static class SimpleStructureElement implements StructureElementProvider {
		private String label;
		private Icon icon;
		public SimpleStructureElement(String label, String iconUrl) {
			this.label = label;
			this.icon = new ImageIcon(ClassLoader
					.getSystemClassLoader().getResource(iconUrl));
		}

		@Override
		public String getLabel() {
			return label;
		}

		@Override
		public Icon getIcon() {
			return icon;
		}

		@Override
		public boolean canHaveChildren() {
			return false;
		}

		@Override
		public int getChildCount() {
			return 0;
		}

	}
}

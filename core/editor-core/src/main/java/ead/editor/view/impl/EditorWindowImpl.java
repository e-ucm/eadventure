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

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.editor.R;
import ead.editor.control.Controller;
import ead.editor.model.DependencyNode;
import ead.editor.view.EditorWindow;
import ead.editor.view.ToolPanel;
import ead.editor.view.dock.ClassDockableFactory;
import ead.editor.view.menu.EditorMenuBar;
import ead.editor.view.panel.RawElementPanel;
import ead.gui.structurepanel.StructureElement;
import ead.gui.structurepanel.StructureElementProvider;
import ead.gui.structurepanel.StructurePanel;
import ead.utils.i18n.Resource;
import ead.utils.swing.SwingUtilities;
import java.io.File;
import java.io.IOException;
import javax.swing.*;

/**
 * Default implementation of the main editor window
 */
@Singleton
public class EditorWindowImpl implements EditorWindow {

    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger("EWindowImpl");
    /**
     * Main left panel in the editor; contains list-of-tools
     */
    private StructurePanel leftPanel;
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
     * The menu bar for the editor
     */
    private EditorMenuBar editorMenuBar;
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
	
    /**
     * default EditorWindow implementation.
     *
     * @param toolPanel
     * @param editorMenuBar
     */
    @Inject
    public EditorWindowImpl(ToolPanel toolPanel,
            EditorMenuBar editorMenuBar,
			StructurePanel structurePanel) {
        this.toolPanel = toolPanel;
        this.editorMenuBar = editorMenuBar;
		this.leftPanel = structurePanel;
    }

    @Override
    public void initialize() {

        titlePanel = new JPanel();
        editorMenuBar.getMenuBar().add(new JSeparator());
        editorMenuBar.getMenuBar().add(toolPanel.getPanel());

		leftPanel.addElement(new StructureElement(
				new SimpleStructureElement("Scenes", R.Drawable.sidePanel__scenes_png)));
		leftPanel.addElement(new StructureElement(
				new SimpleStructureElement("Player", R.Drawable.sidePanel__player_png)));
		leftPanel.addElement(new StructureElement(
				new SimpleStructureElement("NPCs", R.Drawable.sidePanel__npcs_png)));
		leftPanel.addElement(new StructureElement(
				new SimpleStructureElement("Conversations", R.Drawable.sidePanel__conversations_png)));
		leftPanel.addElement(new StructureElement(
				new SimpleStructureElement("Items", R.Drawable.sidePanel__items_png)));
		leftPanel.addElement(new StructureElement(
				new SimpleStructureElement("Atrezzo", R.Drawable.sidePanel__atrezzo_png)));
		leftPanel.addElement(new StructureElement(
				new SimpleStructureElement("Books", R.Drawable.sidePanel__books_png)));
		leftPanel.addElement(new StructureElement(
				new SimpleStructureElement("Advanced", R.Drawable.sidePanel__advanced_png)));
		leftPanel.addElement(new StructureElement(
				new SimpleStructureElement("Cutscenes", R.Drawable.sidePanel__cutscenes_png)));
		leftPanel.addElement(new StructureElement(
				new SimpleStructureElement("Adaptation Profiles", R.Drawable.sidePanel__adaptationProfiles_png)));
		leftPanel.addElement(new StructureElement(
				new SimpleStructureElement("Assessment Profiles", R.Drawable.sidePanel__assessmentProfiles_png)));
		leftPanel.createElements();
		
        dockController = new CControl();

        // requires left and right panel; builds an animated splitPane
        createMainWindow();
        // requires main window
        setIcons();

        dockController.addMultipleDockableFactory("test",
                new ClassDockableFactory(
                RawElementPanel.class,
                DependencyNode.class, controller.getModel(), this));
    }

	@Override
    public Controller getController() {
        return controller;
    }

	@Override
	public void restoreViews() {
		File f = controller.getModel().relativeFile("views.xml");
		if (f.exists() && f.canRead()) {
			try {
				dockController.read(f);
			} catch (IOException ex) {
				logger.error("Could not restore views from {}", f, ex);
			}
		}
	}

	@Override
	public void saveViews() {
		File f = controller.getModel().relativeFile("views.xml");
		try {
			dockController.write(f);
		} catch (IOException ex) {
			logger.error("Could not save views into {}", f, ex);
		}
	}

    /**
     * Creates a new view of a given category.
     */
    private void createNewView(String id) {
        logger.info("opening view for #{}...", id);
        if (dockController.getMultipleDockable(id) != null) {
            logger.info("Learn how to make visible here!");
        } else {
            ClassDockableFactory f = (ClassDockableFactory) dockController.getMultipleDockableFactory("test");
            DefaultMultipleCDockable d = f.createDockable(id);
            dockController.addDockable(id, d);
            setLocationToRelativesIfPossible(d);
            d.setVisible(true);
        }
    }

    @Override
    public void addView(String type, String elementId, JPanel view, boolean reuseExisting) {
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
    private JDialog currentModalDialog = null;

    @Override
    public void addModalPanel(JPanel modalPanel) {
        if (currentModalDialog != null) {
            removeModalPanel(true);
        }
        currentModalDialog = new JDialog(editorWindow, true);
        currentModalDialog.add(modalPanel);
        currentModalDialog.setVisible(true);
    }

    @Override
    public void removeModalPanel(boolean cancelChanges) {
        currentModalDialog.setVisible(false);
        currentModalDialog.dispose();
        currentModalDialog = null;
    }

    /**
     * Create the main window of the editor
     */
    private void createMainWindow() {
        editorWindow = new JFrame();
        editorWindow.setTitle("eAdventure Editor");
        editorWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                leftPanel, dockController.getContentArea());
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
        SwingUtilities.doInEDTNow(new Runnable() {

            @Override
            public void run() {
                editorWindow.setMinimumSize(new Dimension(640, 400));
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
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
	
	/**
	 * Set the actual super-controller.
	 * @param controller the main controller, providing access to model, views,
	 * and more
	 */
	@Override
	public void setController(Controller controller) {
		this.controller = controller;
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

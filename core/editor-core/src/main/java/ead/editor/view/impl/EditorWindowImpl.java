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

import javax.swing.BorderFactory;
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
import ead.editor.view.dock.ElementPanel;
import ead.editor.view.menu.EditorMenuBar;
import ead.utils.i18n.Resource;
import ead.utils.swing.SwingUtilities;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
            EditorMenuBar editorMenuBar, Controller controller) {
        this.toolPanel = toolPanel;
        this.editorMenuBar = editorMenuBar;
        this.controller = controller;
    }

    @Override
    public void initialize() {

        titlePanel = new JPanel();
        editorMenuBar.getMenuBar().add(new JSeparator());
        editorMenuBar.getMenuBar().add(toolPanel.getPanel());

        leftPanel = new JPanel();
        leftPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        leftPanel.setLayout(new BorderLayout());
        leftPanel.setOpaque(true);
        leftPanel.setBackground(leftPanel.getBackground().darker());

        dockController = new CControl();

        // requires left and right panel; builds an animated splitPane
        createMainWindow();
        // requires main window
        setIcons();

        dockController.addMultipleDockableFactory("test",
                new ClassDockableFactory(
                RawElementPanel.class,
                DependencyNode.class, controller.getModel(), this));

        SwingUtilities.doInEDTNow(new Runnable() {

            @Override
            public void run() {
                File f1 = new File("/home/mfreire/code/e-ucm/e-adventure-1.x/games/PrimerosAuxiliosGame.ead");
                File f2 = new File("/tmp/cached.zip.eap");
                try {
                    if (!f2.exists()) {
                        controller.getModel().loadFromImportFile(f1);
                        controller.getModel().save(f2);
                    } else {
                        controller.getModel().load(f2);
                    }
                } catch (IOException ex) {
                    logger.error("could not load", ex);
                }
                createNewView("1");
                createNewView("2");
                createNewView("3");
                createNewView("qparam-x");
            }
        });
    }

    public Controller getController() {
        return controller;
    }

    public static class RawElementPanel extends JPanel implements ElementPanel<DependencyNode> {

        private DependencyNode target;
        private EditorWindow ew;
        private JPanel inner = new JPanel();

        @Override
        public void setTarget(DependencyNode target) {
            this.target = target;
            rebuild();
        }

        @Override
        public DependencyNode getTarget() {
            return target;
        }

        private JPanel startRow(JPanel container) {
            logger.debug("   -- new row --");
            JPanel jp = new JPanel();
//			jp.setBackground(container.getComponentCount()%2 == 0? Color.lightGray : Color.lightGray.brighter());
            FlowLayout fl = new FlowLayout(FlowLayout.LEFT, 0, 0);
			fl.setAlignOnBaseline(true);
			jp.setLayout(fl);			
            container.add(jp);
            return jp;
        }

        private void addLabelToRow(JPanel row, String text) {
            logger.debug("   appending label: " + text);
			JLabel jl = new JLabel(text);
//			jl.setBackground(Math.random() > 0.5?new Color(0xbbffbbff):new Color(0xffbbbbff));
//			jl.setOpaque(true);
			Dimension d = new Dimension(jl.getPreferredSize());
			FontMetrics fm = jl.getFontMetrics(jl.getFont());
			d.setSize(d.width, fm.getMaxAscent() + fm.getMaxDescent() + fm.getLeading());
			jl.setPreferredSize(d);
			jl.setMinimumSize(d);
			jl.setMaximumSize(d);
			row.add(jl);
        }

        private void addButtonToRow(JPanel row, String text, String id) {
            logger.debug("   appending button for id: " + id);
            JButton jb = new JButton(htmlize(id));
            jb.setForeground(Color.blue);
            jb.setBorderPainted(false);
            jb.setMargin(new Insets(0, 0, 0, 0));
            jb.setContentAreaFilled(false);
            jb.addActionListener(new OpenLinkAction(id));

            jb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            row.add(jb);
        }

        private static class CheapVerticalLayout implements LayoutManager {

            private Dimension min = new Dimension(10, 10);
            private Dimension pref = new Dimension(10, 10);
			private int maxRowHeight = 0;
			
            @Override
            public void addLayoutComponent(String name, Component comp) {
            }

            @Override
            public void removeLayoutComponent(Component comp) {
            }

            @Override
            public Dimension preferredLayoutSize(Container parent) {
                int maxX = 0;
                int maxY = 0;
                for (int i = 0; i < parent.getComponentCount(); i++) {
                    Component c = parent.getComponent(i);
                    Dimension d = c.getPreferredSize();
                    maxY = Math.max(d.height, maxY);
                    maxX = Math.max(d.width, maxX);
					
                }
				maxRowHeight = maxY;
                pref.setSize(maxX, maxRowHeight * parent.getComponentCount());
                return pref;
            }

            @Override
            public Dimension minimumLayoutSize(Container parent) {
                return min;
            }

            @Override
            public void layoutContainer(Container parent) {
                // recalculates row-height
				preferredLayoutSize(parent);
				int x = 0;
                int y = 0;
                for (int i = 0; i < parent.getComponentCount(); i++) {
                    Component c = parent.getComponent(i);
                    Dimension d = c.getPreferredSize();
                    c.setLocation(x, y + (maxRowHeight-d.height)/2);
                    c.setSize(d.width, d.height);
                    y += maxRowHeight;
                }
            }
        }

        private void rebuild() {
            removeAll();
            setLayout(new BorderLayout());
            inner = new JPanel();
            inner.setLayout(new CheapVerticalLayout());
            add(new JScrollPane(inner,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
            String st = target.getTextualDescription(ew.getController().getModel());
            logger.debug("preparing to render\n" + st);

            Pattern p = Pattern.compile("[(]([0-9]+)[)]|([\n]+)");
            Matcher m = p.matcher(st);
            int offset = 0;
            JPanel row = startRow(inner);
            while (m.find()) {
                if (m.group(1) == null) {
                    addLabelToRow(row, st.substring(offset, m.start()));
                    row = startRow(inner);
                } else {
                    addLabelToRow(row, st.substring(offset, m.start()));
                    String id = st.substring(m.start(1), m.end(1));
                    addButtonToRow(row, id, id);
                }
                offset = m.end();
            }
            addLabelToRow(row, st.substring(offset));

            revalidate();
        }

        private String htmlize(String s) {
            return "<html>" + s + "</html>";
        }

        @Override
        public void setEditor(EditorWindow ew) {
            this.ew = ew;
        }

        class OpenLinkAction implements ActionListener {

            String id;

            private OpenLinkAction(String id) {
                this.id = id;
            }

            @Override
            public void actionPerformed(ActionEvent ae) {
                ew.addView("", id, null, true);
            }
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
            removeModalPanel();
        }
        currentModalDialog = new JDialog(editorWindow, true);
        currentModalDialog.add(modalPanel);
        currentModalDialog.setVisible(true);
    }

    @Override
    public void removeModalPanel() {
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
}

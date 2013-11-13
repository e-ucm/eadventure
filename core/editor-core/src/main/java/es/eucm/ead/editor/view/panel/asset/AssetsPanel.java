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

package es.eucm.ead.editor.view.panel.asset;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import es.eucm.ead.editor.view.panel.AbstractElementPanel;
import org.jdesktop.swingx.JXRadioGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.eucm.ead.model.assets.drawable.EAdDrawable;
import es.eucm.ead.editor.model.ModelEvent;
import es.eucm.ead.editor.model.nodes.DependencyNode;
import es.eucm.ead.editor.model.nodes.EditorNode;
import es.eucm.ead.editor.model.nodes.asset.AssetNode;
import es.eucm.ead.editor.model.nodes.asset.AssetsNode;
import es.eucm.ead.editor.view.components.EditorLinkFactory;
import es.eucm.ead.editor.view.components.NodeBrowserPanel;
import es.eucm.ead.editor.view.components.PropertiesTablePanel;
import es.eucm.ead.editor.view.components.ThumbnailPanel;
import es.eucm.ead.engine.desktop.utils.assetviewer.AssetViewer;
import es.eucm.ead.engine.desktop.utils.assetviewer.AssetViewer.ImageGrabber;

/**
 * A panel that displays all assets, by type. A preview is available
 * on the left-hand side.
 *
 * @author mfreire
 */
public class AssetsPanel extends AbstractElementPanel<AssetsNode> {

	static private Logger logger = LoggerFactory.getLogger(AssetsPanel.class);
	private AssetsNode assetsNode;
	private JSplitPane split;
	private JTabbedPane tabs;
	private AssetPreviewer previewer;
	private HashMap<String, NodeBrowserPanel> thumbPanels;
	private AssetViewer rootAssetViewer;
	private HashMap<String, ArrayList<EditorNode>> nodesByCategory = new HashMap<String, ArrayList<EditorNode>>();
	private Class<? extends NodeBrowserPanel> nodeBrowserClass;

	private void setNodeBrowserClass(
			Class<? extends NodeBrowserPanel> nodeBrowserClass) {
		this.nodeBrowserClass = nodeBrowserClass;
		rebuild();
	}

	private NodeBrowserPanel createNodeBrowserPanel() {
		try {
			return nodeBrowserClass.newInstance();
		} catch (Exception e) {
			throw new IllegalArgumentException("unable to instantiate "
					+ nodeBrowserClass, e);
		}
	}

	private String iconBrowserButtonName = "Icons";
	private String detailedBrowserButtonName = "Details";

	public AssetsPanel() {

		final JXRadioGroup<String> jxrg = new JXRadioGroup<String>(
				new String[] { detailedBrowserButtonName, iconBrowserButtonName });
		jxrg.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				Class<? extends NodeBrowserPanel> next = jxrg
						.getSelectedValue().equals(detailedBrowserButtonName) ? PropertiesTablePanel.class
						: ThumbnailPanel.class;
				if (nodeBrowserClass == null) {
					nodeBrowserClass = next;
				} else if (!next.equals(nodeBrowserClass)) {
					setNodeBrowserClass(next);
				}
			}
		});
		jxrg.setSelectedValue(detailedBrowserButtonName);

		JPanel radioHolder = new JPanel(new FlowLayout());
		radioHolder.add(jxrg);

		thumbPanels = new HashMap<String, NodeBrowserPanel>();
		tabs = new JTabbedPane();
		JPanel tabsHolder = new JPanel(new BorderLayout());
		tabsHolder.add(tabs, BorderLayout.CENTER);
		tabsHolder.add(radioHolder, BorderLayout.SOUTH);
		previewer = new AssetPreviewer();
		split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tabsHolder,
				previewer);
		split.setDividerLocation(500);

		setLayout(new BorderLayout());
		add(split, BorderLayout.CENTER);
	}

	/**
	 * Internal class that previews individual assets
	 */
	private class AssetPreviewer extends JPanel {

		private EditorNode previewedNode;
		private JButton prev = new JButton("<");
		private JPanel current = new JPanel();
		private JButton next = new JButton(">");

		private AssetPreviewer() {
			setLayout(new BorderLayout());

			JPanel buttonsPanel = new JPanel(new BorderLayout());
			buttonsPanel.add(prev, BorderLayout.WEST);
			buttonsPanel.add(current, BorderLayout.CENTER);
			buttonsPanel.add(next, BorderLayout.EAST);
			add(buttonsPanel, BorderLayout.SOUTH);

			prev.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					NodeBrowserPanel selectedPane = (NodeBrowserPanel) tabs
							.getSelectedComponent();
					EditorNode prev = selectedPane.getPrevious();
					if (prev != null) {
						setNode(prev);
					}
				}
			});
			next.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					NodeBrowserPanel selectedPane = (NodeBrowserPanel) tabs
							.getSelectedComponent();
					EditorNode next = selectedPane.getNext();
					if (next != null) {
						setNode(next);
					}
				}
			});
		}

		@Override
		public void setSize(Dimension d) {
			super.setSize(d);
			updateCanvasSize();
		}

		@Override
		public void setSize(int w, int h) {
			super.setSize(w, h);
			updateCanvasSize();
		}

		private void updateCanvasSize() {
			int m = Math.max(0, Math.min(getWidth(), getHeight() - 40));
			rootAssetViewer.getCanvas().setPreferredSize(new Dimension(m, m));
			doLayout();
		}

		public void setNode(final EditorNode node) {
			logger.info("Setting preview to node {}", node.getId());

			previewedNode = node;

			if (rootAssetViewer == null) {
				rootAssetViewer = controller.createAssetViewer();
				add(rootAssetViewer.getCanvas(), BorderLayout.CENTER);
				updateCanvasSize();
			}

			current.removeAll();
			current.add(EditorLinkFactory.createLink(node, controller));
			if (node.getFirst().getContent() instanceof EAdDrawable) {
				EAdDrawable d = (EAdDrawable) node.getFirst().getContent();
				rootAssetViewer.setDrawable(d);
				final ImageGrabber grabber = new ImageGrabber();
				grabber.setCallback(new Runnable() {
					@Override
					public void run() {
						node.setThumbnail(grabber.getImage());
						tabs.getSelectedComponent().repaint();
					}
				});
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(100);
							rootAssetViewer.grabImage(grabber);
						} catch (InterruptedException ex) {
							logger.warn("interrupt while grabbing image");
						}
					}
				}).start();
			}
			revalidate();
			repaint();
		}
	}

	private void addThumbnailPanel(String fullClassName) {
		NodeBrowserPanel tp = createNodeBrowserPanel();
		tp.setController(controller);
		tp.addPropertyChangeListener(NodeBrowserPanel.selectedPropertyName,
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						EditorNode node = ((NodeBrowserPanel) evt.getSource())
								.getLastSelected();
						previewer.setNode(node);
					}
				});
		tabs.add(fullClassName.substring(fullClassName.lastIndexOf('.') + 1),
				tp);
		thumbPanels.put(fullClassName, tp);
	}

	@Override
	protected void rebuild() {
		this.assetsNode = target;
		tabs.removeAll();
		thumbPanels.clear();
		nodesByCategory.clear();
		for (AssetNode n : assetsNode.getNodes(controller.getModel())) {

			String cn = n.getFirst().getContent().getClass().getName();
			NodeBrowserPanel tp = thumbPanels.get(cn);
			ArrayList<EditorNode> al = nodesByCategory.get(cn);
			if (tp == null) {
				addThumbnailPanel(cn);
				al = new ArrayList<EditorNode>();
				nodesByCategory.put(cn, al);
				logger.info("Added category {}", cn);
			}
			al.add((EditorNode) n);
		}
		for (String s : thumbPanels.keySet()) {
			logger.info("Setting {} nodes for category {}", new Object[] {
					nodesByCategory.get(s).size(), s });
			thumbPanels.get(s).setNodes(nodesByCategory.get(s));
		}
		if (thumbPanels.containsKey("Image")) {
			tabs.setSelectedComponent(thumbPanels.get("Image"));
		}
		tabs.revalidate();
		previewer.revalidate();
	}

	/**
	 * Determines if a modelChange affects this panel. Any change that
	 * adds or removes assets, or changes them, is interpreted to affect us.
	 *
	 * @param event
	 */
	@Override
	public void modelChanged(ModelEvent event) {
		HashSet<String> toRefresh = new HashSet<String>();

		for (DependencyNode[] array : new DependencyNode[][] {
				event.getAdded(), event.getRemoved(), event.getChanged() }) {
			for (DependencyNode n : array) {
				if (n instanceof AssetNode) {
					AssetNode an = (AssetNode) n;
					String cn = an.getFirst().getContent().getClass().getName();
					if (nodesByCategory.get(cn) == null) {
						ArrayList<EditorNode> al = new ArrayList<EditorNode>();
						nodesByCategory.put(cn, al);
						al.add(an);
						toRefresh.add(cn);
					} else if (!nodesByCategory.get(cn).contains(an)) {
						nodesByCategory.get(cn).add(an);
						toRefresh.add(cn);
					} else {
						toRefresh.add(cn);
					}

					// also update previews
					if (previewer.previewedNode.getId() == n.getId()) {
						previewer.setNode(an);
					}
				}
			}
		}
		for (String cn : toRefresh) {
			if (thumbPanels.get(cn) == null) {
				addThumbnailPanel(cn);
			}
			thumbPanels.get(cn).modelChanged(event);
		}
	}
}
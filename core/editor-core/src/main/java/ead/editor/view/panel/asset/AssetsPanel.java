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
package ead.editor.view.panel.asset;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ead.common.resources.assets.drawable.EAdDrawable;
import ead.editor.control.Controller;
import ead.editor.model.nodes.asset.AssetsNode;
import ead.editor.model.nodes.DependencyNode;
import ead.editor.model.nodes.EditorNode;
import ead.editor.model.nodes.asset.AssetNode;
import ead.editor.view.components.EditorLinkFactory;
import ead.editor.view.components.NodeBrowserPanel;
import ead.editor.view.components.ThumbnailPanel;
import ead.editor.view.panel.AbstractElementPanel;
import ead.engine.core.gdx.desktop.utils.assetviewer.AssetViewer;
import ead.engine.core.gdx.desktop.utils.assetviewer.AssetViewer.ImageGrabber;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import ead.editor.model.EditorModel.ModelEvent;

/**
 * A panel that displays all assets, by type. A preview is available
 * on the left-hand side.
 *
 * @author mfreire
 */
public class AssetsPanel extends AbstractElementPanel<AssetsNode> {

	private static final Logger logger = LoggerFactory.getLogger("AssetsPanel");
	private AssetsNode assetsNode;
	private JSplitPane split;
	private JTabbedPane tabs;
	private AssetPreviewer previewer;
	private HashMap<String, ThumbnailPanel> thumbPanels;
	private AssetViewer rootAssetViewer;

	public AssetsPanel() {
		thumbPanels = new HashMap<String, ThumbnailPanel>();
		tabs = new JTabbedPane();
		previewer = new AssetPreviewer();
		split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tabs, previewer);
		split.setDividerLocation(500);

		setLayout(new BorderLayout());
		add(split, BorderLayout.CENTER);
	}

	/**
	 * Internal class that previews individual assets
	 */
	private class AssetPreviewer extends JPanel {

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
					ThumbnailPanel selectedPane = (ThumbnailPanel) tabs
							.getSelectedComponent();
					setNode(selectedPane.getPrevious());
				}
			});
			next.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					ThumbnailPanel selectedPane = (ThumbnailPanel) tabs
							.getSelectedComponent();
					setNode(selectedPane.getNext());
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

			if (node == null) {
				return;
			}

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
							Thread.yield();
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

	@Override
	protected void rebuild() {
		HashMap<String, ArrayList<EditorNode>> nodesByCategory = new HashMap<String, ArrayList<EditorNode>>();

		this.assetsNode = (AssetsNode) target;
		tabs.removeAll();
		for (AssetNode n : assetsNode.getNodes(controller.getModel())) {

			String cn = n.getFirst().getContent().getClass().getName();
			ThumbnailPanel tp = thumbPanels.get(cn);
			ArrayList<EditorNode> al = nodesByCategory.get(cn);
			if (tp == null) {
				tp = new ThumbnailPanel();
				tp.setController(controller);
				tp.addPropertyChangeListener(
						NodeBrowserPanel.selectedPropertyName,
						new PropertyChangeListener() {
							@Override
							public void propertyChange(PropertyChangeEvent evt) {
								EditorNode node = ((NodeBrowserPanel) evt
										.getSource()).getLastSelected();
								previewer.setNode(node);
							}
						});
				tabs.add(cn.substring(cn.lastIndexOf('.') + 1), tp);
				thumbPanels.put(cn, tp);

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
		tabs.revalidate();
		previewer.revalidate();
	}

	/**
	 * Determines if a modelChange affects this panel. Any change that 
	 * adds or removes assets, or changes them, is interpreted to affect us.
	 * 
	 * FIXME: this could be a lot more efficient, by making the ThumbnailPanels
	 * (and their subpanels) responsive to changes.
	 * 
	 * @param event 
	 */
	@Override
	public void modelChanged(ModelEvent event) {
		boolean mustRebuild = false;
		for (DependencyNode[] array : new DependencyNode[][] {
				event.getAdded(), event.getRemoved(), event.getChanged() }) {
			if (!mustRebuild) {
				for (DependencyNode n : array) {
					if (n instanceof AssetNode) {
						mustRebuild = true;
					}
				}
			}
		}
		if (mustRebuild) {
			rebuild();
		}
	}
}
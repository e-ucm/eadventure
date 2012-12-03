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

package ead.editor.view.panel;

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
import ead.editor.view.components.NodeBrowserPanel;
import ead.editor.view.components.ThumbnailPanel;
import ead.engine.core.gdx.desktop.utils.assetviewer.AssetViewer;
import ead.engine.core.gdx.desktop.utils.assetviewer.AssetViewer.ImageGrabber;
import java.awt.FlowLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

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
	private HashMap<String, ThumbnailPanel> panels = new HashMap<String, ThumbnailPanel>();
	private AssetViewer rootAssetViewer;

	public AssetsPanel() {
		tabs = new JTabbedPane();
		previewer = new AssetPreviewer();
		split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tabs, previewer);

		setLayout(new BorderLayout());
		add(split, BorderLayout.CENTER);
	}

	private class AssetPreviewer extends JPanel {
		private JButton prev = new JButton("<");
		private JPanel current = new JPanel();
		private JButton next = new JButton(">");

		private void initialize(Controller controller) {
			tabs.removeAll();

			if (rootAssetViewer != null) {
				rootAssetViewer.stop();
			}
			rootAssetViewer = controller.createAssetViewer();
			add(rootAssetViewer.getCanvas(), BorderLayout.CENTER);

			JPanel buttonsPanel = new JPanel(new FlowLayout());
			buttonsPanel.add(prev);
			buttonsPanel.add(current);
			buttonsPanel.add(next);
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

		public void setNode(final EditorNode node) {
			if (node.getFirst() instanceof EAdDrawable) {
				rootAssetViewer.setDrawable(null);
				final ImageGrabber grabber = new ImageGrabber();
				grabber.setCallback(new Runnable() {
					@Override
					public void run() {
						node.setThumbnail(grabber.getImage());
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
		}
	}

	@Override
	protected void rebuild() {
		HashMap<String, ArrayList<EditorNode>> nodesByCategory = new HashMap<String, ArrayList<EditorNode>>();

		this.assetsNode = (AssetsNode) target;
		tabs.removeAll();
		previewer.initialize(controller);
		for (DependencyNode n : assetsNode.getNodes(controller.getModel())) {

			String cn = n.getContent().getClass().getName();
			ThumbnailPanel tp = panels.get(cn);
			ArrayList<EditorNode> al = nodesByCategory.get(cn);
			if (tp == null) {
				tp = new ThumbnailPanel();
				al = new ArrayList<EditorNode>();
				panels.put(cn, tp);
				nodesByCategory.put(cn, al);
				tabs.add(cn.substring(cn.lastIndexOf('.') + 1), tp);

			}
			tp.add(new ThumbnailPanel());
			al.add((EditorNode) n);
			tp.addPropertyChangeListener(NodeBrowserPanel.selectedPropertyName,
					new PropertyChangeListener() {
						@Override
						public void propertyChange(PropertyChangeEvent evt) {
							EditorNode node = ((NodeBrowserPanel) evt
									.getSource()).getLastSelected();
							previewer.setNode(node);
						}
					});
		}
		for (String s : panels.keySet()) {
			panels.get(s).setNodes(nodesByCategory.get(s));
		}
	}
}
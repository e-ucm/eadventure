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
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ead.common.resources.assets.AssetDescriptor;
import ead.common.resources.assets.drawable.EAdDrawable;
import ead.editor.control.Controller;
import ead.editor.model.nodes.AssetsNode;
import ead.editor.model.nodes.DependencyNode;
import ead.engine.core.gdx.desktop.utils.assetviewer.AssetViewer;
import java.util.HashMap;
import javax.swing.JTabbedPane;

/**
 * An elementPanel that can display anything, in a non-editable fashion.
 *
 * @author mfreire
 */
public class AssetsPanel extends AbstractElementPanel<AssetsNode> {

	private static final Logger logger = LoggerFactory.getLogger("ActorPanel");

	private AssetsNode assetsNode;

	private JTabbedPane tabs;
	private HashMap<String, AssetCategoryPanel> panels
			= new HashMap<String, AssetCategoryPanel>();

	public AssetsPanel() {
		tabs = new JTabbedPane();
		setLayout(new BorderLayout());
		this.add(tabs, BorderLayout.CENTER);
	}

	@Override
	protected void rebuild() {
		this.assetsNode = (AssetsNode) target;
		for (AssetCategoryPanel cp : panels.values()) {
			cp.clear();
		}
		tabs.removeAll();
		for (DependencyNode n : assetsNode.getNodes(controller.getModel())) {
			String cn = n.getContent().getClass().getName();
			AssetCategoryPanel cp = panels.get(cn);
			if (cp == null) {
				cp = new AssetCategoryPanel();
				panels.put(cn, cp);
				tabs.add(cn.substring(cn.lastIndexOf('.')+1), cp);
			}
			cp.add(new AssetPanel(n, controller));
		}
		for (AssetCategoryPanel cp : panels.values()) {
			cp.commit();
		}
	}

	private class AssetCategoryPanel extends JPanel {
		private JPanel scrollable;
		private JPanel spacerPanel;
		private ArrayList<AssetPanel> panels = new ArrayList<AssetPanel>();

		private AssetCategoryPanel() {
			scrollable = new JPanel();
			scrollable.setLayout(new GridBagLayout());
			spacerPanel = new JPanel();
			setLayout(new BorderLayout());
			add(new JScrollPane(scrollable), BorderLayout.CENTER);
		}

		/**
		 * Prepare the panel for drawing. Makes changes visible.
		 */
		public void commit() {
			scrollable.removeAll();
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.gridheight = 1;
			gbc.gridwidth = 1;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.weightx = 1.0;
			gbc.weighty = 0;
			for (JPanel p : panels) {
				gbc.gridy++;
				scrollable.add(p, gbc);
			}
			gbc.gridy++;
			gbc.weighty = 1.0;
			scrollable.add(spacerPanel, gbc);
			revalidate();
		}

		public void add(AssetPanel panel) {
			panels.add(panel);
		}

		public void clear() {
			for (AssetPanel p : panels) {
				if (p.visible) {
					p.toggleShow();
				}
			}
			panels.clear();
		}
	}

	private class AssetPanel extends JPanel {
		private final DependencyNode an;
		private JPanel canvasPanel;
		private boolean visible = false;
		private AssetViewer av;
		private JButton showButton;
		private AssetDescriptor descriptor;

		public void toggleShow() {
			if (visible) {
				if (av != null) {
					av.stop();
				}
				canvasPanel.removeAll();
				showButton.setText("<O>");
			} else {
				av = controller.createAssetViewer();
				av.setDrawable((EAdDrawable) an.getContent());
				canvasPanel.add(av.getCanvas(), BorderLayout.CENTER);
				showButton.setText("\\_/");
			}
			visible = !visible;
		}

		public AssetPanel(DependencyNode an, Controller controller) {
			this.an = an;
			this.descriptor = (AssetDescriptor) an.getContent();
			GridBagConstraints gbc = new GridBagConstraints(1, 0, 1, 1, 1, 0,
					GridBagConstraints.WEST, GridBagConstraints.BOTH,
					new Insets(5, 5, 5, 5), 0, 0);
			setLayout(new GridBagLayout());
			JLabel description = new JLabel();
			description.setText("<html>"
					+ descriptor.toString()
					+ controller.getAssetHandler().getRuntimeAsset(descriptor).getLength()
					+ " bytes"
					+ "</html>");
			description.setHorizontalAlignment(SwingConstants.LEFT);
			add(description, gbc);
			canvasPanel = new JPanel();
			canvasPanel.setLayout(new BorderLayout());
			canvasPanel.setPreferredSize(new Dimension(100, 100));
			gbc.weightx = 0;
			gbc.gridx = 0;
			gbc.gridheight = 2;
			add(canvasPanel, gbc);
			showButton = new JButton("<O>");
			showButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					toggleShow();
				}
			});
			if (!(descriptor instanceof EAdDrawable)) {
				showButton.setEnabled(false);
			} else {
				toggleShow();
			}
			gbc.weightx = 0;
			gbc.gridx = 1;
			gbc.gridy = 1;
			gbc.gridheight = 1;
			gbc.fill = GridBagConstraints.NONE;
			add(showButton, gbc);
		}
	}
}
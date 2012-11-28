/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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

package ead.editor.view.components;

import ead.editor.model.nodes.DependencyNode;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.TreeSet;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A panel that displays thumbnails for a number of elements.
 * Element thumbnails can be selected (all or some), and have, as titles, the
 * EditorLinks.
 * @author mfreire
 */
public class ThumbnailPanel extends JPanel {

	private static final Logger logger = LoggerFactory
			.getLogger("ThumbnailPanel");

	private TreeSet<Integer> selected = new TreeSet<Integer>();
	private JPanel inner;

	public final static String selectedPropertyName = "node_selected";

	private final static int thumbImageSize = 64;
	private final static int thumbWidth = 100;
	private final static int thumbHeight = 92;
	private final static int thumbInnerMargins = 3;

	private class Thumbnail extends JPanel {
		private final DependencyNode node;

		public Thumbnail(DependencyNode node) {
			this.node = node;
			setLayout(null);

			int m = (thumbWidth - (thumbImageSize + thumbInnerMargins * 2)) / 2;
			Thumb t = new Thumb();
			t.setBounds(m, thumbInnerMargins, thumbImageSize, thumbImageSize);
			t.setSize(thumbImageSize, thumbImageSize);
			add(t);

			EditorLink e = EditorLinkFactory.createLink(node, null);
			e.setBounds(0, thumbImageSize, thumbWidth, (int) e
					.getPreferredSize().getHeight());
			e.setSize(thumbWidth, (int) e.getPreferredSize().getHeight());
			add(e);

			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					int id = Thumbnail.this.node.getId();
					int ctrlDown = MouseEvent.CTRL_DOWN_MASK;
					if ((e.getModifiersEx() & ctrlDown) != ctrlDown) {
						selected.clear();
						selected.add(id);
						getParent().repaint();
					} else {
						repaint();
						selected.add(id);
					}
					ThumbnailPanel.this.firePropertyChange(selectedPropertyName, null, id);
				}
			});
		}

		@Override
		public void paint(Graphics g) {
			if (selected.contains(node.getId())) {
				super.paintComponent(g);
				g.setColor(new Color(0.8f, 0.8f, 1.0f));
				g.fillRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 2, 2);
				super.paintChildren(g);
			} else {
				super.paint(g);
			}
		}

		private class Thumb extends JPanel {
			public Thumb() {
				setPreferredSize(new Dimension(64, 64));
			}

			@Override
			public void paint(Graphics g) {
				super.paint(g);
				g.drawImage(node.getThumbnail(), 0, 0, getWidth(), getHeight(),
						this);
			}
		}
	}

	public static class DynamicGridLayout implements LayoutManager {

		private static int tw = thumbWidth;
		private static int th = thumbHeight;
		private static int margin = 5;

		private Container lastParent;

		public static int colsFor(int width) {
			return (width - margin) / (tw + margin);
		}

		public static int widthFor(int cols) {
			return cols * (tw + margin) + margin;
		}

		private int heightFor(int n, int cols) {
			if (n == 0 || cols == 0) {
				return 0;
			}
			int rows = n / cols + ((n % cols > 0) ? 1 : 0);
			return rows * (th + margin) + margin;
		}

		@Override
		public void addLayoutComponent(String name, Component comp) {
		}

		@Override
		public void removeLayoutComponent(Component comp) {
		}

		@Override
		public Dimension preferredLayoutSize(Container parent) {
			lastParent = parent;
			int w = Math.max(parent.getWidth(), margin + tw + margin);
			int cols = colsFor(w);
			return new Dimension(widthFor(cols), heightFor(parent
					.getComponentCount(), cols));
		}

		@Override
		public Dimension minimumLayoutSize(Container parent) {
			return preferredLayoutSize(parent);
		}

		@Override
		public void layoutContainer(Container parent) {
			Rectangle r = new Rectangle(margin, margin, tw, th);
			int cols = Math.max(1, colsFor(parent.getWidth()));
			for (int i = 0; i < parent.getComponentCount(); i++) {
				int row = i / cols;
				int col = i % cols;
				r.x = margin + col * (tw + margin);
				r.y = margin + row * (th + margin);
				Component c = parent.getComponent(i);
				c.setBounds(r);
				c.setSize(tw, th);
			}
		}
	}

	public static class WidthSensitiveScrollPane extends JScrollPane {
		private ComponentListener resizeListener = new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				Dimension p = e.getComponent().getSize();
				Component v = getViewport().getComponent(0);
				Dimension c = new Dimension(v.getPreferredSize());
				c.width = DynamicGridLayout.widthFor(DynamicGridLayout
						.colsFor(p.width)) + 4;
				v.setPreferredSize(c);
				v.doLayout();
			}
		};

		public void setParent(Component parent) {
			parent.addComponentListener(resizeListener);
		}
	}

	public ThumbnailPanel() {
		inner = new JPanel();
		inner.setLayout(new DynamicGridLayout());
		WidthSensitiveScrollPane scroll = new WidthSensitiveScrollPane();
		scroll.setViewportView(inner);
		scroll.setParent(this);
		setLayout(new BorderLayout());
		add(scroll, BorderLayout.CENTER);
	}

	public void setNodes(Collection<DependencyNode> nodes) {
		selected.clear();
		inner.removeAll();
		for (DependencyNode n : nodes) {
			inner.add(new Thumbnail(n));
		}
		inner.validate();
	}

	public TreeSet<Integer> getSelected() {
		return selected;
	}
}
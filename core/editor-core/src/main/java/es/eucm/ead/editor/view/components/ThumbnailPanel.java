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

package es.eucm.ead.editor.view.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceAdapter;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import es.eucm.ead.editor.model.ModelEvent;
import es.eucm.ead.editor.model.ModelEventUtils;
import es.eucm.ead.editor.model.nodes.DependencyNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.eucm.ead.editor.model.nodes.EditorNode;

/**
 * A panel that displays thumbnails for a number of elements.
 * Element thumbnails can be selected (all or some), and have, as titles, the
 * EditorLinks.
 * @author mfreire
 */
public class ThumbnailPanel extends NodeBrowserPanel {

	static private Logger logger = LoggerFactory
			.getLogger(ThumbnailPanel.class);

	private JPanel inner;

	private int thumbImageSize = 64;
	private int thumbWidth = 128;
	private int thumbHeight = 92;
	private int thumbInnerMargins = 3;

	private DynamicGridLayout dgl;

	private NodeTransferHandler transferHandler = new NodeTransferHandler();

	private HashMap<Integer, Thumbnail> thumbMap = new HashMap<Integer, Thumbnail>();

	public ThumbnailPanel() {
		inner = new JPanel();
		dgl = new DynamicGridLayout();
		inner.setLayout(dgl);
		WidthSensitiveScrollPane scroll = new WidthSensitiveScrollPane();
		scroll.getVerticalScrollBar().setUnitIncrement(16);
		scroll.setViewportView(inner);
		scroll.setParent(this);
		inner.setTransferHandler(transferHandler);
		add(scroll, BorderLayout.CENTER);
	}

	@Override
	public void modelChanged(ModelEvent event) {
		for (DependencyNode n : nodes) {
			if (ModelEventUtils.changes(event, n)) {
				int pos = thumbMap.get(n.getId()).index;
				inner.remove(pos);
				thumbMap.put(n.getId(), new Thumbnail((EditorNode) n, pos));
				inner.add(thumbMap.get(n.getId()), pos);
			}
		}
	}

	private class Thumbnail extends JPanel {
		private final EditorNode node;
		private final int index;

		public Thumbnail(EditorNode node, int index) {
			this.node = node;
			this.index = index;
			setLayout(null);

			DragSource.getDefaultDragSource()
					.createDefaultDragGestureRecognizer(this,
							DnDConstants.ACTION_COPY,
							new DragGestureListener() {
								@Override
								public void dragGestureRecognized(
										DragGestureEvent dge) {
									Transferable t = transferHandler
											.createTransferable(ThumbnailPanel.this);
									dge.startDrag(DragSource.DefaultCopyNoDrop,
											t, new DragSourceAdapter() {

											});
								}
							});

			int m = (thumbWidth - (thumbImageSize + thumbInnerMargins * 2)) / 2;
			Thumb t = new Thumb();
			t.setBounds(m, thumbInnerMargins, thumbImageSize, thumbImageSize);
			t.setSize(thumbImageSize, thumbImageSize);
			add(t);

			EditorLink e = EditorLinkFactory.createLink(node, controller);
			e.setBounds(0, thumbImageSize, thumbWidth, (int) e
					.getPreferredSize().getHeight());
			e.setSize(thumbWidth, (int) e.getPreferredSize().getHeight());
			add(e);

			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					EditorNode node = Thumbnail.this.node;
					int ctrlDown = MouseEvent.CTRL_DOWN_MASK;
					boolean wasSelected = selected.contains(node);
					if ((e.getModifiersEx() & ctrlDown) != ctrlDown) {
						selected.clear();
						if (!wasSelected) {
							selected.add(node);
						}
					} else {
						if (!wasSelected) {
							selected.add(node);
						} else {
							selected.remove(node);
						}
					}
					lastSelected = node;
					ThumbnailPanel.this.firePropertyChange(
							selectedPropertyName, null, node);
					getParent().repaint();
				}
			});
		}

		@Override
		public void paint(Graphics g) {
			if (selected.contains(node)) {
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
				g.drawImage(node.getThumbnail(), 0, 0, getWidth(), getHeight(),
						this);
			}
		}
	}

	private class DynamicGridLayout implements LayoutManager {

		private int margin = 5;
		private int minMargin = 5;

		public int colsFor(int width) {
			return (width - minMargin) / (thumbWidth + minMargin);
		}

		public void updateMarginFromWidth(int width, int cols) {
			int need = cols * thumbWidth;
			int extra = width - need;
			margin = extra / (cols + 1);
			logger
					.info(
							"UpdateMargin: width {}, {} cols; need {}, {} left-over - setting margin to {}",
							new Object[] { width, cols, need, extra, margin });
		}

		public int widthFor(int cols) {
			return cols * (thumbWidth + minMargin) + minMargin;
		}

		private int heightFor(int n, int cols) {
			if (n == 0 || cols == 0) {
				return 0;
			}
			int rows = n / cols + ((n % cols > 0) ? 1 : 0);
			return rows * (thumbHeight + minMargin) + minMargin;
		}

		@Override
		public void addLayoutComponent(String name, Component comp) {
		}

		@Override
		public void removeLayoutComponent(Component comp) {
		}

		@Override
		public Dimension preferredLayoutSize(Container parent) {
			int w = Math.max(parent.getWidth(), margin + thumbWidth + margin);
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
			Rectangle r = new Rectangle(margin, minMargin, thumbWidth,
					thumbHeight);
			int cols = Math.max(1, colsFor(parent.getWidth()));
			for (int i = 0; i < parent.getComponentCount(); i++) {
				int row = i / cols;
				int col = i % cols;
				r.x = margin + col * (thumbWidth + margin);
				r.y = minMargin + row * (thumbHeight + minMargin);
				Component c = parent.getComponent(i);
				c.setBounds(r);
				c.setSize(thumbWidth, thumbHeight);
			}
		}
	}

	private class WidthSensitiveScrollPane extends JScrollPane {
		private ComponentListener resizeListener = new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				Dimension p = e.getComponent().getSize();
				Component v = getViewport().getComponent(0);
				Dimension c = new Dimension(v.getPreferredSize());
				int cols = dgl.colsFor(p.width - 4);
				c.width = dgl.widthFor(cols);
				dgl.updateMarginFromWidth(p.width - 4, cols);
				v.setPreferredSize(c);
				v.doLayout();
			}
		};

		public void setParent(Component parent) {
			parent.addComponentListener(resizeListener);
		}
	}

	@Override
	public void setNodes(Collection<EditorNode> nodes) {
		selected.clear();
		inner.removeAll();
		this.nodes.clear();
		for (EditorNode n : nodes) {
			addNode(n);
		}
		inner.revalidate();
		revalidate();
	}

	@Override
	public void addNode(EditorNode node) {
		nodes.add(node);
		Thumbnail t = new ThumbnailPanel.Thumbnail(node, inner
				.getComponentCount());
		thumbMap.put(node.getId(), t);
		inner.add(t);
	}
}
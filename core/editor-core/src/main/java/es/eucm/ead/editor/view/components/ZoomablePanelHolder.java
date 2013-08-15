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

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A JPanel that centers an inner panel, which can be resized
 * via zoom and dragged around.
 * @author mfreire
 */
public class ZoomablePanelHolder extends JPanel {

	private static final Logger logger = LoggerFactory
			.getLogger("ZoomablePanelHolder");

	public ZoomablePanelHolder(Component child) {
		GrowableScrollPane scrollPane = new GrowableScrollPane();
		scrollPane.setParent(this);
		scrollPane.setViewportView(child);
		SmartLayout layout = new SmartLayout(scrollPane);
		setLayout(layout);
		add(scrollPane);
	}

	/**
	 * A layout that centers its contents, and always sets its preferred
	 * size to that of the contents.
	 */
	public static class SmartLayout implements LayoutManager {
		private Component content;
		private Dimension d = new Dimension();

		public SmartLayout(Component content) {
			this.content = content;
		}

		@Override
		public void addLayoutComponent(String name, Component comp) {
		}

		@Override
		public void removeLayoutComponent(Component comp) {
		}

		@Override
		public Dimension preferredLayoutSize(Container parent) {
			return new Dimension(parent.getSize());
		}

		@Override
		public Dimension minimumLayoutSize(Container parent) {
			return new Dimension();
		}

		@Override
		public void layoutContainer(Container parent) {
			if (content == null) {
				logger.debug("SmartLayout short-circuited");
				return;
			}

			Dimension p = parent.getSize();
			Dimension c = content.getPreferredSize();

			d.setSize(Math.min(p.width, c.width), Math.min(p.height, c.height));
			int marginX = (p.width - d.width) / 2;
			int marginY = (p.height - d.height) / 2;
			logger.debug("SmartLayout resizing to {}x{}", new Object[] {
					d.width, d.height });
			content.setBounds(marginX, marginY, d.width, d.height);
			content.setSize(d);
		}
	}

	/**
	 * A ScrollPane that can grow and shrink itself, instead of keeping
	 * always constant size. Listens to its parent's resizes for shrinking.
	 */
	public static class GrowableScrollPane extends JScrollPane {
		private ComponentListener resizeListener = new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				doLayout();
			}
		};

		public void setParent(Component parent) {
			parent.addComponentListener(resizeListener);
		}

		@Override
		public JViewport createViewport() {
			return new JViewport() {
				@Override
				public void doLayout() {
					Dimension c = new Dimension(getComponent(0)
							.getPreferredSize());
					logger.debug("GrowableViewport resizing to {}x{}",
							new Object[] { c.width, c.height });
					setPreferredSize(c);
					getParent().doLayout();
					super.doLayout();
				}
			};
		}

		@Override
		public void doLayout() {
			// used mostly to shrink
			fixSizes();
			super.doLayout();
		}

		private void fixSizes() {
			Dimension pd = new Dimension(getParent().getSize());
			Dimension cd = getViewport().getPreferredSize();
			logger.debug(
					"GrowableScrollpanel parent is {}x{}, child wants {}x{}",
					new Object[] { pd.width, pd.height, cd.width, cd.height });
			pd.width = Math.min(cd.width + 4, pd.width);
			pd.height = Math.min(cd.height + 4, pd.height);
			logger.debug("GrowableScrollpanel --> {}x{}", new Object[] {
					pd.width, pd.height });
			setPreferredSize(pd);
			getParent().doLayout();
		}
	}
}

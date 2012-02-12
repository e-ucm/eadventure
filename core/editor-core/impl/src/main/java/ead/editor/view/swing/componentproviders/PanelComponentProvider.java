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

package ead.editor.view.swing.componentproviders;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Scrollable;

import ead.editor.view.ComponentProvider;
import ead.editor.view.ProviderFactory;
import ead.editor.view.generics.InterfaceElement;
import ead.editor.view.generics.Panel;
import ead.gui.EAdBorderedPanel;
import ead.gui.EAdScrollPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PanelComponentProvider implements ComponentProvider<Panel, JPanel> {

	private static final Logger logger = LoggerFactory.getLogger("PanelComponentProvider");

	private ProviderFactory<JComponent> providerFactory;

	public PanelComponentProvider(ProviderFactory<JComponent> providerFactory) {
		this.providerFactory = providerFactory;
	}

	//TODO Should support different element positioning policies
	@Override
	public JPanel getComponent(Panel element) {
		JPanel mainPanel;
		if (element.getTitle() != null)
			mainPanel = new EAdBorderedPanel(element.getTitle());
		else
			mainPanel = new JPanel();

		JPanel panel = new ScrollablePanel();
		EAdScrollPane scrollPane = new EAdScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(scrollPane, BorderLayout.CENTER);

		java.awt.GridBagConstraints c = null;

		if (element.getLayoutPolicy() == Panel.LayoutPolicy.HORIZONTAL ||
				element.getLayoutPolicy() == Panel.LayoutPolicy.VERTICAL) {
			panel.setLayout(new java.awt.GridBagLayout());
			c = new java.awt.GridBagConstraints();
			c.fill = java.awt.GridBagConstraints.BOTH;
			c.gridx = 0;
			c.weightx = 1.0;
			c.gridy = 0;
			c.weighty = 1.0;
		} else if (element.getLayoutPolicy() == Panel.LayoutPolicy.FLOW)
			panel.setLayout(new java.awt.FlowLayout());
		else
			panel.setLayout(new java.awt.GridLayout(0,1));

		for (InterfaceElement newElement : element.getElements()) {
			ComponentProvider<InterfaceElement, JComponent> cp = providerFactory.getProvider(newElement);
			if (cp == null)
				logger.error("No provider for class {}", newElement.getClass());
			JComponent component = cp.getComponent(newElement);
			if (component == null)
				logger.error("No component for class {} with provider {}",
                        newElement.getClass(), cp.getClass());
			if (element.getLayoutPolicy() == Panel.LayoutPolicy.VERTICAL) {
				c.weighty = component.getMinimumSize().getHeight();
				panel.add(component, c);
				c.gridy++;
			} else if (element.getLayoutPolicy() == Panel.LayoutPolicy.HORIZONTAL) {
				panel.add(component, c);
				c.gridx++;
			}
		}

		//mainPanel.doLayout();

		return mainPanel;
	}

	private class ScrollablePanel extends JPanel implements Scrollable {

		private static final long serialVersionUID = -8779328786327371343L;

		public Dimension getPreferredSize() {
			Dimension preferred = super.getPreferredSize();
			Dimension container = super.getParent().getParent().getSize();
			return new Dimension((int) Math.max(preferred.getWidth(), container.getWidth()),
					(int) Math.max(preferred.getHeight(), container.getHeight()));
		}

		@Override
		public Dimension getPreferredScrollableViewportSize() {
			return super.getPreferredSize();
		}

		@Override
		public int getScrollableBlockIncrement(Rectangle visibleRect,
				int orientation, int direction) {
			//TODO check
			return 10;
		}

		@Override
		public boolean getScrollableTracksViewportHeight() {
			return false;
		}

		@Override
		public boolean getScrollableTracksViewportWidth() {
			return true;
		}

		@Override
		public int getScrollableUnitIncrement(Rectangle visibleRect,
				int orientation, int direction) {
			//TODO check
			return 1;
		}

	}

}

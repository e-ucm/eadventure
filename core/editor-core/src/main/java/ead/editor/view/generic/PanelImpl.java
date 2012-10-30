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

package ead.editor.view.generic;

import ead.editor.control.CommandManager;
import java.util.ArrayList;
import java.util.List;

import ead.editor.view.generic.InterfaceElement;
import ead.editor.view.generic.Panel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Scrollable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PanelImpl implements Panel {

	private static final Logger log = LoggerFactory.getLogger("PanelOption");

	private List<InterfaceElement> elements;

	private String title;

	private LayoutPolicy layoutPolicy;

	public PanelImpl(String title, LayoutPolicy layoutPolicy) {
		elements = new ArrayList<InterfaceElement>();
		this.title = title;
		this.layoutPolicy = layoutPolicy;
	}

	@Override
	public List<InterfaceElement> getElements() {
		return elements;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void addElement(InterfaceElement element) {
		elements.add(element);
	}

	@Override
	public LayoutPolicy getLayoutPolicy() {
		return layoutPolicy;
	}

	//TODO Should support different element positioning policies
	@Override
	public JPanel getComponent(CommandManager manager) {
		JPanel mainPanel = new JPanel();
		if (getTitle() != null) {
			mainPanel.setBorder(
					BorderFactory.createTitledBorder(getTitle()));
		}

		JPanel panel = new ScrollablePanel();
		JScrollPane scrollPane = new JScrollPane(panel);

		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(scrollPane, BorderLayout.CENTER);

		java.awt.GridBagConstraints c = null;

		if (getLayoutPolicy() == Panel.LayoutPolicy.HORIZONTAL ||
				getLayoutPolicy() == Panel.LayoutPolicy.VERTICAL) {
			panel.setLayout(new java.awt.GridBagLayout());
			c = new java.awt.GridBagConstraints();
			c.fill = java.awt.GridBagConstraints.BOTH;
			c.gridx = 0;
			c.weightx = 1.0;
			c.gridy = 0;
			c.weighty = 1.0;
		} else if (getLayoutPolicy() == Panel.LayoutPolicy.FLOW)
			panel.setLayout(new java.awt.FlowLayout());
		else
			panel.setLayout(new java.awt.GridLayout(0,1));

		for (InterfaceElement newElement : getElements()) {
			JComponent component = newElement.getComponent(manager);
			if (getLayoutPolicy() == Panel.LayoutPolicy.VERTICAL) {
				c.weighty = component.getMinimumSize().getHeight();
				panel.add(component, c);
				c.gridy++;
			} else if (getLayoutPolicy() == Panel.LayoutPolicy.HORIZONTAL) {
				panel.add(component, c);
				c.gridx++;
			}
		}

		//mainPanel.doLayout();

		return mainPanel;
	}

	private class ScrollablePanel extends JPanel implements Scrollable {

		private static final long serialVersionUID = -8779328786327371343L;

		@Override
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

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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import ead.editor.control.CommandManager;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
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

	private JPanel basePanel;

	private int insets;

	private LayoutBuilder builder;

	public PanelImpl(String title, LayoutPolicy layoutPolicy, int insets) {
		elements = new ArrayList<InterfaceElement>();
		this.title = title;
		this.layoutPolicy = layoutPolicy;
		this.insets = insets;
		switch (layoutPolicy) {
		case Flow: {
			builder = new FlowBuilder();
			break;
		}
		case VerticalEquallySpaced: {
			builder = new VerticalEquallySpacedBuilder();
			break;
		}
		case HorizontalBlocks: {
			builder = new HorizontalBlocksBuilder();
			break;
		}
		case VerticalBlocks: {
			builder = new VerticalBlocksBuilder();
			break;
		}
		default:
			throw new IllegalArgumentException("No builder for " + layoutPolicy);
		}
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
	public PanelImpl addElement(InterfaceElement element) {
		elements.add(element);
		return this;
	}

	@Override
	public LayoutPolicy getLayoutPolicy() {
		return layoutPolicy;
	}

	//TODO Should support different element positioning policies
	@Override
	public JPanel getComponent(CommandManager manager) {
		basePanel = new ScrollablePanel();
		JScrollPane scrollPane = new JScrollPane(basePanel);
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		builder.start();
		for (InterfaceElement e : getElements()) {
			builder.add(e, manager);
		}
		builder.finish();
		basePanel.revalidate();
		return mainPanel;
	}

	@Override
	public void cleanup(CommandManager manager) {
		for (InterfaceElement ie : elements) {
			ie.cleanup(manager);
		}
	}

	private class ScrollablePanel extends JPanel implements Scrollable {

		private static final long serialVersionUID = -8779328786327371343L;

		@Override
		public Dimension getPreferredSize() {
			Dimension preferred = super.getPreferredSize();
			Dimension container = super.getParent().getParent().getSize();
			int w = Math.max(preferred.width, container.width);
			int h = Math.max(preferred.height, container.height);
			return new Dimension(w, h - 10);
		}

		@Override
		public Dimension getPreferredScrollableViewportSize() {
			return super.getPreferredSize();
		}

		@Override
		public int getScrollableBlockIncrement(Rectangle visibleRect,
				int orientation, int direction) {
			return 16;
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
			return 16;
		}
	}

	// ----- layout builders here -----

	public interface LayoutBuilder {
		void start();

		void add(InterfaceElement element, CommandManager manager);

		void finish();
	}

	public abstract class SimpleBuilder implements LayoutBuilder {
		public abstract LayoutManager getLayout();

		@Override
		public void start() {
			basePanel.setLayout(getLayout());
		}

		@Override
		public void add(InterfaceElement element, CommandManager manager) {
			basePanel.add(element.getComponent(manager));
		}

		@Override
		public void finish() {
			// nothing to do.
		}
	}

	public class FlowBuilder extends SimpleBuilder {
		@Override
		public LayoutManager getLayout() {
			return new FlowLayout(FlowLayout.CENTER, insets, insets);
		}
	}

	public class VerticalEquallySpacedBuilder extends SimpleBuilder {
		@Override
		public LayoutManager getLayout() {
			return new GridLayout(0, 1, insets, insets);
		}
	}

	public abstract class GridBuilder implements LayoutBuilder {
		protected GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1,
				0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(insets, insets, insets, insets), 0, 0);

		@Override
		public void start() {
			basePanel.setLayout(new GridBagLayout());
		}

		@Override
		public void add(InterfaceElement element, CommandManager manager) {
			if (element.getTitle() != null) {
				JLabel label = new JLabel(element.getTitle());
				prepareLabel();
				basePanel.add(label, gbc);
				endLabel();
			}
			prepareField();
			basePanel.add(element.getComponent(manager), gbc);
			endField();
		}

		public abstract void prepareLabel();

		public abstract void endLabel();

		public abstract void prepareField();

		public abstract void endField();

		@Override
		public void finish() {
			JPanel spacer = new JPanel();
			gbc.fill = GridBagConstraints.BOTH;
			gbc.weightx = 1.0;
			gbc.weighty = 1.0;
			gbc.insets = new Insets(0, 0, 0, 0);
			basePanel.add(spacer, gbc);
		}
	}

	public class VerticalBlocksBuilder extends GridBuilder {

		@Override
		public void prepareLabel() {
			gbc.fill = GridBagConstraints.NONE;
			gbc.weightx = 0;
		}

		@Override
		public void endLabel() {
			gbc.gridx++;
		}

		@Override
		public void prepareField() {
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.weightx = 5.0;
		}

		@Override
		public void endField() {
			gbc.gridx = 0;
			gbc.gridy++;
		}
	}

	public class HorizontalBlocksBuilder extends GridBuilder {

		@Override
		public void prepareLabel() {
			gbc.fill = GridBagConstraints.NONE;
			gbc.weighty = 0;
		}

		@Override
		public void endLabel() {
			gbc.gridy++;
		}

		@Override
		public void prepareField() {
			gbc.fill = GridBagConstraints.VERTICAL;
			gbc.weighty = 1.0;
		}

		@Override
		public void endField() {
			gbc.gridx++;
			gbc.gridy = 0;
		}

	}
}

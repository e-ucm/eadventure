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

package ead.gui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager2;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;

import javax.swing.JPanel;

import ead.utils.swing.SwingUtilities;

/**
 * <p>Split pane with a hiding left panel</p>
 * This works similarly to {@link JSplitPane} but the left side
 * panel will hide when the mouse leaves it and expand when the mouse
 * hovers over it.
 */
public class EAdHidingSplitPane extends JPanel {

	private static final long serialVersionUID = -4895653681865804475L;

	private Component leftPanel;

	private JPanel leftPanelContainer;

	private Component rightPanel;

	private boolean contracted = true;

	private Changer changer;

	private EAdHidingLayout layout;

	private static final int MIN_WIDTH = 42;

	private static final int MAX_WIDTH = 342;

	private static final int INCREMENT = 25;

	public EAdHidingSplitPane(Component anotherLeftPanel,
			Component anotherRightPanel) {
		this.leftPanel = anotherLeftPanel;
		this.rightPanel = anotherRightPanel;

		this.leftPanelContainer = new JPanel() {

			private static final long serialVersionUID = -8383046686527758671L;

			@Override
			public void doLayout() { }
		};

		this.leftPanelContainer.setLayout(new BorderLayout());
		this.leftPanelContainer.add(leftPanel, BorderLayout.CENTER);

		layout = new EAdHidingLayout();
		this.setLayout(layout);
		this.add(leftPanelContainer, EAdHidingLayout.LEFT_CONTAINER);
		this.add(rightPanel, EAdHidingLayout.RIGHT_PANEL);

		Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {

			@Override
			public void eventDispatched(AWTEvent event) {
				boolean oldContracted = contracted;
				if (leftPanelContainer.getMousePosition() != null ||
						leftPanel.getMousePosition() != null)
					contracted = false;
				if (rightPanel.getMousePosition() != null)
					contracted = true;
				if (oldContracted ^ contracted) {
					if (changer != null) {
						changer.stop();
						changer = null;
					}
					changer = new Changer(!contracted, layout);
					new Thread(changer).start();
				}
			}

		}, AWTEvent.MOUSE_MOTION_EVENT_MASK);
	}

	private class EAdHidingLayout implements LayoutManager2 {

		private Component rightPanel;

		private Component leftContainer;

		public static final String RIGHT_PANEL = "right";

		public static final String LEFT_CONTAINER = "left_container";

		private int width = MIN_WIDTH;

		@Override
		public void addLayoutComponent(String arg0, Component arg1) {
			if (arg0.equals(RIGHT_PANEL))
				this.rightPanel = arg1;
			else if (arg0.equals(LEFT_CONTAINER))
				this.leftContainer = arg1;
			else
				throw new RuntimeException("Can't add other components");
		}

		@Override
		public void layoutContainer(Container arg0) {
			Dimension d = arg0.getSize();
			arg0.setComponentZOrder(leftContainer, 0);

			leftContainer.setBounds(0, 0, width, d.height);
			rightPanel.setBounds(MIN_WIDTH, 0, d.width - MIN_WIDTH, d.height);
			leftPanel.setBounds(0, 0, MAX_WIDTH, d.height);
		}

		@Override
		public Dimension minimumLayoutSize(Container arg0) {
			return arg0.getPreferredSize();
		}

		@Override
		public Dimension preferredLayoutSize(Container arg0) {
			return arg0.getPreferredSize();
		}

		@Override
		public void removeLayoutComponent(Component arg0) {
		}

		@Override
		public void addLayoutComponent(Component arg0, Object arg1) {
			if (((String) arg1).equals(RIGHT_PANEL))
				this.rightPanel = arg0;
			else if (((String) arg1).equals(LEFT_CONTAINER))
				this.leftContainer = arg0;
			else
				throw new RuntimeException("Can't add other components");
		}

		@Override
		public float getLayoutAlignmentX(Container arg0) {
			return 0;
		}

		@Override
		public float getLayoutAlignmentY(Container arg0) {
			return 0;
		}

		@Override
		public void invalidateLayout(Container arg0) {
		}

		@Override
		public Dimension maximumLayoutSize(Container arg0) {
			return arg0.getMaximumSize();
		}

		public int getWidth() {
			return width;
		}

		public void setWidth(int width) {
			this.width = width;
		}
	}

	public class Changer implements Runnable {

		private boolean increment;

		private boolean stopped;

		private EAdHidingLayout layout;

		public Changer(boolean increment, EAdHidingLayout layout) {
			this.increment = increment;
			this.layout = layout;
			stopped = false;
		}

		@Override
		public void run() {
			while (((increment && layout.getWidth() < EAdHidingSplitPane.MAX_WIDTH)
					|| (!increment && layout.getWidth() > EAdHidingSplitPane.MIN_WIDTH)) && !stopped) {
				SwingUtilities.doInEDTNow(new Runnable() {

					@Override
					public void run() {
						if (increment)
							layout.setWidth(layout.getWidth() + INCREMENT);
						else
							layout.setWidth(layout.getWidth() - INCREMENT);
					}

				});

				try {
					Thread.sleep(50);
				} catch (Exception e) {
				}
				SwingUtilities.doInEDT(new Runnable() {

					@Override
					public void run() {
						revalidate();
					}

				});
			}
			stopped = true;
		}

		public void stop() {
			this.stopped = true;
		}

		public boolean isStopped() {
			return stopped;
		}
	}

}

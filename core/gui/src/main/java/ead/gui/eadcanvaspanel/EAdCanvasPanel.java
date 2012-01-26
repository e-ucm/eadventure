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

package ead.gui.eadcanvaspanel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.accessibility.AccessibleContext;
import javax.swing.JComponent;

import ead.gui.eadcanvaspanel.scrollcontainers.ScrollCanvasPanel;

/**
 * {@link EAdCanvasPanel} is a container for {@link JComponent}, where its
 * components are drawn in its absolute coordinates.
 * 
 * Two methods must be overridden in delegated classes to get an customized
 * panel:
 * <ul>
 * <li>paintBackground : specifies how background must be filled. By default,
 * it's filled in white color.</li>
 * <li>paintFocusBorder: specifies how focus border must be drawn. By default, a
 * blue border a few pixels from panel borders is drawn.</li>
 * </ul>
 * 
 * To convenient resizing, it's necessary to add this panel as ComponentListener
 * to its parent.
 * 
 * @author Ángel Serrano Laguna
 */
public class EAdCanvasPanel extends JComponent implements ComponentListener,
		MouseListener {

	private static final long serialVersionUID = 8263525432405192791L;

	// SIZE CONSTANTS

	/**
	 * Canvas' default dimension. Set to 800x600
	 */
	public final static Dimension DEFAULT_CANVAS_DIMENSION = new Dimension(800,
			600);

	// FIELDS

	/**
	 * Dimension for the canvas
	 */
	protected Dimension canvasDimension;

	/**
	 * If canvas resize itself to fit its parent
	 */
	protected boolean autosize;

	/**
	 * Scroll panel containing this canvas. Required for correct resize
	 */
	private ScrollCanvasPanel scrollPanel;

	/**
	 * Used for the first adjustment of canvas' zoom
	 */
	protected boolean firstPaint = true;

	/**
	 * Constructs a {@link EAdCanvasPanel} with autoresize, i.e., the canvas
	 * will fit its parent
	 * 
	 */
	public EAdCanvasPanel() {
		this(DEFAULT_CANVAS_DIMENSION, true);
	}

	/**
	 * Constructs a {@link EAdCanvasPanel}
	 * 
	 * @param autosize
	 *            if <b>true</b>, canvas will fit its parent. Otherwise, canvas'
	 *            size will always be the same, in this case this size is
	 *            defined in the {@link EAdCanvasPanel#DEFAULT_CANVAS_DIMENSION}
	 *            constant
	 */
	public EAdCanvasPanel(boolean autosize) {
		this(DEFAULT_CANVAS_DIMENSION, autosize);
	}

	/**
	 * Constructs a canvas with a fixed size
	 * 
	 * @param width
	 *            Width for the canvas
	 * @param height
	 *            Height for the canvas
	 */
	public EAdCanvasPanel(int width, int height) {
		this(new Dimension(width, height), false);
	}

	/**
	 * Private constructor
	 * 
	 * @param canvasDimension
	 *            Canvas dimension
	 * @param autosize
	 *            Autosize
	 */
	private EAdCanvasPanel(Dimension canvasDimension, boolean autosize) {

		this.canvasDimension = canvasDimension;
		setAutosize(autosize);
		setLayout(null);
		setPreferredSize(canvasDimension);
		setSize(canvasDimension);
		addDefaultFocusListeners();
		addMouseListener(this);
	}

	/**
	 * Adds default focus listeners
	 */
	private void addDefaultFocusListeners() {

		setFocusable(true);
		addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent e) {

				repaint();
			}

			public void focusLost(FocusEvent e) {

				repaint();
			}

		});
	}

	/**
	 * Returns canvas dimension
	 * 
	 * @return canvas dimension
	 */
	public Dimension getCanvasDimension() {
		return canvasDimension;
	}

	/**
	 * @return If canvas resize itself to fit its parent
	 */
	public boolean getAutosize() {
		return autosize;
	}

	/**
	 * Sets the {@link ScrollCanvasPanel} containing this canvas
	 * 
	 * @param scrollPanel
	 *            the container for this canvas
	 */
	public void setScrollCanvasPanel(ScrollCanvasPanel scrollPanel) {
		this.scrollPanel = scrollPanel;
	}

	/**
	 * Sets the autosize for this canvas. If <b>true</b>, this canvas will fit
	 * its parent's size. Otherwise, it won't be possible to resize it
	 * 
	 * @param autosize
	 *            the autosize for this canvas
	 */
	public void setAutosize(boolean autosize) {
		this.autosize = autosize;
		if (!autosize)
			setPreferredSize(canvasDimension);
	}

	@Override
	public void processKeyEvent(KeyEvent e) {
		super.processKeyEvent(e);
	}

	@Override
	public AccessibleContext getAccessibleContext() {

		AccessibleJComponent a = new AccessibleElementComponent();
		return a;
	}

	// --------------------//
	/* PAINT METHODS */
	// --------------------//

	@Override
	public void paint(Graphics g) {

		if (firstPaint) {
			firstPaint = false;
			updateContainerDimensions();
		}

		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		paintBackground(g2);

		if (isFocusOwner()) {
			paintFoucsBorder(g2);
		}

		// Paints children
		super.paint(g2);

	}

	/**
	 * Paints panel's background
	 * 
	 * @param g
	 *            Object graphic
	 */
	protected void paintBackground(Graphics g) {

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
	}

	/**
	 * Paints focus border
	 * 
	 * @param g
	 *            Object graphic
	 */
	protected void paintFoucsBorder(Graphics g) {

		g.setColor(Color.BLUE);
		g.drawRect(2, 2, getWidth() - 4, getHeight() - 4);
	}

	// ----------------- //
	/* SIZE */
	// ------------------//

	/**
	 * Updates container dimensions when zoom changed
	 */
	private void updateContainerDimensions() {

		if (autosize && this.getParent() != null) {
			updateBounds(getParent().getBounds());
		}

		JComponent parent = (JComponent) getParent();

		if (parent != null) {
			parent.setPreferredSize(canvasDimension);
			parent.setSize(canvasDimension);
			parent.revalidate();
		}

		int x = parent != null && parent.getWidth() > canvasDimension.width ? parent
				.getWidth()
				/ 2 - canvasDimension.width / 2
				: 0;
		int y = parent != null && parent.getHeight() > canvasDimension.height ? parent
				.getHeight()
				/ 2 - canvasDimension.height / 2
				: 0;
		setBounds(new Rectangle(new Point(x, y), canvasDimension));

		repaint();
	}

	/**
	 * Centers the panel relative to a given point
	 * 
	 * @param p
	 *            the point
	 */
	public void center(Point p) {
		JComponent parent = (JComponent) getParent();
		int x = parent != null ? parent.getWidth() / 2 - p.x : 0;
		int y = parent != null ? parent.getHeight() / 2 - p.y : 0;
		setLocation(x, y);
	}

	/**
	 * Centers the panel relative to parent center
	 */
	public void center() {
		center(new Point(getWidth() / 2, getHeight() / 2));
	}

	/**
	 * Updates bounds for the canvas. Used when canvas has autosize on
	 * 
	 * @param r
	 *            New bounds
	 */
	public void updateBounds(Rectangle r) {
		if (autosize) {
			int maxX = getX();
			int minX = getWidth();
			int maxY = getY();
			int minY = getHeight();

			for (Component c : this.getComponents()) {
				int newX = c.getX() + c.getWidth();
				int newY = c.getY() + c.getHeight();
				maxX = maxX < newX ? newX : maxX;
				maxY = maxY < newY ? newY : maxY;

				minX = c.getX() < minX ? c.getX() : minX;
				minY = c.getY() < minY ? c.getY() : minY;
			}

			canvasDimension.width = maxX;
			canvasDimension.height = maxY;

			setPreferredSize(canvasDimension);

			if (scrollPanel != null)
				scrollPanel.updateBounds(r);

		}

	}

	// --------------//
	/* ACESSIBILITY */
	// --------------//

	protected class AccessibleElementComponent extends AccessibleJComponent {

		private static final long serialVersionUID = 1L;

		@Override
		public String getAccessibleName() {

			return "Contenedor con " + getComponentCount() + " componentes";
		}

	}

	// -------------------//
	/* COMPONENT LISTENER */
	// -------------------//

	@Override
	public void componentResized(ComponentEvent e) {
		center();
	}

	@Override
	public void componentHidden(ComponentEvent e) {

	}

	@Override
	public void componentMoved(ComponentEvent e) {

	}

	// ----------------//
	/* MOUSE LISTENER */
	// ----------------//

	@Override
	public void componentShown(ComponentEvent e) {

	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		requestFocus();

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

}

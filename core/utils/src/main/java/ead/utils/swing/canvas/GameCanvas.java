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

package ead.utils.swing.canvas;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.LinkedList;

import javax.swing.JComponent;
import javax.swing.JPanel;

import ead.utils.i18n.I18N;
import ead.utils.swing.SwingUtilities;

public class GameCanvas extends JPanel {

	/**
	 * @see java.io.Serializable
	 */
	private static final long serialVersionUID = 1L;

	private Image bgImage;
	
	private int x, y;
	
	private ComponentListener componentListener;
	
	private PaintingStrategy paintingStrategy;
	
	private LinkedList<PaintingOperation> operations;

	public GameCanvas(){
		operations = new LinkedList<PaintingOperation>();
		componentListener = new GameCanvasComponentListener(this);
		addComponentListener(componentListener);
		paintingStrategy = DefaultPaintingStrategy.getInstance();
	}
	
	public void addPaintingOperation(PaintingOperation op){
		paintingStrategy.addOperation(operations, op);
	}

	public PaintingStrategy getPaintingStrategy() {
		return paintingStrategy;
	}

	public void setPaintingStrategy(PaintingStrategy paintingStrategy) {
		if(this.paintingStrategy == null){
			throw new NullPointerException(I18N.bind(Messages.argument_must_not_be_null, "paintingStrategy"));
		}
		this.paintingStrategy = paintingStrategy;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Image image = bgImage;
		if(image == null){
			Rectangle area = SwingUtilities.calculatePaintingArea(this);
			bgImage = this.createImage(area.width, area.height);
			Graphics offg = bgImage.getGraphics();
			g.setColor(getBackground());
			offg.fillRect(0, 0, area.width, area.height);
			doPaintOperations(offg);
			offg.dispose();
			x = area.x;
			y = area.y;
			image = bgImage;
		} else {
			Graphics offg = image.getGraphics();
			doPaintOperations(offg);
			offg.dispose();
		}
		g.drawImage(image, x, y, this);
	}

	protected void doPaintOperations(Graphics g){
		paintingStrategy.executeOperations(operations, g);
	}
	
	private class GameCanvasComponentListener extends ComponentAdapter implements ComponentListener {
		private Component c;
		
		public GameCanvasComponentListener(Component component){
			this.c = component;
		}
		
		@Override
		public void componentResized(ComponentEvent e) {
			Rectangle d = SwingUtilities.calculatePaintingArea((JComponent)e.getComponent());
			Image image = bgImage;
			if (image != null
					&& (image.getWidth(c) != d.width || image.getHeight(c) != d.height)) {
				bgImage = null;
			}
		}
	}
}
